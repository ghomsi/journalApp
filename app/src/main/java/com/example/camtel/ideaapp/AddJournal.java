package com.example.camtel.ideaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;
import com.example.camtel.ideaapp.viewsModel.AddDiaryViewModel;
import com.example.camtel.ideaapp.factory.AddDiaryViewModelFactory;

import java.util.Date;

public class AddJournal extends AppCompatActivity {

    public static final String EXTRA_DIARY_ID = "extra1DiaryId";
    public static final String INSTANCE_DIARY_ID = "instance1DiaryId";
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    // Constant for default DIARY id to be used when not in update mode
    private static final int DEFAULT_DIARY_ID = -1;

    private static final String TAG = AddJournal.class.getSimpleName();


    private RadioGroup mRadioGroup;
    private Button mButton;
    private EditText mTitle;
    private EditText mDescription;


    private DiaryDatabase mDb;


    private int mDiaryId = DEFAULT_DIARY_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_journal);

        initViews();

        mDb = DiaryDatabase.getsInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_DIARY_ID)) {
            mButton.setText(R.string.update_button);
            if (mDiaryId == DEFAULT_DIARY_ID){
                mDiaryId = intent.getIntExtra(EXTRA_DIARY_ID,DEFAULT_DIARY_ID);

                AddDiaryViewModelFactory factory = new AddDiaryViewModelFactory(mDb,mDiaryId);
                final AddDiaryViewModel viewModel
                        = ViewModelProviders.of(this,factory).get(AddDiaryViewModel.class);
                viewModel.getDiary().observe(this, new Observer<Diary>() {
                    @Override
                    public void onChanged(@Nullable Diary diaryEntry) {
                        viewModel.getDiary().removeObserver(this);
                        Log.d(TAG,"Receiving diary from LiveData in ViewModel");
                        populateUI(diaryEntry);
                    }
                });
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(INSTANCE_DIARY_ID, (int) mDiaryId);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initViews(){
        mTitle = (EditText) findViewById(R.id.editDiarryTitle);
        mDescription = (EditText) findViewById(R.id.editDiarryDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.save_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSave();
            }
        });



    }
    public void onSave() {
        String title = mTitle.getText().toString();
        String description = mDescription.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        final Diary diaryEntry = new Diary(title,description, priority, date);
        JournalExecutors.getsInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(mDiaryId==DEFAULT_DIARY_ID){
                    mDb.diaryDAO().insertDiary(diaryEntry);
                }else{
                    diaryEntry.setId((int) mDiaryId);
                    mDb.diaryDAO().updateDiary(diaryEntry);
                }
                finish();
            }
        });

    }

    private void populateUI(Diary diary) {
        if(diary==null){
            return;
        }
        mTitle.setText(diary.getTitle());
        mDescription.setText(diary.getDescription());
        setPriorityInViews(diary.getPriority());

    }
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }




    /*public class diaryAddTask extends AsyncTask<URL, Void, Long> {

        private View mView;
        private String title;
        private  String description;

        public diaryAddTask(View view,String title,String description){
            this.mView = view;
            this.title=title;
            this.description=description;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(URL... urls) {
            mLoadingindicator.setVisibility(View.VISIBLE);

            Diary diary = new Diary(title,description);

            diaryDB.open();
            Long i = diaryDB.insertDiary(diary);

            return i;

        }

        @Override
        protected void onPostExecute(Long i) {
            super.onPostExecute(i);
            mLoadingindicator.setVisibility(View.INVISIBLE);
            diaryId=i;
            if(null!=i){
                showLayoutView();
                Snackbar.make(mView, "Diary Added!", Snackbar.LENGTH_LONG).show();
            }else{
                //showErrorMessage();
                Snackbar.make(mView, "Diary Not Added: Connection problem!", Snackbar.LENGTH_LONG).show();
            }
        }

    }*/
}
