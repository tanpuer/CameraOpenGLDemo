package com.example.templechen.cameraopengldemo.Base;

import android.graphics.SurfaceTexture;

public abstract class BaseCamera {

    public abstract boolean openCamera(int width, int height, int cameraId);

    public abstract void setSurfaceTexture(SurfaceTexture mSurfaceTexture);

    public abstract void startPreview();

    public abstract void stopPreview();

    public abstract void releaseCamera();

}
