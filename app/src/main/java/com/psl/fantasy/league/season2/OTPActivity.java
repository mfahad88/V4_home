package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.EncryptionUtil;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

public class OTPActivity extends AppCompatActivity {

    TextView txtCountDown;
    int counter=90,countLimit=counter*1000;
    public static final int REQUEST_OTP = 151;
    String mobileNumber,authHeader,strOTPCode,strCNIC,strPurpose;
    int action=0;
    Button btnProceed, btnResendOTP;
    EditText txtOTPCode;
    LinearLayout layoutOTPVeri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp);
        try{findViewById(R.id.textView33).bringToFront();} catch (Exception e){}

        Config.PopulateHeader2(OTPActivity.this,findViewById(R.id.helmet_layout));
        // commented by fahad
        //checkAndRequestPermissions();
        mobileNumber = getIntent().getStringExtra(Config.JS_Mobile_Number);
        strPurpose = getIntent().getStringExtra(Config.JS_OTP_Purpose);
        authHeader = getIntent().getStringExtra(Config.JS_Auth_Header);
        strCNIC=getIntent().getStringExtra(Config.CNIC);
        action = getIntent().getIntExtra(Config.JS_Action,0);

        layoutOTPVeri=findViewById(R.id.layoutOTPVeri);

        btnProceed=(Button)findViewById(R.id.btnProceed);
        btnProceed.setVisibility(View.INVISIBLE);
        btnResendOTP=(Button)findViewById(R.id.btnResendOTP);
        layoutOTPVeri=(LinearLayout)findViewById(R.id.layoutOTPVeri);

        txtOTPCode=(EditText)findViewById(R.id.txtOTPCode);
        txtCountDown=(TextView)findViewById(R.id.txtTimerCD);

        txtOTPCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(txtOTPCode.getText().length()>=4)
                    btnProceed.setVisibility(View.VISIBLE);
                else
                    btnProceed.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception e){}

                //  new MyAyncTaskVerifyOTP().execute();
                new MyAyncTaskVerifyOTP().execute();
            }
        });
btnResendOTP.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Exception e){}

        if(action==0)
        new MyAyncTaskSendUserData().execute();
        else {

            setResult(-2, getIntent());
            finish();
        }
    }
});

        if(mobileNumber==null || mobileNumber.length()!=11) {
            getIntent().putExtra("message", "Invalid Mobile number");
            setResult(-1, getIntent());
            finish();
            return;
        }


        if(action==0)
        {
            layoutOTPVeri.setVisibility(View.GONE);
            new MyAyncTaskSendUserData().execute();
        }

        else
        {
            if(authHeader==null || authHeader.isEmpty()) {
                getIntent().putExtra("message", "Invalid auth header.");
                setResult(-1, getIntent());
                finish();
                return;
            }
            waitForOTP();
        }

    }
    class MyAyncTaskSendUserData extends AsyncTask<Void,Void,Void>{

        ProgressDialog progressDialog;
        String res="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OTPActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }
        @Override
        protected Void doInBackground(Void... params) {

            try {

                Connection mCon = new Connection(OTPActivity.this);
                    res = mCon.JSAuth();
                    if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                        authHeader=res;
                        res = mCon.JSGenerateOTP(authHeader, mobileNumber, strPurpose);
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
                waitForOTP();
            }
            else {
               // showAlert2(res.replace("-1", ""), "Failure");
                getIntent().putExtra("message", res.replace("-1", ""));
                setResult(-1, getIntent());
                finish();
                return;
            }

        }
    }
void waitForOTP()
{
    layoutOTPVeri.setVisibility(View.VISIBLE);

    txtCountDown.setVisibility(View.VISIBLE);
    btnResendOTP.setVisibility(View.GONE);
    //counter=60;
    startCountDown();

}
    CountDownTimer countDownTimer;
    void startCountDown()
    {
       countDownTimer= new CountDownTimer(countLimit, 1000){
            public void onTick(long millisUntilFinished){
                counter--;
                if(counter<10)
                    txtCountDown.setText("00:0"+counter);
                    else
                txtCountDown.setText("00:"+counter);

            }
            public  void onFinish(){
                txtCountDown.setText("00:00");
             //   txtCountDown.setVisibility(View.GONE);
                btnResendOTP.setVisibility(View.VISIBLE);

                counter=90;
            }
        }.start();
    }

    class MyAyncTaskVerifyOTP extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res = "";
        String encrptedOTP;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(OTPActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            strOTPCode = txtOTPCode.getText().toString().trim();
           //String key = new String("65412399991212FF65412399991212FF65412399991212FF");
            String key = new String("65412399991212FF65412399991212FF");
            encrptedOTP = EncryptionUtil.encryptWithAES(key, strOTPCode);

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (action == 2) {
                Connection connection = new Connection(OTPActivity.this);
                res = connection.JSVerifyOTP(authHeader, strCNIC, mobileNumber, encrptedOTP);//"success";
            } else {

                res = "success";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.equals("success")) {
                if (countDownTimer != null)
                    countDownTimer.cancel();
                getIntent().putExtra(Config.JS_Encrypted_OTP, encrptedOTP);
                getIntent().putExtra(Config.JS_Auth_Header, authHeader);
                setResult(100, getIntent());
                finish();
            } else {
                if (!res.startsWith("-1Request timeout")) {
                    setResult(-1, getIntent());
                    getIntent().putExtra("message", res.replace("-1", ""));
                    finish();
                } else {
                    showAlert2(res.replace("-1", ""), "Info");
                }

            }
        }
    }

    String extractOTP(String sms)
    {
        String otp=sms.replaceAll("[^0-9]", "");
        return otp;
    }


// commented by fahad
 /*   private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                grand String message = intent.getStringExtra("message");

                txtOTPCode.setText(extractOTP(message));
            }
        }
    };
*/
    @Override
    public void onResume() {
        super.onResume();
        // commented by fahad
        /*LocalBroadcastManager.getInstance(this).
                registerReceiver(smsReceiver, new IntentFilter("otp"));*/

    }
    private  boolean checkAndRequestPermissions() {

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        }
        return true;
    }

    void showAlert(String message)
    {
        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message,final String title)
    {
        Config.getAlert(OTPActivity.this,message,title);

    }


}
