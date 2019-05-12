package com.example.coursework;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.Button;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MainActivity extends AppCompatActivity implements ChosenDayDialog.OnCompleteListener{

    private final static int REQUEST_CODE = 1;
    List<EventDay> events = new ArrayList<>();
    List<Holiday> holidays = new ArrayList<>();
    HolidaysDatabase holidaysDatabase = new HolidaysDatabase(this);
    CountriesDatabase countriesDatabase = new CountriesDatabase(this);
    Calendar calendar;
    CalendarView mCalendarView;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*holidays = holidaysDatabase.getAllHolidays();
        for (int i = 0; i < holidays.size(); i++){
            Date date = null;
            Holiday holiday = holidays.get(i);
            if (holiday.getFavourite().equals("yes") && countriesDatabase.getDataByCode(holiday.getCountryCode())){
                try {
                    date = format.parse(holidays.get(i).getDate());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    CustomEventDay customEventDay = new CustomEventDay(calendar, R.drawable.ic_action_name, holiday.toString());
                    events.add(customEventDay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }*/

        mCalendarView = findViewById(R.id.calendarView);

        /*if (!events.isEmpty())
            mCalendarView.setEvents(events);*/

        mCalendarView.setOnDayClickListener(eventDay -> {

            calendar = eventDay.getCalendar();
            Date date = calendar.getTime();
            String dateString = format.format(date);
            // дата так подробно для наглядности, возможно потом уберу
            ChosenDayDialog chosenDayDialog = new ChosenDayDialog();
            chosenDayDialog.show(getSupportFragmentManager(), "dialog");
            Bundle bundle = new Bundle();
            bundle.putString("CURRENT_DAY_DATA", dateString);
            String todayInfoString = "";
            for (int i = 0; i < events.size(); i++){
                if (events.get(i).getCalendar() == calendar) {
                    CustomEventDay customEventDay = (CustomEventDay) events.get(i);
                    todayInfoString += customEventDay.getEventInfo() + " ";
                }
            }
            bundle.putString("EVENT_INFO", todayInfoString);
            chosenDayDialog.setArguments(bundle);
        });

        Button button = findViewById(R.id.toSettings);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), CountryChoiceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The returned result data is identified by requestCode.
        // The request code is specified in startActivityForResult(intent, REQUEST_CODE_1); method.
        switch (requestCode)
        {
            // This request code is set by startActivityForResult(intent, REQUEST_CODE_1) method.
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    String messageReturn = dataIntent.getStringExtra("message_return");
                    CustomEventDay eventDay = new CustomEventDay(calendar, R.drawable.ic_action_name, messageReturn);
                    events.add(eventDay);
                    mCalendarView.setEvents(events);
                }
        }
    }

    public void onComplete (Intent intent){
        startActivityForResult(intent, REQUEST_CODE);
    }
}
