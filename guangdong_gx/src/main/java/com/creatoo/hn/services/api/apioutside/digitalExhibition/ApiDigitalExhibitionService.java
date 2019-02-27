package com.creatoo.hn.services.api.apioutside.digitalExhibition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.creatoo.hn.dao.mapper.*;
import com.creatoo.hn.dao.mapper.api.ApiActivityOrderMapper;
import com.creatoo.hn.dao.mapper.api.ApiActivityTicketMapper;
import com.creatoo.hn.dao.mapper.api.ApiActivityTimeMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.services.admin.gather.WhgGatherService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.user.WhgBlackListService;
import com.creatoo.hn.services.api.apiinside.InsMessageService;
import com.creatoo.hn.services.comm.SMSService;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumOrderType;
import com.creatoo.hn.util.enums.EnumProject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * 文化活动
 *
 * @author yanjianbo
 * @version 2016.11.16
 */

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgDigitalExhibition", keyGenerator = "simpleKeyGenerator")
public class ApiDigitalExhibitionService extends BaseService {


    @Autowired
    private WhgDigitalExhibitionMapper whgDigitalExhibitionMapper;


    private final String SUCCESS = "0";//成功


    public WhgDigitalExhibition getExhDetail(String actvid) {
        WhgDigitalExhibition whgActivity = this.whgDigitalExhibitionMapper.selectByPrimaryKey(actvid);
        return whgActivity;
    }


    public PageInfo getDigitalExhList(String pageNo, String pageSize, Map map) {
        try {
            Example exp = new Example(WhgDigitalExhibition.class);
            Example.Criteria ca = exp.createCriteria();
            if (map != null) {
                if (map.get("cultids") != null) {
                    ca.andIn("cultid", (List) map.get("cultids"));
                }
                if (map.get("arttype") != null) {
                    ca.andLike("arttype", "%" + map.get("arttype") + "%");
                }
                if (map.get("province") != null) {
                    ca.andEqualTo("province", map.get("province"));
                }
                if (map.get("city") != null) {
                    ca.andEqualTo("city", map.get("city"));
                }
                if (map.get("area") != null) {
                    ca.andEqualTo("area", map.get("area"));
                }
                if (map.get("keywords") != null) {
                    ca.andLike("title", "%" + map.get("keywords") + "%");
                }
            }
            ca.andEqualTo("state", 6);
            ca.andEqualTo("delstate", 0);
            exp.orderBy("publishdate").desc();
            PageHelper.startPage(Integer.parseInt(pageNo), Integer.parseInt(pageSize));
            List list = this.whgDigitalExhibitionMapper.selectByExample(exp);
            return new PageInfo(list);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

}
