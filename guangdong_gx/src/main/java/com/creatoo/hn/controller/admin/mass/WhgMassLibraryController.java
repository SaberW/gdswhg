package com.creatoo.hn.controller.admin.mass;

import com.creatoo.hn.Constant;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.mass.WhgMassLibraryService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTagService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.RequestUtils;
import com.creatoo.hn.util.annotation.WhgOPT;
import com.creatoo.hn.util.bean.ResponseBean;
import com.creatoo.hn.util.enums.EnumOptType;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 群文资源库
 * Created by wangxl on 2017/11/7.
 */
@RestController
@RequestMapping("/admin/mass/library")
public class WhgMassLibraryController extends BaseController {
    /**
     * 资源库服务
     */
    @Autowired
    private WhgMassLibraryService whgMassLibraryService;

    /**
     * service
     */
    @Autowired
    private WhgYunweiTagService whgYunweiTagService;

    /**
     * 跳转到配置页面
     * @param request
     * @param type
     * @return
     */
    @GetMapping("/view/{type}")
    public ModelAndView listview(HttpServletRequest request, @PathVariable("type") String type) {
        ModelAndView view = new ModelAndView();
        try {
            if("add".equals(type)){//添加资源库
                view.addObject("isAdd", type);
                WhgMassLibrary library = new WhgMassLibrary();
                library.setId(IDUtils.getID32());//临时给资源库一个ID， 添加字段时需要
                view.addObject("library", library);
            }else if("edit".equals(type)){//编辑资源库
                String id = RequestUtils.getParameter(request, "id");
                //查询自定义配置
                WhgMassLibrary library = this.whgMassLibraryService.findById(id);
                view.addObject("library", library);
                view.addObject("isAdd", type);
                List<WhgYwiTag> list = new ArrayList<>();
                if(StringUtils.isNotBlank(library.getTags())){
                    String[] tags = library.getTags().split(",");
                    if(StringUtils.isNotBlank(library.getTags())){
                        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(tags));
                        list = whgYunweiTagService.findAllYwiTag("56",null,arrayList);
                        view.addObject("tagList", list);
                    }
                }
                //进入编辑页面时，把临时的数据但未提交的重新置为真实数据
                /*WhgSysUser sysUser = RequestUtils.getAdmin(request);
                if(StringUtils.isNotEmpty(id) && sysUser != null && StringUtils.isNotEmpty(sysUser.getId())) {
                    this.whgMassLibraryService.updateFormFieldTempState(library.getId(), sysUser.getId());
                }*/
            }else if("field".equals(type)){//编辑字段页面
                String fid1 = RequestUtils.getParameter(request, "fid1");//生效的字段ID
                String fid2 = RequestUtils.getParameter(request, "fid2");//维护中的字段ID
                String columnid = RequestUtils.getParameter(request, "columnid");//whg_mass_library_form表的ID
                String libid = RequestUtils.getParameter(request, "libid");//资源库标识，有可能是临时的
                WhgMassLibraryFormField extConfProperty = null;
                if(StringUtils.isNotEmpty(fid2)){
                    extConfProperty = this.whgMassLibraryService.findLibraryFormFieldById(fid2);
                }else if(StringUtils.isNotEmpty(fid1)){
                    extConfProperty = this.whgMassLibraryService.findLibraryFormFieldById(fid1);
                }else{
                    extConfProperty = new WhgMassLibraryFormField();
                    extConfProperty.setLibid(libid);
                }
                if(StringUtils.isNotEmpty(columnid)){
                    view.addObject("formObj", this.whgMassLibraryService.findLibraryFormById(columnid));
                }
                view.addObject("fid1", fid1);
                view.addObject("fid2", fid2);
                view.addObject("fieldObj", extConfProperty);
            }
            view.setViewName("admin/mass/library/view_" + type);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return view;
    }

    /**
     * 根据唯一标识查询资源库信息
     * @param id 资源库唯一标识
     * @return WhgMassLibrary
     * @throws Exception
     */
    @GetMapping("/findLibrary")
    public WhgMassLibrary findLibraryById(@RequestParam("id") String id)throws Exception{
        return this.whgMassLibraryService.findById(id);
    }

