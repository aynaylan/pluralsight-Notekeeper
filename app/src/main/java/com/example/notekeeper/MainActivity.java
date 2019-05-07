package com.example.notekeeper;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String ORIGINAL_NOTE_COURSE_ID = "ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.example.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.example.notekeeper.ORIGINAL_NOTE_TEXT";


    private final String TAG = getClass().getSimpleName();//Allows the TAG vakue to remain the same as the class regardless of change inthe class


    public static final String NOTE_POSITION = "com.example.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo noteInfo;
    private boolean isNewNote;
    private Spinner spinnerCourses;
    private EditText textNoteTitle;
    private EditText textNoteText;
    private int notePosition;
    private boolean isCancelling;
    private String originalNoteCourseId;
    private String originalNoteTitle;
    private String originalNoteText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerCourses = findViewById(R.id.spinner_courses);



        //Adapters are the link between a set of data and the adpaterView that displays the data they also provide a childview
        List<CourseInfo> courses = DataManager.getInstance().getCourses();


        ArrayAdapter<CourseInfo> adapterCourses = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,courses);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCourses.setAdapter(adapterCourses);

        readDisplayStateValues();
        
        
        if(savedInstanceState == null){

            saveOriginalNoteValues();

        }else{
            restoreOriginalNoteValues(savedInstanceState);
        }

        textNoteTitle = findViewById(R.id.text_note_title);
        textNoteText = findViewById(R.id.text_note_text);

        if(!isNewNote){

            displayNote(spinnerCourses, textNoteTitle, textNoteText);
        }
        


        Log.d(TAG,"onCreate");
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {

        originalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        originalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        originalNoteText = savedInstanceState.getString(originalNoteText);


    }

    private void saveOriginalNoteValues() {
        if(isNewNote)
            return;

        originalNoteCourseId = noteInfo.getCourse().getCourseId();
        originalNoteTitle = noteInfo.getTitle();
        originalNoteText = noteInfo.getText();



    }

    private void displayNote(Spinner spinnerCourses, EditText textNoteTitle, EditText textNoteText) {
        List<CourseInfo> course = DataManager.getInstance().getCourses();//Retrieves the list of courses

        int courseIndex = course.indexOf(noteInfo.getCourse());//get the position of the course contained in the note

        spinnerCourses.setSelection(courseIndex);
        textNoteTitle.setText(noteInfo.getTitle());
        textNoteText.setText(noteInfo.getText());
        //noteInfo.
    }


    @Override
    protected void onPause() {
        super.onPause();//base class implementation

        if(isCancelling){

            Log.i(TAG,"Cancelling note at position:  "+ notePosition);

            if(isNewNote){

                DataManager.getInstance().removeNote(notePosition);//Remove the note eevery time er are cancelling

            }else{
                storePreviousNoteValues();
            }


        }else {


            saveNote();

        }

        Log.d(TAG,"onPause");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ORIGINAL_NOTE_COURSE_ID,originalNoteCourseId);
        outState.putString(originalNoteTitle,originalNoteTitle);
        outState.putString(originalNoteText,originalNoteText);
        
        
        
    }

    private void storePreviousNoteValues() {

        CourseInfo course = DataManager.getInstance().getCourse(originalNoteCourseId);
        noteInfo.setCourse(course);
        noteInfo.setTitle(originalNoteTitle);
        noteInfo.setText(originalNoteText);

    }

    private void saveNote() {

        noteInfo.setCourse((CourseInfo) spinnerCourses.getSelectedItem());///get the result of the selected course to our method
        noteInfo.setTitle(textNoteTitle.getText().toString());
        noteInfo.setText(textNoteText.getText().toString());

    }

    //Gets the note from the extra
    private void readDisplayStateValues(){
        Intent intent = getIntent();
        int notePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        //Value types dont have a concept of null....reference types have a concept of null

        isNewNote = notePosition == POSITION_NOT_SET;

        if(isNewNote){
            createNewNote();
        }else{

            Log.d(TAG,"Note Position: "+ notePosition);
            noteInfo = DataManager.getInstance().getNotes().get(notePosition);

        }



    }

    private void createNewNote() {

        DataManager dm = DataManager.getInstance();
        notePosition = dm.createNewNote();//Returns the position of the newly created note
        noteInfo = dm.getNotes().get(notePosition);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {

            sendEmail();
            return true;
        }else if(id == R.id.action_cancel){

            isCancelling = true;

            //exit if the User doesn't want to save note exit the program
            finish();

        }else if(id == R.id.action_next){

            moveNext();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size() -1;
        item.setEnabled(notePosition < lastNoteIndex);


        return super.onPrepareOptionsMenu(menu);
    }

    private void moveNext() {

        saveNote();//SAve any changes they made before proceeding to the next note

        ++notePosition;
        noteInfo = DataManager.getInstance().getNotes().get(notePosition);
        saveOriginalNoteValues();
        displayNote(spinnerCourses,textNoteTitle,textNoteText);

        invalidateOptionsMenu();

    }

    private void sendEmail() {

        CourseInfo course = (CourseInfo) spinnerCourses.getSelectedItem();
        String subject = textNoteTitle.getText().toString();
        String text = "Check out what I learnt in the pluralsight course \"" + course.getTitle()+ "\"\n" + textNoteText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");//Standard MIME type for email
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(intent);


    }
}
