package com.example.camtel.ideaapp.viewsModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;

public class AddDiaryViewModel extends ViewModel {

    private LiveData<Diary> diary;

    public AddDiaryViewModel(DiaryDatabase database,int mDiaryId){
        diary = database.diaryDAO().loadDiaryById(mDiaryId);
    }

    public LiveData<Diary> getDiary() {
        return diary;
    }
}
