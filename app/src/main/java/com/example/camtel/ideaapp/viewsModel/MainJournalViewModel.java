package com.example.camtel.ideaapp.viewsModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;

import java.util.List;

public class MainJournalViewModel extends AndroidViewModel {

    private static final String TAG = MainJournalViewModel.class.getSimpleName();

    private LiveData<List<Diary>> diaries;

    public MainJournalViewModel(@NonNull Application application) {
        super(application);
        DiaryDatabase database = DiaryDatabase.getsInstance(this.getApplication());
        Log.d(TAG,"Actively retrieving the diarues from the DataBase");
        diaries = database.diaryDAO().loadAllDiary();
    }

    public LiveData<List<Diary>> getDiaries() {
        return diaries;
    }
}
