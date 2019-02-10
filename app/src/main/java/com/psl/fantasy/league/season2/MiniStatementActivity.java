package com.psl.fantasy.league.season2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.psl.classes.Config;
import com.psl.classes.MiniStatementClass;
import com.psl.transport.Connection;
import com.psl.fantasy.league.season2.R;;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MiniStatementActivity extends AppCompatActivity {

    TextView balStatus;
    Button btnRetry;
    ListView listView;
    String strOTP,strMob,strAuthHeader;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        balStatus=(TextView)findViewById(R.id.txtBalanceInq);
        btnRetry=(Button)findViewById(R.id.button3);
        btnRetry.setVisibility(View.GONE);
        listView=(ListView)findViewById(R.id.listView);
        Intent intent = new Intent(MiniStatementActivity.this,OTPActivity.class);
        strMob=sharedPreferences.getString(Config.JS_Mobile_Number,"");
        intent.putExtra(Config.JS_Mobile_Number,strMob);
        intent.putExtra(Config.JS_OTP_Purpose,"02");

        startActivityForResult(intent,OTPActivity.REQUEST_OTP);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRetry.setVisibility(View.GONE);
                new MyAyncTaskBalanceINQ().execute();
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
            progressDialog = new ProgressDialog(MiniStatementActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            Connection conn=new Connection(MiniStatementActivity.this);
            res=conn.JSMiniStatement(strAuthHeader,strMob,strOTP);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if(res.startsWith("-1") ) {
                //showAlert2(res.replace("-1", ""), "Failure");
                balStatus.setText("Error : "+res.replace("-1", ""));
                btnRetry.setVisibility(View.VISIBLE);
            }
            else if(res.trim().isEmpty())
            {
                balStatus.setText("Error : "+"An error occured. Please try again.");
                btnRetry.setVisibility(View.VISIBLE);
            }
            else {

                try {
                    ArrayList<MiniStatementClass> miniStatementClassArrayList = new ArrayList<MiniStatementClass>();
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("Transactions");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        MiniStatementClass miniStatementClass = new MiniStatementClass();
                        JSONObject jsonObject1 =(JSONObject) jsonArray.get(i);
                        String date=(String)jsonObject1.get("Date");
                        String Description=(String)jsonObject1.get("Description");
                        String Amount=(String)jsonObject1.get("Amount");
                        if(date.trim().equals(""))
                            continue;
                        miniStatementClass.setAmount(Amount.trim());
                        miniStatementClass.setDescription(Description.trim());
                        miniStatementClass.setDate(date.trim());

                        miniStatementClassArrayList.add(miniStatementClass);
                    }

                    MyListAdapter myListAdapter=new MyListAdapter(MiniStatementActivity.this,miniStatementClassArrayList);
                    listView.setAdapter(myListAdapter);

                }
                catch (Exception e){}

            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OTPActivity.REQUEST_OTP) {

            if (resultCode == -1) {

                String message = data.getStringExtra("message");
                showAlert2(message,"Failure",0);
            }

            else if (resultCode == 100) {

                strOTP = data.getStringExtra(Config.JS_Encrypted_OTP);
                strAuthHeader=data.getStringExtra(Config.JS_Auth_Header);
                new MyAyncTaskBalanceINQ().execute();
            }
        }
    }
    void showAlert2(String message,final String title)
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(MiniStatementActivity.this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();

    }
    void showAlert2(String message,final String title,int a)
    {
        AlertDialog.Builder adb=new AlertDialog.Builder(MiniStatementActivity.this);
        adb.setTitle(title);
        adb.setMessage(message);
        adb.setCancelable(false);
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        adb.show();

    }

    class MyListAdapter extends ArrayAdapter<MiniStatementClass>
    {


        ArrayList<MiniStatementClass> mintArrayList;
       // SimpleDateFormat sdf2 ;
        Context context;
        public MyListAdapter(Context context,List<MiniStatementClass> mintArrayList) {
            super(context, R.layout.mini_statement_prototype,mintArrayList );
            this.mintArrayList=(ArrayList<MiniStatementClass>)mintArrayList;
            this.context=context;
          //  sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            //	 sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            //this.s=s;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vieww = convertView;
            if (vieww == null) {
                LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vieww = inflator.inflate(R.layout.mini_statement_prototype, parent, false);
            }
            vieww.setBackgroundColor(0);
            TextView des = (TextView)vieww.findViewById(R.id.txtDescription);
            TextView amount = (TextView) vieww.findViewById(R.id.txtAmount);
            TextView date = (TextView) vieww.findViewById(R.id.txtDate);
            MiniStatementClass visit=mintArrayList.get(position);


            des.setText(visit.getDescription());
            amount.setText(visit.getAmount());
            date.setText(visit.getDate());


            return vieww;
        }
    }
}
