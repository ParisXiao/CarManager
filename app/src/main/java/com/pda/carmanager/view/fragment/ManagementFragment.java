package com.pda.carmanager.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pda.carmanager.R;


/**
 * Created by Admin on 2017/11/29.
 */

public class ManagementFragment extends Fragment {
    private RelativeLayout my_park;
    private RelativeLayout add_park;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_manaement, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        my_park = (RelativeLayout) layout.findViewById(R.id.my_park);
        add_park = (RelativeLayout) layout.findViewById(R.id.add_park);
    }
}
