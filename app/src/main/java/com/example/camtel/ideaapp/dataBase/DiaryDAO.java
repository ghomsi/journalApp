package com.example.camtel.ideaapp.dataBase;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DiaryDAO {
    @Query("SELECT * FROM diary ORDER BY priority")
    LiveData<List<Diary>> loadAllDiary();

    @Insert
    void insertDiary(Diary diary);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDiary(Diary diary);

    @Delete
    void deleteDiary(Diary diary);

    @Query("SELECT * FROM diary WHERE id = :id")
    LiveData<Diary> loadDiaryById(int id);

    @Query("SELECT * FROM diary WHERE id = :id")
    Diary loadDiarById(int id);

}
