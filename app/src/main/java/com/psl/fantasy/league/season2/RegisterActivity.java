package com.psl.fantasy.league.season2;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.psl.classes.Config;
import com.psl.transport.Connection;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;


public class RegisterActivity extends AppCompatActivity {


    //Login variables
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    String f_name = "";
    String l_name = "";
    CountDownTimer countDownTimer;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    SharedPreferences sharedPreferences;
    CallbackManager callbackManager;
    LoginButton loginButton;
    EditText userName, userPwd;
    Button loginBtn;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 0;

    private CallbackManager mFacebookCallbackManager;
    private LoginManager mLoginManager;
    private AccessTokenTracker mAccessTokenTracker;
    private boolean loggedin;
    String register_via = "form";
    String email_id = "";

    private Pattern password_pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

    /**
     * Called when the activity is first created.
     */
    //EditText txtName,txtEmail,txtPwd,txtRepwd,txtCellNumber,txtCNIC,txtjsWallet;
    EditText et_fistname, et_lastname, et_email, et_mobilenumber;
    Spinner spinner_gender, spinner_age;
    String firstname, lastname, mobilenumber, gender, age;
    String name, email, pwd, repwd, address, cellNumber, jsWallet, cnic;
    Button btnLogin, btnSelectPic;
    Bitmap image;
    String encoded_image;
    ImageView imgProfileImage;
    SharedPreferences mSharedPreferences;
    String action;
    CheckBox checkBox;
    String which_layout = "first";
    LinearLayout layout_otp;
    LinearLayout layout_setpassword;
    ScrollView layout_registeration;
    String received_otp = "";
    String mobileNo = "";
    Button btn_resend;
    Button btn_next;
    Button btn_registeration_end;
    int counter = 60, countLimit = counter * 1000;
    TextView tv_countdown_timer;
    EditText et_otp;
    EditText et_pas_mobileNumber;
    EditText et_enterpassword;
    EditText et_confirmPassword;
    Button btn_register_first;
    LinearLayout layout_first;

    TextView weekTxtView, fairTxtView, strongTxtView;
    LinearLayout passwordStrengthLayout;
    TextView password_strengthTxt;
    String password_strength_check = "Weak";

    Pattern pattern;
    Account[] account;
    String[] StringArray;
    public static final int RequestPermissionCode = 1;

