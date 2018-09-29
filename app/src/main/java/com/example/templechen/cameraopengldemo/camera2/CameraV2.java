package com.example.templechen.cameraopengldemo.camera2;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import com.example.templechen.cameraopengldemo.base.BaseCamera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiresApi(21)
public class CameraV2 extends BaseCamera {

    private static final String TAG = "CameraV2";
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    private static final int CAMERA_WIDTH = 1920;
    private static final int CAMERA_HEIGHT = 1080;
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    private String cameraId = null;
    private CameraManager cameraManager;
    private Activity mActivity;
    private CameraDevice cameraDevice;
    private Size mPreviewSize;
    private HandlerThread cameraThread;
    private Handler cameraHandler;
    private SurfaceTexture mSurfaceTexture;
    private CaptureRequest.Builder mCaptureRequestBuilder;
    private CaptureRequest mCaptureRequest;
    private CameraCaptureSession mSession;

    public CameraV2(Activity activity){
        this.mActivity = activity;
        setCameraThread();
    }

    private void setCameraThread() {
        cameraThread = new HandlerThread("Camera Thread");
        cameraThread.start();
        cameraHandler = new Handler(cameraThread.getLooper());
    }

    public boolean openCamera(int width, int height, int cameraFacing){
        if (width ==0 || height == 0){
            width = CAMERA_WIDTH;
            height = CAMERA_HEIGHT;
        }
        cameraManager = (CameraManager)mActivity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraID : cameraManager.getCameraIdList()){
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
                int facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING).intValue();
                if (facing == cameraFacing){
                    this.cameraId = cameraID;
                    StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                    Log.d(TAG, "openCamera with cameraId :" + cameraId + ", preview-width: " + mPreviewSize.getWidth() + " height: "+ mPreviewSize.getHeight());
                    openCameraV2();
                    return true;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }

    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap){
            Log.d(TAG, "getOptimalSize: " + option.getWidth() + " * " + option.getHeight());
            if (width == option.getWidth() && height == option.getHeight()){
                return new Size(width, height);
            }
            if (width > height){
                if (option.getWidth() >= width && option.getHeight() >= height){
                    sizeList.add(option);
                }
            }else {
                if (option.getWidth() >= height && option.getHeight() >= width){
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() >0){
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    private void openCameraV2(){
        if (cameraId == null){
            return;
        }
        try {
            cameraManager.openCamera(cameraId, stateCallback, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
            Log.d(TAG, "onError: " + error);
        }
    };

    public void setSurfaceTexture(SurfaceTexture mSurfaceTexture) {
        this.mSurfaceTexture = mSurfaceTexture;
    }

    public void startPreview(){
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface surface = new Surface(mSurfaceTexture);
        try {
            mCaptureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    mSession = session;
                    mCaptureRequest = mCaptureRequestBuilder.build();
                    try {
                        mSession.setRepeatingRequest(mCaptureRequest, null, cameraHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, cameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void stopPreview(){

    }

    @Override
    public void releaseCamera() {
        if (cameraDevice != null){
            cameraDevice.close();
            cameraDevice = null;
        }
    }
}
