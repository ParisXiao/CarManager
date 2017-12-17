package com.pda.carmanager.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.view.widght.LicenseKeyboardUtil;

/**
 * Created by Admin on 2017/12/14.
 */

public class KeyInputActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private TextView edit_key1;
    private TextView edit_key2;
    private TextView edit_key3;
    private TextView edit_key4;
    private TextView edit_key5;
    private TextView edit_key6;
    private TextView edit_area1;
    private TextView edit_area2;
    private TextView text_stucar;
    private Button keyboard_btn;
    private int inputType=0;
    private TextView[]  editTexts=null;
    private LicenseKeyboardUtil keyboardUtil;
    private LinearLayout linear_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyinput);
        initView();
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        if (intent!=null){
            inputType=intent.getIntExtra("inputType",inputType);
            if (inputType==1){
                edit_key6.setVisibility(View.VISIBLE);
                editTexts=new TextView[]{edit_area1,edit_area2,edit_key1,edit_key2,edit_key3,edit_key4,edit_key5,edit_key6};
                keyboardUtil=new LicenseKeyboardUtil(KeyInputActivity.this,editTexts,0);
            }
            else if (inputType==2){
                edit_key5.setVisibility(View.GONE);
                text_stucar.setVisibility(View.VISIBLE);
                editTexts=new TextView[]{edit_area1,edit_area2,edit_key1,edit_key2,edit_key3,edit_key4};
                keyboardUtil=new LicenseKeyboardUtil(KeyInputActivity.this,editTexts,0);

            }else {
                editTexts=new TextView[]{edit_area1,edit_area2,edit_key1,edit_key2,edit_key3,edit_key4,edit_key5};
                keyboardUtil=new LicenseKeyboardUtil(KeyInputActivity.this,editTexts,0);
            }
        }

        keyboardUtil.showKeyboard();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.input_car);

    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edit_key1 = (TextView) findViewById(R.id.edit_key1);
        edit_key2 = (TextView) findViewById(R.id.edit_key2);
        edit_key3 = (TextView) findViewById(R.id.edit_key3);
        edit_key4 = (TextView) findViewById(R.id.edit_key4);
        edit_key5 = (TextView) findViewById(R.id.edit_key5);
        edit_key6 = (TextView) findViewById(R.id.edit_key6);
        edit_area1 = (TextView) findViewById(R.id.edit_area1);
        edit_area2 = (TextView) findViewById(R.id.edit_area2);
        text_stucar = (TextView) findViewById(R.id.text_stucar);
        keyboard_btn = (Button) findViewById(R.id.keyboard_btn);
        linear_edit = (LinearLayout) findViewById(R.id.linear_edit);
        linear_edit.setOnClickListener(this);
        toolbar_left_btn.setOnClickListener(this);
        keyboard_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.keyboard_btn:
                Intent intent = new Intent(KeyInputActivity.this, DialogCarWriteActivity.class);
                Bundle bundle=new Bundle();
                intent.putExtra("area1",edit_area1.getText().toString().trim());
                intent.putExtra("area2",edit_area2.getText().toString().trim());
                intent.putExtra("key1",edit_key1.getText().toString().trim());
                intent.putExtra("key2",edit_key2.getText().toString().trim());
                intent.putExtra("key3",edit_key3.getText().toString().trim());
                intent.putExtra("key4",edit_key4.getText().toString().trim());
                intent.putExtra("key5",edit_key5.getText().toString().trim());
                intent.putExtra("key6",edit_key6.getText().toString().trim());
                Log.d("InputString","Key"+edit_key4.getText().toString().trim());
//                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.linear_edit:
                keyboardUtil.showKeyboard();
                break;
        }
    }

}
