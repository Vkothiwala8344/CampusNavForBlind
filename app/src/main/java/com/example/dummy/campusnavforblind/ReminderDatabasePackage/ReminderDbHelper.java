package com.example.dummy.campusnavforblind.ReminderDatabasePackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "remindermodule.db";

    private static final int DATABASE_VERSION = 1;

    public ReminderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + ReminderDbConfig.ReminderEntry.TABLE_NAME + " ("
                + ReminderDbConfig.ReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ReminderDbConfig.ReminderEntry.KEY_TITLE + " TEXT, "
                + ReminderDbConfig.ReminderEntry.KEY_DATE + " TEXT, "
                + ReminderDbConfig.ReminderEntry.KEY_TIME + " TEXT " + " );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
