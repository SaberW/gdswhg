package com.creatoo.guangdong_pos.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.db.entity
 * @Description:
 * @date 2018/4/13
 */
@Entity(tableName = "class_info")
public class ClassEntity {
    @PrimaryKey
    private long id;
}
