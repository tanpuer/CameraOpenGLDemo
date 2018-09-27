package com.example.templechen.cameraopengldemo;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

public class FilterEngine {

    private static final float[] vertexData = {
        1f,1f, 1f,1f,
        -1f,1f,0f,1f,
        -1f,-1f,0f,0f,
        1f,1f,1f,1f,
        -1f,-1f,0f,0f,
        1f,-1f,1f,0f
    };

    public static final String aPosition = "aPosition";
    public static final String uTextureMatrix = "uTextureMatrix";
    public static final String aTextureCoordinate = "aTextureCoordinate";
    public static final String uTextureSampler = "uTextureSampler";

    private Context mContext;
    private int mOESTextureId;
    private FloatBuffer floatBuffer;
    private int vertexShader = -1;
    private int fragmentShader = -1;
    private int mProgram;

    public FilterEngine(Context context, int OESTextureId){
        mContext = context;
        mOESTextureId = OESTextureId;
        floatBuffer = createBuffer(vertexData);
        vertexShader = loadShader(GL_VERTEX_SHADER, Utils.readShaderFromResource(context,R.raw.base_vertex_shader));
        fragmentShader = loadShader(GL_FRAGMENT_SHADER, Utils.readShaderFromResource(context,R.raw.base_fragment_shader));
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

    public FloatBuffer getFloatBuffer() {
        return floatBuffer;
    }

    public int getmProgram() {
        return mProgram;
    }
}
