package com.example.templechen.cameraopengldemo.camera2;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLSurfaceView;
import android.support.annotation.RequiresApi;

import com.example.templechen.cameraopengldemo.BaseFilterEngine;
import com.example.templechen.cameraopengldemo.ScanningLineFilterEngine;
import com.example.templechen.cameraopengldemo.Utils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

@RequiresApi(21)
public class CameraV2Renderer implements GLSurfaceView.Renderer {

    private static final String TAG = "CameraV1Renderer";

    private int mOESTextureId = -1;
    private SurfaceTexture mSurfaceTexture;
    private CameraV2 mCamera;
    private CameraV2GLSurfaceView mGLSurfaceView;
    private boolean bIsPreviewStarted;
    private Context mContext;
    private BaseFilterEngine mFilterEngine;
    private int[] mFBOIds = new int[1];
    private float[] transformMatrix = new float[16];

    public void init(CameraV2 cameraV2, CameraV2GLSurfaceView cameraGLSurfaceView, boolean isPreviewStarted, Context context){
        mCamera = cameraV2;
        mGLSurfaceView = cameraGLSurfaceView;
        bIsPreviewStarted = isPreviewStarted;
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mOESTextureId = Utils.createOESTextureObject();
        //设置不同的filter
        mFilterEngine = new ScanningLineFilterEngine(mContext, mOESTextureId);
        //todo!!!!
//        glGenFramebuffers(1, mFBOIds, 0);
//        glBindFramebuffer(GL_FRAMEBUFFER, mFBOIds[0]);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (!bIsPreviewStarted){
            bIsPreviewStarted = initSurfaceTexture();
            return;
        }
        if (mSurfaceTexture != null){
            mSurfaceTexture.updateTexImage();
            mSurfaceTexture.getTransformMatrix(transformMatrix);
            mFilterEngine.setTransformMatrix(transformMatrix);
        }

        glClearColor(0f,0f,0f,0f);

        mFilterEngine.drawFrame();
    }

    private boolean initSurfaceTexture(){
        if (mCamera == null || mGLSurfaceView == null){
            return false;
        }
        //根据外部纹理id创建surfaceTexture
        mSurfaceTexture = new SurfaceTexture(mOESTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mGLSurfaceView.requestRender();
            }
        });
        //将此surfaceTexture作为相机预览输出
        mCamera.setSurfaceTexture(mSurfaceTexture);
        mCamera.startPreview();
        return true;
    }

    public void destroy(){
        mFilterEngine = null;
        if (mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCamera = null;
        mOESTextureId = -1;
        bIsPreviewStarted = false;
    }
}
