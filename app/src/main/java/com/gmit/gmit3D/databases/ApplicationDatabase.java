package com.gmit.gmit3D.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ApplicationDatabase{

    public static final String TABLE_TIMETABLE = "timetable";
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final String DATABASE_NAME = "gmit3d.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TIMETABLE_CREATE = "create table "
            + TABLE_TIMETABLE + " (module text not null, room integer not null, module_date date not null);";
    public static final String ASSIGNMENT_CREATE = "create table "
            + TABLE_ASSIGNMENT + " (module text not null, due_date date not null);";

    ApplicationDatabaseHelper adHelper;
    Context context;
    SQLiteDatabase db;

    public ApplicationDatabase(Context ctx){
        this.context = ctx;
        adHelper = new ApplicationDatabaseHelper(context);
    }

    public ApplicationDatabase createDatabase() {
        db = adHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        adHelper.close();
    }

    public long insertIntoTimetable(String module, int room, String module_date) {
        ContentValues cv = new ContentValues();
        cv.put("module", module);
        cv.put("room", room);
        cv.put("module_date", module_date);
        return db.insertOrThrow(TABLE_TIMETABLE, null, cv);
    }

    public long insertIntoAssignment(String module, String due_date) {
        ContentValues cv = new ContentValues();
        cv.put("module", module);
        cv.put("due_date", due_date);
        return db.insertOrThrow(TABLE_ASSIGNMENT, null, cv);
    }

    public Cursor returnTimetableData(){
        return db.query(TABLE_TIMETABLE, new String[]{"module", "room", "module_date"}, null, null, null, null, null);
    }

    public Cursor returnAssignmentData(){
        return db.query(TABLE_ASSIGNMENT, new String[]{"module", "due_date"}, null,null,null,null,null);
    }

    public void deleteFromAssignment(String name){
        db.execSQL("DELETE FROM " + TABLE_ASSIGNMENT +  " WHERE module ='" + name +"'");
    }

    private static class ApplicationDatabaseHelper extends SQLiteOpenHelper{

        public ApplicationDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
