package com.lamzone.mareu.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.events.MyCustomEvent;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.model.StateVO;
import com.lamzone.mareu.utils.MeetingUtils;
import com.lamzone.mareu.utils.ui.MyAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lamzone.mareu.utils.CalendarTools.validTimes;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

public class AddMeetingActivity extends AppCompatActivity {
    @BindView(R.id.roomName_spinner) Spinner roomNameSpinner;
    private DatePickerDialog mDatePickerDialog;
    @BindView(R.id.date_Lyt) TextInputLayout dateInputLayout;
    @BindView(R.id.date_It) TextInputEditText dateInputText;
    @BindView(R.id.startTime_Lyt) TextInputLayout startTimeInputLayout;
    @BindView(R.id.startTime_It) TextInputEditText starTimeInputText;
    @BindView(R.id.endTime_Lyt) TextInputLayout endTimeInputLayout;
    @BindView(R.id.endTime_It) TextInputEditText endTimeInputText;
    @BindView(R.id.date_imageView) ImageView dateImageview;
    @BindView(R.id.startTime_imageView) ImageView startTimeImageview;
    @BindView(R.id.endTime_imageView) ImageView endTimeImageview;
    @BindView(R.id.aboutMeeting_Lyt) TextInputLayout aboutMeetingInputLayout;
    @BindView(R.id.aboutMeeting_It) TextInputEditText aboutMeetingInputText;
    @BindView(R.id.addAttendeeFab) FloatingActionButton addAttenbeeFab;
    @BindView(R.id.attendees_spinner) Spinner attendeesSpinner;
    @BindView(R.id.totalAttendee_Tv) TextView totalAttendees;
    @BindView(R.id.attendee_Lyt) TextInputLayout attendeeInputLayout;
    @BindView(R.id.attendee_It) TextInputEditText attendeeInputText;
    @BindView(R.id.btnAdd) Button btnAdd;
    @BindView(R.id.btnCancel) Button btnCancel;
    Calendar actualDate = Calendar.getInstance();
    Calendar dateValue, actualTime, actualTimeRounded;
    Calendar startTimeValue, endTimeValue;
    Calendar selectedTime;
    int roomNamePosition, totalSelectedAttendees = 0;
    String roomNameSelected, attendees;
    List<String> selectedAttendees = new ArrayList<>();
    List<Meeting> meetings = DI.getMeetingApiService().getMeetings(null, "");
    List<String> listAttendees;
    ArrayList<StateVO> listVOs = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration config = getResources().getConfiguration();
        if (config.screenHeightDp <= 500)
        {
            setContentView(R.layout.activity_add_meeting_small_land);
        }
        else
        {
            setContentView(R.layout.activity_add_meeting);
        }
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Spinner roomName Init
        List<String> rooms = DI.getMeetingApiService().getRooms();
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
           this, android.R.layout.simple_spinner_dropdown_item, rooms) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {// Disable the first item from Spinner, first item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomNameSpinner.setAdapter(spinnerArrayAdapter);
        roomNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roomNamePosition = position;
                if (starTimeInputText.length() > 0 && endTimeInputText.length() > 0) {
                    // Making a filter on the roomName
                    List<Meeting> tmp = MeetingUtils.getMeetingsMatchRoomName(roomNameSpinner.getSelectedItem().toString(), meetings);
                    // Testing Meeting Date not taken on the filter list ;-)
                    boolean goodMeetingroomName = MeetingUtils.goodMeetingroomName(roomNameSpinner.getSelectedItem().toString(), startTimeValue, endTimeValue, tmp);
                    testGoodMeetingRoomName(goodMeetingroomName);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Spinner Attendees Init
        listAttendees = DI.getMeetingApiService().getAttendees();
        for (int i = 0; i < listAttendees.size(); i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(listAttendees.get(i));
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(AddMeetingActivity.this, 0, listVOs);
        attendeesSpinner.setAdapter(myAdapter);
        dateInputText.setFocusable(false);
        starTimeInputText.setFocusable(false);
        endTimeInputText.setFocusable(false);
        setSelectedAttendeesTvColor();
        aboutMeetingInputText.setFocusable(false);
        attendeesSpinner.setEnabled(false);
        attendeeInputText.setFocusable(false);
        attendeeInputText.setFocusableInTouchMode(false);
        btnAdd.setEnabled(false);
        inputDateToday();
        init();
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
            aboutMeetingInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                attendeesSpinner.setEnabled(true);
                attendeeInputText.setFocusable(true);
                attendeeInputText.setFocusableInTouchMode(true);
            }
        });
    }

    @OnClick({R.id.date_imageView})
    public void displayDatePicker(View v) {
        if (roomNameSpinner.getSelectedItemPosition() > 0) {
            actualDate.get(YEAR);
            actualDate.get(Calendar.MONTH);
            actualDate.get(Calendar.DAY_OF_MONTH);
            mDatePickerDialog = new DatePickerDialog(AddMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        dateValue = (Calendar) selectedDate.clone();
                        dateInputText.setText(DateFormat.getDateFormat(getApplicationContext()).format(selectedDate.getTime()));
                        //Testing the date is not before -> error + emptying DateInputText
                        if (selectedDate.before(actualDate)) {
                            dateInputLayout.setError(getString(R.string.passed_Date));
                            dateInputText.setText("");
                        }
                    },
            actualDate.get(YEAR),
            actualDate.get(Calendar.MONTH),
            actualDate.get(Calendar.DAY_OF_MONTH));
            // Disable Date IntputLayout Error
            dateInputLayout.setErrorEnabled(false);
            inputDateToday();
            mDatePickerDialog.setMessage(getString(R.string.select_Date));
            mDatePickerDialog.show();
        } else {affectSpinnerError();}
    }

    @OnClick({R.id.startTime_imageView, R.id.endTime_imageView})
    void displayTimePicker(View v) {
        // Meeting times input : checking if there is a  roomName
        if (roomNameSpinner.getSelectedItemPosition() > 0) {
            final int id = v.getId();
            actualTime = Calendar.getInstance();
            actualTimeRounded = Calendar.getInstance();
            int roundedMinutes = actualTimeRounded.get(MINUTE) % 15;
            actualTimeRounded.add(Calendar.MINUTE, roundedMinutes > 0 ? (15 - roundedMinutes) : 0);
            TimePickerDialog mTimePickerDialog = new TimePickerDialog(AddMeetingActivity.this, android.R.style.Theme_Holo_Light_Dialog, (view, hourOfDay, minute) -> {
                // Calendar selectedTime setting -> Setting the Date
                selectedTime = actualTime;
                // Setting Hour and RoundMinute
                selectedTime.set(HOUR_OF_DAY, hourOfDay);
                selectedTime.set(MINUTE, minute);
                selectedTime.set(SECOND, 00);
                if (id == R.id.startTime_imageView) {
                    startTimeValue = (Calendar) selectedTime.clone();
                    startTimeValue.set(YEAR, dateValue.get(YEAR));
                    startTimeValue.set(MONTH, dateValue.get(MONTH));
                    startTimeValue.set(DAY_OF_MONTH, dateValue.get(DAY_OF_MONTH));
                    startTimeValue.set(HOUR_OF_DAY, selectedTime.get(HOUR_OF_DAY));
                    startTimeValue.set(MINUTE, selectedTime.get(MINUTE));
                    startTimeValue.set(SECOND, 00);
                    // Testing the time is not before -> error + emptying startTimeInputText
                    if (startTimeValue.before(actualDate) && startTimeValue.get(DAY_OF_MONTH) == actualDate.get(DAY_OF_MONTH)) {
                        startTimeInputLayout.setError(getString(R.string.passed_Date));
                        starTimeInputText.setText("");
                    } else {
                        // Time Ok Displaying
                        starTimeInputText.setText(DateFormat.getTimeFormat(getApplicationContext()).format(selectedTime.getTime()));
                    }
                } else if (id == R.id.endTime_imageView) {
                    if (starTimeInputText.length() != 0) {
                        endTimeValue = (Calendar) selectedTime.clone();
                        endTimeValue.set(YEAR, dateValue.get(YEAR));
                        endTimeValue.set(MONTH, dateValue.get(MONTH));
                        endTimeValue.set(DAY_OF_MONTH, dateValue.get(DAY_OF_MONTH));
                        endTimeValue.set(HOUR_OF_DAY, selectedTime.get(HOUR_OF_DAY));
                        endTimeValue.set(MINUTE, selectedTime.get(MINUTE));
                        endTimeValue.set(SECOND, 00);
                        // Testing if endTimeValue is before startTimeValue
                        if (endTimeValue.before(startTimeValue)) {
                            endTimeInputLayout.setError(getString((R.string.lower_time)));
                            endTimeInputText.setText("");
                        } else {
                            // endTimeValue is after startTimeValue
                            endTimeInputText.setText(DateFormat.getTimeFormat(getApplicationContext()).format(endTimeValue.getTime()));
                            // Testing Meeting duration > 30 min
                            if (validTimes(startTimeValue, endTimeValue)) {
                                    // Testing meeting Validity
                                    // Making a filter on the roomName
                                    List<Meeting> tmp = MeetingUtils.getMeetingsMatchRoomName(roomNameSpinner.getSelectedItem().toString(), meetings);
                                    // Testing Meeting Date not taken on the filter list ;-)
                                    boolean goodMeetingroomName = MeetingUtils.goodMeetingroomName(roomNameSpinner.getSelectedItem().toString(), startTimeValue, endTimeValue, tmp);
                                    testGoodMeetingRoomName(goodMeetingroomName);
                            } else if (!validTimes(startTimeValue, endTimeValue)) {
                                endTimeInputLayout.setError(getString(R.string.tooShort_Meeting));
                                endTimeInputText.setText("");
                            }
                        }
                    } else if (starTimeInputText.length() == 0) {
                        endTimeInputLayout.setError(getString(R.string.select_StartTime));
                        endTimeInputText.setText("");
                    }
                }
            },
                    actualTimeRounded.get(HOUR_OF_DAY),
                    actualTimeRounded.get(MINUTE),
                    DateFormat.is24HourFormat(AddMeetingActivity.this));
            // Disable Date IntputLayout Error
            if (id == R.id.startTime_imageView) {
                startTimeInputLayout.setErrorEnabled(false);
                mTimePickerDialog.setMessage(getString(R.string.select_StartTime));
            } else if (id == R.id.endTime_imageView) {
                endTimeInputLayout.setErrorEnabled(false);
                mTimePickerDialog.setMessage(getString(R.string.select_EndTime));
            }
            mTimePickerDialog.setMessage(getString(R.string.select_Time));
            mTimePickerDialog.show();
        }
        else {affectSpinnerError();}
    }

    //Reset InputLayout error with click
    @OnClick({R.id.date_It, R.id.startTime_It, R.id.endTime_It, R.id.attendee_It,R.id.aboutMeeting_It})
    void resetError(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.attendee_It:
                if (roomNameSpinner.getSelectedItemPosition() == 0) {
                    affectSpinnerError();
                }
                attendeeInputLayout.setError("");
                break;
            case R.id.startTime_It:
                startTimeInputLayout.setError("");
                break;
            case R.id.endTime_It:
                endTimeInputLayout.setError("");
                break;
            case R.id.date_It:
                dateInputLayout.setError("");
                break;
            case R.id.aboutMeeting_It:
                if (roomNameSpinner.getSelectedItemPosition() == 0) {
                    affectSpinnerError();
                }
                aboutMeetingInputLayout.setError("");
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.addAttendeeFab)
    void addAttendee(View v) {
        String email = attendeeInputText.getText().toString();
        // Test Email validity
        if (email.matches("^[_a-z]{3,}[_a-z0-9]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$")) {
            // Test if the new Attendee is in the list...
            if (!listAttendees.contains(email)) {
                listAttendees.add(attendeeInputText.getText().toString());
                StateVO stateVO = new StateVO();
                stateVO.setTitle(email);
                // Set the new Email Selected
                stateVO.setSelected(true);
                listVOs.add(stateVO);
                Collections.sort(listVOs, StateVO::compareTo);
                totalSelectedAttendees=++totalSelectedAttendees;
                setSelectedAttendeesTvColor();
                attendeeInputLayout.setError("");
            } else {
                attendeeInputLayout.setError(getString(R.string.existing_Email));
            }

        } else {
            attendeeInputLayout.setError(getString(R.string.incorrect_Email));
        }
        attendeeInputText.setText("");
    }

    @OnClick(R.id.btnCancel)
    void cancel() {
        finish();
    }

    public void testGoodMeetingRoomName(Boolean goodMeetingroomName) {
        if (!goodMeetingroomName) {
            //affectSpinnerError();
            setSpinnerError(roomNameSpinner, getString(R.string.conflict_Meeting));
            if (btnAdd.isEnabled()) {
                btnAdd.setEnabled(false);
            }
        } else {
            //Reseting spinner Error
            roomNameSelected = roomNameSpinner.getSelectedItem().toString();
            setSpinnerError(roomNameSpinner, null);
            roomNameSpinner.setSelection(roomNamePosition);
            // Enabling the second part inputs
            aboutMeetingInputText.setFocusable(true);
            aboutMeetingInputText.setFocusableInTouchMode(true);
            // Verifying that Second part isn't empty and enable the button
            if (aboutMeetingInputText.getText().toString() != "" && (totalSelectedAttendees > 1)) {
                if (!btnAdd.isEnabled()) {
                    btnAdd.setEnabled(true);
                    }
                }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick(R.id.btnAdd)
    void addMeeting() {
        // Add Meeting with inputs values
        Integer color = R.color.colorPrimary;
            // Get Attendees multiselect
            for (int i = 1; i < listVOs.size(); i++) {
                if (listVOs.get(i).isSelected()) {
                    selectedAttendees.add(listVOs.get(i).getTitle());
                }
            }
            attendees = String.join(", ", selectedAttendees);
            Meeting meeting = new Meeting(color, roomNameSpinner.getSelectedItem().toString(), startTimeValue, endTimeValue, aboutMeetingInputText.getText().toString(), selectedAttendees);
            DI.getMeetingApiService().addMeeting(meeting);
            finish();
    }

    private void affectSpinnerError() {
        if (roomNameSpinner.getSelectedItemPosition() != 0) {
            setSpinnerError(roomNameSpinner, getString(R.string.conflict_Meeting));
        } else if (roomNameSpinner.getSelectedItemPosition() == 0) {
            setSpinnerError(roomNameSpinner, getString(R.string.select_Room));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Subscribe
    public void customEventReceived(MyCustomEvent event) {
        if (event.getEventAttendee().equals("Add")) {
            totalSelectedAttendees=++totalSelectedAttendees;
            }
        else  if (event.getEventAttendee().equals("Remove")) {
            totalSelectedAttendees=--totalSelectedAttendees;
            }
        setSelectedAttendeesTvColor();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setSelectedAttendeesTvColor() {
        if (totalSelectedAttendees < 2 ) {
            totalAttendees.setTextColor(Color.RED);
            // Disable Validation
            btnAdd.setEnabled(false);}
        else
            {totalAttendees.setTextColor(ContextCompat.getColor(this,R.color.colorGreen));
            // Make the validation possible...
            btnAdd.setEnabled(true);}
        totalAttendees.setText(getString(R.string.total_Attendees)+totalSelectedAttendees);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void inputDateToday() {
        dateInputText.setText(DateFormat.getDateFormat(getApplicationContext()).format(actualDate.getTime()));
        dateValue = actualDate;
    }

    private void setSpinnerError(Spinner spinner, String error) {
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            if (error == null)
            {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError(null);
                selectedTextView.setTextColor(Color.BLACK);
                selectedTextView.setText(roomNameSelected); // return Text
            }
            else
            {
                TextView selectedTextView = (TextView) selectedView;
                selectedTextView.setError(error); // any name of the error will do
                selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
                selectedTextView.setText(error);} // actual error message
        }
    }
    /**
     * Used to navigate to this activity
     * @param activity
     */
    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