    void getAlert(String text) {
        new ViewDialog(RegisterActivity.this).showDialog(RegisterActivity.this, "");
        /*new AlertDialog.Builder(RegisterActivity.this)
                .setTitle("JSFL Terms & Conditions")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(text)
                .show();*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        checkAndRequestPermissions();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

        }
        changeStatusBarColor("");
        EnableRuntimePermission();
        pattern = Patterns.EMAIL_ADDRESS;
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setupInit();//facebook

        layout_otp = (LinearLayout) findViewById(R.id.otp_layout);
        layout_first = (LinearLayout) findViewById(R.id.first_layout);
        btn_register_first = (Button) findViewById(R.id.btnRegister_first);
        layout_setpassword = (LinearLayout) findViewById(R.id.password_layout);
        layout_registeration = (ScrollView) findViewById(R.id.scrollView1);
        btn_resend = (Button) findViewById(R.id.btn_resend);
        btn_next = (Button) findViewById(R.id.btn_next);
        tv_countdown_timer = (TextView) findViewById(R.id.tv_timer);
        et_otp = (EditText) findViewById(R.id.txt_otp);
        et_pas_mobileNumber = (EditText) findViewById(R.id.txt_mobileNumber_pas);
        et_enterpassword = (EditText) findViewById(R.id.enter_password);
        et_confirmPassword = (EditText) findViewById(R.id.txt_confirm_password);

        passwordStrengthLayout = (LinearLayout) findViewById(R.id.passwrodLayout);
        weekTxtView = (TextView) findViewById(R.id.week);
        fairTxtView = (TextView) findViewById(R.id.fair);
        strongTxtView = (TextView) findViewById(R.id.strong);
        password_strengthTxt = (TextView) findViewById(R.id.strngthTxt);


        mSharedPreferences = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        btnLogin = (Button) findViewById(R.id.btnRegister);
        et_fistname = (EditText) findViewById(R.id.txt_firstname);
        et_lastname = (EditText) findViewById(R.id.txt_lastname);
        et_mobilenumber = (EditText) findViewById(R.id.txt_mobilenumber);
        et_mobilenumber.setHint("Mobile number field will be used to\n send updates, notifications, otp etc.");
        et_email = (EditText) findViewById(R.id.txt_email);
        spinner_age = (Spinner) findViewById(R.id.spinner_age);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        btn_registeration_end = (Button) findViewById(R.id.btnRegister_new);


        et_enterpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

                if ((s.length() < 6 && Pattern.compile("[0-9]").matcher(s.toString()).find() && (specialCharacterMatch(s.toString())) && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find())) {
                    weekTxtView.setBackgroundColor(Color.RED);
                    fairTxtView.setBackgroundColor(Color.TRANSPARENT);
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("Weak");
                    password_strength_check = "Weak";
                } else if ((s.length() < 6 && Pattern.compile("[0-9]").matcher(s.toString()).find() == false && (specialCharacterMatch(s.toString()) == false) || (s.length() < 6 && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() == false && (specialCharacterMatch(s.toString()) == false))) || (s.length() < 6 && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() == false && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() == false)) {
                    weekTxtView.setBackgroundColor(Color.RED);
                    fairTxtView.setBackgroundColor(Color.TRANSPARENT);
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("Weak");
                    password_strength_check = "Weak";
                } else if ((!(s.length() < 6)) && Pattern.compile("[0-9]").matcher(s.toString()).find() && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() && (specialCharacterMatch(s.toString()))) {
                    weekTxtView.setBackgroundColor(Color.GREEN);
                    fairTxtView.setBackgroundColor(Color.GREEN);
                    strongTxtView.setBackgroundColor(Color.GREEN);
                    password_strengthTxt.setText("Strong");
                    password_strength_check = "Strong";
                } else if (((!(s.length() < 6)) && Pattern.compile("[0-9]").matcher(s.toString()).find() && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find()) || ((!(s.length() < 6)) && Pattern.compile("[0-9]").matcher(s.toString()).find() && specialCharacterMatch(s.toString())) || ((!(s.length() < 6)) && Pattern.compile("[a-zA-Z]").matcher(s.toString()).find() && specialCharacterMatch(s.toString()))) {
                    weekTxtView.setBackgroundColor(Color.YELLOW);
                    fairTxtView.setBackgroundColor(Color.YELLOW);
                    strongTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("Medium");
                    password_strength_check = "Medium";
                }
                if (s.length() == 0) {
                    //et_enterpassword.setError("Must be 6-20 characters, contain at least one digit and one alphabetic character, and can contains special characters !@#$%^&*()") ;
                    weekTxtView.setBackgroundColor(Color.TRANSPARENT);
                    password_strengthTxt.setText("");
                    passwordStrengthLayout.setVisibility(View.GONE);
                    password_strength_check = "Weak";
                }
                if (s.length() != 0) {
                    passwordStrengthLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        String udata = "Terms & Conditions";
        TextView mTextview = (TextView) findViewById(R.id.tv_terms_conditions);
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        mTextview.setText(content);


        mTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String text = Config.readFile(getBaseContext(), "terms_conditions.txt");
                    getAlert(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNo = et_mobilenumber.getText().toString();
                btn_resend.setClickable(false);
                //startCountDown();
                new SendOTPAsync().execute(mobileNo);
            }
        });
        btn_register_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_first.setVisibility(View.GONE);
                layout_registeration.setVisibility(View.VISIBLE);
                which_layout = "register";
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_otp.getText().toString().equals(received_otp)) {
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                    et_otp.setText("");

                    newMemberSignUP();
                   /* layout_otp.setVisibility(View.GONE);
                    layout_setpassword.setVisibility(View.VISIBLE);
                    et_pas_mobileNumber.setText(mobileNo);
                    which_layout = "password";
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                        et_otp.setText("");
                        et_enterpassword.setText("");
                        et_confirmPassword.setText("");
                    }
                */
                } else {
                    Config.getAlert(RegisterActivity.this, "Invalid OTP");
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firstname = et_fistname.getText().toString().trim();
                lastname = et_lastname.getText().toString().trim();
                email = et_email.getText().toString().trim();

                pwd = et_enterpassword.getText().toString().trim();
                cellNumber = et_mobilenumber.getText().toString().trim();
                age = spinner_age.getSelectedItem().toString();
                gender = spinner_gender.getSelectedItem().toString();

                if (firstname.equals("")) {
                    showAlert("Please enter your first name");
                    et_fistname.requestFocus();
                    return;
                }
                if (lastname.equals("")) {
                    showAlert("Please enter your last name");
                    et_lastname.requestFocus();
                    return;
                }
              /*  if (email.equals("")) {
                    showAlert("Please enter your email address");
                    et_email.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showAlert("Please enter a valid email address");
                    et_email.requestFocus();
                    return;
                }*/

                if (cellNumber.equals("") || cellNumber.length() < 11) {
                    showAlert("Please enter mobile number");
                    et_mobilenumber.requestFocus();
                    return;
                }
                if (!cellNumber.startsWith("03")) {
                    showAlert("Please enter a valid mobile number");
                    et_mobilenumber.requestFocus();
                    return;
                }

                if (et_enterpassword.getText().toString().equals("")) {
                    showAlert("Please enter password");
                    return;
                }
                if (!et_enterpassword.getText().toString().equals(et_confirmPassword.getText().toString())) {
                    showAlert("Password and confirm password does not match");
                    return;
                }
                if (et_enterpassword.getText().toString().length() < 6) {
                    showAlert("Password must be atleast 6 characters long");
                    return;
                }

                if (password_strength_check.equalsIgnoreCase("Weak")) {
                    showAlert("Must be 6-20 characters, contain at least one digit and one alphabetic character, and can contains special characters !@#$%^&*()");
                    return;
                }

                if (!checkBox.isChecked()) {
                    showAlert("Terms and conditions must be checked");
                    //et_mobilenumber.requestFocus();
                    return;
                }

                mobileNo = et_mobilenumber.getText().toString();


                if (received_otp.equals("")) {
                    /*et_otp.setText("");
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                    new SendOTPAsync().execute(mobileNo);*/
                    pwd = et_enterpassword.getText().toString();
                    newMemberSignUP();
                } else {
                    which_layout = "otp";
                    layout_registeration.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                }

                //newMemberSignUP();

            }

        });

        ImageView btn_facebook = (ImageView) findViewById(R.id.fb_button);
        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setupFacebook();
                    mAccessTokenTracker.startTracking();
                    mLoginManager.logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "email"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {

        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        };

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(RegisterActivity.this /* FragmentActivity */, mOnConnectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ImageView signInButton = (ImageView) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        btn_registeration_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_enterpassword.getText().toString().equals("")) {
                    showAlert("Please enter password");
                } else if (!et_enterpassword.getText().toString().equals(et_confirmPassword.getText().toString())) {
                    showAlert("Password and confirm password does not match");
                } else if (et_enterpassword.getText().toString().length() < 6) {
                    showAlert("Password must be atleast 6 characters long");
                } else {
                    pwd = et_enterpassword.getText().toString();
                    newMemberSignUP();
                }

            }
        });
        callbackManager = CallbackManager.Factory.create();
    }

    String getAccountId() {
        try {
            account = AccountManager.get(RegisterActivity.this).getAccounts();
        } catch (SecurityException e) {

        }

        for (Account TempAccount : account) {

            if (pattern.matcher(TempAccount.name).matches()) {

                email = TempAccount.name;
                et_email.setText(email);
            }
        }
        return email;
    }

    void startCountDown() {
        btn_resend.setEnabled(false);
        tv_countdown_timer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(countLimit, 1000) {
            public void onTick(long millisUntilFinished) {
                counter--;
                if (counter < 10)
                    tv_countdown_timer.setText(String.valueOf("00:0" + counter));
                else
                    tv_countdown_timer.setText(String.valueOf("00:" + counter));
            }

            public void onFinish() {
                tv_countdown_timer.setText("00:00");
                tv_countdown_timer.setVisibility(View.INVISIBLE);
                btn_resend.setEnabled(true);
                counter = 60;
            }
        }.start();
    }

    private class SendOTPAsync extends AsyncTask<String, String, String> {
        String mobile_no = et_mobilenumber.getText().toString();
        String objResult;
        ProgressDialog pDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(RegisterActivity.this, "Sending OTP", "Please wait...");
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
                if (objResult.toString().contains("Mobile No") || objResult.toString().length() > 10) {
                    getAlert(objResult.toString());
                } else {
                    //Toast.makeText(getBaseContext(), objResult.toString(), Toast.LENGTH_LONG).show();
                    layout_registeration.setVisibility(View.GONE);
                    layout_otp.setVisibility(View.VISIBLE);
                    received_otp = objResult.toString();
                    which_layout = "otp";
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                    startCountDown();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void newMemberSignUP() {
        new CustomAsyncTask().execute();
    }


    class CustomAsyncTask extends AsyncTask<Void, Void, Void> {
        ProgressDialog pd;
        String result;

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(RegisterActivity.this);
            result = conn.CreateUser(email, pwd, name, email, cellNumber, encoded_image, cnic, jsWallet, firstname, lastname, gender, age, register_via);
            return null;
        }

        @Override
        protected void onPostExecute(Void res) {

            super.onPostExecute(res);
            //initRoster();
            try {
                pd.dismiss();
                result = result.replace("-1", "");

                try {
                    if (result.contains("You have been registered successfully")) {
                        showAlert2(result, "Success");
                    } else if (result.toUpperCase().contains("ALREADY_MEMBER") ||
                            result.endsWith("already_member"))
                        showAlert2("The Phone number is already registered.", "Alert");
                    else
                        showAlert2("Error", "Error");

                } catch (Exception e) {
                    showAlert2("Request not completed", "Error");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setTitle("Requesting");
            pd.setMessage("Please wait....");
            pd.setCancelable(false);
            pd.show();
        }
    }

    void showAlert(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message, final String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(RegisterActivity.this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                if (title.equalsIgnoreCase("Success")) {
                    //setUserInfo();
                    getUserInfo();
                    Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });
        adb.show();

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("NewApi")
    /*protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    image = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    if(image!=null){
                        BitmapDrawable myBackground = new BitmapDrawable(image);
                        imgProfileImage.setImageDrawable(myBackground);
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImageUri = imageReturnedIntent.getData();
                    String[] projection = { MediaColumns.DATA };
                    CursorLoader cursorLoader = new CursorLoader(this,selectedImageUri, projection, null, null,
                            null);
                    Cursor cursor =cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    grand int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    image = BitmapFactory.decodeFile(selectedImagePath, options);
                    if(image==null)
                    {
                        Toast.makeText(RegisterActivity.this, "No image was selected. Please try again or use camera for snap", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        BitmapDrawable myBackground = new BitmapDrawable(image);
                        imgProfileImage.setImageDrawable(myBackground);
                    }
                }

                break;

        }
    }*/

    void setUserInfo() {
        // mSharedPreferences.edit().putString(Constants.UserId,encoded_image ).commit();
        mSharedPreferences.edit().putString(Config.EMAIL, email).commit();
        mSharedPreferences.edit().putString(Config.NAME, name).commit();
        mSharedPreferences.edit().putString(Config.FIRST_NAME, firstname).commit();
        mSharedPreferences.edit().putString(Config.LAST_NAME, lastname).commit();
        mSharedPreferences.edit().putString(Config.PICTURE, encoded_image).commit();
        mSharedPreferences.edit().putString(Config.CELL_NO, cellNumber).commit();
        //     mSharedPreferences.edit().putString(Config.dress,address ).commit();


    }

    private void getUserInfo() {

       /* User usr=new User();
        usr.setName(mSharedPreferences.getString(Config.NAME, ""));
        usr.setEmail(mSharedPreferences.getString(Config.EMAIL, ""));
        usr.setPic(mSharedPreferences.getString(Config.PICTURE ));
        usr.setMobileno(mSharedPreferences.getString(Config.CELL_NO, ""));
        usr.setProfile_link(mSharedPreferences.getString(Constants.UserProfileLink, ""));
        usr.setPic(mSharedPreferences.getString(Constants.UserPicture, ""));
        usr.setLoginType(mSharedPreferences.getString(Constants.UserLoginType, ""));
        usr.setLogin_status(mSharedPreferences.getString(Constants.Status, ""));
        usr.setAddress(mSharedPreferences.getString(Constants.UserAddress, ""));
        Constants.user=usr;*/
    }

    private void changeStatusBarColor(String color) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.lv_fix_row_title_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void EnableRuntimePermission() {

        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                {
                        GET_ACCOUNTS,
                        READ_PHONE_STATE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

            try {
                switch (requestCode) {
                    case RequestPermissionCode:

                        if (grantResults.length > 0) {

                            boolean GetAccountPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                            boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                            if (GetAccountPermission && ReadPhoneStatePermission) {

                                //Toast.makeText(RegisterActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                                getAccountId();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();

                            }
                        }

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    class GmailAsynctasc extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    class FacebookAsynctasc extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //   Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        try {
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                final GoogleSignInAccount acct = result.getSignInAccount();
                acct.getDisplayName();
                register_via = "google";
                if (acct.getDisplayName() != null && !acct.getDisplayName().equals("")) {
                    String[] parts = acct.getDisplayName().split("\\s+");
                    f_name = parts[0];
                    l_name = parts[1];
                }
                email_id = acct.getEmail();

                et_fistname.setText(f_name);
                et_lastname.setText(l_name);
                et_email.setText(email_id);

                layout_registeration.setVisibility(View.VISIBLE);
                layout_first.setVisibility(View.GONE);
                which_layout = "register";

                //Config.getAlert(RegisterActivity.this, "Please enter mobile number to verify");

            } else {
                result.isSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        if (mFacebookCallbackManager != null)
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupInit() {
        //FacebookSdk.sdkInitialize(getApplicationContext());
        // Toolbar
            /*setSupportActionBar(mToolbar);
            setTitle(getString(R.string.app_name));*/
        loggedin = false;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (which_layout.equalsIgnoreCase("register") || which_layout.equalsIgnoreCase("form")) {
            which_layout = "first";
            layout_first.setVisibility(View.VISIBLE);
            layout_registeration.setVisibility(View.GONE);
        } else if (which_layout.equalsIgnoreCase("otp")) {
            layout_otp.setVisibility(View.GONE);
            layout_registeration.setVisibility(View.VISIBLE);
            which_layout = "form";
        } else if (which_layout.equalsIgnoreCase("password")) {
            layout_setpassword.setVisibility(View.GONE);
            layout_otp.setVisibility(View.GONE);
            layout_registeration.setVisibility(View.VISIBLE);
            which_layout = "form";
        } else if (which_layout.equalsIgnoreCase("first")) {
            finish();
        }
    }

    private void setupFacebook() {
        mLoginManager = LoginManager.getInstance();
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // handle
            }
        };

        LoginManager.getInstance().registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                if (loginResult.getRecentlyGrantedPermissions().contains("email")) {
                    requestObjectUser(loginResult.getAccessToken());
                } else {
                    LoginManager.getInstance().logOut();
                    Toast.makeText(RegisterActivity.this, "Error permissions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ERROR", error.toString());
            }
        });

        if (AccessToken.getCurrentAccessToken() != null) {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }
    }

    private void requestObjectUser(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    if (response.getError() != null) {
                        // handle error
                    } else {
                        //Toast.makeText(LoginActivity.this, "Access Token: " + accessToken.getToken(), Toast.LENGTH_SHORT).show();
                        loggedin = true;
                        register_via = "facebook";

                        final String email = object.optString("email");
                        final String name = object.optString("first_name") + " " + object.optString("last_name");
                        final String id = object.optString("id");

                        final String f_name = object.optString("first_name");
                        final String l_name = object.optString("last_name");

                        et_fistname.setText(f_name);
                        et_lastname.setText(l_name);
                        et_email.setText(email);

                        layout_registeration.setVisibility(View.VISIBLE);
                        layout_first.setVisibility(View.GONE);
                        which_layout = "register";

                        //Config.getAlert(RegisterActivity.this, "Please enter mobile number to verify");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();

    }

    public class ViewDialog extends AlertDialog {
        String option = "";
        Context context;

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            this.context = context;
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, String name) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setContentView(R.layout.terms_conditions_layout);
            TextView tv = (TextView) dialog.findViewById(R.id.tv);
            Button btn_dismiss = (Button) dialog.findViewById(R.id.dismiss);
            btn_dismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tv.setMovementMethod(new ScrollingMovementMethod());
            try {
                String data = Config.readFile(context, "terms_conditions.txt");
                tv.setText(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();

        }
    }


    public void PasswordValidator() {
        password_pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     *
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password) {

        matcher = password_pattern.matcher(password);
        return matcher.matches();
    }

    void passwordStrength() {
        et_enterpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_enterpassword.setError("Must be 6-20 characters, contain at least one digit and one alphabetic character, and can contains special characters !@#$%^&*()");
                } else {
                    passwordStrengthLayout.setVisibility(View.GONE);
                }
            }
        });

    }

    public static boolean specialCharacterMatch(String string) {
        boolean isContain = false;
        if (string.contains("!") || string.contains("@") || string.contains("#") || string.contains("$") || string.contains("%") || string.contains("^") || string.contains("&") || string.contains("*") || string.contains("(") || string.contains(")")) {
            isContain = true;

        } else {
            isContain = false;
        }
        return isContain;
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