    /**
     * 查询所有自定义属性
     * @param id 自定义配置的ID
     * @return List<WhgFyiExtConfProperty>
     * @throws Exception
     */
    @GetMapping("/findfields")
    public Object findProperty(String id, String resid)throws Exception{
        //表单
        WhgMassLibraryForm form = new WhgMassLibraryForm();
        form.setLibid(id);
        form.setIstemp(0);
        List<WhgMassLibraryForm> forms = this.whgMassLibraryService.findLibraryForm(form);

        //字段
        WhgMassLibraryFormField field = new WhgMassLibraryFormField();
        field.setIstemp(0);
        field.setLibid(id);
        List<WhgMassLibraryFormField> fields = this.whgMassLibraryService.findLibraryFormField(field);

        //数据
        if(StringUtils.isNotEmpty(resid)){//只查了资源表的数据，子表没有处理
            if(fields != null && fields.size() > 0){
                Map resMap = this.whgMassLibraryService.findResourceById(id, resid);
                for(WhgMassLibraryFormField _field : fields){
                    String fieldCode = _field.getFieldcode();
                    if(resMap.containsKey(fieldCode)){
                        Object fieldVal = resMap.get(fieldCode);
                        _field.setFielddefaultval(fieldVal.toString());
                    }
                }
            }
        }

        //数据组合
        Map<String, List<?>> map = new HashMap<>();
        map.put("forms", forms);
        map.put("fields", fields);
        return map;
    }

