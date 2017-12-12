package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class AddParkActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private EditText jiedaoNum_edit;
    private NiceSpinner niceSpinner;
    private EditText dici_edit;
    private EditText carnum_edit;
    private Button add_park_sure;
    private Button add_park_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpark);
        initView();
        initData();
    }

    private void initData() {
        niceSpinner = (NiceSpinner) findViewById(R.id.niceSpinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("街道段一", "街道段二", "街道段三", "街道段四", "街道段五"));
        niceSpinner.attachDataSource(dataset);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.add_park);
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        jiedaoNum_edit = (EditText) findViewById(R.id.jiedaoNum_edit);
        niceSpinner = (NiceSpinner) findViewById(R.id.niceSpinner);
        dici_edit = (EditText) findViewById(R.id.dici_edit);
        carnum_edit = (EditText) findViewById(R.id.carnum_edit);
        add_park_sure = (Button) findViewById(R.id.add_park_sure);
        add_park_exit = (Button) findViewById(R.id.add_park_exit);

        toolbar_left_btn.setOnClickListener(this);
        add_park_sure.setOnClickListener(this);
        add_park_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.add_park_sure:
                submit();
                break;
            case R.id.add_park_exit:
                finish();
                break;
        }
    }

    private void submit() {
        // validate
        String edit = jiedaoNum_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "请输入街道", Toast.LENGTH_SHORT).show();
            return;
        }

        String edit1 = dici_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit1)) {
            Toast.makeText(this, "请输入地磁编号", Toast.LENGTH_SHORT).show();
            return;
        }

        String edit3 = carnum_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit3)) {
            Toast.makeText(this, "请输入车位编号", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
