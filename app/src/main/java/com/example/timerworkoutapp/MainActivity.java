package com.example.timerworkoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private  BottomNavigationView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         bottomNav=findViewById(R.id.bottomNavigationView);

         bottomNav.setOnNavigationItemSelectedListener(navListener);

         //Metto il fragment Home come quello principale
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new HomeFragment()).commit();

    }
   private BottomNavigationView.OnNavigationItemSelectedListener navListener=
           new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   Fragment fragment=null;
                   switch (item.getItemId())
                   {
                       case R.id.item1:
                           fragment=new HomeFragment();
                           break;
                       case R.id.item2:
                           fragment=new TimerFragment();
                           break;
                       case R.id.item3:
                           fragment=new TabataFragment();
                           break;
                   }
                   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                           fragment).commit();
                   return true;
               }
           };
}
