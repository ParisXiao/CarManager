package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.suke.widget.SwitchButton;

/**
 * Created by Administrator on 2017/12/10 0010.
 */

public class DialogCarWriteActivity extends BaseActivity implements View.OnClickListener {
    private Button chooseSmall;
    private Button chooseBig;
    private TextView text_carType_new;
    private SwitchButton chooseNew;
    private TextView text_carType_stu;
    private SwitchButton chooseStu;
    private Button AreaBtn;
    private IdentifyingCodeView identifyingCodeView;
    private TextView text_stucar;
    private ImageView camera_1;
    private ImageView camera_2;
    private Button pop1_exit;
    private Button pop1_next;
    private CustomerCarDialog areaDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pop_car);
        initView();
        initData();
    }


    private void initView() {
        chooseSmall = (Button) findViewById(R.id.pop_choose_small);
        chooseBig = (Button) findViewById(R.id.pop_choose_big);
        text_carType_new = (TextView) findViewById(R.id.text_carType_new);
        chooseNew = (SwitchButton) findViewById(R.id.btn_catType_new);
        text_carType_stu = (TextView) findViewById(R.id.text_carType_stu);
        chooseStu = (SwitchButton) findViewById(R.id.btn_catType_stu);
        AreaBtn = (Button) findViewById(R.id.btn_area);
        identifyingCodeView = (IdentifyingCodeView) findViewById(R.id.carNum_edit);
        text_stucar = (TextView) findViewById(R.id.text_stucar);
        camera_1 = (ImageView) findViewById(R.id.camera_1);
        camera_2 = (ImageView) findViewById(R.id.camera_2);
        pop1_exit = (Button) findViewById(R.id.pop1_exit);
        pop1_next = (Button) findViewById(R.id.pop1_next);

        chooseSmall.setOnClickListener(this);
        chooseBig.setOnClickListener(this);
        chooseNew.setOnClickListener(this);
        chooseStu.setOnClickListener(this);
        AreaBtn.setOnClickListener(this);
        pop1_exit.setOnClickListener(this);
        pop1_next.setOnClickListener(this);
    }
    private void initData() {
        areaDialog=new CustomerCarDialog(this,"贵","A");
        chooseNew.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    identifyingCodeView.setTextCount(6);
                    chooseNew.setChecked(true);
                    chooseStu.setChecked(false);
                    text_stucar.setVisibility(View.GONE);
                }

            }
        });
        chooseStu.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    identifyingCodeView.setTextCount(4);
                    chooseNew.setChecked(false);
                    chooseStu.setChecked(true);
                    text_stucar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_choose_small:
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose));
                break;
            case R.id.pop_choose_big:
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose));

                break;
            case R.id.btn_area:
                areaDialog.show();
                areaDialog.setOnDateDialogListener(new CustomerCarDialog.DateDialogListener() {
                    @Override
                    public void getDate() {
                        AreaBtn.setText(areaDialog.getSettingHour()+areaDialog.getSettingMinute());
                    }
                });
                break;
            case R.id.pop1_exit:

                break;
            case R.id.pop1_next:

                break;
        }
    }
}
