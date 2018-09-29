package com.example.templechen.cameraopengldemo.camera2;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.templechen.cameraopengldemo.base.BaseCamera;

@RequiresApi(21)
public class CameraV2OpenGLActivity extends Activity {

    private CameraV2GLSurfaceView mGLSurfaceView;
    private BaseCamera mCamera;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLSurfaceView = new CameraV2GLSurfaceView(this);
        mCamera = new CameraV2(this);
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
