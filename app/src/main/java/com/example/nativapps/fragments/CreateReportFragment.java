package com.example.nativapps.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nativapps.R;
import com.example.nativapps.databinding.FragmentCreateReportBinding;
import com.example.nativapps.viewmodel.CameraViewModel;
import com.example.nativapps.viewmodel.CreateReportViewModel;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateReportFragment extends Fragment {

    private FragmentCreateReportBinding binding;
    private CreateReportViewModel createReportViewModel;
    private File image = null;

    public CreateReportFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createReportViewModel = new ViewModelProvider(this).get(CreateReportViewModel.class);
        CameraViewModel cameraViewModel = new ViewModelProvider(requireActivity())
                .get(CameraViewModel.class);
        cameraViewModel.getImage().observe(requireActivity(), (img) -> {
            if (img != null) {
                image = img;
                Bitmap bitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
                binding.imageView.setImageBitmap(bitmap);
            }
        });
        binding.createReportButton.setEnabled(image != null);
        binding.takePhotoButton.setOnClickListener(this::takePhoto);
        binding.createReportButton.setOnClickListener(v -> createReport());
    }

    private void takePhoto(View view) {
        Navigation.findNavController(view).navigate(R.id.nav_camera);
    }

    private void createReport() {
        if (image != null) {
            String description = binding.descriptionEditText.getText().toString();
            createReportViewModel.insert(description, image);
            requireActivity().finish();
        }
    }
}
