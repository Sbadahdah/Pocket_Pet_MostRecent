/* Pet Activity
This activity is where all the fun stuff happens. Data in this activity is being saved using shared preferences.
Home button: This button is programmed to take you to back to the Main Activity (The starting screen).
Heart button: This button is used to give the pet love which increases the Happiness by 5%. It also briefly changes the pet’s facial emotion to happy.
Food Button: This button is used to feed the pet and it increases the Hunger by 5%. It also briefly changes the pet’s facial emotion to happy.
Cleaning Button: This button is used clean up the pet’s poop and it increases the Happiness by 5%. It also briefly changes the pet’s facial emotion to happy.
Play Button: This button causes the pet to do a fun animation and increases the Happiness by 10%. It also briefly changes the pet’s facial emotion to happy.

Egg Timer: This timer goes off after 10 seconds and then simulates the egg “hatching”. Once it goes off the user gets a randomized pet. There is an animation on the egg to make it shake until it “hatches” then the randomized pet spawns in doing a fun bouncing animation.
Hunger Timer: This timer goes off every 15 seconds and it decreases the pet’s hunger by 10% and will pop up an emotion bubble indicating hunger and changes the pet’s facial emotion to upset.
Happiness Timer: This timer goes off every 15 seconds and it decreases the pet’s happiness by 2% and will pop up an emotion bubble indicating hunger and changes the pet’s facial emotion to upset.
Poop Timer: This timer goes off every 35 seconds and it spawns a poop image to simulate the pet pooping. When the pet poops the happiness decrease by 10%. It also shows the user a message telling them the pet has pooped and changes the pet’s facial emotion to upset.
*All timers for this activity is being managed with Handlers. We made different timers for different things.
Once Hunger or Happiness reaches 0% the pet will be “dead” and the app is programmed to respawn a new egg and pet for the user. The app will kind of reset in a sense.

 */

package edu.ucdenver.pocketpet;

import android.media.MediaPlayer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import edu.ucdenver.pocketpet.databinding.PetBinding;


//add music
public class PetActivity extends AppCompatActivity {
    private static final int HATCH_DELAY = 10000;
    private static final int DECREASE_DELAY = 15000;
    private static final int POOP_DELAY = 35000;
    private static final String PREFS_NAME = "PetPrefs";
    private static final String HUNGER_KEY = "hunger";
    private static final String HAPPINESS_KEY = "happiness";
    private static final String HATCHED_KEY = "hatched";
    private static final String RANDOM_NUM_KEY = "randomNum";
    private static final String POOP_KEY = "hasPooped";
    private MediaPlayer buttonClick;
    private MediaPlayer petTheme;
    private Random petPicker = new Random();
    private int randomNum;
    private int hunger;
    private int happiness;
    private boolean hatched = false;
    private boolean hasPooped = false;
    private ImageView petImageView;
    private ImageView emotionImage;
    private ImageView poopImage;
    private TextView hungerTextView;
    private TextView happinessTextView;
    private ImageButton feedButton;
    private ImageButton loveButton;
    private ImageButton playButton;
    private ImageButton cleanButton;
    private Handler handler = new Handler();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PetBinding binding;
        binding = PetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        petImageView = binding.PetImage;
        emotionImage = binding.EmotionImage;
        poopImage = binding.PoopImage;
        hungerTextView = binding.textViewHungPer;
        happinessTextView = binding.textViewHapPer;
        feedButton = binding.FeedButton;
        loveButton = binding.LoveButton;
        playButton = binding.PlayButton;
        cleanButton = binding.CleanButton;
        buttonClick = MediaPlayer.create(this, R.raw.button_click);
        petTheme = MediaPlayer.create(this, R.raw.pet_theme);
        petTheme.setLooping(true); //Looping background music
        petTheme.start();

        saveData(); //Restoring saved data

        hunger = 100; //Initialize hunger to max
        happiness = 100; //Initialize happiness to max
        if (!hatched) {
            hatchTimer();
        }
        decreaseTimer();
        poopTimer();



