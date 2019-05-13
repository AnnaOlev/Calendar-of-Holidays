package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HolidaysDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "holidays database";
    private static final String ID_COLUMN = "id";
    private static final String TABLE_NAME = "holidays_table";
    private static final String DATE_COLUMN = "date";
    private static final String NAME_COLUMN = "name";
    private static final String LOCAL_NAME_COLUMN = "local_name";
    private static final String COUNTRY_COLUMN = "country";
    private static final String FAV_COLUMN = "fav";

    HolidaysDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table holidays_table" +
                "(id integer primary key, date text,name text,local_name text, country text, fav text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS holidays_table");
        onCreate(db);
    }

    boolean insertHoliday(String date, String name, String localName, String country, String fav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("name", name);
        contentValues.put("local_name", localName);
        contentValues.put("country", country);
        contentValues.put("fav", fav);
        db.insert("holidays_table", null, contentValues);
        return true;
    }

    ArrayList<Holiday> getAllHolidays() {
        ArrayList<Holiday> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from holidays_table", null );
        res.moveToFirst();

        while(!res.isAfterLast()){

            String date = res.getString(res.getColumnIndex(DATE_COLUMN));
            String name = res.getString(res.getColumnIndex(NAME_COLUMN));
            String local_name = res.getString(res.getColumnIndex(LOCAL_NAME_COLUMN));
            String country = res.getString(res.getColumnIndex(COUNTRY_COLUMN));
            String fav = res.getString(res.getColumnIndex(FAV_COLUMN));

            Holiday holiday = new Holiday(date, local_name, name, country, fav);
            array_list.add(holiday);

            res.moveToNext();
        }
        res.close();
        return array_list;
    }


    boolean getDataByCountry(String country) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor row;
        String query = "SELECT * FROM holidays_table" + " WHERE country"+" like '%"+country+"%'";
        row = db.rawQuery(query, null);
        return row.getCount() > 0;
    }

    void deleteEntry(String country) {
        SQLiteDatabase db = this.getWritableDatabase();
        String where="country=?";
        int numberOFEntriesDeleted = db.delete(TABLE_NAME, where, new String[]{country});

        Log.e(TAG, "DELETE DELETE DELETE " + numberOFEntriesDeleted);
    }

    int getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor row;
        String count = "SELECT count(*) FROM holidays_table";
        row = db.rawQuery(count, null);
        row.moveToFirst();
        int counter = row.getInt(0);
        row.close();
        return counter;
    }
    // по идее этот класс нужен для проверки того, есть ли элементы с текущей датой в бд, но я хз то ли это что надо
}
