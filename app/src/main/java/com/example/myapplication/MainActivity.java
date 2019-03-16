package com.example.myapplication;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    public static TextInputLayout firstEdt;
    public static TextInputLayout secondEdt;
    public static TextInputEditText first_edt;
    public static TextInputEditText second_edt;
    public static AppCompatButton button;
    public static LinearLayout progressLayout;
    public static  CardView formCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstEdt = findViewById(R.id.first_edt);
        secondEdt = findViewById(R.id.second_edt);
        first_edt = findViewById(R.id.edt_one);
        second_edt = findViewById(R.id.edt_two);
        button = findViewById(R.id.third_btn);
        progressLayout = findViewById(R.id.progress_layout);
        formCard = findViewById(R.id.form_card);

        FetchData fetchData = new FetchData();
        fetchData.execute();


    }
}
