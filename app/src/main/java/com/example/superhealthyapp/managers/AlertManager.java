package com.example.superhealthyapp.managers;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.example.superhealthyapp.R;
import com.example.superhealthyapp.adapters.ReminderViewModel;

/*
Source : https://www.tabnine.com/code/java/methods/android.media.MediaPlayer/start
 */

public class AlertManager extends Activity {
    Context context = this;
    MediaPlayer mp;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        mp = MediaPlayer.create(context, R.raw.cooper_fulleon_sounder_tone_16);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    // Start without a delay
    // Vibrate for 100 milliseconds
    // Sleep for 1000 milliseconds
        long[] pattern = {0, 100, 1000};

        // The '0' here means to repeat indefinitely
        // '0' is actually the index at which the pattern keeps repeating from (the start)
        // To repeat the pattern from any other point, you could increase the index, e.g. '1'
        v.vibrate(pattern, 0);
        final Button b = (Button) findViewById(R.id.buttonReminderSubmit);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (getIntent().getExtras() != null) {
            String alert = "Alert : " + getIntent().getExtras().getString("title");
            builder.setMessage(alert).setCancelable(true).setPositiveButton(getString(R.string.text_button_proceed),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AlertManager.this.finish();

                        }
                    });
            builder.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();

    }

    public void onItemClick(ReminderViewModel adapterView, View view, int position, long id) {
        if (mp != null) {
            if (mp.isPlaying())
                mp.stop();
            mp = null;
        }
        String alarm;
        mp = MediaPlayer.create(context , R.raw.cooper_fulleon_sounder_tone_16);
        mp.start();
    }
}