        feedButton.setOnClickListener(new View.OnClickListener() { //Setting up feed button
            @Override
            public void onClick(View view) {
                increaseHunger();
                buttonClick.start();
                buttonPressEmotion();
            }
        });
        loveButton.setOnClickListener(new View.OnClickListener() { //Setting up hunger button
            @Override
            public void onClick(View view) {
                increaseHappiness(5);
                buttonPressEmotion();
                buttonClick.start();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() { //Setting up play button
            @Override
            public void onClick(View view) {
                setPlayButton();
                buttonPressEmotion();
                buttonClick.start();
            }
        });
        cleanButton.setOnClickListener(new View.OnClickListener() { //Setting up clean button
            @Override
            public void onClick(View view) {
                cleanPoop();
                buttonClick.start();
            }
        });
    }

    @Override
    protected void onPause() {  //Saving the current state
        super.onPause();
        //Using Shared Preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(HUNGER_KEY, hunger); //Saving hunger stat
        editor.putInt(HAPPINESS_KEY, happiness); //Saving happiness stat
        editor.putBoolean(HATCHED_KEY, hatched); //Saving hatch status
        editor.putInt(RANDOM_NUM_KEY, randomNum);  //Saving random number to retrieve pet
        editor.putBoolean(POOP_KEY,hasPooped); //Saving poop state
        editor.apply();

        petTheme.pause();

    }

    @Override
    protected void onResume() { //Restoring the saved state
        super.onResume();
        petTheme.start();
        saveData(); //Calling saved data
    }

    private void saveData(){ //Saving the data
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        hunger = prefs.getInt(HUNGER_KEY, 100); //If not found resetting values
        happiness = prefs.getInt(HAPPINESS_KEY, 100); //If not found resetting values
        hatched = prefs.getBoolean(HATCHED_KEY, false); //If not found resetting values
        randomNum = prefs.getInt(RANDOM_NUM_KEY, 1); //If not found resetting values
        hasPooped = prefs.getBoolean(POOP_KEY, false); //If not found resetting values

        setHunger(hunger); //Setting hunger
        setHappiness(happiness); //setting happiness
        if (hatched) { //checking hatched status and then setting pet picture accordingly
            if (randomNum == 1) {
                petImageView.setImageResource(R.drawable.bigger_jiji);
            } else if (randomNum == 2) {
                petImageView.setImageResource(R.drawable.bigger_no_face);
            } else {
                petImageView.setImageResource(R.drawable.bigger_totoro);
            }
            hatched = true;
        } else {
            petImageView.setImageResource(R.drawable.biggeregg);
        }


        if (hasPooped) {
            poopImage.setImageResource(R.drawable.poop);
        }

        updateEmotion();

    }


    private void hatchTimer() { //Timer for hatching
        applyShakeAnimation();
        Runnable hatchTask = new Runnable() {
            @Override
            public void run() {
                handleHatchTime();
//                Toast.makeText(PetActivity.this, "Your pet has hatched!", Toast.LENGTH_SHORT).show();
                petImageView.clearAnimation();
                petAnimation();
                //handler.postDelayed(this, HATCH_DELAY);
            }
        };
        handler.postDelayed(hatchTask, HATCH_DELAY);
    }

    private void handleHatchTime() {

        randomNum = petPicker.nextInt(3) + 1;

        if (randomNum == 1) {
            updatePet(R.drawable.bigger_jiji);
        } else if (randomNum == 2) {
            updatePet(R.drawable.bigger_no_face);
        } else { //Num should equal 3
            updatePet(R.drawable.bigger_totoro);
        }
        hatched = true;
    }

    private  void petAnimation() {
        Animation funAnimation = AnimationUtils.loadAnimation(this, R.anim.fun_animation);
        petImageView.startAnimation(funAnimation);
    }
    private void applyShakeAnimation() {
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake_animation);
        petImageView.startAnimation(shakeAnimation);
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

    private void handleDecreaseTime() { //What to do when the decrease stats timer is up
        if (hatched){
            decreaseHunger();
            setHunger(hunger);

            decreaseHappiness(2);
            setHappiness(happiness);

            updateEmotion();
        }
    }

    private void updatePet(int imageResource){ //something to set the picture to pet
        petImageView.setImageResource(imageResource);
    }

    private void updateEmotion() { //Updating emotion to upset when happiness or hunger is low
        Drawable currentPet = petImageView.getDrawable();
        Drawable jijiDrawable = getResources().getDrawable(R.drawable.bigger_jiji);
        Drawable noFaceDrawable = getResources().getDrawable(R.drawable.bigger_no_face);
        Drawable totoroDrawable = getResources().getDrawable(R.drawable.bigger_totoro);
        if (hunger <= 50) {
            emotionImage.setImageResource(R.drawable.food_bubble);
            if (currentPet != null) {
                if (currentPet.getConstantState().equals(jijiDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_jiji_upset);
                } else if (currentPet.getConstantState().equals(noFaceDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_no_face_upset);
                } else if (currentPet.getConstantState().equals(totoroDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_totoro_upset);
                }
            }
//            Toast.makeText(this, "Your pet is really hungry, you should feed it!", Toast.LENGTH_SHORT).show();
        }
        if (happiness <= 50) {
            emotionImage.setImageResource(R.drawable.heart_bubble);
            if (currentPet != null) {
                if (currentPet.getConstantState().equals(jijiDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_jiji_upset);
                } else if (currentPet.getConstantState().equals(noFaceDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_no_face_upset);
                } else if (currentPet.getConstantState().equals(totoroDrawable.getConstantState())) {
                    updatePet(R.drawable.bigger_totoro_upset);
                }
            }
//            Toast.makeText(this, "Your pet is getting sad, you should play with it!", Toast.LENGTH_SHORT).show();
        }
    }

    private void buttonPressEmotion() { //Happiness emotion
        Drawable currentPet = petImageView.getDrawable();
        if (currentPet != null) { //Changing pet to happy
            if (randomNum == 1) {
                updatePet(R.drawable.bigger_jiji_happy);
            } else if (randomNum == 2) {
                updatePet(R.drawable.bigger_no_face_happy);
            } else if (randomNum == 3) {
                updatePet(R.drawable.bigger_totoro_happy);
            }
        }
        handler.postDelayed(new Runnable() { //Timer for how long the pet will look happy
            @Override
            public void run() {
                resetEmotion();
            }
        }, 3000); // Show happy emotion for 3 seconds
    }

    private void resetEmotion(){ //resetting pet image and emotion image to default
        Drawable currentEmotion = emotionImage.getDrawable();
        Drawable currentPet = petImageView.getDrawable();
        Drawable jijiDrawable = getResources().getDrawable(R.drawable.bigger_jiji);
        Drawable noFaceDrawable = getResources().getDrawable(R.drawable.bigger_no_face);
        Drawable totoroDrawable = getResources().getDrawable(R.drawable.bigger_totoro);
        if (currentEmotion != null) {
            emotionImage.setImageDrawable(null);
        }
        if (currentPet != null) {
            if (!currentPet.getConstantState().equals(jijiDrawable.getConstantState()) && randomNum == 1) {
                updatePet(R.drawable.bigger_jiji);
            } else if (!currentPet.getConstantState().equals(noFaceDrawable.getConstantState()) && randomNum == 2) {
                updatePet(R.drawable.bigger_no_face);
            } else if (!currentPet.getConstantState().equals(totoroDrawable.getConstantState()) && randomNum == 3) {
                updatePet(R.drawable.bigger_totoro);
            }
        }
    }

    private void setHunger(int hunger) { //Setting hunger
        hungerTextView.setText(String.valueOf(hunger) + "%");
    }

    private void setHappiness(int happiness){ //Setting happiness
        happinessTextView.setText(String.valueOf(happiness) + "%");
    }

    private void decreaseHunger() { //Something to decrease hunger -> if that reaches 0 it dies
        hunger -= 10;

        if (hunger <= 0) { //handling pets death
            respawn();
//            Toast.makeText(this, R.string.pet_died, Toast.LENGTH_LONG).show();
        }
        setHunger(hunger);
    }

    private void decreaseHappiness(int num) { //Something to decrease happiness -> if that reaches 0 it dies
        happiness -= num;

        if (happiness <= 0) {  //handling pets death
            respawn();
//            Toast.makeText(this, R.string.pet_died, Toast.LENGTH_LONG).show();
        }
        setHappiness(happiness);
    }

    private void increaseHunger() { //Something to increase hunger -> feed
        hunger += 5;
        if (hunger > 50) {
            resetEmotion();
        }
        if (hunger >= 100) {
            hunger = 100;
//            Toast.makeText(this, "Pet is all full!", Toast.LENGTH_SHORT).show();
        }
        setHunger(hunger);
    }

    private void increaseHappiness(int num) { //Something to increase happiness
        happiness += num;
        if (happiness > 50) {
            resetEmotion();
        }
        if (happiness >= 100) {
            happiness = 100;
//            Toast.makeText(this, "Pet is at max happiness!", Toast.LENGTH_SHORT).show();
        }
        setHappiness(happiness);
    }

    private void setPlayButton(){
        if (hatched) {
            increaseHappiness(10);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.play_animation);
            petImageView.startAnimation(animation);
        }
    }

