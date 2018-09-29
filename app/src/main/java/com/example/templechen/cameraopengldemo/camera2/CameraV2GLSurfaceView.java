package com.example.templechen.cameraopengldemo.camera2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.support.annotation.RequiresApi;

import com.example.templechen.cameraopengldemo.Base.BaseCamera;
import com.example.templechen.cameraopengldemo.Base.BaseCameraGLSurfaceView;


@RequiresApi(21)
public class CameraV2GLSurfaceView extends BaseCameraGLSurfaceView {

    private CameraV2Renderer mRenderer;

    public CameraV2GLSurfaceView(Context context) {
        super(context);
    }

    public void init(BaseCamera baseCamera, boolean isPreviewStarted, Context context){
        setEGLContextClientVersion(2);
        mRenderer = new CameraV2Renderer();
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
