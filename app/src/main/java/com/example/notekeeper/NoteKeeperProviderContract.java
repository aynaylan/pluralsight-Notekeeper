package com.example.notekeeper;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteKeeperProviderContract {


    public static final String AUTHORITY = "com.example.notekeeper.provider";
    public static final Uri AUTHORUTY_URI = Uri.parse("content://" + AUTHORITY);

    private NoteKeeperProviderContract() {
    }

    protected interface CoursesIdColumns {

        public static final String COLUMN_COURSE_ID = "course_id";
    }


    protected interface CoursesColumns {

        public static final String COLUMN_COURSE_TITLE = "course_title";


    }

    protected interface NotesColumns {

        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";

    }

    //Each class corresponds with each table in the database
    public static final class Course implements BaseColumns, CoursesColumns, CoursesIdColumns {

        public static final String PATH = "courses";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORUTY_URI, PATH);

    }


    public static final class Notes implements BaseColumns, NotesColumns, CoursesIdColumns, CoursesColumns {

        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORUTY_URI, PATH);
        public static final String PATH_EXPANDED = "notes_expanded";
        public static final Uri CONTENT_EXPANDED_URI = Uri.withAppendedPath(AUTHORUTY_URI, PATH_EXPANDED);

    }

    //The basecolumns interface has the _ID column


}
