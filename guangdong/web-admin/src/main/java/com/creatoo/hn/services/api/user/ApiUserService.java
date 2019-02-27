package com.creatoo.hn.services.api.user;

import com.creatoo.hn.ext.bean.ResponseBean;
import com.creatoo.hn.mapper.WhUserMapper;
import com.creatoo.hn.mapper.WhgUsrWeixinMapper;
import com.creatoo.hn.model.WhUser_old;
import com.creatoo.hn.model.WhgUsrWeixin;
import com.creatoo.hn.services.comm.CommService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.utils.MD5Util;
import com.creatoo.hn.utils.RegistRandomUtil;
import com.creatoo.hn.utils.UploadUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户模块接口服务
 * Created by wangxl on 2017/4/13.
 */
@Service
public class ApiUserService {
    /**
     * 日志控制器
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 公共服务，用于生成KEY等
     */
    @Autowired
    private CommService commService;

    /**
     * 短信服务
     */
    @Autowired
    private SMSService smsService;

    /**
     * 微信用户DAO
     */
    @Autowired
    private WhgUsrWeixinMapper whgUsrWeixinMapper;

    /**
     * 会员DAO
     */
    @Autowired
    private WhUserMapper whUserMapper;

    /**
     * 绑定手机
     * @param id 微信账号ID whg_usr_xeixin.id
     * @param phone 手机号
     * @return 100-绑定成功；101-手机格式不正确; 102-参数id无效； 103-手机号已经被其它账号绑定; 104-手机号已经被自己绑定 105--该账号已经绑定手机无法再次绑定
     * @throws Exception
     */
    public ResponseBean bindPhone(String id, String phone)throws Exception{
        ResponseBean res = new ResponseBean();
        String code = "100";

        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("101");
            return res;
        }
        //根据用户ID找到会员
        WhUser_old user = this.whUserMapper.selectByPrimaryKey(id);
        if(user != null && user.getPhone() != null && "".equals(user.getPhone())){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("105");  //该账号已经绑定手机无法再次绑定
            return res;
        }

        //id有效
        Example example1 = new Example(WhgUsrWeixin.class);
        Example.Criteria c = example1.createCriteria();
        c.andEqualTo("userid",id);
        List<WhgUsrWeixin> whgUsrWeixin = whgUsrWeixinMapper.selectByExample(example1);
        if(whgUsrWeixin == null || whgUsrWeixin.size() == 0){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
            return res;
        }
//        if(whgUsrWeixin == null || whgUsrWeixin.getId() == null){
//            return "102";
//        }

        //根据phone找到会员
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<WhUser_old> whuserList = this.whUserMapper.selectByExample(example);
        WhUser_old whuser = null;
        if(whuserList != null && whuserList.size() > 0){
            whuser = whuserList.get(0);
        }

        //存在用户
        if(whuser != null){
            //是否已经绑定
            WhgUsrWeixin userwx = new WhgUsrWeixin();
            userwx.setUserid(whuser.getId());
            userwx = this.whgUsrWeixinMapper.selectOne(userwx);
            if(userwx != null && userwx.getId() != null){
                if(userwx.getUserid().equals(id)){
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("104");
                    res.setData(whuser.getId());
                    return res;
                   // return "103";//手机号已经被其它微信用户绑定
                }else{
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("103");
                    res.setData(whuser.getId());
                    return res;
                    //return "104";//手机号已经被自己绑定
                }
            }else{//未被绑定-直接绑定到
                //删除微信第一次登录时生成的用户信息，将当前用户进行绑定
                this.whUserMapper.deleteByPrimaryKey(whgUsrWeixin.get(0).getUserid());
                whgUsrWeixin.get(0).setUserid(whuser.getId());
                Example exa2 = new Example(WhgUsrWeixin.class);
                exa2.createCriteria().andEqualTo("userid", id);
                this.whgUsrWeixinMapper.updateByExampleSelective(whgUsrWeixin.get(0), exa2);
                res.setData(whuser.getId());
            }
        }else{//不存在用户
            //插入用户记录
            WhUser_old whUser1 = this.whUserMapper.selectByPrimaryKey(whgUsrWeixin.get(0).getUserid());
            if(whUser1.getPassword() == null || "".equals(whUser1.getPassword())){
                String password = RegistRandomUtil.random();
                String passwordMD5 = MD5Util.toMd5(password);
                whUser1.setPassword( passwordMD5 );
                //发送短信，告诉密码
                Map<String, String> data = new HashMap<String, String>();
                data.put("userName",whUser1.getNickname());
                data.put("password",password);
                smsService.t_sendSMS(phone, "LOGIN_PASSWROD", data, whUser1.getId());
            }
            //whUser1.setId(commService.getKey("whuser"));
            String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            whUser1.setPhone(phone);
           // whUser1.setNickname(nickname);
           // whUser1.setIsrealname(0);
           // whUser1.setIsperfect(0);
           // whUser1.setIsinner(0);

            this.whUserMapper.updateByPrimaryKeySelective(whUser1);
            res.setData(whUser1.getId());
            //修改微信用户表userid
//            whgUsrWeixin.setUserid(whUser.getId());
//            Example exa2 = new Example(WhgUsrWeixin.class);
//            exa2.createCriteria().andEqualTo("id", id);
//            this.whgUsrWeixinMapper.updateByExampleSelective(whgUsrWeixin, exa2);
        }

