package com.creatoo.hn.services.admin.yunwei;


import com.creatoo.hn.dao.mapper.WhgMassLibraryMapper;
import com.creatoo.hn.dao.mapper.WhgYwiQuotaMapper;
import com.creatoo.hn.dao.model.WhgSysUser;
import com.creatoo.hn.dao.model.WhgYwiQuota;
import com.creatoo.hn.dao.vo.WhgYwiQuotaVO;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemUserService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.EmailUtil;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.bean.ResponseBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/17.
 */
@Service
@CacheConfig(cacheNames = "WhgYwiQuota", keyGenerator = "simpleKeyGenerator")
public class WhgYunweiQuotaService extends BaseService {
    /**
     * 管理员服务类
     */
    @Autowired
    private WhgSystemUserService whgSystemUserService;

    /**
     * 关键字mapper
     */
    @Autowired
    private WhgYwiQuotaMapper quotaMapper;

    @Autowired
    private WhgSystemCultService systemCultService;

    @Autowired
    private WhgMassLibraryMapper libraryMapper;

    @Autowired
    private InsMessageService insMessageService;

    /**
     * 短信公开服务类
     */

    @Autowired
    private SMSService smsService;

    public WhgYwiQuota findDefaultSet() {
        WhgYwiQuota quota = this.quotaMapper.findDefaultDefaultSet();
        if (quota == null) {
            quota = new WhgYwiQuota();
            quota.setSize("1024");
            quota.setWarningpercent(50F);
            quota.setId(IDUtils.getID32());
            this.quotaMapper.insertSelective(quota);
        }
        return quota;
    }

    /**
     * 修改系统空间
     *
     * @param
     */
    @CacheEvict(allEntries = true)
    public void t_edit(WhgYwiQuota quota, WhgSysUser sysUser) throws Exception {
        //判断修改空间是否小于实际使用
        if (!checkUsedsize(quota.getCultid(), quota.getSize())) {
            throw new Exception("修改空间小于实际使用，请刷新页面核对后重新输入！");
        }
        quota.setMdfdate(new Date());
        quota.setMdfuser(sysUser.getId());
        //修改配额重置发送通知
        quota.setIssendnotice(false);
        if (StringUtils.isEmpty(quota.getId())) {
            quota.setId(IDUtils.getID32());
            this.quotaMapper.insertSelective(quota);
        } else {
            this.quotaMapper.updateByPrimaryKeySelective(quota);
        }
        sendQuotaNotice(quota.getId());
    }

