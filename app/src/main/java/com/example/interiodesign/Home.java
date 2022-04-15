package com.example.interiodesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



//        getSupportActionBar().setTitle("InteriorDesign");

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationbar);
        bottomNavigationView.setBackground(null);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_container_home, new HomeFragment()).commit();


        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment temp = null;
                switch (item.getItemId())
                {
                    case R.id.gallery:
                        temp = new UserFragment();
                        Log.e("her1", String.valueOf(item.getItemId()));
                        break;
                    case R.id.collection :
                        temp = new CollectionFragment();
                        Log.e("her1", String.valueOf(item.getItemId()));
                        break;
                    case R.id.cart :
                        temp = new CartFragment();
                        Log.e("her1", String.valueOf(item.getItemId()));
                        break;
                    case R.id.home:
                        temp = new HomeFragment();
                        Log.e("her1", String.valueOf(item.getItemId()));

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_container_home, temp).commit();
                return true;
            }
        });

        findViewById(R.id.ar_button).setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(),showAR.class);
            //intent.putExtra("file_name",null);
            startActivity(intent);
        });


    }
}