package com.example.superhealthyapp.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;


public class AlertReceiverManager extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra("title");
            Intent newIntent = new Intent(context, AlertManager.class);
            newIntent.putExtra("title",title);
         //   Toast.makeText(context, "Don't panic but your time is up!!!!.",
            //        Toast.LENGTH_LONG).show();
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
    }