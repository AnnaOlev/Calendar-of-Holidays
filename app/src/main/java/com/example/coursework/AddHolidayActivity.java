package com.example.coursework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class AddHolidayActivity extends AppCompatActivity {

    Button mConfirmButton;
    EditText mEnterName;
    EditText mEnterDate;
    String name;
    String date;
    HolidaysDatabase holidaysDatabase = new HolidaysDatabase(this);

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_holiday);

        mConfirmButton = findViewById(R.id.buttonConfirm);
        mEnterName = findViewById(R.id.enterName);
        mEnterDate = findViewById(R.id.enterDate);
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String dateformat = "yyyymmdd";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + dateformat.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(6,8));
                        int mon  = Integer.parseInt(clean.substring(4,6));
                        int year = Integer.parseInt(clean.substring(0,4));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",year, mon, day);
                    }

                    clean = String.format("%s-%s-%s", clean.substring(0, 4),
                            clean.substring(4, 6),
                            clean.substring(6, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    mEnterDate.setText(current);
                    mEnterDate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        mEnterDate.addTextChangedListener(tw);

        mConfirmButton.setOnClickListener(view -> {
            if (mEnterName.getText().toString().trim().length() != 0 & mEnterDate.getText().toString().trim().length() != 0) {
                name = String.valueOf(mEnterName.getText());
                date = String.valueOf(mEnterDate.getText());

                holidaysDatabase.insertHoliday(date, name, name, "your family, we guess?)", "yes");
                this.finish();
            }
        });
    }
}
