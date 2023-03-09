package com.example.nativapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.nativapps.databinding.ActivityCameraBinding;
import com.example.nativapps.viewmodel.CameraViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraActivity extends AppCompatActivity {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final String[] CAMERA_PERMISSION = new String[]{android.Manifest.permission.CAMERA};
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String PHOTO_EXTENSION = ".jpg";

    private ActivityCameraBinding binding;
    private CameraViewModel cameraViewModel;
    @Nullable
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        if (hasCameraPermission()) {
            startCamera();
        } else {
            requestPermission();
        }
        binding.imageCaptureButton.setOnClickListener(v -> takePhoto());
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                // Preview
                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
                // ImageCapture
                imageCapture = new ImageCapture.Builder().build();
                // Select back camera as a default
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll();
                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
                } catch(Exception exc) {
                    Log.e(TAG, "Use case binding failed", exc);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (this.imageCapture == null) { return; }
        // Get a stable reference of the modifiable image capture use case
        ImageCapture imageCapture = this.imageCapture;
        Optional<File> outputDirectory = Arrays.stream(this.getExternalMediaDirs()).findFirst();
        outputDirectory.ifPresent(File::mkdirs);
        if (outputDirectory.isPresent()) {
            File file = createFile(outputDirectory.get());
            // Create output options object which contains file + metadata
            ImageCapture.OutputFileOptions outputFileOptions = (
                new ImageCapture.OutputFileOptions.Builder(file).build()
            );
            // Set up image capture listener, which is triggered after photo has been taken
            Executor cameraExecutor = ContextCompat.getMainExecutor(getBaseContext());
            imageCapture.takePicture(outputFileOptions, cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                        String msg = "Captura de foto obtenida: " + output.getSavedUri();
                        Log.d(TAG, msg);
                        toast(msg);
                        cameraViewModel.setImage(file);
                        finish();
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException error) {
                        String msg = "Photo capture failed: " + error.getMessage();
                        Log.e(TAG, msg, error);
                        toast(msg);
                    }
                }
            );
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }

    private File createFile(File baseFolder) {
        SimpleDateFormat sdf = new SimpleDateFormat(FILENAME_FORMAT, Locale.US);
        return new File(
                baseFolder,
                sdf.format(System.currentTimeMillis()) + PHOTO_EXTENSION
        );
    }

    private void toast(CharSequence message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }
}
