package com.pda.carmanager.view.widght;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class LoopProgressBar extends View {
    private int topStartX;

    private int width;
    private int height;

    private ValueAnimator animator;

    public LoopProgressBar(Context context) {
        super(context);
    }

    public LoopProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoopProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoopProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        int drawWidth = height;
        if (height > width) return;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(new RectF(0, 0, width, height),
                height / 2, height / 2, paint);
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawRoundRect(new RectF(0, 0, width, height),
                height / 2, height / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        paint.setColor(Color.rgb(254, 166, 1));
        Path path;
        int count = width / height;
        for (int i = -1 ; i < count + 1; i += 2) {
            path = new Path();
            path.moveTo(topStartX + i * drawWidth, 0);
            path.lineTo(topStartX + (i + 1) * drawWidth, 0);
            path.lineTo(topStartX + i * drawWidth, height);
            path.lineTo(topStartX + (i - 1)* drawWidth, height);
            path.close();
            canvas.drawPath(path, paint);
        }
        //最后将画笔去除Xfermode
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);

    }

    public void start() {
        if (animator != null && animator.isRunning())
            return;
        animator = new ValueAnimator().ofFloat(0, 20);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                topStartX = (int) (height * value / 10);
                invalidate();
            }
        });
        animator.start();
    }

    public void stop() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = null;
    }
}
