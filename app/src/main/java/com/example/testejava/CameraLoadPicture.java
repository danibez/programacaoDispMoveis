package com.example.testejava;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ImageCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public class CameraLoadPicture extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private ImageView imageView;

    ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Uri image_uri = result.getData().getData();
                        imageView.setImageURI(image_uri);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_load_picture);

        Button cameraAct = (Button) findViewById(R.id.cameraAct);
        Button camera = (Button) findViewById(R.id.camera);
        Button galeria = (Button) findViewById(R.id.galeria);
        imageView = (ImageView) findViewById(R.id.avatar);

        galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryActivityResultLauncher.launch(galleryIntent);
            }
        });

        Intent intent = new Intent(this, CameraPreview.class);
        cameraAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
    }

    Uri image_uri;
    //TODO opens camera so that user can capture image
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        cameraActivityResultLauncher.launch(cameraIntent);
    }

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bitmap inputImage = uriToBitmap(image_uri);
//                        Bitmap rotated = rotateBitmap(inputImage);
                        imageView.setImageBitmap(inputImage);

                    }
                }
            });

    private Bitmap uriToBitmap(Uri selectedFileUri) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedFileUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
            parcelFileDescriptor.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }

//    //TODO rotate image if image captured on samsung devices
//    //TODO Most phone cameras are landscape, meaning if you take the photo in portrait, the resulting photos will be rotated 90 degrees.
//    @SuppressLint("Range")
//    public Bitmap rotateBitmap(Bitmap input){
//        String[] orientationColumn = {MediaStore.Images.Media.ORIENTATION};
//        Cursor cur = getContentResolver().query(image_uri, orientationColumn, null, null, null);
//        int orientation = -1;
//        if (cur != null && cur.moveToFirst()) {
//            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]));
//        }
//        Log.d("tryOrientation",orientation+"");
//        Matrix rotationMatrix = new Matrix();
//        rotationMatrix.setRotate(orientation);
//        Bitmap cropped = Bitmap.createBitmap(input,0,0, input.getWidth(), input.getHeight(), rotationMatrix, true);
//        return cropped;
//    }
//}

//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                bindPreview(cameraProvider);
//            } catch (ExecutionException | InterruptedException e) {
//                // No errors need to be handled for this Future.
//                // This should never be reached.
//            }
//        }, ContextCompat.getMainExecutor(this));
//
//        bConf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ContentValues contentValues = new ContentValues();
//                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "NEW_IMAGE");
//                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
//
//                ImageCapture.OutputFileOptions outputFileOptions =
//                        new ImageCapture.OutputFileOptions.Builder(getContentResolver(),
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                contentValues).build();
//                cameraExecutor = Executors.newSingleThreadExecutor();
//                imageCapture.takePicture(outputFileOptions, cameraExecutor,
//                        new ImageCapture.OnImageSavedCallback() {
//                            @Override
//                            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
//                                //
//                            }
//                            @Override
//                            public void onError(ImageCaptureException error) {
//                                //
//                            }
//                        }
//                );
//            }
//        });
//
//    }
//
//    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//        Preview preview = new Preview.Builder()
//                .build();
//
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
//                .build();
//
//        imageCapture = new ImageCapture.Builder()
//                .setTargetRotation(previewView.getDisplay().getRotation())
//                .build();
//
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//        // viewFinder is a PreviewView instance
//        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
//
//        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageCapture, preview);
////        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
//    }
}