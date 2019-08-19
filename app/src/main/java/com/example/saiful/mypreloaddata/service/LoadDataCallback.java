package com.example.saiful.mypreloaddata.service;

public interface LoadDataCallback {
    void onPreload();
    void onProgressUpdate(long progress);
    void onLoadSucces();
    void onLoadFailed();
    void onLoadCancel();


}
