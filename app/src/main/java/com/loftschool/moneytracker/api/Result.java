package com.loftschool.moneytracker.api;


import android.text.TextUtils;

public class Result {

    String status;

    public boolean isSucsess () {
        return TextUtils.equals(status, "sucsess");
    }
}
