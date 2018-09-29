package com.example.templechen.cameraopengldemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.templechen.cameraopengldemo.camera2.CameraV2OpenGLActivity;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private static final int REQUEST_CAMERA_CODE = 10001;

    private Button startFrontCameraBtn;
    private Button startBackCameraBtn;
    private Button startFrontCameraBtnV2;
    private Button startBackCameraBtnV2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startFrontCameraBtn = findViewById(R.id.front_camera_btn);
        startBackCameraBtn = findViewById(R.id.back_camera_btn);
        startFrontCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivty(false);
            }
        });
        startBackCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraActivty(true);
            }
        });

        startFrontCameraBtnV2 = findViewById(R.id.front_camera_btn_v2);
        startBackCameraBtnV2 = findViewById(R.id.back_camera_btn_v2);
        startFrontCameraBtnV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraV2Activity(false);
            }
        });
        startBackCameraBtnV2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraV2Activity(true);
            }
        });

        checkCameraPermission();
    }

    private void checkCameraPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_CODE);
        }else {
//            startCameraActivty();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length >0 && grantResults.length >0){
            switch (requestCode){
                case REQUEST_CAMERA_CODE:{
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                        startCameraActivty();
                    }else {
                        Toast.makeText(this, "必须要相机和存储权限", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void startCameraActivty(boolean openBackCamera){
        Intent intent = new Intent(this, CameraV1OpenGLActivity.class);
        intent.putExtra("openBackCamera",openBackCamera);
        startActivity(intent);
    }

    private void startCameraV2Activity(boolean openBackCamera){
        Intent intent = new Intent(this, CameraV2OpenGLActivity.class);
        intent.putExtra("openBackCamera",openBackCamera);
        startActivity(intent);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
