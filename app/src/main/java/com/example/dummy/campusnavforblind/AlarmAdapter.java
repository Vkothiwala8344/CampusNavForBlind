package com.example.dummy.campusnavforblind;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.dummy.campusnavforblind.ReminderDatabasePackage.ReminderDbConfig;


public class AlarmAdapter extends CursorAdapter {

    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage , mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public AlarmAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDateAndTimeText = (TextView) view.findViewById(R.id.recycle_date_time);

        mThumbnailImage = (ImageView) view.findViewById(R.id.thumbnail_image);

        int titleColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_DATE);
        int timeColumnIndex = cursor.getColumnIndex(ReminderDbConfig.ReminderEntry.KEY_TIME);


        String title = cursor.getString(titleColumnIndex);
        String date = cursor.getString(dateColumnIndex);
        String time = cursor.getString(timeColumnIndex);



        setReminderTitle(title);

        if (date != null){
            String dateTime = date + " " + time;
            setReminderDateTime(dateTime);
        }else{
            mDateAndTimeText.setText("Date not set");
        }



    }

    // Set reminder title view
    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    // Set date and time views
    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

}