    private void poopTimer() { //Poop timer
        Runnable decreaseTask = new Runnable() {
            @Override
            public void run() {
                poop();
                handler.postDelayed(this, POOP_DELAY);
            }
        };
        handler.postDelayed(decreaseTask, POOP_DELAY);
    }

    private void poop(){
        hasPooped = true;
        poopImage.setImageResource(R.drawable.poop);
        decreaseHappiness(10); //decreasing happiness
//        Toast.makeText(this, "Your pet has pooped.", Toast.LENGTH_SHORT).show(); //Letting user know pet has pooped

        //changing pet image to upset
        Drawable currentPet = petImageView.getDrawable();
        Drawable jijiDrawable = getResources().getDrawable(R.drawable.bigger_jiji);
        Drawable noFaceDrawable = getResources().getDrawable(R.drawable.bigger_no_face);
        Drawable totoroDrawable = getResources().getDrawable(R.drawable.bigger_totoro);

        if (currentPet != null) {
            if (currentPet.getConstantState().equals(jijiDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_jiji_upset);
            } else if (currentPet.getConstantState().equals(noFaceDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_no_face_upset);
            } else if (currentPet.getConstantState().equals(totoroDrawable.getConstantState())) {
                updatePet(R.drawable.bigger_totoro_upset);
            }
        }


    }

