package com.psl.fantasy.league.season2;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.psl.asynctasks.VersionChecker;
import com.psl.classes.Config;
import com.psl.classes.User;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.io.ByteArrayOutputStream;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    String f_name = "";
    String l_name = "";

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
    String register_via = "";

    TextView tv_forgotPin;

    void showAlert(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message, final String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", null);
        adb.show();

    }


    void getAlert(String text) {
        new ViewDialog(getBaseContext()).showDialog(LoginActivity.this, "");
        /*new AlertDialog.Builder(LoginActivity.this)
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
    protected void onResume() {
        super.onResume();
        if (Config.w0.equals("")) {
            startActivity(new Intent(LoginActivity.this, SplashScreen.class));
            return;
        }
    }

    public int compareVersionNames(String oldVersionName, String newVersionName) {
        int res = 0;

        String[] oldNumbers = oldVersionName.split("\\.");
        String[] newNumbers = newVersionName.split("\\.");

        // To avoid IndexOutOfBounds
        int maxIndex = Math.min(oldNumbers.length, newNumbers.length);

        for (int i = 0; i < maxIndex; i++) {
            int oldVersionPart = Integer.valueOf(oldNumbers[i]);
            int newVersionPart = Integer.valueOf(newNumbers[i]);

            if (oldVersionPart < newVersionPart) {
                res = -1;
                break;
            } else if (oldVersionPart > newVersionPart) {
                res = 1;
                break;
            }
        }

        // If versions are the same so far, but they have different length...
        if (res == 0 && oldNumbers.length != newNumbers.length) {
            res = (oldNumbers.length > newNumbers.length) ? 1 : -1;
        }

        return res;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        if (Config.w0.equals("")) {
            startActivity(new Intent(LoginActivity.this, SplashScreen.class));
            return;
        }
        //FacebookSdk.sdkInitialize(this.getApplicationContext());

        setContentView(R.layout.activity_login);

        try {
            // int res=compareVersionNames("1.8.4","1.8.3");

            VersionChecker versionChecker = new VersionChecker(LoginActivity.this);
            String playStoreVersion = versionChecker.execute().get();

            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String runningVersion = pInfo.versionName;
            int res = compareVersionNames(runningVersion, playStoreVersion);
            if (res < 0) {
                AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
                adb.setMessage("New vesrion of application is available on Play Store.");
                adb.setTitle("Update Application");
                adb.setCancelable(false);
                adb.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        } finally {
                            finish();
                        }
                    }
                });
                adb.show();

            }
            //Toast.makeText(SplashScreen.this,res+"", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        String udata = "Terms & Conditions";
        TextView mTextview = (TextView) findViewById(R.id.terms);
        SpannableString content = new SpannableString(udata);
        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        mTextview.setText(content);

        changeStatusBarColor("");

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

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_ICON_ONLY);
        //signInButton.setBackgroundResource(0);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        ((TextView) findViewById(R.id.txt_ResetPin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPin.class));
            }
        });

        userName = (EditText) findViewById(R.id.txt_MobileNumber);
        userPwd = (EditText) findViewById(R.id.txt_Pin);
        loginBtn = (Button) findViewById(R.id.btn_Login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = userName.getText().toString();
                final String pwd = userPwd.getText().toString();
                if (email.equals("")) {
                    showAlert("Please enter your mobile number");
                    userName.requestFocus();
                    return;
                }
              /*  if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    showAlert("Please enter a valid email address");
                    userName.requestFocus();
                    return;
                }*/

                if (pwd.equals("")) {
                    showAlert("Please enter password");
                    userPwd.requestFocus();
                    return;
                }
                new AsyncTask<Void, Void, Void>() {
                    ProgressDialog progressDialog;
                    String res = "";

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        try {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            res = res.replace("-1", "");
                            if (res.endsWith("UNVERIFIED")) {
                                progressDialog.dismiss();
                                userPwd.setText("");
                                showAlert2("Invalid mobile number or password", "Login Fail");

                            } else if (res.endsWith("VERIFIED")) {
                                userName.setText("");
                                userPwd.setText("");

                                res = res.replace("VERIFIED", "");
                                XMLParser xmp = new XMLParser();
                                xmp.parse(res);
                                User mUser = xmp.getMyProfileLogin();
                                if (mUser != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(Config.USER_ID, mUser.getId());
                                    editor.putString(Config.USER_KEY, pwd);
                                    editor.putString(Config.NAME, mUser.getName());
                                    editor.putString(Config.EMAIL, mUser.getEmail());
                                    editor.putString(Config.CNIC, mUser.getCnic());
                                    editor.putString(Config.CELL_NO, mUser.getCellNo());
                                    editor.putString(Config.GENDER, mUser.getGender());
                                    editor.putString(Config.AGE, mUser.getAge());
                                    editor.putString(Config.FIRST_NAME, mUser.getFirst_name());
                                    editor.putString(Config.LAST_NAME, mUser.getLast_name());

                                    editor.putString(Config.IMAGE_DATA, mUser.getPicture());
                                    editor.putString(Config.JS_Mobile_Number, mUser.getJswallet());
                                    if (!mUser.getJswallet().trim().equals("")) {
                                        editor.putBoolean(Config.JS_WALLET_ACCOUNT, true);
                                    }
                                    editor.putString(Config.TEAM_ID, mUser.getTeam_id());
                                    editor.putString(Config.TEAM_NAME, mUser.getTeam_name());
                                    editor.putString(Config.USER_RANK, mUser.getUser_ranking());
                                    editor.putString(Config.USER_BUDGET, mUser.getUser_points());
                                    editor.putString(Config.SWAP_COUNT, mUser.getSwaps());
                                    editor.putString(Config.USER_RANK, mUser.getUser_ranking());

                                    editor.putString(Config.USER_PURPLE_CAP, mUser.getUser_purple_cap());
                                    editor.putString(Config.USER_ORANGE_CAP, mUser.getUser_orange_cap());
                                    editor.putString(Config.USER_GOLDEN_GLOVES, mUser.getUser_goldengloves());
                                    editor.putString(Config.USER_ICONIC_PLAYER, mUser.getUser_iconic_player());
                                    editor.putString(Config.USER_TEAM_SAFETY, mUser.getUser_teamsafety());
                                    editor.putString(Config.USER_PLAYER_SAFETY, mUser.getUser_playersafety());


                                    editor.commit();

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else
                                    showAlert2("Login fails due to internal server error. Please try later", "Error");
                            } else {
                                showAlert2(res.replace("-1", ""), "Error");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected Void doInBackground(Void... params) {

                        try {
                            Connection conn = new Connection(LoginActivity.this);
                            res = conn.VerifyUser(email, pwd);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setCancelable(false);
                        progressDialog.setTitle("Please Wait");
                        progressDialog.setMessage("Verifying...");
                        progressDialog.show();

                    }

                }.execute();
            }
        });
        //loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions("email");
        //loginButton.setBackgroundResource(R.drawable.btn_facebook);
        //loginButton.setBackgroundResource(R.drawable.com_facebook_button_background);
        //callbackManager = CallbackManager.Factory.create();
        // If using in a fragment

        ((Button) findViewById(R.id.btn_Registr)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        //yaqoob


        // Other app specific specialization
        // Callback registration

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //Yaqoob
    private void setupInit() {
        //FacebookSdk.sdkInitialize(getApplicationContext());
        // Toolbar
            /*setSupportActionBar(mToolbar);
            setTitle(getString(R.string.app_name));*/

        loggedin = false;
    }


    String BMPToB64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();


        String b64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return b64;
    }

    private boolean checkAndRequestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCallingPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        0);
            }
        } else {

        }
        return true;
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

    public static class ViewDialog extends AlertDialog {
        String option = "";
        Context context;

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            this.context = context;
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, String name) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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


}