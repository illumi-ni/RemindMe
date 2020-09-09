package com.example.googleintegration;

import org.junit.Test;

import static org.junit.Assert.*;

public class AddNewTest {

    @Test
    public void setAlarm(){
    }

    @Test
    public void formatTime() {
        int hour = 0;
        int minute = 9;
        String expected = "12:09 AM";
        String output;

        AddNew ad = new AddNew();
        output = ad.FormatTime(hour, minute);

        assertEquals(expected, output);
    }

    @Test
    public void formatDate() {
        int year = 2020, month = 12, day = 8;
        String expected = "2020-12-08";
        String output;

        AddNew ad = new AddNew();
        output = ad.formatDate(year, month, day);

        assertEquals(expected, output);

    }
}
