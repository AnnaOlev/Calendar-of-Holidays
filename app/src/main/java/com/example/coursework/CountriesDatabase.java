package com.example.coursework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class CountriesDatabase extends SQLiteOpenHelper {

        private static final int VERSION = 1;
        private static final String DATABASE_NAME = "countries database";
        private static final String ID_COLUMN = "id";
        private static final String TABLE_NAME = "countries_table";
        private static final String COUNTRY_NAME_COLUMN = "name";
        private static final String COUNTRY_CODE_COLUMN = "code";
        private static final String IF_ADDED_COLUMN = "added";

        CountriesDatabase(Context context){
            super(context, DATABASE_NAME, null, VERSION );
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table countries_table" +
                    "(id integer primary key, name text, code text, added text)"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS countries_table");
            onCreate(db);
        }

        boolean insertCountry(String name, String code, String added) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("code", code);
            contentValues.put("added", added);
            db.insert("countries_table", null, contentValues);
            Log.i(TAG, "Country was added to the database" + name);
            return true;
        }

        ArrayList<Country> getAllCountries() {
            ArrayList<Country> array_list = new ArrayList<>();

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from countries_table", null );
            res.moveToFirst();

            while(!res.isAfterLast()){

                String name = res.getString(res.getColumnIndex(COUNTRY_NAME_COLUMN));
                String code = res.getString(res.getColumnIndex(COUNTRY_CODE_COLUMN));
                String added = res.getString(res.getColumnIndex(IF_ADDED_COLUMN));
                String id = res.getString(res.getColumnIndex(ID_COLUMN));

                Country country = new Country(name, code, added, id);
                array_list.add(country);
                Log.i(TAG, "Country was added to the array" + name);

                res.moveToNext();
            }
            res.close();
            return array_list;
        }

        int getData() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor row;
            String count = "SELECT count(*) FROM countries_table";
            row = db.rawQuery(count, null);
            row.moveToFirst();
            int counter = row.getInt(0);
            row.close();
            return counter;
        }

        boolean getDataByCode(String code) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor row;
            String query = "SELECT * FROM countries_table" + " WHERE code"+" like '%"+code+"%' AND added" + " like '%" + "yes" + "%'";
            row = db.rawQuery(query, null);
            return row.getCount() > 0;
        }
}
