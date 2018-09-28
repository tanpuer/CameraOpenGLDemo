package com.example.templechen.cameraopengldemo;

import android.content.Context;
import static android.opengl.GLES20.*;

public class ScanningLineFilterEngine extends BaseFilterEngine {

    private static final String uScanningLineY = "uScanningLineY";

    private int uScanningLineYLocation = -1;
    private float scanningLineY = 0.0f;

    public ScanningLineFilterEngine(Context context, int OESTextureId) {
        super(context, OESTextureId);
        vertexShader = Utils.loadShader(GL_VERTEX_SHADER, Utils.readShaderFromResource(context,R.raw.scanning_line_vertex_shader));
        fragmentShader = Utils.loadShader(GL_FRAGMENT_SHADER, Utils.readShaderFromResource(context,R.raw.scanning_line_fragment_shader));
        mProgram = Utils.createProgram(vertexShader, fragmentShader);
    }

    @Override
    public void drawFrame() {
        if (scanningLineY > 1.0f){
            scanningLineY = 0.0f;
        }
        scanningLineY += 0.005f;
        uScanningLineYLocation = glGetUniformLocation(mProgram, uScanningLineY);
        glUniform1f(uScanningLineYLocation, scanningLineY);
        super.drawFrame();
    }
}
