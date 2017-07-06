package com.single.baidumapbase.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by li on 2017/7/6.
 */

public class PhoneUtil {

    /**
     * 获取SIM硬件信息
     *
     * @return
     */
    public static String getTelephonyManager(Context context) {

        TelephonyManager   tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        StringBuffer sb = new StringBuffer();
        sb.append("DeviceId(IMEI)-----> " + tm.getDeviceId());
        sb.append("\nDeviceName ---->" + Build.MODEL);
        sb.append("\nDeviceFactory ---->" + Build.BOARD + "--" + Build.MANUFACTURER);
//        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
//        sb.append("\nLine1Number = " + tm.getLine1Number());
//        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
//        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
//        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
//        sb.append("\nNetworkType = " + tm.getNetworkType());
//        sb.append("\nPhoneType = " + tm.getPhoneType());
//        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
//        sb.append("\nSimOperator = " + tm.getSimOperator());
//        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
//        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
//        sb.append("\nSimState = " + tm.getSimState());
//        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
//        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
//        LogUtils.i(sb.toString());

        return sb.toString();
    }

}
