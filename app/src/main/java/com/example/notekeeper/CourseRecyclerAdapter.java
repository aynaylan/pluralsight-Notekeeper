package com.example.notekeeper;

import android.content.Context;

import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder>{

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<CourseInfo> courses;


    public CourseRecyclerAdapter(Context context, List<CourseInfo> courses) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.courses = courses;
    }


    //Creates the instannces for our viewholder and views themselves
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = layoutInflater.inflate(R.layout.item_course_list, viewGroup,false);

    //the item view is passed to the inner class where items can be declared and accessed


        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        CourseInfo course = courses.get(i);
        viewHolder.textCourse.setText(course.getTitle());
        viewHolder.currentPostion = i;


    }


    //Get the number of data that we have
    @Override
    public int getItemCount() {

        //Get the size of the list(Noteinfo)
        return courses.size();
    }






    //Holds information for the recycler Adpater
    public  class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;

        public  int currentPostion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textCourse = itemView.findViewById(R.id.text_course);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Snackbar.make(v,courses.get(currentPostion).getTitle(),Snackbar.LENGTH_LONG);

                }
            });




        }
    }




}
