package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;

public class CameraGLSurfaceView extends GLSurfaceView {

    private CameraV1Renderer mRenderer;

    public CameraGLSurfaceView(Context context) {
        super(context);
    }

    public void init(CameraV1 cameraV1, boolean isPreviewStarted, Context context){
        setEGLContextClientVersion(2);
        mRenderer = new CameraV1Renderer();
        mRenderer.init(cameraV1, this, isPreviewStarted, context);
        setRenderer(mRenderer);
    }

    public void destory(){
        if (mRenderer != null){
            mRenderer.destroy();
            mRenderer = null;
        }
    }

}
