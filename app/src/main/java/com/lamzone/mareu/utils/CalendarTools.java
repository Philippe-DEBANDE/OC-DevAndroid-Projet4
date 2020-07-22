package com.lamzone.mareu.utils;

import com.lamzone.mareu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

public class CalendarTools {

    public static int dateColor(Calendar dateNow, Calendar startTime, Calendar endTime) {
        // Init the circle to the future
        int dateColor = R.color.colorMeetDateAfter;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Strings To compare Date Equality
        String nowDate = dateFormat.format(dateNow.getTime());
        String meetingDate = dateFormat.format(startTime.getTime());
        // Setting Color Tests
        // Condition 1 : Dates equal --> Go further and test Hour and minutes
        if (nowDate.equals(meetingDate)) {
            if (endTime.get(HOUR_OF_DAY) < dateNow.get(HOUR_OF_DAY)) {
                dateColor = R.color.colorMeetDateBefore;
            } else if (endTime.get(HOUR_OF_DAY) == dateNow.get(HOUR_OF_DAY)) {
                if (endTime.get(MINUTE) > dateNow.get(MINUTE)) {
                    // Ending of the meeting in the hour
                    dateColor = R.color.colorMeetDateSoon;
                } else {
                    // The meeting is passed
                    dateColor = R.color.colorMeetDateBefore;
                }
            } else {
                // The Meeting is coming in few next hours
                dateColor = R.color.colorMeetDateSoon;
            }

        }
        // Else Condition 1 : Dates not equal --> Test For passed date
        else {
            if (startTime.before(dateNow)) {
                dateColor = R.color.colorMeetDateBefore;
            }
        }
        return dateColor;
    }

    public static boolean validTimes(Calendar startTime, Calendar endTime) {
        boolean validTimes = false;
        Calendar minTime;
        minTime=(Calendar) startTime.clone();
        minTime.add(Calendar.MINUTE, 29);
        if (endTime.after(minTime))
        {
            validTimes=true;
        }
        return validTimes;
    }
}