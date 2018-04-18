package com.example.dummy.campusnavforblind;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dummy.campusnavforblind.ReminderDatabasePackage.ReminderDbConfig;
import com.example.dummy.campusnavforblind.ReminderDatabasePackage.ReminderDbHelper;


public class ReminderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmAdapter mCursorAdapter;
    ReminderDbHelper alarmReminderDbHelper = new ReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;
    TextView reminderText;

    private String alarmTitle = "";

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

      /*  mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name); */


        reminderListView = (ListView) findViewById(R.id.list);
        // View emptyView = findViewById(R.id.empty_view);
        // reminderListView.setEmptyView(emptyView);


        // reminderText = (TextView) findViewById(R.id.reminderText);




        mCursorAdapter = new AlarmAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {



                Intent intent = new Intent(ReminderActivity.this, AddReminderActivity.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(ReminderDbConfig.ReminderEntry.CONTENT_URI,id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });


        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);

        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddReminderActivity.class);
                startActivity(intent);
                // addReminderTitle();
            }
        });

        getSupportLoaderManager().initLoader(VEHICLE_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ReminderDbConfig.ReminderEntry._ID,
                ReminderDbConfig.ReminderEntry.KEY_TITLE,
                ReminderDbConfig.ReminderEntry.KEY_DATE,
                ReminderDbConfig.ReminderEntry.KEY_TIME
                /*  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                  AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                  AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE */

        };

        return new CursorLoader(this,   // Parent activity context
                ReminderDbConfig.ReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
      /*  if (cursor.getCount() > 0){
            reminderText.setVisibility(View.VISIBLE);
        }else{
            reminderText.setVisibility(View.INVISIBLE);
        } */

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    public void restartLoader(){
        getSupportLoaderManager().restartLoader(VEHICLE_LOADER, null, this);
    }
}
