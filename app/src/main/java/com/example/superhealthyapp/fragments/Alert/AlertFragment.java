package com.example.superhealthyapp.fragments.Alert;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.superhealthyapp.adapters.ReminderAdapter;
import com.example.superhealthyapp.databases.ReminderDatabase;
import com.example.superhealthyapp.R;
import com.example.superhealthyapp.activities.MainActivity;

import com.example.superhealthyapp.fragments.BaseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AlertFragment extends BaseFragment implements View.OnClickListener{
    private int fragCount;
    private ListView listView;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity()!=null){
          ((MainActivity)getActivity()).updateToolbarTitle(getString(R.string.title_reminder));
        }
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);
        Bundle args = getArguments();
        if (args != null) {
            fragCount = args.getInt(ARGS_INSTANCE);
        }
        FloatingActionButton floatingActionButtonReminder = view.findViewById(R.id.fab);
        floatingActionButtonReminder.setOnClickListener(this);
        listView = view.findViewById(R.id.listReminder);
        ReminderDatabase dbHelper = new ReminderDatabase(this.getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] column = {ReminderDatabase.KEY_ID, ReminderDatabase.TITLE, ReminderDatabase.DETAIL, ReminderDatabase.TYPE, ReminderDatabase.TIME, ReminderDatabase.DATE};

        Cursor cursor = sqLiteDatabase.query(ReminderDatabase.TABLE_NAME,column,null,null,null,null,null);
        ReminderAdapter reminderAdapter = new ReminderAdapter(getContext(),cursor,0);
        listView.setAdapter(reminderAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(getActivity() != null) {
                    Fragment fragment = new AlertDetail();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fm.beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);
                    getActivity().getIntent().putExtra("rowId", id);
                    transaction.commit();
                }
            }
        });

        if (getActivity() != null) {
            int a = reminderAdapter.getCount();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("reminderCount", Context.MODE_PRIVATE).edit();
            editor.putInt("reminderCount",a);
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            Fragment fragment = new AddAlert();
            if(getActivity()!=null) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_frame, fragment);
                transaction.commit();
            }
        }
    }
}