package com.example.notekeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private NoteRecyclerAdapter noteRecyclerAdapter;
    private LinearLayoutManager notesLayoutManager;
    private RecyclerView recyclerItems;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    private GridLayoutManager coursesLayoutManager;
    private NoteKeeperOpenHelper openHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        openHelper = new NoteKeeperOpenHelper(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));

            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();//if the navigation drawer is open the Actionbar needs to know that




        initializeDisplayContent();
    }


    @Override
    protected void onDestroy() {
        //close the database when the activity is being destroyed
        openHelper.close();
        super.onDestroy();


    }

    @Override
    protected void onResume() {
        super.onResume();

        // adapterNotes.notifyDataSetChanged();

        //Refresh our dataset
        noteRecyclerAdapter.notifyDataSetChanged();
        
        
        updateNavHeader();
    }

    private void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textUserName = headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = headerView.findViewById(R.id.text_user_email);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName =  pref.getString("user_display_name","");
        String userEmail = pref.getString("user_email_address","");

        textUserName.setText(userName);
        textEmailAddress.setText(userEmail);

    }

    private void initializeDisplayContent() {

        DataManager.loadFromDatabase(openHelper);



        recyclerItems = findViewById(R.id.list_items);
        notesLayoutManager = new LinearLayoutManager(this);

        coursesLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.course_grid_span));


        recyclerItems.setLayoutManager(notesLayoutManager);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        noteRecyclerAdapter = new NoteRecyclerAdapter(this,notes);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        courseRecyclerAdapter = new CourseRecyclerAdapter(this,courses);



        displayNotes();

    }

    private void displayNotes() {

        recyclerItems.setLayoutManager(notesLayoutManager);
        recyclerItems.setAdapter(noteRecyclerAdapter);


        selectNavigationMenuItem(R.id.nav_notes);


    }

    private void selectNavigationMenuItem(int id) {
        //Display notes by default
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();//feteh the menu from the navigation View
        menu.findItem(id).setChecked(true);
    }


    private void displayCourses(){
        recyclerItems.setLayoutManager(coursesLayoutManager);
        recyclerItems.setAdapter(courseRecyclerAdapter);

        selectNavigationMenuItem(R.id.nav_courses);
    }


    //called when the user presses back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        //if the drawer is currently open close the drawer
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {

            displayNotes();

        } else if (id == R.id.nav_courses) {
            displayCourses();

        } else if (id == R.id.nav_share) {

            handleSelection(R.string.nav_share_message);
            handleShare();
        } else if (id == R.id.nav_send) {

            handleSelection(R.string.nav_send_message);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleShare() {

        View view = findViewById(R.id.list_items);
        Snackbar.make(view,"Share to - "+ PreferenceManager.getDefaultSharedPreferences(this).getString("user_favourite_social",""),Snackbar.LENGTH_LONG).show();
    }

    private void handleSelection(int message_id) {

        View view = findViewById(R.id.list_items);
        Snackbar.make(view,message_id,Snackbar.LENGTH_LONG).show();

    }
}
