package com.creatoo.hn.services.api.apioutside;

import com.creatoo.hn.dao.mapper.api.ApiGlobalSearchMapper;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
@Service
public class ApiGlobalSearchService extends BaseService {

    @Autowired
    private ApiGlobalSearchMapper apiGlobalSearchMapper;

    /**
     * 全局搜索
     * @param page
     * @param pageSize
     * @param srchkey
     * @return
     * @throws Exception
     */
    public Object globalSearch(int page, int pageSize, String srchkey) throws Exception{
        ApiResultBean arb = new ApiResultBean();

        if (srchkey == null || srchkey.isEmpty()) {
            arb.setRows(new ArrayList());
            return arb;
        }

        PageHelper.startPage(page, pageSize);
        List list = this.apiGlobalSearchMapper.globalSearch(srchkey);

        PageInfo pageInfo = new PageInfo(list);
        arb.setPageInfo(pageInfo);
        arb.setRows(pageInfo.getList());

        return arb;
    }
}
