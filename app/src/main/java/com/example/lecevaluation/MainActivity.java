package com.example.lecevaluation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String URL_FOR_LOGIN = "http://192.168.168.1/android_login/login.php";


    ProgressDialog progressDialog;
    private EditText loginInputregNo, loginInputPassword;
    private Button btnlogin;
    private Button btnLinkSignup;
    TextView textView;
    int counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInputregNo =  findViewById(R.id.login_input_regNo);
        loginInputPassword = findViewById(R.id.login_input_password);
        btnlogin =  findViewById(R.id.btn_login);
        btnLinkSignup =  findViewById(R.id.btn_link_signup);
        textView = findViewById(R.id.textView3);
        textView.setVisibility(View.GONE);

        //Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        //To avoid auto popping of the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(loginInputregNo.getText().toString(),
                        loginInputPassword.getText().toString());

                textView.setVisibility(View.VISIBLE);
                textView.setBackgroundColor(Color.RED);
                counter--;
                textView.setText(Integer.toString(counter));

                if (counter == 0) {
                    btnlogin.setEnabled(false);
                }
            }
        });

        btnLinkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginUser(final String reg_no, final String password){
        //Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error){
                        String User = jObj.getJSONObject("user").getString("reg_no");

                        /**
                        * Launch User Activity
                        * */

                        Intent intent = new Intent(
                                MainActivity.this,
                                UserActivity.class);
                        intent.putExtra("reg_no" //or regNo or username
                                , User);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("reg_no", reg_no);
                params.put("password", password);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}

