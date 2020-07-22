package com.lamzone.mareu.ui;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static java.util.Calendar.HOUR_OF_DAY;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hourOfDay, minute;
        Calendar actualTime = Calendar.getInstance();
        Calendar actualTimeRounded = Calendar.getInstance();
        int roundedMinutes = actualTimeRounded.get(Calendar.MINUTE) % 15;
        actualTimeRounded.add(Calendar.MINUTE, roundedMinutes > 0 ? (15 - roundedMinutes) : 0);
        // -----> selectedTime = dateValue;
        Calendar selectedTime = actualTime;
        hourOfDay = actualTime.get(HOUR_OF_DAY);
        minute=actualTimeRounded.MINUTE;
        selectedTime.set(HOUR_OF_DAY, hourOfDay);
        //Taking Rounded Minute
        selectedTime.set(actualTimeRounded.MINUTE, minute);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getContext(),this, hourOfDay, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute ) {
        //Action
    }
}