package com.example.dummy.campusnavforblind;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dummy.campusnavforblind.ReminderDatabasePackage.ReminderDbConfig;
import com.example.dummy.campusnavforblind.ReminderDatabasePackage.ReminderDbHelper;
import com.example.dummy.campusnavforblind.ReminderServicePackage.ReminderScheduler;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class AddReminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_VEHICLE_LOADER = 0;
    SQLiteDatabase sqLiteDatabaseObj;
    ReminderDbHelper sqliteHelper;
    Cursor cursor;

    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    //  private String mRepeat;
    //   private String mRepeatNo;
    //   private String mRepeatType;
    //   private String mActive;

    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    // private static final String KEY_REPEAT = "repeat_key";
    //  private static final String KEY_REPEAT_NO = "repeat_no_key";
    //  private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    //  private static final String KEY_ACTIVE = "active_key";


    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();


        if (mCurrentReminderUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_reminder));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a reminder that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {

            setTitle(getString(R.string.editor_activity_title_edit_reminder));


            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }


        // Initialize Views

        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);


        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        //   mRepeatNoText.setText(mRepeatNo);
        //    mRepeatTypeText.setText(mRepeatType);
        //   mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;


        }

      /*  setSupportActionBar(mToolbar); */
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
      /*  outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive); */
    }

    // On clicking Time picker
    public void setTime(View v){
       /* if(mCurrentReminderUri == null){
            Toast.makeText(this, "click again on the reminder list to set time alarm", Toast.LENGTH_LONG).show();
            return;
        } */
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
       /* if(mCurrentReminderUri == null){
            Toast.makeText(this, "click again on the reminder list to set date alarm", Toast.LENGTH_LONG).show();
            return;
        } */
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new reminder, hide the "Delete" menu item.
        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.save_reminder:


                if (mTitleText.getText().toString().length() == 0){
                    mTitleText.setError("Reminder Title cannot be blank!");
                }

                else {
                    saveReminder();

                   Intent i = new Intent(AddReminderActivity.this,ActivityHomePage.class);
                   startActivity(i);
                }
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.discard_reminder:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the reminder hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!mVehicleHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteReminder() {
        // Only perform the delete if this is an existing reminder.
        if (mCurrentReminderUri != null) {
            // Call the ContentResolver to delete the reminder at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentreminderUri
            // content URI already identifies the reminder that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        Intent i = new Intent(AddReminderActivity.this,ActivityHomePage.class);
        startActivity(i);

        // Close the activity
        //finish();
    }

    // On clicking the save button
    public void saveReminder(){

       /* if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        } */


        ContentValues values = new ContentValues();

        values.put(ReminderDbConfig.ReminderEntry.KEY_TITLE, mTitle);
        values.put(ReminderDbConfig.ReminderEntry.KEY_DATE, mDate);
        values.put(ReminderDbConfig.ReminderEntry.KEY_TIME, mTime);
       /* values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive); */


        // Set up calender for creating the notification
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        long selectedTimestamp =  mCalendar.getTimeInMillis();


        if (mCurrentReminderUri == null) {
            // This is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            Uri newUri = getContentResolver().insert(ReminderDbConfig.ReminderEntry.CONTENT_URI, values);
            int record_id=0;
            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Reminder saved",
                        Toast.LENGTH_SHORT).show();

                sqLiteDatabaseObj = openOrCreateDatabase("remindermodule.db", Context.MODE_PRIVATE, null);
                sqliteHelper = new ReminderDbHelper(this);
                sqLiteDatabaseObj = sqliteHelper.getWritableDatabase();

                cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM " + ReminderDbConfig.ReminderEntry.TABLE_NAME + " ", null);

                if (cursor.moveToLast()) {

                    record_id =  cursor.getInt(0);

                    cursor.close();

                }
                // Toast.makeText(this, "ID="+record_id, Toast.LENGTH_SHORT).show();


                mCurrentReminderUri = ContentUris.withAppendedId(ReminderDbConfig.ReminderEntry.CONTENT_URI, record_id);
            }

        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Create a new notification
     /*  if (mActive.equals("true")) {
            if (mRepeat.equals("true")) {
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat.equals("false")) {
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        } */

       // Toast.makeText(this, "Alarm time is " + selectedTimestamp,Toast.LENGTH_LONG).show();
       /* Toast.makeText(this, "URI " + mCurrentReminderUri,
                Toast.LENGTH_LONG).show(); */


        new ReminderScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);


        // Create toast to confirm new reminder
      /*  Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show(); */

    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }




    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                ReminderDbConfig.ReminderEntry._ID,
                ReminderDbConfig.ReminderEntry.KEY_TITLE,
                ReminderDbConfig.ReminderEntry.KEY_DATE,
                ReminderDbConfig.ReminderEntry.KEY_TIME,
                /*  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                  AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, */
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentReminderUri,         // Query the content URI for the current reminder
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_TIME);
       /*     int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE); */

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
           /* String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex); */



            // Update the views on the screen with the values from the database
            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
       /*     mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);
            mRepeatText.setText("Every " + repeatNo + " " + repeatType + "(s)");
            // Setup up active buttons
            // Setup repeat switch
            if (repeat == null){
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);
            }
            else if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            } */

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
