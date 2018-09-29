package com.example.templechen.cameraopengldemo.mediaRecorder;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.example.templechen.cameraopengldemo.camera1.CameraV1;

import java.io.File;
import java.io.IOException;

public class MediaRecorderWrap {

    private MediaRecorder mediaRecorder;
    private CameraV1 mCamera;
    public boolean isRecording;
    File videoFile;
    private Context mContext;

    public MediaRecorderWrap(CameraV1 camera, Context context){
        this.mCamera = camera;
        this.mContext = context;
    }

    public boolean prepareVideoRecorder(){

        mediaRecorder = new MediaRecorder();
        mCamera.getmCamera().unlock();
        mediaRecorder.setCamera(mCamera.getmCamera());
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setProfile();
        videoFile = getOutputMediaFile();
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setOutputFile(videoFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public void startRecord(){
        if (!isRecording){
            isRecording = true;
            mediaRecorder.start();
            MediaScannerConnection.scanFile (mContext, new String[] {videoFile.toString()}, null, null);
        }
    }

    public void releaseMediaRecorder(){
        if (mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (mCamera != null){
            mCamera.getmCamera().lock();
        }
    }

    private static File getOutputMediaFile(){
        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4");
        if (mediaFile.exists()){
            mediaFile.delete();
        }
        return mediaFile;
    }

}
