package com.example.dummy.campusnavforblind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.dummy.campusnavforblind.SQLiteHelper.DATABASE_NAME;


public class ShowSingleRecordActivity extends AppCompatActivity {

    String IDholder;
    TextView id, subject, professor, room, day, type,start,end;
    SQLiteDatabase sqLiteDatabase;
    SQLiteHelper sqLiteHelper;
    Cursor cursor;
    Button Delete, Edit;
    SQLiteDatabase sqLiteDatabaseObj;
    String SQLiteDataBaseQueryHolder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_record);

        id = (TextView) findViewById(R.id.textViewID);
        subject = (TextView) findViewById(R.id.textViewSubject);
        professor = (TextView) findViewById(R.id.textViewProfessor);
        room = (TextView)findViewById(R.id.textViewRoom);

        day = (TextView)findViewById(R.id.textViewDay);
        type = (TextView)findViewById(R.id.textViewType);
        start = (TextView)findViewById(R.id.textViewStart);
        end = (TextView)findViewById(R.id.textViewEnd);


        Delete = (Button)findViewById(R.id.buttonDelete);
        Edit = (Button)findViewById(R.id.buttonEdit);

        sqLiteHelper = new SQLiteHelper(getApplicationContext(), DATABASE_NAME, this, 1);

        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowSingleRecordActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        OpenSQLiteDataBase();

                        SQLiteDataBaseQueryHolder = "DELETE FROM "+SQLiteHelper.TABLE_NAME+" WHERE id = "+IDholder+"";

                        sqLiteDatabase.execSQL(SQLiteDataBaseQueryHolder);

                        sqLiteDatabase.close();

                        finish();


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

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),EditSingleRecordActivity.class);

                intent.putExtra("EditID", IDholder);

                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {

        ShowSingleRecordInTextView();

        super.onResume();
    }

    public void ShowSingleRecordInTextView() {

        sqLiteDatabase = sqLiteHelper.getWritableDatabase();

        IDholder = getIntent().getStringExtra("ListViewClickedItemValue");

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SQLiteHelper.TABLE_NAME + " WHERE id = " + IDholder + "", null);

        if (cursor.moveToFirst()) {

            do {
                id.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_ID)));
                subject.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_1)));
                professor.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_2)));
                room.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_3)));
                day.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_4)));
                type.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_5)));
                start.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_6)));
                end.setText(cursor.getString(cursor.getColumnIndex(SQLiteHelper.Table_Column_7)));

            }
            while (cursor.moveToNext());

            cursor.close();

        }
    }

    public void OpenSQLiteDataBase(){

        sqLiteDatabaseObj = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);

    }
}
