package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class CameraV1Renderer implements GLSurfaceView.Renderer{

    private static final String TAG = "CameraV1Renderer";

    private int mOESTextureId = -1;
    private SurfaceTexture mSurfaceTexture;
    private CameraV1 mCamera;
    private CameraGLSurfaceView mGLSurfaceView;
    private boolean bIsPreviewStarted;
    private Context mContext;
    private FilterEngine mFilterEngine;
    private FloatBuffer mDataBuffer;
    private int mProgram;
    private int[] mFBOIds = new int[1];
    private float[] transformMatrix = new float[16];

    int aPositionLocation = -1;
    int uTextureMatrixLocation = -1;
    int aTextureCoordinateLocation = -1;
    int uTextureSamplerLocation = -1;

    public void init(CameraV1 cameraV1, CameraGLSurfaceView cameraGLSurfaceView, boolean isPreviewStarted, Context context){
        mCamera = cameraV1;
        mGLSurfaceView = cameraGLSurfaceView;
        bIsPreviewStarted = isPreviewStarted;
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        mOESTextureId = Utils.createOESTextureObject();
        mFilterEngine = new FilterEngine(mContext, mOESTextureId);
        mDataBuffer = mFilterEngine.getFloatBuffer();
        mProgram = mFilterEngine.getmProgram();
        glGenFramebuffers(1, mFBOIds, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, mFBOIds[0]);
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
        }

        glClearColor(0f,0f,0f,0f);

        aPositionLocation = glGetAttribLocation(mProgram, FilterEngine.aPosition);
        uTextureMatrixLocation = glGetUniformLocation(mProgram, FilterEngine.uTextureMatrix);
        aTextureCoordinateLocation = glGetAttribLocation(mProgram, FilterEngine.aTextureCoordinate);
        uTextureSamplerLocation = glGetUniformLocation(mProgram, FilterEngine.uTextureSampler);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId);

        glUniform1i(uTextureSamplerLocation, 0);
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false,transformMatrix, 0);

        if (mDataBuffer != null){
            mDataBuffer.position(0);
            glEnableVertexAttribArray(aPositionLocation);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, mDataBuffer);
            mDataBuffer.position(2);
            glEnableVertexAttribArray(aTextureCoordinateLocation);
            glVertexAttribPointer(aTextureCoordinateLocation, 2,GL_FLOAT, false, 16, mDataBuffer);
        }

        glDrawArrays(GL_TRIANGLES, 0, 6);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
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
        mCamera.setPreviewTexture(mSurfaceTexture);
        mCamera.startPreview();
        return true;
    }

    public void destroy(){
        mFilterEngine = null;
        mDataBuffer = null;
        if (mSurfaceTexture != null){
            mSurfaceTexture.release();
            mSurfaceTexture = null;
        }
        mCamera = null;
        mOESTextureId = -1;
        bIsPreviewStarted = false;
    }
}