    private void cleanPoop(){
        Drawable isTherePoop = poopImage.getDrawable();
        if (isTherePoop != null) {
            hasPooped = false;
            poopImage.setImageDrawable(null); //getting rid of poop image
            increaseHappiness(5); //increasing happiness
            buttonPressEmotion(); //making pet smile

        }

//        else Toast.makeText(this, R.string.nothing_to_clean, Toast.LENGTH_SHORT).show();

    }

    private void resetImages() { //resetting all images if pet dies
        Drawable currentEmotion = emotionImage.getDrawable();
        if (currentEmotion != null) { //Resetting all emotions
            emotionImage.setImageDrawable(null);
        }

        Drawable isTherePoop = poopImage.getDrawable();
        if (isTherePoop != null) { //Resetting poop in case there is poop still
            poopImage.setImageDrawable(null);
        }

        updatePet(R.drawable.biggeregg); //Spawning egg
        hatched = false; //Resetting hatched status


    }

    private void respawn() {  //Figure out how to respawn egg when pet dies
        hunger = 100; //Resetting hunger
        happiness = 100; //Resetting happiness
        resetImages();
        hatchTimer(); //Starting hatch timer
//        Toast.makeText(this, R.string.pet_died, Toast.LENGTH_LONG).show();
    }


    public void onHomeClick(View view) { //Something to send user back home if home button is pressed
        buttonClick.start();
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);

    }


}
