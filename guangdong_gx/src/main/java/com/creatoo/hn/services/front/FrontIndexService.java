package com.creatoo.hn.services.front;

import com.creatoo.hn.dao.model.WhgSysRole;
import com.creatoo.hn.services.BaseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页业务服务类
 * Created by wangXL on 2017/7/12.
 */
@Service
@CacheConfig(cacheNames = "temp_WhgSysRole", keyGenerator = "simpleKeyGenerator")
public class FrontIndexService extends BaseService {
    @Cacheable
    public WhgSysRole get(String id)throws Exception{

        //存入缓存
        this.addCache("WhgSysRole", "wxl", "wagnxl123");
        return new WhgSysRole();
    }

    @Cacheable
    public List<WhgSysRole> findRoles(int page, int pageSize, String sort, String order, WhgSysRole role)throws Exception{
        /*Object obj = this.getCache("WhgSysRole", "wxl");
        System.out.println("==========缓存obj:"+obj+"===================");*/
        Example example = new Example(WhgSysRole.class);
        example.setOrderByClause("crtdate desc");

        PageHelper.startPage(page, 2);
        List<WhgSysRole> roles = new ArrayList<>();
        PageInfo<WhgSysRole> pageInfo = new PageInfo<>(roles);
        return pageInfo.getList();
    }

    @CacheEvict(allEntries = true)
    public WhgSysRole add()throws Exception{
        WhgSysRole role = new WhgSysRole();
        role.setId("ROLE20170712001_2");
        role.setName("ROLE20170712001");
        role.setCrtdate(new Date());
        role.setCrtuser("1");
        role.setState(1);
        //role.setDelstate(0);
        int row = 0;
        if(row  != 1){
            throw new Exception("add Error");
        }
        return role;
    }

    @CacheEvict(allEntries = true)
    public WhgSysRole edit(WhgSysRole role)throws Exception{
        int row = 1;//
        if(row  != 1){
            throw new Exception("edit Error");
        }
        return role;
    }
}
