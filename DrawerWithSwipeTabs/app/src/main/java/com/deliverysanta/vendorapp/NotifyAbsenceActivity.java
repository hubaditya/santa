package com.deliverysanta.vendorapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class NotifyAbsenceActivity extends AppCompatActivity {
    Button b1;
    RadioGroup rg1;
    RadioButton rb1;
    TextView tv1,tv2,tv3;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    static final int FROM_DIALOG_ID = 1;
    static final int TO_DIALOG_ID = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_absence);
        b1=(Button)findViewById(R.id.button);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        tv1=(TextView)findViewById(R.id.textView7); //single date
        tv2=(TextView)findViewById(R.id.textView10); //from date
        tv3=(TextView)findViewById(R.id.textView11); //to date
        rg1=(RadioGroup)findViewById(R.id.radioGroup);

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(FROM_DIALOG_ID);
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TO_DIALOG_ID);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = rg1.getCheckedRadioButtonId();
                rb1 = (RadioButton) findViewById(selectedId);
                if(selectedId==0){
                    tv1.getText().toString();
                }
                else if(selectedId==1)
                {
                    tv2.getText().toString();
                    tv3.getText().toString();
                }
                Intent intent=new Intent(NotifyAbsenceActivity.this,MainActivity.class);
                startActivity(intent);
                Toast.makeText(v.getContext(), "ABSENCE NOTIFIED.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notify_absence, menu);
        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
            case FROM_DIALOG_ID:
                return new DatePickerDialog(this,
                        mFromSetListener,
                        mYear, mMonth, mDay);

            case TO_DIALOG_ID:
                return new DatePickerDialog(this,
                        mToSetListener,
                        mYear, mMonth, mDay);

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    tv1.setText(
                            new StringBuilder()
                                    // Month is 0 based so add 1
                                    .append(mDay).append("-")
                                    .append(mMonth+1).append("-")
                                    .append(mYear).append(" "));
                }
            };

    private DatePickerDialog.OnDateSetListener mFromSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    tv2.setText(
                            new StringBuilder()
                                    // Month is 0 based so add 1
                                    .append(mDay).append("-")
                                    .append(mMonth + 1).append("-")
                                    .append(mYear).append(" "));
                }
            };

    private DatePickerDialog.OnDateSetListener mToSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    tv3.setText(
                            new StringBuilder()
                                    // Month is 0 based so add 1
                                    .append(mDay).append("-")
                                    .append(mMonth+1).append("-")
                                    .append(mYear).append(" "));
                }
            };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}