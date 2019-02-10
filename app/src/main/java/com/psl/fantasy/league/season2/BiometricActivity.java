package com.psl.fantasy.league.season2;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.google.gson.reflect.TypeToken;
import com.paysyslabs.instascan.Fingers;
import com.psl.classes.Config;
import com.psl.transport.Connection;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BiometricActivity extends AppCompatActivity  {

    String strCNIC,strMobNmbr,strFatherName,strOTP,name, presentAddress, gender,expiryDate,motherName,strCNICStatus,dateOfBirth,strPlaceofbirth,authHeader;
    private EditText cnic;
    //private Spinner fingerSpinner;
    private Activity mActivity;
    private ArrayAdapter<Fingers> fingers;
    private TextView statusText;
    private List<Fingers> fingerList;
    Button btnScan,btnNext;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biometric);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
    //    enableAllFingers();
        Intent intent=getIntent();
        strCNIC=intent.getStringExtra("CNIC");
        strMobNmbr=intent.getStringExtra("MOBILE");
        strOTP=intent.getStringExtra("OTP");
        authHeader=intent.getStringExtra("AUTH");
        cnic = (EditText) findViewById(R.id.cnic_field);
        fingerList = new ArrayList<Fingers>(Arrays.asList(Fingers.values()));
        ((ImageView)findViewById(R.id.imageView10)).setBackgroundResource(R.drawable.about_fsl_logo);
    //    findViewById(R.id.layoutstatus).setVisibility(View.VISIBLE);

        fingers = new ArrayAdapter<Fingers>(this, android.R.layout.simple_list_item_1, fingerList);
        statusText = (TextView) findViewById(R.id.debug_status_message);
      //  fingerSpinner = (Spinner) findViewById(R.id.debug_finger_spinner);
       // fingerSpinner.setAdapter(fingers);
        cnic.setText(strCNIC);cnic.setEnabled(true);
        btnScan = (Button) findViewById(R.id.debug_proceed_button);
        btnNext= (Button) findViewById(R.id.btnNext);
        name = "";
        presentAddress="";
        gender = "";
        expiryDate = "";
        motherName = "Mother Name";
        dateOfBirth = "DOB";
        strPlaceofbirth="Birth Place";
        strFatherName="Father";
    /*    btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(BiometricActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ){
                    ActivityCompat.requestPermissions(BiometricActivity.this,new String[]{ Manifest.permission.CAMERA},101);
                return;
                }
                Fingers finger = (Fingers) fingerSpinner.getSelectedItem();
                Intent i = new Intent(BiometricActivity.this, MyNadraActivity.class);
                i.putExtra("finger", finger.getValue());
                i.putExtra("cnic", cnic.getText().toString());
                try {
                    startActivityForResult(i, MyNadraActivity.REQUEST_NADRA);//MyNadraActivity.REQUEST_NADRA);
                }
                catch (Exception e){}
                }
        });*/

    btnNext.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           /* Intent i = new Intent(BiometricActivity.this, AccountOpeningActivity.class);
            i.putExtra("name",name);
            i.putExtra("presentAddress",presentAddress);
            i.putExtra("gender",gender);
            i.putExtra("expiryDate",expiryDate);
            i.putExtra("motherName",motherName);
            i.putExtra("strCNIC",strCNIC);
            i.putExtra("strMobNmbr",strMobNmbr);
            i.putExtra("strOTP",strOTP);
            i.putExtra("dateOfBirth",dateOfBirth);
            i.putExtra("strPlaceofbirth",strPlaceofbirth);
            i.putExtra("AUTH",authHeader);
            i.putExtra("strFatherName",strFatherName);
            i.putExtra("strCNICStatus",strCNICStatus);


            startActivity(i);*/
           new MyAyncTaskSendUserData().execute();
