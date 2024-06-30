package com.example.myapplication;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static String formatReminderTime(long reminderTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date resultDate = new Date(reminderTime);
        return sdf.format(resultDate);
    }
}