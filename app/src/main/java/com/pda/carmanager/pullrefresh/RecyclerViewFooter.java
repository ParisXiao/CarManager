package com.pda.carmanager.pullrefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pda.carmanager.R;


/**
 */

public class RecyclerViewFooter extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private Context context;
    private View contentView;
    private View progressBar;
    private TextView hintView;
    private TextView refreshView;


    public RecyclerViewFooter(Context context) {
        this(context, null);
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();

    }

    private void initView() {
        LinearLayout moreView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.pullrefrefh_recyclerview_footer, null);
        addView(moreView);
        moreView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        contentView = moreView.findViewById(R.id.pullrefrefh_footer_content);
        progressBar = moreView.findViewById(R.id.pullrefrefh_footer_ProgressBar);
        hintView = (TextView) moreView.findViewById(R.id.pullrefrefh_footer_hint_TextView);
        refreshView = (TextView) moreView.findViewById(R.id.pullrefrefh_footer_refresh_TextView);
    }

    /**
     * 设置状态
     *
     * @param state
     */
    public void setState(int state) {
        hintView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        hintView.setVisibility(View.GONE);
        if (state == STATE_READY) {
            hintView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            refreshView.setVisibility(View.GONE);
            hintView.setText(R.string.text_loaded);
        } else if (state == STATE_LOADING) {
            progressBar.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.VISIBLE);
            hintView.setVisibility(View.GONE);
            refreshView.setText(R.string.text_loading);
        } else if (state == STATE_NORMAL) {
            progressBar.setVisibility(View.GONE);
            refreshView.setVisibility(View.GONE);
            hintView.setVisibility(View.VISIBLE);
            hintView.setText(R.string.text_refresh_up);
        }
    }

    /**
     * 设置距离下边的BottomMargin
     *
     * @param height
     */
    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.bottomMargin = height;
        contentView.setLayoutParams(lp);

    }

    /**
     * 获取BottomMargin
     *
     * @return
     */
    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        return lp.bottomMargin;
    }


    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.height = 0;
        contentView.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) contentView.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        contentView.setLayoutParams(lp);
    }
}
