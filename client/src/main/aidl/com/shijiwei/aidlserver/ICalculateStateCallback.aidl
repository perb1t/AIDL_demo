// ICalculateStateCallback.aidl
package com.shijiwei.aidlserver;

// Declare any non-default types here with import statements

interface ICalculateStateCallback {

    void onBeforeCalculate(String ret);

    void onCalculating(String ret);

    void onCompleteCalculate(String ret);
}
