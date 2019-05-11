package com.example.coursework;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

public class CustomEventDay extends EventDay {

    private String eventInfo;

    CustomEventDay(Calendar day, int imageResource, String note) {
        super(day, imageResource);
        eventInfo = note;
    }
}
