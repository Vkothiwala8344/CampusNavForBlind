package com.example.dummy.campusnavforblind;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> ID;
    ArrayList<String> SUBJECT;
    ArrayList<String> PROFESSOR;
    ArrayList<String> ROOM;
    ArrayList<String> DAY;
    ArrayList<String> TYPE;
    ArrayList<String> START;
    ArrayList<String> END;


    public ListAdapter(
            Context context2,
            ArrayList<String> id,
            ArrayList<String> subject,
            ArrayList<String> professor,
            ArrayList<String> room,
            ArrayList<String> day,
            ArrayList<String> type,
            ArrayList<String> start,
            ArrayList<String> end
    )
    {

        this.context = context2;
        this.ID = id;
        this.SUBJECT = subject;
        this.PROFESSOR = professor;
        this.ROOM = room;
        this.DAY = day;
        this.TYPE = type;
        this.START = start;
        this.END = end;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return ID.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;

        LayoutInflater layoutInflater;

        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            child = layoutInflater.inflate(R.layout.items, null);

            holder = new Holder();

            holder.ID_TextView = (TextView) child.findViewById(R.id.textViewID);
            holder.SUBJECT_TextView = (TextView) child.findViewById(R.id.textViewSUBJECT);
            holder.PROFESSOR_TextView = (TextView) child.findViewById(R.id.textViewPROFESSOR);
            holder.ROOM_TextView = (TextView) child.findViewById(R.id.textViewROOM);
            holder.DAY_TextView = (TextView) child.findViewById(R.id.textViewDAY);
            holder.TYPE_TextView = (TextView) child.findViewById(R.id.textViewTYPE);
            holder.START_TextView = (TextView) child.findViewById(R.id.textViewSTART);
            holder.END_TextView = (TextView) child.findViewById(R.id.textViewEND);


            child.setTag(holder);

        } else {

            holder = (Holder) child.getTag();
        }
        holder.ID_TextView.setText(ID.get(position));
        holder.SUBJECT_TextView.setText(SUBJECT.get(position));
        holder.PROFESSOR_TextView.setText(PROFESSOR.get(position));
        holder.ROOM_TextView.setText(ROOM.get(position));
        holder.DAY_TextView.setText(DAY.get(position));
        holder.TYPE_TextView.setText(TYPE.get(position));
        holder.START_TextView.setText(START.get(position));
        holder.END_TextView.setText(END.get(position));
        return child;
    }

    public class Holder {

        TextView ID_TextView;
        TextView SUBJECT_TextView;
        TextView PROFESSOR_TextView;
        TextView ROOM_TextView;
        TextView DAY_TextView;
        TextView TYPE_TextView;
        TextView START_TextView;
        TextView END_TextView;

    }

}

