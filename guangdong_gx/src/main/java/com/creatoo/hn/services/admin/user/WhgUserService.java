package com.creatoo.hn.services.admin.user;

import com.creatoo.hn.dao.mapper.WhgUserMapper;
import com.creatoo.hn.dao.mapper.WhgUserProductMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.dao.model.WhgUserProduct;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人用户中心业务类
 *
 * @author dzl
 */
@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgUser", keyGenerator = "simpleKeyGenerator")
public class WhgUserService extends BaseService {
    @Autowired
    private WhgUserMapper userMapper;

    @Autowired
    private WhgUserProductMapper whgUserProductMapper;

/*    @Autowired
    public WhUserTroupeuserMapper troupeUser;
    
    @Autowired
    public WhgTraMapper whTrainMapper;*/

    /**
     * 分页查询用户信息
     *
     * @return
     * @throws Exception
     */
    //@Cacheable
    public PageInfo<WhgUser> t_srchList4p(int page, int rows, WhgUser whuser, String sort, String order) throws Exception {

        //搜索条件
        Example example = new Example(WhgUser.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (whuser != null && whuser.getName() != null) {
            c.andLike("name", "%" + whuser.getName() + "%");
            whuser.setName(null);
        }
        if (whuser != null && whuser.getNickname() != null) {
            c.andLike("nickname", "%" + whuser.getNickname() + "%");
            whuser.setNickname(null);
        }
        if (whuser != null && whuser.getPhone() != null) {
            c.andLike("phone", "%" + whuser.getPhone() + "%");
            whuser.setPhone(null);
        }
        if (whuser != null && whuser.getEmail() != null) {
            c.andLike("email", "%" + whuser.getEmail() + "%");
            whuser.setEmail(null);
        }

        //其它条件
        c.andEqualTo(whuser);

        //排序
        if (sort!=null && !sort.isEmpty()) {
            StringBuffer sb = new StringBuffer(sort);
            if (order!=null && !order.isEmpty()) {
                sb.append(" ").append(order);
            }
            example.setOrderByClause(sb.toString());
        } else {
            example.setOrderByClause("id desc");
        }

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgUser> list = this.userMapper.selectByExample(example);
        return new PageInfo<WhgUser>(list);
    }

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    //@Cacheable
    public WhgUser t_srchOne(String id) throws Exception {
        WhgUser record = new WhgUser();
        record.setId(id);
        return this.userMapper.selectOne(record);
    }


    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgUser t_srchByNumber(String usernumber) throws Exception {
        Example example = new Example(WhgUser.class);
        Example.Criteria c = example.createCriteria();
        if (usernumber != null) {
            c.andEqualTo("idcard", usernumber);
        }
        List<WhgUser> list = this.userMapper.selectByExample(example);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 查询用户信息
     *
     * @param id 用户ID
     * @return
     * @throws Exception
     */
    @Cacheable
    public WhgUser t_srchOneByPhone(String phone) throws Exception {
        WhgUser record = new WhgUser();
        record.setPhone(phone);
        return this.userMapper.selectOne(record);
    }

    /**
     * 取用户ID 的授权产品列表
     * @param userid
     * @return
     * @throws Exception
     */
    //@Cacheable
    public List<WhgUserProduct> srchUserProduct(String userid) throws Exception{
        WhgUserProduct up = new WhgUserProduct();
        up.setUserid(userid);
        return this.whgUserProductMapper.select(up);
    }

    /**
     * 编辑文化馆
     *
     * @param whuser
     * @return
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public WhgUser t_edit(WhgUser whuser, WhgSysUser sysUser) throws Exception {
        int rows = this.userMapper.updateByPrimaryKeySelective(whuser);
        if (rows != 1) {
            throw new Exception("编辑会员信息失败");
        }
        return whuser;
    }

    /**
     * 删除会员
     *
     * @param ids 文化馆ID
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public void t_del(String ids, WhgSysUser sysUser) throws Exception {
        if (ids != null) {
            String[] idArr = ids.split(",");
            Example example = new Example(WhgUser.class);
            Example.Criteria c = example.createCriteria();
            c.andIn("id", Arrays.asList(idArr));
            this.userMapper.deleteByExample(example);
        }
    }

    /**
     * 显示用户列表
     *
     * @return
     */
    @Cacheable
    public List<WhgUser> getList() {

        return this.userMapper.selectAll();
    }

    /**
     * 添加用户信息
     *
     * @throws Exception
     */
    @CacheEvict(allEntries = true)
    public Object addUser(WhgUser whuser) {
        try {
            whuser.setId(IDUtils.getID());
            this.userMapper.insert(whuser);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 根据id获得用户信息
     *
     * @param id
     * @return
     */
    @Cacheable
    public Object getUserId(Object id) {
        return this.userMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除用户信息
     */
    @CacheEvict(allEntries = true)
    public int removeUser(String id) {
        return this.userMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改用户信息
     */
    @CacheEvict(allEntries = true)
    public Object modifyUser(WhgUser whuser) {
        return this.userMapper.updateByPrimaryKeySelective(whuser);
    }

    /**
     * 根据条件查询用户
     */
    @Cacheable
    public Object findPage(int page, int rows) throws Exception {
        // 带条件的分页查询
        Example example = new Example(WhgUser.class);
        PageHelper.startPage(page, rows);
        List<WhgUser> list = this.userMapper.selectByExample(example);

        // 取分页信息
        PageInfo<WhgUser> pageInfo = new PageInfo<WhgUser>(list);

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
    }

    /**
     * 工具栏加载 数据
     *
     * @param page
     * @param rows
     * @param request
     * @return
     */
    @Cacheable
    public Object loadUser(int page, int rows, WebRequest request) throws Exception {
        Map<String, Object> param = ReqParamsUtil.parseRequest(request);
        PageHelper.startPage(page, rows);

        Example example = new Example(WhgUser.class);
        Criteria criteria = example.createCriteria();

        //根据姓名查找用户信息
        if (param.containsKey("name") && param.get("name") != null) {
            String name = (String) param.get("name");
            if (!"".equals(name.trim())) {
                criteria.andLike("name", "%" + name.trim() + "%");
            }
        }

        //根据昵称查找用户信息
        if (param.containsKey("nickname") && param.get("nickname") != null) {
            String nickname = (String) param.get("nickname");
            if (!"".equals(nickname.trim())) {
                criteria.andLike("nickname", "%" + nickname.trim() + "%");
            }
        }

        //根据籍贯查找用户信息
        if (param.containsKey("origo") && param.get("origo") != null) {
            String origo = (String) param.get("origo");
            if (!"".equals(origo.trim())) {
                criteria.andLike("origo", "%" + origo.trim() + "%");
            }
        }

        //根据手机号码查找用户信息
        if (param.containsKey("phone") && param.get("phone") != null) {
            String phone = (String) param.get("phone");
            if (!"".equals(phone.trim())) {
                criteria.andLike("phone", "%" + phone.trim() + "%");
            }
        }
        //根据邮箱地址查找用户信息
        if (param.containsKey("email") && param.get("email") != null) {
            String email = (String) param.get("email");
            if (!"".equals(email.trim())) {
                criteria.andLike("email", "%" + email.trim() + "%");
            }
        }
        //根据实名状态查找用户信息
        if (param.containsKey("isrealname") && param.get("isrealname") != null) {
            String isrealname = (String) param.get("isrealname");
            if (!"".equals(isrealname.trim())) {
                criteria.andEqualTo("isrealname", isrealname);
            }
        }
        List<WhgUser> list = this.userMapper.selectByExample(example);
        //List<Map> list = this.troupeUser.selectUser(param);
        PageInfo<WhgUser> pinfo = new PageInfo<WhgUser>(list);

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("total", pinfo.getTotal());
        res.put("rows", pinfo.getList());

        return res;
    }

    /**
     * 实名审核
     *
     * @param user
     */
    @CacheEvict(allEntries = true)
    public WhgUser checkUserReal(WhgUser user) throws Exception {
        this.userMapper.updateByPrimaryKeySelective(user);
        return this.userMapper.selectByPrimaryKey(user.getId());
    }

    @CacheEvict(allEntries = true)
    public ResponseBean setUserPhone(String id, String phone) throws Exception{
        ResponseBean res = new ResponseBean();
        if (id==null || id.isEmpty() || phone==null || phone.isEmpty()){
            res.setErrormsg("参数错误");
            res.setSuccess(res.FAIL);
            return res;
        }
        if (!phone.matches("\\d{11}")){
            res.setErrormsg("参数格式错误");
            res.setSuccess(res.FAIL);
            return res;
        }
        WhgUser user = this.userMapper.selectByPrimaryKey(id);
        if (user == null){
            res.setErrormsg("用户信息不存在");
            res.setSuccess(res.FAIL);
            return res;
        }
        //终止相同的修改
        if (user.getPhone()!=null && user.getPhone().equals(phone)){
            return res;
        }

        //检测重复手机号
        WhgUser record = new WhgUser();
        record.setPhone(phone);
        int rpcount = this.userMapper.selectCount(record);
        if (rpcount > 0){
            res.setErrormsg("指定的手机号已被使用");
            res.setSuccess(res.FAIL);
            return res;
        }
        //装入ID参数执行修改
        record.setId(id);
        this.userMapper.updateByPrimaryKeySelective(record);

        return res;
    }

    /**
     * 查找历史培训
     *
     * @return

    public Map<String, Object> selOldTra(Map<String, Object> param) throws Exception {
        //分页信息
        int page = Integer.parseInt((String) param.get("page"));
        int rows = Integer.parseInt((String) param.get("rows"));

        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.whTrainMapper.selOldTra(param);
        ;

        // 取分页信息
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
    }*/

    /**
     * 查找历史场馆
     *
     * @param uid
     * @return

    public Map<String, Object> selOldVen(Map<String, Object> param) throws Exception {
        //分页信息
        int page = Integer.parseInt((String) param.get("page"));
        int rows = Integer.parseInt((String) param.get("rows"));

        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.whTrainMapper.selOldVen(param);

        // 取分页信息
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
        //return
    }*/

    /**
     * 查找历史活动
     *
     * @param paramMap
     * @return

    public Map<String, Object> selOldAct(Map<String, Object> param) {
        //分页信息
        int page = Integer.parseInt((String) param.get("page"));
        int rows = Integer.parseInt((String) param.get("rows"));

        //带条件的分页查询
        PageHelper.startPage(page, rows);
        List<Map> list = this.whTrainMapper.selOldAct(param);

        // 取分页信息
        PageInfo<Map> pageInfo = new PageInfo<Map>(list);

        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
    } */

}
