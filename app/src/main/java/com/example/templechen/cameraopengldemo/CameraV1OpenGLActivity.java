package com.example.templechen.cameraopengldemo;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class CameraV1OpenGLActivity extends AppCompatActivity{

    private CameraGLSurfaceView mGLSurfaceView;
    private int mCameraId;
    private CameraV1 mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mGLSurfaceView = new CameraGLSurfaceView(this);
        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        DisplayMetrics metrics = new DisplayMetrics();
        mCamera = new CameraV1(this);
        if (!mCamera.openCamera(metrics.widthPixels, metrics.heightPixels,mCameraId)){
            return;
        }
        mGLSurfaceView.init(mCamera, false,this);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGLSurfaceView != null){
            mGLSurfaceView.onPause();
            mGLSurfaceView.destory();
            mGLSurfaceView = null;
        }
        if (mCamera != null){
            mCamera.stopPreivew();
            mCamera.releaseCamera();
            mCamera = null;
        }
    }
}
