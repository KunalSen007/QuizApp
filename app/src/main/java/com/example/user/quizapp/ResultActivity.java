package com.example.user.quizapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView result, rating;
    Button btnRestart;
    String rate;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = (TextView)findViewById(R.id.marks);
        rating = (TextView)findViewById(R.id.rating);

        btnRestart = (Button) findViewById(R.id.btnRestart);

        result.setText(QuestionActivity.correct + "/20");
        if(QuestionActivity.correct<=10)
        {
            rate="Needs Improvement";
        }
        else if(QuestionActivity.correct<=14 && QuestionActivity.correct>10)
        {
            rate="Good";
        }
        else if(QuestionActivity.correct<=18 && QuestionActivity.correct>14)
        {
            rate="Excellent";
        }
        else if(QuestionActivity.correct<=20 && QuestionActivity.correct>18)
        {
            rate="Outstanding";
        }
        rating.setText("Ratings : "+rate);

        QuestionActivity.correct=0;
        QuestionActivity.wrong=0;

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),GuideLines.class);
                startActivity(in);
            }
        });
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ResultActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customdialog, viewGroup, false);
        builder.setView(dialogView);

        alertDialog = builder.create();
        alertDialog.show();
        Button ok=(Button)alertDialog.findViewById(R.id.buttonOk);
        Button cancel=(Button)alertDialog.findViewById(R.id.buttonCancel);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}
