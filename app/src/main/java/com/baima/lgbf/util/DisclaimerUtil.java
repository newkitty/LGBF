package com.baima.lgbf.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DisclaimerUtil{

    public static void showDisclaimerDialog(Activity activity){
        String message="本程序 内容仅供参考，" +
                "因使用本程序产生的问题作者不予负责。";
        new AlertDialog.Builder(activity)
                .setTitle("免责声明")
                .setMessage(message)
                .setNegativeButton("确定",null)
                .show();
    }
}
