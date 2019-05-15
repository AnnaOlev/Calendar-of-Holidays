package com.example.coursework;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        mConfirmButton.setOnClickListener(view -> {
            name = String.valueOf(mEnterName.getText());
            date = String.valueOf(mEnterDate.getText());

            holidaysDatabase.insertHoliday(date, name, name, "your family, we guess?)", "yes");
            this.finish();
        });


    }
}
