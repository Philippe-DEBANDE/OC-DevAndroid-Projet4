package com.lamzone.mareu.model;

import java.util.Calendar;
import java.util.List;

public class Meeting implements Comparable<Meeting>{
    private int id;
    private static int lastId;
    private String roomName;
    // First step : using 3 Calendar
    // Second step : using 2 Calendar because the date is inside
    // private Calendar date;
    private Calendar startTime;
    private Calendar endTime;
    private String about;
    private List<String> attendee;
    private int color;

    /**
     * Constructor
     * @param
     * @param roomName
     * @prama date
     * @param startTime
     * @param endTime
     * @param about
     * @param attendee
     */
    public  Meeting(Integer color, String roomName, Calendar startTime, Calendar endTime, String about, List attendee) {
        id = ++lastId;
        this.roomName = roomName;
        this.startTime= startTime;
        this.endTime = endTime;
        this.about = about;
        this.attendee = attendee;
        // First step thinking about a random but saw that green events where soon
        // so decided to use three Colors :
        // Red : Passed Meeting
        // Orange : Coming soon Meeting
        // Green : Tomorrow and over Meeting
        this.color = color;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getColor() {
        return this.color;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public Calendar getStartTime() {
        return this.startTime;
    }

    public Calendar getEndTime() {
        return this.endTime;
    }

    public String getAbout() {
        return this.about;
    }

    public List getAttendee() {return this.attendee;}

    public void setColor(int color) {this.color = color;}

    /*
     * Sorting by startTime;
     */
    @Override
    public int compareTo(Meeting compareMeeting) {
        return getStartTime().compareTo(compareMeeting.getStartTime());
    }



}





