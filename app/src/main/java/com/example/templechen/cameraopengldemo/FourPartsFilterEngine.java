package com.example.templechen.cameraopengldemo;

import android.content.Context;
import static android.opengl.GLES20.*;

public class FourPartsFilterEngine extends BaseFilterEngine {

    public FourPartsFilterEngine(Context context, int OESTextureId) {
        super(context, OESTextureId);
        vertexShader = Utils.loadShader(GL_VERTEX_SHADER, Utils.readShaderFromResource(context,R.raw.four_part_vertex_shader));
        fragmentShader = Utils.loadShader(GL_FRAGMENT_SHADER, Utils.readShaderFromResource(context,R.raw.four_part_fragment_shader));
        mProgram = Utils.createProgram(vertexShader, fragmentShader);
    }

    @Override
    public void drawFrame() {
        super.drawFrame();
    }
}
