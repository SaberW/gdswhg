package com.creatoo.hn.services.admin.train;

import com.creatoo.hn.dao.mapper.WhgDrscMapper;
import com.creatoo.hn.dao.mapper.WhgMajorContactMapper;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.admin.xj.WhgXjReasonService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.UploadUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;

/**
 * 数字资源服务类
 * @author wangxl
 * @version 20161108
 */
@SuppressWarnings("ALL")
@Service
public class WhgDrscService {
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	public WhgDrscMapper drscMapper;

	@Autowired
	private WhgMajorContactMapper whgMajorContactMapper;

	@Autowired
	private WhgSystemUserService whgSystemUserService;

	@Autowired
	private WhgSysCultMapper whgSysCultMapper;

    @Autowired
    private WhgXjReasonService whgXjReasonService;
	
	/**
	 * 批量修改资源状态
	 * @param ids 资源标识，多个用逗号分隔
	 * @param fromState 修改之前的状态
	 * @param toState 修改之后的状态
	 */
	public void updState(String ids, String fromState, int toState, String userid, String reason, int issms)throws Exception{
		if(ids != null){
			String[] idArr = ids.split(",");
			Date now = new Date();
			for(String id : idArr){
				if(id != null){
					Example example = new Example(WhgDrsc.class);
					example.createCriteria().andEqualTo("drscid", id).andIn("drscstate", Arrays.asList(fromState.split(",")));

					WhgDrsc record = new WhgDrsc();
					record.setDrscstate(toState);
					if(EnumBizState.STATE_CAN_PUB.getValue() == toState) {//提交审核
						record.setCheckdate(now);
						record.setCheckor(userid);
					}else if(EnumBizState.STATE_CAN_EDIT.getValue() == toState && (EnumBizState.STATE_CAN_CHECK.getValue()+"").equals(fromState)){
						//审核者-不通过
						record.setCheckdate(now);
						record.setCheckor(userid);
					}else if(EnumBizState.STATE_PUB.getValue() == toState) {//发布
						record.setPublisher(userid);
						record.setPublishdate(now);
					}
					record.setDrscopttime(now);

                    try {
                        if (reason!=null && !reason.isEmpty() && toState == EnumBizState.STATE_NO_PUB.getValue()){
                            List<WhgDrsc> srclist = this.drscMapper.selectByExample(example);
                            if (srclist!=null){
                                for (WhgDrsc _src : srclist){
                                    WhgXjReason xjr = new WhgXjReason();
                                    xjr.setFkid(_src.getDrscid());
                                    xjr.setFktype("培训资源");
                                    xjr.setFktitile(_src.getDrsctitle());
                                    xjr.setCrtuser(userid);
                                    xjr.setCrtdate(new Date());
                                    xjr.setReason(reason);
                                    xjr.setTouser(_src.getPublisher());
                                    xjr.setIssms(issms);

                                    this.whgXjReasonService.t_add(xjr);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(),e);
                    }

					this.drscMapper.updateByExampleSelective(record, example);
				}
			}
		}
	}

    public void updState(String ids, String fromState, int toState, String userid)throws Exception{
        this.updState(ids, fromState, toState, userid, null, 0);
    }
	
	/**
	 * 添加数字资源
	 * @param drsc
	 * @throws Exception
	 */
	public void t_add(WhgDrsc drsc,String mid,WhgSysUser user)throws Exception{
		drsc.setDrscid(IDUtils.getID());
		drsc.setDrsccrttime(new Date());
		drsc.setDrscopttime(new Date());
		drsc.setCrtuser(user.getId());
		drsc.setIsrecommend(0);
		drsc.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
		drsc.setDrscstate(EnumBizState.STATE_CAN_EDIT.getValue());
		this.drscMapper.insert(drsc);
		if(mid != null && !"".equals(mid)){
			WhgMajorContact contact = new WhgMajorContact();
			contact.setId(IDUtils.getID());
			contact.setEntid(drsc.getDrscid());
			contact.setMajorid(mid);
			contact.setType(3);
			this.whgMajorContactMapper.insertSelective(contact);
		}
	}
	
	/**编辑数字资源
	 * @param drsc
	 * @throws Exception
	 */
	public void t_edit(WhgDrsc drsc)throws Exception{
		/*Example example = new Example(WhDrsc.class);
		example.createCriteria().andEqualTo("drscid", drsc.getDrscid()).andIn("drscstate", new ArrayList<Integer>(){
			private static final long serialVersionUID = 1L;
			{
			add(0);
			add(1);
			}
		});
		this.drscMapper.updateByExampleSelective(drsc, example);*/
		this.drscMapper.updateByPrimaryKeySelective(drsc);
	}
	
	/**
	 * 删除数字资源
	 * @throws Exception
	 */
	public void delete(String id, String uploadPath)throws Exception{
		//获取图片和资源
		WhgDrsc record = new WhgDrsc();
		record.setDrscid(id);
		record = this.drscMapper.selectOne(record);
		String derscpic = record.getDrscpic();
		String drscpath = record.getEnturl();
		
		//删除数字资源
		Example example = new Example(WhgDrsc.class);
		example.createCriteria().andEqualTo("drscid", id).andIn("drscstate", new ArrayList<Integer>(){
			private static final long serialVersionUID = 1L;
			{
			add(0);
			add(1);
			}
		});
		int cnt = this.drscMapper.deleteByExample(example);
		if(cnt > 0){
			//删除图片
			if(record.getEnturl() != null){
				UploadUtil.delUploadFile(uploadPath, drscpath);
			}
			//删除资源 
			if(derscpic != null){
				UploadUtil.delUploadFile(uploadPath, derscpic);
			}
		}
	}

	/**
	 * 修改回收状态
	 * @param id
	 * @throws Exception
	 */
	public void t_recycle(String id, int delstate)throws Exception{
		WhgDrsc drsc = new WhgDrsc();
		drsc.setDrscid(id);
		drsc.setDrscstate(delstate);
		drsc.setDelstate(delstate);
		this.drscMapper.updateByPrimaryKeySelective(drsc);
	}
	
	/**
	 * 分页查询数字资源
	 * @return 分页内容
	 * @throws Exception
	 */
	public Map<String, Object> srchPagging(int page,int rows, String sort, String order,WhgDrsc drsc,String sysUserId, String defaultState)throws Exception{

		Example example = new Example(WhgDrsc.class);
		Criteria criteria = example.createCriteria();

		//状态
		if(StringUtils.isNotEmpty(defaultState) && drsc.getDrscstate() == null){
			drsc.setDrscstate(null);
			criteria.andIn("drscstate", Arrays.asList(defaultState.split(",")));
		}

		//资源名称
		if (drsc.getDrsctitle()!=null && !drsc.getDrsctitle().isEmpty()){
			criteria.andLike("drsctitle", "%"+drsc.getDrsctitle()+"%");
			drsc.setDrsctitle(null);
		}

		//文化馆和部门
		if (drsc.getDrscvenueid() == null || drsc.getDrscvenueid().isEmpty()) {
			drsc.setDrscvenueid(null);
			try {
				List<String> cultids = this.whgSystemUserService.getAllCultId4PMS(sysUserId);
				if (cultids!=null && cultids.size()>0){
					criteria.andIn("drscvenueid", cultids);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		if (drsc.getDeptid() == null || drsc.getDeptid().isEmpty()) {
			drsc.setDeptid(null);
			try {
				List<String> deptids = this.whgSystemUserService.getAllDeptId4PMS(sysUserId);
				if (deptids != null && deptids.size() > 0) {
					criteria.andIn("deptid", deptids);
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
		criteria.andEqualTo(drsc);

		//排序
		if (sort!=null && !sort.isEmpty()){
			Example.OrderBy orderBy = example.orderBy(sort);
			if (order!=null && "desc".equalsIgnoreCase(order)){
				orderBy.desc();
			}
		}else{
			example.orderBy("drsccrttime").desc();
		}

		//带条件的分页查询
		PageHelper.startPage(page, rows);
		List<WhgDrsc> list = this.drscMapper.selectByExample(example);
		
		// 取分页信息
        PageInfo<WhgDrsc> pageInfo = new PageInfo<WhgDrsc>(list);
       
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
		return rtnMap;
	};

	/**
	 * 根据主键查询在线点播
	 * @param id
	 * @return
     */
	public Object srchOne(String id) {
		return drscMapper.selectByPrimaryKey(id);
	}

	/**
	 * 查询已发布的资源列表
	 * @param page
	 * @param rows
     * @return
     */
	public PageInfo t_srchDrscList(int page, int rows,String mid,String cultid) {
		PageHelper.startPage(page,rows);
		List list = drscMapper.selDrscList(mid,cultid);
		return new PageInfo(list);
	}

	/**
	 * 是否推荐
	 * @param ids
	 * @param formrecoms
	 * @param torecom
	 * @return
	 */
	public ResponseBean t_updrecommend(String ids, String formrecoms, int torecom) throws Exception {
		ResponseBean res = new ResponseBean();
		if(ids == null){
			res.setSuccess(ResponseBean.FAIL);
			res.setErrormsg("主键丢失");
			return res;
		}
		Example example = new Example(WhgDrsc.class);
		Example.Criteria c1 = example.createCriteria();
		c1.andIn("drscid", Arrays.asList( ids.split("\\s*,\\s*") ));
		c1.andIn("isrecommend", Arrays.asList( formrecoms.split("\\s*,\\s*") ));
		WhgDrsc drsc = new WhgDrsc();
		drsc.setIsrecommend(torecom);
		this.drscMapper.updateByExampleSelective(drsc,example);
		return res;
	}

	/**
	 * 分页查询总分馆培训资源
	 * @param page
	 * @param rows
	 * @param drsc
	 * @param sort
	 * @param order
     * @param param
     * @return
     */
	public PageInfo t_srchSysList4p(int page, int rows, WhgDrsc drsc, String sort, String order, Map<String, String> param) throws Exception{
		Example example = new Example(WhgDrsc.class);
		Example.Criteria c = example.createCriteria();
		if (drsc!=null){
			//标题处理
			if (drsc.getDrsctitle()!=null){
				c.andLike("drsctitle", "%"+drsc.getDrsctitle()+"%");
				drsc.setDrsctitle(null); //去除title等于条件
			}

			c.andEqualTo(drsc);
		}

		c.andIn("drscstate", Arrays.asList(6,4));
		c.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

		String iscult = param.get("iscult");
		if (iscult!=null && "1".equals(iscult)){
			String cultid = param.get("syscultid");
			if (cultid == null){
				throw new Exception("文化馆ID丢失");
			}

			c.andEqualTo("drscvenueid", cultid);
		}else{
			String level = param.get("level");
			String cultname = param.get("cultname");
			if (level==null || level.isEmpty()){
				throw new Exception("文化馆级别丢失");
			}
			if (cultname==null || cultname.isEmpty()){
				throw new Exception("cultname丢失");
			}

			Example syscultemp = new Example(WhgSysCult.class);
			Example.Criteria sc = syscultemp.createCriteria();
			sc.andEqualTo("state", EnumBizState.STATE_PUB.getValue());
			sc.andEqualTo("delstate", EnumStateDel.STATE_DEL_NO.getValue());

			String areakey = "province";
			if ("2".equals(level)){
				areakey = "city";
			}else if ("3".equals(level)){
				areakey = "area";
			}
			sc.andEqualTo(areakey, cultname);

			List<String> refcultids = new ArrayList();
			List<WhgSysCult> refcults = this.whgSysCultMapper.selectByExample(syscultemp);
			if (refcults== null || refcults.size()==0){
				throw new Exception("没有找到文化馆");
			}
			for(WhgSysCult cult : refcults){
				refcultids.add(cult.getId());
			}
			c.andIn("drscvenueid", refcultids);
		}

		//排序
		if (sort!=null && !sort.isEmpty()){
			if (order!=null && "asc".equalsIgnoreCase(order)){
				example.orderBy(sort).asc();
			}else{
				example.orderBy(sort).desc();
			}
		}else{
			example.orderBy("crtdate").desc();
		}

		PageHelper.startPage(page, rows);
		List<WhgDrsc> list= this.drscMapper.selectByExample(example);
		PageInfo pageInfo = new PageInfo(list);
		List restList = new ArrayList();
		if (list!=null){
			BeanMap bm = new BeanMap();
			for(WhgDrsc _drsc : list){
				bm.setBean(_drsc);
				Map info = new HashMap();
				info.putAll(bm);
				if (_drsc.getDrscvenueid()!=null ){
					WhgSysCult sysCult = this.whgSysCultMapper.selectByPrimaryKey(_drsc.getDrscvenueid());
					if (sysCult!=null){
						info.put("cultname", sysCult.getName());
					}
				}
				restList.add(info);
			}
		}

		pageInfo.setList(restList);
		return pageInfo;
	}
}
