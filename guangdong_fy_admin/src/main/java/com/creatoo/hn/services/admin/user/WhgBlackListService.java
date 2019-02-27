package com.creatoo.hn.services.admin.user;

import com.creatoo.hn.dao.mapper.WhgActivityOrderMapper;
import com.creatoo.hn.dao.mapper.WhgUserBlacklistMapper;
import com.creatoo.hn.dao.model.WhgActivity;
import com.creatoo.hn.dao.model.WhgActivityOrder;
import com.creatoo.hn.dao.model.WhgUserBlacklist;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.creatoo.hn.util.ReqParamsUtil;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * 黑名单services
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/26.
 */
@Service
@CacheConfig(cacheNames = "WhgBlackList", keyGenerator = "simpleKeyGenerator")
public class WhgBlackListService {
    @Autowired
    private WhgUserBlacklistMapper whgUserBlacklistMapper;

    @Autowired
    private WhgActivityOrderMapper whgActOrderMapper;

    @Cacheable
    public PageInfo<WhgUserBlacklist> t_srchList4p(HttpServletRequest request) {
        Map<String, Object> paramMap = ReqParamsUtil.parseRequest(request);
        int page = Integer.parseInt((String) paramMap.get("page"));
        int rows = Integer.parseInt((String) paramMap.get("rows"));


        String state = request.getParameter("state");
        Example example = new Example(WhgUserBlacklist.class);
        Example.Criteria c = example.createCriteria();

        if (!"".equals(state) && state != null) {
            c.andEqualTo("state", state);
        }
        String userphone = (String)paramMap.get("userphone");
        if(userphone != null && !"".equals(userphone)){
            c.andLike("userphone","%"+userphone+"%");
        }

        example.setOrderByClause("jointime desc");
        //开始分页
        PageHelper.startPage(page, rows);
        List<WhgUserBlacklist> typeList = this.whgUserBlacklistMapper.selectByExample(example);
        return new PageInfo<>(typeList);
    }

    /**
     * 取消黑名单
     * @param id
     */
    @CacheEvict(allEntries = true)
    public void t_cancel(String id, String userid)throws Exception {
        WhgUserBlacklist black = whgUserBlacklistMapper.selectByPrimaryKey(id);
        black.setState(0);
        whgUserBlacklistMapper.updateByPrimaryKeySelective(black);
        Example example = new Example(WhgActivityOrder.class);
        example.createCriteria().andEqualTo("userid",userid).andEqualTo("ticketstatus",3);
        List<WhgActivityOrder> order = whgActOrderMapper.selectByExample(example);
        if(order.size() >0){
            for(int i=0; i<order.size(); i++){
                order.get(i).setOrderisvalid(3);
                whgActOrderMapper.updateByPrimaryKey(order.get(i));
            }
        }

    }
}
