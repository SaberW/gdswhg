package com.creatoo.hn.services.admin.activity;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.ext.emun.EnumBizState;
import com.creatoo.hn.ext.emun.EnumDelState;
import com.creatoo.hn.ext.emun.EnumState;
import com.creatoo.hn.mapper.*;
import com.creatoo.hn.mapper.admin.CrtWhgActivityMapper;
import com.creatoo.hn.model.*;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.utils.ReqParamsUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@SuppressWarnings("all")
@Service
/**
 * 活动管理 服务层
 * @author heyi
 *
 */
public class WhgActivityActService {
	
	/**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 公共服务
     */
    @Autowired
    private CommService commService;

    /**
     * 活动DAO
     */
    @Autowired
    private WhgActActivityMapper whgActActivityMapper;

    @Autowired
    private CrtWhgActivityMapper crtWhgActivityMapper;

    @Autowired
    private WhgActTimeMapper whgActTimeMapper;

    @Autowired
    private WhgActOrderMapper whgActOrderMapper;

    @Autowired
    private WhgActTicketMapper whgActTicketMapper;

    @Autowired
    private WhgActSeatMapper whgActSeatMapper;

    @Autowired
    private WhgActivitySeatService whgActivitySeatService;
    
    /**
     * 分页查询活动信息
     * @param page
     * @param rows
     * @param request
     * @return
     * @throws Exception
     */
    public PageInfo srchList4p(int page, int rows, Map param) throws Exception{
        PageHelper.startPage(page, rows);
        List list = this.crtWhgActivityMapper.srchListActivity(param);
        return new PageInfo<Object>(list);
    }
    
    public PageInfo getActivityScreenings(int page,int rows,WhgActTime whgActTime) throws  Exception{
        PageHelper.startPage(page,rows);
        List list = whgActTimeMapper.select(whgActTime);
        return new PageInfo<Object>(list);
    }

    /**
     * 分页查询活动信息
     * @param request 请求对象
     * @param act 条件对象
     * @return 分页数据
     * @throws Exception
     */
    public PageInfo<WhgActActivity> t_srchList4p(HttpServletRequest request, WhgActActivity act)throws Exception{
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);

        //分页信息
        int page = Integer.parseInt((String)paramMap.get("page"));
        int rows = Integer.parseInt((String)paramMap.get("rows"));

        //搜索条件
        Example example = new Example(WhgActActivity.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(act != null && act.getName() != null){
            c.andLike("name", "%"+act.getName()+"%");
            act.setName(null);
        }

        //其它条件
        c.andEqualTo(act);

        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgActActivity> list = this.whgActActivityMapper.selectByExample(example);
        return new PageInfo<WhgActActivity>(list);
    }

    /**
     * 查询活动列表
     * @param request 请求对象
     * @param act 条件对象
     * @return 活动列表
     * @throws Exception
     */
    public List<WhgActActivity> t_srchList(HttpServletRequest request, WhgActActivity act)throws Exception{
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);

        //搜索条件
        Example example = new Example(WhgActActivity.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if(act != null && act.getName() != null){
            c.andLike("name", "%"+act.getName()+"%");
            act.setName(null);
        }

        //其它条件
        c.andEqualTo(act);

        //排序
        if(paramMap.containsKey("sort") && paramMap.get("sort")!= null){
            StringBuffer sb = new StringBuffer((String)paramMap.get("sort"));
            if(paramMap.containsKey("order") && paramMap.get("order") !=  null){
                sb.append(" ").append(paramMap.get("order"));
            }
            example.setOrderByClause(sb.toString());
        }else{
            example.setOrderByClause("crtdate desc");
        }

        //分页查询
        return this.whgActActivityMapper.selectByExample(example);
    }

