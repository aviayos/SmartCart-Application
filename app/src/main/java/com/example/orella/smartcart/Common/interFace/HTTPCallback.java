package com.example.orella.smartcart.Common.interFace;

import android.net.NetworkInfo;

import com.example.orella.smartcart.Common.Result;

import java.util.ArrayList;

public interface HTTPCallback<T>{
    interface Progress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }
    void updateFailure(String result);


    void updateSuccess(Result result);

    NetworkInfo getActiveNetworkInfo();

    void onProgressUpdate(int progressCode, int percentComplete);

    void finish();
}
