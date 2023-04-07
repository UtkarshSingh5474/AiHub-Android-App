package com.example.aihub.lessons;

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
import com.example.aihub.dataobjects.SubLessons;
import com.google.android.material.card.MaterialCardView;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

public class SubLessonRecyclerViewAdapter extends RecyclerView.Adapter<SubLessonRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private List<SubLessons> subLessonsList;
    private static Map<String,String> progressMap = Collections.emptyMap();;
    private static String lessonIndex;

    public SubLessonRecyclerViewAdapter(Context context, List<SubLessons> subLessonsList,Map<String,String> progressMap, String lessonIndex) {
        this.context = context;
        this.subLessonsList = subLessonsList;
        this.progressMap = progressMap;
        this.lessonIndex = lessonIndex;
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

        String index = subLessonsList.get(holder.getAbsoluteAdapterPosition()).getIndex();
        String name = subLessonsList.get(holder.getAbsoluteAdapterPosition()).getName();
        Log.d("map", "onBindViewHolder: " + progressMap);

        holder.indexTextView.setText("Sublesson " + index);
        holder.nameTextView.setText(name);
        holder.countTextView.setText("Includes " + subLessonsList.get(holder.getAbsoluteAdapterPosition()).cards + " cards");
        //double progress = Double.parseDouble(progressMap.get(lessonIndex+":"+index));
        int progress;
        try {
            progress= Integer.valueOf(progressMap.get(lessonIndex+":"+index));
        }catch (Exception e){
            progress=0;
        }

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
                Intent intent = new Intent(context, LessonExampleActivity.class);
                intent.putExtra("lessonIndex", lessonIndex);
                intent.putExtra("subLessonIndex", index);
                try{
                    if(progressMap.get(""+lessonIndex+":"+ String.valueOf(holder.getAbsoluteAdapterPosition()+1)) != null ){
                        intent.putExtra("progress", Integer.valueOf(progressMap.get(""+lessonIndex+":"+ String.valueOf(holder.getAbsoluteAdapterPosition()+1))));
                    }
                    else{
                        intent.putExtra("progress", 0);
                    }
                }catch (Exception e){
                    intent.putExtra("progress", 0);
                }

                //intent.putExtra("progress",  progressMap.get(""+lessonIndex+":"+ String.valueOf(holder.getAbsoluteAdapterPosition()+1)));
                context.startActivity(intent);

            }
        });



    }

    @Override
    public int getItemCount() {
        return subLessonsList.size();
    }


}