//            finish();
        }
    });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        statusText.setVisibility(View.VISIBLE);
        if (requestCode == MyNadraActivity.REQUEST_NADRA) {

            if (resultCode == -1) {

                String code = data.getStringExtra("code");
                String message = data.getStringExtra("message");
                statusText.setText(message+". Code : "+code);
                findViewById(R.id.imageView2).setBackgroundResource(R.drawable.cross_big);

            } else if (resultCode == -2) {

                Type listType = new TypeToken<List<Fingers>>() {
                }.getType();
                String code = data.getStringExtra("code");
                String message = data.getStringExtra("message");
                String fingerJson = data.getStringExtra("fingers");
                List<Fingers> valid = MyNadraActivity.GSON.fromJson(fingerJson, listType);
                /*fingers.clear();
                fingers.addAll(valid);
                fingers.notifyDataSetChanged();*/
                disableAllFingers();
                for (Fingers finger:valid) {
                    enableFinger(finger.getValue());


                }

                statusText.setText(message+".Please select other finger");
                findViewById(R.id.imageView2).setBackgroundResource(R.drawable.cross_big);

            } else if (resultCode == 100) {

                final String personJson = data.getStringExtra("person");
                disableAllFingersOnSuccess();
                try {
                    JSONObject jsonObject = new JSONObject(personJson);
                 try {   name = (String) jsonObject.get("name");                     }  catch (Exception e){name="";}
                 try {   presentAddress = (String) jsonObject.get("presentAddress"); }  catch (Exception e){presentAddress="PA";}
                 try {   gender = (String) jsonObject.get("gender");                 }  catch (Exception e){gender="Gender";}
                 try {   expiryDate = (String) jsonObject.get("expiryDate");         }
                 catch (Exception e){
  //                   expiryDate="2017-12-24";
                     //strCNICStatus="0";

                 }
                 try {   motherName = (String) jsonObject.get("motherName");         }  catch (Exception e){ motherName="Mother NAme";}
                 try {   dateOfBirth = (String) jsonObject.get("dateOfBirth");       }  catch (Exception e){strPlaceofbirth="Birth Place";}
                 try {   strPlaceofbirth = (String) jsonObject.get("birthPlace");    }  catch (Exception e){strFatherName="Father";}
                 try {   strFatherName= (String) jsonObject.get("FatherHusbandName");}  catch (Exception e){strCNICStatus="CNIC STATUS";}
                 try {   strCNICStatus= (String) jsonObject.get("cardExpired");}  catch (Exception e){}
                    if(strCNICStatus.equals("yes")) {
                        strCNICStatus = "0";
                    }
                        else strCNICStatus="1";

                } catch (Exception e){}
                    new AsyncTask<Void,Void,Void>()
             {
                 ProgressDialog progressDialog;
                 String res="";
                 @Override
                 protected void onPreExecute() {
                     super.onPreExecute();
                     progressDialog = new ProgressDialog(BiometricActivity.this);
                     progressDialog.setCancelable(false);
                     progressDialog.setMessage("Translating...");
                     progressDialog.show();
                 }

                 @Override
                 protected Void doInBackground(Void... params) {


                     Connection connection = new Connection(BiometricActivity.this);
                     res=connection.TranslateData(name,strPlaceofbirth,motherName,presentAddress,strFatherName,strMobNmbr);
                     return null;
                 }

                 @Override
                 protected void onPostExecute(Void aVoid) {
                     super.onPostExecute(aVoid);

                     statusText.setText("You have successfully verified. Press next to proceed");

                     try {

                         if(res.startsWith("-1")) {

                           //  statusText.setText(personJson);

                         }
                         else {
                             JSONObject jsonObject = new JSONObject(personJson);
                             jsonObject = new JSONObject(res);
                             try {
                                 name = (String) jsonObject.get("name");
                                 presentAddress = (String) jsonObject.get("presentAddress");
                                 motherName = (String) jsonObject.get("motherName");
                                 strPlaceofbirth = (String) jsonObject.get("birthPlace");
                                  strFatherName= (String) jsonObject.get("fatherName");
                                 if(strFatherName.equals(""))
                                 {
                                     strFatherName="Father";
                                 }
                             }
                             catch (Exception e)
                             {

                             }
                          //   statusText.setText(res);

                         }

                         String info="Name : "+name+"\nPresent Address : "+presentAddress+"\nGender : "+gender+"\nCard Expiry Date : "+expiryDate;
                       //  statusText.setText(info);

                     }
                     catch (Exception e)
                     {

                     }
                     findViewById(R.id.imageView2).setBackgroundResource(R.drawable.tick_big);
                     // statusText.setVisibility(View.GONE);
                     btnScan.setVisibility(View.GONE);
                     //fingerSpinner.setVisibility(View.GONE);
                     btnNext.setVisibility(View.VISIBLE);
                     progressDialog.dismiss();
                 }
             }.execute();


            }
        }
    }

    class MyAyncTaskSendUserData extends AsyncTask<Void,Void,Void> {

        ProgressDialog progressDialog;
        String res="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BiometricActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            try {

                Connection mCon = new Connection(BiometricActivity.this);
                res = mCon.JSOpenAccount(authHeader.trim(),strOTP.trim(),strCNIC.trim(),strMobNmbr.trim(),name.trim(),name.trim(),strPlaceofbirth.trim(),presentAddress.trim()
                        ,strCNICStatus.trim(),expiryDate.trim(),dateOfBirth.trim(),strFatherName.trim(),motherName.trim(),gender.trim(),"2");

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
                startActivity(new Intent(BiometricActivity.this,SuccessAccountOpen.class));
                finish();
                // showAlert2("Your JS Wallet Acount is created Successfully.","Success",1);

            }
            else
                Config.getAlert(BiometricActivity.this,res.replace("-1",""));


        }
    }

    Fingers selectedFinger;
    public void FingerClick(View v)
    {
        if (ActivityCompat.checkSelfPermission(BiometricActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(BiometricActivity.this,new String[]{ Manifest.permission.CAMERA},101);
            return;
        }
        findViewById(R.id.imageView2).setBackgroundResource(0);
        statusText.setText("");
        ((ImageView) v).setImageResource(R.drawable._checked);

        switch (v.getId()) {
            case   R.id.imgLeftIndex:      selectedFinger= Fingers.LEFT_INDEX;        break;
            case   R.id.imgLeftLittle:     selectedFinger= Fingers.LEFT_LITTLE;       break;
            case   R.id.imgLeftmiddle:     selectedFinger= Fingers.LEFT_MIDDLE;        break;
            case   R.id.imgLeftRing:       selectedFinger= Fingers.LEFT_RING;        break;
            case   R.id.imgLeftThumb:      selectedFinger= Fingers.LEFT_THUMB;        break;

            case   R.id.imgRightIndex:     selectedFinger= Fingers.RIGHT_INDEX;        break;
            case   R.id.imgRightLittle:    selectedFinger= Fingers.RIGHT_LITTLE;       break;
            case   R.id.imgRightMiddle:    selectedFinger= Fingers.RIGHT_MIDDLE;        break;
            case   R.id.imgRightRing:      selectedFinger= Fingers.RIGHT_RING;        break;
            case   R.id.imgRightThumb:     selectedFinger= Fingers.RIGHT_THUMB;        break;

        }
        Intent i = new Intent(BiometricActivity.this, MyNadraActivity.class);
        i.putExtra("finger",selectedFinger.getValue());
        i.putExtra("cnic", cnic.getText().toString());
        startActivityForResult(i, MyNadraActivity.REQUEST_NADRA);//MyNadraActivity.REQUEST_NADRA);
    }
    void disableAllFingers()
    {
        ImageView v;
        v=findViewById(R.id.imgLeftIndex);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgLeftLittle);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgLeftmiddle);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgLeftRing);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgLeftThumb);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgRightIndex);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgRightLittle);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgRightMiddle);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgRightRing);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
        v=findViewById(R.id.imgRightThumb);
        v.setEnabled(false);
        v.setImageResource(R.drawable.cross_finger);
    }

    void enableFinger(String fingerValue)
    {
        ImageView v;
        switch (fingerValue)
        {
            case  "LEFT_INDEX"  :     v=findViewById(R.id.imgLeftIndex);             v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "LEFT_LITTLE" :     v=findViewById(R.id.imgLeftLittle);        v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "LEFT_MIDDLE" :     v=findViewById(R.id.imgLeftmiddle);        v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "LEFT_RING"   :     v=findViewById(R.id.imgLeftRing);      v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "LEFT_THUMB"  :     v=findViewById(R.id.imgLeftThumb);         v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "RIGHT_INDEX" :     v=findViewById(R.id.imgRightIndex);        v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "RIGHT_LITTLE":     v=findViewById(R.id.imgRightLittle);       v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "RIGHT_MIDDLE":     v=findViewById(R.id.imgRightMiddle);       v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "RIGHT_RING"  :     v=findViewById(R.id.imgRightRing);         v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
            case  "RIGHT_THUMB" :     v=findViewById(R.id.imgRightThumb);        v.setImageResource(R.drawable.finger_enable); v.setEnabled(true); break;
        }

    }
    void enableAllFingers() {
       ImageView v=findViewById(R.id.imgLeftIndex);   v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgLeftLittle);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgLeftmiddle);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgLeftRing);             v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgLeftThumb);            v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgRightIndex);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgRightLittle);          v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgRightMiddle);          v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgRightRing);            v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);
        v=findViewById(R.id.imgRightThumb);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(true);

    }
    void disableAllFingersOnSuccess()
    {
        ImageView v=findViewById(R.id.imgLeftIndex);   v.setImageResource(R.drawable.finger_enable);v.setEnabled(false);
        v=findViewById(R.id.imgLeftLittle);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgLeftmiddle);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgLeftRing);             v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgLeftThumb);            v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgRightIndex);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgRightLittle);          v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgRightMiddle);          v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgRightRing);            v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
        v=findViewById(R.id.imgRightThumb);           v.setImageResource(R.drawable.finger_enable); v.setEnabled(false);
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
                    Config.prevFragment=null;
                    finish();
                }
            });
            adb.setNegativeButton("No", null);
            adb.show();
        }
        return false;

    }


}
