package com.pda.carmanager.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.config.CommonlConfig;
import com.pda.carmanager.inter.PhotoInter;
import com.pda.carmanager.presenter.PostParkPresenter;
import com.pda.carmanager.service.ScanService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.BarcodeCreater;
import com.pda.carmanager.util.BitmapTools;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.GZIPutil;
import com.pda.carmanager.util.PhotoUtils;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IPostParkViewInter;
import com.pda.carmanager.view.test.PDAPrintActivity;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.pda.carmanager.view.widght.LicenseKeyboardUtil;
import com.pda.carmanager.view.widght.PhotoShowDialog;
import com.suke.widget.SwitchButton;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10 0010.
 */

public class DialogCarWriteActivity extends BaseActivity implements View.OnClickListener, PhotoInter,IPostParkViewInter {
    private Button chooseSmall;
    private Button chooseBig;
    private TextView text_carType_new;
    private SwitchButton chooseNew;
    private TextView text_carType_stu;
    private SwitchButton chooseStu;
    private Button AreaBtn;
    private TextView text_stucar;
    private ImageView camera_1;
    private ImageView camera_2;
    private Button pop1_exit;
    private Button pop1_next;
    private CustomerCarDialog areaDialog;
    private Button pop_choose_small;
    private Button pop_choose_big;
    private SwitchButton btn_catType_new;
    private SwitchButton btn_catType_stu;
    private String IMG_PATH;
    private static final int PHOTO_CAPTURE = 0x11;// 拍照
    private List<Bitmap> maps = new ArrayList<Bitmap>();
    private int flage = 0;
    private TextView text_camera1;
    private TextView text_camera2;
    private LicenseKeyboardUtil keyboardUtil;
    private TextView text_area1;
    private TextView text_area2;
    private TextView text_key1;
    private TextView text_key2;
    private TextView text_key3;
    private TextView text_key4;
    private TextView text_key5;
    private TextView text_key6;
    private LinearLayout linear_input;
    private int inputType = 0;
    private String carType = "1";
    private String carNum = "";
    private static final int RequsetInput = 110;
    private String area1;
    private String area2;
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String key5;
    private String key6;
    private boolean flag=false;
    private PostParkPresenter postParkPresenter;
    private String ParkId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_pop_car);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

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
        linear_input = (LinearLayout) findViewById(R.id.linear_input);
        linear_input.setOnClickListener(this);
        text_stucar = (TextView) findViewById(R.id.text_stucar);
        camera_1 = (ImageView) findViewById(R.id.camera_1);
        camera_2 = (ImageView) findViewById(R.id.camera_2);
        pop1_exit = (Button) findViewById(R.id.pop1_exit);
        pop1_next = (Button) findViewById(R.id.pop1_next);
        camera_1.setOnClickListener(this);
        camera_2.setOnClickListener(this);
        chooseSmall.setOnClickListener(this);
        chooseBig.setOnClickListener(this);
        chooseNew.setOnClickListener(this);
        chooseStu.setOnClickListener(this);
        pop1_exit.setOnClickListener(this);
        pop1_next.setOnClickListener(this);
        pop_choose_small = (Button) findViewById(R.id.pop_choose_small);
        pop_choose_small.setOnClickListener(this);
        pop_choose_big = (Button) findViewById(R.id.pop_choose_big);
        pop_choose_big.setOnClickListener(this);
        btn_catType_new = (SwitchButton) findViewById(R.id.btn_catType_new);
        btn_catType_new.setOnClickListener(this);
        btn_catType_stu = (SwitchButton) findViewById(R.id.btn_catType_stu);
        btn_catType_stu.setOnClickListener(this);
        text_camera1 = (TextView) findViewById(R.id.text_camera1);
        text_camera1.setOnClickListener(this);
        text_camera2 = (TextView) findViewById(R.id.text_camera2);
        text_camera2.setOnClickListener(this);

        text_key1 = (TextView) findViewById(R.id.text_key1);
        text_key2 = (TextView) findViewById(R.id.text_key2);
        text_key3 = (TextView) findViewById(R.id.text_key3);
        text_key4 = (TextView) findViewById(R.id.text_key4);
        text_key5 = (TextView) findViewById(R.id.text_key5);
        text_key6 = (TextView) findViewById(R.id.text_key6);
        text_area1 = (TextView) findViewById(R.id.text_area1);
        text_area2 = (TextView) findViewById(R.id.text_area2);
        text_key6.setVisibility(View.GONE);
        text_stucar.setVisibility(View.GONE);
    }

    private void initData() {
        postParkPresenter=new PostParkPresenter(this,this);
        ParkId=getIntent().getStringExtra("ParkId");
        IMG_PATH = PhotoUtils.getSDPath(this);
        areaDialog = new CustomerCarDialog(this, "贵", "A");
        chooseNew.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    chooseNew.setChecked(true);


                    if (chooseStu.isChecked()) {
                        chooseStu.setChecked(false);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_stucar.setVisibility(View.GONE);
                            text_key6.setVisibility(View.VISIBLE);
                            text_key5.setVisibility(View.VISIBLE);
                            inputType = 1;
                        }
                    }, 500);

                } else {
                    text_stucar.setVisibility(View.GONE);
                    text_key6.setVisibility(View.GONE);
                    text_key5.setVisibility(View.VISIBLE);
                    inputType = 0;
                }

            }
        });
        chooseStu.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    chooseStu.setChecked(true);

                    if (chooseNew.isChecked()) {
                        chooseNew.setChecked(false);
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            text_stucar.setVisibility(View.VISIBLE);
                            text_key6.setVisibility(View.GONE);
                            text_key5.setVisibility(View.GONE);
                            text_stucar.setVisibility(View.VISIBLE);
                            inputType = 2;
                        }
                    }, 500);
                } else {
                    text_stucar.setVisibility(View.GONE);
                    text_key6.setVisibility(View.GONE);
                    text_key5.setVisibility(View.VISIBLE);
                    inputType = 0;
                }
            }
        });
        camera_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (maps.size() == 0)
                    return true;
                removePhoto(0);
                return true;
            }
        });
        camera_2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (maps.size() == 0)
                    return true;
                if (maps.size() == 1)
                    return true;
                removePhoto(1);
                return true;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pop_choose_small:
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose));
                carType = "1";
                break;
            case R.id.pop_choose_big:
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose));
                carType = "2";
                break;
