package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AppInfo extends AppCompatActivity {

    ImageView imgBgAppInfo;
    TextView txtCoderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getIdActivity();

    }

    private void getIdActivity() {
        imgBgAppInfo = findViewById(R.id.imageViewBgAppInfo);
        txtCoderName = findViewById(R.id.textViewCoderName);
        txtCoderName.setSelected(true);
        Glide.with(this).asGif().load(R.drawable.bg_app_info).into(imgBgAppInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
