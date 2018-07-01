package com.example.camtel.ideaapp.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.camtel.ideaapp.dataBase.DiaryDatabase;
import com.example.camtel.ideaapp.viewsModel.DetailDiaryViewModel;

public class DetailDiaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DiaryDatabase mDb;
    private final  int mDiaryId;

    public  DetailDiaryViewModelFactory(DiaryDatabase mDb, int mDiaryId){

        this.mDb = mDb;
        this.mDiaryId = mDiaryId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailDiaryViewModel(mDb,mDiaryId);
    }
}
