package com.alexandr4.loftmoney;

import android.text.TextUtils;

public class Result {
    String status;

    public boolean isSuccess() {
        return TextUtils.equals(status, "success");
    }
}