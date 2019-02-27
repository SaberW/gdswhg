package com.creatoo.hn.services.comm;

import com.creatoo.hn.dao.mapper.WhgYwiConfigMapper;
import com.creatoo.hn.dao.model.WhgYwiConfig;
import com.creatoo.hn.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
@CacheConfig(cacheNames = "WhgYwiConfig", keyGenerator = "simpleKeyGenerator")
public class YwiConfigService extends BaseService {

    @Autowired
    private WhgYwiConfigMapper whgYwiConfigMapper;

    //阿里云短信分类常量
    public static final String CFGTYPE_ALIYUNSMS = "aliyunsmscfg";

    @CacheEvict(allEntries = true)
    public void saveConfigList(List<WhgYwiConfig> list) throws Exception{
        for(WhgYwiConfig info : list){
            try {
                this.saveConfigItem(info);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @CacheEvict(allEntries = true)
    public void saveConfigItem(WhgYwiConfig info) throws Exception{
        WhgYwiConfig cfg = this.whgYwiConfigMapper.selectByPrimaryKey(info.getCfgkey());
        if (cfg!=null){
            this.whgYwiConfigMapper.updateByPrimaryKeySelective(info);
        }else{
            this.whgYwiConfigMapper.insert(info);
        }
    }

    @Cacheable
    public Map getConfigMap4Type(String cfgtype) throws Exception{
        Map resmap = new HashMap();
        if (cfgtype==null){
            return resmap;
        }

        WhgYwiConfig record = new WhgYwiConfig();
        record.setCfgtype(cfgtype);
        List<WhgYwiConfig> list = this.whgYwiConfigMapper.select(record);
        if (list == null || list.size()==0){
            return resmap;
        }
        for(WhgYwiConfig cfg : list){
            resmap.put(cfg.getCfgkey(), cfg.getCfgvalue());
        }

        return resmap;
    }

    @Cacheable
    public String getConfigValue(String cfgkey){
        return this.getConfigValue(cfgkey, null);
    }
    @Cacheable
    public String getConfigValue(String cfgkey, String cfgtype){
        return this.getConfigValue(cfgkey, cfgtype, null);
    }
    @Cacheable
    public String getConfigValue(String cfgkey, String cfgtype, String defval){
        if (cfgkey==null){
            return defval;
        }
        WhgYwiConfig record = new WhgYwiConfig();
        record.setCfgkey(cfgkey);
        if (cfgtype!=null) {
            record.setCfgtype(cfgtype);
        }
        List<WhgYwiConfig> list = this.whgYwiConfigMapper.select(record);
        if (list == null || list.size()==0){
            return defval;
        }
        record = list.get(0);
        if (record.getCfgvalue()!=null){
            defval = record.getCfgvalue();
        }
        return defval;
    }
}
