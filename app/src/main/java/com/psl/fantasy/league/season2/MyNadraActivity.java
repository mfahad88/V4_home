package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.google.gson.Gson;
import com.paysyslabs.instascan.Fingers;
import com.paysyslabs.instascan.NadraActivity;
import com.paysyslabs.instascan.NadraScanListener;
import com.paysyslabs.instascan.model.PersonData;
import com.psl.classes.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyNadraActivity extends NadraActivity implements NadraScanListener {
    public static final int REQUEST_NADRA = 150;

    public static final Gson GSON = new Gson();
    private static final String TAG = MyNadraActivity.class.getName();

    private ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_nadra);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading");
        dialog.setCancelable(false);

        String cnic = getIntent().getStringExtra("cnic");
        String fingerValue = getIntent().getStringExtra("finger");
        Fingers finger = Fingers.valueOf(fingerValue);

        initializeNadraActivity(this, cnic, finger);
        Toast.makeText(this, "Place your "+fingerValue.replace("_"," ")+" finger in front of camera", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNadraBaseRequestStarted() {
       super.onNadraBaseRequestStarted();
       if(!dialog.isShowing()) {
           try {
               dialog.show();
           } catch (Exception e) {
               e.printStackTrace();
               Toast.makeText(MyNadraActivity.this, "Please wait verifying.....", Toast.LENGTH_LONG).show();
           }
       }
    }

    @Override
    public void onNadraBaseError(String s, String s1) {
        super.onNadraBaseError(s, s1);
    }

    @Override
    public void onSuccessfulScan(PersonData personData) {
        Log.e(TAG, "onSuccessfulScan");
        getIntent().putExtra("person", GSON.toJson(personData));
        setResult(100, getIntent());
        finish();
    }

    @Override
    public void onInvalidFingerIndex(String s, String s1, List<Fingers> list) {
        Log.e(TAG, "onInvalidFingerIndex");
        getIntent().putExtra("code", s);
        getIntent().putExtra("message", s1);
        getIntent().putExtra("fingers", GSON.toJson(list));

        setResult(-2, getIntent());
        finish();

    }

    @Override
    public void onError(String s, String s1) {
        Log.e(TAG, "CODE: " + s + " MESSAGE: " + s1);
        getIntent().putExtra("code", s);
        getIntent().putExtra("message", s1);
        setResult(-1, getIntent());
        finish();
    }

    @Override
    public void onRequestStarted() {
        Log.e(TAG, "onRequestStarted");
        if(!dialog.isShowing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MyNadraActivity.this, "Please wait verifying.....", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResponseReceived() {
        Log.e(TAG, "onResponseReceived");
        dialog.dismiss();
    }
    @Override
    public int getScanFragmentContainer() {
        return R.id.mainLayout;
    }
    @Override
    public String getLicenseKey() {
        return "33ae95cb79474d048f9fb63453e646ff";
    }
    @Override
    public boolean shouldUseLegacyCaptureFrame() {
        return false;//super.shouldUseLegacyCaptureFrame();
    }

    @Override
    public boolean shouldRetryOnBadCapture() {
        Toast.makeText(this,"Finger Print not clear place finger again",1000).show();
        return  true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public String getCustomProxyURL() {
      // return "https://sandbox.jsbl.com/userauth/bvs/v0/verify";
      return Config.w5 +"/userauth/bvs/v1/verify";
    }

    @Override
    public String getCustomCookie() {
        return "JSESSIONID=123";
    }

    @Override
    public boolean useCustomProxy() {
        return true;
    }



    @Override
    public Map<String, String> getCustomAuthenticationData() {
        HashMap<String, String> authorizationData = new HashMap<>();
        authorizationData.put("client_id", Config.w3);
        authorizationData.put("client_secret", Config.w4);
        return authorizationData;
    }

}
