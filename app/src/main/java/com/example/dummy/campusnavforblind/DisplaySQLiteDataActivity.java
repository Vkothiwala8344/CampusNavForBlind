package com.example.dummy.campusnavforblind;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.dummy.campusnavforblind.SQLiteHelper.DATABASE_NAME;


public class DisplaySQLiteDataActivity extends AppCompatActivity {
    SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    ListAdapter listAdapter ;
    ListView LISTVIEW;

    ArrayList<String> ID_Array;
    ArrayList<String> SUBJECT_Array;
    ArrayList<String> PROFESSOR_Array;
    ArrayList<String> ROOM_Array;
    ArrayList<String> DAY_Array;
    ArrayList<String> TYPE_Array;
    ArrayList<String> START_Array;
    ArrayList<String> END_Array;

    ArrayList<String> ListViewClickItemArray = new ArrayList<String>();
    String TempHolder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sqlite_data);

        LISTVIEW = (ListView) findViewById(R.id.listView1);

        ID_Array = new ArrayList<String>();

        SUBJECT_Array = new ArrayList<String>();

        PROFESSOR_Array = new ArrayList<String>();

        ROOM_Array = new ArrayList<String>();

        DAY_Array = new ArrayList<String>();
        TYPE_Array = new ArrayList<String>();
        START_Array = new ArrayList<String>();
        END_Array = new ArrayList<String>();

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), DATABASE_NAME, this, 1);

        LISTVIEW.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                Intent intent = new Intent(getApplicationContext(),ShowSingleRecordActivity.class);

                intent.putExtra("ListViewClickedItemValue", ListViewClickItemArray.get(position).toString());

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {

        ShowSQLiteDBdata() ;

        super.onResume();
    }

    private void ShowSQLiteDBdata() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SQLiteHelper.TABLE_NAME+"", null);

        ID_Array.clear();
        SUBJECT_Array.clear();
        PROFESSOR_Array.clear();
        ROOM_Array.clear();
        DAY_Array.clear();
        TYPE_Array.clear();
        START_Array.clear();
        END_Array.clear();

        if (cursor.moveToFirst()) {
            do {

                ID_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                //Inserting Column ID into Array to Use at ListView Click Listener Method.
                ListViewClickItemArray.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));

                SUBJECT_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1)));

                PROFESSOR_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2)));

                ROOM_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3)));

                DAY_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4)));

                TYPE_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5)));
                START_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6)));
                END_Array.add(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_7)));


            } while (cursor.moveToNext());
        }

        listAdapter = new ListAdapter(DisplaySQLiteDataActivity.this,

                ID_Array,
                SUBJECT_Array,
                PROFESSOR_Array,
                ROOM_Array,
                DAY_Array,
                TYPE_Array,
                START_Array,
                END_Array
        );

        LISTVIEW.setAdapter(listAdapter);

        cursor.close();
    }
}