    /**
     * 分页查询资源库
     * @param sysUser
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param library
     * @return
     */
    @PostMapping("/srchList4p")
    public ResponseBean findByPaging(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                   @RequestParam(value="page", defaultValue="1")Integer page,
                                   @RequestParam(value="rows", defaultValue="10")Integer rows,
                                   @RequestParam(value="sort", required=false)String sort,
                                   @RequestParam(value="order", required=false)String order,
                                   WhgMassLibrary library){
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<WhgMassLibrary> pageInfo = this.whgMassLibraryService.findByPaging(page, rows, sort, order, library, sysUser);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 保存资源库自定义表单中的行信息
     * @param totalcolumns 共有几列
     * @param labelname1 列文字说明
     * @param columntype1 列字段形式
     * @param labelname2 列文字说明
     * @param columntype2 列字段形式
     * @return
     */
    @PostMapping("/saverow")
    public ResponseBean saveRow(
            @SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
            @RequestParam(value = "libid") String libid,
            @RequestParam(value = "totalcolumns") Integer totalcolumns,
            @RequestParam(value = "labelname1") String labelname1,
            @RequestParam(value = "columntype1") Integer columntype1,
            @RequestParam(value = "columns1",defaultValue = "1") Integer columns1,
            @RequestParam(value = "labelname2", required = false) String labelname2,
            @RequestParam(value = "columntype2", required = false) Integer columntype2,
            @RequestParam(value = "columns2", defaultValue = "1") Integer columns2
    ){
        ResponseBean res = new ResponseBean();
        try {
            List<WhgMassLibraryForm> forms = new ArrayList<>();
            WhgMassLibraryForm form = new WhgMassLibraryForm();
            form.setTotalcolumns(totalcolumns);
            form.setColumns(0);
            form.setLabelname(labelname1);
            form.setColumntype(columntype1);
            form.setSize(columns1);
            form.setLibid(libid);
            forms.add(form);
            if(StringUtils. isNotEmpty(labelname2)){
                WhgMassLibraryForm form2 = new WhgMassLibraryForm();
                form2.setTotalcolumns(totalcolumns);
                form2.setColumns(1);
                form2.setLabelname(labelname2);
                form2.setColumntype(columntype2);
                form2.setSize(columns2);
                form2.setLibid(libid);
                forms.add(form2);
            }
            List<String> ids = this.whgMassLibraryService.saveRow4temp(sysUser, forms);
            res.setData(StringUtils.join(ids.toArray(), ","));
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 保存自定义字段
     * @param sysUser 管理员
     * @param field 自定义字段信息
     * @return
     */
    @PostMapping("/savefield")
    public ResponseBean saveField(HttpServletRequest request, @SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser, WhgMassLibraryFormField field, String fid1){
        ResponseBean res = new ResponseBean();
        try {
            //下拉框可选值
            String fieldlistdata = "";
            String[] val = request.getParameterValues("optionval");
            if(val != null && val.length > 0){
                fieldlistdata = StringUtils.join(val, ",");
                field.setFieldlistdata(fieldlistdata);
            }
            String cptwidth1 = RequestUtils.getParameter(request, "cptwidth1");
            String cptwidth2 = RequestUtils.getParameter(request, "cptwidth2");
            String cptheight = RequestUtils.getParameter(request, "cptheight");
            if(StringUtils.isNotEmpty(cptwidth1) && StringUtils.isNotEmpty(cptwidth2)){
                field.setFieldwidth(cptwidth1+cptwidth2);
            }
            if(StringUtils.isNotEmpty(cptheight)){
                field.setFieldheight(cptheight);
            }
            WhgMassLibraryFormField fieldid = this.whgMassLibraryService.saveField4temp(sysUser, field, fid1);
            res.setData(fieldid);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 添加资源库
     * @param sysUser 管理员
     * @param library 资源库
     * @return
     */
    @PostMapping("/add")
    public ResponseBean add(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,String adds, String edits,String dels, String keeps, WhgMassLibrary library){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.add(sysUser, library, adds, edits, dels, keeps);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 编辑资源库
     * @param sysUser 管理员
     * @param id 资源库标识
     * @param library 修改信息
     * @return
     */
    @PostMapping("/edit")
    public ResponseBean edit(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser
                             ,@RequestParam String id,String adds, String edits,String dels, String keeps
                             ,WhgMassLibrary library){
        ResponseBean res = new ResponseBean();
        try {
            library.setId(id);
            this.whgMassLibraryService.edit(sysUser, library, adds, edits, dels, keeps);
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改资源库状态
     * @param sysUser 管理员
     * @param id 资源库标识
     * @param fromState 修改之前状态
     * @param toState 修改之后的状态
     * @return
     */
    @PostMapping("/updstate")
    public ResponseBean updstate(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser
            ,@RequestParam String id
            ,@RequestParam Integer fromState
            ,@RequestParam Integer toState
    ){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.updateState(sysUser, id, fromState, toState);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 删除资源库
     * @param id
     * @return
     */
    @PostMapping("/del")
    public ResponseBean del(@RequestParam String id, @RequestParam(value="force", defaultValue = "0")String force){
        ResponseBean res = new ResponseBean();
        try {
            this.whgMassLibraryService.delete(id, "1".equals(force));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }

    /**
     * 查询某个分类下的资源库
     * @param sysUser 管理员标识
     * @param id img/video/audio/file
     * @param cultid 文化馆标识
     * @param deptid 部门标识
     * @return
     */
    @PostMapping("/srchlibs")
    public List<Map<String, String>> findLibs(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                             String id, String cultid, String deptid, String limittype){
        List<Map<String, String>> rtnList = new ArrayList<>();
        try {
            //如果id为空,直接查询常量
            if(StringUtils.isEmpty(id)){
                Class<?> class1 = Class.forName("com.creatoo.hn.util.enums.EnumUploadType");
                Object[] objs = class1.getEnumConstants();
                for(Object obj : objs){
                    Method valueMethod = obj.getClass().getMethod("getValue");
                    Method nameMethod = obj.getClass().getMethod("getName");
                    String _val = valueMethod.invoke(obj).toString();
                    String _nam = nameMethod.invoke(obj).toString();
                    Map<String, String> map = new HashMap<String, String>();
                    if(StringUtils.isEmpty(limittype) || _val.equals(limittype)) {
                        map.put("text", _nam);
                        map.put("id", _val);
                        map.put("state", "closed");
                        rtnList.add(map);
                    }
                }
            }else {
                WhgMassLibrary library = new WhgMassLibrary();
                library.setResourcetype(id);
                if(StringUtils.isBlank(cultid)){
                    library.setCultid(sysUser.getCultid());
                }else{
                    library.setCultid(cultid);
                }
                if(StringUtils.isNotBlank(deptid)){
                    library.setDeptid(deptid);
                }
                library.setState(EnumState.STATE_YES.getValue());
                List<WhgMassLibrary> list = this.whgMassLibraryService.find(null, null, library, sysUser);
                if(list != null){
                    for(WhgMassLibrary lib : list){
                        Map<String, String> map = new HashMap<>();
                        map.put("id", lib.getId());
                        map.put("text", lib.getName());
                        map.put("state", "open");
                        rtnList.add(map);
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return rtnList;
    }


    /**
     * 查询某个分类下的资源库
     * @param sysUser 管理员标识
     * @param id img/video/audio/file
     * @param cultid 文化馆标识
     * @param deptid 部门标识
     * @return
     */
    @PostMapping("/findShareLibrary")
    public List<Map<String, String>> findShareLibrary(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                              String id, String cultid, String deptid, String limittype){
        List<Map<String, String>> rtnList = new ArrayList<>();
        try {
            //如果id为空,直接查询常量
            if(StringUtils.isEmpty(id)){
                Class<?> class1 = Class.forName("com.creatoo.hn.util.enums.EnumUploadType");
                Object[] objs = class1.getEnumConstants();
                for(Object obj : objs){
                    Method valueMethod = obj.getClass().getMethod("getValue");
                    Method nameMethod = obj.getClass().getMethod("getName");
                    String _val = valueMethod.invoke(obj).toString();
                    String _nam = nameMethod.invoke(obj).toString();
                    Map<String, String> map = new HashMap<String, String>();
                    if(StringUtils.isEmpty(limittype) || _val.equals(limittype)) {
                        map.put("text", _nam);
                        map.put("id", _val);
                        map.put("state", "closed");
                        rtnList.add(map);
                    }
                }
            }else {
                WhgMassLibrary library = new WhgMassLibrary();
                library.setResourcetype(id);
                if(StringUtils.isBlank(cultid)){
                    library.setCultid(sysUser.getCultid());
                }else{
                    library.setCultid(cultid);
                }
                if(StringUtils.isNotBlank(deptid)){
                    library.setDeptid(deptid);
                }
                library.setState(EnumState.STATE_YES.getValue());
                List<WhgMassLibrary> list = this.whgMassLibraryService.findShareLibrary(null, null, library, sysUser);
                if(list != null){
                    for(WhgMassLibrary lib : list){
                        Map<String, String> map = new HashMap<>();
                        map.put("id", lib.getId());
                        map.put("text", lib.getName());
                        map.put("state", "open");
                        rtnList.add(map);
                    }
                }
            }

        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return rtnList;
    }

    /**
     * 分页查询资源库
     * @param sysUser
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param userid
     * @param library
     * @return
     */
    @PostMapping("/srchlibsByPage")
    public ResponseBean srchlibsByPage(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                     @RequestParam(value="page", defaultValue="1")Integer page,
                                     @RequestParam(value="rows", defaultValue="10")Integer rows,
                                     @RequestParam(value="sort", required=false)String sort,
                                     @RequestParam(value="order", required=false)String order,
                                     @RequestParam(value="userid", required=true)String userid,
                                     WhgMassLibrary library){
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = this.whgMassLibraryService.findByPaging3(page, rows, sort, order, library, sysUser, userid);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 资源库是否可以上传资源
     *
     * @param id 资源库id
     * @param cultid 文化馆id
     * @param deptid 部门id
     * @return
     */
    @RequestMapping(value = "/libIsShare", method = RequestMethod.POST)
    public ResponseBean libIsShare(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                   String id, String cultid, String deptid){
        ResponseBean res = new ResponseBean();
        int count = 0;
        try {
            WhgMassLibrary library = new WhgMassLibrary();
            library.setId(id);
            library.setCultid(cultid);
            library.setDeptid(deptid);
            library.setState(EnumState.STATE_YES.getValue());
            count = this.whgMassLibraryService.find(library, sysUser);
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        res.setData(count);
        return res;
    }

    /**
     * 给用户分配资源库权限
     *
     * @param userid 用户
     * @param libids 资源库id，多个以逗号隔开
     * @param power 权限（view：查看，download：下载）
     * @param flag 取消/开启（0：取消，1：开启）
     * @return
     */
    @PostMapping("/addUserAuth")
    public ResponseBean addUserAuth(String userid, String libids, String power, String flag){
        ResponseBean res = new ResponseBean();
        try {
            if(StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(libids)
                    && StringUtils.isNotBlank(power) && StringUtils.isNotBlank(flag)){
                this.whgMassLibraryService.updateUserAuthByLib(userid, libids, power, flag);
            }else{
                throw new Exception("请求参数不正确");
            }
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 申请用户
     * @param sysUser
     * @param page
     * @param rows
     * @return
     */
    @PostMapping("/apply_user_list")
    public ResponseBean applyUserList(@SessionAttribute(Constant.SESSION_ADMIN_KEY)WhgSysUser sysUser,
                                       @RequestParam(value="page", defaultValue="1")Integer page,
                                       @RequestParam(value="rows", defaultValue="10")Integer rows,
                                        String account,String contactnum,
                                       WhgMassApplyUser applyUser){
        ResponseBean res = new ResponseBean();
        try {
            PageInfo<Map> pageInfo = this.whgMassLibraryService.applyUserList(page, rows,  applyUser, account,contactnum,sysUser);
            res.setRows(pageInfo.getList());
            res.setTotal(pageInfo.getTotal());
        } catch (Exception e) {
            res.setSuccess(ResponseBean.FAIL);
            res.setRows(new ArrayList(0));
            res.setTotal(0);
            log.error(e.getMessage());
        }
        return res;
    }

    /**
     * 修改状态
     * @param request 请求对象
     * @param ids 用逗号分隔的多个ID
     * @param toState 修改后的状态
     * @return 执行操作返回结果的JSON信息
     */
    @RequestMapping(value = "/applyuser/updstate")
    @WhgOPT(optType = EnumOptType.ADMIN, optDesc = {"通过", "拒绝"}, valid = {"toState=1", "toState=2"})
    public ResponseBean updstate(HttpServletRequest request, String ids,  String toState){
        ResponseBean res = new ResponseBean();
        try {
            whgMassLibraryService.applyuser_updstate(ids, toState, RequestUtils.getAdmin(request));
        }catch (Exception e){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg(e.getMessage());
            log.error(e.getMessage(), e);
        }
        return res;
    }
}
