package com.example.camtel.ideaapp.dataBase;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

@Database(entities = {Diary.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DiaryDatabase extends RoomDatabase {

    private static final String LOG_TAG = DiaryDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME="journalApp";
    private static DiaryDatabase sInstance;

    public static DiaryDatabase getsInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                Log.d(LOG_TAG,"Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        DiaryDatabase.class,DiaryDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG,"Getting the database instance");
        return sInstance;
    }
    public abstract DiaryDAO diaryDAO();
}
