package com.example.camtel.ideaapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camtel.ideaapp.dataBase.Diary;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalAdapterViewHolder> {
    private static final String TAG = JournalAdapter.class.getSimpleName();


    private final journalAdapterOnClickHandler mDiaryClickHandler;

    private static final String DATE_FORMAT = "dd/MM/yyy";

    private List<Diary> mDiaryEntries;
    private Context mContext;

    //date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());


    public interface journalAdapterOnClickHandler{
        void onLoadFinished(@NonNull Loader<String[]> loader, String[] data);

        void onDiaryClick(int elementId);

    }


    //constructor
    public JournalAdapter(Context context,journalAdapterOnClickHandler listener){
        mContext = context;
        mDiaryClickHandler=listener;
    }


    public class JournalAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{


        TextView mDiaryTitle;
        TextView mDiaryDescription;
        TextView mPriorityView;
        TextView updatedAtView;


        public JournalAdapterViewHolder(View view){
            super(view);
            mDiaryTitle =  view.findViewById(R.id.diaryTitle);
            mDiaryDescription = view.findViewById(R.id.diaryDescription);
            mPriorityView = view.findViewById(R.id.priorityTextView);
            updatedAtView = view.findViewById(R.id.diaryUpdatedAt);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = getAdapterPosition();
            mDiaryClickHandler.onDiaryClick(elementId);
        }
    }

    @Override
    public  JournalAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){

        //Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.journal_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediatly = false;

        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediatly);
        return new JournalAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(JournalAdapterViewHolder holder,int position){
        Diary diaryEntry = mDiaryEntries.get(position);

        String title = diaryEntry.getTitle();
        String description = diaryEntry.getDescription();
        String updatedAt = dateFormat.format(diaryEntry.getUpdatedAt());
        int priority = diaryEntry.getPriority();

        //set values
        holder.mDiaryTitle.setText(title);
        holder.mDiaryDescription.setText(description);
        holder.updatedAtView.setText(updatedAt);

        String priorityString = "" + priority; // converts int to String
        holder.mPriorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.mPriorityView.getBackground();
        // Get the appropriate background color based on the priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);
    }

    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch (priority) {
            case 1:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialRed);
                break;
            case 2:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialOrange);
                break;
            case 3:
                priorityColor = ContextCompat.getColor(mContext, R.color.materialYellow);
                break;
            default:
                break;
        }
        return priorityColor;
    }

    @Override
    public int getItemCount(){
        if (null==mDiaryEntries) return 0;
        return mDiaryEntries.size();
    }

    public void setDiary(List<Diary> diaryEntries){
        mDiaryEntries =diaryEntries;
        notifyDataSetChanged();
    }

    public List<Diary> getmDiaryEntries() {
        return mDiaryEntries;
    }
}