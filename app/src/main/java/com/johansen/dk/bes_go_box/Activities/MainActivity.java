package com.johansen.dk.bes_go_box.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.johansen.dk.bes_go_box.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.regex.Pattern;

import static android.os.Build.VERSION_CODES.LOLLIPOP;

public class MainActivity extends AppCompatActivity {

    private SurfaceView cameraPreview;
    private CameraSource cameraSrc;
    private BarcodeDetector barcodeDetector;
    private TextView loginInfo;
    private Vibrator vibe;

    private long scanTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInfo = findViewById(R.id.informationText);

        cameraPreview = findViewById(R.id.cameraPreview);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSrc = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setAutoFocusEnabled(true)
                .build();

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }



    private void createQRscan() {
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if(!(Build.VERSION.SDK_INT <= LOLLIPOP)) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        askPermission();
                        return;
                    }
                }
                try {
                    cameraSrc.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSrc.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if (qrCodes.size() != 0) {
                    loginInfo.post(() -> {
                        if (timeAllowedBetweenScans()) {
                            scanTime = System.currentTimeMillis();
                            if (true/*qrRegex(qrCodes.valueAt(0).displayValue)*/) {

                                vibe.vibrate(200);
                                //TODO: Refractor activityname "nextActivity" to something more appropriate
                                Intent intent = new Intent(MainActivity.this, NextActivity.class);
                                intent.putExtra("QRvalue", qrCodes.valueAt(0).displayValue);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),"Couldn't recognize QR-code, try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    private boolean timeAllowedBetweenScans() {
        long currentTime = System.currentTimeMillis();
        return currentTime - scanTime > 3000;
    }


    // Must be done during an initialization phase like onCreate
    private void askPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        recreate();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()) {
                            //TODO: Handle denial efficiently
                        } else{

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    private boolean qrRegex(String codeString) {

        // 6-14 character password requiring numbers and alphabets of both cases
        final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{16}$";

        /* 4-32 character password requiring at least 3 out of 4 (uppercase
        // and lowercase letters, numbers & special characters) and at-most
        // 2 equal consecutive chars.
        final String COMPLEX_PASSWORD_REGEX =
                "^(?:(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])|" +
                        "(?=.*\\d)(?=.*[^A-Za-z0-9])(?=.*[a-z])|" +
                        "(?=.*[^A-Za-z0-9])(?=.*[A-Z])(?=.*[a-z])|" +
                        "(?=.*\\d)(?=.*[A-Z])(?=.*[^A-Za-z0-9]))(?!.*(.)\\1{2,})" +
                        "[A-Za-z0-9!~<>,;:_=?*+#.\"&§%°()\\|\\[\\]\\-\\$\\^\\@\\/]" +
                        "{8,32}$";
        */

        final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

        return PASSWORD_PATTERN.matcher(codeString).matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        createQRscan();
    }
}
