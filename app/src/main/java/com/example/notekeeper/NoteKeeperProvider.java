package com.example.notekeeper;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import static com.example.notekeeper.NoteKeeperDatabaseContract.CourseInfoEntry;
import static com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry.COLUMN_COURSE_ID;
import static com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry.TABLE_NAME;
import static com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry._ID;
import static com.example.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry.getQName;
import static com.example.notekeeper.NoteKeeperProviderContract.AUTHORITY;
import static com.example.notekeeper.NoteKeeperProviderContract.Course;
import static com.example.notekeeper.NoteKeeperProviderContract.Notes;


public class NoteKeeperProvider extends ContentProvider {

    public static final int COURSES = 0;
    public static final int NOTES = 1;
    public static final int NOTES_EXPANDED = 2;
    public static final int NOTES_ROW = 3;
    private static final String MIME_VENDOR_TYPE = "vnd." + NoteKeeperProviderContract.AUTHORITY + ".";
    private static final int COURSES_ROW = 4;
    private static final int NOTES_EXPANDED_ROW = 5;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {


        sUriMatcher.addURI(AUTHORITY, Course.PATH, COURSES);
        sUriMatcher.addURI(AUTHORITY, Notes.PATH, NOTES);
        sUriMatcher.addURI(AUTHORITY, Notes.PATH_EXPANDED, NOTES_EXPANDED);
        sUriMatcher.addURI(AUTHORITY, Course.PATH + "/#", COURSES_ROW);
        sUriMatcher.addURI(AUTHORITY, Notes.PATH + "/#", NOTES_ROW);
        sUriMatcher.addURI(AUTHORITY, Notes.PATH_EXPANDED + "/#", NOTES_EXPANDED_ROW);
    }

    private NoteKeeperOpenHelper mDbOpenHelper;

    public NoteKeeperProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case COURSES:
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(CourseInfoEntry.TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = _ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.delete(TABLE_NAME, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }

        return nRows;

    }

    @Override
    public String getType(Uri uri) {
        String mimeType = null;
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case COURSES:
                // vnd.android.cursor.dir/vnd.com.jwhh.jim.notekeeper.provider.courses
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                        MIME_VENDOR_TYPE + Course.PATH;
                break;
            case NOTES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;
            case NOTES_EXPANDED:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;
            case COURSES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Course.PATH;
                break;
            case NOTES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH;
                break;
            case NOTES_EXPANDED_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Notes.PATH_EXPANDED;
                break;
        }
        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        long rowId = -1;
        Uri rowUri = null;
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case NOTES:
                rowId = db.insert(TABLE_NAME, null, values);
                // content://com.jwhh.jim.notekeeper.provider/notes/1
                rowUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
                break;
            case COURSES:
                rowId = db.insert(CourseInfoEntry.TABLE_NAME, null, values);
                rowUri = ContentUris.withAppendedId(Course.CONTENT_URI, rowId);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
                break;
        }

        return rowUri;
    }

    @Override
    public boolean onCreate() {


        mDbOpenHelper = new NoteKeeperOpenHelper(getContext());

        return true;//If the content provider was created successfully return true
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case COURSES:
                cursor = db.query(CourseInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTES:
                cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case NOTES_EXPANDED:
                cursor = notesExpandedQuery(db, projection, selection, selectionArgs, sortOrder);

        }


        return cursor;
    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder) {


        String[] columns = new String[projection.length];
        for (int idx = 0; idx < projection.length; idx++) {
            columns[idx] = projection[idx].equals(BaseColumns._ID) ? getQName(projection[idx]) : projection[idx];
        }


        String tablesWithJoin = TABLE_NAME + " JOIN " + CourseInfoEntry.TABLE_NAME + " ON " + getQName(COLUMN_COURSE_ID)
                + " = " + CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);


        return db.query(tablesWithJoin, columns, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        long rowId = -1;
        String rowSelection = null;
        String[] rowSelectionArgs = null;
        int nRows = -1;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case COURSES:
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES:
                nRows = db.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTES_EXPANDED:
                // throw exception saying that this is a read-only table
            case COURSES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = CourseInfoEntry._ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(CourseInfoEntry.TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_ROW:
                rowId = ContentUris.parseId(uri);
                rowSelection = _ID + " = ?";
                rowSelectionArgs = new String[]{Long.toString(rowId)};
                nRows = db.update(TABLE_NAME, values, rowSelection, rowSelectionArgs);
                break;
            case NOTES_EXPANDED_ROW:
                // throw exception saying that this is a read-only table
                break;
        }

        return nRows;
    }
}
