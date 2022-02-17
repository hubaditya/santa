package com.delivery.santa.deliver;

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
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    Button signIn;
    TextInputLayout nameLogin;
    TextInputLayout passwordLogin;
    EditText etNameLogin;
    EditText etPasswordLogin;

    JSONClasses ob;
    private ProgressDialog progressDialog;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        signIn = (Button) findViewById(R.id.signin_button);
        nameLogin = (TextInputLayout) findViewById(R.id.name_loginlayout);
        passwordLogin = (TextInputLayout) findViewById(R.id.password_loginlayout);
        etNameLogin = (EditText) findViewById(R.id.name_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
            startActivity(intent);
            finish();
        }

        signIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void checkLogin(final String name, final String password)
    {
        ob = new JSONClasses();

        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if(message.equals("Successfully login"))
                    {
                        ob.token = jsonObject.getString("token");
                        AppController.setList(LoginActivity.this, "token", ob);
                        session.setLogin(true);

                        Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                        startActivity(intent);
                        finish();
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "Login Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }

                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("name", name);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


    @Override
    public void onClick(View v)
    {
        String name = etNameLogin.getText().toString();
        String password = etPasswordLogin.getText().toString();


        if (name.trim().length() > 0 && password.trim().length() > 0) {
            checkLogin(name, password);
        }

        else {
            Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onBackPressed()
    {
        System.exit(0);
    }
}

