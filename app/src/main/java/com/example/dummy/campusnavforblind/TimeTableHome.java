package com.example.dummy.campusnavforblind;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.dummy.campusnavforblind.SQLiteHelper.DATABASE_NAME;


/**
 * Created by vkoth on 2018-03-06.
 */

public class TimeTableHome extends AppCompatActivity {

    Button add,view,delete;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder;
    TextView ansOutput,curTime;

    SQLiteHelper sqLiteHelper;
    Cursor cursor;

    SimpleDateFormat simpleDateFormat;
    String time;
    Calendar calander;


    private final int SPEECH_RECOGNITION_CODE = 1;
    private ImageButton btnMicrophone;
    TextToSpeech tt;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_home);

        add = (Button)findViewById(R.id.addTimetable);
        view = (Button)findViewById(R.id.viewTimetable);
        delete = (Button)findViewById(R.id.deleteTimetable);
        ansOutput = (TextView)findViewById(R.id.answer);
        curTime = (TextView)findViewById(R.id.curTime);

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), DATABASE_NAME, this, 1);

        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });



        //----------------------------------------








        //----------------------------------------

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TimeTableHome.this, AddTimetable.class);
                startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TimeTableHome.this, DisplaySQLiteDataActivity.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(TimeTableHome.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        SQLiteDataBaseBuild();
                        DeleteSQLiteDatabase();
                        dialog.dismiss();

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

    }

    //sppech method------------------------------------------------------

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    // txtOutput.setText(text + "?");
                    res = text;

                }
                break;
            }
        }
        curTime.setText(res);
        if (res.equals("when is my next lecture")) {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            final String currentTime,startTime,subject,room;
            String raw="";
            String id;

            calander = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("h:ma");

            time = simpleDateFormat.format(calander.getTime());


            Calendar rightNow = Calendar.getInstance();
            int t = rightNow.get(Calendar.HOUR_OF_DAY);
            int h,i=0;

            try {
                sqLiteDatabaseObj = sqLiteHelper.getReadableDatabase();
                cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE day = '" +dayOfTheWeek+ "'", null);

                if (cursor.moveToFirst()) {


                    do {
                        id = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID));
                        raw = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6));

                        i = Integer.parseInt(id);
                        // curTime.setText(raw);


                        if (raw.equals("12:0AM")) {
                            h = 24;
                        } else if (raw.equals("1:0AM")) {
                            h = 1;
                        } else if (raw.equals("2:0AM")) {
                            h = 2;
                        } else if (raw.equals("3:0AM")) {
                            h = 3;
                        } else if (raw.equals("4:0AM")) {
                            h = 4;
                        } else if (raw.equals("5:0AM")) {
                            h = 5;
                        } else if (raw.equals("6:0AM")) {
                            h = 6;
                        } else if (raw.equals("7:0AM")) {
                            h = 7;
                        } else if (raw.equals("8:0AM")) {
                            h = 8;
                        } else if (raw.equals("9:0AM")) {
                            h = 9;
                        } else if (raw.equals("10:0AM")) {
                            h = 10;
                        } else if (raw.equals("11:0AM")) {
                            h = 11;
                        } else if (raw.equals("12:0AM")) {
                            h = 12;
                        } else if (raw.equals("1:0PM")) {
                            h = 13;
                        } else if (raw.equals("2:0PM")) {
                            h = 14;
                        } else if (raw.equals("3:0PM")) {
                            h = 15;
                        } else if (raw.equals("4:0PM")) {
                            h = 14;
                        } else if (raw.equals("5:0PM")) {
                            h = 17;
                        } else if (raw.equals("6:0PM")) {
                            h = 18;
                        } else if (raw.equals("7:0PM")) {
                            h = 19;
                        } else if (raw.equals("8:0PM")) {
                            h = 20;
                        } else if (raw.equals("9:0PM")) {
                            h = 21;
                        } else if (raw.equals("10:0PM")) {
                            h = 22;
                        } else if(raw.equals("11:0PM")){
                            h = 23;
                        }
                        else
                        {
                            h=101;
                            i=102;
                        }

                        if (h > t) {
                            break;
                        }
                        if(h <= t)
                        {
                            h = 101;
                            i = 102;
                        }
                    }while (cursor.moveToNext());
                    //  curTime.setText("h="+Integer.toString(h));
                    //  ansOutput.setText("i="+Integer.toString(i));

                    if(i!=102 || h!=101) {
                        cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE id = " + i + "", null);
                        if (cursor.moveToFirst()) {

                            subject = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1));
                            startTime = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6));
                            room = cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3));

                            //  ansOutput.setText("Your next lecture is of " + subject + " in room number " + room + " at " + startTime);

                            tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                                public void onInit(int s) {
                                    // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                    if (s != TextToSpeech.ERROR) {
                                        tt.setLanguage(Locale.CANADA);
                                        //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                        tt.speak("Your next lecture is of " + subject + " in room number " + room + " at " + startTime, TextToSpeech.QUEUE_FLUSH, null, null);
                                        try {
                                            Thread.sleep(8000);
                                        } catch (Exception e) {
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                    else
                    {
                        //ansOutput.setText("No lecture");
                        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                            public void onInit(int s) {
                                // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                if (s != TextToSpeech.ERROR) {
                                    tt.setLanguage(Locale.CANADA);
                                    //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                    tt.speak("You do not have any lecture", TextToSpeech.QUEUE_FLUSH, null, null);
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    cursor.close();

                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                Log.i("Error",e.toString());
            }

        }
        else if(res.equals("how many lectures do I have today"))
        {

            //  ansOutput.setText("answer");

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            String id;
            int i=0;
            try {

                sqLiteDatabaseObj = sqLiteHelper.getReadableDatabase();
                cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE day = '" +dayOfTheWeek+ "'", null);
                final int count;
                if (cursor.moveToFirst()) {


                    do {
                        id= cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1));
                        i++;
                    } while (cursor.moveToNext());
//speech code
                    count=i;
                    //   ansOutput.setText("count="+Integer.toString(count));
                    if(count !=0) {
                        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                            public void onInit(int s) {
                                // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                if (s != TextToSpeech.ERROR) {
                                    tt.setLanguage(Locale.CANADA);
                                    //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                    tt.speak("You have " + Integer.toString(count) + "lectures today", TextToSpeech.QUEUE_FLUSH, null, null);
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if(count == 0)
                    {
                        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                            public void onInit(int s) {
                                // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                if (s != TextToSpeech.ERROR) {
                                    tt.setLanguage(Locale.CANADA);
                                    //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                    tt.speak("You have no lectures today", TextToSpeech.QUEUE_FLUSH, null, null);
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                cursor.close();
            }catch (Exception e)
            {

            }

        }
        else if(res.equals("how many lectures do I have in this week"))
        {
            ansOutput.setText("ans");
            // Toast.makeText(getApplicationContext(),"pass",Toast.LENGTH_SHORT).show();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            String id;
            int i=0;
            try {

                sqLiteDatabaseObj = sqLiteHelper.getReadableDatabase();
                cursor = sqLiteDatabaseObj.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " ", null);
                final int count;
                if (cursor.moveToFirst()) {


                    do {
                        id= cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1));
                        i++;
                    } while (cursor.moveToNext());

                    count=i;
                    if(count != 0) {
                        ansOutput.setText("ans=" + Integer.toString(count));
                        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                            public void onInit(int s) {
                                // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                if (s != TextToSpeech.ERROR) {
                                    tt.setLanguage(Locale.CANADA);
                                    //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                    tt.speak("You have " + Integer.toString(count) + "lectures in this week", TextToSpeech.QUEUE_FLUSH, null, null);
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else
                    {
                        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                            public void onInit(int s) {
                                // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                                if (s != TextToSpeech.ERROR) {
                                    tt.setLanguage(Locale.CANADA);
                                    //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                                    tt.speak("You have no lectures in this week", TextToSpeech.QUEUE_FLUSH, null, null);
                                    try {
                                        Thread.sleep(8000);
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    // curTime.setText(res);
                    // ansOutput.setText("count="+Integer.toString(i));

                }
                cursor.close();
            }catch (Exception e)
            {

            }

        }
        else {
            //  ansOutput.setText("Sorry! I did not get you");

            tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                public void onInit(int s) {
                    // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                    if (s != TextToSpeech.ERROR) {
                        tt.setLanguage(Locale.CANADA);
                        //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                        tt.speak("Sorry! I did not get you", TextToSpeech.QUEUE_FLUSH, null, null);
                        try {
                            Thread.sleep(8000);
                        }
                        catch (Exception e)
                        {}
                    } else {
                        Toast.makeText(getApplicationContext(), "Google speech is not working", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    //------abvjjuibew-------------------------------------------

    public void SQLiteDataBaseBuild(){
        sqLiteDatabaseObj = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
    }

    public void SQLiteTableBuild(){
        sqLiteDatabaseObj.execSQL("CREATE TABLE IF NOT EXISTS "+SQLiteHelper.TABLE_NAME+" ("+SQLiteHelper.Table_Column_ID+" INTEGER PRIMARY KEY, "+SQLiteHelper.Table_Column_1+" VARCHAR, "+SQLiteHelper.Table_Column_2+" VARCHAR, "+SQLiteHelper.Table_Column_3+" VARCHAR, "+SQLiteHelper.Table_Column_4+" VARCHAR, "+SQLiteHelper.Table_Column_5+" VARCHAR, "+SQLiteHelper.Table_Column_6+" VARCHAR, "+SQLiteHelper.Table_Column_7+" VARCHAR)");
    }

    public void DeleteSQLiteDatabase(){

        SQLiteDataBaseQueryHolder = "DELETE FROM "+SQLiteHelper.TABLE_NAME+" ";

        sqLiteDatabaseObj.execSQL(SQLiteDataBaseQueryHolder);

        sqLiteDatabaseObj.close();

        Toast.makeText(TimeTableHome.this,"Timetable deleted Successfully", Toast.LENGTH_LONG).show();


    }

}

