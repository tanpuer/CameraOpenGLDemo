package com.example.templechen.cameraopengldemo.base;

import android.content.Context;
import android.opengl.GLSurfaceView;

public abstract class BaseCameraGLSurfaceView extends GLSurfaceView {

    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public BaseCameraGLSurfaceView(Context context) {
        super(context);
    }

    public abstract void init(BaseCamera baseCamera, boolean isPreviewStarted, Context context);

    public abstract void destroy();

    public void setAspectRatio(int width, int height){
        if (width <0 || height <0){
            throw new RuntimeException("width or height can not be negative");
        }
        this.mRatioWidth = width;
        this.mRatioHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mRatioHeight ==0 || mRatioWidth ==0){
            setMeasuredDimension(width, height);
        }else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }
    }
}