    public void sendQuotaNotice(String id) {
        try {
            WhgYwiQuota quota = this.quotaMapper.selectByPrimaryKey(id);
            Map<String, String> noticeparam = new HashMap<>();
            noticeparam.put("size", quota.getSize());
            if (StringUtils.isNotEmpty(quota.getNoticephone())) {
                // TODO: 2019/2/25 0025  发送短信配额通知
                smsService.t_sendSMS(quota.getNoticephone(), "SMS_QUOTA_NOTICE", noticeparam, quota.getId());
            }
            if (StringUtils.isNotEmpty(quota.getNoticeemail())) {
                // TODO: 2019/2/25 0025  发送配额通知邮件
                EmailUtil.sendNoticeEmail(quota.getNoticeemail(), "您好，您的群文资源库使用空间已被修改为" + quota.getSize() + "GB，请及时备份资源并合理使用空间。");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendQuotaFullNotice(String id) {
        try {
            Float size = 0F;
            WhgYwiQuota quota = this.quotaMapper.selectByPrimaryKey(id);
            WhgYwiQuota defaultQuota = this.findDefaultSet();
            if(StringUtils.isNotEmpty(quota.getSize())){
                size = Float.valueOf(defaultQuota.getSize());
            }else{
                size = Float.valueOf(quota.getSize());
            }
            Float usedsize = Float.valueOf(quota.getUsedsize()) / 1024 / 1024 / 1024;
            Float warningpercent = defaultQuota.getWarningpercent() / 100;

            if(usedsize/size >= warningpercent){
                Map<String, String> noticeparam = new HashMap<>();
                noticeparam.put("size", quota.getSize());
                    if (StringUtils.isNotEmpty(quota.getNoticephone())) {
                        // TODO: 2019/2/25 0025  发送短信配额通知
                        smsService.t_sendSMS(quota.getNoticephone(), "QUOTA_FULL_NOTICE", noticeparam, quota.getId());
                    }
                    if (StringUtils.isNotEmpty(quota.getNoticeemail())) {
                        // TODO: 2019/2/25 0025  发送配额通知邮件
                        EmailUtil.sendNoticeEmail(quota.getNoticeemail(), "您好，您的群文资源库使用空间即将达到上限，请及时备份资源并合理使用空间。");
                    }
                    WhgYwiQuota quota1 = new WhgYwiQuota();
                    quota1.setId(quota.getId());
                    quota1.setIssendnotice(true);
                    this.quotaMapper.updateByPrimaryKeySelective(quota);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public ResponseBean batchEdit(WhgYwiQuota quota, WhgSysUser sysUser, String cultids) throws Exception {
        ResponseBean responseBean = new ResponseBean();
        //判断修改空间是否小于实际使用
        String resultData = "";
        String cids[] = cultids.split(",");
        for (String cultid : cids) {
            quota.setMdfdate(new Date());
            quota.setMdfuser(sysUser.getId());
            quota.setCultid(cultid);
            WhgYwiQuota cultQuota = this.quotaMapper.selectByCult(cultid);
            if (cultQuota != null) {
                if (StringUtils.isNotEmpty(cultQuota.getUsedsize())) {
                    Float usedsize = Float.valueOf(cultQuota.getUsedsize()) / 1024 / 1024 / 1024;
                    if (Float.valueOf(quota.getSize()) < usedsize) {
                        resultData += (systemCultService.t_srchOne(quota.getCultid())).getName() + ",";
                        continue;
                    }
                }
                quota.setId(cultQuota.getId());
                quotaMapper.updateByPrimaryKeySelective(quota);
            } else {
                quota.setId(IDUtils.getID32());
                quotaMapper.insertSelective(quota);
            }
        }
        if (StringUtils.isNotEmpty(resultData)) {
            responseBean.setData(resultData.substring(0, resultData.length() - 1));
        }
        return responseBean;
    }


    public boolean checkUsedsize(String cultid, String size) {
        boolean result = true;
        WhgYwiQuota quota = this.quotaMapper.selectByCult(cultid);
        if (quota != null) {
            Float usedsize = Float.valueOf(quota.getUsedsize()) / 1024 / 1024 / 1024;
            if (Float.valueOf(size) < usedsize) {
                result = false;
            }
        }
        return result;
    }


    public WhgYwiQuota queryNoticetype(WhgYwiQuota quota) throws Exception {
        WhgYwiQuota quota1 = this.quotaMapper.selectByCult(quota.getCultid());
        return quota1;
    }

    public void editNoticetype(WhgYwiQuota quota, WhgSysUser sysUser) throws Exception {
        WhgYwiQuota quota1 = this.quotaMapper.selectByCult(quota.getCultid());
        quota.setMdfdate(new Date());
        quota.setMdfuser(sysUser.getId());
        if (quota1 == null) {
            quota.setId(IDUtils.getID32());
            this.quotaMapper.insertSelective(quota);
        } else {
            quota.setId(quota1.getId());
            this.quotaMapper.updateByPrimaryKeySelective(quota);
        }
    }


    /**
     * 分页查询分类列表信息
     *
     * @param
     */
    @Cacheable
    public PageInfo<WhgYwiQuotaVO> t_srchList4p(int page, int rows, WhgYwiQuotaVO quota, String userId) throws Exception {
        //开始分页
        WhgYwiQuota defaultQuota = this.findDefaultSet();
        float per = defaultQuota.getWarningpercent() / 100;
        PageHelper.startPage(page, rows);
        List<WhgYwiQuotaVO> voList = this.quotaMapper.selectQuotaList(per, quota.getName(), quota.getUsedstate());
        return new PageInfo<>(voList);
    }


    public void addQuotaRelation(String cultid, String ressize, int type) throws Exception {
        WhgYwiQuota quota = this.quotaMapper.selectByCult(cultid);
        if (quota == null) {
            quota = new WhgYwiQuota();
            quota.setCultid(cultid);
            quota.setUsedsize(ressize);
            quota.setId(IDUtils.getID32());
            quota.setIssendnotice(false);
            quotaMapper.insertSelective(quota);
        } else {
            if (type == 1) {
                //上传资源占用使用空间
                this.quotaMapper.updateAddUsedszeByCult(cultid, Integer.valueOf(ressize));
                if(quota.getIssendnotice())  return;
                sendQuotaFullNotice(quota.getId());
            } else {
                //释放使用空间
                this.quotaMapper.updateReduceUsedszeByCult(cultid, Integer.valueOf(ressize));
            }
        }
    }

    public void set_default(WhgYwiQuota quota, WhgSysUser sysUser) {
        quota.setMdfdate(new Date());
        quota.setMdfuser(sysUser.getId());
        this.quotaMapper.updateByPrimaryKeySelective(quota);
    }
}
