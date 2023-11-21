package edu.ucdenver.pocketpet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;
import java.util.Calendar;



import androidx.appcompat.app.AppCompatActivity;

import edu.ucdenver.pocketpet.databinding.PetBinding;


//add music
public class PetActivity extends AppCompatActivity {
    private static final int HATCH_DELAY = 5000;
    private static final int DECREASE_DELAY = 10000;
//    private static final int HATCH_TIME_SECONDS = 5;
//    private static final int PASSED_TIME_SECONDS = 10;
    private Random petPicker = new Random();
    private int randomNum;
    private int hunger;
    private int happiness;
    private boolean hatched = false;
    private ImageView petImageView;
    private ImageView emotionImage;
    private Drawable currentPet;
    private TextView hungerTextView;
    private TextView happinessTextView;
    private ImageButton feedButton;
    private ImageButton loveButton;
    private ImageButton playButton;
    private ImageButton cleanButton;
    private long appClosedTimestamp = 0; // Timestamp when the app was closed
    private Handler handler = new Handler();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetBinding binding;
        binding = PetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petImageView = binding.PetImage;
        emotionImage = binding.EmotionImage;
        currentPet = petImageView.getDrawable();
        hungerTextView = binding.textViewHungPer;
        happinessTextView = binding.textViewHapPer;
        feedButton = binding.FeedButton;
        loveButton = binding.LoveButton;
        playButton = binding.PlayButton;
        cleanButton = binding.CleanButton;
        randomNum = petPicker.nextInt(3) + 1;

        hunger = 100; //Initialize hunger to max
        happiness = 100; //Initialize happiness to max
        hatchTimer();
        decreaseTimer();

        if (hunger<= 50){
            updateEmotion();
        } else {
            resetEmotion();
        }




        feedButton.setOnClickListener(new View.OnClickListener() { //Setting up feed button
            @Override
            public void onClick(View view) {
                increaseHunger();
                setHunger(hunger);
            }
        });
        loveButton.setOnClickListener(new View.OnClickListener() { //Setting up hunger button
            @Override
            public void onClick(View view) {
                increaseHappiness();
                setHappiness(happiness);
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() { //Setting up play button
            @Override
            public void onClick(View view) {

            }
        });
        cleanButton.setOnClickListener(new View.OnClickListener() { //Setting up clean button
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void hatchTimer() { //Timer for hatching
        Runnable hatchTask = new Runnable() {
            @Override
            public void run() {
                handleHatchTime();
                //handler.postDelayed(this, HATCH_DELAY);
            }
        };
        handler.postDelayed(hatchTask, HATCH_DELAY);
    }
    private void handleHatchTime() {
//        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int minute = Calendar.getInstance().get(Calendar.MINUTE);
//        int sec = Calendar.getInstance().get(Calendar.MILLISECOND);
        long timeDifference = calculateTimeDifference();
        long secondsDifference = timeDifference / 1000; // Convert milliseconds to seconds


        if (secondsDifference >= 5 && !hatched) { //hourOfDay >= 8 && hourOfDay < 20

            if (randomNum == 1) {
                updatePet(R.drawable.bigger_jiji);
            } else if (randomNum == 2) {
                updatePet(R.drawable.bigger_no_face);
            } else { //Num should equal 3
                updatePet(R.drawable.bigger_totoro);
            }
            hatched = true;
        }

//        if (secondsDifference >= 20) {
//            decreaseHunger();
//            setHunger(hunger);
//        }
//        if (secondsDifference >= 20) {
//            decreaseHappiness();
//            setHappiness(happiness);
//        }
    }

    private void decreaseTimer() { //Timer for decreasing stats
        Runnable decreaseTask = new Runnable() {
            @Override
            public void run() {
                handleDecreaseTime();
                handler.postDelayed(this, DECREASE_DELAY);
            }
        };
        handler.postDelayed(decreaseTask, DECREASE_DELAY);
    }
    private void handleDecreaseTime() {
//        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int minute = Calendar.getInstance().get(Calendar.MINUTE);
//        int sec = Calendar.getInstance().get(Calendar.MILLISECOND);
//        long timeDifference = calculateTimeDifference();
//        long secondsDifference = timeDifference / 1000; // Convert milliseconds to seconds

        decreaseHunger();
        setHunger(hunger);

        decreaseHappiness();
        setHappiness(happiness);

//        if (secondsDifference >= 20) {
//            decreaseHunger();
//            setHunger(hunger);
//        }
//        if (secondsDifference >= 20) {
//            decreaseHappiness();
//            setHappiness(happiness);
//        }
    }
    private long calculateTimeDifference() {
        // Get the current time
        long currentTime = System.currentTimeMillis();

        // Calculate the time difference
        return currentTime - appClosedTimestamp;
    }

    private void updatePet(int imageResource){ //something to set the picture to pet once egg cracks -> When certain time passes egg cracks -> pet spawns
        petImageView.setImageResource(imageResource);
    }

    private void updateEmotion() {

        emotionImage.setImageResource(R.drawable.food_bubble);

        Drawable currentDrawable = petImageView.getDrawable();
        Drawable jijiDrawable = getResources().getDrawable(R.drawable.bigger_jiji);
        Drawable noFaceDrawable = getResources().getDrawable(R.drawable.bigger_no_face);
        Drawable totoroDrawable = getResources().getDrawable(R.drawable.bigger_totoro);

        if (currentDrawable != null) {
            if (currentDrawable.getConstantState().equals(jijiDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_jiji_upset);
            } else if (currentDrawable.getConstantState().equals(noFaceDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_no_face_upset);
            } else if (currentDrawable.getConstantState().equals(totoroDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_totoro_upset);
            }
        }
    }
    private void resetEmotion(){
        emotionImage.setImageDrawable(null);

    }

    private void setHunger(int hunger) {
        hungerTextView.setText(String.valueOf(hunger) + "%");

    }

    private void setHappiness(int happiness){
        happinessTextView.setText(String.valueOf(happiness) + "%");
    }

    private void decreaseHunger() { //Something to decrease hunger
        hunger -= 25;
    }

    private void decreaseHappiness() { //Something to decrease happiness -> if that reaches 0 decrease it dies
        happiness -= 1;
    }

    private void increaseHunger() { //Something to increase hunger -> if that reaches 0 decrease it dies
        hunger += 5;
        if (hunger >= 100) {
            hunger = 100;
        }

    }

    private void increaseHappiness() { //Something to increase happiness
        happiness += 5;
        if (happiness >= 100) {
            happiness = 100;
        }
    }

    private void setCleanButton(){

    }

    private void setPlayButton(){

    }

    private void respawn() {  //Figure out how to respawn egg when pet dies

    }


    public void onHomeClick(View view) { //Something to send user back home if home button is pressed
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }


}
