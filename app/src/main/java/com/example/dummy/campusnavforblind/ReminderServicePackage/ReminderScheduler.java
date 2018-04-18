package com.example.dummy.campusnavforblind.ReminderServicePackage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class ReminderScheduler {

    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        AlarmManager manager = ReminderManager.getAlarmManager(context);

        PendingIntent operation =
                ReminderService.getReminderPendingIntent(context, reminderTask);


        if (Build.VERSION.SDK_INT >= 23) {

            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else if (Build.VERSION.SDK_INT >= 19) {

            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        } else {

            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);

        }
    }
}
