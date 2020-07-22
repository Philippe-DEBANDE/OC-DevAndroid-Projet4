package com.lamzone.mareu.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.events.DeleteMeetingEvent;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.utils.CalendarTools;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.MyViewHolder> {
    //Data
    private final List<Meeting> mMeetings;
    public ListMeetingAdapter(List<Meeting> meetings) {
        this.mMeetings = meetings;
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private Calendar dateNow = Calendar.getInstance();
    private Context mContext;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.fragment_meeting_item, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SimpleDateFormat", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Meeting meeting = mMeetings.get(position);
        int dateColor, color;
        // When
        Calendar calDate = (meeting.getStartTime());
        Calendar calStartTime = meeting.getStartTime();
        Calendar calEndTime = meeting.getEndTime();
        // When -- >set the circle color
        dateColor = CalendarTools.dateColor(dateNow, calStartTime, calEndTime);
        meeting.setColor(dateColor);
        color = ContextCompat.getColor(mContext, dateColor);
        holder.circleColor_iv.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN);
        String nowDate = dateFormat.format(dateNow.getTime());
        String meetingDate = dateFormat.format(calDate.getTime());
        String meetingStartTime = timeFormat.format(calStartTime.getTime());
        String meetingEndTime = timeFormat.format(calEndTime.getTime());

        String meetingWhen = meetingDate+" - "+meetingStartTime+" -> "+meetingEndTime;
        holder.meetingWhen_tv.setText(meetingWhen);

        // What and Where
        String aboutItem = (meeting.getAbout()+" - "+meeting.getRoomName());
        holder.meetingAbout_tv.setText(aboutItem);

        // Who
        List temp = meeting.getAttendee();
        String attendeeListString = String.join(", ", temp);
        holder.meetingAttendee_tv.setText(attendeeListString);

        // Delete meeting
        holder.mDeleteButton.setOnClickListener(
                v->EventBus.getDefault().post(new DeleteMeetingEvent(meeting)));
    }

    @Override
    public int getItemCount() {return mMeetings.size();}

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circle_item) public ImageView circleColor_iv;
        @BindView(R.id.meetingWhen_item) public TextView meetingWhen_tv;
        @BindView(R.id.meetingAbout_item) public TextView meetingAbout_tv;
        @BindView(R.id.meetingAttendee_item) public TextView meetingAttendee_tv;
        @BindView(R.id.delete_Meeting) public ImageButton mDeleteButton;
        public MyViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
        };
    }

}