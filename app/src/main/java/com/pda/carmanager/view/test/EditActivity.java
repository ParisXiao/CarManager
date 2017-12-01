package com.pda.carmanager.view.test;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.pda.carmanager.view.widght.PwdEditText;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Admin on 2017/12/1.
 */

public class EditActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_edit;
    private PopupWindow popupWindow;
    private NiceSpinner nice_spinner1;
    private NiceSpinner nice_spinner2;
    private IdentifyingCodeView pwd_edit;
    private LinkedList<String> data1=new LinkedList<>(Arrays.asList("渝", "京", "津", "沪","冀"));
    private LinkedList<String> data2=new LinkedList<>(Arrays.asList("A", "B", "C", "D"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        initPopupwindow();

    }

    private void initPopupwindow() {
        View view= LayoutInflater.from(this).inflate(R.layout.layout_edit, null);
        popupWindow = new PopupWindow(this);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        nice_spinner1 = (NiceSpinner) view.findViewById(R.id.nice_spinner1);
        nice_spinner1.setTextColor(Color.BLACK);
        nice_spinner2 = (NiceSpinner) view.findViewById(R.id.nice_spinner2);
        nice_spinner2.setTextColor(Color.BLACK);
        pwd_edit = (IdentifyingCodeView)view.findViewById(R.id.pwd_edit);
        nice_spinner1.attachDataSource(data1);
        nice_spinner2.attachDataSource(data2);
        nice_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nice_spinner1.setText(data1.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nice_spinner1.setText(data1.get(0));
            }
        });
        nice_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nice_spinner2.setText(data2.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                nice_spinner2.setText(data2.get(0));
            }
        });
        pwd_edit.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {

            }

            @Override
            public void deleteContent() {

            }
        });

    }

    private void initView() {
        btn_edit = (Button) findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit:
                popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
                break;
        }
    }

    private void submit() {
        // validate

        // TODO validate success, do something


    }
}
