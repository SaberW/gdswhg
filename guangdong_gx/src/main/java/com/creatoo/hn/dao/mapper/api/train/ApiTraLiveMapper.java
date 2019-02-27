package com.creatoo.hn.dao.mapper.api.train;

import java.util.List;
import java.util.Map;

/**
 * Api-在线课程Mapper
 * Created by wangxl on 2017/10/17.
 */
public interface ApiTraLiveMapper {
    /**
     * 查询培训(在线课程)
     * @param condition 查询条件
     * @return
     */
    List<Map> queryAllLive(Map condition);

    /**
     * 根据视频直播录制的开始时间和appName,streamName获取相应的live配置信息
     * @param condition
     * @return
     */
    List<Map> queryLiveByTime(Map condition);
}
