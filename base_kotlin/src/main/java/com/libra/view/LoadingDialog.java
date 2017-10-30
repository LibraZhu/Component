package com.libra.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.libra.base.R;

/**
 * Created by libra on 2017/6/17.
 */

public class LoadingDialog extends ProgressDialog {
    private static final int HANDLE_TIMER_OUT = 2;
    private int timeOut = 70;
    private int timer = 0;
    private int w, h;
    private TimeOutListener listener;
    private String message;
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
        super(context, R.style.CommonDialog);
    }

    public LoadingDialog(Context context, int width, int height) {
        super(context, R.style.CommonDialog);
        w = width;
        h = height;
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
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        frameLayout.addView(linearLayout,
                            new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                         ViewGroup.LayoutParams.WRAP_CONTENT,
                                                         Gravity.CENTER));
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//适配5.0以下
            ImageView imageView = new ImageView(getContext());
            final MaterialProgressDrawable drawable =
                    new MaterialProgressDrawable(getContext(), imageView);
            drawable.updateSizes(MaterialProgressDrawable.LARGE);
            drawable.setBackgroundColor(
                    ContextCompat.getColor(getContext(), android.R.color.transparent));
            drawable.setColorSchemeColors(
                    ContextCompat.getColor(getContext(), R.color.colorAccent));
            imageView.setImageDrawable(drawable);
            drawable.setAlpha(255);
            drawable.start();
            linearLayout.addView(imageView,
                                 new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                              ViewGroup.LayoutParams.WRAP_CONTENT));
            setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handler.removeMessages(HANDLE_TIMER_OUT);
                    timer = 0;
                    if (drawable != null) {
                        drawable.stop();
                    }
                }
            });
        } else {
            ProgressBar progressBar = new ProgressBar(getContext());
            progressBar.setInterpolator(new LinearInterpolator());
            linearLayout.addView(progressBar,
                                 new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                              ViewGroup.LayoutParams.WRAP_CONTENT));
            setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    handler.removeMessages(HANDLE_TIMER_OUT);
                    timer = 0;
                }
            });
        }
        TextView textView = new TextView(getContext());
        if (!TextUtils.isEmpty(message)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(message);
        } else {
            textView.setVisibility(View.GONE);
        }
        linearLayout.addView(textView,
                             new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                          ViewGroup.LayoutParams.WRAP_CONTENT));
        if (w > 0 && h > 0) {
            setContentView(frameLayout, new FrameLayout.LayoutParams(w, h, Gravity.CENTER));
        } else {
            setContentView(frameLayout,
                           new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                                        Gravity.CENTER));
        }
    }

    @Override
    public void show() {
        timer = 0;
        super.show();
    }

    public void show(int timeOut) {
        timer = 0;
        this.timeOut = timeOut;
        handler.sendEmptyMessage(HANDLE_TIMER_OUT);
        super.show();
    }

    public LoadingDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public interface TimeOutListener {
        public void timeOutConnectError();
    }
}
