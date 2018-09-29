package com.example.templechen.cameraopengldemo.camera1;

import android.content.Context;

import com.example.templechen.cameraopengldemo.base.BaseCamera;
import com.example.templechen.cameraopengldemo.base.BaseCameraGLSurfaceView;

public class CameraV1GLSurfaceView extends BaseCameraGLSurfaceView {

    private CameraV1Renderer mRenderer;

    public CameraV1GLSurfaceView(Context context) {
        super(context);
    }

    public void init(BaseCamera baseCamera, boolean isPreviewStarted, Context context){
        setEGLContextClientVersion(2);
        mRenderer = new CameraV1Renderer();
        mRenderer.init(baseCamera, this, isPreviewStarted, context);
        setRenderer(mRenderer);
    }

    public void destroy(){
        if (mRenderer != null){
            mRenderer.destroy();
            mRenderer = null;
        }
    }
}
