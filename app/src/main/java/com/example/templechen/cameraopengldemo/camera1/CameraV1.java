package com.example.templechen.cameraopengldemo.camera1;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Surface;

import com.example.templechen.cameraopengldemo.Base.BaseCamera;
import com.example.templechen.cameraopengldemo.mediaCodec.CameraMediaCodec;

import java.io.IOException;
import java.util.List;

public class CameraV1 extends BaseCamera {

    private static final String TAG = "CameraV1";
    private static final int BACK_CAMERA_WIDTH = 1920;
    private static final int BACK_CAMERA_HEIGHT = 1080;
    private static final int FRONT_CAMERA_WIDTH = 1920;
    private static final int FRONT_CAMERA_HEIGHT = 1080;

    private Activity mActivity;
    private Camera mCamera;
    private CameraMediaCodec mediaCodec;

    public CameraV1(Activity activity){
        mActivity = activity;
    }

    public boolean openCamera(int width, int height, int cameraId){
        try{
            boolean openBackCamera = cameraId == CameraInfo.CAMERA_FACING_BACK? true:false;
            mCamera = Camera.open(cameraId);
            Parameters parameters = mCamera.getParameters();
            parameters.set("orientation", "portrait");
            if (openBackCamera){
                parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            List<Size> previewSizes = parameters.getSupportedPreviewSizes();
            if (previewSizes.size() <1){
                return false;
            }
            int previewWidth = previewSizes.get(0).width;
            int previewHeight = previewSizes.get(0).height;
            int estimatedWidth = openBackCamera?BACK_CAMERA_WIDTH:FRONT_CAMERA_WIDTH;
            int estimatedHeight = openBackCamera?BACK_CAMERA_HEIGHT:FRONT_CAMERA_HEIGHT;
            for (Size size: previewSizes){
                Log.d(TAG, "openCamera: " + size.width);
                Log.d(TAG, "openCamera: " + size.height);
                if (size.width == estimatedWidth && size.height == estimatedHeight){
                    previewWidth = estimatedWidth;
                    previewHeight = estimatedHeight;
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

            //mediacodec
//            mediaCodec = new CameraMediaCodec();
//            mediaCodec.init(previewWidth, previewHeight);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void setSurfaceTexture(SurfaceTexture mSurfaceTexture) {
        if (mCamera != null){
            try {
                //必须在startPreview方法之前设置
                mCamera.setPreviewTexture(mSurfaceTexture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startPreview(){
        if (mCamera != null){
            mCamera.startPreview();

//            mediaCodec.startCodec();

            setPreviewCallback();
        }
    }

    public void stopPreview(){
        if (mCamera != null){
            mCamera.stopPreview();
        }
    }

    public void setPreviewCallback(){
        if (mCamera != null){
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    //todo MediaCodec and MediaMuxer pratice
//                    mediaCodec.encodeFrame(data);
                }
            });
        }
    }

    public void releaseCamera(){
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
        if (mediaCodec != null){
//            mediaCodec.pauseCodec();
//            mediaCodec.release();
        }
    }

    public Camera getmCamera() {
        return mCamera;
    }
}
