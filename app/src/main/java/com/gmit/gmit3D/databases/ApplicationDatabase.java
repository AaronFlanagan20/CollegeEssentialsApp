package com.gmit.gmit3D.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aaron on 11/01/2016.
 */
public class ApplicationDatabase{

    public static final String TABLE_TIMETABLE = "timetable";
    public static final String TABLE_ASSIGNMENT = "assignment";
    public static final String DATABASE_NAME = "gmit3d.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TIMETABLE_CREATE = "create table"
            + TABLE_TIMETABLE + "(module primary key text not null, room integer not null, module_date date not null);";
    public static final String ASSIGNMENT_CREATE = "create table"
            + TABLE_ASSIGNMENT + "(module primary key text not null, due_date date not null);";

    ApplicationDatabaseHelper adHelper;
    Context context;
    SQLiteDatabase db;

    public ApplicationDatabase(Context ctx){
        this.context = ctx;
        adHelper = new ApplicationDatabaseHelper(ctx);
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
        cv.put("MODULE", module);
        cv.put("ROOM", room);
        cv.put("MODULE_DATE", module_date);
        return db.insertOrThrow(TABLE_TIMETABLE, null, cv);
    }

    public long insertIntoAssignment(String module, String due_date) {
        ContentValues cv = new ContentValues();
        cv.put("MODULE", module);
        cv.put("DUE_DATE", due_date);
        return db.insertOrThrow(TABLE_ASSIGNMENT, null, cv);
    }

    public Cursor returnTimetableData(){
        return db.query(TABLE_TIMETABLE, new String[]{"module", "room", "module_date"}, null, null, null, null, null);
    }

    public Cursor returnAssignmetData(){
        return db.query(TABLE_ASSIGNMENT, new String[]{"module", "due_date"}, null,null,null,null,null);
    }

    private static class ApplicationDatabaseHelper extends SQLiteOpenHelper{

        public ApplicationDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TIMETABLE_CREATE);
                db.execSQL(ASSIGNMENT_CREATE);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //Drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMETABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
            onCreate(db);
        }
    }

}
