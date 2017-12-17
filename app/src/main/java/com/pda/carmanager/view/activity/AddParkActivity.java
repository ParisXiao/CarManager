package com.pda.carmanager.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.SweetBean;
import com.pda.carmanager.bean.SweetDuanBean;
import com.pda.carmanager.presenter.AddParkPresenter;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.FormatUtil;
import com.pda.carmanager.util.SearchAdapter;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IAddParkViewInter;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class AddParkActivity extends BaseActivity implements View.OnClickListener, IAddParkViewInter {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private AutoCompleteTextView jiedaoNum_edit;
    private NiceSpinner niceSpinner;
    private EditText dici_edit;
    private EditText carnum_edit;
    private Button add_park_sure;
    private Button add_park_exit;
    private AddParkPresenter addParkPresenter;
    private List<SweetBean> sweetBeens = new ArrayList<>();
    private List<SweetDuanBean> sweetDuanBeen = new ArrayList<>();
    private String jd;
    private String jdd;

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
        DialogUtil.showMessage(this, getResources().getString(R.string.text_loading));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.add_park);
        addParkPresenter = new AddParkPresenter(this, this);
        addParkPresenter.getSweetData(sweetBeens);
        jiedaoNum_edit.addTextChangedListener(new listener1());
        niceSpinner.addTextChangedListener(new listener2());

    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        jiedaoNum_edit = (AutoCompleteTextView) findViewById(R.id.jiedaoNum_edit);
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

    class listener1 implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            jiedaoNum_edit.getText().toString().trim();
            List<String> strings=new ArrayList<String>();
            for (int i = 0; i < sweetBeens.size()&&sweetBeens.size()>0; i++) {
                if (jiedaoNum_edit.getText().toString().trim().equals(sweetBeens.get(i).getName())){
                    jd=sweetBeens.get(i).getId();
                    sweetDuanBeen=new ArrayList<>();
                    for (int j = 0; j < sweetBeens.get(i).getSweetDuanBean().size(); j++) {
                        SweetDuanBean sweetDuanBean=new SweetDuanBean();
                        sweetDuanBean.setId(sweetBeens.get(i).getSweetDuanBean().get(j).getId());
                        sweetDuanBean.setName(sweetBeens.get(i).getSweetDuanBean().get(j).getName());
                        sweetDuanBeen.add(sweetDuanBean);
                        strings.add(sweetBeens.get(i).getSweetDuanBean().get(j).getName());
                    }
                }
            }
            if (strings.size()>0) {
                Log.d("jdd",FormatUtil.listToString(strings));
                niceSpinner.attachDataSource(strings);
            }
        }
    }
    class listener2 implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (sweetDuanBeen.size()>0){
                for (int i = 0; i < sweetDuanBeen.size(); i++) {
                    if (niceSpinner.getText().toString().trim().equals(sweetDuanBeen.get(i).getName())){
                        jdd=sweetDuanBeen.get(i).getId();
                    }
                }
            }
            if (jdd!=null) {
                Log.d("jdd", jdd);
            }
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
        addParkPresenter.addPark(jd,jdd,edit1,edit3);

        // TODO validate success, do something


    }

    @Override
    public void getSuccesss() {

        List<String> str = new ArrayList<>();
        if (sweetBeens.size() > 0) {
            for (int i = 0; i < sweetBeens.size(); i++) {
                str.add(sweetBeens.get(i).getName());

            }
            SearchAdapter<String> adapter = new SearchAdapter<String>(AddParkActivity.this,
                    android.R.layout.simple_list_item_1, str, SearchAdapter.ALL);
            jiedaoNum_edit.setAdapter(adapter);
        }
    }

    @Override
    public void getFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(AddParkActivity.this);
            AMUtil.actionStart(AddParkActivity.this, LoginActivity.class);
            finish();
        }else {
            finish();
        }
    }

    @Override
    public void getDuanSuccesss() {

    }

    @Override
    public void getDuanFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(AddParkActivity.this);
            AMUtil.actionStart(AddParkActivity.this, LoginActivity.class);
            finish();
        }
    }

    @Override
    public void addSuccess()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        finish();
    }

    @Override
    public void addFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(AddParkActivity.this);
            AMUtil.actionStart(AddParkActivity.this, LoginActivity.class);
            finish();
        }
    }
}
