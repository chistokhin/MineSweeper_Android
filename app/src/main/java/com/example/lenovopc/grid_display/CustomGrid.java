package com.example.lenovopc.grid_display;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.View;
import java.util.Locale;

public class CustomGrid extends AppCompatButton {
    Boolean checkMine;
    int i;
    int j;
    int score;
    boolean flag;
    boolean checkReveal;

    //Class for controlling grid
    public CustomGrid(Context context, int rowNumber, int colNumber){
        super(context);
        checkReveal = false;
        i = rowNumber;
        j = colNumber;
        flag = false;
        checkMine = false;
        score = 0;
        setText(" ");
        setTextColor(Color.BLACK);
        setTextSize(30);
        setGravity(Gravity.CENTER_VERTICAL);
        setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        setPadding(0,0,0,0);
    }

    //Method to check whether grid positions are revealed or not
    public boolean hasBeenRevealed(){
        return checkReveal;
    }

    //Function to confirm grid position reveal
    public void reveal(){
        checkReveal=true;
    }

    //Function to increase score of the user
    public void increaseScore(int sc){
        score=score + sc;
    }

    //Function to check whether the grid position has mine or not
    public Boolean isMineButton() {
        return checkMine;
    }

    //Set mine on the particular grid position
    public void setMine() {
        checkMine = true;
    }

    //Function to show all mine positions in the grid
    public void show() {
        setBackgroundColor(getResources().getColor(R.color.openBox));
        if(checkMine){
            setText("*");
            setTextColor(Color.RED);
            setTextSize(30);
            setTypeface(null,Typeface.BOLD);
        }
        else {
            if(score > 0)
                setText(String.format(Locale.ENGLISH, "%d", score));
            else
                setText(" ");
        }
        invalidate();
    }

    //Toggle flags for particular positions on the grid
    public void setFlag() {
        if(!this.hasBeenRevealed()){
            if(flag){
                setText(" ");
                flag = false;
            }
            else{
                setText("F");
                setTextColor(Color.BLACK);
                setTypeface(null,Typeface.BOLD_ITALIC);
                flag = true;
            }
        }
    }
}
