package edu.ucdenver.pocketpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    //Something that sends user back to main activity if home button is pressed
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
//        binding = PetBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//

    }

    public void onHomeClick(View view) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }
}
