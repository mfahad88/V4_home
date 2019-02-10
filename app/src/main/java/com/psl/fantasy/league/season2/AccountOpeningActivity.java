package com.psl.fantasy.league.season2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.R;;
import com.psl.transport.Connection;

import java.util.Calendar;

public class AccountOpeningActivity extends AppCompatActivity {

    String authHeader,strCNIC,strFatherName,strCNICStatus,strAccTitle,strMobNmbr,strOTP,name, presentAddress, gender,expiryDate,motherName,dateOfBirth,strPlaceofbirth;;
    EditText txtCNIC,txtMobNmbr,txtUserAccTitle,txtUserFullName,txtFatherName, txtpresentAddress, txtgender,txtexpiryDate,txtmotherName,txtdateOfBirth,txtPlaceofbirth;
    RadioButton rbgMale,rbgFemale;
    DatePickerDialog datePickerDialog;
    Button proceedBtn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_opening);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);

        try {
                Intent intnt = getIntent();
                strCNIC = intnt.getStringExtra("strCNIC");
                strMobNmbr = intnt.getStringExtra("strMobNmbr");
                strOTP = intnt.getStringExtra("OTP");
                presentAddress = intnt.getStringExtra("presentAddress");
                gender = intnt.getStringExtra("gender");
                expiryDate = intnt.getStringExtra("expiryDate");
                motherName = intnt.getStringExtra("motherName");
                dateOfBirth = intnt.getStringExtra("dateOfBirth");
                strFatherName=intnt.getStringExtra("strFatherName");
                strPlaceofbirth = intnt.getStringExtra("strPlaceofbirth");
                name = intnt.getStringExtra("name");
                authHeader=intnt.getStringExtra("AUTH");
                strCNICStatus=intnt.getStringExtra("strCNICStatus");
                txtUserFullName = (EditText) findViewById(R.id.txtUserFullName);
                txtdateOfBirth = (EditText) findViewById(R.id.txtUserDOB);
                txtCNIC = (EditText) findViewById(R.id.txtUserCNIC);
                txtMobNmbr = (EditText) findViewById(R.id.txtUserMobNumber);
                txtpresentAddress= (EditText) findViewById(R.id.txtPresentAddress);
                txtmotherName= (EditText) findViewById(R.id.txtMotherName);
                txtdateOfBirth= (EditText) findViewById(R.id.txtUserDOB);
                txtPlaceofbirth= (EditText) findViewById(R.id.txtBirthPlace);
                txtUserAccTitle=(EditText)findViewById(R.id.txtUserAccTitle);
                txtFatherName=(EditText)findViewById(R.id.txtFatherName);

                txtdateOfBirth .setText(dateOfBirth);

                txtUserFullName.setText(name);
                //txtUserAccTitle.setText(name);
                txtCNIC.setText(strCNIC) ;
                txtMobNmbr.setText(strMobNmbr) ;
                txtpresentAddress .setText(presentAddress);
                txtmotherName.setText(motherName);
                txtPlaceofbirth.setText(strPlaceofbirth);
                txtFatherName.setText(strFatherName);

            if( ! name.trim().equals("")) txtUserFullName.setVisibility(View.GONE);
            if( ! strFatherName.trim().equals("")) txtFatherName.setVisibility(View.GONE);
            if( ! motherName.trim().equals("")) txtmotherName.setVisibility(View.GONE);
            if( ! presentAddress.trim().equals("")) txtpresentAddress.setVisibility(View.GONE);
            if( ! strPlaceofbirth.trim().equals("")) txtPlaceofbirth.setVisibility(View.GONE);
            if( ! dateOfBirth.trim().equals("")) txtdateOfBirth.setVisibility(View.GONE);





                proceedBtn=(Button)findViewById(R.id.btnNext);

                rbgMale=(RadioButton)findViewById(R.id.rbMale);
                rbgFemale=(RadioButton)findViewById(R.id.rbFemale);

                if(gender.trim().toLowerCase().equals("male")) {
                    findViewById(R.id.textView6).setVisibility(View.GONE);
                    ((View) rbgMale.getParent()).setVisibility(View.GONE);
                    rbgMale.setChecked(true);
                }
                else if(gender.toLowerCase().equals("female")) {
                    findViewById(R.id.textView6).setVisibility(View.GONE);
                    ((View) rbgMale.getParent()).setVisibility(View.GONE);

                    rbgFemale.setChecked(true);
                }
            else {
                    findViewById(R.id.textView6).setVisibility(View.VISIBLE);
                    ((View) rbgMale.getParent()).setVisibility(View.VISIBLE);
                }


                rbgFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked)
                        gender="female";
                    }
                });
            rbgMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                        gender="male";
                }
            });
        }
            catch (Exception e){
                 e.printStackTrace();
            }


            txtdateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDatePicker();
            }
        });
            // txtUserDOB.setEnabled(false);
            txtdateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

               // if(hasFocus)
                //ShowDatePicker();
            }
        });

        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception e){}
                name=txtUserFullName.getText().toString().trim();
                presentAddress=txtpresentAddress.getText().toString().trim();
                motherName=txtmotherName.getText().toString().trim();
                strAccTitle=txtUserAccTitle.getText().toString().trim();
                strFatherName=txtFatherName.getText().toString().trim();
                strPlaceofbirth=txtPlaceofbirth.getText().toString().trim();
                if(validate())
                {
                    new MyAyncTaskSendUserData().execute();
                }
            }
        });

    }

    boolean validate()
    {
        if(strCNIC.trim().length()!=13)
        {
            showAlert2("CNIC is not valid","Invalid Information");
            txtCNIC.requestFocus();
            return  false;
        }
        if(strMobNmbr.trim().length()!=11)
        {
            showAlert2("Mobile Number is not valid","Invalid Information");
            txtMobNmbr.requestFocus();
            return  false;
        }
        if(name.trim().length()<=4)
        {
            showAlert2("Your Full Name is too short","Invalid Information");
            txtUserFullName.requestFocus();
            return  false;
        }
        if(strAccTitle.trim().length()<=4)
        {
            showAlert2("Account Title is too short","Invalid Information");
            txtUserAccTitle.requestFocus();
            return  false;
        }
        if(strFatherName.trim().length()<=4)
        {
            showAlert2("Father Name is too short","Invalid Information");
            txtFatherName.requestFocus();
            return  false;
        }
        if(motherName.trim().length()<3)
        {
            showAlert2("Mother's name is too short","Invalid Information");
            txtpresentAddress.requestFocus();
            return  false;
        }
        if(strPlaceofbirth.trim().length()<3)
        {
            showAlert2("Place of Birth is too short","Invalid Information");
            txtPlaceofbirth.requestFocus();
            return  false;
        }
        if(presentAddress.trim().length()<3)
        {
            showAlert2("Your address is too short","Invalid Information");
            txtpresentAddress.requestFocus();
            return  false;
        }
        if(gender.length()<4)
        {
            showAlert2("Gender is not valid","Invalid Information");
            return  false;
        }


        return true;
    }
    public void ShowDatePicker() {


            // Get Current Date
            final Calendar c = Calendar.getInstance();
           int mYear = c.get(Calendar.YEAR);
           int mMonth = c.get(Calendar.MONTH);
           int mDay = c.get(Calendar.DAY_OF_MONTH);


             datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtdateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                try{
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception e){}
            }
        });
            datePickerDialog.show();
    }
    class MyAyncTaskSendUserData extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AccountOpeningActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {

                Connection mCon = new Connection(AccountOpeningActivity.this);
               res = mCon.JSOpenAccount(authHeader,strOTP,strCNIC,strMobNmbr,name,strAccTitle,strPlaceofbirth,presentAddress
               ,strCNICStatus,expiryDate,dateOfBirth,strFatherName,motherName,gender,"2");

                if(res.toLowerCase().equals("success"))
                {
                    //save userinfo on server
                }
            }
            catch (Exception e)
            {
                res="-1"+e.getMessage();
            }
            return  null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();

            if(res.toLowerCase().equals("success"))
            {
                sharedPreferences.edit().putBoolean(Config.JS_WALLET_ACCOUNT,true).commit();
                sharedPreferences.edit().putString(Config.JS_Mobile_Number,strMobNmbr).commit();
                startActivity(new Intent(AccountOpeningActivity.this,SuccessAccountOpen.class));
                finish();
               // showAlert2("Your JS Wallet Acount is created Successfully.","Success",1);

            }
            else
                showAlert2(res.replace("-1",""),"Failure");


        }
    }
    void showAlert(String message)
    {
        Toast.makeText(AccountOpeningActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message,final String title)
    {
        Config.getAlert(AccountOpeningActivity.this,message,title);

    }

    void showAlert2(String message,final String title,int act)
    {
        Config.getAlert(AccountOpeningActivity.this,message,title);


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Confirm");
            adb.setMessage("Are you sure to go Home Screen?");
            adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    finish();
                }
            });
            adb.setNegativeButton("No", null);
            adb.show();
        }
        return false;

    }

}
