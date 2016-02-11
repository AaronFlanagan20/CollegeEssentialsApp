package com.gmit.gmit3D.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gmit.gmit3D.main.CollegeSelection;

public class ApplicationDatabase{

    public static final String TABLE_TIMETABLE = "timetable";
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final int DATABASE_VERSION = 1;
    public static final String TIMETABLE_CREATE = "create table "
            + TABLE_TIMETABLE + " (module text not null, room text not null, teacher text not null, day text not null, time text not null);";
    public static final String ASSIGNMENT_CREATE = "create table "
            + TABLE_ASSIGNMENT + " (name text not null, due_date date not null);";

    ApplicationDatabaseHelper adHelper;
    Context context;
    SQLiteDatabase db;
    String dbName;

    public ApplicationDatabase(Context ctx, String dbName){
        this.dbName = dbName;
        this.context = ctx;
        adHelper = new ApplicationDatabaseHelper(context, dbName);
    }

    public String getDbName() {
        return dbName;
    }

    public ApplicationDatabase createDatabase() {
        db = adHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        adHelper.close();
    }

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

    public Cursor returnTimetableData(){
        return db.query(TABLE_TIMETABLE, new String[]{"module", "room", "teacher", "day", "time"}, null, null, null, null, null);
    }

    public Cursor returnAssignmentData(){
        return db.query(TABLE_ASSIGNMENT, new String[]{"name", "due_date"}, null,null,null,null,null);
    }

    public void deleteFromAssignment(String name){
        db.execSQL("DELETE FROM " + TABLE_ASSIGNMENT +  " WHERE name ='" + name +"'");
    }

    public void deleteFromTimetable(String module){
        db.execSQL("DELETE FROM " + TABLE_TIMETABLE +  " WHERE module ='" + module +"'");
    }

    private static class ApplicationDatabaseHelper extends SQLiteOpenHelper{

        static CollegeSelection cs = new CollegeSelection();

        public ApplicationDatabaseHelper(Context context, String dbName) {
            super(context, dbName+".db", null, DATABASE_VERSION);
            System.out.println(dbName);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL(TIMETABLE_CREATE);
                db.execSQL(ASSIGNMENT_CREATE);
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
            onCreate(db);
        }
    }

}
