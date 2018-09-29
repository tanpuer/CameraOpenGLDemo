package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

public class CameraV1GLSurfaceView extends GLSurfaceView {

    private CameraV1Renderer mRenderer;

    public CameraV1GLSurfaceView(Context context) {
        super(context);
    }

    public void init(CameraV1 cameraV1, boolean isPreviewStarted, Context context){
        setEGLContextClientVersion(2);
        mRenderer = new CameraV1Renderer();
        mRenderer.init(cameraV1, this, isPreviewStarted, context);
        setRenderer(mRenderer);
    }

    public void destroy(){
        if (mRenderer != null){
            mRenderer.destroy();
            mRenderer = null;
        }
    }
}
