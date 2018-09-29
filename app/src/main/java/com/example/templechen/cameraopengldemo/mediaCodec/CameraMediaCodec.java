package com.example.templechen.cameraopengldemo.mediaCodec;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CameraMediaCodec implements IMediaCodec {

    private static final long TIMEOUT_USEC = 20;

    private static final int framerate = 30;
    private MediaCodec mediaCodec;
    private int width, height;
    private boolean initialized;
    private BufferedOutputStream outputStream;

    @Override
    @RequiresApi(21)
    public void init(int width, int height) {
//        MediaCodecList mediaCodecList = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
            MediaFormat mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2 * width *height *framerate);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
            //yuv420p
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
            mediaCodec.configure(mediaFormat,null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
            initialized =false;
        }
        initialized = true;
    }

    @Override
    @RequiresApi(16)
    public void startCodec() {
        if (!initialized){
            return;
        }
        createFile();
        mediaCodec.start();
    }

    @Override
    @RequiresApi(16)
    public void encodeFrame(byte[] data) {
        if (!initialized){
            return;
        }
        byte[] input = data;
        byte[] yuv420sp = new byte[width*height*3/2];
        NV21ToNV12(input,yuv420sp,width, height);
        if (input != null){
            ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
            ByteBuffer[] outputBuffers = mediaCodec.getOutputBuffers();
            int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
            //当输入缓冲区有效时
            if (inputBufferIndex >= 0){
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input);
                mediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, System.nanoTime()/1000, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
            while (outputBufferIndex >=0){
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                byte[] outData = new byte[bufferInfo.size];
                outputBuffer.get(outData);
                //outData就是输出的h264数据
                try {
                    outputStream.write(outData, 0, outData.length);
                    mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                    outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_USEC);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @RequiresApi(16)
    public void pauseCodec() {
        mediaCodec.stop();
    }

    @Override
    @RequiresApi(16)
    public void release() {
        mediaCodec.release();
        mediaCodec = null;
        try {
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //摄像头N21
//    Y Y Y Y
//    Y Y Y Y
//    Y Y Y Y
//    Y Y Y Y
//    V U V U
//    V U V U
    //N12
//    Y Y Y Y
//    Y Y Y Y
//    Y Y Y Y
//    Y Y Y Y
//    U V U V
//    U V U V
    private void NV21ToNV12(byte[] nv21,byte[] nv12,int width,int height){
        if(nv21 == null || nv12 == null)return;
        int framesize = width*height;
        int i = 0,j = 0;
        System.arraycopy(nv21, 0, nv12, 0, framesize);
        for(i = 0; i < framesize; i++){
            nv12[i] = nv21[i];
        }
        for (j = 0; j < framesize/2; j+=2)
        {
            nv12[framesize + j-1] = nv21[j+framesize];
        }
        for (j = 0; j < framesize/2; j+=2)
        {
            nv12[framesize + j] = nv21[j+framesize-1];
        }
    }

    private void createFile(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.h264";
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            initialized = false;
            e.printStackTrace();
        }
    }

}
