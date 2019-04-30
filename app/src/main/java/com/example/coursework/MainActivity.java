package com.example.coursework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.applandeo.materialcalendarview.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnDayClickListener(eventDay -> {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = eventDay.getCalendar();
            Date date = calendar.getTime();
            String dateString = format.format(date);
            Intent intent = new Intent(this, TodayHolidaysActivity.class);
            intent.putExtra("clickedDate", dateString);
            startActivity(intent);
            //Toast.makeText(getApplicationContext(), dateString, Toast.LENGTH_SHORT).show(); // тупо тест жмакалки
        });
    }
}