    /**
     * 查询单个活动信息
     * @param id 活动主键
     * @return 活动对象
     * @throws Exception
     */
    public WhgActActivity t_srchOne(String id)throws Exception{
//        WhgActActivity record = new WhgActActivity();
//        record.setId(id);
        return this.whgActActivityMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加活动
     * @param act
     * @return
     * @throws Exception
     */
    public WhgActActivity t_add(WhgActActivity act, WhgSysUser user)throws Exception{
        //名称不能重复
        Example example = new Example(WhgActActivity.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", act.getName());
        int count = this.whgActActivityMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("活动名称重复");
        }

        //设置初始值
        Date now = new Date();
        act.setId(commService.getKey("whg_sys_act"));
        act.setState(EnumState.STATE_YES.getValue());
        act.setCrtdate(now);
        act.setCrtuser(user.getId());
        act.setUpindex(0);  //上首页
        //act.setCultid(user.getCultid());    //文化馆
        act.setDeptid(user.getDeptid());    //部门
        act.setDelstate(EnumDelState.STATE_DEL_NO.getValue());
        act.setStatemdfdate(now);
        act.setStatemdfuser(user.getId());
        int rows = this.whgActActivityMapper.insertSelective(act);
        if(rows != 1){
            throw new Exception("添加活动失败");
        }

        return act;
    }

    /**
     *  编辑活动基本资料
     * @param act 活动信息
     * @param user 操作员
     * @param seatJson 座位字符串
     * @param updateSeat true-需要更新活动的座位信息 false-不需要
     * @return 活动信息
     * @throws Exception
     */
    public WhgActActivity t_edit(WhgActActivity act, WhgSysUser user, String seatJson, boolean updateSeat)throws Exception{
        //名称不能重复
        Example example = new Example(WhgActActivity.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("name", act.getName());
        c.andNotEqualTo("id", act.getId());
        int count = this.whgActActivityMapper.selectCountByExample(example);
        if(count > 0){
            throw new Exception("活动名称重复");
        }

        //如果是从在线选座修改为自由选座, 修改whg_act_time.seats字段-预订总数
        WhgActActivity oldAct = null;
        if( "2".equals(act.getSellticket()+"")) {
            //修改之前对象
            oldAct = this.whgActActivityMapper.selectByPrimaryKey(act.getId());

            //自由选座时是否修改了每场座位数
            boolean isModify = false;//
            if(oldAct.getTicketnum() != null && act.getTicketnum() != null){
                if(oldAct.getTicketnum().intValue() != act.getTicketnum().intValue()){
                    isModify = true;
                }
            }

            //自由选座时修改了每场座位数 或者 从在线选座修改为自由选座 时，重置 场次里面的总座位数
            if( isModify || !"2".equals(oldAct.getSellticket()+"")) {//从在线选座修改为自由选座
                WhgActTime whgActTime = new WhgActTime();
                whgActTime.setSeats(act.getTicketnum());

                Example example_t = new Example(WhgActTime.class);
                example_t.createCriteria().andEqualTo("actid", act.getId());
                this.whgActTimeMapper.updateByExampleSelective(whgActTime, example_t);
            }
        }

        //在线选座时，修改时座位信息的处理: 如果场馆发生变化，删除所有座位信息，重新生成, 如果没有发生变化，则修改座位信息的状态
        if( updateSeat && "3".equals(act.getSellticket()+"")) {
            //查询修改之前的活动信息
            if(oldAct == null){
                oldAct = this.whgActActivityMapper.selectByPrimaryKey(act.getId());
            }

            if(oldAct != null){
                String roomId_old = oldAct.getRoomid();
                String roomId_new = act.getRoomid();
                int validSeatNum = 0; //修改后的有效总票数
                if(roomId_old != null && roomId_new != null && roomId_new.equals(roomId_old)){//活动室没有变化,不修改座位的ID,只修改座位的状态
                    List<Map<String, String>> seats = whgActivitySeatService.setSeatList(seatJson);
                    validSeatNum = whgActivitySeatService.t_edit(seats, user, act.getId());
                }else if(roomId_new == null && EnumBizState.STATE_NO_PUB.getValue() == oldAct.getState()){//下架后编辑时活动室没有变化,不修改座位的ID,只修改座位的状态
                    List<Map<String, String>> seats = whgActivitySeatService.setSeatList(seatJson);
                    validSeatNum = whgActivitySeatService.t_edit(seats, user, act.getId());
                }else if(roomId_new != null){//活动室发生了变化
                    //删除座位信息
                    Example example_seat = new Example(WhgActSeat.class);
                    example_seat.createCriteria().andEqualTo("activityid", act.getId());
                    whgActSeatMapper.deleteByExample(example_seat);

                    //修改座位
                    List<Map<String, String>> seats = whgActivitySeatService.setSeatList(seatJson);
                    validSeatNum = whgActivitySeatService.t_add(seats, user, act.getId());
                }

                //更新场次表中的总票数
                WhgActTime whgActTime_record = new WhgActTime();
                whgActTime_record.setSeats(validSeatNum);
                Example example_acttime = new Example(WhgActTime.class);
                example_acttime.createCriteria().andEqualTo("actid", act.getId());
                this.whgActTimeMapper.updateByExampleSelective(whgActTime_record, example_acttime);
            }
        }

        //设置初始值
        Date now = new Date();
        act.setStatemdfdate(now);
        act.setStatemdfuser(user.getId());
        int rows = this.whgActActivityMapper.updateByPrimaryKeySelective(act);
        if(rows != 1){
            throw new Exception("编辑活动信息失败");
        }

        return act;
    }

    /**
     * 删除活动
     * @param ids 活动ID
     * @throws Exception
     */
    public void t_del(String ids, WhgSysUser user)throws Exception{
        if(ids != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgActActivity.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.whgActActivityMapper.deleteByExample(example);
        }
    }

    /**
     * 更新活动状态
     * @param statemdfdate 状态修改时间
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updstate(String statemdfdate, String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(ids != null && toState != null){
            String[] idArr = ids.split(",");
            Example example = new Example(WhgActActivity.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            if(fromState != null){
                c.andEqualTo("state", fromState);
            }
            WhgActActivity record = new WhgActActivity();
            record.setState(Integer.parseInt(toState));
            Date now = new Date();
            //设置状态修改时间
            if(!"".equals(statemdfdate) && statemdfdate != null){
                record.setStatemdfdate(sdf.parse(statemdfdate));
            }else{
                record.setStatemdfdate(new Date());
            }
            //record.setStatemdfdate(now);

            record.setStatemdfuser(user.getId());
            this.whgActActivityMapper.updateByExampleSelective(record, example);
        }
    }
    
    /**
     * 更新推荐状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updCommend(String ids, String fromState, String toState, WhgSysUser user)throws Exception{
        if(ids != null && toState != null){
            WhgActActivity record = whgActActivityMapper.selectByPrimaryKey(ids);
        	record.setIsrecommend(Integer.parseInt(toState));
            Date now = new Date();
            record.setStatemdfdate(now);
            record.setStatemdfuser(user.getId());
            this.whgActActivityMapper.updateByPrimaryKey(record);
        }
    }
    
    /**
     * 数据还原状态
     * @param ids 活动ID，多个用逗号分隔
     * @param fromState 改状态之前的限制状态
     * @param toState 修改后的状态
     * @throws Exception
     */
    public void t_updDelstate(String ids,  String delState, WhgSysUser user)throws Exception{
        if(ids != null && delState != null){
            WhgActActivity record = whgActActivityMapper.selectByPrimaryKey(ids);
            if(record.getDelstate() == 1){
             	String[] idArr = ids.split(",");
                 Example example = new Example(WhgActActivity.class);
                 Example.Criteria c = example.createCriteria();
                 c.andIn("id", Arrays.asList(idArr));
                 this.whgActActivityMapper.deleteByExample(example);
             }else{
            	 record.setDelstate(Integer.parseInt(delState));
                 Date now = new Date();
                 record.setStatemdfdate(now);
                 record.setStatemdfuser(user.getId());
                 this.whgActActivityMapper.updateByPrimaryKey(record);
             }
            
        }
    }
    
    public void reBack(String ids,String delState, WhgSysUser user){
    	if(ids != null && delState != null){
    		 WhgActActivity record = whgActActivityMapper.selectByPrimaryKey(ids);
    		 record.setDelstate(Integer.parseInt(delState));
    		 Date now = new Date();
             record.setStatemdfdate(now);
             record.setStatemdfuser(user.getId());
             this.whgActActivityMapper.updateByPrimaryKey(record);
    	}
    }

    /**
     * 查询所有活动订单，以时间降序排列
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    public PageInfo getActOrderForBackManager(int page,int rows, Map<String,Object> params) throws Exception{
        PageHelper.startPage(page, rows);
        List list = crtWhgActivityMapper.getActOrderForBackManager(params);
        return new PageInfo<Object>(list);
    }

    public Map<String, Object> getOrderInfo(String orderid) throws Exception{
        Map<String, Object> rest = new HashMap<String, Object>();
        if (orderid==null || orderid.isEmpty()){
            return rest;
        }

        //订单
        WhgActOrder order = this.whgActOrderMapper.selectByPrimaryKey(orderid);
        rest.put("order", order);
        //活动
        if (order!=null && order.getActivityid()!=null){
            WhgActActivity act = this.whgActActivityMapper.selectByPrimaryKey(order.getActivityid());
            rest.put("act", act);
        }
        //场次
        if (order!=null && order.getEventid()!=null){
            WhgActTime time = this.whgActTimeMapper.selectByPrimaryKey(order.getEventid());
            rest.put("time", time);
        }
        //票
        if (order!=null){
            WhgActTicket ticket = new WhgActTicket();
            ticket.setOrderid(order.getId());
            List<WhgActTicket> ticketList = this.whgActTicketMapper.select(ticket);
            rest.put("ticketList", ticketList);
        }

        return rest;
    }

    /**
     * 上首页
     * @param ids
     * @param formupindex
     * @param toupindex
     * @return
     */
    public ResponseBean t_upindex(String ids, String formupindex, int toupindex) {
        ResponseBean res = new ResponseBean();
        if(ids == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("活动主键丢失");
            return res;
        }
        Example example = new Example(WhgActActivity.class);
        Example.Criteria c = example.createCriteria();
        c.andIn("id", Arrays.asList( ids.split("\\s*,\\s*") ));
        c.andIn("upindex", Arrays.asList( formupindex.split("\\s*,\\s*") ));
        WhgActActivity act = new WhgActActivity();
        act.setUpindex(toupindex);
        this.whgActActivityMapper.updateByExampleSelective(act,example);
        return res;
    }

    /**
     *
     * @param request
     * @return
     */
    public List<WhgActOrderExcel> serch(HttpServletRequest request) throws Exception {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd");
        String id = request.getParameter("activityId");
        WhgActActivity act = whgActActivityMapper.selectByPrimaryKey(id);
        String actname = act.getName();

        Map<String, Object> params = new HashMap<>();
        String activityId = request.getParameter("activityId");
        if(activityId != null && !"".equals(activityId)){
            params.put("activityId",activityId);
        }
        if( request.getParameter("ordernumber") != null && !"".equals( request.getParameter("ordernumber"))){
            params.put("ordernumber",request.getParameter("ordernumber"));
        }if( request.getParameter("orderphoneno") != null && !"".equals( request.getParameter("orderphoneno"))){
            params.put("orderphoneno",request.getParameter("orderphoneno"));
        }if( request.getParameter("starttime") != null && !"".equals( request.getParameter("starttime"))){
            params.put("starttime",request.getParameter("starttime"));
        }
        if( request.getParameter("endtime") != null && !"".equals( request.getParameter("endtime"))){
            params.put("endtime",request.getParameter("endtime"));
        }
        List<Map> list = crtWhgActivityMapper.getActOrderForBackManager(params);
        String orderid;
        int state;
        String username;
        String orderphoneno;
        String ordercreatetime;
        String starttime;
        String endtime;
        String playstime;
        String playetime;
        int ordersmsstate;
        List<WhgActOrderExcel> taoist = new ArrayList<>();
        if(list.size() > 0){
            for(int i = 0; i<list.size(); i++){
                orderid = (String)list.get(i).get("ordernumber");
                state = Integer.parseInt(String.valueOf(list.get(i).get("ticketstatus")));
                username = (String)list.get(i).get("ordername");
                orderphoneno = (String)list.get(i).get("orderphoneno");
                ordercreatetime = sdf1.format((Date) list.get(i).get("playdate"));
                starttime = sdf1.format((Date)list.get(i).get("starttime"));
                endtime = sdf1.format((Date) list.get(i).get("endtime"));
                playstime = (String)list.get(i).get("playstime");
                playetime = (String)list.get(i).get("playetime");
                ordersmsstate = Integer.parseInt(String.valueOf(list.get(i).get("ordersmsstate")));

                WhgActOrderExcel orderex = new WhgActOrderExcel();
                orderex.setId(id);
                orderex.setActname(actname);
                orderex.setOrderid(orderid);
                orderex.setOrdercreatetime(ordercreatetime);
                orderex.setOrderphoneno(orderphoneno);
                orderex.setUsername(username);
                orderex.setState(state);
                orderex.setStarttime(starttime);
                orderex.setEndtime(endtime);
                orderex.setPlaystime(playstime);
                orderex.setPlayetime(playetime);
                orderex.setOrdersmsstate(ordersmsstate);

                taoist.add(orderex);
            }
        }
        return taoist;
    }

}
