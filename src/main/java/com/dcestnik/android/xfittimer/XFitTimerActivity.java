package com.dcestnik.android.xfittimer;

import static android.os.Process.killProcess;
import static android.os.Process.myPid;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class XFitTimerActivity extends Activity {
    private Chronometer chrono;
    private boolean clockRunning = false;
    private boolean onTickListenerRunning = false;
    private boolean inOffInterval = false;
    private long elapsedTime;
    private int goalTime;
    private int timeOff;
    private int numberOfRounds;
    private int elapsedRounds = 1;
    private long startTime;
    private MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        chrono = (Chronometer) findViewById(R.id.Chrono);
	mp = MediaPlayer.create(this.getApplicationContext(), R.raw.buzzer);
        findViewById(R.id.StartStopButton)
                .setOnClickListener(startStopListener);
        findViewById(R.id.ResetButton).setOnClickListener(resetListener);
    }

    View.OnClickListener resetListener = new OnClickListener() {
        public void onClick(View v) {
            resetChrono();
        }
    };

    View.OnClickListener startStopListener = new OnClickListener() {
        public void onClick(View v) {
            if (clockRunning) {
                stopChrono();
            } else {
                startChrono();
            }
        }
    };

    Chronometer.OnChronometerTickListener stopWatchClickListener = new OnChronometerTickListener() {
        public void onChronometerTick(Chronometer chronometer) {
        }
    };

    Chronometer.OnChronometerTickListener timerClickListener = new OnChronometerTickListener() {
        public void onChronometerTick(Chronometer chronometer) {
            long seconds = getChronoSeconds();
            if ((seconds == goalTime) && clockRunning) {
                stopChrono();
                displayFinishedDialog();
            }
        }
    };

    Chronometer.OnChronometerTickListener roundClickListener = new OnChronometerTickListener() {
        public void onChronometerTick(Chronometer chronometer) {
            if ((getChronoSeconds() >= goalTime) && clockRunning
                    && !onTickListenerRunning) {
                onTickListenerRunning = true;
                if (elapsedRounds == numberOfRounds) {
                    stopChrono();
                    displayFinishedDialog();
                } else {
                    elapsedRounds++;
                    playBuzzerSound();
                    resetRound();
                }
                onTickListenerRunning = false;
            }
        }
    };

    Chronometer.OnChronometerTickListener intervalClickListener = new OnChronometerTickListener() {
        public void onChronometerTick(Chronometer chronometer) {
            if (clockRunning && !onTickListenerRunning) {
                onTickListenerRunning = true;
                if (inOffInterval && (getChronoSeconds() == timeOff)) {
                    ((LinearLayout) findViewById(R.id.MainLayout))
                            .setBackgroundColor(getResources().getColor(
                                    R.color.subtle_green));
                    inOffInterval = false;
                    if (elapsedRounds == numberOfRounds) {
                        stopChrono();
                        displayFinishedDialog();
                    } else {
                        elapsedRounds++;
                        playBuzzerSound();
                        resetRound();
                    }
                } else if (getChronoSeconds() == goalTime) {
                    ((LinearLayout) findViewById(R.id.MainLayout))
                            .setBackgroundColor(Color.BLACK);
                    resetRound();
                    inOffInterval = true;
                    playBuzzerSound();
                }
                onTickListenerRunning = false;
            }
        }
    };

    OnDismissListener timerDialogDissmissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            goalTime = ((FixedTimerDialog) dialog).getInputtedTime();
            if (goalTime > 0) {
                ((TextView) findViewById(R.id.SubTitle)).setText(R.string.timer_subtitle);
                ((TextView) findViewById(R.id.Messages)).setText(String.format(
                        getString(R.string.timer_message), goalTime));
                chrono.setOnChronometerTickListener(timerClickListener);
            } else {
                displayInputErrorDialog();
            }
        }
    };


    OnDismissListener roundDialogDissmissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            goalTime = ((RoundTimerDialog) dialog).getInputtedTime();
            numberOfRounds = ((RoundTimerDialog) dialog).getInputtedRounds();
            if ((numberOfRounds) > 0 && (goalTime > 0)) {
                ((TextView) findViewById(R.id.SubTitle)).setText(R.string.round_subtitle);
                chrono.setOnChronometerTickListener(roundClickListener);
                refreshRoundMessage();
            } else {
                displayInputErrorDialog();
            }
        }
    };

    OnDismissListener intervalDialogDissmissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            goalTime = ((IntervalDialog) dialog).getInputtedTimeOn();
            timeOff = ((IntervalDialog) dialog).getInputtedTimeOff();
            numberOfRounds = ((IntervalDialog) dialog).getInputtedRounds();
            if ((numberOfRounds > 0) && (goalTime > 0) && (timeOff > 0)) {
                ((TextView) findViewById(R.id.SubTitle)).setText(R.string.interval_subtitle);
                chrono.setOnChronometerTickListener(intervalClickListener);
                refreshRoundMessage();
            } else {
                displayInputErrorDialog();
            }
        }
    };

    private void displayInputErrorDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.input_error_dialog_title);
        alertDialog.setButton(getString(R.string.alert_dialog_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetChrono();
                    }
                });
        alertDialog.show();
    }

    private void displayFinishedDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.alert_dialog_title);
        alertDialog.setButton(getString(R.string.alert_dialog_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetChrono();
                    }
                });
        playBuzzerSound();
        alertDialog.show();
    }

    private long getChronoSeconds() {
        String[] txt = ((Chronometer) findViewById(R.id.Chrono)).getText()
                .toString().split(":");
        return Long.parseLong(txt[1]) + (60 * Long.parseLong(txt[0]));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        stopChrono();
        resetChrono();
        switch (item.getItemId()) {
        case R.id.stopwatch_menu:
            ((TextView) findViewById(R.id.SubTitle))
                    .setText(R.string.stopwatch_subtitle);
            chrono.setOnChronometerTickListener(stopWatchClickListener);
            ((TextView) findViewById(R.id.Messages)).setText("");
            return true;
        case R.id.timer_menu:
            FixedTimerDialog timerDialog = new FixedTimerDialog(this);
            timerDialog.setOnDismissListener(timerDialogDissmissListener);
            timerDialog.show();
            return true;
        case R.id.round_menu:
            RoundTimerDialog roundDialog = new RoundTimerDialog(this);
            roundDialog.setOnDismissListener(roundDialogDissmissListener);
            roundDialog.show();
            return true;
        case R.id.interval_menu:
            IntervalDialog intervalDialog = new IntervalDialog(this);
            intervalDialog.setOnDismissListener(intervalDialogDissmissListener);
            intervalDialog.show();
            return true;
        case R.id.tabata_menu:
            ((TextView) findViewById(R.id.SubTitle))
                    .setText(R.string.tabata_subtitle);
            goalTime = 20;
            timeOff = 10;
            numberOfRounds = 8;
            chrono.setOnChronometerTickListener(intervalClickListener);
            refreshRoundMessage();
            return true;
        case R.id.quit_menu:
            super.onDestroy();
            finish();
            killProcess(myPid());
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void startChrono() {
        clockRunning = true;
        startTime = SystemClock.elapsedRealtime();
        if (elapsedTime != -1) {
            chrono.setBase(startTime - elapsedTime);
        } else {
            chrono.setBase(startTime);
        }
        chrono.start();
        ((Button) findViewById(R.id.StartStopButton)).setText("Stop");
        ((Button) findViewById(R.id.StartStopButton)).setTextColor(Color.RED);
        ((LinearLayout) findViewById(R.id.MainLayout))
                .setBackgroundColor(getResources().getColor(
                        R.color.subtle_green));

    }

    private void stopChrono() {
        chrono.stop();
        elapsedTime = SystemClock.elapsedRealtime() - chrono.getBase();
        clockRunning = false;
        ((Button) findViewById(R.id.StartStopButton)).setText("Start");
        ((Button) findViewById(R.id.StartStopButton))
                .setTextColor(getResources().getColor(R.color.subtle_green));
        ((LinearLayout) findViewById(R.id.MainLayout))
                .setBackgroundColor(Color.BLACK);

    }

    private void resetChrono() {
        resetRound();
        elapsedRounds = 1;
        refreshRoundMessage();
    }

    private void resetRound() {
        chrono.setBase(SystemClock.elapsedRealtime());
        elapsedTime = -1;
        startTime = -1;
        refreshRoundMessage();
    }

    private void refreshRoundMessage() {
        if (chrono.getOnChronometerTickListener() == roundClickListener) {
            ((TextView) findViewById(R.id.Messages)).setText(String.format(
                    getString(R.string.round_message), elapsedRounds,
                    numberOfRounds, goalTime));
        } else if (chrono.getOnChronometerTickListener() == intervalClickListener) {
            ((TextView) findViewById(R.id.Messages)).setText(String.format(
                    getString(R.string.interval_message), elapsedRounds,
                    numberOfRounds, goalTime, timeOff));
        }
    }

    private void playBuzzerSound() {
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}
