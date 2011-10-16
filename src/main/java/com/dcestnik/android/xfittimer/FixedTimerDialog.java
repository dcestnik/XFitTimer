package com.dcestnik.android.xfittimer;

import static java.lang.Integer.parseInt;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Dialog to get user input for the length of time.
 *
 * @author dcestnik
 */
public class FixedTimerDialog extends Dialog {
    /**
     * Time in seconds.
     */
    private int inputtedTime = 0;

    /**
     * Needed so since constructors are not inherited. 
     *
     * @See {@link AndroidCharacter.app.Dialog }
     */
    public FixedTimerDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * Needed so since constructors are not inherited. 
     *
     * @See {@link AndroidCharacter.app.Dialog }
     */
    public FixedTimerDialog(Context context, int theme) {
        super(context, theme);
    }

    /**
     * Needed so since constructors are not inherited. 
     *
     * @See {@link AndroidCharacter.app.Dialog }
     */
    public FixedTimerDialog(Context context) {
        super(context);
    }

    /**
     * 
     */
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
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.TimerSecondsSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.TimerMinutesSpinner)).setAdapter(adapter);
        ((Spinner) findViewById(R.id.TimerMinutesSpinner)).setSelection(10);
    }

    /**
     * 
     */
    View.OnClickListener submitListener = new View.OnClickListener() {
        public void onClick(View v) {
            inputtedTime = getViewIntValue(R.id.TimerSecondsSpinner)
                    + (60 * getViewIntValue(R.id.TimerMinutesSpinner));
            dismiss();
        }
    };

    /**
     * 
     * @param id
     * @return
     */
    private int getViewIntValue(int id) {
        // unsafe but isolated
        return parseInt(
                ((Spinner) findViewById(id)).getSelectedItem().toString());
    }

    /**
     * Simple accessor for the number of seconds entered.
     * @return The number of seconds for the timer.
     */
    int getInputtedTime() {
        return inputtedTime;
    }
}
