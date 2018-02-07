package com.shijiwei.aidlserver.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shijiwei.aidlserver.ICalculate;
import com.shijiwei.aidlserver.ICalculateStateCallback;

/**
 * Created by shijiwei on 2018/2/6.
 *
 * @VERSION 1.0
 */

public class CalculateServer extends Service {

    private static final String TAG = "CalculateServer";

    private RemoteCallbackList<ICalculateStateCallback> mRcbList = new RemoteCallbackList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    private IBinder iBinder = new ICalculate.Stub() {

        @Override
        public double add(double a, double b) throws RemoteException {
            int N = mRcbList.getRegisteredCallbackCount();
            Log.e(TAG, " Regist cb Number is ： " + N);
            mRcbList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                ICalculateStateCallback cb = mRcbList.getBroadcastItem(i);
                cb.onBeforeCalculate(cb + " |onBeforeCalculate");
                cb.onCalculating(cb + " |onCalculating");
                cb.onCompleteCalculate(cb + " | onCompleteCalculate");
            }
            mRcbList.finishBroadcast();
            return a + b;
        }

        @Override
        public void registCalculateStateCallback(ICalculateStateCallback cb) throws RemoteException {
            mRcbList.register(cb);
        }
    };
}
