package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.asynctasks.UpdatePendingTransactionLogs;
import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.TeamInventory;
import com.psl.classes.Transaction_Details;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Belal on 18/09/16.
 */


public class MyInventory extends Fragment {
    String app = "";
    //View view;
    List<InventoryClass> boostersArrayList;
    LinearLayout layoutBoosters;
    SharedPreferences sharedPreferences;
    String userID = "";
    TeamInventory teamInventory;
    View view = null;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        try {
            view = inflater.inflate(R.layout.fragment_my_inventory, container, false);
            try{view.findViewById(R.id.textView11).bringToFront();} catch (Exception e){}

            layoutBoosters = view.findViewById(R.id.layoutBooster);
            Config.PopulateHeader(getActivity(), view.findViewById(R.id.helmet_layout));
            sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
            userID = sharedPreferences.getString(Config.USER_ID, "");
            // view.findViewById(R.id.layout000).bringToFront();
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
            shake.setRepeatCount(-1);
            shake.setRepeatMode(Animation.INFINITE);
            view.findViewById(R.id.layout000).startAnimation(shake);
            view.findViewById(R.id.layout000).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.findViewById(R.id.layout000).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getActivity(),"Coming Soon...",1000).show();
                /*Intent intent=new Intent(getActivity(),DealOfDay.class);
                startActivity(intent);*/
                            DealOfDay dealOfDay = new DealOfDay(getActivity());
                            //dealOfDay.show();
                        }
                    });
                }
            });

            new MyAsynctask().execute();

            view.findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    InventoryHelpDialog helpDialog = new InventoryHelpDialog(getActivity());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    class MyAsynctask extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            new UpdatePendingTransactionLogs(getActivity()).execute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection connection = new Connection(getActivity());
                List<Transaction_Details> transaction_detailses = new ArrayList<>();
                Transaction_Details transaction_details = new Transaction_Details();
                transaction_detailses.add(transaction_details);
                res = connection.getInventryCount(userID);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");
            } else {
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(res);
                teamInventory = xmlParser.getInventoryV2();//new ArrayList<InventoryClass>();
                // inventoryArrayList.addAll(inventoryArrayList);
                populateBoosters();
            }
        }
    }

    void populateBoosters() {
        try {
            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = null;//inflator.inflate(R.layout.activity_, null);
            HashMap<String, String> hashMap = teamInventory.getAllItems();
            String[] array = JSUtils.getArray(hashMap);
            for (int i = 0; i < array.length; i++) {
                myView = inflator.inflate(R.layout.activity_inventory_prototype, null);

                TextView txtName = myView.findViewById(R.id.txtBoosterType);
                TextView txtremain = myView.findViewById(R.id.txtItemPrice);
                ImageView imgBooster = myView.findViewById(R.id.imageView14);
                TextView assign = myView.findViewById(R.id.textView15);

                try {
                    if (array[i].contains("transfer"))
                        sharedPreferences.edit().putString(Config.SWAP_COUNT, hashMap.get(array[i]) + "").commit();
                    int rem = 0;
                    try {
                        rem = Integer.parseInt(hashMap.get(array[i]));
                    } catch (Exception e) {
                    }
                    final int rem2 = rem;
                    txtremain.setText(hashMap.get(array[i]) + "");
                    txtName.setText(JSUtils.capitalize(array[i].replace("_", " ")));
                    imgBooster.setImageResource(JSUtils.getItemResID(txtName.getText().toString()));
                    final String item_name = array[i].toLowerCase();
                    assign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                /*long diff = Config.datesDifference(getActivity());
                                long post_match_diff = Config.getPostDifference(getActivity());

                                if (post_match_diff == 0)
                                    post_match_diff = 31;

                                if (diff > Config.match_time && post_match_diff > Config.post_match_time) {*/

                                    if (rem2 == 0) {
                                        Toast.makeText(getActivity(), "You cannot assign this item", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (item_name.contains("transfer")) {
                                            Fragment fragment = new Team();
                                            Bundle arg = new Bundle();
                                            arg.putString("Is_assign", "");
                                            fragment.setArguments(arg);
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_frame, fragment);
                                            ft.commit();

                                        } else {

                                            Fragment fragment = new Team();
                                            Bundle arg = new Bundle();
                                            arg.putString("Is_assign", "Yes");
                                            fragment.setArguments(arg);
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_frame, fragment);
                                            ft.commit();
                                        }

                                        try {
                                            final RadioButton radiogroup1 = getActivity().findViewById(R.id.iv_fake);
                                            radiogroup1.setChecked(true);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                /*} else {
                                    Config.getAlert(getActivity(), "You cannot edit the team 30 minutes before and after match start time.");
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {

                    myView.findViewById(R.id.layout1).setVisibility(View.GONE);
                }

                layoutBoosters.addView(myView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);

    }

}