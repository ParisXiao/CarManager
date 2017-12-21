package com.pda.carmanager.view.test;

import android.app.Activity;
import android.content.OperationApplicationException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by King on 2016/8/3.
 */
public class chatHubActivity extends Activity implements View.OnClickListener {

    private final static String TAG = "KING";
    private final static String HUB_URL = "http://192.168.1.232:8008/signalr/hubs";
    private Button btnSend;
    private EditText chat_name;
    private EditText chat_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_hub);
        initView();
        beginConnect();
    }

    /**
     * hub链接
     */
    private HubConnection conn = new HubConnection(HUB_URL, this, new LongPollingTransport()) {
        @Override
        public void OnError(Exception exception) {
            Log.d(TAG, "OnError=" + exception.getMessage());
        }

        @Override
        public void OnMessage(String message) {
            Log.d(TAG, "message=" + message);
        }

        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.d(TAG, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
        }
    };

    /*
     * hub代理 panderman 2013-10-25
     */
    private IHubProxy hub = null;

    /**
     * 开启推送服务 panderman 2013-10-25
     */
    private void beginConnect() {
        try {
            hub = conn.CreateHubProxy("ChatHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
        hub.On("addNewMessageToPage", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
                EditText chatText = (EditText) findViewById(R.id.chat_value);
                //chatText.setText(args.toString());
                for (int i = 0; i < args.length(); i++) {
                    chatText.append(args.opt(i).toString());
                }
            }
        });
        conn.Start();
    }

    private void initView() {
        btnSend = (Button) findViewById(R.id.btnSend);

        btnSend.setOnClickListener(this);
        chat_name = (EditText) findViewById(R.id.chat_name);
        chat_name.setOnClickListener(this);
        chat_value = (EditText) findViewById(R.id.chat_value);
        chat_value.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:

                    submit();


                break;
        }
    }


    private void submit() {
        // validate
        String name = chat_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "name不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String value = chat_value.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            Toast.makeText(this, "value不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HubInvokeCallback callback = new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                Toast.makeText(chatHubActivity.this, response, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnError(Exception ex) {
                Toast.makeText(chatHubActivity.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
        List<String> args = new ArrayList<String>(2);
        args.add("123");
        args.add("xiaoli");
        hub.Invoke("login", args, callback);
        // TODO validate success, do something


    }
}
