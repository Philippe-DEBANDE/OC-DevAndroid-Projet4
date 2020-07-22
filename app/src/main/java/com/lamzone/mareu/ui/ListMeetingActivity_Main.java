package com.lamzone.mareu.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lamzone.mareu.R;
import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.events.DeleteMeetingEvent;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.service.MeetingApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListMeetingActivity_Main extends AppCompatActivity implements FilterFragment.OnButtonClickedListener {

    // Design
    @BindView(R.id.fab_addMeeting) FloatingActionButton addMeetingFab;
    @BindView(R.id.List_Meeting_rv) RecyclerView rv;

    //Data
    public static MeetingApiService sApiService;
    private List<Meeting> meetings;
    private ListMeetingAdapter adapter;
    Calendar date;
    String roomName="";

    // Create and configure
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sApiService = DI.getMeetingApiService();
        meetings=sApiService.getMeetings(null,null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        configureRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DI.getMeetingApiService().getMeetings(date, roomName);
        configureRecyclerView();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Delete the Data
        sApiService.deleteMeetings();
        DI.getMeetingApiService().getMeetings(date, roomName);
        configureRecyclerView();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // Configuration of RecyclerView
    private void configureRecyclerView() {
        rv.setAdapter(new ListMeetingAdapter(meetings));
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        DI.getMeetingApiService().deleteMeeting(event.meeting);
        Context context= getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text="";
        text = getString(R.string.del_Meeting)+event.meeting.getRoomName()+" "+getString(R.string.del_Meeting_at)+event.meeting.getStartTime().get(Calendar.DAY_OF_MONTH)+"/"+(event.meeting.getStartTime().get(Calendar.MONTH)+1)+" "+getString(R.string.del_Meeting_done);
        Toast toast_Sup_Meeting = Toast.makeText(context, text, duration);
        toast_Sup_Meeting.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast_Sup_Meeting.show();
        meetings= sApiService.getMeetings(date, roomName);
        configureRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            executeFilter();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void executeFilter() {
        FilterFragment filterDialog = new FilterFragment(sApiService.getRooms());
        filterDialog.show(getSupportFragmentManager(), "filter");
}

    @Override
    public void applyTexts(Calendar date, String roomName)
    {
        meetings = sApiService.getMeetings(date, roomName);
        configureRecyclerView();
    };

    @OnClick(R.id.fab_addMeeting)
    void addMeeting() {
           AddMeetingActivity.navigate(this);
    }

}
