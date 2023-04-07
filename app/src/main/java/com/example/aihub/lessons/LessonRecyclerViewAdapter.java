package com.example.aihub.lessons;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aihub.R;
import com.example.aihub.SublessonsActivity;
import com.example.aihub.dataobjects.Lessons;
import com.google.android.material.card.MaterialCardView;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class LessonRecyclerViewAdapter extends RecyclerView.Adapter<LessonRecyclerViewAdapter.ViewHolder> {

   private final Context context;
    private List<Lessons> lessonsList;
    private static Map<String,String> progressMap = Collections.emptyMap();


    public LessonRecyclerViewAdapter(Context context, List<Lessons> lessonsList, Map<String,String> progressMap) {
        this.context = context;
        this.lessonsList = lessonsList;
        this.progressMap =progressMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView indexTextView;
        TextView nameTextView;;
        TextView countTextView;
        CircularProgressIndicator circularProgress;
        MaterialCardView lessonCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            indexTextView = itemView.findViewById(R.id.indexTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
            circularProgress = itemView.findViewById(R.id.circularProgress);
            lessonCardView = itemView.findViewById(R.id.lessonCardView);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.lessons_recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String index = lessonsList.get(holder.getAbsoluteAdapterPosition()).getIndex();
        String name = lessonsList.get(holder.getAbsoluteAdapterPosition()).getName();

        holder.indexTextView.setText("Lesson " + index);
        holder.nameTextView.setText(name);
        holder.countTextView.setText("Includes " + lessonsList.get(holder.getAbsoluteAdapterPosition()).subLessons.size() + " sub lessons");
        int progress = getLessonProgress(lessonsList.get(holder.getAbsoluteAdapterPosition()).getIndex(),lessonsList.get(holder.getAbsoluteAdapterPosition()).subLessons.size());
        holder.circularProgress.setProgress(progress,100);


        if (progress == 0) {
            holder.circularProgress.setTextSizeSp(0);
        }
        else if (progress <= 25) {
            holder.circularProgress.setProgressColor(ContextCompat.getColor(context, R.color.red));
        } else if (progress <= 50) {
            holder.circularProgress.setProgressColor(ContextCompat.getColor(context, R.color.orange));
            holder.circularProgress.setTextColor(ContextCompat.getColor(context, R.color.orange));
        } else if (progress <= 75) {
            holder.circularProgress.setProgressColor(ContextCompat.getColor(context, R.color.yellow));
            holder.circularProgress.setTextColor(ContextCompat.getColor(context, R.color.yellow));
        } else if (progress <= 100) {
            holder.circularProgress.setProgressColor(ContextCompat.getColor(context, R.color.green));
            holder.circularProgress.setTextColor(ContextCompat.getColor(context, R.color.green));
        }

        holder.lessonCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SublessonsActivity.class);
                intent.putExtra("lessonIndex",lessonsList.get(holder.getAbsoluteAdapterPosition()).index);
                intent.putExtra("subLessonsList", (Serializable) lessonsList.get(holder.getAbsoluteAdapterPosition()).subLessons);
                intent.putExtra("progressMap", (Serializable) progressMap);
                context.startActivity(intent);
            }
        });



    }

    private int getLessonProgress( String index, int subLessonsCount) {
        double progress = 0;
        for(int i=1; i<=subLessonsCount; i++){
            int localProgress;
            try {
                localProgress= Integer.valueOf(progressMap.get(index+":"+i));
            }catch (Exception e){
                localProgress=0;
            }

            if(localProgress!=0){
                progress+= localProgress;
            }
        }

        int finalProgress = (int)((progress / (subLessonsCount * 100))*100);

        return finalProgress;
    }


    @Override
    public int getItemCount() {
        return lessonsList.size();
    }


}

