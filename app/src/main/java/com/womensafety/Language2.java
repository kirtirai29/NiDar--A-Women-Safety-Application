package com.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;

import java.util.Locale;

public class Language2 extends AppCompatActivity {
    Button eng;
    Button hin;
    Button mara;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language2);
        hin = findViewById(R.id.action_Hindi);
        eng = findViewById(R.id.action_English);
        mara= findViewById(R.id.action_Marathi);

        hin.setOnClickListener(v -> {
            setLocal(Language2.this, "hi");
            finish();

            startActivity(new Intent(Language2.this, MainActivity.class));
        });
        eng.setOnClickListener(v -> {
            setLocal(Language2.this, "en");
            finish();
            startActivity(new Intent(Language2.this, MainActivity.class));
        });
        mara.setOnClickListener(v -> {
            setLocal(Language2.this, "mr");
            finish();
            startActivity(new Intent(Language2.this, MainActivity.class));
        });





    }
    public void setLocal(Activity activity, String langCode)
    {
        Locale locale =new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources= activity.getResources();
        Configuration config= resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config,resources.getDisplayMetrics());

    }
}