package com.example.camtel.ideaapp.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.camtel.ideaapp.dataBase.DiaryDatabase;
import com.example.camtel.ideaapp.viewsModel.AddDiaryViewModel;

public class AddDiaryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final DiaryDatabase mDb;
    private final  int mDiaryIb;

    public AddDiaryViewModelFactory(DiaryDatabase mDb, int mDiaryIb){

        this.mDb = mDb;
        this.mDiaryIb = mDiaryIb;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddDiaryViewModel(mDb,mDiaryIb);
    }
}
