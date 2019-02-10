package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.fantasy.league.season2.R;;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.psl.fantasy.league.season2.RegisterActivity.specialCharacterMatch;

public class ResetPin extends AppCompatActivity {

    EditText et_mobilenumber, et_otp, et_newpin, et_confirmpin;
    LinearLayout layout_mobilenumber, layout_otp, layout_pin;
    Button btn_send, btn_resend, btn_done;
    boolean isUp;
    int visible_layout = 1;
    String mobilenumber;
    String my_otp;
    String pin_code;
    ImageButton btn_Back;
    EditText et_mobilenumber_new;
    Button btnNext;

    TextView weekTxtView, fairTxtView, strongTxtView ;
    LinearLayout passwordStrengthLayout ;
    TextView password_strengthTxt ;
    String password_strength_check = "Weak";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pin);
        checkAndRequestPermissions();
        et_mobilenumber = (EditText) findViewById(R.id.txt_reset_mobile_no);
        et_mobilenumber_new = (EditText) findViewById(R.id.txt_mobileNumber_new_);
        et_otp = (EditText) findViewById(R.id.txt_otp);
        et_newpin = (EditText) findViewById(R.id.txt_new_pin);
        et_confirmpin = (EditText) findViewById(R.id.txt_confirm_pin);
        btn_Back = (ImageButton)findViewById(R.id.btn_back);

        layout_mobilenumber = (LinearLayout) findViewById(R.id.layout_mobileNo);
        layout_otp = (LinearLayout) findViewById(R.id.layout_otp);
        layout_pin = (LinearLayout) findViewById(R.id.layout_reset_pin);

        passwordStrengthLayout = (LinearLayout)findViewById(R.id.passwrodLayout) ;
        weekTxtView = (TextView)findViewById(R.id.week);
        fairTxtView = (TextView)findViewById(R.id.fair);
        strongTxtView = (TextView)findViewById(R.id.strong);
        password_strengthTxt = (TextView)findViewById(R.id.strngthTxt) ;


        et_newpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            public void beforeTextChanged(CharSequence s, int start, int count,int after){}
            public void afterTextChanged(Editable s) {

                if((s.length() < 6 && Pattern.compile("[0-9]").matcher(s.toString()).find() && (specialCharacterMatch(s.toString())) && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find())){
                    weekTxtView.setBackgroundColor(Color.RED) ;
                    fairTxtView.setBackgroundColor(Color.TRANSPARENT);
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("Weak");
                    password_strength_check = "Weak";
                }
                else
                if((s.length() <6 && Pattern.compile("[0-9]").matcher(s.toString()).find()==false &&(specialCharacterMatch(s.toString())==false) || (s.length() <6 && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find()==false &&(specialCharacterMatch(s.toString())==false))) || (s.length() <6 && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find()==false && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find()==false)){
                    weekTxtView.setBackgroundColor(Color.RED) ;
                    fairTxtView.setBackgroundColor(Color.TRANSPARENT);
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("Weak");
                    password_strength_check = "Weak";
                }
                else
                if((!(s.length() < 6)) &&Pattern.compile("[0-9]").matcher(s.toString()).find() && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() && (specialCharacterMatch(s.toString()))){
                    weekTxtView.setBackgroundColor(Color.GREEN) ;
                    fairTxtView.setBackgroundColor(Color.GREEN) ;
                    strongTxtView.setBackgroundColor(Color.GREEN) ;
                    password_strengthTxt.setText("Strong");
                    password_strength_check = "Strong";
                }

                else
                if(((!(s.length() < 6)) && Pattern.compile("[0-9]").matcher(s.toString()).find() && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find()) || ((!(s.length() < 6)) && Pattern.compile("[0-9]").matcher(s.toString()).find() && specialCharacterMatch(s.toString()))|| ((!(s.length() < 6)) && Pattern.compile("[a-z]").matcher(s.toString()).find() && specialCharacterMatch(s.toString()))){
                    weekTxtView.setBackgroundColor(Color.YELLOW) ;
                    fairTxtView.setBackgroundColor(Color.YELLOW) ;
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT) ;
                    password_strengthTxt.setText("Medium");
                    password_strength_check = "Medium";
                }
                if(s.length()==0) {
                    //et_enterpassword.setError("Must be 6-20 characters, contain at least one digit and one alphabetic character, and can contains special characters !@#$%^&*()") ;
                    weekTxtView.setBackgroundColor(Color.TRANSPARENT) ;
                    password_strengthTxt.setText("") ;
                    passwordStrengthLayout.setVisibility(View.GONE) ;
                    password_strength_check = "Weak";
                }
                if(s.length() != 0){
                    passwordStrengthLayout.setVisibility(View.VISIBLE) ;
                }
            }
        });

        et_otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Toast.makeText(getBaseContext(), s.toString(), Toast.LENGTH_LONG).show();
                if(s.toString().length() == 6)
                {
                   if(s.toString().equals(my_otp))
                    {
                        pin_code = et_newpin.getText().toString();
                        new SavePinAsync().execute(mobilenumber, et_newpin.getText().toString());
                        /*//slideDown(layout_otp);
                        layout_otp.setVisibility(View.GONE);
                        //slideUp(layout_pin);
                        layout_pin.setVisibility(View.VISIBLE);
                        btn_Back.setVisibility(View.GONE);*/
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void Next_Onclick(View view) {
        if(et_otp.toString().equals(my_otp))
        {
            /*//mobilenumber = et_mobilenumber.getText().toString();
            et_mobilenumber_new.setText(mobilenumber);
            slideDown(layout_otp);
            layout_otp.setVisibility(View.GONE);
            slideUp(layout_pin);
            layout_pin.setVisibility(View.VISIBLE);
            btn_Back.setVisibility(View.GONE);*/
            pin_code = et_newpin.getText().toString();
            new SavePinAsync().execute(mobilenumber, et_newpin.getText().toString());

        }else
        {
            Toast.makeText(getBaseContext(), "Please enter valid OTP", Toast.LENGTH_LONG).show();
        }
    }

    public void Send_Onclick(View view) {
        if (et_mobilenumber.getText().equals("") || et_mobilenumber.getText().length() < 11) {
            Toast.makeText(getBaseContext(), "Please enter valid mobile number", Toast.LENGTH_LONG).show();
        } else {

            mobilenumber = et_mobilenumber.getText().toString();
            layout_mobilenumber.setVisibility(View.GONE);
            layout_pin.setVisibility(View.VISIBLE);

            //layout_otp.setVisibility(View.VISIBLE);
            //new SendOTPAsync().execute(mobilenumber);
        }

    }

    public void Resend_Onclick(View view) {
            try {
                et_otp.setText("");
                mobilenumber = et_mobilenumber.getText().toString();
                new SendOTPAsync().execute(mobilenumber);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
    }

    public void Back_Onclick(View view) {

    }
    void getAlertVerification(String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(ResetPin.this);
        //adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pin_code = et_newpin.getText().toString();
                new SavePinAsync().execute(mobilenumber, et_newpin.getText().toString());
            }
        });


        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        adb.show();

    }

    public void Done_Onclick(View view) {
        if (et_newpin.getText().equals("") || et_mobilenumber.getText().length() < 6) {
            Toast.makeText(getBaseContext(), "Password must be atleast 6 characters long 6", Toast.LENGTH_LONG).show();
        }else if (!et_newpin.getText().toString().equals(et_confirmpin.getText().toString())) {
            Toast.makeText(getBaseContext(), "New and Confirm Password should be same", Toast.LENGTH_LONG).show();
        }else if (password_strength_check.equalsIgnoreCase("Weak")) {
            getAlert("Must be 6-20 characters, contain at least one digit and one alphabetic character, and can contains special characters !@#$%^&*()");
            return;
        } else {
            mobilenumber = et_mobilenumber.getText().toString();
            new SendOTPAsync().execute(mobilenumber);
            //pin_code = et_newpin.getText().toString();
            //new SavePinAsync().execute(mobilenumber, et_newpin.getText().toString());
        }

    }

    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth(),                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                view.getWidth(),                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(layout_mobilenumber);
            // myButton.setText("Slide up");
        } else {
            slideUp(layout_otp);
            //myButton.setText("Slide down");
        }
        //isUp = !isUp;
    }

    private class SendOTPAsync extends AsyncTask<String, String, String> {
        String mobile_no = et_mobilenumber.getText().toString();
        String objResult;
        ProgressDialog pDialog = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(ResetPin.this, "Sending OTP", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                Connection connection = new Connection("Send_otp_to_user", getBaseContext());
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.addProperties("mobile", params[0]);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                if (object != null)
                    objResult = object.getPropertyAsString("Send_otp_to_userResult").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                pDialog.dismiss();
                if(objResult.toString().contains("Mobile No") || objResult.toString().length() > 10)
                {
                    getAlert(objResult.toString());
                }else {
                    layout_pin.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    et_otp.setText("");
                    my_otp = objResult.toString();
                    //Toast.makeText(getBaseContext(), objResult.toString(), Toast.LENGTH_LONG).show();
                    //layout_mobilenumber.setVisibility(View.GONE);
                    //layout_otp.setVisibility(View.VISIBLE);
                    //my_otp = objResult.toString();

                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    void getAlert(String message)
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(ResetPin.this);
        adb.setMessage(message);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    private class SavePinAsync extends AsyncTask<String, String, String> {
        String mobile_no = et_mobilenumber.getText().toString();
        String objResult;
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(ResetPin.this, "Resetting Password", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection("save_new_pin", getBaseContext());
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.addProperties("mobile", params[0]);
                connection.addProperties("pin", params[1]);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                if (object != null)
                    objResult = object.getPropertyAsString("save_new_pinResult").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            try {
                AlertDialog.Builder adb=new AlertDialog.Builder(ResetPin.this);
                adb.setMessage(objResult.toString());
                adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getBaseContext(), LoginActivity.class));
                        finish();
                    }
                });
                adb.show();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    String extractOTP(String sms) {
        String otp = sms.replaceAll("[^0-9]", "");
        return otp;
    }


    private BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");

                et_otp.setText(extractOTP(message));
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).
                registerReceiver(smsReceiver, new IntentFilter("otp"));

    }

    private boolean checkAndRequestPermissions() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
