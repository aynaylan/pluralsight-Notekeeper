package com.example.notekeeper;

import android.provider.ContactsContract;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {

    static DataManager dataManager;

    @BeforeClass
    public static void classSetUp(){

        dataManager = DataManager.getInstance();

    }

    @Before
    public void setUp(){

        DataManager dm = DataManager.getInstance();
        dataManager.getNotes().clear();
        dataManager.initializeExampleNotes();

    }

    @Test
    public void createNewNote() {

        DataManager dm = DataManager.getInstance();
        final CourseInfo course = dataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText = "THis is the body of my test note" ;


        int noteIndex = dataManager.createNewNote();
        NoteInfo newNote = dataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote = dataManager.getNotes().get(noteIndex);
        assertEquals(compareNote.getCourse(),course);
        assertEquals(compareNote.getTitle(),noteTitle);
        assertEquals(compareNote.getText(),noteText);


    }

    @Test
    public void findSimilarNotes(){

        final DataManager dm = DataManager.getInstance();
        final CourseInfo course = dataManager.getCourse("android_async");
        final String noteTitle = "Test note title";
        final String noteText1 = "THis is the body of my test note" ;
        final String noteText2 = "THis is the body of my second test note" ;


        int noteIndex1 = dataManager.createNewNote();
        NoteInfo newNote1 = dataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);


        int noteIndex2 = dataManager.createNewNote();
        NoteInfo newNote2 = dataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2);

        int foundIndex1 = dataManager.findNote(newNote1);
        assertEquals(noteIndex1,foundIndex1);

        int foundIndex2 = dataManager.findNote(newNote2);
        assertEquals(noteIndex2,foundIndex2);

    }


    @Test
    public void createNewNoteOneStepCreation(){

        final CourseInfo course =dataManager.getCourse("android_async");
        final String noteTitle = "test note title";
        final String noteText = "THis is the body of my test note";


        int noteIndex =dataManager.createNewNote(course,noteTitle,noteText);

        NoteInfo compareNote = dataManager.getNotes().get(noteIndex);
        assertEquals(course,compareNote.getCourse());
        assertEquals(noteTitle,compareNote.getTitle());
        assertEquals(noteText,compareNote.getText());

    }

}