package com.example.dummy.campusnavforblind;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import static com.example.dummy.campusnavforblind.SQLiteHelper.DATABASE_NAME;


public class EditSingleRecordActivity extends AppCompatActivity {

    EditText subject, professor,room,start,end;
    Button update;
    SQLiteDatabase sqLiteDatabase;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    String IDholder,format;
    String SQLiteDataBaseQueryHolder ;
    SQLiteDatabase sqLiteDatabaseObj;



    //Calendar
    Calendar calendar;

    //spinner
    Spinner dateSpin,typeSpin;

    //spinner varibale
    public String selectedDay,selectedClass;
    // String selectedClass = "Lecture";

    //varibales
    private int CalendarHour, CalendarMinute;
    TimePickerDialog timepickerdialog;

    //spinner data
    String[] daysofWeek={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String[] classType = {"Lecture","Lab"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_single_record);


        subject = (EditText) findViewById(R.id.editText);
        professor = (EditText) findViewById(R.id.professorName);
        room = (EditText)findViewById(R.id.roomNumber);
        start = (EditText)findViewById(R.id.startTime);
        end = (EditText)findViewById(R.id.endTime);


        String pos = selectedDay;
        update = (Button) findViewById(R.id.buttonUpdate);

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), DATABASE_NAME, this, 1);

        //spinner
        dateSpin = (Spinner)findViewById(R.id.daySpinner);
        typeSpin = (Spinner)findViewById(R.id.typeSpinner);

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        IDholder = getIntent().getStringExtra("EditID");

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE id = " + IDholder + "", null);

        if (cursor.moveToFirst()) {

            do {
                selectedDay = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4));
                selectedClass = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5));
                subject.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1)));
                professor.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2)));
                room.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3)));
                start.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6)));
                end.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_7)));
            }
            while (cursor.moveToNext());

            cursor.close();

            // Toast.makeText(getApplicationContext(),selectedClass,Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),selectedDay,Toast.LENGTH_SHORT).show();

        }


        //spinner
        //day picker
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,daysofWeek);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        dateSpin.setAdapter(aa);

        int spinnerPosition = aa.getPosition(selectedDay);
        // Toast.makeText(getApplicationContext(),"day1="+selectedDay,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),"postion="+spinnerPosition,Toast.LENGTH_SHORT).show();
        dateSpin.setSelection(spinnerPosition);

        dateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDay = daysofWeek[position];
                // Toast.makeText(getApplicationContext(),selectedDay,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter bb = new ArrayAdapter(this,android.R.layout.simple_spinner_item,classType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        typeSpin.setAdapter(bb);
        int spinnerPosition1 = bb.getPosition(selectedClass);
        // Toast.makeText(getApplicationContext(),"day1="+selectedDay,Toast.LENGTH_SHORT).show();
        // Toast.makeText(getApplicationContext(),selectedClass+"position="+spinnerPosition1,Toast.LENGTH_SHORT).show();

        typeSpin.setSelection(spinnerPosition1);

        typeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedClass = classType[position];
                // Toast.makeText(getApplicationContext(),selectedClass,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // start time picker
        start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(EditSingleRecordActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                start.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }

        });

        // end time picker

        end.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                calendar = Calendar.getInstance();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);


                timepickerdialog = new TimePickerDialog(EditSingleRecordActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                }
                                else if (hourOfDay == 12) {

                                    format = "PM";

                                }
                                else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                }
                                else {

                                    format = "AM";
                                }


                                end.setText(hourOfDay + ":" + minute + format);
                            }
                        }, CalendarHour, CalendarMinute, false);
                timepickerdialog.show();

            }

        });



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GetSubject = subject.getText().toString();
                String GetProfessor = professor.getText().toString();
                String GetRoom = room.getText().toString();
                String GetStart = start.getText().toString();
                String GetEnd = end.getText().toString();

                OpenSQLiteDataBase();

                SQLiteDataBaseQueryHolder = "UPDATE " + SQLiteHelper.TABLE_NAME + " SET "+SQLiteHelper.Table_Column_1+" = '"+GetSubject+"' , "+SQLiteHelper.Table_Column_2+" = '"+GetProfessor+"' , "+SQLiteHelper.Table_Column_3+" = '"+GetRoom+"' , "+SQLiteHelper.Table_Column_4+" = '"+selectedDay+"' , "+SQLiteHelper.Table_Column_5+" = '"+selectedClass+"' , "+SQLiteHelper.Table_Column_6+" = '"+GetStart+"' , "+SQLiteHelper.Table_Column_7+" = '"+GetEnd+"' WHERE id = " + IDholder + "";

                sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

                sqLiteDatabase.close();

                Toast.makeText(EditSingleRecordActivity.this,"Data Edit Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(EditSingleRecordActivity.this, TimeTableHome.class);
                startActivity(intent);

              /*  Intent intent = new Intent(getApplicationContext(),ShowSingleRecordActivity.class);

                intent.putExtra("ListViewClickedItemValue", IDholder);

                startActivity(intent); */

            }
        });

    }

    @Override
    protected void onResume() {

        ShowSRecordInEditText();

        super.onResume();
    }

    public void ShowSRecordInEditText() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        IDholder = getIntent().getStringExtra("EditID");

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE id = " + IDholder + "", null);

        if (cursor.moveToFirst()) {

            do {
                selectedDay = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4));
                selectedClass = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5));
                subject.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1)));
                professor.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2)));
                room.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3)));
                start.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6)));
                end.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_7)));
            }
            while (cursor.moveToNext());

            cursor.close();

            // Toast.makeText(getApplicationContext(),selectedClass,Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplicationContext(),selectedDay,Toast.LENGTH_SHORT).show();

        }
    }

    public void OpenSQLiteDataBase(){

        sqLiteDatabaseObj = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

}

