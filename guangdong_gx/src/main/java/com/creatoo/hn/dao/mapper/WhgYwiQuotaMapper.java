package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgYwiQuota;
import com.creatoo.hn.dao.vo.WhgYwiQuotaVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WhgYwiQuotaMapper extends Mapper<WhgYwiQuota> {

    @Select("select * from whg_ywi_quota t where t.cultid is null")
    WhgYwiQuota findDefaultDefaultSet();

    List<WhgYwiQuotaVO> selectQuotaList(@Param("per") float per,@Param("name") String name,@Param("usedstate") String usedstate);

    @Update("update whg_ywi_quota set usedsize = usedsize + #{ressize} where cultid = #{cultid}")
    void updateAddUsedszeByCult(@Param("cultid") String cultid, @Param("ressize") Integer ressize);

    @Update("update whg_ywi_quota set usedsize = usedsize - #{ressize} where cultid = #{cultid}")
    void updateReduceUsedszeByCult(@Param("cultid") String cultid, @Param("ressize") Integer ressize);

    @Select("select * from whg_ywi_quota t where t.cultid = #{0}")
    WhgYwiQuota selectByCult(String cultid);
}