/*  Copyright 2017 by AlaskaLinuxUser (https://thealaskalinuxuser.wordpress.com)
*
*   Licensed under the Apache License, Version 2.0 (the "License");
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the License at
*
*       http://www.apache.org/licenses/LICENSE-2.0
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/
package com.alaskalinuxuser.hourglass;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    // Define the imageView.
    ImageView myHourGlass;
    // Define the media player.
    MediaPlayer mp;
    // Define the text we wish to set.
    TextView timeGoing;
    // Define the seekbar.
    SeekBar timeBar;
    // Define the countdown timer.
    CountDownTimer timeCount;
    // Define the integers.
    int maxTime;
    int curTime;
    // Define the strings.
    String minutes;
    String seconds;
    String mi;
    String se;

    // Get the application context for the notification.
    public Context context;

    // Define the notificationmanager.
    NotificationManager mNotificationManager;

    // Define stopped boolean.
    boolean stoppedTimer;

    public void theTime (int timeStillLef) {

        // To get the rounded down number of "whole" minutes.
        int m = timeStillLef/60000;
        // To get the remainder in seconds.
        int s = (timeStillLef - (m*60000))/1000;

        mi = String.valueOf(m);
        se = String.valueOf(s);

        if (m <=9) {

            minutes = "0" + mi;

        } else {

            minutes = mi;

        }

        if (s <= 9) {

            seconds = "0" + se;

        } else {

            seconds = se;

        }

        // Set the time.
        timeGoing.setText(minutes + ":" + seconds);

    }

    public void hourClick (View view) {

        if (stoppedTimer) {

            /* Let's make a notification, so the user doesn't forget that they have a timer running.
             * I only want it to display if the timer is actually running.
             * I am giving them the option to clear the notification, it is not persistent.
             *
             */
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.hourglass).setContentTitle("Hourglass").setContentText(
                            "Your hourglass timer is running."
                    );

            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(pIntent);
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification notif = builder.build();
            mNotificationManager.notify(0, notif);

            timeCount = new CountDownTimer(timeBar.getProgress(), 1000) {

                @Override
                public void onTick(long timeLeft) {

                    // Update the time every tick with the method "theTime" based on the long "timeLeft".
                    theTime((int) timeLeft);

                }

                // What to do when the timer is done.
                @Override
                public void onFinish() {

                    // Cancel the notification.
                    mNotificationManager.cancel(0);

                    // Spin the hourglass for a cool special affect.
                    myHourGlass.animate().rotation(0f).setDuration(1000).start();

                    //Play the sound file.
                    mp.start();

                    // Set the time.
                    timeGoing.setText("00:00");

                    // And make a toast popup.
                    Toast timeUp = Toast.makeText(getApplicationContext(), "Time is up!", Toast.LENGTH_LONG);
                    timeUp.show();

                    // And vibrate, if the permission was granted, for 2 seconds.
                    Vibrator vibRate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibRate.vibrate(2000);

                }

            };

            // Spin the hourglass for a cool special affect.
            myHourGlass.animate().rotation(360f).setDuration(1000).start();

            // Start the timer and set the boolean for running.
            timeCount.start();
            stoppedTimer = false;

        } else {

            // Stop the timer with cancel, and set the boolean for it being stopped.
            timeCount.cancel();
            stoppedTimer = true;

            // Cancel the notification, based on it's number, in this case, 0.
            mNotificationManager.cancel(0);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define our context for our notification.
        context = getApplicationContext();

        // Set the timer boolean to true for stopped, since we just opened the app.
        stoppedTimer = true;

        // Define the resources.
        timeGoing = (TextView)findViewById(R.id.timerView);
        myHourGlass = (ImageView)findViewById(R.id.imageGlass);

        // Define the sound file.
        mp = MediaPlayer.create(this, R.raw.shipbell);

        // Define the maximum time in milliseconds.
        maxTime = 3600000;

        // Defining the seekbar wich will act as a volume control
        timeBar = (SeekBar) findViewById(R.id.seekBarTime);

        // Define the maximum value and current value for seekbar.
        timeBar.setMax(maxTime);
        // The curent time could be set to what ever you want. But I have it set to nothing, or 0.
        timeBar.setProgress(curTime);

        // Set up our seekbar to listen to changes.
        timeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                // Do method "theTime" in reference to int "i".
                theTime(i);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                // What to do when they touch the seekbar. In this case, nothing.

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                // What to do when they release the seekbar. In this case, nothing.

            }
        });


    }

    public void aboutClick (View v)
    {
        // Call an intent to go to the second screen when you click the about button.
        // First you define it.
            Intent myintent = new Intent(MainActivity.this, SetScaleActivity.class);
        // Now you call it.
            startActivity(myintent);
    }

}