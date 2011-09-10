package com.dcestnik.android.xfittimer;

import static java.lang.Integer.parseInt;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RoundTimerDialog extends Dialog {
    private int inputtedTime = 0;
    private int inputtedRounds = 0;

    public RoundTimerDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public RoundTimerDialog(Context context, int theme) {
        super(context, theme);
    }

    public RoundTimerDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_dialog);
        setTitle(R.string.round_dialog_title);
        findViewById(R.id.RoundDialogSubmitButton).setOnClickListener(
                submitListener);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this.getContext(), R.array.zero_to_sixty,
                android.R.layout.simple_spinner_item);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(
                this.getContext(), R.array.zero_to_thirty,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.RoundSecondsSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.RoundMinutesSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.RoundRoundsSpinner)).setAdapter(adapter2);
        ((Spinner) findViewById(R.id.RoundSecondsSpinner)).setSelection(10);
        ((Spinner) findViewById(R.id.RoundRoundsSpinner)).setSelection(5);
    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {
            inputtedTime = getNumberFromSpinner(R.id.RoundSecondsSpinner)
                    + (60 * getNumberFromSpinner(R.id.RoundMinutesSpinner));
            inputtedRounds = getNumberFromSpinner(R.id.RoundRoundsSpinner);
            dismiss();
        }
    };

    int getInputtedTime() {
        return inputtedTime;
    }

    int getInputtedRounds() {
        return inputtedRounds;
    }

    private int getNumberFromSpinner(final int id) {
        return parseInt(((Spinner) findViewById(id)).getSelectedItem().toString());
    }
}
