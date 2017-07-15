/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package com.libra.loadingdialog.drawable;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Keep;

/**
 * Created by libra on 2017/7/7
 * 适配5.0以下的progressbar circle进度条效果
 */
public class IndeterminateCircularProgressDrawable extends BaseIndeterminateProgressDrawable
        implements MaterialProgressDrawable {

    private static final int PROGRESS_INTRINSIC_SIZE_DP = 42;
    private static final int PADDED_INTRINSIC_SIZE_DP = 48;
    private static final RectF RECT_BOUND = new RectF(-21, -21, 21, 21);
    private static final RectF RECT_PADDED_BOUND = new RectF(-24, -24, 24, 24);
    private static final RectF RECT_PROGRESS = new RectF(-19, -19, 19, 19);

    private int mProgressIntrinsicSize;
    private int mPaddedIntrinsicSize;
    private boolean mUseIntrinsicPadding = true;

    private RingPathTransform mRingPathTransform = new RingPathTransform();
    private RingRotation mRingRotation = new RingRotation();

    /**
     * Create a new {@code IndeterminateCircularProgressDrawable}.
     *
     * @param context the {@code Context} for retrieving style information.
     */
    public IndeterminateCircularProgressDrawable(Context context) {
        super(context);

        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicSize = Math.round(PROGRESS_INTRINSIC_SIZE_DP * density);
        mPaddedIntrinsicSize = Math.round(PADDED_INTRINSIC_SIZE_DP * density);

        mAnimators = new Animator[] {
                createIndeterminate(mRingPathTransform), createIndeterminateRotation(mRingRotation)
        };
    }

    private int getIntrinsicSize() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicSize : mProgressIntrinsicSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicWidth() {
        return getIntrinsicSize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getIntrinsicHeight() {
        return getIntrinsicSize();
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.MITER);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {

        if (mUseIntrinsicPadding) {
            canvas.scale(width / RECT_PADDED_BOUND.width(), height / RECT_PADDED_BOUND.height());
            canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        } else {
            canvas.scale(width / RECT_BOUND.width(), height / RECT_BOUND.height());
            canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);
        }

        drawRing(canvas, paint);
    }

    private void drawRing(Canvas canvas, Paint paint) {

        int saveCount = canvas.save();
        canvas.rotate(mRingRotation.mRotation);

        // startAngle starts at 3 o'clock on a watch.
        float startAngle = -90 + 360 * (mRingPathTransform.mTrimPathOffset
                + mRingPathTransform.mTrimPathStart);
        float sweepAngle =
                360 * (mRingPathTransform.mTrimPathEnd - mRingPathTransform.mTrimPathStart);
        canvas.drawArc(RECT_PROGRESS, startAngle, sweepAngle, false, paint);

        canvas.restoreToCount(saveCount);
    }

    public static Animator createIndeterminate(Object target) {

        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator trimPathStartAnimator =
                ObjectAnimator.ofFloat(target, "trimPathStart", 0, 0.75f);
        trimPathStartAnimator.setDuration(1333);
        trimPathStartAnimator.setInterpolator(Interpolators.TRIM_PATH_START.INSTANCE);
        trimPathStartAnimator.setRepeatCount(ValueAnimator.INFINITE);

        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator trimPathEndAnimator =
                ObjectAnimator.ofFloat(target, "trimPathEnd", 0, 0.75f);
        trimPathEndAnimator.setDuration(1333);
        trimPathEndAnimator.setInterpolator(Interpolators.TRIM_PATH_END.INSTANCE);
        trimPathEndAnimator.setRepeatCount(ValueAnimator.INFINITE);

        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator trimPathOffsetAnimator =
                ObjectAnimator.ofFloat(target, "trimPathOffset", 0, 0.25f);
        trimPathOffsetAnimator.setDuration(1333);
        trimPathOffsetAnimator.setInterpolator(Interpolators.LINEAR.INSTANCE);
        trimPathOffsetAnimator.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .playTogether(trimPathStartAnimator, trimPathEndAnimator, trimPathOffsetAnimator);
        return animatorSet;
    }

    /**
     * Create a backported Animator for
     * {@code @android:anim/progress_indeterminate_rotation_material}.
     *
     * @param target The object whose properties are to be animated.
     * @return An Animator object that is set up to behave the same as the its native counterpart.
     */
    public static Animator createIndeterminateRotation(Object target) {
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator rotationAnimator =
                ObjectAnimator.ofFloat(target, "rotation", 0, 720);
        rotationAnimator.setDuration(6665);
        rotationAnimator.setInterpolator(Interpolators.LINEAR.INSTANCE);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        return rotationAnimator;
    }

    private static class RingPathTransform {

        public float mTrimPathStart;
        public float mTrimPathEnd;
        public float mTrimPathOffset;

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathStart(float trimPathStart) {
            mTrimPathStart = trimPathStart;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathEnd(float trimPathEnd) {
            mTrimPathEnd = trimPathEnd;
        }

        @Keep
        @SuppressWarnings("unused")
        public void setTrimPathOffset(float trimPathOffset) {
            mTrimPathOffset = trimPathOffset;
        }
    }

    private static class RingRotation {

        private float mRotation;

        @Keep
        @SuppressWarnings("unused")
        public void setRotation(float rotation) {
            mRotation = rotation;
        }
    }
}
