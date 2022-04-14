package com.example.interiodesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {
    private FirebaseData firebaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        firebaseData = new FirebaseData();
        firebaseData.getAllProducts();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("manual",this. getClass(). getSimpleName()+" "+"taking to LoginRegister after 3 sec");
                Intent i = new Intent(getBaseContext(), LoginRegister.class);
                startActivity(i);
                finish();
            }
        }, 1000);


    }

}