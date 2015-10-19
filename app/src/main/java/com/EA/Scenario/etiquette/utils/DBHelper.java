package com.EA.Scenario.etiquette.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String TABLE_NAME = "etiquette";
    public static final String COLUMN_Trip_Number = "TripNumber";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_START = "odometerstartval";
    public static final String COLUMN_END = "odometerendval";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS odovalues " +
                        "(TripNumber text primary key, date text,odometerstartval text,odometerendval text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS odovalues");
        onCreate(db);
    }

    public boolean insertvalue  (String tripnumber, String date, String odostart, String odoend)
    {
        if(updatevalue(tripnumber,date,odostart,odoend) == 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("TripNumber", tripnumber);
            contentValues.put("date", date);
            contentValues.put("odometerstartval", odostart);
            contentValues.put("odometerendval", odoend);


            db.insert("odovalues", null, contentValues);
        }
        return true;
    }
    public String getStart(String tripnumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from odovalues where TripNumber="+tripnumber+"", null );
        res.moveToFirst();
        if(res.getCount() > 0) {
            return res.getString(res.getColumnIndex(COLUMN_START));
        } else
            return "0";
    }
    public String getEnd(String tripnumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from odovalues where TripNumber="+tripnumber+"", null );
        res.moveToFirst();
        if(res.getCount()  > 0) {
            return res.getString(res.getColumnIndex(COLUMN_END));
        } else
            return "0";
    }

    public int checkExist(String tripnumber){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from odovalues where TripNumber="+tripnumber+"", null );
        return res.getCount();
    }
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
    public int updatevalue (String tripnumber, String date, String odostart, String odoend)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TripNumber", tripnumber);
        contentValues.put("date", date);
        contentValues.put("odometerstartval", odostart);
        contentValues.put("odometerendval", odoend);
        return db.update("odovalues", contentValues, "TripNumber = ? ", new String[] { tripnumber } );

    }

//    public Integer deleteContact (Integer id)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("odovalues",
//                "id = ? ",
//                new String[] { Integer.toString(id) });
//    }
//    public ArrayList getAllCotacts()
//    {
//        ArrayList array_list = new ArrayList();
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from odovalues", null );
//        res.moveToFirst();
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
//        return array_list;
//    }
}

