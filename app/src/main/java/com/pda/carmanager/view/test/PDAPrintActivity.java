package com.pda.carmanager.view.test;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.provider.MediaStore;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.base.BaseApplication;
import com.pda.carmanager.service.ScanService;
import com.pda.carmanager.util.BarcodeCreater;
import com.pda.carmanager.util.BitmapTools;
import com.pda.carmanager.util.DialogUtil;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class PDAPrintActivity extends BaseActivity {
    private Button print;
    private PosApi mPosSDK;
    private Button btnCreat1D;
    private Button btnCreat2D;
    private Button btnCreatPic;
    private Button btnPrint;
    private Button btnPrintText;
    private Button btnPrintMix;
    private ImageView iv;
    private EditText etContent;

    private EditText etImgHeight;
    private EditText etImgWidth;
    private EditText etImgMarginLeft;
    private EditText etConcentration;

    private EditText ed_str;

    private Bitmap mBitmap = null;

    private PrintQueue mPrintQueue = null;

    private byte mGpioPower = 0x1E;// PB14
    private byte mGpioTrig = 0x29;// PC9

    private int mCurSerialNo = 3; // usart3
    private int mBaudrate = 4; // 9600

    private static final int REQUEST_EX = 1;

    MediaPlayer player;

    boolean isCanPrint=true;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_barcode);

        initViews();
        initData();


    }
    private void initViews() {
        // TODO Auto-generated method stub

        btnCreat1D = (Button) this.findViewById(R.id.btnCreat1d);
        btnCreat2D = (Button) this.findViewById(R.id.btnCreat2d);
        btnCreatPic = (Button) this.findViewById(R.id.btnCreatPic);
        btnPrintMix = (Button) this.findViewById(R.id.btnPrintMix);
        btnPrint = (Button) this.findViewById(R.id.btnPrint);
        btnPrintText = (Button) this.findViewById(R.id.btnPrintText);

        iv = (ImageView) this.findViewById(R.id.iv2d);

        etContent = (EditText) this.findViewById(R.id.etContent);
        etImgHeight = (EditText) this.findViewById(R.id.etContentHeight);
        etImgWidth = (EditText) this.findViewById(R.id.etContentWidth);
        etImgMarginLeft = (EditText) this.findViewById(R.id.etMarginLeft);
        etConcentration = (EditText) this.findViewById(R.id.etConcentration);

        ed_str = (EditText) this.findViewById(R.id.ed_str);

        ed_str.setText("");
        etContent.setText("1234567890");
        etImgHeight.setText("300");
        etImgWidth.setText("300");
        etImgMarginLeft.setText("0");
        etConcentration.setText("35");
        btnCreat1D.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etContent.getText().toString())
                        || TextUtils.isEmpty(etImgHeight.getText().toString())
                        || TextUtils.isEmpty(etImgWidth.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请检查图像参数",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etContent.getText().toString().getBytes().length > etContent.getText().toString().length()) {
                    Toast.makeText(
                            PDAPrintActivity.this,
                            getString(R.string.cannot_create_bar), Toast.LENGTH_SHORT)
                            .show();
                    return;
                }

                ed_str.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);

                int mWidth = Integer.valueOf(etImgWidth.getText().toString()
                        .trim());
                int mHeight = Integer.valueOf(etImgHeight.getText().toString()
                        .trim());
                mBitmap = BarcodeCreater.creatBarcode(getApplicationContext(),
                        etContent.getText().toString(), mWidth, mHeight, true,
                        1);

                iv.setImageBitmap(mBitmap);

            }
        });

        btnCreat2D.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(etContent.getText().toString())
                        || TextUtils.isEmpty(etImgHeight.getText().toString())
                        || TextUtils.isEmpty(etImgWidth.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "请检查图像参数",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ed_str.setVisibility(View.GONE);
                iv.setVisibility(View.VISIBLE);

                int mWidth = Integer.valueOf(etImgWidth.getText().toString()
                        .trim());
                int mHeight = Integer.valueOf(etImgHeight.getText().toString()
                        .trim());

                mBitmap = BarcodeCreater.encode2dAsBitmap(etContent.getText()
                        .toString(), mWidth, mHeight, 2);
                iv.setImageBitmap(mBitmap);

            }
        });

        btnCreatPic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
