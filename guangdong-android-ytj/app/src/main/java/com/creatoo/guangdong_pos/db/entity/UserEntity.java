package com.creatoo.guangdong_pos.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.db.entity
 * @Description:
 * @date 2018/4/13
 */
@Entity(tableName = "user_info"
        ,indices = @Index(value = {"name"},unique = true)
        ,foreignKeys = {@ForeignKey(entity = ClassEntity.class,parentColumns = "id",childColumns = "class_id")})
public class UserEntity {
    @PrimaryKey
    private long id;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "class_id")
    private String classId;

    @Ignore
    private String remark;
}
