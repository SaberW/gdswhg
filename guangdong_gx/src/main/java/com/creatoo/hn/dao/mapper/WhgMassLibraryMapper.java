package com.creatoo.hn.dao.mapper;

import com.creatoo.hn.dao.model.WhgMassLibrary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface WhgMassLibraryMapper extends Mapper<WhgMassLibrary> {
    /**
     *
     * @param param
     * @return
     */
    List t_whgsrchList(Map param);

    List apiMassLibraryPage(Map param);

    List queryLibByApplyUserId(@Param("userid") String userid, @Param("resourcetype") String type);


    List apply_user_list(Map map);

    @Insert("insert into whg_mass_resource_url_tmp (resurl,ressize) values(#{resurl},#{ressize})")
    void saveTxtUrl(@Param("ressize") long fileSize, @Param("resurl") String lineTxt);

    @Select("select * from whg_mass_resource_url_tmp where resurl like '${resurl}' ")
    Map getResUrl(@Param("resurl") String s);
}