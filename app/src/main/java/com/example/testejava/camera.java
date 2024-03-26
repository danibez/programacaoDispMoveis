package com.example.testejava;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.SurfaceRequest;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;

import com.google.common.util.concurrent.ListenableFuture;

public class camera extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ExecutorService cameraExecutor;
    private ImageCapture imageCapture;
    ImageView imageView;
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Button button = (Button) this.findViewById(R.id.button1);
        imageView = findViewById(R.id.preview);

        startCamera();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
    }

    private void startCamera() {
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            ProcessCameraProvider cameraProvider;
            try {
                cameraProvider = cameraProviderFuture.get();
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            bindPreview(cameraProvider);
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(new Preview.SurfaceProvider() {
                                       @Override
                                       public void onSurfaceRequested(@NonNull SurfaceRequest request) {

                                       }
                                   });

        imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

        // Set up ImageAnalysis for displaying camera preview on ImageView
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy image) {
                Bitmap bitmap = imageToBitmap(image);
                if (bitmap != null) {
                    runOnUiThread(() -> imageView.setImageBitmap(bitmap));
                }
                image.close();
            }
        });

        cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis);
    }

    @OptIn(markerClass = ExperimentalGetImage.class)
    private Bitmap imageToBitmap(ImageProxy image) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(image.getImage().getPlanes()[0].getBuffer().array(),
                    0, image.getImage().getPlanes()[0].getBuffer().capacity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private File getOutputDirectory() {
        File mediaDir = new File(getExternalMediaDirs()[0], "CameraX");
        mediaDir.mkdirs();
        return mediaDir;
    }

    private void takePhoto() {
        File photoFile = new File(getOutputDirectory(), new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(System.currentTimeMillis()) + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(photoFile).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                runOnUiThread(() -> Toast.makeText(camera.this, "Photo saved successfully: " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                runOnUiThread(() -> Toast.makeText(camera.this, "Error saving photo", Toast.LENGTH_SHORT).show());
            }
        });
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

}