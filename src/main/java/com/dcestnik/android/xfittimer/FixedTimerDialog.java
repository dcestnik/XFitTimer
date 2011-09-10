package com.dcestnik.android.xfittimer;

import static java.lang.Integer.parseInt;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class FixedTimerDialog extends Dialog {
    private int inputtedTime = 0;

    public FixedTimerDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_dialog);
        setTitle(R.string.timer_dialog_title);
        findViewById(R.id.TimerDialogSubmitButton).setOnClickListener(
                submitListener);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this.getContext(), R.array.zero_to_sixty,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.TimerSecondsSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.TimerMinutesSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.TimerMinutesSpinner)).setSelection(10);
    }

    public FixedTimerDialog(Context context, int theme) {
        super(context, theme);
    }

    public FixedTimerDialog(Context context) {
        super(context);
    }

    View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {
            inputtedTime = parseInt(((Spinner) findViewById(R.id.TimerSecondsSpinner)).getSelectedItem().toString())
                    + (60 * parseInt(((Spinner) findViewById(R.id.TimerMinutesSpinner)).getSelectedItem().toString()));
            dismiss();
        }
    };

    int getInputtedTime() {
        return inputtedTime;
    }
}
