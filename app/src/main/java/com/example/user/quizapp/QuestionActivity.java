package com.example.user.quizapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class QuestionActivity extends AppCompatActivity {
    TextView tv;
    Button submitbutton, quitbutton;
    AlertDialog alertDialog;
    ProgressDialog progressDialog;
    RadioGroup radio_g;
    RadioButton rb1,rb2,rb3,rb4;
    String filename="questions.json";
    Options optionsList;
    ArrayList<Options> options;
    String questions[] ;
    String answers[] ;
    int flag=0;
    public static int marks=0,correct=0,wrong=0;
    TextView questionNumber,timerView;
    String selectedAnswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionNumber=(TextView)findViewById(R.id.QuestionNumber);
        Intent intent = getIntent();
        timerView=(TextView) findViewById(R.id.timerView);
        submitbutton=(Button)findViewById(R.id.button3);
        quitbutton=(Button)findViewById(R.id.buttonquit);
        tv=(TextView) findViewById(R.id.tvque);

        radio_g=(RadioGroup)findViewById(R.id.answersgrp);
        rb1=(RadioButton)findViewById(R.id.radioButton);
        rb2=(RadioButton)findViewById(R.id.radioButton2);
        rb3=(RadioButton)findViewById(R.id.radioButton3);
        rb4=(RadioButton)findViewById(R.id.radioButton4);

        new QuizAPI().execute();
        new CountDownTimer(300000, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = String.format(Locale.getDefault(), "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                timerView.setText(text);

            }

            public void onFinish() {
                timerView.setText("TIME OVER");
                try {
                    RadioButton selectedRb = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                    selectedAnswer = selectedRb.getText().toString();

                    if (selectedAnswer.equals(answers[flag])) {
                        correct++;

                    } else {
                        wrong++;

                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                progressDialog = new ProgressDialog(QuestionActivity.this);
                progressDialog.setMax(20);
                progressDialog.setMessage("Checking answers....");
                progressDialog.setTitle("Submitting");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                        startActivity(in);
                    }


                });
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDialog.getProgress() <= progressDialog
                                    .getMax()) {
                                Thread.sleep(200);
                                handle.sendMessage(handle.obtainMessage());
                                if (progressDialog.getProgress() == progressDialog
                                        .getMax()) {

                                    progressDialog.dismiss();


                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                }
            Handler handle = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    progressDialog.incrementProgressBy(1);
                }
            };

        }.start();
        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedRb = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                selectedAnswer = selectedRb.getText().toString();

                if(selectedAnswer.equals(answers[flag])) {
                    correct++;

                }
                else {
                    wrong++;

                }

                flag++;

                /*if (score != null)
                    score.setText(""+correct);*/

                if(flag<questions.length)
                {
                    tv.setText(questions[flag]);
                    questionNumber.setText("Question "+(flag+1));
                    rb1.setText(options.get(flag).getOption1());
                    rb2.setText(options.get(flag).getOption2());
                    rb3.setText(options.get(flag).getOption3());
                    rb4.setText(options.get(flag).getOption4());
                    if((flag+1)==questions.length)
                    {
                        submitbutton.setText("SUBMIT");
                    }
                }
                else
                {
                    marks=correct;

                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                    ViewGroup viewGroup = findViewById(android.R.id.content);

                    View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customdialogsubmit, viewGroup, false);
                    builder.setView(dialogView);

                    alertDialog = builder.create();
                    alertDialog.show();
                    Button ok=(Button)alertDialog.findViewById(R.id.buttonOk);
                    Button cancel=(Button)alertDialog.findViewById(R.id.buttonCancel);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            progressDialog = new ProgressDialog(QuestionActivity.this);
                            progressDialog.setMax(20);
                            progressDialog.setMessage("Checking answers....");
                            progressDialog.setTitle("Submitting");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                            progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Intent in = new Intent(getApplicationContext(),ResultActivity.class);
                                    startActivity(in);
                                }


                            });
                            progressDialog.show();
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        while (progressDialog.getProgress() <= progressDialog
                                                .getMax()) {
                                            Thread.sleep(200);
                                            handle.sendMessage(handle.obtainMessage());
                                            if (progressDialog.getProgress() == progressDialog
                                                    .getMax()) {

                                                progressDialog.dismiss();


                                            }

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }

                        Handler handle = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                progressDialog.incrementProgressBy(1);
                            }
                        };








                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });


                }
                radio_g.clearCheck();
            }
        });

        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
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
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
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


    public class QuizAPI extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            options=new ArrayList<Options>();

        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                JSONObject extractJson=new JSONObject(readJSONFromAsset(filename));
               JSONObject quiz=extractJson.getJSONObject("quiz");
                int i=0;
                int k=1;
                int j=0;

                int resultSize=quiz.length();

                questions=new String[resultSize];
                answers=new String[resultSize];

                for (i = 0; i < resultSize; i++) {
                    JSONObject c = quiz.getJSONObject("q"+k);
                    questions[i]=c.getString("question");
                    JSONArray answer=c.getJSONArray("options");
                    optionsList = new Options();
                    optionsList.setOption1(answer.getString(0));
                    optionsList.setOption2(answer.getString(1));
                    optionsList.setOption3(answer.getString(2));
                    optionsList.setOption4(answer.getString(3));
                    options.add(optionsList);

                    answers[i]=c.getString("answer");
                    k++;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            tv.setText(questions[flag]);
            questionNumber.setText("Question "+(flag+1));

            rb1.setText(options.get(flag).getOption1());
            rb2.setText(options.get(flag).getOption2());
            rb3.setText(options.get(flag).getOption3());
            rb4.setText(options.get(flag).getOption4());
        }
    }
    public String readJSONFromAsset(String fileName)
    {
        String json = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
