package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgUsrWeixin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**微信端用户DAO
 * Created by caiyong on 2017/5/8.
 */
@SuppressWarnings("all")
public interface CrtWeChatMapper {
    List<WhgUsrWeixin> queryWhWeChat(@Param("whgUsrWeixin") WhgUsrWeixin whgUsrWeixin);
}
