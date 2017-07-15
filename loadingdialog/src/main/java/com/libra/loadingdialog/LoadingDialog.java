package com.libra.loadingdialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.libra.loadingdialog.drawable.IndeterminateCircularProgressDrawable;

/**
 * Created by libra on 2017/6/17.
 */

public class LoadingDialog extends ProgressDialog {
    private static final int HANDLE_TIMER_OUT = 2;
    private int timeOut = 70;
    private int timer = 0;
    private TimeOutListener listener;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg.what == HANDLE_TIMER_OUT) {
                if (timer >= timeOut) {
                    if (isShowing()) {
                        dismiss();
                    }
                    if (listener != null) {
                        listener.timeOutConnectError();
                    }
                    return;
                }
                timer++;
                sendEmptyMessageDelayed(HANDLE_TIMER_OUT, 1000);
            }
        }
    };

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, TimeOutListener listener) {
        super(context, R.style.CommonDialog);
        this.listener = listener;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundResource(R.drawable.bg_loadingdialog);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                                                      getContext().getResources()
                                                                  .getDisplayMetrics());
        frameLayout.setPadding(padding, padding, padding, padding);
        ProgressBar progressBar = new ProgressBar(getContext());
        progressBar.setInterpolator(new LinearInterpolator());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//适配5.0以下
            progressBar.setIndeterminateDrawable(
                    new IndeterminateCircularProgressDrawable(getContext()));
        }
        frameLayout.addView(progressBar,
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                         ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(frameLayout,
                       new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    Gravity.CENTER));
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeMessages(HANDLE_TIMER_OUT);
                timer = 0;
            }
        });
    }

    @Override
    public void show() {
        timer = 0;
        handler.sendEmptyMessage(HANDLE_TIMER_OUT);
        super.show();
    }

    public void show(int timeOut) {
        timer = 0;
        this.timeOut = timeOut;
        handler.sendEmptyMessage(HANDLE_TIMER_OUT);
        super.show();
    }

    public interface TimeOutListener {
        public void timeOutConnectError();
    }
}
