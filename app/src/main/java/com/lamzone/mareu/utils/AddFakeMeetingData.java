package com.lamzone.mareu.utils;

import com.lamzone.mareu.R;
import com.lamzone.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;


public class AddFakeMeetingData {

        private static int color = R.color.colorPrimary;
        private static Calendar dateToday = Calendar.getInstance();
        private static Integer day = dateToday.get(DAY_OF_MONTH);
        private static Integer month = dateToday.get(MONTH);
        private static Integer year = dateToday.get(YEAR);
        // Hour and minutes
        private static Integer hour = dateToday.get(HOUR_OF_DAY);
        private static Integer minute1=0, minute2=45;

        private static Calendar start = new GregorianCalendar(year, month, day, hour, minute1,0);
        private static Calendar end = new GregorianCalendar(year, month, day, hour, minute2,0);
        private static Calendar startLess = new GregorianCalendar(year, month, day, 9, minute1,0);
        private static Calendar endLess = new GregorianCalendar(year, month, day, 9, 30,0);
        private static Calendar start1 = new GregorianCalendar(year, month, day-1, hour, 0,0);
        private static Calendar end1 = new GregorianCalendar(year, month, day-1, hour, minute2,0);
        private static Calendar start2 = new GregorianCalendar(year, month, day+2, hour, 0,0);
        private static Calendar end2 = new GregorianCalendar(year, month, day+2, hour, 30,0);

        public static List<Meeting> DUMMY_MEETING = Arrays.asList(
                new Meeting( color, "Salle 01", start1, end1, "Réunion software", Arrays.asList("alex@lamzone.com", "amandine@lamzone.com", "paul@lamzone.com")),
                new Meeting( color, "Salle 02", start1, end1, "Réunion software", Arrays.asList("maxime@lamzone.com", "alex@lamzone.com", "phil@lamzone.com")),
                new Meeting( color, "Salle 04", startLess, endLess, "Réunion software", Arrays.asList("karine@lamzone.com", "amandine@lamzone.com", "phil@lamzone.com")),
                new Meeting( color, "Salle 03", start, end, "Réunion software", Arrays.asList("amandine@lamzone.com", "paul@lamzone.com", "paul@lamzone.com", "maxime@lamzone.com")),
                new Meeting( color, "Salle 04", start2, end2, "Réunion software", Arrays.asList("karine@lamzone.com", "amandine@lamzone.com", "phil@lamzone.com")),
                new Meeting( color, "Salle 05", start2, end2, "Réunion software", Arrays.asList("alex@lamzone.com", "karine@lamzone.com", "paul@lamzone.com")));

    public static List<Meeting> generateFakeMeeting() {return new ArrayList<>(DUMMY_MEETING);}

 }
