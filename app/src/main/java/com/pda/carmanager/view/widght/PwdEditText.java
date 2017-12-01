package com.pda.carmanager.view.widght;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Admin on 2017/11/30.
 */

public class PwdEditText extends EditText {
    /**
     * 间隔
     */
    private final int PWD_SPACING = 5;
    /**
     * 密码大小
     */
    private final int PWD_SIZE = 5;
    /**
     * 密码长度
     */
    private final int PWD_LENGTH = 5;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 输入画笔
     */
    private Paint editPaint;
    /**
     * 输入框画笔
     */
    private Paint rectPaint;
    /**
     * 输入框高宽；
     */
    private int rectWidth;
    private int rectHeight;
    /**
     * 输入框
     */
    private Rect rect;
    /**
     * 输入的密码长度
     */
    private int mInputLength;
    /**
     * 输入结束监听
     */
    private OnInputFinishListener mOnInputFinishListener;
    public PwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        editPaint=new Paint();
        editPaint.setColor(Color.BLACK);
        editPaint.setAntiAlias(true);
        editPaint.setStyle(Paint.Style.FILL);
        rectPaint=new Paint();
        rectPaint.setColor(Color.LTGRAY);
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectWidth=getWidth();
        rectHeight=getHeight();

        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(0,0,rectWidth,rectHeight,paint);
        int width=(rectWidth - PWD_SPACING * (PWD_LENGTH - 1)) / PWD_LENGTH;
        // 绘制密码框
        for (int i = 0; i < PWD_LENGTH; i++) {
            int left = (width + PWD_SPACING) * i;
            int top = 2;
            int right = left + width;
            int bottom = rectHeight - top;
            rect = new Rect(left, top, right, bottom);
            canvas.drawRect(rect, rectPaint);
        }

        // 绘制密码
        for (int i = 0; i < mInputLength; i++) {
            int cx = width / 2 + (width + PWD_SPACING) * i;
            int cy = rectHeight / 2;
            canvas.drawCircle(cx, cy, PWD_SIZE, editPaint);
        }
    }
    @Override
    protected void onTextChanged(CharSequence text, int start,
                                 int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mInputLength = text.toString().length();
        invalidate();
        if (mInputLength == PWD_LENGTH && mOnInputFinishListener != null) {
            mOnInputFinishListener.onInputFinish(text.toString());
        }
    }

    public interface OnInputFinishListener {
        /**
         * 密码输入结束监听
         *
         * @param password
         */
        void onInputFinish(String password);
    }

    /**
     * 设置输入完成监听
     *
     * @param onInputFinishListener
     */
    public void setOnInputFinishListener(
            OnInputFinishListener onInputFinishListener) {
        this.mOnInputFinishListener = onInputFinishListener;
    }
}
