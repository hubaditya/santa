package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    TextInputLayout emailRegister, passwordRegister;
    EditText etEmailRegister, etPasswordRegister;
    Button registerButton;

    SessionManager session;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.register_button);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        registerButton.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
    }

    private void registerUser(final String email, final String password)
    {
        final JSONClasses ob =new JSONClasses();

        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    String message = jObj.getString("message");

                    if (message.equals("Admin created"))
                    {
                        session.setLogin(true);
                        ob.token = jObj.getString("token");

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("username", email);
                params.put("password", password);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v)
    {
        String email = etEmailRegister.getText().toString();
        String password = etPasswordRegister.getText().toString();

        if (!email.isEmpty() && !password.isEmpty()) {
            registerUser(email, password);
        }

        else {
            Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG).show();
        }
    }
}
