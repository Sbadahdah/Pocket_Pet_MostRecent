package edu.ucdenver.pocketpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import edu.ucdenver.pocketpet.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        //setContentView(view);

    }
    //Function that calls pet activity
    public void onStartClick(View view) {
        Intent startIntent = new Intent(this, PetActivity.class);
        startActivity(startIntent);
    }

    //Function that calls the setting/info
    public void onInfoClick(View view) {
        Intent infoIntent = new Intent(this, InfoActivity.class);
        startActivity(infoIntent);
    }



    //Something to to save data -> Shared Preferences
}

