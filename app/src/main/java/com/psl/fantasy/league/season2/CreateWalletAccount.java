package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.psl.fantasy.league.season2.R;;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.Transaction_Details;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

public class CreateWalletAccount extends Fragment {

    EditText txtMobNumber, txtMobNumber1, txtCNIC, txtCNIC1, txtCNIC2;
    String strMobNmbr, strCNIC, strOTPCode, strAccType, authHeader = "";
    ;
    RadioButton rbNew, rbExst;
    Button btnSubmit;
    LinearLayout layoutUserInfo;
    SharedPreferences sharedPreferences;
    boolean IS_ALERT_DISPLAYED = false;

    String password;

    private boolean checkAndRequestPermissions() {

        int receiveSMS = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.RECEIVE_SMS);

        int readSMS = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(),
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    1);
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.activity_create_wallet, container, false);
        // super.onCreate(savedInstanceState);

        checkAndRequestPermissions();

        txtMobNumber = (EditText) view.findViewById(R.id.txtUserMobNumber);
        txtMobNumber1 = (EditText) view.findViewById(R.id.txtUserMobNumber1);


        txtCNIC = (EditText) view.findViewById(R.id.txtUserCNIC);
        txtCNIC1 = (EditText) view.findViewById(R.id.txtUserCNIC1);
        txtCNIC2 = (EditText) view.findViewById(R.id.txtUserCNIC2);

        rbNew = (RadioButton) view.findViewById(R.id.rbNewWallet);
        rbExst = (RadioButton) view.findViewById(R.id.rbExistingWallet);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        layoutUserInfo = (LinearLayout) view.findViewById(R.id.layoutUserInfo);
        String mNmbr = sharedPreferences.getString(Config.CELL_NO, "");
        password = sharedPreferences.getString(Config.USER_KEY, "");
        if (mNmbr.length() == 11) {
            txtMobNumber.setText(mNmbr);
            txtMobNumber1.setText(mNmbr.substring(4));
        }
        txtCNIC.setText(sharedPreferences.getString(Config.CNIC, ""));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  /* if(true)
                    {
                        startActivity(new Intent(getActivity(),SuccessAccountOpen.class));
                        Fragment fragment =null;
                        if(Config.prevFragment!=null)
                            fragment= Config.prevFragment;
                        else
                            fragment= new Home();
                        FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                        return;

                    }*/
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                }
                if (rbExst.isChecked())
                    strAccType = "exist";
                else if (rbNew.isChecked())
                    strAccType = "new";
                else
                    strAccType = "";

                strMobNmbr = txtMobNumber.getText().toString().trim();//+txtMobNumber1.getText().toString().trim();
                strCNIC = txtCNIC.getText().toString().trim();//+txtCNIC1.getText().toString().trim()+txtCNIC2.getText().toString().trim();
                if (ValidateUserDate()) {


                    new MyAyncTaskSendUserData().execute();
                }
            }
        });

        txtMobNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtMobNumber.getText().toString().length() == 11) {
                    txtCNIC.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        txtCNIC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtCNIC.getText().toString().length() == 13) {
                    btnSubmit.requestFocus();
                    try {
                        InputMethodManager inputManager = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        txtCNIC1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtCNIC1.getText().toString().length() == 7) {
                    txtCNIC2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*if (Config.WALLET_TIMEOUT)
            getAlert();*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Config.WALLET_TIMEOUT) {
                if(!IS_ALERT_DISPLAYED) {
                    getAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAlert() {
        final EditText edittext = new EditText(getActivity());
        edittext.setHint("Please enter your password");
        edittext.setMaxLines(1);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittext.setMaxEms(30);
        //edittext.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.digits_to_allow_password)));

        int maxLength = 30;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edittext.setFilters(fArray);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(edittext)
                .setTitle("Password")
                .setCancelable(false)
                .setPositiveButton("Done", null)
                .setNegativeButton("Cancel", null)
                .create();

        // grand Button bb = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button b2 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String value = edittext.getText().toString();
                        try {
                            if (value.trim().equalsIgnoreCase(password)) {
                                Config.WALLET_TIMEOUT = false;
                                IS_ALERT_DISPLAYED = false;
                                MainActivity.resetDisconnectTimerWallet();
                                dialog.dismiss();
                            } else {

                                Toast toast = Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            RadioButton rb = getActivity().findViewById(R.id.iv_team);
                            rb.setChecked(true);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    class MyAyncTaskSendUserData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                Connection mCon = new Connection(getActivity());
                res = mCon.JSAuth();

                if (!res.startsWith("-1") && !res.trim().isEmpty()) {

                    authHeader = res;
                    if (strAccType.equals("exist"))
                        res = mCon.JSVerifyAccount(authHeader, strCNIC, strMobNmbr, "02");
                    else
                        res = mCon.JSVerifyAccount(authHeader, strCNIC, strMobNmbr, "01");

                }

            } catch (Exception e) {
                res = "-1" + e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();


            if (res.toLowerCase().equals("success")) {

                Intent intent = new Intent(getActivity(), OTPActivity.class);
                intent.putExtra(Config.JS_Mobile_Number, strMobNmbr);
                intent.putExtra(Config.JS_Auth_Header, authHeader);
                intent.putExtra(Config.CNIC, strCNIC);
                if (strAccType.equals("exist"))
                    intent.putExtra(Config.JS_Action, 2);
                else
                    intent.putExtra(Config.JS_Action, 1);
                startActivityForResult(intent, OTPActivity.REQUEST_OTP);


            } else
                showAlert2(res.replace("-1", ""), "Failure");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OTPActivity.REQUEST_OTP) {

            if (resultCode == -1) {

                String message = data.getStringExtra("message");
                showAlert2(message, "Failure");
            } else if (resultCode == -2) {

                btnSubmit.performClick();
            } else if (resultCode == 100) {
                if (strAccType.equals("exist")) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Connection connection = new Connection(getActivity());
                            List<Transaction_Details> transaction_detailses = connection.prepareTransList("0", "0", "Link_User", strMobNmbr, strCNIC, "Linked Wallet Account", "SUCCESS", "");
                            String test = connection.UpdateTransactionNew(transaction_detailses);
                            return null;
                        }
                    }.execute();

                    sharedPreferences.edit().putString(Config.JS_Mobile_Number, strMobNmbr).commit();
                    sharedPreferences.edit().putBoolean(Config.JS_WALLET_ACCOUNT, true).commit();
                    showAlert2("You have successfully linked your account.", "Success", 0);
                } else {
                    String otp = data.getStringExtra(Config.JS_Encrypted_OTP);
                    Intent intent = new Intent(getActivity(), BiometricActivity.class);
                    intent.putExtra("CNIC", strCNIC);
                    intent.putExtra("MOBILE", strMobNmbr);
                    intent.putExtra("OTP", otp);
                    intent.putExtra("AUTH", authHeader);

                    /*Fragment fragment = new Home();
                    FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();*/
                    getFragmentManager().popBackStack();
                    startActivity(intent);
                    Fragment fragment = null;
                    if (Config.prevFragment != null)
                        fragment = Config.prevFragment;
                    else
                        fragment = new Dashboard();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    //  getActivity().finish();

                }

            }
        }
    }

    boolean ValidateUserDate() {
        if (strAccType.equals("")) {
            showAlert("Please select Wallet Status");
            rbNew.requestFocus();
            return false;
        }
        if (strMobNmbr.equals("") || strMobNmbr.length() < 11) {
            showAlert("Please enter mobile number");
            txtMobNumber.requestFocus();
            return false;
        }
        if (!strMobNmbr.startsWith("03")) {
            showAlert("Please enter a valid mobile number");
            txtMobNumber.requestFocus();
            return false;
        }
        if (strCNIC.equals("") || strCNIC.length() < 13) {
            showAlert("Please enter CNIC number");
            txtCNIC.requestFocus();
            return false;
        }
        return true;
    }

    void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);

    }

    void showAlert2(String message, final String title, int a) {
       /* AlertDialog.Builder adb=new AlertDialog.Builder(getActivity());
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {*/
        //startActivity(getIntent());
        Config.getAlert(getActivity(), message, title);
        Fragment fragment = null;
        if (Config.prevFragment != null)
            fragment = Config.prevFragment;
        else
            fragment = new MyWallet();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();


    }


}
