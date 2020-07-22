package com.lamzone.mareu.utils;

import com.lamzone.mareu.model.Meeting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MeetingUtils {
    /**
     * Return ordered meetings list filter by room name
     *
     * @param roomName selected room name
     * @param meetings meetings list to filter
     * @return filtered meetings list
     */
    public static List<Meeting> getMeetingsMatchRoomName(String roomName, List<Meeting> meetings) {
        List<Meeting> tmp = new ArrayList<>();
        for (Meeting m : meetings)
            if (m.getRoomName().trim().equals(roomName.trim()))
                tmp.add(m);
        Collections.sort(tmp);
        return tmp;
    }

    /**
     * Return ordered meetings list filter by date
     * @param date selected date
     * @param meetings meetings list to filter
     * @return filtered meetings list
     */
    public static List<Meeting> getMeetingsMatchDate(Calendar date, List<Meeting> meetings) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String mdate, mdateStart;
        List<Meeting> tmp = new ArrayList<>();

        for (Meeting m: meetings) {
            mdate = dateFormat.format(date.getTime());
            mdateStart = dateFormat.format(m.getStartTime().getTime());
            if (mdateStart.equals(mdate))
                tmp.add(m);
        }
        Collections.sort(tmp);
        return tmp;
    }


    /**
     * Validate a new meeting with roomName, startTime, endTime,
     * this to know if the user need to choose another date before input all the data before
     * meeting validation
     * @param roomName selected rooName
     * @param startTime startTime user input
     * @param endTime  endTime user input
     * @param tmp filtered with the rommName roomName and ordered
     */
    public static boolean goodMeetingroomName(String roomName, Calendar startTime, Calendar endTime, List<Meeting>  tmp) {
        boolean goodMeetingroomName=true;
        int conflictCount = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Strings To compare Date Equality
        String startDate = dateFormat.format(startTime.getTime());

        //Go across the list tmp filtered and sorted
        for (Meeting m : tmp)
            {
            String meetingDate = dateFormat.format(m.getStartTime().getTime());

             if (startTime.getTime().before(m.getStartTime().getTime()) && (endTime.getTime().before(m.getStartTime().getTime()))
                || (startTime.getTime().after(m.getEndTime().getTime())) && (endTime.getTime().after(m.getEndTime().getTime())))
                 {

                 }
            else if (startTime.getTime().after(m.getStartTime().getTime()) || (endTime.getTime().before(m.getEndTime().getTime())) || (startTime.getTime().equals(m.getStartTime().getTime()))  || (endTime.getTime().equals(m.getEndTime().getTime())) )
                 {
                 conflictCount = ++conflictCount;
                 }
            }
    if (conflictCount > 0) goodMeetingroomName = false;

    return goodMeetingroomName;
    }
 }