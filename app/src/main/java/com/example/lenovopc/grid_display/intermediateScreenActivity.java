package com.example.lenovopc.grid_display;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;


public class intermediateScreenActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    int totalRow = 9;
    int totalCol = 9;
    int mines = 30;
    int mine_row = mines/totalRow ;
    int mineCount;

    int max;
    int score;

    int state_complete = 1;
    int state_incomplete = 0;
    int present_state = state_incomplete;

    LinearLayout baseGrid;
    CustomGrid grid[][];
    LinearLayout rows[];

    Chronometer stopwatch;
    Button reset;

    //Function to create base grid layout, calls method to initialize it and sets chronometer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamelayout);

        baseGrid = (LinearLayout) findViewById(R.id.baseGrid);
        initialize();
        stopwatch=(Chronometer)findViewById(R.id.chronometer);
        stopwatch.setBase(SystemClock.elapsedRealtime());
        stopwatch.start();
        reset= (Button)findViewById(R.id.reset);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mineCount=0;
                initialize();
                Toast.makeText(intermediateScreenActivity.this,"Grid Reset Successful!!",Toast.LENGTH_SHORT).show();
                stopwatch.setBase(SystemClock.elapsedRealtime());
                stopwatch.start();
            }
        });
    }

    //Function to initialize game grid
    public void initialize() {
        score = 0;
        present_state = state_incomplete;
        baseGrid.removeAllViews();
        rows = new LinearLayout[totalRow];
        grid = new CustomGrid[totalRow][totalCol];
        int i;
        for (i = 0; i < totalRow; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams rowParams;
            rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            linearLayout.setLayoutParams(rowParams);
            rows[i] = linearLayout;
            baseGrid.addView(linearLayout);
            for (int j = 0; j < totalCol; j++) {
                CustomGrid adapter = new CustomGrid(this, i, j);
                LinearLayout.LayoutParams gridParams;
                gridParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
                adapter.setLayoutParams(gridParams);
                adapter.setOnClickListener(this);
                adapter.setOnLongClickListener(this);
                grid[i][j] = adapter;
                rows[i].addView(adapter);
            }
        }
        placingMines();
        max = maxScore();
    }

    //Function to place mines at respective positions on the grid and assign scores to the grid positions
    private void placingMines() {
        mineCount=0;
        int randomMinePos[] = new int[mines];
        int index = 0;
        for (int i = 0; i < mines; i++) {
            randomMinePos[i] = (int) (Math.random() * totalCol);
        }
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < mine_row; j++) {
                int column_index = randomMinePos[index++];
                CustomGrid adapter = (CustomGrid) rows[i].getChildAt(column_index);
                adapter.setMine();
                grid[i][column_index].setMine();
                mineCount++;
            }
        }
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < totalCol; j++) {
                nearbyMines(i, j);
            }
        }
    }

    //Function to check non-mine bounds of the grid
    private boolean checkNonMineBound(int i, int j) {
        if (((i >= 0) && (i < totalRow)) && ((j >= 0) && (j < totalCol))) {
            return !grid[i][j].isMineButton();
        }
        return false;
    }

    //Function to check range of the total grid
    private boolean range(int i, int j) {
        return i >= 0 && i < totalRow && j >= 0 && j < totalCol;
    }

    //Function to get maximum score of the game
    private int maxScore() {
        int mS = 0;
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < totalCol; j++) {
                CustomGrid adapter = (CustomGrid) rows[i].getChildAt(j);
                mS = mS+ adapter.score;
            }
        }
        return mS;
    }

    //Function to check neighbouring grid positions for mines (flood-fill algorithm)
    private void nearbyMines(int i, int j) {
        if (!grid[i][j].isMineButton()) {
            CustomGrid adapter = (CustomGrid) rows[i].getChildAt(j);
            int count = 0;
            if (range(i - 1, j - 1)) {
                if (grid[i - 1][j - 1].isMineButton())
                    count++;
            }
            if (range(i - 1, j)) {
                if (grid[i - 1][j].isMineButton())
                    count++;
            }
            if (range(i - 1, j + 1)) {
                if (grid[i - 1][j + 1].isMineButton())
                    count++;
            }
            if (range(i, j - 1)) {
                if (grid[i][j - 1].isMineButton())
                    count++;
            }
            if (range(i, j + 1)) {
                if (grid[i][j + 1].isMineButton())
                    count++;
            }
            if (range(i + 1, j - 1)) {
                if (grid[i + 1][j - 1].isMineButton())
                    count++;
            }
            if (range(i + 1, j)) {
                if (grid[i + 1][j].isMineButton())
                    count++;
            }
            if (range(i + 1, j + 1)) {
                if (grid[i + 1][j + 1].isMineButton())
                    count++;
            }
            adapter.increaseScore(count);
        }
    }

    //Function to reveal the non-mine grid positions
    private int nonMineReveal(int i, int j) {
        CustomGrid adapter = (CustomGrid) rows[i].getChildAt(j);
        int sum = 0;
        if (adapter.score == 0 && !adapter.hasBeenRevealed())
        {
            adapter.reveal();
            adapter.show();
            if (checkNonMineBound(i - 1, j - 1)) {
                ((CustomGrid) rows[i - 1].getChildAt(j - 1)).show();
                sum=sum + nonMineReveal(i - 1, j - 1);
            }
            if (checkNonMineBound(i - 1, j)) {
                ((CustomGrid) rows[i - 1].getChildAt(j)).show();
                sum=sum + nonMineReveal(i - 1, j);
            }
            if (checkNonMineBound(i - 1, j + 1)) {
                ((CustomGrid) rows[i - 1].getChildAt(j + 1)).show();
                sum=sum + nonMineReveal(i - 1, j + 1);
            }
            if (checkNonMineBound(i, j - 1)) {
                ((CustomGrid) rows[i].getChildAt(j - 1)).show();
                sum=sum + nonMineReveal(i, j - 1);
            }
            if (checkNonMineBound(i, j + 1)) {
                ((CustomGrid) rows[i].getChildAt(j + 1)).show();
                sum=sum + nonMineReveal(i, j + 1);
            }
            if (checkNonMineBound(i + 1, j - 1)) {
                ((CustomGrid) rows[i + 1].getChildAt(j - 1)).show();
                sum=sum + nonMineReveal(i + 1, j - 1);
            }
            if (checkNonMineBound(i + 1, j)) {
                ((CustomGrid) rows[i + 1].getChildAt(j)).show();
                sum=sum + nonMineReveal(i + 1, j);
            }
            if (checkNonMineBound(i + 1, j + 1)) {
                ((CustomGrid) rows[i + 1].getChildAt(j + 1)).show();
                sum=sum + nonMineReveal(i + 1, j + 1);
            }
        }
        else if(adapter.score > 0 && !adapter.hasBeenRevealed()){
            adapter.reveal();
            adapter.show();
            sum=sum + adapter.score;
        }
        return sum;
    }

    //Function to reveal all mines when a user clicks on a mine position of the grid
    private void revealAllMine() {
        for (int i = 0; i < totalRow; i++) {
            for (int j = 0; j < totalCol; j++) {
                CustomGrid adapter = (CustomGrid) rows[i].getChildAt(j);
                if (adapter.isMineButton() && !adapter.hasBeenRevealed()) {
                    adapter.show();
                    adapter.reveal();
                }
            }
        }
        present_state = state_complete;
        if (score < max) {
            Toast lost=Toast.makeText(this, "You Lose!!", Toast.LENGTH_SHORT);
            lost.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
            LinearLayout lostLayout = (LinearLayout) lost.getView();
            TextView lostToast = (TextView) lostLayout.getChildAt(0);
            lostToast.setTextSize(30);
            lost.show();
            stopwatch.stop();
            MediaPlayer gameLost = MediaPlayer.create(getApplicationContext(), R.raw.gamelost);
            gameLost.start();
        }
    }

    //Function to check condition for the user to win/lose on clicking the grid
    @Override
    public void onClick(View view) {
        if (present_state == state_incomplete)
        {
            CustomGrid adapter = (CustomGrid) view;
            if(adapter.getText()=="F")
                return;
            int i = adapter.i;
            int j = adapter.j;
            if (adapter.isMineButton()) {
                adapter.reveal();
                adapter.show();
                revealAllMine();
            } else
            {
                score += nonMineReveal(i, j);
                if(score == max){
                    Toast won= Toast.makeText(this, "You WON!!", Toast.LENGTH_LONG);
                    won.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    LinearLayout wonLayout = (LinearLayout) won.getView();
                    TextView wonToast = (TextView) wonLayout.getChildAt(0);
                    wonToast.setTextSize(30);
                    won.show();
                    present_state = state_complete;
                    stopwatch.stop();
                    MediaPlayer gameWin = MediaPlayer.create(getApplicationContext(), R.raw.gamewin);
                    gameWin.start();
                }
            }
        }
    }

    //Function to toggle flag on the grid positions on long tap
    @Override
    public boolean onLongClick(View view) {
        if (present_state == state_incomplete)
        {
            CustomGrid adapter = (CustomGrid) view;
            adapter.setFlag();
        }
        return true;
    }

    //Function to take in input of back key button
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //Function to set up dialogue box on pressing back key
    protected void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit the game?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();
    }
}