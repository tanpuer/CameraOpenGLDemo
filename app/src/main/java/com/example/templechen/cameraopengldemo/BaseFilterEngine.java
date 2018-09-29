package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.opengl.GLES11Ext;

import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

public class BaseFilterEngine {
    protected static final String aPosition = "aPosition";
    protected static final String uTextureMatrix = "uTextureMatrix";
    protected static final String aTextureCoordinate = "aTextureCoordinate";
    protected static final String uTextureSampler = "uTextureSampler";

    protected Context mContext;
    protected int mOESTextureId;
    protected FloatBuffer floatBuffer;
    protected int vertexShader = -1;
    protected int fragmentShader = -1;
    protected int mProgram;
    protected float[] transformMatrix = new float[16];

    protected int aPositionLocation = -1;
    protected int uTextureMatrixLocation = -1;
    protected int aTextureCoordinateLocation = -1;
    protected int uTextureSamplerLocation = -1;

    public BaseFilterEngine(Context context, int OESTextureId){
        mContext = context;
        mOESTextureId = OESTextureId;
        floatBuffer = Utils.createBuffer(Utils.vertexData);
    }

    public void drawFrame(){
        aPositionLocation = glGetAttribLocation(mProgram, aPosition);
        uTextureMatrixLocation = glGetUniformLocation(mProgram, uTextureMatrix);
        aTextureCoordinateLocation = glGetAttribLocation(mProgram, aTextureCoordinate);
        uTextureSamplerLocation = glGetUniformLocation(mProgram, uTextureSampler);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId);

        glUniform1i(uTextureSamplerLocation, 0);
        glUniformMatrix4fv(uTextureMatrixLocation, 1, false,transformMatrix, 0);

        if (floatBuffer != null){
            floatBuffer.position(0);
            glEnableVertexAttribArray(aPositionLocation);
            glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 16, floatBuffer);
            floatBuffer.position(2);
            glEnableVertexAttribArray(aTextureCoordinateLocation);
            glVertexAttribPointer(aTextureCoordinateLocation, 2,GL_FLOAT, false, 16, floatBuffer);
        }
        glDrawArrays(GL_TRIANGLES, 0, 6);
//        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setTransformMatrix(float[] transformMatrix) {
        this.transformMatrix = transformMatrix;
    }
}