//				verifyStoragePermissions(PrintActivity.this);

                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_EX);

            }
        });

        btnPrintText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!isCanPrint) return;

                if(level_battry<=12){
                    Toast.makeText(PDAPrintActivity.this, "Low power,can't print!", Toast.LENGTH_SHORT).show();
                    return;
                }

                printText();
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mBitmap == null)
                    return;

                if(!isCanPrint) return;

                if(level_battry<=12){
                    Toast.makeText(PDAPrintActivity.this, "Low power,can't print!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int mLeft = Integer.valueOf(etImgMarginLeft.getText()
                        .toString().trim());
                byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
                int concentration = Integer.valueOf(etConcentration.getText()
                        .toString().trim());
//				if(concentration>=40){
//					concentration=40;
//				}

                if(concentration<=25){
                    concentration=25;
                }
                mPrintQueue.addBmp(concentration, mLeft, mBitmap.getWidth(),
                        mBitmap.getHeight(), printData);
                try {
                    mPrintQueue.addText(concentration, "\n\n\n\n\n".toString()
                            .getBytes("GBK"));
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isCanPrint=false;

                mPrintQueue.printStart();

            }
        });

        btnPrintMix.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!isCanPrint) return;

                if(level_battry<=12){
                    Toast.makeText(PDAPrintActivity.this, "Low power,can't print!", Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] test = new byte[] { 0x33, 0x34, 0x31, 0x33, 0x34, 0x31,
                        0x33, 0x34, 0x31, 0x33, 0x34, 0x31, 0x00 };
                String string = "微信支付 微信支付\n";

                try {
                    test = string.getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                printMix();

            }
        });
    }

    private void initData() {


        getButerryNum();

        // mPosApi = App.getInstance().getPosApi();
        mPrintQueue = new PrintQueue(this, ScanService.mApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        getString(R.string.print_complete), Toast.LENGTH_SHORT)
                        .show();

                isCanPrint=true;

            }

            @Override
            public void onFailed(int state) {
                // TODO Auto-generated method stub
                isCanPrint=true;
                switch (state) {
                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        // 打印缺纸
                        showTip(getString(R.string.print_no_paper));
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        // 打印失败
                        showTip(getString(R.string.print_failed));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        // 电压过低
                        showTip(getString(R.string.print_voltate_low));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        // 电压过高
                        showTip(getString(R.string.print_voltate_high));
                        break;
                }
            }

            @Override
            public void onGetState(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPrinterSetting(int state) {
                // TODO Auto-generated method stub
                isCanPrint=true;
                switch(state){
                    case 0:
                        Toast.makeText(PDAPrintActivity.this, "持续有纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(PDAPrintActivity.this, getString(R.string.no_paper), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(PDAPrintActivity.this, getString(R.string.label), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(PosApi.ACTION_POS_COMM_STATUS);
        registerReceiver(receiver, mFilter);

        player = MediaPlayer.create(getApplicationContext(), R.raw.beep);

        findViewById(R.id.black_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
            }
        });
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equalsIgnoreCase(PosApi.ACTION_POS_COMM_STATUS)) {
                int cmdFlag = intent.getIntExtra(PosApi.KEY_CMD_FLAG, -1);
                int status = intent.getIntExtra(PosApi.KEY_CMD_STATUS, -1);
                int bufferLen = intent.getIntExtra(PosApi.KEY_CMD_DATA_LENGTH,
                        0);
                byte[] buffer = intent
                        .getByteArrayExtra(PosApi.KEY_CMD_DATA_BUFFER);

                switch (cmdFlag) {
                    case PosApi.POS_EXPAND_SERIAL_INIT:
                        if (status == PosApi.COMM_STATUS_SUCCESS) {
                            // ed_str.setText("open success\n ");
                        } else {
                            // ed_str.setText("open fail\n");
                        }
                        break;
                    case PosApi.POS_EXPAND_SERIAL3:
                        if (buffer == null)
                            return;

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < buffer.length; i++) {
                            if (buffer[i] == 0x0D) {
                                // sb.append("\n");
                            } else {
                                sb.append((char) buffer[i]);
                            }
                        }
                        player.start();
                        try {
                            String str = new String(buffer, "GBK");

                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        break;
                }
                buffer = null;
            }
        }
    };

    private void openDevice() {
        // open power

        ScanService.mApi.gpioControl(mGpioPower, 0, 1);

        ScanService.mApi.extendSerialInit(mCurSerialNo, mBaudrate, 1, 1, 1, 1);

    }

    private void closeDevice() {
        // close power
        ScanService.mApi.gpioControl(mGpioPower, 0, 0);

        ScanService.mApi.extendSerialClose(mCurSerialNo);

    }

    String picPath;
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EX && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            picPath = picturePath;
            iv.setImageURI(selectedImage);
            mBitmap = resizeImage(BitmapFactory.decodeFile(picPath),384,384);
            if (mBitmap.getHeight() > 384) {
                mBitmap = BitmapFactory.decodeFile(picPath);
                iv.setImageBitmap(resizeImage(mBitmap, 384, 384));
            }
            ed_str.setVisibility(View.GONE);
            iv.setVisibility(View.VISIBLE);
            cursor.close();
        }
    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        if (width >= newWidth) {
            float scaleWidth = ((float) newWidth) / width;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                    height, matrix, true);
            return resizedBitmap;
        } else {

            Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
                    bitmap.getConfig());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(Color.WHITE);

            canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

            return bitmap2;
        }
    }

    /*
     * 打印文字 size 1 --倍大小 2--2倍大小
     */
    private void addPrintTextWithSize(int size, int concentration, byte[] data) {
        if (data == null)
            return;
        // 2倍字体大小
        byte[] _2x = new byte[] { 0x1b, 0x57, 0x02 };
        // 1倍字体大小
        byte[] _1x = new byte[] { 0x1b, 0x57, 0x01 };
        byte[] mData = null;
        if (size == 1) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_1x, 0, mData, 0, _1x.length);
            System.arraycopy(data, 0, mData, _1x.length, data.length);

            mPrintQueue.addText(concentration, mData);

        } else if (size == 2) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_2x, 0, mData, 0, _2x.length);
            System.arraycopy(data, 0, mData, _2x.length, data.length);

            mPrintQueue.addText(concentration, mData);

        }

    }

    /**
     * text to bitmap
     *
     * @param str
     * @return
     */
    public Bitmap word2bitmap(String str) {
        Bitmap bMap = Bitmap.createBitmap(384, 120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bMap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(35.0F);
        StaticLayout layout = new StaticLayout(str, textPaint, bMap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, (float) 1.0, (float) 0.0, true);
        layout.draw(canvas);
        return bMap;
    }


    private void printText() {
        int concentration = Integer.valueOf(etConcentration.getText()
                .toString().trim());

        if(concentration<=25){
            concentration=25;
        }

        String str = ed_str.getText().toString();

        try {
            addPrintTextWithSize(1, concentration,
                    (str + "\n").getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        isCanPrint=false;
        mPrintQueue.printStart();

    }


    private void printMix() {

        try {
            int concentration = Integer.valueOf(etConcentration.getText()
                    .toString().trim());

            if(concentration<=25){
                concentration=25;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("        收 银 凭 据                 ");
            sb.append("\n");
            sb.append("时间   : ");
            sb.append("2016-11-15     16:00");
            sb.append("\n");
            sb.append("操作员:admin");
            sb.append("\n");
            sb.append("收据单号：1234567890");
            sb.append("\n");
            sb.append("编号  数量  单价  折扣  小计");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("AM126   1  1200  0   1200");
            sb.append("\n");
            sb.append("AM127   1  1300  0   1300");
            sb.append("\n");
            sb.append("AM128   1  1400  0   1400");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("共销售数量: 3 ");
            sb.append("\n");
            sb.append("售价合计(RMB): 3900");
            sb.append("\n");
            sb.append("实收金额(RMB): 3900");
            sb.append("\n");
            sb.append("找零金额(RMB): 0");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("支付方式: 微信支付 ");
            sb.append("\n");
            sb.append("欢迎下次光临    请保留好小票！");
            sb.append("\n");

            sb.append("-----------------------------");
            sb.append("\n");
            byte[] text = null;
            text = sb.toString().getBytes("GBK");

            addPrintTextWithSize(1, concentration, text);

            sb = new StringBuilder();
            sb.append("   谢谢惠顾");
            sb.append("\n");

            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(2, concentration, text);

            sb = new StringBuilder();
            sb.append("\n");
            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(1, concentration, text);

            int mWidth = 300;
            int mHeight = 60;
            mBitmap = BarcodeCreater.creatBarcode(getApplicationContext(),
                    "1234567890", mWidth, mHeight, true, 1);
            byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);


            mPrintQueue.addBmp(concentration, 30, mBitmap.getWidth(),
                    mBitmap.getHeight(), printData);

            sb = new StringBuilder();
            sb.append("\n");
            sb.append("     扫一扫下载APP更多优惠");
            sb.append("\n");
            sb.append("\n");
            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(1, concentration, text);

            mWidth = 150;
            mHeight = 150;

            mBitmap = BarcodeCreater.encode2dAsBitmap("1234567890", mWidth,
                    mHeight, 2);
            printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
            mPrintQueue.addBmp(concentration, 100, mBitmap.getWidth(),
                    mBitmap.getHeight(), printData);

            sb = new StringBuilder();
            sb.append("1个月之内可凭票至服务台开具发票!");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            text = sb.toString().getBytes("GBK");

            addPrintTextWithSize(1, concentration, text);


            mPrintQueue.printStart();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showTip(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.tips))
                .setMessage(msg)
                .setNegativeButton(getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
    }

    /**
     * 文字转图片
     *
     * @param str
     * @return
     */
    public Bitmap word2bitmap2(String str,int wight,int height,int size) {
        Bitmap bMap = Bitmap.createBitmap(wight, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bMap);
        canvas.drawColor(Color.WHITE);
        TextPaint textPaint = new TextPaint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(size);
        StaticLayout layout = new StaticLayout(str, textPaint, bMap.getWidth(),
                Layout.Alignment.ALIGN_NORMAL, (float) 1.0, (float) 0.0, true);
        layout.draw(canvas);
        return bMap;
    }

    /**
     * 图片旋转
     * @param bm
     * @param orientationDegree
     * @return
     */
    Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        Matrix m = new Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
                (float) bm.getHeight() / 2);
        float targetX, targetY;
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }

        final float[] values = new float[9];
        m.getValues(values);

        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];

        m.postTranslate(targetX - x1, targetY - y1);

        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(),
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);

        return bm1;
    }

    public void getButerryNum(){
        registerReceiver(new BatteryBroadcastReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public int level_battry=50;

    /**接受电量改变广播*/
    class BatteryBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){

                level_battry = intent.getIntExtra("level", 0);
            }
        }
    }

    public static Bitmap textAsBitmap(String text, float textSize) {

        TextPaint textPaint = new TextPaint();

        // textPaint.setARGB(0x31, 0x31, 0x31, 0);
        textPaint.setColor(Color.BLACK);

        textPaint.setTextSize(textSize);

        StaticLayout layout = new StaticLayout(text, textPaint, 300,
                Layout.Alignment.ALIGN_NORMAL, 1.3f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(layout.getWidth() + 20,
                layout.getHeight() + 20, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(10, 10);
        canvas.drawColor(Color.WHITE);

        layout.draw(canvas);
        Log.d("textAsBitmap",
                String.format("1:%d %d", layout.getWidth(), layout.getHeight()));
        return bitmap;
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        openDevice();

        SharedPreferences sharedPreferences = this.getSharedPreferences("setting", MODE_PRIVATE);
        etImgHeight.setText(sharedPreferences.getString("height","300"));
        etImgWidth.setText(sharedPreferences.getString("wight","300"));

    }

    @Override
    protected void onStop() {
        //获取一个文件名为test、权限为private的xml文件的SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("setting", MODE_PRIVATE);

        //得到SharedPreferences.Editor对象，并保存数据到该对象中
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("height", etImgHeight.getText().toString().trim());
        editor.putString("wight", etImgWidth.getText().toString().trim());
        //保存key-value对到文件中
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mBitmap != null) {
            mBitmap.recycle();
        }

        if (mPrintQueue != null) {
            mPrintQueue.close();
        }
        unregisterReceiver(receiver);
    }
}
