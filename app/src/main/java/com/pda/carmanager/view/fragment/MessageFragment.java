package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.MessageAdapter;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.presenter.MsgPresenter;
import com.pda.carmanager.pulltorefresh.PullToRefreshBase;
import com.pda.carmanager.pulltorefresh.PullToRefreshListView;
import com.pda.carmanager.service.SignalAService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.activity.LoginActivity;
import com.pda.carmanager.view.inter.IMsgViewInter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static android.content.Context.BIND_AUTO_CREATE;


/**
 * Created by Admin on 2017/11/29.
 */

public class MessageFragment extends Fragment implements IMsgViewInter {
    private Activity context;
    private PullToRefreshListView pullRefresh_msg;
    private List<MsgBean> msgBeanList = null;
    private List<MsgBean> msgBeanListShow = new ArrayList<>();
    private MessageAdapter messageAdapter;
    private MsgPresenter msgPresenter;
    private ImageView empty_img;
    private TextView empty_text;
    private View emptyView;
    private int page = 1;
    private int Pages = 0;
    private boolean reFreshNext;
    private boolean hasNext;
    private boolean isRefreah;
    private int list = 10;
    private int load = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!reFreshNext) {
                        msgBeanListShow.clear();
                    }
                    if (msgBeanList != null && msgBeanList.size() > 0) {
                        Log.e("list", list + "");
                        int s = msgBeanList.size() < list ? msgBeanList.size() : list;
                        for (int i = 0; i < s; i++) {
                            msgBeanListShow.add(msgBeanList.get(i));
                        }
                        messageAdapter.notifyDataSetChanged();
                    } else {
                        msgBeanListShow.clear();
                        messageAdapter.notifyDataSetChanged();
                    }
                    isRefreah = false;
                    reFreshNext = false;
                    pullRefresh_msg.setEmptyView(emptyView);
                    pullRefresh_msg.onRefreshComplete();
                    break;
                case 1:
                    pullRefresh_msg.onRefreshComplete();
                    break;
            }
        }
    };


    private SignalAService mService;
    private NewsServiceConn conn;


    public class NewsServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((SignalAService.LocalBinder) iBinder).getService();
            //将当前Activity添加为观察者
            mService.addNewsObservable(new Observer() {
                @Override
                public void update(Observable o, Object arg) {
                    MsgBean msgBean = (MsgBean) arg;
                    msgBeanListShow.add(0,msgBean);
                    messageAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }

    }

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
        pullRefresh_msg = (PullToRefreshListView) view.findViewById(R.id.pullRefresh_msg);
        pullRefresh_msg.setMode(PullToRefreshBase.Mode.BOTH);
        PullToRefreshBase.OnRefreshListener2<ListView> pullListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                load = 1;
                if (!isRefreah) {
                    isRefreah = true;
                    if (!reFreshNext) {
                        page = 1;
                    }
                    msgPresenter.getMsg(page + "", msgBeanList);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                load = 2;
                if (hasNext) {
                    page += 1;
                    reFreshNext = true;
                    if (!isRefreah) {
                        isRefreah = true;
                        if (!reFreshNext) {
                            page = 1;
                        }
                        msgPresenter.getMsg(page + "", msgBeanList);
                    }
                } else {
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
        pullRefresh_msg.setOnRefreshListener(pullListener);
        emptyView = View.inflate(context, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        msgBeanList = new ArrayList<>();
        msgPresenter = new MsgPresenter(context, this);
        messageAdapter = new MessageAdapter(context, msgBeanListShow);
        pullRefresh_msg.setAdapter(messageAdapter);
        conn = new NewsServiceConn();
        context.bindService(new Intent(context, SignalAService.class), conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        page=1;
        msgPresenter.getMsg(page + "", msgBeanList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context.unbindService(conn);
    }


    @Override
    public void getSuccess(String pages) {
        this.Pages = Integer.valueOf(pages);
        if (Pages <= page) {
            hasNext = false;
        } else {
            hasNext = true;
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getFail(String decs) {
        if (decs.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(context);
            AMUtil.actionStart(context, LoginActivity.class);
            context.finish();
        }
    }
}
