package com.example.nativapps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.nativapps.adapter.ItemAdapter;
import com.example.nativapps.databinding.ActivityMainBinding;
import com.example.nativapps.viewmodel.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.nativapps.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RecyclerView recyclerView = binding.recyclerView;
        MainViewModel mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getAll().observe(this, reports -> {
            recyclerView.setAdapter(new ItemAdapter(reports));
        });
        recyclerView.setHasFixedSize(true);
        binding.createReportButton.setOnClickListener(v -> createReport());
    }

    private void createReport() {
        Intent intent = new Intent(this, CreateReportActivity.class);
        startActivity(intent);
    }
}
