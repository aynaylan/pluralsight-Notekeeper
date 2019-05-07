package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

import java.util.List;

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


                startActivity(new Intent(NoteListActivity.this,MainActivity.class));



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

        List<NoteInfo> notes = DataManager.getInstance().getNotes();


        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);
        recyclerNotes.setAdapter(noteRecyclerAdapter);



        /*
        final ListView listNotes = findViewById(R.id.list_notes);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        adapterNotes = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,notes);
        listNotes.setAdapter(adapterNotes);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(NoteListActivity.this,MainActivity.class);

                //NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);

                intent.putExtra(MainActivity.NOTE_POSITION, position);
                startActivity(intent);






            }
        });*/



    }

}
