package com.creatoo.guangdong_pos.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.creatoo.guangdong_pos.db.entity.UserEntity;

import java.util.List;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.db.dao
 * @Description:
 * @date 2018/4/13
 */
@Dao
public interface UserDao {
    @Query("select * from user_info")
    List<UserEntity> getAll();

    @Query("select * from user_info where id in (:ids)")
    List<UserEntity> getAllById(long [] ids);

    @Insert
    void insert(UserEntity... entities);

    @Delete
    void delete(UserEntity entity);

    @Update
    void update(UserEntity entity);
}
