package com.example.templechen.cameraopengldemo.auto;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.templechen.cameraopengldemo.Base.BaseCamera;
import com.example.templechen.cameraopengldemo.Base.BaseCameraGLSurfaceView;
import com.example.templechen.cameraopengldemo.camera1.CameraV1;
import com.example.templechen.cameraopengldemo.camera1.CameraV1GLSurfaceView;
import com.example.templechen.cameraopengldemo.camera2.CameraV2;
import com.example.templechen.cameraopengldemo.camera2.CameraV2GLSurfaceView;

public class CameraAutoVersionOpenGLSurfaceView extends Activity {

    private BaseCameraGLSurfaceView mGLSurfaceView;
    private BaseCamera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int version = Build.VERSION.SDK_INT;
        if (version >=21) {
            mGLSurfaceView = new CameraV2GLSurfaceView(this);
            mCamera = new CameraV2(this);
        }else {
            mGLSurfaceView = new CameraV1GLSurfaceView(this);
            mCamera = new CameraV1(this);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        int cameraFacing = getIntent().getBooleanExtra("openBackCamera", true)? CameraCharacteristics.LENS_FACING_BACK: CameraCharacteristics.LENS_FACING_FRONT;
        if (!mCamera.openCamera(metrics.widthPixels, metrics.heightPixels, cameraFacing)){
            return;
        }
        mGLSurfaceView.init(mCamera, false, this);
        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGLSurfaceView != null){
            mGLSurfaceView.onPause();
            mGLSurfaceView.destroy();
        }
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera = null;
        }
    }
}
