/* Info Activity
This handles the Info page. This activity is used to go back to the starting page.
 */
package edu.ucdenver.pocketpet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    private MediaPlayer infoSong;
    private MediaPlayer buttonClick;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_info);
//        binding = PetBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();

        buttonClick = MediaPlayer.create(this, R.raw.button_click);

    }


    @Override
    protected void onResume() {
        super.onResume();

        infoSong = MediaPlayer.create(this, R.raw.info_song);
        infoSong.setLooping(true);
        infoSong.start();
    }

    @Override
    protected void onPause() {
        super.onPause();

        infoSong.stop();
        infoSong.release();
}




    public void onHomeClick(View view) {//Something that sends user back to main activity if home button is pressed
        buttonClick.start();
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }
}
