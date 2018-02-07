package com.shijiwei.aidlclient.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shijiwei.aidlclient.R;
import com.shijiwei.aidlserver.ICalculate;
import com.shijiwei.aidlserver.ICalculateStateCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText etAddFirstNumber;
    private EditText etAddSecendNumber;
    private TextView tvAddResult;
    private TextView btAdd;

    private ICalculate iCalculate;
    private ICalculateStateCallback iCalculateStateCallback = new ICalculateStateCallback.Stub() {
        @Override
        public void onBeforeCalculate(String ret) throws RemoteException {
            Log.e(TAG, ret);
        }

        @Override
        public void onCalculating(String ret) throws RemoteException {
            Log.e(TAG, ret);
        }

        @Override
        public void onCompleteCalculate(String ret) throws RemoteException {
            Log.e(TAG, ret);
        }
    };

    private ServiceConnection iCalculateConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iCalculate = ICalculate.Stub.asInterface(service);
            if (iCalculate != null)
                try {
                    iCalculate.registCalculateStateCallback(iCalculateStateCallback);
                    iCalculate.registCalculateStateCallback(iCalculateStateCallback);
                    iCalculate.registCalculateStateCallback(iCalculateStateCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iCalculate = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialView();
        initialEvn();
        _bindService();
    }

    private void _bindService() {
        Intent remoteIntent = new Intent();
        remoteIntent.setAction("com.shijiwei.aidlserver.service.CALCULATE");
        // 版本（5.0后）必须显式intent启动 绑定服务
        remoteIntent.setComponent(new ComponentName("com.shijiwei.aidlserver", "com.shijiwei.aidlserver.service.CalculateServer"));
        bindService(remoteIntent, iCalculateConn, BIND_AUTO_CREATE);

    }

    private void initialEvn() {
        btAdd.setOnClickListener(this);
    }

    private void initialView() {
        /* add */
        etAddFirstNumber = (EditText) findViewById(R.id.et_first_number);
        etAddSecendNumber = (EditText) findViewById(R.id.et_secend_number);
        tvAddResult = (TextView) findViewById(R.id.tv_add_result);
        btAdd = (TextView) findViewById(R.id.btn_add);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:

                double a, b;

                String aStr = etAddFirstNumber.getText().toString().trim();
                a = Double.parseDouble(aStr.length() == 0 ? "0" : aStr);

                String bStr = etAddSecendNumber.getText().toString().trim();
                b = Double.parseDouble(bStr.length() == 0 ? "0" : bStr);

                try {
                    if (iCalculate != null) {
                        double result = iCalculate.add(a, b);
                        tvAddResult.setText(result + "");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
