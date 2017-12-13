package com.pda.carmanager.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.config.CommonlConfig;
import com.pda.carmanager.inter.PhotoInter;
import com.pda.carmanager.util.PhotoUtils;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.pda.carmanager.view.widght.LicenseKeyboardUtil;
import com.pda.carmanager.view.widght.PhotoShowDialog;
import com.suke.widget.SwitchButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10 0010.
 */

public class DialogCarWriteActivity extends BaseActivity implements View.OnClickListener, PhotoInter {
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
    private EditText edit_key1;
    private EditText edit_key2;
    private EditText edit_key3;
    private EditText edit_key4;
    private EditText edit_key5;
    private EditText edit_key6;
    private EditText[] editTexts={edit_key1,edit_key2,edit_key3,edit_key4,edit_key5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        AreaBtn.setOnClickListener(this);
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

        edit_key1 = (EditText) findViewById(R.id.edit_key1);
        edit_key2 = (EditText) findViewById(R.id.edit_key2);
        edit_key3 = (EditText) findViewById(R.id.edit_key3);
        edit_key4 = (EditText) findViewById(R.id.edit_key4);
        edit_key5 = (EditText) findViewById(R.id.edit_key5);
        edit_key6 = (EditText) findViewById(R.id.edit_key6);
        edit_key6.setVisibility(View.GONE);
        text_stucar.setVisibility(View.GONE);
        keyboardUtil=new LicenseKeyboardUtil(this,editTexts,0);
    }

    private void initData() {
        IMG_PATH = PhotoUtils.getSDPath(this);
        areaDialog = new CustomerCarDialog(this, "贵", "A");
        chooseNew.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    text_stucar.setVisibility(View.GONE);

                    chooseNew.setChecked(true);
                    chooseStu.setChecked(false);
                    text_stucar.setVisibility(View.GONE);
                } else {
                    text_stucar.setVisibility(View.GONE);
                }

            }
        });
        chooseStu.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    text_stucar.setVisibility(View.VISIBLE);
                    chooseNew.setChecked(false);
                    chooseStu.setChecked(true);
                    text_stucar.setVisibility(View.VISIBLE);
                } else {
                    text_stucar.setVisibility(View.GONE);
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
                break;
            case R.id.pop_choose_big:
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose));

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

        if (requestCode == PHOTO_CAPTURE) {
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

    private void submit() {
        // validate
        String key1 = edit_key1.getText().toString().trim();
        if (TextUtils.isEmpty(key1)) {
            Toast.makeText(this, "key1不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String key2 = edit_key2.getText().toString().trim();
        if (TextUtils.isEmpty(key2)) {
            Toast.makeText(this, "key2不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String key3 = edit_key3.getText().toString().trim();
        if (TextUtils.isEmpty(key3)) {
            Toast.makeText(this, "key3不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String key4 = edit_key4.getText().toString().trim();
        if (TextUtils.isEmpty(key4)) {
            Toast.makeText(this, "key4不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String key5 = edit_key5.getText().toString().trim();
        if (TextUtils.isEmpty(key5)) {
            Toast.makeText(this, "key5不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String key6 = edit_key6.getText().toString().trim();
        if (TextUtils.isEmpty(key6)) {
            Toast.makeText(this, "key6不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something


    }
}
