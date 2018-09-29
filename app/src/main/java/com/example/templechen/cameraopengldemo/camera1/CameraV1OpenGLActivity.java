package com.example.templechen.cameraopengldemo.camera1;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.example.templechen.cameraopengldemo.Base.BaseCamera;
import com.example.templechen.cameraopengldemo.mediaRecorder.MediaRecorderWrap;

public class CameraV1OpenGLActivity extends Activity{

    private static final String TAG = "CameraV1OpenGLActivity";

    private CameraV1GLSurfaceView mGLSurfaceView;
    private int mCameraId;
    private BaseCamera mCamera;
    private static MediaRecorderWrap mediaRecorderWrap;
    private MediaRecorderAsyncTask mAsyncTask;

    private Handler mainHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mGLSurfaceView = new CameraV1GLSurfaceView(this);
        mCameraId = getIntent().getBooleanExtra("openBackCamera", true)? Camera.CameraInfo.CAMERA_FACING_BACK: Camera.CameraInfo.CAMERA_FACING_FRONT;
        DisplayMetrics metrics = new DisplayMetrics();
        mCamera = new CameraV1(this);
        if (!mCamera.openCamera(metrics.widthPixels, metrics.heightPixels,mCameraId)){
            return;
        }
        mGLSurfaceView.init(mCamera, false,this);
        setContentView(mGLSurfaceView);

//        mainHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //use MediaRecorder to capture camera video
//                mediaRecorderWrap = new MediaRecorderWrap(mCamera, CameraV1OpenGLActivity.this);
//                mAsyncTask = new MediaRecorderAsyncTask();
//                mAsyncTask.execute(null, null, null);
//            }
//        },2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGLSurfaceView != null){
            mGLSurfaceView.onPause();
            mGLSurfaceView.destroy();
            mGLSurfaceView = null;
        }
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.releaseCamera();
            mCamera = null;
        }
        if (mAsyncTask != null){
//            mAsyncTask.cancel(true);
//            mAsyncTask = null;
        }
    }

    public class MediaRecorderAsyncTask extends AsyncTask<Void,Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (mediaRecorderWrap.prepareVideoRecorder()){
                mediaRecorderWrap.startRecord();
                mediaRecorderWrap.isRecording = true;
            }else {
                mediaRecorderWrap.releaseMediaRecorder();
                return false;
            }
            return true;
        }
    }

}
