package com.dcestnik.android.xfittimer;

import static java.lang.Integer.parseInt;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class IntervalDialog extends Dialog {
    private int inputtedTimeOn = 0;
    private int inputtedTimeOff = 0;
    private int inputtedRounds = 0;

    public IntervalDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public IntervalDialog(Context context, int theme) {
        super(context, theme);
    }

    public IntervalDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interval_dialog);
        setTitle(R.string.interval_dialog_title);
        findViewById(R.id.IntervalDialogSubmitButton).setOnClickListener(
                submitListener);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this.getContext(), R.array.zero_to_sixty,
                android.R.layout.simple_spinner_item);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this.getContext(), R.array.zero_to_thirty,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.IntervalSecondsOnSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.IntervalSecondsOffSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.IntervalMinutesOnSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.IntervalMinutesOffSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.IntervalRoundsSpinner)).setAdapter(adapter2);
        ((Spinner) findViewById(R.id.IntervalSecondsOnSpinner)).setSelection(20);
        ((Spinner) findViewById(R.id.IntervalSecondsOffSpinner)).setSelection(10);
        ((Spinner) findViewById(R.id.IntervalRoundsSpinner)).setSelection(8);
    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {
            inputtedTimeOn = parseInt(((Spinner) findViewById(R.id.IntervalSecondsOnSpinner))
                    .getSelectedItem().toString())
                    + (60 * parseInt(((Spinner) findViewById(R.id.IntervalMinutesOnSpinner))
                            .getSelectedItem().toString()));
            inputtedTimeOff = parseInt(((Spinner) findViewById(R.id.IntervalSecondsOffSpinner))
                    .getSelectedItem().toString())
                    + (60 * parseInt(((Spinner) findViewById(R.id.IntervalMinutesOffSpinner))
                            .getSelectedItem().toString()));
            inputtedRounds = parseInt(((Spinner) findViewById(R.id.IntervalRoundsSpinner))
                    .getSelectedItem().toString());
            dismiss();
        }
    };

    int getInputtedTimeOn() {
        return inputtedTimeOn;
    }

    int getInputtedTimeOff() {
        return inputtedTimeOff;
    }

    int getInputtedRounds() {
        return inputtedRounds;
    }
}
