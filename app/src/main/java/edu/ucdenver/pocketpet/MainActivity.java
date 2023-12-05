/* Main Activity
This is what shows when user opens the app. This activity is used to start the game and go to info page.

This activity is what starts when the app opens.
Start button: This button is programmed to take you to the Pet Activity.
Info button: This button is programed to take you to the Info Activity.
Extra Features: There is an animation on the egg to make it shake, for some visual interest on this page.

 */
package edu.ucdenver.pocketpet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.media.MediaPlayer;

import edu.ucdenver.pocketpet.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageView eggImageView;
    private MediaPlayer theme;
    private MediaPlayer buttonClick;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding .inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonClick = MediaPlayer.create(this, R.raw.button_click);

        theme = MediaPlayer.create(this, R.raw.fun_cute_simple_song);
        theme.setLooping(true); //Looping background music
        theme.start();

        eggImageView = binding.imageViewEgg;

        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        eggImageView.startAnimation(shakeAnimation);

    }

    @Override
    protected void onResume() {
        super.onResume();
        theme.start();

    }

    protected  void onPause() {
        super.onPause();
        theme.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        theme.stop();
        theme.release();

    }

        //Function that calls pet activity
    public void onStartClick(View view) {
        buttonClick.start();
        Intent startIntent = new Intent(this, PetActivity.class);
        startActivity(startIntent);
    }

    //Function that calls the setting/info
    public void onInfoClick(View view) {
        buttonClick.start();
        Intent infoIntent = new Intent(this, InfoActivity.class);
        startActivity(infoIntent);
    }
}

