package com.creatoo.hn.services.api.wechat;

import com.creatoo.hn.dao.mapper.WhgUserMapper;
import com.creatoo.hn.dao.model.WhgUser;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.UploadUtil;
import com.creatoo.hn.util.bean.ApiResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserRealService {
	
	@Autowired
	private WhgUserMapper userMapper;

	@Autowired
	public Environment env;

	/** 保存用户实名图片
	 * @param user
	 * @param file
	 * @param filemake
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public WhgUser saveUserIdCardPic(WhgUser user, MultipartFile file, String filemake, HttpServletRequest request) throws Exception{
		if (file.isEmpty()) return user;
		//图片类型验证
		String mimetype = file.getContentType().toLowerCase();
		if (mimetype == null || !mimetype.startsWith("image")){
			throw new Exception("2");
		}
		//取最新会话用户数据
		user = this.userMapper.selectByPrimaryKey(user.getId());
		//若已审核实名，阻止修改
		if (user.getIsrealname()!=null && user.getIsrealname().compareTo(new Integer(1))==0){
			throw new Exception("1");
		}
		
		String rootPath = env.getProperty("upload.local.addr");

		//删旧的相关图片
		String oldpath = user.getIdcardface();
		if (filemake!=null && filemake.equalsIgnoreCase("idcardback")){
			oldpath = user.getIdcardback();
		}
		UploadUtil.delUploadFile(rootPath, oldpath);
		
		//处理图片
		Date now = new Date();
		String picPath = UploadUtil.getUploadFilePath(file.getOriginalFilename(), IDUtils.getID(), "whuserreal", "picture", now);
		file.transferTo( UploadUtil.createUploadFile(rootPath, picPath) );
		String newPath = UploadUtil.getUploadFileUrl(rootPath, picPath);
		
		//写入新的图片关连
		if (filemake!=null && filemake.equalsIgnoreCase("idcardback")){
			user.setIdcardback(newPath);
		}else{
			user.setIdcardface(newPath);
		}
		_setterState(user);
		this.userMapper.updateByPrimaryKeySelective(user);
		
		//返回新图片访问地址
		return user;
	}
	
	/** 保存用户实名信息
	 * @param user
	 * @return
	 */
	public WhgUser saveInfo(WhgUser user) throws Exception{
		WhgUser _user = this.userMapper.selectByPrimaryKey(user.getId());
		//若已审核实名，阻止修改
		if (_user.getIsrealname()!=null && _user.getIsrealname().compareTo(new Integer(1))==0){
			throw new Exception("1");
		}
		//若已存在审核或待审相同的身份证号，阻止修改
		if (this._getIdcardNum(user.getIdcard(), user.getId()) > 0){
			throw new Exception("3");
		}
		_user.setName(user.getName());
		_user.setIdcard(user.getIdcard());
		_setterState(_user);
		this.userMapper.updateByPrimaryKeySelective(_user);
		return _user;
	}
	
	//设置用户实名状态
	private void _setterState(WhgUser user){
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
		List<Integer> realState = new ArrayList<>();
		realState.add(1);
		realState.add(3);
		Example example = new Example(WhgUser.class);
		example.createCriteria().andEqualTo("idcard", idcard)
			.andIn("isrealname", realState)
			.andNotEqualTo("id", uid);
		return this.userMapper.selectCountByExample(example);
	}

	/**
	 * 微信端实名认证
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Object realNameApprove(WhgUser user) throws Exception{
		ApiResultBean arb = new ApiResultBean();

		if (user.getId()==null || user.getId().isEmpty()){
			arb.setCode(103);
			arb.setMsg("用户标识不能为空");
			return arb;
		}

		WhgUser _user = this.userMapper.selectByPrimaryKey(user.getId());
		if (_user == null){
			arb.setCode(102);
			arb.setMsg("用户信息查询失败");
			return arb;
		}

		//若已审核实名，阻止修改
		if (_user.getIsrealname()!=null && _user.getIsrealname().compareTo(new Integer(1))==0){
			arb.setMsg("用户已实名审核完成，取消修改");
			arb.setCode(103);
			return arb;
		}

		if (user.getName()==null || user.getName().isEmpty()){
			arb.setCode(103);
			arb.setMsg("姓名不能为空");
			return arb;
		}
		if (user.getIdcard()==null || user.getIdcard().isEmpty()){
			arb.setCode(103);
			arb.setMsg("身份证不能为空");
			return arb;
		}

		//若已存在审核或待审相同的身份证号，阻止修改
		if (this._getIdcardNum(user.getIdcard(), user.getId()) > 0){
			arb.setMsg("输入的身份证已存在，取消修改");
			arb.setCode(103);
			return arb;
		}

		String idcard = user.getIdcard();
		Integer idcardtype = user.getIdcardtype();
		if (idcardtype!=null && idcardtype.intValue()==2){
			if (user.getBirthday()==null){
				arb.setCode(103);
				arb.setMsg("出生日期不能为空");
				return arb;
			}
		}
		else if (idcard.matches("^\\d{15,17}.*")){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String year = idcard.substring(6,10);
			String month = idcard.substring(10,12);
			String day = idcard.substring(12,14);
			String birth = year+"-"+month+"-"+day;
			user.setBirthday(sdf.parse(birth));
		}

		_user.setName(user.getName());
		_user.setIdcard(user.getIdcard());
		_user.setIdcardback(user.getIdcardback());
		_user.setIdcardface(user.getIdcardface());
		_user.setBirthday(user.getBirthday());

        if (user.getIdcardtype()!=null){
            _user.setIdcardtype(user.getIdcardtype());
        }
        if (user.getSex()!=null){
            _user.setSex(user.getSex());
        }

		_setterState(_user);
		this.userMapper.updateByPrimaryKeySelective(_user);
		arb.setData(_user);
		return arb;
	}
}
