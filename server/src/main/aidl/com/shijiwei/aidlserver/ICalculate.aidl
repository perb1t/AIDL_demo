// ICalculate.aidl
package com.shijiwei.aidlserver;

// Declare any non-default types here with import statements
import  com.shijiwei.aidlserver.ICalculateStateCallback;

interface ICalculate {

    double add(double a,double b);

    void registCalculateStateCallback(ICalculateStateCallback cb);
}
