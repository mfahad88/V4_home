package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.psl.classes.Config;
import com.psl.classes.JSUtils;
import com.psl.transport.Connection;

import org.json.JSONObject;
import com.psl.fantasy.league.season2.R;;
import java.util.Arrays;
import java.util.HashMap;

public class BillPaymentActivity extends AppCompatActivity {
    Spinner spinerBillerComp;
    EditText txtConsumer,txtAmount;
    TextView txtAccNumber,textConsumer,textAmount,textBillerComp,textCharges,txtDueDate,txtOverDueAmount;

    HashMap<String,String> biller_names;
    String strOTP,conType,strMobNmbr,strAuthHeader,strAmount,strConsumerId,strProductId,strCharges,strBillerCompany,strDueDate,strOverDue,strActualAmount;
    SharedPreferences sharedPreferences;
    Button btnInqNext,btnConfrm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_payment);

        try{findViewById(R.id.textView14).bringToFront();} catch (Exception e){}


        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        txtConsumer=(EditText)findViewById(R.id.txtConsumerNum);
        Config.PopulateHeader(this,findViewById(R.id.helmet_layout));

        txtAmount=(EditText)findViewById(R.id.txtAmount);
        spinerBillerComp=(Spinner)findViewById(R.id.spinBillerCompany);
        btnInqNext=(Button)findViewById(R.id.btnNext);
        txtAccNumber=(TextView)findViewById(R.id.txtAccNumber);
        textConsumer=(TextView)findViewById(R.id.textConsumerNum);
        textAmount=(TextView)findViewById(R.id.textAmount);
        textBillerComp=(TextView)findViewById(R.id.textBillerCompany);
        textCharges=(TextView)findViewById(R.id.textCharges);
        btnConfrm=(Button) findViewById(R.id.btnConfirm);
        txtDueDate=(TextView)findViewById(R.id.txtDueDate);
        txtOverDueAmount=(TextView)findViewById(R.id.txtOverDueAmount) ;

        txtAccNumber.setText(sharedPreferences.getString(Config.JS_Mobile_Number,""));
        int id=getIntent().getIntExtra("ID",-1);
        switch (id)
        {

            case R.id.electricity_bill:
                biller_names= JSUtils.get_Electric_Companies_info();
                break;
            case R.id.bb_telco_bill:
                biller_names= JSUtils.get_Telecom_Companies_info();
                break;
            case R.id.gas_bill:
                biller_names= JSUtils.get_GAS_Companies_info();
                break;
            case R.id.water_bill:
                biller_names= JSUtils.get_Water_Companies_info();
                break;
            default:
                finish();


        }
        String[] spinnerArray= JSUtils.getArray(biller_names);
        Arrays.sort(spinnerArray);
        strMobNmbr=sharedPreferences.getString(Config.JS_Mobile_Number,"");
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(BillPaymentActivity.this,
                android.R.layout.simple_spinner_item, spinnerArray);
      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerBillerComp.setAdapter(adapter);


        findViewById(R.id.layout2).setVisibility(View.GONE);
        findViewById(R.id.layout1).setVisibility(View.VISIBLE);
        spinerBillerComp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item=spinerBillerComp.getSelectedItem().toString();
                if(item.toLowerCase().contains("prepaid"))
                {
                    conType="prepaid";
                    txtAmount.setVisibility(View.VISIBLE);
                }
                else {
                    conType="";
                    txtAmount.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInqNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(conType.equals("prepaid"))
                strAmount = txtAmount.getText().toString();
                else
                    strAmount="1";
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
                    new MyAyncTaskBillPayINQ().execute();
                }
            }
        });
btnConfrm.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        new MyAyncTaskPayBill().execute();
    }
});
    }
Boolean isDue=false;
    class MyAyncTaskBillPayINQ extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";
        String encrptedOTP;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BillPaymentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(BillPaymentActivity.this);
            res = conn.JSAuth();

            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                {
                    strAuthHeader = res;
                    res = conn.JSBillPaymentInquiry(strAuthHeader, strMobNmbr, strAmount, strProductId, strConsumerId);
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
                        if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                            JSONObject jsonObject = new JSONObject(res);
                            jsonObject = new JSONObject(res);
                            strAmount = (String) jsonObject.get("BillAmount");
                            strDueDate = (String) jsonObject.get("DueDate");
                            strOverDue = (String) jsonObject.get("LateBillAmount");

                        }

                 // Date date=new SimpleDateFormat("yyyymmdd").parse(strDueDate);

                //    strDueDate=simpleDateFormat.format(date);
                    strCharges=res;
                    Intent intent = new Intent(BillPaymentActivity.this,OTPActivity.class);
                    intent.putExtra(Config.JS_Mobile_Number,strMobNmbr);
                    intent.putExtra(Config.JS_Auth_Header,strAuthHeader);
                    intent.putExtra(Config.JS_Action,1);
                    startActivityForResult(intent, OTPActivity.REQUEST_OTP);
                }
                catch (Exception e){

                    Config.getAlert(BillPaymentActivity.this,e.getMessage(),"Error");
                }



            }


        }
    }
    class CheckDateAsync extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BillPaymentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(BillPaymentActivity.this);
            res = conn.checkdate(strDueDate);

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                try {

                    isDue = Boolean.parseBoolean(res);
                } catch (Exception e) {
                    isDue = false;
                }
            }
            findViewById(R.id.layout1).setVisibility(View.GONE);
            findViewById(R.id.layout2).setVisibility(View.VISIBLE);
            textBillerComp.setText(spinerBillerComp.getSelectedItem().toString());
            textConsumer.setText(strConsumerId);
            textAmount.setText("PKR "+strAmount);
            //    textCharges.setText("Payment Charges : PKR "+strCharges);
            txtDueDate.setText(strDueDate);
            txtOverDueAmount.setText("PKR "+strOverDue);
            if(!isDue)
            {
                strActualAmount=strOverDue;
                txtOverDueAmount.setTextColor(Color.GREEN);
            }
            else {

                strActualAmount=strAmount;
                textAmount.setTextColor(Color.GREEN);

            }
            txtDueDate.setText(strDueDate);


        }
    }

    class MyAyncTaskPayBill extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BillPaymentActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(BillPaymentActivity.this);
            res = conn.JSBillPayment(strAuthHeader, strMobNmbr, strActualAmount, strProductId, strConsumerId,strOTP,strBillerCompany);

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

               showAlert2("Bill Payment was successfull.\n"+ Config.JS_TransactionID+" : "+res,"Success",true);


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
                new CheckDateAsync().execute();
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
        if(strConsumerId.trim().equals("") || strConsumerId.trim().length()<3)
        {
            showAlert("Please enter Consumer number");
            txtConsumer.requestFocus();
            return false;
        }
        if(conType.equals("prepaid")) {
            if (strAmount.trim().equals("") || Integer.parseInt(strAmount.trim()) == 0) {
                showAlert("Please enter Amount");
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
        Toast.makeText(BillPaymentActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message,final String title,boolean finish)
    {
        Config.getAlert(BillPaymentActivity.this,message,title,finish);
       // finish();


    }

}
