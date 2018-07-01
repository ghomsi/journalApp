package com.example.camtel.ideaapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

import com.example.camtel.ideaapp.data.JournalDB;
import com.example.camtel.ideaapp.dataBase.Diary;
import com.example.camtel.ideaapp.dataBase.DiaryDatabase;
import com.example.camtel.ideaapp.utilities.NetworkUtils;
import com.example.camtel.ideaapp.viewsModel.MainJournalViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        JournalAdapter.journalAdapterOnClickHandler, LoaderCallbacks<String[]> {


    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int JOURNAL_LOADER_ID = 0;

    private static final String SEARCH_QUERY_URL_EXTRA = "query";

    private static final String SEARCH_RESULTS_RAW_JSON="results";

    private JournalAdapter mJournalAdapter;
    private RecyclerView mRecyclerView;

    private TextView mUrlDisplayTextView;
    private TextView mSearchResultsTextView;
    private TextView mErrorMessageTextView;

    private Toast mToast;

    private EditText mSearchBoxEditText;
    private ProgressBar mLoadingindicator;

    private DiaryDatabase mDb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = MainActivity.this;

                Class destinationActivity = AddJournal.class;
                Intent intent = new Intent(context,destinationActivity);

                //String message = "add clicked";
               // Toast.makeText(context,message,Toast.LENGTH_LONG).show();

                startActivity(intent);


            }
        });

        initViews();

        mDb = DiaryDatabase.getsInstance(getApplicationContext());

        setupViewModel();


        //save instance
        if(savedInstanceState!=null){
            String queryUrl = savedInstanceState.getString(SEARCH_QUERY_URL_EXTRA);

            mUrlDisplayTextView.setText(queryUrl);
        }



        int loaderId = JOURNAL_LOADER_ID;

        LoaderCallbacks<String[]> callback = (LoaderCallbacks<String[]>) MainActivity.this;
        //initialize loader

        Bundle bundleForloader =null;

        getSupportLoaderManager().initLoader(loaderId,bundleForloader,callback);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void setupViewModel() {
        MainJournalViewModel viewModel = ViewModelProviders.of(this).get(MainJournalViewModel.class);
        viewModel.getDiaries().observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(@Nullable List<Diary> diaries) {
                Log.d(TAG,"Updating list of diary from LiveData in ViewModel");
                mJournalAdapter.setDiary(diaries);
            }
        });

    }

    private void initViews(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_journalList);


        mSearchBoxEditText = (EditText) findViewById(R.id.jl_search_box);

        mLoadingindicator = (ProgressBar) findViewById(R.id.pd_loading_indicator);



        //scroll view content
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mJournalAdapter = new JournalAdapter(this,this);

        mRecyclerView.setAdapter(mJournalAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(this, VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                JournalExecutors.getsInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<Diary> diarys = mJournalAdapter.getmDiaryEntries();
                        mDb.diaryDAO().deleteDiary(diarys.get(position));
                    }
                });

            }

        }).attachToRecyclerView(mRecyclerView);
    }




    private void showJsonDataView(){
    }

    private void showErrorMessage(){
    }

    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int id, @Nullable final Bundle args) {
        return (Loader<String[]>) new AsyncTaskLoader<String[]>(this) {

            String[] mJournalJson=null;

            @Override
            protected void onStartLoading() {

                if(mJournalJson!=null){
                    deliverResult(mJournalJson);
                }else{
                    mLoadingindicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {
                String locationQuery = JournalDB.getDbUrl(MainActivity.this);

                URL journalRequestUrl = NetworkUtils.buildUrl(locationQuery);

                /*if(searchQueryUrlString==null || TextUtils.isEmpty(searchQueryUrlString)){
                    return null;
                }*/

                /*try {
                    String journalSearchResults = NetworkUtils
                            .getResponseFromHttpurl(journalRequestUrl);

                    String[] journalJsonData = OpenJournalJsonUtils
                            .getSimpleJournalStringsFromJson(MainActivity.this,journalSearchResults);

                    return journalJsonData;
                }catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }*/
                return null;
            }

            @Override
            public void deliverResult(@Nullable String[] data) {
                mJournalJson = data;
                super.deliverResult(data);
            }
        };
    }



    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] data) {

        mLoadingindicator.setVisibility(View.INVISIBLE);

        if(null == data){

        }else{

        }

    }

    @Override
    public void onDiaryClick(int elementId) {
        Context context = MainActivity.this;

        Class destinationActivity = DetailJournal.class;
        Intent detailIntent = new Intent(context,destinationActivity);
        detailIntent.putExtra(DetailJournal.EXTRA_DIARY_ID,elementId);
        startActivity(detailIntent);
    }

    /*private void invalidateData(){
        mJournalAdapter.setJournalData(null);
    }*/


    public class JournalQueryTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String journalSearchResults = null;
            try {
                journalSearchResults = NetworkUtils.getResponseFromHttpurl(searchUrl);
            }catch (IOException e){
                e.printStackTrace();
            }
            return journalSearchResults;

        }

        @Override
        protected void onPostExecute(String s) {
            mLoadingindicator.setVisibility(View.INVISIBLE);
            if(s != null && s.equals("")){
                showJsonDataView();
            }else{
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Context context = MainActivity.this;
            Toast.makeText(context,"In construction :)",Toast.LENGTH_LONG).show();
            return true;
        }
        if(id==R.id.action_search){
            //invalidateData();
            //makeJournalSearchQuery();
            Context context = MainActivity.this;
            Toast.makeText(context,"In construction :)",Toast.LENGTH_LONG).show();

            return true;
        }
        if(id==R.id.action_about){

            Context context = MainActivity.this;

            Class destinationActivity = About.class;
            Intent aboutIntent = new Intent(context,destinationActivity);
            startActivity(aboutIntent);

        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }
}
