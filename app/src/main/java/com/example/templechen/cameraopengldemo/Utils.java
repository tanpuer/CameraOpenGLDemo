package com.example.templechen.cameraopengldemo;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_NO_ERROR;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glGetError;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUseProgram;

public class Utils {

    public static final float[] vertexData = {
            1f,1f, 1f,1f,
            -1f,1f,0f,1f,
            -1f,-1f,0f,0f,
            1f,1f,1f,1f,
            -1f,-1f,0f,0f,
            1f,-1f,1f,0f
    };

    public static int createOESTextureObject(){
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex,0);
        //将此纹理绑定到外部纹理上
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
        //设置纹理过滤参数
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        //解除绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    public static String readShaderFromResource(Context context, int resourceId) {
        StringBuilder builder = new StringBuilder();
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            is = context.getResources().openRawResource(resourceId);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (isr != null) {
                    isr.close();
                    isr = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }

    public static FloatBuffer createBuffer(float[] vertexData){
        FloatBuffer floatBuffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(vertexData,0, vertexData.length).position(0);
        return floatBuffer;
    }

    public static int loadShader(int type, String shaderSource){
        int shader = glCreateShader(type);
        if (shader == 0){
            throw new RuntimeException("create shader failed, type: " + type);
        }
        glShaderSource(shader, shaderSource);
        glCompileShader(shader);
        return shader;
    }

    public static int createProgram(int vertexShader, int fragmentShader){
        int mProgram = glCreateProgram();
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

}
