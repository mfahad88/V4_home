package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.JSUtils;
import com.psl.transport.Connection;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class MobileTopupActivity extends AppCompatActivity {
    Spinner spinerBillerComp;
    EditText txtConsumer,txtAmount;
    TextView textConsumer,textAmount,textBillerComp,textCharges,txtAccNumber;

    HashMap<String,String> biller_names;
    String strOTP,strMobNmbr,strAuthHeader,strAmount,strConsumerId,strBillerCompany,strProductId,strCharges,strDueDate,strOverDue;;
    SharedPreferences sharedPreferences;
    Button btnInqNext,btnConfrm;
    String conType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_topup);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);

        try{findViewById(R.id.textView14).bringToFront();} catch (Exception e){}

        /////////////////////////profile//////////////////////////////

        Config.PopulateHeader(this,findViewById(R.id.helmet_layout));

        //////////////////////////////////////////////////////////
        txtAccNumber=(TextView)findViewById(R.id.textView34);

        txtAccNumber.setText(sharedPreferences.getString(Config.JS_Mobile_Number,""));

        txtConsumer=(EditText)findViewById(R.id.txtConsumerNum);
        txtAmount=(EditText)findViewById(R.id.txtAmount);
        spinerBillerComp=(Spinner)findViewById(R.id.spinBillerCompany);
        btnInqNext=(Button)findViewById(R.id.btnNext);

        textConsumer=(TextView)findViewById(R.id.textConsumerNum);
        textAmount=(TextView)findViewById(R.id.textAmount);
        textBillerComp=(TextView)findViewById(R.id.textBillerCompany);
        textCharges=(TextView)findViewById(R.id.textCharges);
        btnConfrm=(Button) findViewById(R.id.btnConfirm);

        biller_names= JSUtils.get_Mobile_Companies_info();
        String[] spinnerArray= JSUtils.getArray(biller_names);
        Arrays.sort(spinnerArray);
        strMobNmbr=sharedPreferences.getString(Config.JS_Mobile_Number,"");

        ArrayAdapter<String> adapter =new ArrayAdapter<String>(MobileTopupActivity.this,
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerBillerComp.setAdapter(adapter);


        findViewById(R.id.layout2).setVisibility(View.GONE);
        findViewById(R.id.layout1).setVisibility(View.VISIBLE);
        btnInqNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strAmount = txtAmount.getText().toString();
                strConsumerId = txtConsumer.getText().toString();
                strBillerCompany=spinerBillerComp.getSelectedItem().toString();
                strProductId=biller_names.get(strBillerCompany);
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
                catch (Exception e){}

                if (ValidateUserDate()) {

                    String cmp= spinerBillerComp.getSelectedItem().toString();
                    if(cmp.toLowerCase().contains("postpaid"))
                    conType="postpaid";
                    else if(cmp.toLowerCase().contains("prepaid"))
                        conType="prepaid";


                    new MyAyncTaskBillPayINQ().execute();
                }
            }
        });

        spinerBillerComp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item=spinerBillerComp.getSelectedItem().toString();
                if(item.toLowerCase().contains("postpaid"))
                {
                    txtAmount.setVisibility(View.GONE);
                }
                else {
                    txtAmount.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnConfrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAyncTaskPayBill().execute();
            }
        });
    }

    class MyAyncTaskBillPayINQ extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";
        String encrptedOTP;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MobileTopupActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(MobileTopupActivity.this);
            res = conn.JSAuth();

            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                {
                    strAuthHeader = res;
                  //  if(conType.equals("prepaid"))
                        res= conn.JSTopupInquiry(strAuthHeader, strMobNmbr, strAmount, strProductId, strConsumerId);
                    /*else if(conType.equals("postpaid"))

                        res = conn.JSBillPaymentInquiry(strAuthHeader, strMobNmbr, strAmount, strProductId, strConsumerId);
*/

                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure",false);

            }
            else {

                try {
                    if (conType.equals("postpaid")) {
                        JSONObject jsonObject = new JSONObject(res);
                        jsonObject = new JSONObject(res);
                        strAmount = (String) jsonObject.get("BillAmount");
                        strDueDate = (String) jsonObject.get("DueDate");
                        strOverDue = (String) jsonObject.get("OverDue");
                      //  Date date=new SimpleDateFormat("yyyymmdd").parse(strDueDate);
                       // strDueDate=Config.simpleDateFormat.format(date);
                    } else {
                        strCharges = "0.0";// res;
                    }
                    Intent intent = new Intent(MobileTopupActivity.this, OTPActivity.class);
                    intent.putExtra(Config.JS_Mobile_Number, strMobNmbr);
                    intent.putExtra(Config.JS_Auth_Header, strAuthHeader);
                    intent.putExtra(Config.JS_Action, 1);
                    startActivityForResult(intent, OTPActivity.REQUEST_OTP);
                }
                catch (Exception e){

                    Config.getAlert(MobileTopupActivity.this,e.getMessage(),"Error");
                }

            }


        }
    }

    class MyAyncTaskPayBill extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MobileTopupActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(MobileTopupActivity.this);
          //  if(conType.equals("postpaid"))
              //  res = conn.JSBillPayment(strAuthHeader, strMobNmbr, strAmount, strProductId, strConsumerId,strOTP,strBillerCompany);
            //else if(conType.equals("prepaid"))
                res = conn.JSTopupPayment(strAuthHeader, strMobNmbr, strAmount, strProductId, strConsumerId,strOTP,strBillerCompany);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(res.startsWith("-1") ) {
                showAlert2(res.replace("-1", ""), "Failure",false);

            }
            else {
                    showAlert2("Mobile Topup was successfull.\n"+ Config.JS_TransactionID+" : "+res,"Success",true);
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
            else if (resultCode == -2) {

                btnInqNext.performClick();
            }
            else if (resultCode == 100)
            {
                strOTP = data.getStringExtra(Config.JS_Encrypted_OTP);
                findViewById(R.id.layout1).setVisibility(View.GONE);
                findViewById(R.id.layout2).setVisibility(View.VISIBLE);
                textBillerComp.setText(spinerBillerComp.getSelectedItem().toString());
                textConsumer.setText(strConsumerId);
                textAmount.setText("PKR "+strAmount);
          //      textCharges.setText("Payment Charges : "+strCharges+" Rs.");

            }
        }
    }
    boolean ValidateUserDate()
    {

        if(strProductId.equals("0"))
        {
            showAlert("Please select Biller Company.");
            spinerBillerComp.performClick();
            return false;
        }
        if(strConsumerId.trim().equals("") || strConsumerId.trim().length()!=11)
        {
            showAlert("Please enter Mobile number");
            txtConsumer.requestFocus();
            return false;
        }
        if(! strConsumerId.trim().startsWith("03"))
        {
            showAlert("Please enter valid Mobile Number");
            txtConsumer.requestFocus();
            return false;
        }
        String item=spinerBillerComp.getSelectedItem().toString();
        if(item.toLowerCase().contains("prepaid")) {
            if (strAmount.trim().equals("") || Integer.parseInt(strAmount.trim()) == 0) {
                showAlert("Please enter Amount");
                txtAmount.requestFocus();
                return false;
            }

            if (Integer.parseInt(strAmount.trim()) > 5000) {
                showAlert("Amount must not be greater than PKR 5000.");
                txtAmount.requestFocus();
                return false;
            }
            if (Integer.parseInt(strAmount.trim()) < 50) {
                showAlert("Amount must be greater than or equal PKR 50.");
                txtAmount.requestFocus();
                return false;
            }
        }
        else {
            strAmount="1";
        }

        return true;
    }

    void showAlert(String message)
    {
        Toast.makeText(MobileTopupActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message,final String title,boolean finish)
    {
        Config.getAlert(MobileTopupActivity.this,message,title,finish);
    }

}
