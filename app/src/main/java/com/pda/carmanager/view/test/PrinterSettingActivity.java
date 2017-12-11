package com.pda.carmanager.view.test;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.pda.carmanager.R;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.util.BluetoothUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PrintUtil;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrinterSettingActivity extends BasePrintActivity implements View.OnClickListener {

    ListView mLvPairedDevices;
    Button mBtnSetting;
    Button mBtnTest;
    Button mBtnPrint;
    Button mBtnZXing;
    Bitmap bitmapZxing;


    DeviceListAdapter mAdapter;
    int mSelectedPosition = -1;

    final static int TASK_TYPE_CONNECT = 1;
    final static int TASK_TYPE_PRINT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printer_setting);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillAdapter();
    }

    private void initViews() {
        mLvPairedDevices = (ListView) findViewById(R.id.lv_paired_devices);
        mBtnSetting = (Button) findViewById(R.id.btn_goto_setting);
        mBtnTest = (Button) findViewById(R.id.btn_test_conntect);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnZXing = (Button) findViewById(R.id.btn_test_zxing);

        mLvPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedPosition = position;
                mAdapter.notifyDataSetChanged();
            }
        });

        mBtnSetting.setOnClickListener(this);
        mBtnTest.setOnClickListener(this);
        mBtnPrint.setOnClickListener(this);
        mBtnZXing.setOnClickListener(this);

        mAdapter = new DeviceListAdapter(this);
        mLvPairedDevices.setAdapter(mAdapter);
    }

    /**
     * 从所有已配对设备中找出打印设备并显示
     */
    private void fillAdapter() {
        //推荐使用 BluetoothUtil.getPairedPrinterDevices()
        List<BluetoothDevice> printerDevices = BluetoothUtil.getPairedDevices();
        mAdapter.clear();
        mAdapter.addAll(printerDevices);
        refreshButtonText(printerDevices);
    }

    private void refreshButtonText(List<BluetoothDevice> printerDevices) {
        if (printerDevices.size() > 0) {
            mBtnSetting.setText("配对更多设备");
        } else {
            mBtnSetting.setText("还未配对打印机，去设置");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_setting:
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                break;

            case R.id.btn_test_conntect:
                connectDevice(TASK_TYPE_CONNECT);
                break;

            case R.id.btn_print:
                connectDevice(TASK_TYPE_PRINT);
                break;
            case R.id.btn_test_zxing:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String password="123456";
                        String strBase64 = new String(Base64.encode(password.getBytes(), Base64.DEFAULT));
                        String[] key = new String[]{"username","password","encode"};
                        Map<String,String> map=new HashMap<String, String>();
                        map.put("password",strBase64);
                        map.put("encode","1213");
                        OKHttpUtil.GetMessage(PrinterSettingActivity.this,key,map);
//                        OKHttpUtil.SendDataByPost(UrlConfig.HttpUrl);
                    }
                }).start();
                break;

        }
    }

    private void connectDevice(int taskType) {
        if (mSelectedPosition >= 0) {
            BluetoothDevice device = mAdapter.getItem(mSelectedPosition);
            if (device != null)
                super.connectDevice(device, taskType);
        } else {
            Toast.makeText(this, "还未选择打印设备", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打印参数
     *
     * @param socket
     * @param taskType
     */
    @Override
    public void onConnected(BluetoothSocket socket, int taskType) {
        switch (taskType) {
            case TASK_TYPE_PRINT:
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                Bitmap bitmap = EncodingUtils.createQRCode("www.baidu.com", 360, 360, logoBitmap);
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_image);
                PrintUtil.printTest(socket, bitmap, "石桥铺", "001", "渝A74110", "2017年12月6日 08:29:12", "11", "重庆解放碑", "002", "2017年12月1日 09:11:09", "2017年12月2日 12:12:56");

                break;
        }
    }


    class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

        public DeviceListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            BluetoothDevice device = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_bluetooth_device, parent, false);
            }

            TextView tvDeviceName = (TextView) convertView.findViewById(R.id.tv_device_name);
            CheckBox cbDevice = (CheckBox) convertView.findViewById(R.id.cb_device);

            tvDeviceName.setText(device.getName());

            cbDevice.setChecked(position == mSelectedPosition);

            return convertView;
        }
    }
}
