package com.example.siddh.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import android.os.Handler;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    TextView txtUserScore, txtComputerScore, txtLogs;
    ImageView imgDice;
    Button btnRoll, btnHold, btnReset;
    private Random random = new Random();
    private Random otherRandom = new Random();
    private int currentUserScore, currentComputerScore, sureComputerScore;
    private final int MAX_SCORE = 100;
    private int diceIcons [] = {
            R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6
    };
    private Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate views
        txtUserScore = (TextView) findViewById(R.id.yourScore);
        txtComputerScore = (TextView) findViewById(R.id.computerScore);
        imgDice = (ImageView) findViewById(R.id.imageView);
        btnRoll = (Button) findViewById(R.id.rollbtn);
        btnHold = (Button) findViewById(R.id.holdbtn);
        btnReset = (Button) findViewById(R.id.resetbtn);
        // start game
        // onStart()
        // event listeners
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart();
            }
        });
        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startComputerTurn();
            }
        });
    }

    Runnable computerTurnRunnable = new Runnable() {
        @Override
        public void run() {
            if (otherRandom.nextInt(10) < 7 || currentComputerScore == 0){
                boolean b = computerTurn();
                if (b){
                    timerHandler.postDelayed(this, 1000);
                } else {
                    endComputerTurn();
                }
            } else { // hold
                endComputerTurn();
            }
        }
    };

    private void endComputerTurn(){
        updateComputerScoreBoard();
        btnHold.setEnabled(true);
        btnRoll.setEnabled(true);
    }

    private void startComputerTurn(){
        btnHold.setEnabled(false);
        btnRoll.setEnabled(false);
        currentUserScore = 0;
        currentComputerScore = 0;
        sureComputerScore = Integer.parseInt(txtComputerScore.getText().toString());
        timerHandler.postDelayed(computerTurnRunnable, 500);
    }

    private void updateComputerScoreBoard(){
        txtComputerScore.setText("" + (sureComputerScore + currentComputerScore));
    }

    private boolean computerTurn(){
        int num;
        num = random.nextInt(6) + 1;
        imgDice.setImageResource(diceIcons[num-1]);
        if (num == 1){
            currentComputerScore = 0;
            return false;
        } else {
            currentComputerScore += num;
            updateComputerScoreBoard();
            if ((sureComputerScore + currentComputerScore) >= MAX_SCORE){
                endGame("Computer");
                return false;
            }
        }
        return true;
    }

    private void rollDice(){
        int num = random.nextInt(6) + 1;
        int currentScore = Integer.parseInt(txtUserScore.getText().toString());
        imgDice.setImageResource(diceIcons[num-1]);
        if (num == 1){
            currentScore -= currentUserScore;
            txtUserScore.setText(currentScore + "");
            startComputerTurn();
        } else {
            currentScore += num;
            if (currentScore >= MAX_SCORE){
                endGame("You");
                return;
            }
            currentUserScore += num;
            txtUserScore.setText(currentScore + "");
        }
    }

    private void endGame(String winner){
        (Toast.makeText(this, "Game over. " + winner + " won", Toast.LENGTH_LONG)).show();
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUserScore = 0;
        currentComputerScore = 0;
        sureComputerScore = 0;
        txtUserScore.setText("0");
        txtComputerScore.setText("0");
        timerHandler.removeCallbacks(computerTurnRunnable);
    }
}