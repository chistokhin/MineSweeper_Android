package com.example.lenovopc.grid_display;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class homeScreenActivity extends AppCompatActivity {

    //Function to create the homescreen layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        TextView name=(TextView)findViewById(R.id.name);
        name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.heading_home,0,0,0);
        Animation translatebu= AnimationUtils.loadAnimation(this, R.anim.animate_home);
        name.startAnimation(translatebu);
        Button beginnerbutton=(Button)findViewById(R.id.beginnerbutton);
        Button intermediatebutton=(Button)findViewById(R.id.intermediatebutton);
        Button advancedbutton=(Button)findViewById(R.id.advancedbutton);

        //Function to call beginner game layout on clicking beginner button
        beginnerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openbeginner=new Intent(view.getContext(),beginnerScreenActivity.class);
                startActivityForResult(openbeginner,0);
            }
        });

        //Function to call intermediate game layout on clicking intermediate button
        intermediatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openintermediate=new Intent(view.getContext(),intermediateScreenActivity.class);
                startActivityForResult(openintermediate,0);
            }
        });

        //Function to call advanced game layout on clicking advanced button
        advancedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openadvanced=new Intent(view.getContext(),advancedScreenActivity.class);
                startActivityForResult(openadvanced,0);
            }
        });
    }
}
