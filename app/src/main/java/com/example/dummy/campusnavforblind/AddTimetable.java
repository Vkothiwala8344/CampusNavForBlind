package com.example.dummy.campusnavforblind;


import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddTimetable extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabaseObj;
    //edit text
    EditText subject, professor,room,start,end;

    //string
    String subjectHolder, professorHolder,roomHolder,format,startHolder,endHolder, SQLiteDataBaseQueryHolder;

    //button
    Button EnterData, ButtonDisplayData;

    //bulean to check empty field
    Boolean EditTextEmptyHold;

    //Calendar
    Calendar calendar;

    //spinner
    Spinner dateSpin,typeSpin;

    //spinner varibale
    String selectedDay = "Monday";
    String selectedClass = "Lecture";

    //varibales
    private int CalendarHour, CalendarMinute;
    TimePickerDialog timepickerdialog;

    //spinner data
    String[] daysofWeek={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String[] classType = {"Lecture","Lab"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);
        //buttons
        EnterData = (Button)findViewById(R.id.add);
        //  ButtonDisplayData = (Button)findViewById(R.id.show);

        //edittext
        subject = (EditText)findViewById(R.id.editText);
        professor = (EditText)findViewById(R.id.professorName);
        room = (EditText)findViewById(R.id.roomNumber);

        //spinner
        dateSpin = (Spinner)findViewById(R.id.daySpinner);
        typeSpin = (Spinner)findViewById(R.id.typeSpinner);

        //timer object
        start = (EditText)findViewById(R.id.startTime);
        end = (EditText)findViewById(R.id.endTime);

        //spinner
        //day picker
        //Creating the ArrayAdapter instance having the bank name list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,daysofWeek);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        dateSpin.setAdapter(aa);

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


                timepickerdialog = new TimePickerDialog(AddTimetable.this,
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


                timepickerdialog = new TimePickerDialog(AddTimetable.this,
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




        EnterData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SQLiteDataBaseBuild();

                SQLiteTableBuild();

                CheckEditTextStatus();

                InsertDataIntoSQLiteDatabase();

                EmptyEditTextAfterDataInsert();


            }
        });

      /*  ButtonDisplayData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, DisplaySQLiteDataActivity.class);
                startActivity(intent);
            }
        }); */
    }

    public void SQLiteDataBaseBuild(){

        sqLiteDatabaseObj = openOrCreateDatabase(SQLiteHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);

    }

    public void SQLiteTableBuild(){

        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "+SQLiteHelper.TABLE_NAME+" ("+SQLiteHelper.Table_Column_ID+" " +
                "INTEGER PRIMARY KEY, "+SQLiteHelper.Table_Column_1+" VARCHAR, "+SQLiteHelper.Table_Column_2+" VARCHAR, "+
                SQLiteHelper.Table_Column_3+" VARCHAR, "+SQLiteHelper.Table_Column_4+" VARCHAR, "+SQLiteHelper.Table_Column_5+" VARCHAR, "+
                SQLiteHelper.Table_Column_6+" VARCHAR, "+SQLiteHelper.Table_Column_7+" VARCHAR)");

    }

    public void CheckEditTextStatus(){

        subjectHolder = subject.getText().toString() ;
        professorHolder = professor.getText().toString();
        roomHolder = room.getText().toString();
        startHolder = start.getText().toString();
        endHolder = end.getText().toString();

        //  Toast.makeText(getApplicationContext(),subjectHolder,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),professorHolder,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),roomHolder,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),startHolder,Toast.LENGTH_SHORT).show();
        //  Toast.makeText(getApplicationContext(),endHolder,Toast.LENGTH_SHORT).show();

        if(TextUtils.isEmpty(subjectHolder) || TextUtils.isEmpty(professorHolder) || TextUtils.isEmpty(roomHolder) || TextUtils.isEmpty(startHolder) || TextUtils.isEmpty(endHolder)){

            EditTextEmptyHold = false ;

        }
        else {

            EditTextEmptyHold = true ;
        }
    }

    public void InsertDataIntoSQLiteDatabase(){

        if(EditTextEmptyHold == true)
        {

            SQLiteDataBaseQueryHolder = "INSERT INTO "+SQLiteHelper.TABLE_NAME+" (subject,professor,room,day,type,start,end) VALUES('"+subjectHolder+"','"+professorHolder+"','"+roomHolder+"','"+selectedDay+"','"+selectedClass+"','"+startHolder+"','"+endHolder+"');";

            sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

            sqLiteDatabaseObj.close();

            Toast.makeText(AddTimetable.this,"Data Inserted Successfully", Toast.LENGTH_LONG).show();

        }
        else {

            Toast.makeText(AddTimetable.this,"Please Fill All The Required Fields.", Toast.LENGTH_LONG).show();

        }

    }

    public void EmptyEditTextAfterDataInsert(){

        subject.getText().clear();
        professor.getText().clear();
        room.getText().clear();
        start.getText().clear();
        end.getText().clear();

    }

}
