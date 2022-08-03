package com.hottechnologies.onewood.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;


import com.hottechnologies.onewood.R;

public class FeedbackActivity extends AppCompatActivity {

    ImageView backtomain;
    EditText feedback;
    RatingBar rating;


    ImageView firststar, secondstar , thirdstar,fourstar,fifthstar;

    public int star = R.drawable.star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

//        rating = findViewById(R.id.rate);

//        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//
//            }
//        });

        backtomain = findViewById(R.id.backtodrawer);
        firststar = findViewById(R.id.firststar);
        secondstar = findViewById(R.id.secondstar);
        thirdstar = findViewById(R.id.thirdstar);
        fourstar = findViewById(R.id.fourthstar);
        fifthstar = findViewById(R.id.fifthstar);

        feedback = findViewById(R.id.feedbdack);
        feedback.computeScroll();
//        feedback.setScroller(new Scroller(FeedbackActivity.this));
//        feedback.setMaxLines(300);
//        feedback.setVerticalScrollBarEnabled(true);
//        feedback.setMovementMethod(new ScrollingMovementMethod());

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backtomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        firststar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firststar.setBackgroundResource(R.drawable.star);
                secondstar.setBackgroundResource(R.drawable.star);
                thirdstar.setBackgroundResource(R.drawable.star);
                fourstar.setBackgroundResource(R.drawable.star);
                fifthstar.setBackgroundResource(R.drawable.star);

                firststar.setBackgroundResource(R.drawable.clickstar);
            }
        });

        secondstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firststar.setBackgroundResource(R.drawable.star);
                secondstar.setBackgroundResource(R.drawable.star);
                thirdstar.setBackgroundResource(R.drawable.star);
                fourstar.setBackgroundResource(R.drawable.star);
                fifthstar.setBackgroundResource(R.drawable.star);

                firststar.setBackgroundResource(R.drawable.clickstar);
                secondstar.setBackgroundResource(R.drawable.clickstar);
            }
        });

        thirdstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firststar.setBackgroundResource(R.drawable.star);
                secondstar.setBackgroundResource(R.drawable.star);
                thirdstar.setBackgroundResource(R.drawable.star);
                fourstar.setBackgroundResource(R.drawable.star);
                fifthstar.setBackgroundResource(R.drawable.star);

                firststar.setBackgroundResource(R.drawable.clickstar);
                secondstar.setBackgroundResource(R.drawable.clickstar);
                thirdstar.setBackgroundResource(R.drawable.clickstar);
            }
        });

        fourstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firststar.setBackgroundResource(R.drawable.star);
                secondstar.setBackgroundResource(R.drawable.star);
                thirdstar.setBackgroundResource(R.drawable.star);
                fourstar.setBackgroundResource(R.drawable.star);
                fifthstar.setBackgroundResource(R.drawable.star);

                firststar.setBackgroundResource(R.drawable.clickstar);
                secondstar.setBackgroundResource(R.drawable.clickstar);
                thirdstar.setBackgroundResource(R.drawable.clickstar);
                fourstar.setBackgroundResource(R.drawable.clickstar);
            }
        });

        fifthstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firststar.setBackgroundResource(R.drawable.star);
                secondstar.setBackgroundResource(R.drawable.star);
                thirdstar.setBackgroundResource(R.drawable.star);
                fourstar.setBackgroundResource(R.drawable.star);
                fifthstar.setBackgroundResource(R.drawable.star);

                firststar.setBackgroundResource(R.drawable.clickstar);
                secondstar.setBackgroundResource(R.drawable.clickstar);
                thirdstar.setBackgroundResource(R.drawable.clickstar);
                fourstar.setBackgroundResource(R.drawable.clickstar);
                fifthstar.setBackgroundResource(R.drawable.clickstar);
            }
        });

    }
}