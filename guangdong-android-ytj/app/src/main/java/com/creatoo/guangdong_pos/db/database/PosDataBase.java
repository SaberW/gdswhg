package com.creatoo.guangdong_pos.db.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.creatoo.guangdong_pos.db.dao.UserDao;
import com.creatoo.guangdong_pos.db.entity.ClassEntity;
import com.creatoo.guangdong_pos.db.entity.UserEntity;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.db.database
 * @Description:
 * @date 2018/4/13
 */
@Database(entities = {UserEntity.class, ClassEntity.class},version = 1)
public abstract class PosDataBase extends RoomDatabase{

    public abstract PosDataBase infoDao();
    private static PosDataBase INSTANCE;
    private static final Object sLock = new Object();
    public static PosDataBase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        PosDataBase.class, "pos_database")
                        .build();
            }
            return INSTANCE;
        }
    }

    public abstract UserDao getUserDao();
}
