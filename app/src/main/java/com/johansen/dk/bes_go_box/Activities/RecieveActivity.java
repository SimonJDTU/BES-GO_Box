package com.johansen.dk.bes_go_box.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.johansen.dk.bes_go_box.R;

import java.io.File;
import java.io.IOException;

public class RecieveActivity extends AppCompatActivity {
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getQrCodeImage();
    }

    private void getQrCodeImage(){
        File localFile = null;
        try {
            localFile = File.createTempFile("box1", "bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference ref = mStorageRef.child("box1.png");
        ref.getBytes(1024*1024)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Successfully downloaded data to local file
                        // ...
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView iv = findViewById(R.id.qrcode);
                        iv.setImageBitmap(bitmap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });
    }

    private void updateImage(File image){

    }
}
