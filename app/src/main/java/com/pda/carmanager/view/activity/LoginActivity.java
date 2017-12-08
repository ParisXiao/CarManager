package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.HideSoftKeyboardUtil;

/**
 * Created by Admin on 2017/12/7.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private Toolbar toolbar;
    private EditText name_edit;
    private EditText password_edit;
    private EditText encode_edit;
    private Button button;
    private RelativeLayout layout_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name_edit = (EditText) findViewById(R.id.name_edit);
        password_edit = (EditText) findViewById(R.id.password_edit);
        encode_edit = (EditText) findViewById(R.id.encode_edit);
        button = (Button) findViewById(R.id.button_login);

        button.setOnClickListener(this);
        name_edit.setOnClickListener(this);
        password_edit.setOnClickListener(this);
        encode_edit.setOnClickListener(this);
        layout_login = (RelativeLayout) findViewById(R.id.layout_login);

    }
    private void initData(){
        toolbar_mid.setText("泊讯停车|云视临街收费终端");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        UpdateUI(layout_login);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                DialogUtil.showMessage(LoginActivity.this,"登录中...");
                break;
            case R.id.name_edit:
                name_edit.setCursorVisible(true);
                break;
            case R.id.password_edit:
                password_edit.setCursorVisible(true);
                break;
            case R.id.encode_edit:
                encode_edit.setCursorVisible(true);
                break;
        }
    }

    public void UpdateUI(View view) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub
                    HideSoftKeyboardUtil.hideSoftKeyboard(LoginActivity.this);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                UpdateUI(innerView);
            }
        }
    }

    private void submit() {
        // validate
        String edit = name_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "edit不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edit1 = password_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "edit不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String edit2 = encode_edit.getText().toString().trim();
        if (TextUtils.isEmpty(edit)) {
            Toast.makeText(this, "edit不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
