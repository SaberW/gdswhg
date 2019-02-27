package com.creatoo.guangdong_pos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.creatoo.guangdong_pos.db.entity.ClassEntity;

import java.util.List;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.db.dao
 * @Description:
 * @date 2018/4/13
 */
@Dao
public interface ClassDao {
    @Query("select * from class_info")
    List<ClassEntity> getAll();

    @Query("select * from class_info where id in (:ids)")
    List<ClassEntity> getAllById(long [] ids);

    @Insert
    void insert(ClassEntity... entities);

    @Delete
    void delete(ClassEntity entity);

    @Update
    void update(ClassEntity entity);
}
