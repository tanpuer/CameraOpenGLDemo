package com.example.templechen.cameraopengldemo.mediaCodec;

import android.hardware.Camera;

public interface ICameraPreviewCallback {
    void onPreviewFrame(byte[] data, Camera camera);
}
