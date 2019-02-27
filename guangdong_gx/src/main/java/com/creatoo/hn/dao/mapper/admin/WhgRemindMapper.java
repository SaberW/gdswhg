package com.creatoo.hn.dao.mapper.admin;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/1.
 */
public interface WhgRemindMapper {

    /**
     * 内部供需
     * @param param
     * @return
     */
    List t_nbgxsrchList4p(Map param);

    /**
     * 外部供需
     * @param param
     * @return
     */
    List t_wbgxsrchList(Map param);

    /**
     * 网络培训
     * @param param
     * @return
     */
    List t_wlpxsrchList(Map param);

    /**
     * 群文
     * @param param
     * @return
     */
    List t_massSrchList(Map param);
}
