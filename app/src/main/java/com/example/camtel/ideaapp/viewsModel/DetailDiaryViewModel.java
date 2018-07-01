package com.example.camtel.ideaapp.viewsModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;

public class DetailDiaryViewModel extends ViewModel {

    private LiveData<Diary> diary;

    public DetailDiaryViewModel(DiaryDatabase database, int mDiaryId){
        Log.d(DetailDiaryViewModel.class.getSimpleName(),"Receiving diary id:"+mDiaryId);
        diary = database.diaryDAO().loadDiaryById(mDiaryId);
    }
    public LiveData<Diary> getDiary() {
        return diary;
    }
}
