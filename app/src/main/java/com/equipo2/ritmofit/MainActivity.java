package com.equipo2.ritmofit;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.equipo2.ritmofit.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int clicks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvCounter.setText("Clicks: " + clicks);

        binding.btnClick.setOnClickListener(v -> {
            clicks += 1;
            binding.tvCounter.setText("Clicks: " + clicks);
        });

        binding.btnNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("count", clicks);
            startActivity(intent);
        });
    }
}
