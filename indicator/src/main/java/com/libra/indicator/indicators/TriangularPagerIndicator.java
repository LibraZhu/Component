package com.libra.indicator.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import com.libra.indicator.abs.IPagerIndicator;
import com.libra.indicator.helper.FragmentContainerHelper;
import com.libra.indicator.model.PositionData;
import java.util.List;

/**
 * 带有小尖角的直线指示器
 */
public class TriangularPagerIndicator extends View implements IPagerIndicator {
    private List<PositionData> mPositionDataList;
    private Paint mPaint;
    private int mLineHeight;
    private int mLineColor;
    private int mTriangleHeight;
    private int mTriangleWidth;
    private float mYOffset;

    private Path mPath = new Path();
    private Interpolator mStartInterpolator = new LinearInterpolator();
    private float mAnchorX;

    public TriangularPagerIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mLineHeight = dip2px(context, 3);
        mTriangleWidth = dip2px(context, 14);
        mTriangleHeight = dip2px(context, 8);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setStrokeWidth(dip2px(getContext(), 2));
        mPaint.setColor(mLineColor);
        canvas.drawLine(0, getHeight() - mYOffset, mAnchorX - mTriangleWidth / 2,
                        getHeight() - mYOffset, mPaint);
        mPaint.setStrokeWidth(dip2px(getContext(), 1));
        canvas.drawLine(mAnchorX - mTriangleWidth / 2, getHeight() - mYOffset, mAnchorX,
                        getHeight() - mTriangleHeight - mYOffset, mPaint);
        canvas.drawLine(mAnchorX - 1, getHeight() - mTriangleHeight - mYOffset,
                        mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset, mPaint);
        mPaint.setStrokeWidth(dip2px(getContext(), 2));
        canvas.drawLine(mAnchorX + mTriangleWidth / 2, getHeight() - mYOffset, getWidth(),
                        getHeight() - mYOffset, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDataList == null || mPositionDataList.isEmpty()) {
            return;
        }

        // 计算锚点位置
        PositionData current =
                FragmentContainerHelper.getImitativePositionData(mPositionDataList, position);
        PositionData next =
                FragmentContainerHelper.getImitativePositionData(mPositionDataList, position + 1);

        float leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mLeft + (next.mRight - next.mLeft) / 2;

        mAnchorX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);

        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDataList = dataList;
    }

    public int getLineHeight() {
        return mLineHeight;
    }

    public void setLineHeight(int lineHeight) {
        mLineHeight = lineHeight;
    }

    public int getLineColor() {
        return mLineColor;
    }

    public void setLineColor(int lineColor) {
        mLineColor = lineColor;
    }

    public int getTriangleHeight() {
        return mTriangleHeight;
    }

    public void setTriangleHeight(int triangleHeight) {
        mTriangleHeight = triangleHeight;
    }

    public int getTriangleWidth() {
        return mTriangleWidth;
    }

    public void setTriangleWidth(int triangleWidth) {
        mTriangleWidth = triangleWidth;
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float yOffset) {
        mYOffset = yOffset;
    }

    public int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue,
                                               context.getResources().getDisplayMetrics());
    }

}
