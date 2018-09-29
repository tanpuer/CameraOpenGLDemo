package com.example.templechen.cameraopengldemo.mediaCodec;

public interface IMediaCodec {

    void init(int width, int height);

    void startCodec();

    void encodeFrame(byte[] data);

    void pauseCodec();

    void release();
}
