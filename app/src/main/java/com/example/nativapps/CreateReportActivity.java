package com.example.nativapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.nativapps.databinding.ActivityCreateReportBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreateReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCreateReportBinding binding = (
                ActivityCreateReportBinding.inflate(getLayoutInflater())
        );
        setContentView(binding.getRoot());
    }
}
