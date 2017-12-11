package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.MessageAdapter;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.pullrefresh.PullRefreshRecyclerView;
import com.pda.carmanager.pullrefresh.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin on 2017/11/29.
 */

public class MessageFragment extends Fragment {
    private Activity context;
    private PullRefreshRecyclerView pullRefresh_msg;
    private List<MsgBean> msgBeanList=null;
    private MessageAdapter messageAdapter;
    private View mEmptyView;

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
        mEmptyView = view.findViewById(R.id.id_empty_view);
        pullRefresh_msg = (PullRefreshRecyclerView) view.findViewById(R.id.pullRefresh_msg);
        pullRefresh_msg.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        pullRefresh_msg.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle)));
        pullRefresh_msg.setHasFixedSize(true);
        pullRefresh_msg.setEmptyView(mEmptyView);
        msgBeanList=new ArrayList<>();
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        messageAdapter=new MessageAdapter(context,msgBeanList);
        pullRefresh_msg.setAdapter(messageAdapter);
        pullRefresh_msg.setOnRefreshListener(new PullRefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据

                //结束刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefresh_msg.stopRefresh();
                    }
                }, 2000);

            }

            @Override
            public void onLoadMore() {
                //加载更多

                //结束加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefresh_msg.stopLoadMore();
                    }
                }, 2000);
            }
        });
    }
}
