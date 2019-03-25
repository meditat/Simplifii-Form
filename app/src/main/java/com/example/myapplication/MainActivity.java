package com.example.myapplication;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
public class MainActivity extends AppCompatActivity {

    public static TextInputLayout firstEdt;
    public static TextInputLayout secondEdt;
    public static TextInputEditText first_edt;
    public static TextInputEditText second_edt;
    public static AppCompatButton button;
    public static LinearLayout progressLayout;
    public static LinearLayout submitLayout;
    public static  CardView formCard;
    public static RelativeLayout parent;

    private Model model;
    FetchData fetchData;

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
        parent = findViewById(R.id.parent);
        submitLayout = findViewById(R.id.submit_layout);

        model = new Model();

        fetchData = new FetchData(this);
        fetchData.execute();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                first_edt.clearFocus();
                second_edt.clearFocus();

                //this thing  is temporary
                if (!model.isAuthEnabled()) {
                    submitLayout.setVisibility(View.VISIBLE);
                    fetchData.postData();
                } else {
                    Snackbar snackbar = Snackbar.make(parent, Html.fromHtml("<font color=\"#FFFFFF\">Login To Continue</font>"), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });


    }


    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

}
