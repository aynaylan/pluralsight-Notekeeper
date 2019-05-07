package com.example.notekeeper;

import java.util.List;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.onView;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;


public class NextThroughTest {


    //calls the activity before any of the test are run
    @Rule
    public ActivityTestRule<Navigation> navigationActivityTestRule = new ActivityTestRule<>(Navigation.class);

    @Test
    public void NextThroughNOtes(){
    onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

    //Navigate and open the
    onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));

    ///get to the recyclerview
        onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        List<NoteInfo> notes = DataManager.getInstance().getNotes();


        for(int index = 0;index < notes.size();index++){



            NoteInfo note =notes.get(index);

            //Check the value of the spinner
            onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(note.getCourse().getTitle())));

            //Check whether the textfield matches the Title in the list
            onView(withId(R.id.text_note_title)).check(matches(withText(note.getTitle())));
           //Check whether the text fiels matches the Text in the list
            onView(withId(R.id.text_note_text)).check(matches(withText(note.getText())));

            //Go to the next menu item inthe list
            if(index < notes.size() -1)
                onView(allOf(withId(R.id.action_next), isEnabled() )).perform(click());

        }

        //onView(withId(R.id.action_next)).check(not(isEnabled()));
        pressBack();




    }

}