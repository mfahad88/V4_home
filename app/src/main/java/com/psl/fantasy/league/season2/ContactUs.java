package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import com.psl.fantasy.league.season2.R;;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.psl.classes.Config;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob
 */


public class ContactUs extends Fragment {
    SharedPreferences sharedPreferences;
    EditText name, mobile, email, question;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contactus, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        name = (EditText) view.findViewById(R.id.txt_name);
        mobile = (EditText) view.findViewById(R.id.txt_mobile);
        email = (EditText) view.findViewById(R.id.txt_email);
        question = (EditText) view.findViewById(R.id.txt_question);

        name.setText(sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));
        mobile.setText(sharedPreferences.getString(Config.CELL_NO, ""));
        email.setText(sharedPreferences.getString(Config.EMAIL,""));

        ((Button) view.findViewById(R.id.iv_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (name.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your name");
                } else if (mobile.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your mobile number");
                } else if (question.getText().toString().equals("")) {
                    Config.getAlert(getActivity(), "Please enter your question");
                } else {
                    new SendEmail().execute(name.getText().toString(), email.getText().toString(), mobile.getText().toString(), question.getText().toString());
                }
            }
        });

        return view;
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("yaqoobskhan@gmail.com"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            //Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private class SendEmail extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;
        String mResult;
        String user_id = sharedPreferences.getString(Config.USER_ID, "");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Submitting", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection connection = new Connection("send_email", getActivity());

                connection.addProperties("user_id", user_id);
                connection.addProperties("name", params[0]);
                connection.addProperties("email", params[1]);
                connection.addProperties("mobile", params[2]);
                connection.addProperties("question", params[3]);
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (pDialog != null)
                    pDialog.dismiss();

                if (mResult.contains("success")) {
                    Config.getAlert(getActivity(), "Your question has been submitted", "Success");
                    question.setText("");
                    RadioButton rb = (RadioButton)getActivity().findViewById(R.id.iv_team);
                    rb.setChecked(true);
                } else {
                    Config.getAlert(getActivity(), "Failed to submit, try again");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}