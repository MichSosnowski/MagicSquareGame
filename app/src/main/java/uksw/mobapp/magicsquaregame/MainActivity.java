/**********************************************
 * Android and Mobile Application Programming *
 *            Magic Square Game               *
 *         Author: Michał Sosnowski           *
 *         Academic year: 2022/2023           *
 **********************************************/
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

/**
 * This class is responsible for the correct operation of the main activity
 * of this application. It manages its life cycle and the the entire functioning.
 * It is able to display two layouts depending on the screen orientation. It calls
 * the appropriate methods for each of the buttons contained in these layouts.
 */
public class MainActivity extends AppCompatActivity {

    // input fields of the magic square
    private EditText number1, number2, number3, number4, number5, number6, number7, number8, number9, levelNumber;

    // fields containing the results for the corresponding rows and columns in the magic square
    private TextView result1, result2, result3, result4, result5, result6, information, scoreText;

    // references to the buttons used in the application
    private Button applyButton, submitButton, continueButton, newGameButton, helpButton;

    // reference to the chronometer - the timer used in the application
    private Chronometer myChronometer;

    // an array containing random number in the range (0, 9) without repetition
    private ArrayList<Integer> randomNumbers;

    // information about whether the Apply button has been clicked
    private boolean applyUsed = false;

    // input field states of the magic square - enabled or disabled
    private boolean[] editTextsState;

    // remembered last input field states of the magic square
    private boolean[] rememberedEditTextsState;

    // current time since the beginning of the game
    private long currentTime = 0;

    // maximum number of numbers drawn equal to the number of fields of the magic square
    private final int MAX_NUMBER = 9;

    // success status for exiting the application
    private final int EXIT_STATUS = 0;

    // text displayed after the player wins the game
    private final String CORRECT = "Congratulations! Correct answer.";

    // text displayed after the player loses the game
    private final String INCORRECT = "Incorrect answer!";

    // no text displayed during the game in the results box
    private final String NO_TEXT = "";

