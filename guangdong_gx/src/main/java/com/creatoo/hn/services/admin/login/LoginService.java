package com.creatoo.hn.services.admin.login;

import com.creatoo.hn.Constant;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.mapper.WhgSysDeptMapper;
import com.creatoo.hn.dao.mapper.WhgSysUserCultDeptMapper;
import com.creatoo.hn.dao.mapper.WhgSysUserCultMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 控制台登录
 * Created by wangxl on 2017/7/13.
 */
@SuppressWarnings("ALL")
@Service
public class LoginService extends BaseService{

}
