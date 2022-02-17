package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{

    TextView registerHere;
    TextView signIn;
    String tod,amount,deliv,totalamount,collegeName;
    TextInputLayout emailLogin;
    TextInputLayout passwordLogin;
    EditText etEmailLogin;
    EditText etPasswordLogin;
    private int st, backp;
    private String inst, contactNo;
    private Bundle extras;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private String clgName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        extras = getIntent().getExtras();
        if(extras!=null)
        {
            tod = extras.getString("tod");
            amount=extras.getString("amount");
            deliv=extras.getString("deliv");
            totalamount=extras.getString("totalamount");
            inst=extras.getString("specialInst");
            contactNo = extras.getString("contact");
            collegeName = extras.getString("collegeName");

            st=1;
        }

        else backp=1;

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        registerHere=(TextView)findViewById(R.id.registerhere_button);
        signIn=(TextView)findViewById(R.id.signin_button);
        emailLogin=(TextInputLayout)findViewById(R.id.email_loginlayout);
        passwordLogin=(TextInputLayout)findViewById(R.id.password_loginlayout);
        etEmailLogin = (EditText) findViewById(R.id.email_login);
        etPasswordLogin = (EditText) findViewById(R.id.password_login);
        registerHere.setOnClickListener(this);
        signIn.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn() && st==1)
        {
            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
            intent.putExtra("tod",tod);
            intent.putExtra("amount",amount);
            intent.putExtra("deliv",deliv);
            intent.putExtra("totalamount",totalamount);
            intent.putExtra("specialInst",inst);
            intent.putExtra("contact", contactNo);
            startActivity(intent);
            finish();
        }
    }

    private void checkLogin(final String email, final String password)
    {
        final JSONClasses ob =new JSONClasses();

        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);

                    String message= jObj.getString("message");


                    if (message.equals("Successfully login"))
                    {
                        session.setLogin(true);
                        String token = jObj.getString("token");
                        ob.token = token;
                        getCollegeName(token);
                        AppController.setList(LoginActivity.this, "token", ob);

                    }

                    else if(message.equals("Invalid Email/Password")) {
                        Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
                    }

                    else if(message.equals("User does not exist")) {
                        Toast.makeText(getApplicationContext(), "This contact no is not registered.", Toast.LENGTH_LONG).show();
                    }

                    else if(message.equals("User is not verified"))
                    {
                        final String phn = jObj.getString("contactNo");
                        final String mailID = jObj.getString("email");
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Verification");
                        alert.setMessage("Enter OTP");
                        final EditText otp = new EditText(LoginActivity.this);
                        otp.setInputType(InputType.TYPE_CLASS_NUMBER);
                        otp.setRawInputType(Configuration.KEYBOARD_12KEY);
                        otp.setTextColor(Color.BLACK);
                        alert.setView(otp);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String srt = otp.getEditableText().toString();
                                verifyOtp(phn, srt, mailID);
                            }
                        });
                        alert.setNegativeButton("RESEND OTP", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                showToast();
                                resendOtp(phn, mailID);
                            }
                        });
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                    }
                }

                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("contactNo", email);
                params.put("password", password);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void getCollegeName(final String token)
    {
        final JSONClasses ob = new JSONClasses();

        final String tag_string_req = "req_colleges";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ME, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONObject jsonobject = new JSONObject(response);
                    String college = jsonobject.getString("college");
                    clgName = college;

                    if(st==1)
                    {
                        if(collegeName.equals(clgName))
                        {
                            ob.name = jsonobject.getString("name");
                            ob.email = jsonobject.getString("email");

                            AppController.setList(getApplicationContext(), "name", ob);
                            AppController.setList(getApplicationContext(), "email", ob);

                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            intent.putExtra("tod",tod);
                            intent.putExtra("amount",amount);
                            intent.putExtra("deliv",deliv);
                            intent.putExtra("totalamount",totalamount);
                            intent.putExtra("specialInst",inst);
                            intent.putExtra("contact", contactNo);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Please select your college.",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(LoginActivity.this, CollegeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("token", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showToast() {
        Toast.makeText(this, "Resending OTP", Toast.LENGTH_SHORT).show();
    }
    public void verifyOtp(final String contactNo, final String value, final String mailID)
    {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFY, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    JSONObject jObj = new JSONObject(response);

                    String number = jObj.getString("nModified");

                    if (number.equals("1")) {
                        loginSuccess();
                    }
                    else {
                        loginUnsuccess();
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("contactNo", contactNo);
                params.put("value", value);
                params.put("email", mailID);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void resendOtp(final String phn, final String mailID)
    {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RESEND, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try {

                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("contactNo", phn);
                params.put("email", mailID);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void loginUnsuccess() {
        Toast.makeText(this, "Please enter valid OTP to login", Toast.LENGTH_LONG).show();
    }

    private void loginSuccess() {
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
    }

    private void showDialog()
    {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog()
    {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.registerhere_button:
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                if(st==1)
                {
                        intent.putExtra("tod",tod);
                        intent.putExtra("amount",amount);
                        intent.putExtra("deliv",deliv);
                        intent.putExtra("totalamount",totalamount);
                        intent.putExtra("specialInst",inst);
                        intent.putExtra("contact", contactNo);
                }
                    startActivity(intent);
                break;

            case R.id.signin_button:
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                if(email.length()!=10){
                    Toast.makeText(LoginActivity.this,"Please enter a valid no.",Toast.LENGTH_LONG).show();
                }
                if (email.trim().length() > 0 && password.trim().length() > 0) {
                    checkLogin(email, password);
                }

                else {
                    Snackbar.make(v, "Please enter the credentials!", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed(){
        if(backp==1) {
            Intent i = new Intent(LoginActivity.this, CollegeActivity.class);
            startActivity(i);
            finish();
        }
        else super.onBackPressed();
    }
}