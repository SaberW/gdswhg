package com.creatoo.hn.services.comm;

import com.creatoo.hn.dao.mapper.WhgYwiTagMapper;
import com.creatoo.hn.dao.mapper.WhgYwiTypeMapper;
import com.creatoo.hn.dao.model.WhgYwiTag;
import com.creatoo.hn.dao.model.WhgYwiType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
@Service
public class ResetRefidService {

    @Autowired
    private WhgYwiTypeMapper whgYwiTypeMapper;

    @Autowired
    private WhgYwiTagMapper whgYwiTagMapper;

    /**
     * type转换
     * @param info
     * @param keys
     */
    public void resetRefid4type(Map info, String... keys){
        if (info == null) {
            return;
        }
        if (keys == null || keys.length == 0) {
            return;
        }

        for(String key : keys){
            try {
                if (!info.containsKey(key)) {
                    continue;
                }
                String value = (String) info.get(key);
                if (value == null || value.isEmpty()) {
                    continue;
                }

                boolean isSplit = value.contains(",");
                if (isSplit) {
                    String[] values = value.split("\\s*,\\s*");
                    Example texp = new Example(WhgYwiType.class);
                    texp.createCriteria().andIn("id", Arrays.asList(values));
                    texp.orderBy("idx");
                    List<WhgYwiType> list = this.whgYwiTypeMapper.selectByExample(texp);
                    StringBuilder sb = new StringBuilder();
                    if (list!= null && list.size()>0){
                        for (WhgYwiType ent : list) {
                            if (sb.length()>0){
                                sb.append(",");
                            }
                            sb.append(ent.getName());
                        }
                    }
                    info.put(key, sb.toString());
                }else{
                    WhgYwiType type = this.whgYwiTypeMapper.selectByPrimaryKey(value);
                    if (type != null) {
                        info.put(key, type.getName());
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    /**
     * tag转换
     * @param info
     * @param keys
     */
    public void resetRefid4tag(Map info, String... keys){
        if (info == null) {
            return;
        }
        if (keys == null || keys.length == 0) {
            return;
        }

        for(String key : keys){
            try {
                if (!info.containsKey(key)) {
                    continue;
                }
                String value = (String) info.get(key);
                if (value == null || value.isEmpty()) {
                    continue;
                }

                boolean isSplit = value.contains(",");
                if (isSplit) {
                    String[] values = value.split("\\s*,\\s*");
                    Example tagexp = new Example(WhgYwiTag.class);
                    tagexp.createCriteria().andIn("id",Arrays.asList(values));
                    tagexp.orderBy("idx");
                    List<WhgYwiTag> list = this.whgYwiTagMapper.selectByExample(tagexp);
                    StringBuilder sb = new StringBuilder();
                    if (list!= null && list.size()>0){
                        for (WhgYwiTag ent : list) {
                            if (sb.length()>0){
                                sb.append(",");
                            }
                            sb.append(ent.getName());
                        }
                    }
                    info.put(key, sb.toString());
                }else{
                    WhgYwiTag type = this.whgYwiTagMapper.selectByPrimaryKey(value);
                    if (type != null) {
                        info.put(key, type.getName());
                    }
                }
            } catch (Exception e) {
                continue;
            }
        }
    }
}
