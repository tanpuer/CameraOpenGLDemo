package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.opengl.GLES11Ext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

public class GrayFilterEngine {

    private static final float[] vertexData = {
        1f,1f, 1f,1f,
        -1f,1f,0f,1f,
        -1f,-1f,0f,0f,
        1f,1f,1f,1f,
        -1f,-1f,0f,0f,
        1f,-1f,1f,0f
    };

    private static final String aPosition = "aPosition";
    private static final String uTextureMatrix = "uTextureMatrix";
    private static final String aTextureCoordinate = "aTextureCoordinate";
    private static final String uTextureSampler = "uTextureSampler";

    private Context mContext;
    private int mOESTextureId;
    private FloatBuffer floatBuffer;
    private int vertexShader = -1;
    private int fragmentShader = -1;
    private int mProgram;
    private float[] transformMatrix = new float[16];

    private int aPositionLocation = -1;
    private int uTextureMatrixLocation = -1;
    private int aTextureCoordinateLocation = -1;
    private int uTextureSamplerLocation = -1;

    public GrayFilterEngine(Context context, int OESTextureId){
        mContext = context;
        mOESTextureId = OESTextureId;
        floatBuffer = createBuffer(vertexData);
        vertexShader = loadShader(GL_VERTEX_SHADER, Utils.readShaderFromResource(context,R.raw.gray_vertex_shader));
        fragmentShader = loadShader(GL_FRAGMENT_SHADER, Utils.readShaderFromResource(context,R.raw.gray_fragment_shader));
        mProgram = createProgram(vertexShader, fragmentShader);
    }

    public FloatBuffer createBuffer(float[] vertexData){
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                                            .order(ByteOrder.nativeOrder())
                                            .asFloatBuffer();
        floatBuffer.put(vertexData,0, vertexData.length).position(0);
        return floatBuffer;
    }

    public int loadShader(int type, String shaderSource){
        int shader = glCreateShader(type);
        if (shader == 0){
            throw new RuntimeException("create shader failed, type: " + type);
        }
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        return shader;
    }

    public int createProgram(int vertexShader, int fragmentShader){
        mProgram = glCreateProgram();
        if (mProgram == 0){
            throw new RuntimeException("create Program failed");
        }
        glAttachShader(mProgram, vertexShader);
        glAttachShader(mProgram, fragmentShader);
        glLinkProgram(mProgram);
        if (glGetError() != GL_NO_ERROR){
            throw new RuntimeException("link Program failed");
        }
        glUseProgram(mProgram);
        return mProgram;
    }

    public void drawFrame(){
        aPositionLocation = glGetAttribLocation(mProgram, GrayFilterEngine.aPosition);
        uTextureMatrixLocation = glGetUniformLocation(mProgram, GrayFilterEngine.uTextureMatrix);
        aTextureCoordinateLocation = glGetAttribLocation(mProgram, GrayFilterEngine.aTextureCoordinate);
        uTextureSamplerLocation = glGetUniformLocation(mProgram, GrayFilterEngine.uTextureSampler);

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
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void setTransformMatrix(float[] transformMatrix) {
        this.transformMatrix = transformMatrix;
    }
}
