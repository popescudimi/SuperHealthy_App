package com.example.superhealthyapp.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.superhealthyapp.databases.ReminderDatabase;
import com.example.superhealthyapp.R;

public class ReminderAdapter extends   CursorAdapter {
    private LayoutInflater inflater;
    private int resource;
    private Context c;

    /*
    public ReminderAdapter(Context c, int resource, List<CalendarContract.Reminders> objects, Cursor c) {
        super(c, resource, objects, c);
        this.c = c;
        this.resource = resource;
        inflater = LayoutInflater.from();
    }
*/

    public ReminderAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        inflater = LayoutInflater.from(context);
    }

    public static LayoutInflater from(Context context) {
        LayoutInflater LayoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (LayoutInflater == null) {
            throw new AssertionError("LayoutInflater not found.");
        }
        return LayoutInflater;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTitle =  view.findViewById(R.id.ReminderTitle);
        TextView textViewText =  view.findViewById(R.id.ReminderDescription);
        TextView textViewDate =  view.findViewById(R.id.ReminderDate);
        TextView textViewTime =  view.findViewById(R.id.ReminderTime);
        TextView textViewType =  view.findViewById(R.id.Reminder);
        ImageView imageViewTypeLogo = view.findViewById(R.id.Notification);
        textViewTitle.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.TITLE)));
        textViewText.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.DETAIL)));
        textViewDate.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.DATE)));
        textViewTime.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.TIME)));
        textViewType.setText(cursor.getString(cursor.getColumnIndex(ReminderDatabase.TYPE)));
        if (cursor.getString(cursor.getColumnIndex(ReminderDatabase.TYPE)) != null) {
            imageViewTypeLogo.setImageResource(R.drawable.splash_logo);
        }
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.reminders,parent,false);
    }

}