//            case R.id.btn_area:
//                areaDialog.show();
//                areaDialog.setOnDateDialogListener(new CustomerCarDialog.DateDialogListener() {
//                    @Override
//                    public void getDate() {
//                        AreaBtn.setText(areaDialog.getSettingHour() + areaDialog.getSettingMinute());
//                    }
//                });
//                break;
            case R.id.camera_1:
                if (maps.size() >= 1) {

                    PhotoShowDialog.showPhotoDialog(DialogCarWriteActivity.this, maps.get(0));
                } else {
                    takePhoto();
                }
                break;
            case R.id.camera_2:
                if (maps.size() >= 2) {
                    PhotoShowDialog.showPhotoDialog(DialogCarWriteActivity.this, maps.get(1));
                } else {
                    takePhoto();
                }
                break;
            case R.id.pop1_exit:
                finish();
                break;
            case R.id.pop1_next:
                if (inputType==0){
                    if (area1.equals("")||area2.equals("")||key1.equals("")||key2.equals("")||key3.equals("")||key4.equals("")||key5.equals("")){
                        Toast.makeText(this,"请输入完整车牌号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if (inputType==1){
                    if (area1.equals("")||area2.equals("")||key1.equals("")||key2.equals("")||key3.equals("")||key4.equals("")||key5.equals("")||key6.equals("")){
                        Toast.makeText(this,"请输入完整车牌号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if (inputType==2){
                    if (area1.equals("")||area2.equals("")||key1.equals("")||key2.equals("")||key3.equals("")||key4.equals("")){
                        Toast.makeText(this,"请输入完整车牌号",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (maps.size()!=2){
                    Toast.makeText(this,"请拍两张照片",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!flag){
                    flag=true;
                    DialogUtil.showMessage(DialogCarWriteActivity.this,getResources().getString(R.string.text_uping));
                    postParkPresenter.postPark(ParkId,carNum.trim(),carType, GZIPutil.compressForGzip(PhotoUtils.SendBitmap(maps.get(0))), GZIPutil.compressForGzip(PhotoUtils.SendBitmap(maps.get(1))));
                }else {
                    Toast.makeText(DialogCarWriteActivity.this,"数据提交中，请勿重复操作",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.linear_input:
                Intent intent1 = new Intent(DialogCarWriteActivity.this, KeyInputActivity.class);
                intent1.putExtra("inputType", inputType);
                startActivityForResult(intent1, RequsetInput);
                break;
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMG_PATH, "temp.jpg")));
        startActivityForResult(intent, PHOTO_CAPTURE);
        flage = CommonlConfig.PHOTO_KEY;
        return;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_CAPTURE:
                    Bitmap bitmap = PhotoUtils.decodeUriAsBitmap(Uri.fromFile(new File(IMG_PATH, "temp.jpg")), PhotoUtils.getBitmapDegree(Uri.fromFile(new File(IMG_PATH, "temp.jpg")).getPath()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bitmap = PhotoUtils.createWatermark(bitmap);
                    bitmap = PhotoUtils.createWatermark(bitmap);
                    maps.add(bitmap);
                    setPhoto();
                    break;
                case RequsetInput:
                    Bundle bundle = data.getExtras();
                    area1 = bundle.getString("area1");
                    area2 = bundle.getString("area2");
                    key1 = bundle.getString("key1");
                    key2 = bundle.getString("key2");
                    key3 = bundle.getString("key3");
                    key4 = bundle.getString("key4");
                    key5 = bundle.getString("key5");
                    key6 = bundle.getString("key6");
                    Log.d("InputString", key4);
                    text_key1.setText(key1);
                    text_key2.setText(key2);
                    text_key3.setText(key3);
                    text_key4.setText(key4);
                    text_key5.setText(key5);
                    text_key6.setText(key6);
                    text_area1.setText(area1);
                    text_area2.setText(area2);
                    carNum=area1+area2+key1+key2+key3+key4+key5+key6;
                    break;
            }
        }

    }

    private void removePhoto(final int index) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("提示");
        alertDialog.setMessage("是否删除照片");
        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (index == 0) {
                    if (maps.size() >= 0)
                        maps.remove(0);
                } else {
                    if (maps.size() >= 1)
                        maps.remove(1);
                }
                setPhoto();
            }
        });
        alertDialog.setNeutralButton("取消", null);
        alertDialog.create();
        alertDialog.show();

    }


    private void setPhoto() {
        for (int i = 0; i < maps.size(); i++) {
            if (i == 0) {
                camera_1.setImageBitmap(maps.get(i));
                text_camera1.setText(R.string.camera_del);
            } else if (i == 1) {
                camera_2.setImageBitmap(maps.get(i));
                text_camera2.setText(R.string.camera_del);
            }
        }
        int size = maps.size();
        if (size == 0) {
            text_camera1.setText(R.string.camera);
            text_camera2.setText(R.string.camera);
            camera_1.setImageResource(R.drawable.paizhao);
            camera_2.setImageResource(R.drawable.paizhao);
        } else if (size == 1) {
            camera_2.setImageResource(R.drawable.paizhao);
            text_camera1.setText(R.string.camera_del);
            text_camera2.setText(R.string.camera);
        }

    }

    @Override
    public void addBitmapList(List<Bitmap> bitmaps) {

    }

    @Override
    public void postSuccess(PrintBean printBean) {
        flag=false;
        Intent intent = new Intent(DialogCarWriteActivity.this, MyParkActivity.class);
        printBean.setCarNum(carNum);
        intent.putExtra("Print",printBean);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void postFail(String msg) {
        flag=false;
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(DialogCarWriteActivity.this);
            AMUtil.actionStart(DialogCarWriteActivity.this, LoginActivity.class);
            finish();
        }

    }
}
