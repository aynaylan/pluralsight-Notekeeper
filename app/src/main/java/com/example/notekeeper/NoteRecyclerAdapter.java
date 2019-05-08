package com.example.notekeeper;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteRecyclerAdapter.ViewHolder>{

    private final Context context;
    private final LayoutInflater layoutInflater;
    private final List<NoteInfo> notes;


    public NoteRecyclerAdapter(Context context, List<NoteInfo> notes) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

        this.notes = notes;
    }


    //Creates the instannces for our viewholder and views themselves
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = layoutInflater.inflate(R.layout.item_note_list, viewGroup,false);

    //the item view is passed to the inner class where items can be declared and accessed


        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        NoteInfo note = notes.get(i);
        viewHolder.textCourse.setText(note.getCourse().getTitle());
        viewHolder.currentPostion = i;
        viewHolder.textTitle.setText(note.getTitle());


    }


    //Get the number of data that we have
    @Override
    public int getItemCount() {

        //Get the size of the list(Noteinfo)
        return notes.size();
    }






    //Holds information for the recycler Adpater
    public  class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView textCourse;
        public final TextView textTitle;

        public  int currentPostion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textCourse = itemView.findViewById(R.id.text_course);
            textTitle = itemView.findViewById(R.id.text_title);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NoteActivity.class);
                    intent.putExtra(NoteActivity.NOTE_POSITION, currentPostion);
                    context.startActivity(intent);

                }
            });




        }
    }




}
