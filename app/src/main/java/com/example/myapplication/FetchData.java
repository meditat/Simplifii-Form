package com.example.myapplication;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static com.example.myapplication.MainActivity.button;
import static com.example.myapplication.MainActivity.firstEdt;
import static com.example.myapplication.MainActivity.first_edt;
import static com.example.myapplication.MainActivity.secondEdt;
import static com.example.myapplication.MainActivity.second_edt;


public class FetchData extends AsyncTask<Void, Void, Void> {

    private String data = "";
    private Model model;

    @Override
    protected Void doInBackground(Void... voids) {
        //Sleep the thread for 5 secs
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        model = new Model();
        try {
            // my Json server url /https://api.myjson.com/bins/rtx6m
            URL apiUrl = new URL("https://ca.platform.simplifii.xyz/api/v1/static/assignment2");
            HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";

            while (line != null) {

                line = reader.readLine();
                data = data + line;
            }

            JSONObject jsonObject = new JSONObject(data);
//            Log.d("Something", "doInBackground: "
//            + jsonObject.get("response"));
            JSONObject da = (JSONObject) jsonObject.get("response");
            JSONArray jsonArray = da.getJSONArray("data");
            Log.d("not good", "doInBackground: " + da.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d("lets" + i, "doInBackground: " + jsonArray.get(i));
                JSONObject singleObject = jsonArray.getJSONObject(i);
                if (i == 0) {
                    model.setType1(singleObject.get("type").toString());
                    model.setLabel1(singleObject.get("label").toString());
                    model.setName1(singleObject.get("name").toString());
                    model.setMin(Integer.parseInt(singleObject.get("min").toString()));
                    model.setMax(Integer.parseInt(singleObject.get("max").toString()));
                    model.setIntervals(Integer.parseInt(singleObject.get("intervals").toString()));
                }
                if (i == 1) {
                    model.setType2(singleObject.get("type").toString());
                    model.setLabel2(singleObject.get("label").toString());
                    model.setName2(singleObject.get("name").toString());
                    model.setInputType(singleObject.get("inputType").toString());
                    JSONArray jsonArray1 = singleObject.getJSONArray("validations");
                    JSONObject jsonObject1 = (JSONObject) jsonArray1.get(0);
                    model.setValidationName(jsonObject1.get("name").toString());
                    model.setValidationMsg(jsonObject1.get("message").toString());
                }
                if (i == 2) {
                    model.setType3(singleObject.get("type").toString());
                    model.setAction(singleObject.get("action").toString());
                    model.setLabel3(singleObject.get("label").toString());
                    JSONObject jsonObject1 = (JSONObject) singleObject.get("api");
                    model.setApi_uri(jsonObject1.get("ui").toString());
                    model.setApi_method(jsonObject1.get("method").toString());
                    model.setAuthEnabled(Boolean.parseBoolean(jsonObject1.get("authEnabled").toString()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity.formCard.setVisibility(View.VISIBLE);
        MainActivity.progressLayout.setVisibility(View.INVISIBLE);

        //set labels
        firstEdt.setHint(model.getLabel1());
        if (model.getValidationName().equals("required")) {
            secondEdt.setHint(model.getLabel2() + "*");
        } else {
            secondEdt.setHint(model.getLabel2());

        }
        button.setText(model.getLabel3());

        //set input types
        first_edt.setInputType(InputType.TYPE_CLASS_NUMBER);
        String inputType = model.getInputType();

        Log.d("input", "onPostExecute: " + inputType);
        if (inputType.equals("password")) {
            second_edt.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            second_edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        if (inputType.equals("email"))
            second_edt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        else
            second_edt.setInputType(InputType.TYPE_CLASS_TEXT);


        //check conditions
        first_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Integer.parseInt(s.toString()) < model.getMin() | Integer.parseInt(s.toString()) > model.getMax()) {
                    first_edt.setError("Should be between " + model.getMin() + " and " + model.getMax());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        second_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidEmail(s)) {
                    second_edt.setError(model.getValidationMsg());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        super.onPostExecute(aVoid);
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
