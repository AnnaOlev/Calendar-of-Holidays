package com.example.coursework;

import com.applandeo.materialcalendarview.EventDay;

import java.util.Calendar;

class CustomEventDay extends EventDay {

    private String eventInfo;

    CustomEventDay(Calendar day, int imageResource, String note) {
        super(day, imageResource);
        eventInfo = note;
    }

    String getEventInfo() {
        return eventInfo;
    }
}
