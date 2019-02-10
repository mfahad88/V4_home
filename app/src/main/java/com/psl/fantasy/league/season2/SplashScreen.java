package com.psl.fantasy.league.season2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesVO;
import com.psl.classes.JavaNI;
import com.psl.classes.JsLocationsVO;
import com.psl.classes.MD5Utils;
import com.psl.classes.PlayerProfileVO;
import com.psl.classes.XMLParser;
import com.psl.fantasy.league.season2.R;;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;
    List<JsLocationsVO> locationsList;
    DatabaseHandler dbhandler;
    String user_id = "";
    ProgressBar progressBar;
    SharedPreferences sharedPreference;
    String imei;
    String device_information;
    MediaPlayer audioPlayer;
    public static final int MY_PERMISSIONS_REQUEST_STORAGE = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkLocationPermission();
       /*String key = new String("65412399991212FF");//65412399991212FF65412399991212FF");
      String stringtoenc="56808";
     String  encrptedOTP = EncryptionUtil.encryptWithAES(key, stringtoenc);*/

    //   createNotification2("This message one This message one This message one This message one This message one This message one ","Message 1 Title*");
      //createNotification2("This message one This message one This message one This message one This message one This message one ","Message 2 Title");

     /*   Intent intent = new Intent(SplashScreen.this, BiometricActivity.class);
        intent.putExtra("CNIC", "3740503452271");
        intent.putExtra("MOBILE", "03355100042");
        intent.putExtra("OTP", "");
        intent.putExtra("AUTH", "");
        startActivity(intent);
        startActivity(new Intent(SplashScreen.this,TestBioActivity.class));
        finish();
*/

      // createNotification2("This is method body","Test Title");

        audioPlayer= MediaPlayer.create(SplashScreen.this,R.raw.apni_audio);
        audioPlayer.start();

        try {
            //lx1();

            callAccessories();

            //loadIMEI();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //callAccessories();
       /* try {
            sharedPreference = SplashScreen.this.getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            grand String guid = sharedPreference.getString(Config.GUID, "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (guid.trim().equals(""))
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        DatabaseHandler dbHandler = new DatabaseHandler(getBaseContext());
                        dbHandler.deleteAll();
                        dbHandler.deleteAllFixtures();
                        SharedPreferences.Editor editor = sharedPreference.edit();
                        editor.clear().commit();
                        editor.putString(Config.GUID, guid).commit();
                    }

                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }

        changeStatusBarColor("");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        try {

            sharedPreference = SplashScreen.this.getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            user_id = sharedPreference.getString(Config.USER_ID, "");

            if (user_id == null || user_id.equals(""))
                user_id = "-1";

            dbhandler = new DatabaseHandler(SplashScreen.this);

            //new GetUserFantasyTeam().execute();
            new GetFixturesAsync().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }
    private void checkLocationPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreen.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    new AlertDialog.Builder(SplashScreen.this)
                            .setTitle("Storage Permission Needed")
                            .setMessage("This app needs the Storage permission, please accept to use location functionality")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        //Prompt the user once explanation has been shown
                                        ActivityCompat.requestPermissions(SplashScreen.this,
                                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                MY_PERMISSIONS_REQUEST_STORAGE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .create()
                            .show();


                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(SplashScreen.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_STORAGE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void loadIMEI() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 102);
                }
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                device_information = Build.MANUFACTURER
                        + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                        + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();

                imei = telephonyManager.getDeviceId();
                //Toast.makeText(getBaseContext(), imei + " " + device_information, Toast.LENGTH_LONG).show();

                callAccessories();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetRuleBook extends AsyncTask<String, String, String> {
        String mResult;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection("rulebook_text",SplashScreen.this);
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.getPropertyAsString("rulebook_textResult");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                if(mResult !=null)
                    Config.RULE_BOOK_TEXT = mResult;
                //Toast.makeText(getBaseContext(), Config.RULE_BOOK_TEXT, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(SplashScreen.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        //makeDir();

                        // getCurrentLocationAndDrawMarkers();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SplashScreen.this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            try {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        imei = telephonyManager.getImei();
                    } else {
                        imei = telephonyManager.getDeviceId();
                    }
                }
                device_information = Build.MANUFACTURER
                        + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                        + " " + Build.VERSION_CODES.class.getFields()[Build.VERSION.SDK_INT].getName();

                //Toast.makeText(getBaseContext(), imei + " " + device_information, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        callAccessories();

    }

    private void lx1() {
        try {
            String md5 = loadSignatureMd5ByJava();
            BigInteger bigInteger = new BigInteger(md5, (8 + 8));
            md5 = bigInteger.toString((1 + 1));
            Config.w0 = resultant(  JavaNI.U1(md5));
            Config.w1= resultant  (   JavaNI.U2(md5));
            Config.w2= resultant(   JavaNI.U3(md5));
            Config.w4 = resultant(  JavaNI.U4(md5));
            Config.w3 = resultant(  JavaNI.U5(md5));
            Config.w5 = resultant(  JavaNI.U6(md5));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void importDatafromExcel() {
        try {
            AssetManager am = getAssets();// If this line gives you ERROR then try AssetManager am=getActivity().getAssets();
            InputStream is = am.open("js_locations.xls");
            Workbook wb = Workbook.getWorkbook(is);
            Sheet s = wb.getSheet(0);
            int row = s.getRows();
            int col = s.getColumns();
            String xx = "";
            JsLocationsVO location;
            for (int i = 0; i < row; i++) {
                if (i > 0) {
                    location = new JsLocationsVO();
                    for (int c = 0; c < col; c++) {
                        Cell z = s.getCell(c, i);
                        if (c == 0)
                            location.setLoc_name(z.getContents());
                        else if (c == 1)
                            location.setAddress(z.getContents());
                        else if (c == 2)
                            location.setArea(z.getContents());
                        else if (c == 3)
                            location.setCategory(z.getContents());
                        else if (c == 4)
                            location.setLatitude(z.getContents());
                        else if (c == 5)
                            location.setLongitude(z.getContents());
                        xx = xx + z.getContents();
                    }
                    locationsList.add(location);
                    //xx = xx + "\n";
                }
            }
            dbhandler.saveJsLocations(locationsList);
        } catch (Exception e) {
        }
    }

    private class GetUserFantasyTeam extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<PlayerProfileVO> selectedPlayerList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(SplashScreen.this);
                mResult = connection.getUserFantasyTeam(user_id);

                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    selectedPlayerList = xmp.getUserFantasyteam("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {

                dbhandler.deleteAll();

                if (selectedPlayerList != null && selectedPlayerList.size() > 0)
                    dbhandler.saveSelectedPlayers(selectedPlayerList);

                new GetFixturesAsync().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetFixturesAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<FixturesVO> fixruresList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                fixruresList = dbhandler.getFixtures();
                //if (fixruresList.size() == 0) {

                Connection connection = new Connection(SplashScreen.this);
                mResult = connection.getFixturesData("fixtures");
                fixruresList = new ArrayList<FixturesVO>();
                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    fixruresList = xmp.getFixturesData();
                }
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            try {
                List<FixturesVO> list = dbhandler.getFixtures();
                //if (list == null || list.size() == 0) {
                dbhandler.deleteAllFixtures();
                dbhandler.saveFixtures(fixruresList);
                // }


                if (!sharedPreference.getString(Config.EMAIL, "").equals("")) {
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

                //new GetFixturesAsyncAll().execute();
               // new GetRuleBook().execute();

                //locationsList = new ArrayList<JsLocationsVO>();
               // List<JsLocationsVO> loc_list = dbhandler.getJsLocations();



                /*if (loc_list == null || loc_list.size() == 0)
                    importDatafromExcel();*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] h2(String str)
    {
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < bytes.length; i++)
        {
            bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), (9+7));
        }
        return bytes;
    }
    String resultant(String s)
    {
        String text="";
        try {


            BigInteger b = new BigInteger(s, (5-3));
            byte[] bytes = h2(b.toString((10+6)));
            String s22222 = Base64.encodeToString(bytes, Base64.DEFAULT);
            byte[] b2 = Base64.decode(s22222, Base64.DEFAULT);
            text = new String(b2, "UTF-8");
            return text;
        }
        catch (Exception e)
        {
            return "";
        }
    }

    private String loadSignatureMd5ByJava() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        return MD5Utils.md5(packageInfo.signatures[0].toCharsString());
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

    void callAccessories() {
        try {
            sharedPreference = SplashScreen.this.getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            final String guid = sharedPreference.getString(Config.GUID, "");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (guid.trim().equals(""))
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {

                        DatabaseHandler dbHandler = new DatabaseHandler(getBaseContext());
                        dbHandler.deleteAll();
                        dbHandler.deleteAllFixtures();
                        SharedPreferences.Editor editor = sharedPreference.edit();
                        editor.clear().commit();
                        editor.putString(Config.GUID, guid).commit();
                    }

                }
            }).start();


        } catch (Exception e) {
            e.printStackTrace();
        }

        changeStatusBarColor("");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        try {

            sharedPreference = SplashScreen.this.getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            user_id = sharedPreference.getString(Config.USER_ID, "");

            if (user_id == null || user_id.equals(""))
                user_id = "-1";

            dbhandler = new DatabaseHandler(SplashScreen.this);

            //new GetUserFantasyTeam().execute();
            new GetFixturesAsync().execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeDir()  {
        File myDirectory = new File(Environment.getExternalStorageDirectory()+"/ACL");

        if(!myDirectory.exists()) {
            myDirectory.mkdirs();
            File gpxfile = new File(myDirectory+"/IP.txt");
            FileWriter writer = null;
            try {
                writer = new FileWriter(gpxfile);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private class GetFixturesAsyncAll extends AsyncTask<String, String, String> {
        String objResult;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (Config.FIXTURES_LIST == null || Config.FIXTURES_LIST.size() == 0) {

                    Connection connection = new Connection(SplashScreen.this);
                    mResult = connection.getFixturesDataAll("fixtures_new");
                    Config.FIXTURES_LIST = new ArrayList<FixturesVO>();
                    if (mResult != null && !mResult.equals("")) {
                        XMLParser xmp = new XMLParser();
                        xmp.parse(mResult);
                        Config.FIXTURES_LIST = xmp.getFixturesData();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




}