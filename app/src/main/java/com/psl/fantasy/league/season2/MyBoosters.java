package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.BoosterVO;
import com.psl.classes.Config;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.List;

public class MyBoosters extends Fragment {


    //View view;
    List<BoosterVO> boostersArrayList;
    LinearLayout layoutBoosters;
    SharedPreferences sharedPreferences;
    TextView coins_field;
    TextView rank_field;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
            userID = sharedPreferences.getString(Config.USER_ID, "");
            view = inflater.inflate(R.layout.activity_my_boosters, container, false);
            try{view.findViewById(R.id.textView11).bringToFront();} catch (Exception e){}

            layoutBoosters = view.findViewById(R.id.layoutBooster);
            Config.PopulateHeader(getActivity(), view.findViewById(R.id.helmet_layout));

            new MyAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    void populateBoosters() {
        layoutBoosters.removeAllViews();
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = null;//inflator.inflate(R.layout.activity_, null);

        for (int i = 0; i < boostersArrayList.size(); i++) {
            myView = inflator.inflate(R.layout.activity_boosters_prototype, null);
            final BoosterVO boosterVO = boostersArrayList.get(i);
            TextView txtPoints = myView.findViewById(R.id.txtPoints);
            TextView txtBoosterType = myView.findViewById(R.id.txtBoosterType);
            Button btnRedeem = myView.findViewById(R.id.btnRedeem);

            // txtPoints.setText(boosterVO.getBoosterPoints());
            try {
                txtPoints.setText(Config.format(boosterVO.getBoosterPoints() + ""));
            }
            catch (Exception e){}
            final int pos = i;
            btnRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AsyncTask<Void, Void, Void>() {

                        ProgressDialog progressDialog;
                        String res = "";

                        @Override
                        protected void onPreExecute() {
                            try {
                                super.onPreExecute();
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setMessage("Loading...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {

                                Connection connection = new Connection(getActivity());
                                res = connection.redeemBoosters(boosterVO.getID(), userID);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            progressDialog.dismiss();
                            if (res.trim().toLowerCase().equals("success")) {
                                Toast.makeText(getActivity(), "Redeemed Successfully", 1000).show();


                                try {
                                    int point = Integer.parseInt(sharedPreferences.getString(Config.USER_BUDGET, "0"));
                                    int boosterPoint = Integer.parseInt(boosterVO.getBoosterPoints());
                                    int totalPoint = point + boosterPoint;
                                    sharedPreferences.edit().putString(Config.USER_BUDGET, totalPoint + "").commit();
                                    coins_field.setText(sharedPreferences.getString(Config.USER_BUDGET, "100000"));

                                    boostersArrayList.remove(pos);
                                } catch (Exception e) {
                                }
                                populateBoosters();


                                //showAlert2(boosterVO.getBoosterPoints()+" points are successfully added to your account.","Success");
                            } else {
                                showAlert2(res.replace("-1", ""), "Failure");

                                if (res.trim().toLowerCase().contains("already")) {
                                    boostersArrayList.remove(pos);
                                    populateBoosters();
                                }
                            }

                        }
                    }.execute();
                }
            });
            layoutBoosters.addView(myView);
        }
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection connection = new Connection(getActivity());
                res = connection.getMyBoosters(userID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                progressDialog.dismiss();
                if (res.startsWith("-1")) {
                    showAlert2(res.replace("-1", ""), "Failure");

                } else {
                    XMLParser xmlParser = new XMLParser();
                    xmlParser.parse(res);
                    boostersArrayList = xmlParser.getMyBoosters();
                    populateBoosters();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);
        if (title.equals("Success"))
            getActivity().recreate();


    }

}
