package com.creatoo.hn.services.admin.xj;

import com.creatoo.hn.dao.mapper.CrtWhgActivityMapper;
import com.creatoo.hn.dao.mapper.WhgActivityMapper;
import com.creatoo.hn.dao.mapper.WhgXjReasonMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgXjReason;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.ReqParamsUtil;
import com.creatoo.hn.util.enums.EnumBizState;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 活动管理 服务层
 *
 * @author heyi
 */
@Service
@CacheConfig(cacheNames = "WhgXjReason", keyGenerator = "simpleKeyGenerator")
public class WhgXjReasonService extends BaseService {


    @Autowired
    private WhgXjReasonMapper whgXjReasonMapper;

    @Autowired
    private SMSService smsService;

    @Autowired
    private WhgSystemUserService systemUserService;

    /**
     * 添加下架原因
     * @return
     * @throws Exception
     */
    public int t_add(WhgXjReason reason) {
        int rows = 0;
        try {
            reason.setId(IDUtils.getID());
            reason.setCrtdate(new Date());
            if (reason.getIssms() == null) {
                reason.setIssms(0);
            }
            rows = this.whgXjReasonMapper.insert(reason);
            if (reason.getIssms() != null && reason.getIssms() == 1) {//发送短信
                Map smsData = new HashMap();
                smsData.put("type", reason.getFktype());
                smsData.put("reason", reason.getReason());
                smsData.put("title", reason.getFktitile());
                if (reason.getTouser() != null) {
                    WhgSysUser user = systemUserService.t_srchOne(reason.getTouser());
                    if (user != null && user.getContactnum() != null) {
                        smsService.t_sendSMS(user.getContactnum(), "XJ_REASON", smsData, reason.getFkid());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    /**
     * 查询
     *
     * @return
     * @throws Exception
     */
    public List<WhgXjReason> t_srchList(WhgXjReason reason) throws Exception {
        //搜索条件
        Example example = new Example(WhgXjReason.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo(reason);
        example.setOrderByClause("crtdate desc");
        return this.whgXjReasonMapper.selectByExample(example);
    }


}
