package com.collegeessentials.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This application uses SQLiteDatabase as the app's background database, used to store assignments, timetables etc
 * It also uses SQLiteOpenHelper to manage the creation and in the future the version controlling
 *
 * @version 1.0
 * @see SQLiteDatabase, SQLiteOpenHelper, ApplicationDatabaseHelper
 */
public class ApplicationDatabase{

    /* SQL Statements and table names */
    public static final String TABLE_TIMETABLE = "timetable";
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final String TABLE_MARKERS = "markers";
    public static final int DATABASE_VERSION = 1;
    public static final String TIMETABLE_CREATE = "create table "
            + TABLE_TIMETABLE + " (module text not null, room text not null, teacher text not null, day text not null, time text not null);";
    public static final String ASSIGNMENT_CREATE = "create table "
            + TABLE_ASSIGNMENT + " (name text not null, due_date date not null);";
    public static final String MARKERS_CREATE = "create table "
            + TABLE_MARKERS + "(title text, lat float, long float);";

    private ApplicationDatabaseHelper adHelper;
    private Context context;
    private SQLiteDatabase db;
    private String dbName;

    public ApplicationDatabase(Context ctx, String dbName){
        this.dbName = dbName;
        this.context = ctx;
        adHelper = new ApplicationDatabaseHelper(context, dbName);
    }

    public ApplicationDatabase createDatabase() {
        db = adHelper.getWritableDatabase();
        return this;
    }

    /* Premade sql function to insert data into giveen table */
    public long insertIntoTimetable(String module, String room, String teacher, String day, String time) {
        ContentValues cv = new ContentValues();
        cv.put("module", module);
        cv.put("room", room);
        cv.put("teacher", teacher);
        cv.put("day", day);
        cv.put("time", time);
        return db.insertOrThrow(TABLE_TIMETABLE, null, cv);
    }

    public long insertIntoAssignment(String module, String due_date) {
        ContentValues cv = new ContentValues();
        cv.put("name", module);
        cv.put("due_date", due_date);
        return db.insertOrThrow(TABLE_ASSIGNMENT, null, cv);
    }

    public long insertIntoMarkers(String title, float lat, float lon){
        ContentValues cv = new ContentValues();
        cv.put("title", title);
        cv.put("lat", lat);
        cv.put("long", lon);
        return db.insertOrThrow(TABLE_MARKERS, null, cv);
    }

    //Returns everything as a Cursor that can be looped through
    public Cursor returnTimetableData(){
        return db.query(TABLE_TIMETABLE, new String[]{"module", "room", "teacher", "day", "time"}, null, null, null, null, null);
    }

    public Cursor returnAssignmentData(){
        return db.query(TABLE_ASSIGNMENT, new String[]{"name", "due_date"}, null,null,null,null,null);
    }

    public Cursor returnMarkers(){
        return db.query(TABLE_MARKERS, new String[]{"title, lat, long"}, null,null,null,null,null);
    }

    //Premade sql statement used to delete data where NAME = 'etc'
    public void deleteFromAssignment(String name){
        db.execSQL("DELETE FROM " + TABLE_ASSIGNMENT +  " WHERE name ='" + name +"'");
    }

    public void deleteFromTimetable(String module){
        db.execSQL("DELETE FROM " + TABLE_TIMETABLE +  " WHERE module ='" + module +"'");
    }

    public void deleteMarker(String title){
        db.execSQL("DELETE FROM " + TABLE_MARKERS +  " WHERE title ='" + title +"'");
    }

    /**
     * SQLiteOpenHelper is used to manage database creation and version management
     */
    private static class ApplicationDatabaseHelper extends SQLiteOpenHelper{

        public ApplicationDatabaseHelper(Context context, String dbName) {
            super(context, dbName+".db", null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL(TIMETABLE_CREATE);
                db.execSQL(ASSIGNMENT_CREATE);
                db.execSQL(MARKERS_CREATE);
                db.setTransactionSuccessful();
            }catch (SQLException e){
                e.printStackTrace();
            }finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKERS);
            onCreate(db);
        }
    }

}
