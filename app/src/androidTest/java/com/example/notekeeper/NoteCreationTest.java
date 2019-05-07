package com.example.notekeeper;

import androidx.test.espresso.ViewInteraction;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;


import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.action.ViewActions.pressBack;

import static androidx.test.espresso.assertion.ViewAssertions.*;

@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {

    static DataManager dataManager;

    @BeforeClass
    public static void classSetUp(){
        dataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityTestRule<NoteListActivity> activityRule =
            new ActivityTestRule(NoteListActivity.class);


    @Test
    public void createNewNote(){

        final CourseInfo course = dataManager.getCourse("java_lang");
        final String noteTitle = "Text note Title";
        final String noteText = "Text note Text";

        //Interact with the view and perform a click on the specified button with the ID
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class), equalTo(course))).perform(click());
        onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(containsString(course.getTitle()))));

        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle)).check(matches(withText(noteTitle)));



        onView(withId(R.id.text_note_text)).perform(typeText(noteText)).check(matches(withText(containsString(noteText))));;
        closeSoftKeyboard();


        onView(withId(R.id.text_note_title)).check(matches(withText(containsString(noteTitle))));
        pressBack();

        int noteIndex =dataManager.getNotes().size() -1;
        NoteInfo note = dataManager.getNotes().get(noteIndex);
        assertEquals(noteTitle,note.getTitle());
        assertEquals(noteText,note.getText());




    }


}