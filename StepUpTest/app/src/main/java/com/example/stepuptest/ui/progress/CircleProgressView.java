package com.example.stepuptest.ui.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleProgressView extends View {

    private Paint backgroundPaint;
    private Paint progressPaint;
    private float progress = 0f;

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xFFE0E0E0); // Light gray
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20f);
        backgroundPaint.setAntiAlias(true);

        progressPaint = new Paint();
        progressPaint.setColor(0xFF3F51B5); // Blue
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(20f);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = Math.min(getWidth(), getHeight());
        int padding = 20;
        int radius = (size - padding * 2) / 2;

        canvas.drawCircle(getWidth()/2, getHeight()/2, radius, backgroundPaint);

        float sweepAngle = 360 * progress;
        canvas.drawArc(
                padding, padding,
                getWidth() - padding, getHeight() - padding,
                -90, sweepAngle,
                false, progressPaint
        );
    }

    public void setProgress(float progress) {
        this.progress = Math.min(1f, Math.max(0f, progress)); // between 0 and 1
        invalidate(); // redraw
    }
}