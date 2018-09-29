package com.example.templechen.cameraopengldemo.base;

import android.content.Context;
import android.opengl.GLSurfaceView;

public abstract class BaseCameraGLSurfaceView extends GLSurfaceView {

    public BaseCameraGLSurfaceView(Context context) {
        super(context);
    }

    public abstract void init(BaseCamera baseCamera, boolean isPreviewStarted, Context context);

    public abstract void destroy();
}
