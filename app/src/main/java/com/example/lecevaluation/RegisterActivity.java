package com.example.lecevaluation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final  String URL_FOR_REGISTRATION = "http://192.168.42.247/android_login/register.php";
    ProgressDialog progressDialog;

    private EditText SignupInputFirstName,SignupInputLastName,SignupInputRegNo, SignupInputEmail, SignupInputPassword;
    private Button btnSignup;
    private Button btnLinkLogin;
    private RadioGroup genderRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //To avoid auto popping of the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //process Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        SignupInputFirstName = (EditText) findViewById(R.id.signup_input_FirstName);
        SignupInputLastName = (EditText) findViewById(R.id.signup_input_LastName);
        SignupInputRegNo = (EditText) findViewById(R.id.signup_input_regNo);
        SignupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        SignupInputPassword = (EditText) findViewById(R.id.signup_input_password);

        btnLinkLogin = (Button) findViewById(R.id.btn_link_login);
        btnSignup = (Button) findViewById(R.id.btn_signup);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });
        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void submitForm(){

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if (selectedId == R.id.female_radio_btn)
                gender = "Female";
        else
            gender = "Male";

        registerUser(
                SignupInputFirstName.getText().toString(),
                SignupInputLastName.getText().toString(),
                SignupInputRegNo.getText().toString(),
                SignupInputEmail.getText().toString(),
                gender,
                SignupInputPassword.getText().toString());
    }

    private void registerUser(final String first_name, final String last_name,final String reg_no,
                              final String email, final String password, final String gender){
        //Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Registering you...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("first_name");
                        Toast.makeText(getApplicationContext(), "Hello " + user + ", You are successfully Registered!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                MainActivity.class);
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
                Log.e(TAG,"Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("first_name", first_name);
                params.put("last_name", last_name);
                params.put("reg_no", reg_no);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                return params;
            }
        };
        //Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog(){
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog(){
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
