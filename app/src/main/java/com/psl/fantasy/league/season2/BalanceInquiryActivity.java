package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.fantasy.league.season2.R;;
import com.psl.transport.Connection;

public class BalanceInquiryActivity extends AppCompatActivity {

    TextView balStatus;
    Button btnRetry;
    String strOTP,strMob,strAuthHeader;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_inquiry);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        Config.PopulateHeader2(this,findViewById(R.id.helmet_layout));

        balStatus=(TextView)findViewById(R.id.txtBalanceInq);
        btnRetry=(Button)findViewById(R.id.btnRetry);
        btnRetry.setVisibility(View.GONE);
        Intent intent = new Intent(BalanceInquiryActivity.this,OTPActivity.class);
        strMob=sharedPreferences.getString(Config.JS_Mobile_Number,"");
        intent.putExtra(Config.JS_Mobile_Number,strMob);
        intent.putExtra(Config.JS_OTP_Purpose,"01");
        //intent.putExtra(Config.JS_Action,1);
        startActivityForResult(intent, OTPActivity.REQUEST_OTP);
    btnRetry.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        btnRetry.setVisibility(View.GONE);
        new MyAyncTaskBalanceINQ().execute();
    }
});

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class MyAyncTaskBalanceINQ extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";
        String encrptedOTP;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BalanceInquiryActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn=new Connection(BalanceInquiryActivity.this);
            res=conn.JSBalanceInquiry(strAuthHeader,strMob,strOTP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure",false);
                balStatus.setText("Error : "+res.replace("-1", ""));
                btnRetry.setVisibility(View.VISIBLE);
            }
            else {
                DatabaseHandler dbHandler=new DatabaseHandler(BalanceInquiryActivity.this);
                dbHandler.saveJsBooster(System.currentTimeMillis()+"","Balance Inquiry","1000");
                balStatus.setText("Your current balance is PKR "+res);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OTPActivity.REQUEST_OTP) {

            if (resultCode == -1) {

                String message = data.getStringExtra("message");
                showAlert2(message,"Failure",false);
            }

            else if (resultCode == 100) {

                strOTP = data.getStringExtra(Config.JS_Encrypted_OTP);
                strAuthHeader=data.getStringExtra(Config.JS_Auth_Header);
                new MyAyncTaskBalanceINQ().execute();
            }
        }
    }

    void showAlert(String message)
    {
        Toast.makeText(BalanceInquiryActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message,final String title,boolean act)
    {
        Config.getAlert(BalanceInquiryActivity.this,message,title,act);


    }

}