        return res;
    }

    /**
     * 取消绑定手机
     * @param id id 微信账号ID whg_usr_xeixin.id
     * @param phone 手机号
     * @return 100-解绑成功；101-手机格式不正确; 102-参数id无效
     * @throws Exception
     */
    public String unbindPhone(String id, String phone)throws Exception{
        String code = "100";

        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            return "101";
        }

        //id有效
        WhgUsrWeixin whgUsrWeixin = whgUsrWeixinMapper.selectByPrimaryKey(id);
        if(whgUsrWeixin == null || whgUsrWeixin.getId() == null){
            return "102";
        }

        //解绑
        whgUsrWeixin.setUserid(null);
        Example exa2 = new Example(WhgUsrWeixin.class);
        exa2.createCriteria().andEqualTo("id", id);
        this.whgUsrWeixinMapper.updateByExample(whgUsrWeixin, exa2);

        return code;
    }

    /**
     * 微信注册
     * @param phone  手机号码
     * @param password  密码
     * @return 100-注册成功；101-手机格式不正确; 102-该号码已经存在
     */
    public String register(String phone, String password) throws Exception {
        String code = "100";
        //手机格式不正确
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            return "101";
        }
        //根据phone找到会员
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<WhUser_old> whuserList = this.whUserMapper.selectByExample(example);
        //没有用户
        if(whuserList != null && whuserList.size() > 0){
            //用户已经存在
            return "102";

        }else{
            //插入用户记录
            WhUser_old whuser = new WhUser_old();
            whuser.setId(commService.getKey("whuser"));
            String nickname = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            whuser.setPhone(phone);
            whuser.setNickname(nickname);
            whuser.setIsrealname(0);
            whuser.setIsperfect(0);
            whuser.setIsinner(0);
            whuser.setRegisttime(new Date());
            whuser.setPassword( password);
            this.whUserMapper.insert(whuser);
        }
        return code;
    }

    /**
     * 修改用户资料
     * @param whuser 用户信息
     * @throws Exception
     */
    public void saveUserInfo(WhUser_old whUser)throws  Exception{
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("id", whUser.getId());
        this.whUserMapper.updateByExampleSelective(whUser, example);
    }

    /**
     * 接口实名认证
     * @param user
     * @return
     */
    public ResponseBean saveRealnameInfo(WhUser_old user, String accesstoken, HttpServletRequest req) throws Exception {
        ResponseBean res = new ResponseBean();
        String rootPath = UploadUtil.getUploadPath(req);
        //处理图片保存
        String idcardface = this._saveMediaImg2Local(accesstoken, user.getIdcardface(), rootPath);
        String idcardback = this._saveMediaImg2Local(accesstoken, user.getIdcardback(), rootPath);
        WhUser_old _user = this.whUserMapper.selectByPrimaryKey(user.getId());
        if(_user == null){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("105");
            return res;
        }
        //若已审核实名，阻止修改
        if (_user.getIsrealname()!=null && _user.getIsrealname().compareTo(new Integer(1))==0){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");
            return res;
        }
        //若已存在审核或待审相同的身份证号，阻止修改
        if (this._getIdcardNum(user.getIdcard(), user.getId()) > 0){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("103");
            return res;
        }
        String _idcard = user.getIdcard().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String year = _idcard.substring(6,10);
        String month = _idcard.substring(10,12);
        String day = _idcard.substring(12,14);
        String birth = year+"-"+month+"-"+day;
        _user.setSex(user.getSex());
        _user.setBirthday(sdf.parse(birth));
        _user.setName(user.getName());
        _user.setIdcard(_idcard);
        _user.setIdcardface(idcardface);
        _user.setIdcardback(idcardback);
        _setterState(_user);
        this.whUserMapper.updateByPrimaryKeySelective(_user);
        return res;
    }

    /**
     * 处理操作 B 的图片
     * @param accesstoken
     * @param media_id
     * @param rootPath
     * @return
     * @throws Exception
     */
    private String _saveMediaImg2Local(String accesstoken, String media_id, String rootPath) throws Exception{
        Map<String, Object> mediaMap = this._getWapMediaStream(accesstoken, media_id);
        InputStream is = (InputStream) mediaMap.get("is");
        String filename = (String) mediaMap.get("filename");
        FileOutputStream fileOutputStream = null;
        try {
            //获取文件后缀
            String fileExt = filename.substring(filename.lastIndexOf("."));
            Date now = new Date();
            String picPath = UploadUtil.getUploadFilePath(filename, media_id, "whuser", "picture", now);

            File dbfile = UploadUtil.createUploadFile(rootPath, picPath);
            if (dbfile.exists()){
                dbfile.delete();
            }
            dbfile.createNewFile();
            fileOutputStream = new FileOutputStream(dbfile);

            byte[] data = new byte[1024];
            int len = 0;
            while ((len = is.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }

            return UploadUtil.getUploadFileUrl(rootPath, picPath);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 按access_token 和 media_id 获取资源文件输入流
     * @param accesstoken
     * @param media_id
     * @return
     * @throws Exception
     */
    private Map<String, Object> _getWapMediaStream(String accesstoken, String media_id) throws Exception{
        try {
            InputStream is = null;
            String url = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken+ "&media_id=" + media_id;
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            //System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            //System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();

            // 获取文件转化为byte流
            is = http.getInputStream();

            //检查是否返回了错误信息
            String contentType = http.getContentType();
            if (contentType.startsWith("application/json")){
                int len = http.getContentLength();
                byte[] b = new byte[len]; is.read(b);
                String t = new String(b, "UTF-8");
                is.close();
                throw new Exception(t);
            }

            //文件名分析
            String disposition = http.getHeaderField("Content-Disposition");
            String filename = "XXX.jpg";
            if (disposition == null){
                contentType.startsWith("image");
                filename = media_id+".jpg";
            }else{
                String[] a = disposition.split("filename=");
                if (a.length>1){
                    filename = a[1];
                    filename = filename.replaceAll("\"", "");
                }
            }

            Map<String, Object> resmap = new HashMap<String, Object>();
            resmap.put("is", is);
            resmap.put("filename", filename);

            return resmap;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("1001");
        }
    }

    //设置用户实名状态
    private void _setterState(WhUser_old user){
        //已实名的不做处理
        if (user.getIsrealname()!=null && user.getIsrealname().compareTo(new Integer(1))==0){
            return;
        }
        //信息完备后设为待审状态
        if (user.getName()!=null && user.getIdcard()!=null && user.getIdcardface()!=null && user.getIdcardback()!=null
                && _getIdcardNum(user.getIdcard(), user.getId())==0){
            user.setIsrealname(3);
        }else{
            user.setIsrealname(0);
        }
    }

    /**
     * 检查用户身份证已审和待审状态的记录数，不包函自己ID的计数
     * @param idcard
     * @return
     */
    private int _getIdcardNum(String idcard, String uid){
        List<Integer> realState = new ArrayList<Integer>();
        realState.add(1);
        realState.add(3);
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("idcard", idcard)
                .andIn("isrealname", realState)
                .andNotEqualTo("id", uid);
        return this.whUserMapper.selectCountByExample(example);
    }

    /**
     *检查身份证是否存在
     * @param idcard
     */
    public int checkIdcard(String idcard,String uid) throws Exception {
        List<Integer> realState = new ArrayList<Integer>();
        realState.add(1);
        realState.add(3);
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("idcard", idcard)
                .andIn("isrealname", realState)
                .andNotEqualTo("id", uid);
        return this.whUserMapper.selectCountByExample(example);
    }

    /**
     * 个人中心编辑用户信息
     * @param request
     * @return
     */
    public ResponseBean editUserInfo(HttpServletRequest request)throws Exception {
        ResponseBean res = new ResponseBean();
        WhUser_old user = new WhUser_old();
        String id = request.getParameter("id");
        if(id == null && "".equals(id)){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("201");//ID不存在
        }
        String nickname = request.getParameter("nickname");
        String nation = request.getParameter("nation");
        String origo = request.getParameter("origo");
        String job = request.getParameter("job");
        String company = request.getParameter("company");
        String address = request.getParameter("address");
        if(nickname != null && !"".equals(nickname.trim()) && nickname.length() < 20){
            user.setNickname(nickname.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");//昵称为空或长度过长
        }
        if(nation != null && !"".equals(nation.trim()) && nation.length() < 50){
            user.setNation(nation.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("103");//名族为空或过长
        }
        if(origo != null && !"".equals(origo) && origo.length() < 50){
            user.setOrigo(origo.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("104");//籍贯为空或者过长
        }
        if(job != null && !"".equals(job.trim()) && job.length() < 100){
            user.setJob(job.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("105");//职业为空或过长
        }
        if(company != null && !"".equals(company)){
            user.setCompany(company.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("106");//工作单位为空或过长
        }
        if(address != null && !"".equals(address)){
            user.setAddress(address.trim());
        }else{
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("107");//通讯地址为空或过长
        }
        whUserMapper.updateByPrimaryKeySelective(user);
        return res;
    }

    /**
     * 检查电话号码是否存在
     * @param
     * @return
     */
    public ResponseBean checkPhone(String phone)throws Exception {
        ResponseBean res = new ResponseBean();
        //String phone = request.getParameter("phone");
        if(phone == null || !phone.matches("^1[3|4|5|7|8][0-9]\\d{8}$")){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("102");  //手机格式不正确
            return res;
        }
        Example example = new Example(WhUser_old.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("phone",phone);
        List<WhUser_old> userList = this.whUserMapper.selectByExample(example);
        if(userList == null || userList.size() == 0){
            res.setSuccess(ResponseBean.FAIL);
            res.setErrormsg("103");  //号码不存在
        }
        return res;
    }

    /**
     * 号码是否绑定
     * @param phone
     * @return
     */
    public ResponseBean t_isBindPhone(String phone,String id) {
        ResponseBean res = new ResponseBean();
        //根据phone找到会员
        Example example = new Example(WhUser_old.class);
        example.createCriteria().andEqualTo("phone", phone);
        List<WhUser_old> whuserList = this.whUserMapper.selectByExample(example);
        WhUser_old whUser = null;
        if(whuserList != null && whuserList.size() > 0){
            whUser = whuserList.get(0);
        }

        //存在用户
        if(whUser != null) {
            //是否已经绑定
            WhgUsrWeixin userwx = new WhgUsrWeixin();
            userwx.setUserid(whUser.getId());
            userwx = this.whgUsrWeixinMapper.selectOne(userwx);
            if (userwx != null && userwx.getId() != null) {
                if (userwx.getUserid().equals(id)) {
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("103");
                    res.setData(whUser.getId());
                    return res;
                    // return "103";//手机号已经被其它微信用户绑定
                } else {
                    res.setSuccess(ResponseBean.FAIL);
                    res.setErrormsg("104");
                    res.setData(whUser.getId());
                    return res;
                    //return "104";//手机号已经被自己绑定
                }
            }
        }
        return res;
    }

}