    // text with the score displayed after the game
    private final String TEXT_SCORE = "Your score: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // retrieving references to objects from layout files (buttons, edittexts etc.)
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
        // setting the appropriate states for the buttons
        continueButton.setEnabled(false);
        newGameButton.setEnabled(false);
        helpButton.setEnabled(false);
        // initialize arrays, draw numbers and fill GUI with them
        randomNumbers = new ArrayList<>();
        editTextsState = new boolean[] {true, true, true, true, true, true, true, true, true};
        rememberedEditTextsState = new boolean[editTextsState.length];
        chooseRandomNumbers(MAX_NUMBER);
        setResults();
    }

    /**
     * life cycle methods of the main activity
     */
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
        savedInstanceState.putBooleanArray("numberTexts", editTextsState);
        savedInstanceState.putBooleanArray("rememberedNumberTexts", rememberedEditTextsState);
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
        editTextsState = savedInstanceState.getBooleanArray("numberTexts");
        rememberedEditTextsState = savedInstanceState.getBooleanArray("rememberedNumberTexts");
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

    /**
     * This method draws numbers in the range (0, 9) without repetition
     * and saves them in the array - randomNumbers.
     */
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

    /**
     * This method sets the results of adding the drawn numbers for the rows and columns
     * of the magic square.
     */
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

    /**
     * This method removes the contents of the input fields of the magic square.
     */
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

    /**
     * This method sets the state of the input fields of the magic square -
     * enabled or disabled.
     */
    private void setEditTextsEnabled() {
        number1.setEnabled(editTextsState[0]);
        number2.setEnabled(editTextsState[1]);
        number3.setEnabled(editTextsState[2]);
        number4.setEnabled(editTextsState[3]);
        number5.setEnabled(editTextsState[4]);
        number6.setEnabled(editTextsState[5]);
        number7.setEnabled(editTextsState[6]);
        number8.setEnabled(editTextsState[7]);
        number9.setEnabled(editTextsState[8]);
    }

    /**
     * This method is called when the player presses the Apply button.
     * It sets the level of the game. Level 1 means one digit missing,
     * level 2 - two digits missing etc.
     */
    public void setLevel(View view) {
        try {
            int level = Integer.parseInt(levelNumber.getText().toString());
            // Level of the game cannot be equal to 0 (no missing digits).
            if (level == 0) {
                Toast.makeText(this, "Level should be greater than 0", Toast.LENGTH_LONG).show();
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

    /**
     * This method calculates the result of the entire game.
     */
    private int calculateResult(long timeForResult) {
        int result = MAX_NUMBER * 100;

        int hints = 0;
        for (boolean i : rememberedEditTextsState) {
            if (!i)
                hints++;
        }
        result -= hints * 100;

        long timePunishment = SystemClock.elapsedRealtime() - timeForResult;
        if (timePunishment > 300000) {
            timePunishment = (timePunishment - 300000) / 60000;
            int punishment = (int) timePunishment * 10;
            result -= punishment;
        }
        return Math.max(result, 0);
    }

    /**
     * This method checks whether the answers of the player are correct or not.
     * It is called when the player presses the Submit button.
     */
    public void checkAnswer(View view) {
        long timeForResult = myChronometer.getBase();
        currentTime = myChronometer.getBase() - SystemClock.elapsedRealtime();
        myChronometer.stop();
        submitButton.setEnabled(false);
        System.arraycopy(editTextsState, 0, rememberedEditTextsState, 0, editTextsState.length);
        Arrays.fill(editTextsState, false);
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

    /**
     * This method allows the player to continue the game.
     * It is called when the player presses the Continue? button.
     */
    public void continueGame(View view) {
        submitButton.setEnabled(true);
        continueButton.setEnabled(false);
        newGameButton.setEnabled(false);
        if (!applyUsed) {
            levelNumber.setEnabled(true);
            applyButton.setEnabled(true);
        }
        else
            helpButton.setEnabled(true);
        System.arraycopy(rememberedEditTextsState, 0, editTextsState, 0, rememberedEditTextsState.length);
        setEditTextsEnabled();
        information.setText(NO_TEXT);
        myChronometer.setBase(SystemClock.elapsedRealtime() + currentTime);
        myChronometer.start();
    }

    /**
     * This method allows the player to start a new game.
     * It is called when the player presses the New Game button.
     * Then, this method initializes the new game.
     */
    public void createNewGame(View view) {
        setEditTextsClear();
        Arrays.fill(editTextsState, true);
        Arrays.fill(rememberedEditTextsState, true);
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

    /**
     * This method restarts the chronometer - the timer used
     * in the game.
     */
    private void restartMyChronometer() {
        currentTime = 0;
        myChronometer.setBase(SystemClock.elapsedRealtime() + currentTime);
        myChronometer.start();
    }

    /**
     * This method is called when the player presses the Exit Game button.
     * Then, the application will be closed.
     */
    public void exitApp(View view) {
        System.exit(EXIT_STATUS);
    }

    /**
     * This method is called when the player presses the Help button.
     * It gives them a hint where to enter one number. This number is
     * entered by the application and the player cannot edit the input field
     * containing this number.
     */
    public void giveAHint(View view) {
        Random random = new Random();
        int choice = random.nextInt(MAX_NUMBER);
        while (true) {
            if (choice == 0 && number1.isEnabled()) {
                number1.setText(String.valueOf(randomNumbers.get(choice)));
                number1.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 1 && number2.isEnabled()) {
                number2.setText(String.valueOf(randomNumbers.get(choice)));
                number2.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 2 && number3.isEnabled()) {
                number3.setText(String.valueOf(randomNumbers.get(choice)));
                number3.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 3 && number4.isEnabled()) {
                number4.setText(String.valueOf(randomNumbers.get(choice)));
                number4.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 4 && number5.isEnabled()) {
                number5.setText(String.valueOf(randomNumbers.get(choice)));
                number5.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 5 && number6.isEnabled()) {
                number6.setText(String.valueOf(randomNumbers.get(choice)));
                number6.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 6 && number7.isEnabled()) {
                number7.setText(String.valueOf(randomNumbers.get(choice)));
                number7.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 7 && number8.isEnabled()) {
                number8.setText(String.valueOf(randomNumbers.get(choice)));
                number8.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else if (choice == 8 && number9.isEnabled()) {
                number9.setText(String.valueOf(randomNumbers.get(choice)));
                number9.setEnabled(false);
                editTextsState[choice] = false;
                break;
            }
            else
                choice = random.nextInt(MAX_NUMBER);
        }
        System.arraycopy(editTextsState, 0, rememberedEditTextsState, 0, editTextsState.length);
        checkEditTextsAllDisabled();
    }

    /**
     * This method checks if all input fields are disabled by pressing Apply
     * or the Help button. If so, the game is over and the score for the player
     * is displayed.
     */
    private void checkEditTextsAllDisabled() {
        if (!number1.isEnabled() && !number2.isEnabled() && !number3.isEnabled() && !number4.isEnabled()
            && !number5.isEnabled() && !number6.isEnabled() && !number7.isEnabled() && !number8.isEnabled()
            && !number9.isEnabled()) {
            long timeForResult = myChronometer.getBase();
            currentTime = myChronometer.getBase() - SystemClock.elapsedRealtime();
            myChronometer.stop();
            submitButton.setEnabled(false);
            continueButton.setEnabled(false);
            newGameButton.setEnabled(true);
            helpButton.setEnabled(false);
            information.setText(CORRECT);
            int result = calculateResult(timeForResult);
            String score = TEXT_SCORE + result;
            scoreText.setText(score);
        }
    }
}