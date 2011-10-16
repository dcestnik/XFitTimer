package com.dcestnik.android.xfittimer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class CountDownDialog extends Dialog {
    public CountDownDialog(Context context) {
        super(context);
    }

    public CountDownDialog(Context context, boolean cancelable,
            OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CountDownDialog(Context context, int theme) {
        super(context, theme);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countdown_dialog);

        new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long millis) {
                ((TextView) findViewById(R.id.CountDownMessage)).setText(String.format(getContext().getString(R.string.countdown_message), millis/1000));
            }
            @Override
            public void onFinish() {
                dismiss();
            }
        }.start();
    }

}
