package com.example.templechen.cameraopengldemo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.util.List;

public class CameraV1 {

    private static final String TAG = "CameraV1";

    private Activity mActivity;
    private Camera mCamera;

    public CameraV1(Activity activity){
        mActivity = activity;
    }

    public boolean openCamera(int width, int height, int cameraId){
        try{
            mCamera = Camera.open(cameraId);
            Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            List<Size> previewSizes = parameters.getSupportedPreviewSizes();
            int previewWidth = 1280;
            int previewHeight = 720;
            for (Size size: previewSizes){
                Log.d(TAG, "openCamera: " + size.width);
                Log.d(TAG, "openCamera: " + size.height);
                if (size.width == 1920 && size.height == 1080){
                    previewWidth = 1920;
                    previewHeight = 1080;
                }
            }
            parameters.setPreviewSize(previewWidth, previewHeight);

            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotation = mActivity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation){
                case Surface.ROTATION_0:{
                    degrees = 0;
                    break;
                }
                case Surface.ROTATION_90:{
                    degrees = 90;
                    break;
                }
                case Surface.ROTATION_180:{
                    degrees = 180;
                    break;
                }
                case Surface.ROTATION_270:{
                    degrees = 270;
                    break;
                }
                default:
                    break;
            }
            int result;
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT){
                //前置
                result = (info.orientation + degrees)%360;
                result = (360-result)%360;
            }else {
                //后置摄像头
                result = (info.orientation - degrees + 360)%360;
            }
            mCamera.setDisplayOrientation(result);

            mCamera.setParameters(parameters);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void startPreview(){
        if (mCamera != null){
            mCamera.startPreview();
        }
    }

    public void stopPreivew(){
        if (mCamera != null){
            mCamera.stopPreview();
        }
    }

    public void setPreviewTexture(SurfaceTexture surfaceTexture){
        if (mCamera != null){
            try {
                //必须在startPreview方法之前设置
                mCamera.setPreviewTexture(surfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }
}
