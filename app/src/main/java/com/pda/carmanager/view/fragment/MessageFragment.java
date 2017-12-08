package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pda.carmanager.R;


/**
 * Created by Admin on 2017/11/29.
 */

public class MessageFragment extends Fragment {
    private RecyclerView recycler_message;
    private Activity context;
    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        context = getActivity();
        recycler_message = (RecyclerView) view.findViewById(R.id.recycler_message);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
    }
}
