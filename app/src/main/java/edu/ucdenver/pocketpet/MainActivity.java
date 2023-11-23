/* Main Activity
This is what shows when user opens the app. This activity is used to start the game and go to info page.
 */
package edu.ucdenver.pocketpet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import edu.ucdenver.pocketpet.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageView eggImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eggImageView = binding.imageViewEgg;

        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        eggImageView.startAnimation(shakeAnimation);

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
}

