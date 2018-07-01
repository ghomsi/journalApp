package com.example.camtel.ideaapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;
import com.example.camtel.ideaapp.factory.DetailDiaryViewModelFactory;
import com.example.camtel.ideaapp.viewsModel.DetailDiaryViewModel;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DetailJournal extends AppCompatActivity {

    private static final String TAG = DetailJournal.class.getSimpleName();

    public static final String EXTRA_DIARY_ID = "extra0DiaryId";
    private static final int DEFAULT_DIARY_ID = -1;
    private int mDiaryId;
    public static final String INSTANCE_DIARY_ID = "instance0DiaryId";

    private TextView mDisplayTitle;
    private TextView mDisplayDescription;
    private TextView mDisplayUpdatedAt;

    private static final String DATE_FORMAT = "dd/MM/yyy";

    //date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    private DiaryDatabase mDb;


    private static final String JOURNAL_SHARE_HASHTAG ="#DevJournalApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_journal);

        initViews();

        mDb = DiaryDatabase.getsInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_DIARY_ID)) {
            mDiaryId = savedInstanceState.getInt(INSTANCE_DIARY_ID, DEFAULT_DIARY_ID);
        }

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity!=null && intentThatStartedThisActivity.hasExtra(EXTRA_DIARY_ID)){
            if(mDiaryId==DEFAULT_DIARY_ID){
                mDiaryId = intentThatStartedThisActivity.getIntExtra(EXTRA_DIARY_ID,DEFAULT_DIARY_ID);
                Log.d(TAG,"Receiving diary id:"+mDiaryId);
                DetailDiaryViewModelFactory factory = new DetailDiaryViewModelFactory(mDb,mDiaryId);
                final DetailDiaryViewModel viewModel =
                        ViewModelProviders.of(this,factory).get(DetailDiaryViewModel.class);
                viewModel.getDiary().observe(this, new Observer<Diary>() {
                    @Override
                    public void onChanged(@Nullable Diary diary) {
                        Log.d(TAG,"Receiving diary from LiveData in ViewModel");
                        populateUI(diary);
                    }
                });
            }
        }
    }

    private void populateUI(Diary diary) {
        if(diary==null){
            Log.d(TAG,"diary null");
            return;

        }
        Log.d(TAG,"diary ok");
        mDiaryId = diary.getId();
        mDisplayTitle.setText(diary.getTitle());
        mDisplayDescription.setText(diary.getDescription());
        mDisplayUpdatedAt.setText(dateFormat.format(diary.getUpdatedAt()));
    }


    private void initViews() {
        mDisplayTitle = findViewById(R.id.jl_display_title);
        mDisplayDescription =  findViewById(R.id.jl_display_description);
        mDisplayUpdatedAt = findViewById(R.id.jl_display_UpdatedAt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //MenuItem menuItem = menu.findItem(R.id.action_share);
        if (id == R.id.action_share) {
             String title = "#"+mDisplayTitle.getText().toString().trim();
             String description = mDisplayDescription.getText().toString();
             if(TextUtils.isEmpty(title)){
                 title = "#letenjoy";
                 description = "thk to Andela Community";
             }
            item.setIntent(shareText(title,description));
            //return true;
        }
        if(id == R.id.action_update){
            Context context = DetailJournal.this;

            Class destinationActivity = AddJournal.class;
            Intent addIntent = new Intent(context,destinationActivity);
            addIntent.putExtra(AddJournal.EXTRA_DIARY_ID,mDiaryId);
            startActivity(addIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private Intent shareText(String title, String textToShare){
        String minetype = "text/plain";


        Intent shareintent = ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(minetype)
                .setText(textToShare + JOURNAL_SHARE_HASHTAG).getIntent();
        return shareintent;
    }

}
