package uksw.mobapp.magicsquaregame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText number1, number2, number3, number4, number5, number6, number7, number8, number9, levelNumber;
    private TextView result1, result2, result3, result4, result5, result6, information, scoreText;
    private Button applyButton, submitButton, continueButton, newGameButton, helpButton;
    private Chronometer myChronometer;
    private ArrayList<Integer> randomNumbers;
    private boolean applyUsed = false;
    private boolean[] buttonsState;
    private boolean[] rememberedButtonsState;
    private long currentTime = 0;
    private final int MAX_NUMBER = 9;
    private final int EXIT_STATUS = 0;
    private final String CORRECT = "Congratulations! Correct answer.";
    private final String INCORRECT = "Incorrect answer!";
    private final String NO_TEXT = "";
    private final String TEXT_SCORE = "Your score: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        number7 = findViewById(R.id.number7);
        number8 = findViewById(R.id.number8);
        number9 = findViewById(R.id.number9);
        result1 = findViewById(R.id.result1);
        result2 = findViewById(R.id.result2);
        result3 = findViewById(R.id.result3);
        result4 = findViewById(R.id.result4);
        result5 = findViewById(R.id.result5);
        result6 = findViewById(R.id.result6);
        myChronometer = findViewById(R.id.chronometer);
        information = findViewById(R.id.information);
        levelNumber = findViewById(R.id.levelNumber);
        scoreText = findViewById(R.id.scoreText);
        applyButton = findViewById(R.id.apply_button);
        submitButton = findViewById(R.id.submit_button);
        continueButton = findViewById(R.id.continue_button);
        newGameButton = findViewById(R.id.new_game_button);
        helpButton = findViewById(R.id.help_button);
        continueButton.setEnabled(false);
        newGameButton.setEnabled(false);
        helpButton.setEnabled(false);
        randomNumbers = new ArrayList<>();
        buttonsState = new boolean[] {true, true, true, true, true, true, true, true, true};
        rememberedButtonsState = new boolean[buttonsState.length];
        chooseRandomNumbers(MAX_NUMBER);
        setResults();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myChronometer.setBase(SystemClock.elapsedRealtime() + currentTime);
        if (submitButton.isEnabled())
            myChronometer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (submitButton.isEnabled())
            currentTime = myChronometer.getBase() - SystemClock.elapsedRealtime();
        myChronometer.stop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt("number1", randomNumbers.get(0));
        savedInstanceState.putInt("number2", randomNumbers.get(1));
        savedInstanceState.putInt("number3", randomNumbers.get(2));
        savedInstanceState.putInt("number4", randomNumbers.get(3));
        savedInstanceState.putInt("number5", randomNumbers.get(4));
        savedInstanceState.putInt("number6", randomNumbers.get(5));
        savedInstanceState.putInt("number7", randomNumbers.get(6));
        savedInstanceState.putInt("number8", randomNumbers.get(7));
        savedInstanceState.putInt("number9", randomNumbers.get(8));
        if (!information.getText().toString().equals(NO_TEXT))
            savedInstanceState.putString("information", information.getText().toString());
        savedInstanceState.putBooleanArray("numberTexts", buttonsState);
        savedInstanceState.putBooleanArray("rememberedNumberTexts", rememberedButtonsState);
        savedInstanceState.putBoolean("levelNumber", levelNumber.isEnabled());
        savedInstanceState.putBoolean("apply_used", applyUsed);
        savedInstanceState.putBoolean("apply", applyButton.isEnabled());
        savedInstanceState.putBoolean("submit", submitButton.isEnabled());
        savedInstanceState.putBoolean("continue", continueButton.isEnabled());
        savedInstanceState.putBoolean("new_game", newGameButton.isEnabled());
        savedInstanceState.putBoolean("help", helpButton.isEnabled());
        savedInstanceState.putString("scoreText", scoreText.getText().toString());
        savedInstanceState.putLong("chronometer", currentTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        randomNumbers.clear();
        randomNumbers.add(savedInstanceState.getInt("number1"));
        randomNumbers.add(savedInstanceState.getInt("number2"));
        randomNumbers.add(savedInstanceState.getInt("number3"));
        randomNumbers.add(savedInstanceState.getInt("number4"));
        randomNumbers.add(savedInstanceState.getInt("number5"));
        randomNumbers.add(savedInstanceState.getInt("number6"));
        randomNumbers.add(savedInstanceState.getInt("number7"));
        randomNumbers.add(savedInstanceState.getInt("number8"));
        randomNumbers.add(savedInstanceState.getInt("number9"));
        if (savedInstanceState.containsKey("information"))
            information.setText(savedInstanceState.getString("information"));
        buttonsState = savedInstanceState.getBooleanArray("numberTexts");
        rememberedButtonsState = savedInstanceState.getBooleanArray("rememberedNumberTexts");
        setEditTextsEnabled();
        levelNumber.setEnabled(savedInstanceState.getBoolean("levelNumber"));
        applyUsed = savedInstanceState.getBoolean("apply_used");
        applyButton.setEnabled(savedInstanceState.getBoolean("apply"));
        submitButton.setEnabled(savedInstanceState.getBoolean("submit"));
        continueButton.setEnabled(savedInstanceState.getBoolean("continue"));
        newGameButton.setEnabled(savedInstanceState.getBoolean("new_game"));
        helpButton.setEnabled(savedInstanceState.getBoolean("help"));
        scoreText.setText(savedInstanceState.getString("scoreText"));
        currentTime = savedInstanceState.getLong("chronometer");
        setResults();
    }

    protected void chooseRandomNumbers(int maxNumber) {
        Random random = new Random();
        for (int i = 0; i < maxNumber; i++) {
            int num = random.nextInt(maxNumber) + 1;
            while (true) {
                if (!randomNumbers.contains(num)) {
                    randomNumbers.add(num);
                    break;
                }
                else
                    num = random.nextInt(maxNumber) + 1;
            }
        }
    }

    protected void setResults() {
        int result = randomNumbers.get(0) + randomNumbers.get(1) + randomNumbers.get(2);
        result1.setText(String.valueOf(result));
        result = randomNumbers.get(3) + randomNumbers.get(4) + randomNumbers.get(5);
        result2.setText(String.valueOf(result));
        result = randomNumbers.get(6) + randomNumbers.get(7) + randomNumbers.get(8);
        result3.setText(String.valueOf(result));
        result = randomNumbers.get(0) + randomNumbers.get(3) + randomNumbers.get(6);
        result4.setText(String.valueOf(result));
        result = randomNumbers.get(1) + randomNumbers.get(4) + randomNumbers.get(7);
        result5.setText(String.valueOf(result));
        result = randomNumbers.get(2) + randomNumbers.get(5) + randomNumbers.get(8);
        result6.setText(String.valueOf(result));
    }

    private void setEditTextsClear() {
        number1.setText("");
        number2.setText("");
        number3.setText("");
        number4.setText("");
        number5.setText("");
        number6.setText("");
        number7.setText("");
        number8.setText("");
        number9.setText("");
    }

    private void setEditTextsEnabled() {
        number1.setEnabled(buttonsState[0]);
        number2.setEnabled(buttonsState[1]);
        number3.setEnabled(buttonsState[2]);
        number4.setEnabled(buttonsState[3]);
        number5.setEnabled(buttonsState[4]);
        number6.setEnabled(buttonsState[5]);
        number7.setEnabled(buttonsState[6]);
        number8.setEnabled(buttonsState[7]);
        number9.setEnabled(buttonsState[8]);
    }

    public void setLevel(View view) {
        try {
            int level = Integer.parseInt(levelNumber.getText().toString());
            if (level == 0 || level == 9) {
                Toast.makeText(this, "Level should be from range (0, 9)", Toast.LENGTH_LONG).show();
                return;
            }
            for (int i = 0; i < MAX_NUMBER - level; i++) giveAHint(view);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, "Level cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        levelNumber.setEnabled(false);
        applyButton.setEnabled(false);
        helpButton.setEnabled(true);
        applyUsed = true;
    }

    private int calculateResult(long timeForResult) {
        int result = MAX_NUMBER * 100;

        int hints = 0;
        for (boolean i : rememberedButtonsState) {
            if (!i)
                hints++;
        }
        result -= hints * 100;

        long timePunishment = SystemClock.elapsedRealtime() - timeForResult;
        if (timePunishment > 300000) {
            timePunishment = (timePunishment - 300000) / 60000;
            int punishment = ((int) timePunishment / 10) * 10;
            result -= punishment;
        }
        return result;
    }

    public void checkAnswer(View view) {
        long timeForResult = myChronometer.getBase();
        currentTime = myChronometer.getBase() - SystemClock.elapsedRealtime();
        myChronometer.stop();
        submitButton.setEnabled(false);
        System.arraycopy(buttonsState, 0, rememberedButtonsState, 0, buttonsState.length);
        Arrays.fill(buttonsState, false);
        setEditTextsEnabled();
        if (number1.getText().toString().equals(String.valueOf(randomNumbers.get(0)))
            && number2.getText().toString().equals(String.valueOf(randomNumbers.get(1)))
            && number3.getText().toString().equals(String.valueOf(randomNumbers.get(2)))
            && number4.getText().toString().equals(String.valueOf(randomNumbers.get(3)))
            && number5.getText().toString().equals(String.valueOf(randomNumbers.get(4)))
            && number6.getText().toString().equals(String.valueOf(randomNumbers.get(5)))
            && number7.getText().toString().equals(String.valueOf(randomNumbers.get(6)))
            && number8.getText().toString().equals(String.valueOf(randomNumbers.get(7)))
            && number9.getText().toString().equals(String.valueOf(randomNumbers.get(8)))) {
            information.setText(CORRECT);
            int result = calculateResult(timeForResult);
            String score = TEXT_SCORE + result;
            scoreText.setText(score);
        }
        else {
            information.setText(INCORRECT);
            continueButton.setEnabled(true);
        }
        levelNumber.setEnabled(false);
        applyButton.setEnabled(false);
        newGameButton.setEnabled(true);
        helpButton.setEnabled(false);
    }

    public void continueGame(View view) {
        submitButton.setEnabled(true);
        continueButton.setEnabled(false);
        newGameButton.setEnabled(false);
        helpButton.setEnabled(true);
        if (!applyUsed) {
            levelNumber.setEnabled(true);
            applyButton.setEnabled(true);
        }
        System.arraycopy(rememberedButtonsState, 0, buttonsState, 0, rememberedButtonsState.length);
        setEditTextsEnabled();
        information.setText(NO_TEXT);
        myChronometer.setBase(SystemClock.elapsedRealtime() + currentTime);
        myChronometer.start();
    }

    public void createNewGame(View view) {
        setEditTextsClear();
        Arrays.fill(buttonsState, true);
        setEditTextsEnabled();
        information.setText(NO_TEXT);
        levelNumber.setEnabled(true);
        levelNumber.setText("1");
        applyUsed = false;
        applyButton.setEnabled(true);
        submitButton.setEnabled(true);
        continueButton.setEnabled(false);
        newGameButton.setEnabled(false);
        scoreText.setText("");
        randomNumbers.clear();
        chooseRandomNumbers(MAX_NUMBER);
        setResults();
        restartMyChronometer();
    }

    private void restartMyChronometer() {
        currentTime = 0;
        myChronometer.setBase(SystemClock.elapsedRealtime() + currentTime);
        myChronometer.start();
    }

    public void exitApp(View view) {
        System.exit(EXIT_STATUS);
    }

    public void giveAHint(View view) {
        Random random = new Random();
        int choice = random.nextInt(MAX_NUMBER);
        while (true) {
            if (choice == 0 && number1.isEnabled()) {
                number1.setText(String.valueOf(randomNumbers.get(choice)));
                number1.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 1 && number2.isEnabled()) {
                number2.setText(String.valueOf(randomNumbers.get(choice)));
                number2.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 2 && number3.isEnabled()) {
                number3.setText(String.valueOf(randomNumbers.get(choice)));
                number3.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 3 && number4.isEnabled()) {
                number4.setText(String.valueOf(randomNumbers.get(choice)));
                number4.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 4 && number5.isEnabled()) {
                number5.setText(String.valueOf(randomNumbers.get(choice)));
                number5.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 5 && number6.isEnabled()) {
                number6.setText(String.valueOf(randomNumbers.get(choice)));
                number6.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 6 && number7.isEnabled()) {
                number7.setText(String.valueOf(randomNumbers.get(choice)));
                number7.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 7 && number8.isEnabled()) {
                number8.setText(String.valueOf(randomNumbers.get(choice)));
                number8.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else if (choice == 8 && number9.isEnabled()) {
                number9.setText(String.valueOf(randomNumbers.get(choice)));
                number9.setEnabled(false);
                buttonsState[choice] = false;
                checkEditTextsAllDisabled();
                break;
            }
            else
                choice = random.nextInt(MAX_NUMBER);
        }
    }

    private void checkEditTextsAllDisabled() {
        if (!number1.isEnabled() && !number2.isEnabled() && !number3.isEnabled() && !number4.isEnabled()
            && !number5.isEnabled() && !number6.isEnabled() && !number7.isEnabled() && !number8.isEnabled()
            && !number9.isEnabled()) {
            myChronometer.stop();
            currentTime = myChronometer.getBase() - SystemClock.elapsedRealtime();
            submitButton.setEnabled(false);
            continueButton.setEnabled(false);
            newGameButton.setEnabled(true);
            helpButton.setEnabled(false);
            information.setText(CORRECT);
        }
    }
}