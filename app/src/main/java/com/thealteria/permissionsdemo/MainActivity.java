package com.thealteria.permissionsdemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_TAKE_PHOTO = 1;

    private ImageView imageView;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Button bCapture = findViewById(R.id.bCapture);

        setPermission();

//        String folder_main = "lolol";
//
//        File folder = new File(Environment.getExternalStorageDirectory() +
//                File.separator + folder_main);
//        boolean success = true;
//        if (!folder.exists()) {
//            success = folder.mkdirs();
//        }
//        if (success) {
//            Log.i(TAG, "onCreate: created");
//        } else {
//            Log.i(TAG, "onCreate: can't created");
//        }
    }

    private void setPermission() {
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Log.i(TAG, "onPermissionGranted: granted");
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
//            setPermission();
            Toast.makeText(MainActivity.this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }
    };

    public void bCapture(View view) {
        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePic.resolveActivity(getPackageManager()) != null) {
            File photoFile;

            photoFile = createPhoto();

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.thealteria.permissionsdemo", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                imageView.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "Saved bc", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File createPhoto() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(name, ".jpg", storageDirectory);
        } catch (IOException e) {
            Log.i(TAG, "createPhoto: " + e.getMessage());
        }
        if (image != null) {
            path = image.getPath();
        }
        return image;
    }
}
