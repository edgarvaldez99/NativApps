package com.example.nativapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.nativapps.databinding.ActivityCreateReportBinding;
import com.example.nativapps.viewmodel.CameraViewModel;
import com.example.nativapps.viewmodel.CreateReportViewModel;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateReportActivity extends AppCompatActivity {

    private ActivityCreateReportBinding binding;
    private File image = null;
    private CreateReportViewModel createReportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createReportViewModel = new ViewModelProvider(this).get(CreateReportViewModel.class);
        CameraViewModel cameraViewModel = new ViewModelProvider(this).get(CameraViewModel.class);
        cameraViewModel.getImage().observe(this, (image) -> {
            if (image != null) {
                this.image = image;
                Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
                binding.imageView.setImageBitmap(bitmap);
                binding.createReportButton.setEnabled(true);
            }
        });
        binding.takePhotoButton.setOnClickListener(v -> takePhoto());
        binding.createReportButton.setEnabled(false);
        binding.createReportButton.setOnClickListener(v -> createReport());
    }

    private void takePhoto() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivity(intent);
    }

    private void createReport() {
        if (image != null) {
            String description = binding.descriptionEditText.getText().toString();
            createReportViewModel.insert(description, image);
            finish();
        }
    }
}
