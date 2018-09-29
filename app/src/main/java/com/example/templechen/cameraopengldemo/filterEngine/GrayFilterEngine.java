package com.example.templechen.cameraopengldemo.filterEngine;

import android.content.Context;

import com.example.templechen.cameraopengldemo.R;
import com.example.templechen.cameraopengldemo.utils.GLUtils;

import static android.opengl.GLES20.*;

public class GrayFilterEngine extends BaseFilterEngine{

    public GrayFilterEngine(Context context, int OESTextureId){
        super(context, OESTextureId);
        vertexShader = GLUtils.loadShader(GL_VERTEX_SHADER, GLUtils.readShaderFromResource(context,R.raw.gray_vertex_shader));
        fragmentShader = GLUtils.loadShader(GL_FRAGMENT_SHADER, GLUtils.readShaderFromResource(context,R.raw.gray_fragment_shader));
        mProgram = GLUtils.createProgram(vertexShader, fragmentShader);
    }

    @Override
    public void drawFrame() {
        super.drawFrame();
    }
}
