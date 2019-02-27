package com.creatoo.hn.services.admin.supply;

import com.creatoo.hn.dao.mapper.WhgSupplyTimeMapper;
import com.creatoo.hn.dao.model.WhgSupplyTime;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SuppressWarnings("ALL")
@Service
public class WhgSupplyTimesService extends BaseService {

    @Autowired
    private WhgSupplyTimeMapper whgSupplyTimeMapper;

    public PageInfo srchList4p(int page, int size, WhgSupplyTime record) throws Exception{
        Example exp = new Example(record.getClass());
        Example.Criteria c = exp.or();
        if (record != null){
            if (record.getPscity()!=null && !record.getPscity().isEmpty()){
                c.andLike("pscity", "%"+record.getPscity()+"%");
                record.setPscity(null);
            }

            if (record.getSupplytype()!=null && record.getSupplytype().isEmpty()){
                record.setSupplytype(null);
            }

            c.andEqualTo(record);
        }
        exp.orderBy("timestart").desc();

        PageHelper.startPage(page, size);
        List<WhgSupplyTime> list = this.whgSupplyTimeMapper.selectByExample(exp);
        return new PageInfo(list);
    }

    public void saveSupplyTime(WhgSupplyTime info) throws Exception{
        if (info.getId()==null){
            info.setId(IDUtils.getID32());
        }
        this.whgSupplyTimeMapper.insert(info);
    }

    public void editSupplyTime(WhgSupplyTime info) throws Exception{
        this.whgSupplyTimeMapper.updateByPrimaryKeySelective(info);
    }

    public void removeSupplyTime(String id) throws Exception{
        if (id == null){
            return;
        }
        this.whgSupplyTimeMapper.deleteByPrimaryKey(id);
    }

    public void clearSupplyTimes4Supplyid(String supplyid, String supplytype) throws Exception{
        if (supplyid == null){
            return;
        }

        WhgSupplyTime record = new WhgSupplyTime();
        record.setSupplyid(supplyid);
        if (supplytype!=null && !supplytype.isEmpty()){
            record.setSupplytype(supplytype);
        }

        this.whgSupplyTimeMapper.delete(record);
    }
}
