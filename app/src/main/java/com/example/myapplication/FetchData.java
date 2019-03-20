package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.example.myapplication.MainActivity.button;
import static com.example.myapplication.MainActivity.firstEdt;
import static com.example.myapplication.MainActivity.first_edt;
import static com.example.myapplication.MainActivity.parent;
import static com.example.myapplication.MainActivity.secondEdt;
import static com.example.myapplication.MainActivity.second_edt;
import static com.example.myapplication.MainActivity.submitLayout;


public class FetchData extends AsyncTask<Void, Void, Void> {

    private String data = "";
    private Model model;
    private Context mContext;
    private boolean isNumValid = false;
    private boolean isTextValid = false;

    FetchData(Context mContext) {
        this.mContext = mContext;
    }


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
            //assesment link https://ca.platform.simplifii.xyz/api/v1/static/assignment2
            URL apiUrl = new URL("https://api.myjson.com/bins/pozw6");
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
//                    model.setApi_uri(singleObject.getJSONObject("api").get("uri").toString());
//                    model.setApi_method(jsonObject1.get("method").toString());
//                    model.setAuthEnabled(Boolean.parseBoolean(jsonObject1.get("authEnabled").toString()));
                    Log.d("obj", "doInBackground: " + singleObject.getJSONObject("api"));
                    JSONObject obj = singleObject.getJSONObject("api");
                    Log.d("objuri", "doInBackground: " + obj.getString("uri"));
                    model.setApi_uri(obj.getString("uri"));
                    Log.d("wtf", "doInBackground: " + model.getApi_uri());

                    model.setApi_method(obj.getString("method"));
                    model.setAuthEnabled(Boolean.parseBoolean(obj.getString("authEnabled")));
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
        final String inputType = model.getInputType();

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
                if (!s.toString().isEmpty()) {
                    if (Integer.parseInt(s.toString()) < model.getMin() | Integer.parseInt(s.toString()) > model.getMax()) {
                        isNumValid = false;

                        first_edt.setError("Should be between " + model.getMin() + " and " + model.getMax());
                    } else {
                        isNumValid = true;
                    }
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
                if (inputType.equals("email")) {
                    if (!isValidEmail(s)) {
                        isTextValid = false;

                        second_edt.setError(model.getValidationMsg());
                    } else {
                        isTextValid = true;
                    }
                }else {
                    isTextValid = true;
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


    void postData() {

        if (Objects.requireNonNull(first_edt.getText()).toString().isEmpty() || Objects.requireNonNull(second_edt.getText()).toString().isEmpty() || !isNumValid || !isTextValid) {
            Snackbar.make(parent, Html.fromHtml("<font color=\"#FFFFFF\">Fields can't be empty</font>"), Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            try {
                AsyncHttpClient client = new AsyncHttpClient();
                // Http Request Params Object
                RequestParams params = new RequestParams();
                String first;
                String second;
                first = first_edt.getText().toString();
                second = second_edt.getText().toString();
                params.put(model.getLabel1(), first);
                params.put(model.getLabel2(), second);
                Log.d("uri", "postData: " + model.getApi_uri());
                Log.d("method", "postData: " + model.getApi_method());
                Log.d("auth", "postData: " + model.isAuthEnabled());
                client.post(model.getApi_uri(), params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {

                        showDialog();
                        //reset the UI
                        reset();
                        submitLayout.setVisibility(View.INVISIBLE);
                        Log.i("Response", "Response SP Status. " + response);
//                        Snackbar.make(parent, Html.fromHtml("<font color=\"#663096\">Form Submitted</font>"), Snackbar.LENGTH_SHORT)
//                                .show();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        super.onFailure(throwable);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reset() {
        first_edt.setText("");
        second_edt.setText("");
    }


    private void showDialog() {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.completion_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }


}

