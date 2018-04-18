package com.example.dummy.campusnavforblind;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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

import com.estimote.mustard.rx_goodness.rx_requirements_wizard.Requirement;
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory;
import com.estimote.proximity_sdk.proximity.EstimoteCloudCredentials;
import com.estimote.proximity_sdk.proximity.ProximityAttachment;
import com.estimote.proximity_sdk.proximity.ProximityObserver;
import com.estimote.proximity_sdk.proximity.ProximityObserverBuilder;
import com.estimote.proximity_sdk.proximity.ProximityZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;





public class ActivityHomePage extends AppCompatActivity {

    private Button verifyRoom,reminder,security,timetable,settings,openNotification,navigation;
    private static final int REQUEST_CALL = 1;

    private final int SPEECH_RECOGNITION_CODE = 1;
    private ImageButton btnMicrophone;
    TextToSpeech tt;
    String res;

    String ans;

    private ProximityObserver proximityObserver;
    ProximityObserver.Handler proximityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        EstimoteCloudCredentials cloudCredentials =
                new EstimoteCloudCredentials("varun-kothiwala-s-proximit-8kf", "94291148934a38a96e7f46a0fdf9d9f6");




        proximityObserver =
                new ProximityObserverBuilder(getApplicationContext(), cloudCredentials)
                        .withOnErrorAction(new Function1<Throwable, Unit>() {
                            @Override
                            public Unit invoke(Throwable throwable) {
                                Log.e("proximity app", "proximity observer error: " + throwable);
                                return null;
                            }
                        })
                        .withBalancedPowerMode()
                        .build();


        // add this below:


        ProximityZone zone1 = proximityObserver.zoneBuilder()
                .forAttachmentKeyAndValue("floor", "8th")
                .inCustomRange(2.5)
                .withOnChangeAction(new Function1<List<? extends ProximityAttachment>, Unit>() {
                    @Override
                    public Unit invoke(List<? extends ProximityAttachment> attachments) {
                        List<String> desks = new ArrayList<>();
                        for (ProximityAttachment attachment : attachments) {
                            desks.add(attachment.getPayload().get("location"));
                        }
                        Log.d("app", "Nearby location: " + desks);
                        int count = desks.size();
                        String data = "";
                        for (int i = 0; i < count; i++) {
                            data = data + desks.get(i) + ",";
                        }

                       // tv.setText(data);
                        if(data.equals("woodworking entrance,"))
                        {
                            ans = "woodworking entrance";
                        }
                        if(data.equals("student lounge start,"))
                        {
                            ans = "student lounge entrance";
                        }
                        if(data.equals("student lounge mid,"))
                        {
                            ans = "student lounge";
                        }
                        if(data.equals("student lounge end,"))
                        {
                            ans = "student lounge end";
                        }
                        return null;
                    }
                })
                .create();
        proximityObserver.addProximityZone(zone1);


        RequirementsWizardFactory
                .createEstimoteRequirementsWizard()
                .fulfillRequirements(this,
                        // onRequirementsFulfilled
                        new Function0<Unit>() {
                            @Override public Unit invoke() {
                                Log.d("app", "requirements fulfilled");
                                proximityHandler =  proximityObserver.start();
                                return null;
                            }
                        },
                        // onRequirementsMissing
                        new Function1<List<? extends Requirement>, Unit>() {
                            @Override public Unit invoke(List<? extends Requirement> requirements) {
                                Log.e("app", "requirements missing: " + requirements);
                                return null;
                            }
                        },
                        // onError
                        new Function1<Throwable, Unit>() {
                            @Override public Unit invoke(Throwable throwable) {
                                Log.e("app", "requirements error: " + throwable);
                                return null;
                            }
                        });

        verifyRoom = (Button)findViewById(R.id.verifyRoom);
        reminder = (Button)findViewById(R.id.openReminder);
         security = (Button)findViewById(R.id.security);
        btnMicrophone = (ImageButton) findViewById(R.id.btn_mic);
        timetable = (Button)findViewById(R.id.timetable);
        reminder =(Button)findViewById(R.id.openReminder);
        settings = (Button)findViewById(R.id.settings);
        openNotification = (Button)findViewById(R.id.openNotification);
        navigation = (Button)findViewById(R.id.navigation);

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ActivityHomePage.this,NavigationActivity.class);
                Bundle b = new Bundle();
                b.putString("startLocation", ans);
                i.putExtras(b);
                startActivity(i);
                startActivity(i);
            }
        });

        openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityHomePage.this,ViewNotification.class);
                startActivity(i);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityHomePage.this,MainActivity.class);
                startActivity(i);
            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityHomePage.this,ReminderActivity.class);
                startActivity(i);
            }
        });

        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityHomePage.this,TimeTableHome.class);
                startActivity(i);
            }
        });

          verifyRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityHomePage.this,QRActivity.class);
                startActivity(i);
            }
        });


        security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityHomePage.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to call security?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        makePhoneCall();
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

        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
        }

    private void makePhoneCall() {
        String number = "+15195024375";
        if (number.trim().length() > 0) {

            if (ContextCompat.checkSelfPermission(ActivityHomePage.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityHomePage.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

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
        if (res.equals("what is today's date")) {

            SimpleDateFormat currentDate = new SimpleDateFormat("MM/dd/yyyy");
            Date todayDate = new Date();
            final String thisDate = currentDate.format(todayDate);
            //  ansOutput.setText(thisDate);
            // Toast.makeText(getApplicationContext(), thisDate, Toast.LENGTH_LONG).show();
            tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                public void onInit(int s) {
                   // Toast.makeText(getApplicationContext(), thisDate, Toast.LENGTH_SHORT).show();

                    if (s != TextToSpeech.ERROR) {
                        tt.setLanguage(Locale.CANADA);
                        //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                        tt.speak(thisDate, TextToSpeech.QUEUE_FLUSH, null, null);
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
        } else if (res.equals("what time is it")) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            final String strDate = "Current Time : " + mdformat.format(calendar.getTime());

            // ansOutput.setText(strDate);

            tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                public void onInit(int s) {
                   //  Toast.makeText(getApplicationContext(), strDate, Toast.LENGTH_SHORT).show();

                    if (s != TextToSpeech.ERROR) {
                        tt.setLanguage(Locale.CANADA);
                        //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                        tt.speak(strDate, TextToSpeech.QUEUE_FLUSH, null, null);

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
        else  if (res.equals("where am I")) {


            tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {


                public void onInit(int s) {
                    // Toast.makeText(getApplicationContext(), Integer.toString(s), Toast.LENGTH_SHORT).show();

                    if (s != TextToSpeech.ERROR) {
                        tt.setLanguage(Locale.CANADA);
                        //  Toast.makeText(getApplicationContext(), "Language is set", Toast.LENGTH_SHORT).show();

                        tt.speak("You are near to "+ans, TextToSpeech.QUEUE_FLUSH, null, null);
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



            }else {
            // ansOutput.setText("Sorry! I did not get you");

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
    @Override
    protected void onDestroy() {
        proximityHandler.stop();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        proximityHandler.stop();
        super.onStop();

    }

}
