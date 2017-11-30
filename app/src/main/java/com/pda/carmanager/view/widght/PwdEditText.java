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
    private final int PWD_LENGTH = 6;
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
    }
}
