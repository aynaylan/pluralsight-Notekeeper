package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteListActivity extends AppCompatActivity {
    private NoteRecyclerAdapter noteRecyclerAdapter;

    //private ArrayAdapter<NoteInfo> adapterNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(NoteListActivity.this, NoteActivity.class));



            }
        });




    initializeDisplayContent();


    }

    @Override
    protected void onResume() {
        super.onResume();

       // adapterNotes.notifyDataSetChanged();

        //Refresh our dataset
        noteRecyclerAdapter.notifyDataSetChanged();
    }

    private void initializeDisplayContent() {


        final RecyclerView recyclerNotes = findViewById(R.id.list_notes);
        final LinearLayoutManager notesLayoutManager = new LinearLayoutManager(this);

        recyclerNotes.setLayoutManager(notesLayoutManager);


        noteRecyclerAdapter = new NoteRecyclerAdapter(this, null);
        recyclerNotes.setAdapter(noteRecyclerAdapter);





    }

}
