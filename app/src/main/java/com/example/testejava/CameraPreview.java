package com.example.testejava;

import android.content.ContentValues;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraPreview extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_preview);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        previewView = (PreviewView)findViewById(R.id.previewView);
        Button bFoto = (Button) findViewById(R.id.cameraAct);



        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));

        bFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "NEW_IMAGE");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");

                ImageCapture.OutputFileOptions outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(getContentResolver(),
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                contentValues).build();
                cameraExecutor = Executors.newSingleThreadExecutor();
                imageCapture.takePicture(outputFileOptions, cameraExecutor,
                        new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                                //
                            }
                            @Override
                            public void onError(ImageCaptureException error) {
                                //
                            }
                        }
                );
            }
        });

    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        imageCapture = new ImageCapture.Builder()
                .setTargetRotation(previewView.getDisplay().getRotation())
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        // viewFinder is a PreviewView instance
        previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, imageCapture, preview);
//        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }
}