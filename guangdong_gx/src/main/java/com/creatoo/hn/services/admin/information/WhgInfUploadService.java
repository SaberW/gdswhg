package com.creatoo.hn.services.admin.information;

import com.creatoo.hn.dao.mapper.WhgInfUploadMapper;
import com.creatoo.hn.dao.model.WhgInfUpload;
import com.creatoo.hn.services.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@CacheConfig(cacheNames = "WhgInfUpload", keyGenerator = "simpleKeyGenerator")
public class WhgInfUploadService extends BaseService {

    @Autowired
    public WhgInfUploadMapper whgInfUploadMapper;

    /**
     * 查询
     *
     * @param param
     * @return
     */
    public Map<String, Object> inquire(Map<String, Object> param) {
        //分页信息
        int page = Integer.parseInt((String) param.get("page"));
        int rows = Integer.parseInt((String) param.get("rows"));

        PageHelper.startPage(page, rows);
        Example example = new Example(WhgInfUpload.class);
        Criteria criteria = example.createCriteria();
        //排序
        if (param.containsKey("sort") && param.get("sort") != null) {
            StringBuffer sbf = new StringBuffer((String) param.get("sort"));
            if (param.containsKey("order") && param.get("order") != null) {
                sbf.append(" ").append(param.get("order"));
            }
            example.setOrderByClause(sbf.toString());
        } else {
            example.setOrderByClause("uptime desc");
        }
        //类型
        if (param.containsKey("uptype") && param.get("uptype") != null) {
            String uptype = (String) param.get("uptype");
            if (!"".equals(uptype.trim())) {
                criteria.andEqualTo("uptype", uptype);
            }
        }
        List<WhgInfUpload> list = this.whgInfUploadMapper.selectByExample(example);

        PageInfo<WhgInfUpload> pageInfo = new PageInfo<WhgInfUpload>(list);
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", pageInfo.getTotal());
        rtnMap.put("rows", pageInfo.getList());
        return rtnMap;
    }


    /**
     * 下载列表
     *
     * @param id
     * @return
     */
    public List<WhgInfUpload> selecup(String id) throws Exception {
        Example example = new Example(WhgInfUpload.class);
        Criteria Criteria = example.createCriteria();
        Criteria.andEqualTo("uptype", id);
        Criteria.andEqualTo("upstate", 1);
        List<WhgInfUpload> whup = this.whgInfUploadMapper.selectByExample(example);
        return whup;

    }

    /**
     * 添加
     *
     * @param whup
     */
    public void save(WhgInfUpload whup) {
        this.whgInfUploadMapper.insert(whup);
    }

    /**
     * 更新
     *
     * @param whup
     */
    public void updata(WhgInfUpload whup) {
        this.whgInfUploadMapper.updateByPrimaryKeySelective(whup);

    }

    public WhgInfUpload select(String upid) {
        return this.whgInfUploadMapper.selectByPrimaryKey(upid);
    }

    /**
     * 删除
     *
     * @param upid
     */
    @SuppressWarnings("unused")
    public void delete(String upid) {
        int id = this.whgInfUploadMapper.deleteByPrimaryKey(upid);
    }

    /**
     * 改变状态
     *
     * @param whup
     */
    public void checkup(WhgInfUpload whup) {
        this.whgInfUploadMapper.updateByPrimaryKeySelective(whup);
    }

}
