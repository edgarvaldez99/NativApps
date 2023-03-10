package com.example.nativapps.fragments;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nativapps.databinding.FragmentCameraBinding;
import com.example.nativapps.viewmodel.CameraViewModel;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CameraFragment extends Fragment {

    private static final String TAG = CameraFragment.class.getSimpleName();
    private static final String[] CAMERA_PERMISSION = new String[] {
            android.Manifest.permission.CAMERA
    };
    private static final int CAMERA_REQUEST_CODE = 10;
    private static final String FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS";
    private static final String PHOTO_EXTENSION = ".jpg";

    private FragmentCameraBinding binding;
    private CameraViewModel cameraViewModel;
    @Nullable
    private ImageCapture imageCapture;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;

    public CameraFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCameraBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cameraViewModel = new ViewModelProvider(requireActivity()).get(CameraViewModel.class);
        if (hasCameraPermission()) {
            startCamera();
        } else {
            requestPermission();
            toast("Permissions not granted");
            Navigation.findNavController(view).popBackStack();
        }
        binding.imageCaptureButton.setOnClickListener(this::takePhoto);
    }

    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
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
                    cameraProvider.bindToLifecycle(
                            this, cameraSelector, preview, imageCapture
                    );
                } catch(Exception exc) {
                    Log.e(TAG, "Use case binding failed", exc);
                }
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void takePhoto(View view) {
        if (this.imageCapture == null) { return; }
        // Get a stable reference of the modifiable image capture use case
        ImageCapture imageCapture = this.imageCapture;
        File[] externalMediaDirs = requireActivity().getExternalMediaDirs();
        Optional<File> outputDirectory = Arrays.stream(externalMediaDirs).findFirst();
        outputDirectory.ifPresent(File::mkdirs);
        if (outputDirectory.isPresent()) {
            File file = createFile(outputDirectory.get());
            // Create output options object which contains file + metadata
            ImageCapture.OutputFileOptions outputFileOptions = (
                    new ImageCapture.OutputFileOptions.Builder(file).build()
            );
            // Set up image capture listener, which is triggered after photo has been taken
            Executor cameraExecutor = ContextCompat.getMainExecutor(requireContext());
            imageCapture.takePicture(outputFileOptions, cameraExecutor,
                    new ImageCapture.OnImageSavedCallback() {
                        @Override
                        public void onImageSaved(@NonNull ImageCapture.OutputFileResults output) {
                            String msg = "Captura de foto obtenida: " + output.getSavedUri();
                            Log.d(TAG, msg);
                            toast(msg);
                            cameraViewModel.setImage(file);
                            Navigation.findNavController(view).popBackStack();
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
                requireContext(),
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(
                requireActivity(),
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }
}
