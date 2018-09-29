package com.example.templechen.cameraopengldemo.camera2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.RequiresApi;


@RequiresApi(21)
public class CameraV2GLSurfaceView extends GLSurfaceView {

    private CameraV2Renderer mRenderer;

    public CameraV2GLSurfaceView(Context context) {
        super(context);
    }

    public void init(CameraV2 cameraV2, boolean isPreviewStarted, Context context){
        setEGLContextClientVersion(2);
        mRenderer = new CameraV2Renderer();
        mRenderer.init(cameraV2, this, isPreviewStarted, context);
        setRenderer(mRenderer);
    }

    public void destroy(){
        if (mRenderer != null){
            mRenderer.destroy();
            mRenderer = null;
        }
    }
}
