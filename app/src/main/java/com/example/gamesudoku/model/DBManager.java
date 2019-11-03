package com.example.gamesudoku.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="SudokuDB";
    private static final String TABLE_NAME ="achievements";
    private static final String ID ="id";
    private static final String NAME ="name";
    private static final String TIME ="time";
    private static final String DATE ="date";

    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME,null, 1);
        Log.d("DBManager", "DBManager: ");
        this.context = context;
    }

    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                ID +" integer primary key, "+
                NAME + " TEXT, "+
                TIME +" TEXT, "+
                DATE+" TEXT)";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    //Add new
    public boolean addAchievements(Achievements achievements){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, achievements.getName());
        values.put(TIME, achievements.getTime());
        values.put(DATE,achievements.getDate());
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME,null,values);
        return true;
    }

    /*
     Getting All Student
      */

    public ArrayList<Achievements> getAllAchievements() {
        ArrayList<Achievements> lst = new ArrayList<Achievements>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Achievements achievements = new Achievements();
                achievements.setId(cursor.getInt(0));
                achievements.setName(cursor.getString(1));
                achievements.setTime(cursor.getString(2));
                achievements.setDate(cursor.getString(3));
                lst.add(achievements);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lst;
    }
}
