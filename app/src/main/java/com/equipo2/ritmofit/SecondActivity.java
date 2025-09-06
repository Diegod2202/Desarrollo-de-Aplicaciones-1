package com.equipo2.ritmofit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        int count = getIntent().getIntExtra("count", 0);
        TextView tv = findViewById(R.id.tvMsg);
        tv.setText("Llegaste con " + count + " clicks 👋");

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Cierra SecondActivity y vuelve a MainActivity
        });

    }
}
