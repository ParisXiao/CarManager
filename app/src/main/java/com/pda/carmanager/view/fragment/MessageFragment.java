package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.MessageAdapter;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.pullrefresh.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/11/29.
 */

public class MessageFragment extends Fragment implements PullToRefreshListener {
    private Activity context;
    private PullToRefreshRecyclerView pullRefresh_msg;
    private List<MsgBean> msgBeanList=null;
    private MessageAdapter messageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private void initView(View view) {
        context = getActivity();
        pullRefresh_msg = (PullToRefreshRecyclerView) view.findViewById(R.id.pullRefresh_msg);
        View emptyView = View.inflate(context, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        pullRefresh_msg.setEmptyView(emptyView);
        pullRefresh_msg.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        pullRefresh_msg.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle)));
        pullRefresh_msg.setHasFixedSize(true);
        //设置是否开启上拉加载
        pullRefresh_msg.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        pullRefresh_msg.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        pullRefresh_msg.displayLastRefreshTime(true);
        //设置刷新回调
        pullRefresh_msg.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_msg.onRefresh();
        msgBeanList=new ArrayList<>();
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        messageAdapter=new MessageAdapter(context,msgBeanList);
        pullRefresh_msg.setAdapter(messageAdapter);
    }
    @Override
    public void onRefresh() {
        pullRefresh_msg.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_msg.setRefreshComplete();
                messageAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        pullRefresh_msg.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_msg.setLoadMoreComplete(); //加载数据完成
                //模拟加载数据的情况
                for (int i = 0; i < 10; i++) {
                    msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
                }
                messageAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
