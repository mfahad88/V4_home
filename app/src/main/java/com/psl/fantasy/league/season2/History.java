package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import com.psl.fantasy.league.season2.R;;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.*;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob on 13/01/18.
 */

public class History extends Fragment {
    public static SharedPreferences sharedPreference;
    public static TextView[] tv_nameArray, tv_priceArray;
    public static DatabaseHandler dbHandler;
    CountDownTimer timer;
    public static LinearLayout[] layout_array;
    //List<PlayerProfileVO> list;
    public static ImageView[] iv_array_pp, iv_array_oc, iv_array_gg, iv_array_ip, iv_array_ts, iv_array_ps;
    ProgressBar pb_fixtures;
    public static ProgressBar pBar_history;
    ListView listview;
    public static TextView tv_total_points;
    public static Dialog dialog;
    public static String share_type = "text";
    public static String share_text = "https://play.google.com/store/apps/details?id=com.psl.fantasy.league.season2 #JSApniCricketLeague #JSBank";
    public static Context context;
    public static int row_id = 0;
    public static boolean IS_METHOD_RUNNING = false;
    public static boolean IS_CURRENT_DAY = false;
    public static String user_id = "";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_history, container, false);
            context = getActivity();
            pb_fixtures = (ProgressBar) view.findViewById(R.id.progressBar_history);
            sharedPreference = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            user_id = sharedPreference.getString(Config.USER_ID, "");
            listview = (ListView) view.findViewById(R.id.lv_fixtures);
            dbHandler = new DatabaseHandler(getActivity());

            if (Config.FIXTURES_LIST == null || Config.FIXTURES_LIST.size() == 0) {
                new GetFixturesAsync().execute();
            } else {
                pb_fixtures.setVisibility(View.GONE);
                FixturesAdapter adapter = new FixturesAdapter(getActivity(), Config.FIXTURES_LIST, "history");
                listview.setAdapter(adapter);

            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (datesDifference(Config.FIXTURES_LIST.get(position).getDate())) {
                        Config.getAlert(getActivity(), "No Record Found");
                    } else {
                        row_id = position;
                        String match = Config.FIXTURES_LIST.get(position).getMatch();
                        String date_venue = Config.FIXTURES_LIST.get(position).getDate().split(" ")[0] + " " + Config.FIXTURES_LIST.get(position).getDate().split(" ")[1] + " " + Config.FIXTURES_LIST.get(position).getDate().split(" ")[2] + " " + Config.FIXTURES_LIST.get(position).getVenue_name().replace("Cricket Stadium", "").replace("International", "");
                        String match_id = Config.FIXTURES_LIST.get(position).getId();
                        new ViewDialog(getActivity()).showDialog(match, date_venue, match_id);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");
    }


    private class GetFixturesAsync extends AsyncTask<String, String, String> {
        String objResult;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pb_fixtures.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if (Config.FIXTURES_LIST == null || Config.FIXTURES_LIST.size() == 0) {

                    Connection connection = new Connection(getActivity());
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

                pb_fixtures.setVisibility(View.GONE);

                FixturesAdapter adapter = new FixturesAdapter(getActivity(), Config.FIXTURES_LIST, "history");
                ((ListView) getActivity().findViewById(R.id.lv_fixtures)).setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class GetMatchWiseHistory extends AsyncTask<String, String, String> {
        String objResult;
        String mResult;
        String match_id = "";
        List<PlayerProfileVO> selectedPlayerList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(pBar_history !=null) {

                if (pBar_history.getVisibility() == View.GONE)
                    pBar_history.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                IS_METHOD_RUNNING = true;
                match_id = params[1];
                selectedPlayerList = dbHandler.getSelectedMatchHistory(match_id, user_id);

                if ((selectedPlayerList == null
                        || selectedPlayerList.size() < 1) || IS_CURRENT_DAY) {

                    Connection connection = new Connection(context);
                    mResult = connection.getMatchWiseInventory(params[0], params[1]);


                    if (mResult != null && !mResult.equals("")) {
                        XMLParser xmp = new XMLParser();
                        xmp.parse(mResult);
                        selectedPlayerList = xmp.getUserFantasyteam("");
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

                if (pBar_history != null)
                    pBar_history.setVisibility(View.GONE);

                //List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();

                if (selectedPlayerList != null && selectedPlayerList.size() > 0) {
                    for (int i = 0; i < selectedPlayerList.size(); i++) {
                        tv_nameArray[i].setText(selectedPlayerList.get(i).getPlayer_name());
                        //tv_priceArray[i].setText(selectedPlayerList.get(i).getPrice() + Config.getTeamName(selectedPlayerList.get(i).getTeam_id()));

                        try {
                            if (selectedPlayerList.get(i).getPrice().equalsIgnoreCase(""))
                                tv_priceArray[i].setText("0");
                            else
                                tv_priceArray[i].setText(selectedPlayerList.get(i).getPrice());
                        } catch (Exception e) {
                            tv_priceArray[i].setText("0");
                            e.printStackTrace();
                        }

                        if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("All Rounder"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.allrounder_field_icon, 0, 0, 0);

                        if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("Bowler"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.bowler_field_icon, 0, 0, 0);

                        if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("Batsman"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.batsman_field_icon, 0, 0, 0);

                        if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                        if (selectedPlayerList.get(i).getIsCaptain().equals("1") && selectedPlayerList.get(i).getIsMom().equals("1"))
                            layout_array[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mom_captain_field_large));
                        else if (selectedPlayerList.get(i).getIsCaptain().equals("1"))
                            layout_array[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.captain_field));
                        else if (selectedPlayerList.get(i).getIsMom().equals("1"))
                            layout_array[i].setBackgroundDrawable(context.getResources().getDrawable(R.drawable.mom_field));

                        if (selectedPlayerList.get(i).getIsPrurplecap().equals("1"))
                            iv_array_pp[i].setVisibility(View.VISIBLE);
                        if (selectedPlayerList.get(i).getIsOrangecap().equals("1"))
                            iv_array_oc[i].setVisibility(View.VISIBLE);
                        if (selectedPlayerList.get(i).getIsGoldengloves().equals("1"))
                            iv_array_gg[i].setVisibility(View.VISIBLE);
                        if (selectedPlayerList.get(i).getIsIconic().equals("1"))
                            iv_array_ip[i].setVisibility(View.VISIBLE);
                        if (selectedPlayerList.get(i).getIsSafety().equals("1"))
                            iv_array_ps[i].setVisibility(View.VISIBLE);
                    }
                    int total_points = 0;
                    for (int i = 0; i < selectedPlayerList.size(); i++) {
                        try {
                            if (selectedPlayerList.get(i).getTotal_points() != null)
                                total_points += Integer.parseInt(selectedPlayerList.get(i).getTotal_points());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    tv_total_points.setText("Total Points: " + String.valueOf(total_points));

                } else {
                    //if (dialog != null)
                      //  dialog.dismiss();

                    Config.getAlert(context, "No Record Found");
                    tv_total_points.setText("No Record Found");

                }

                if (dbHandler.getSelectedMatchHistory(match_id, user_id) == null
                        || dbHandler.getSelectedMatchHistory(match_id, user_id).size() == 0) {


                    if(IS_CURRENT_DAY == false) {
                        if(selectedPlayerList.size() == 0)
                        {
                            PlayerProfileVO player = new PlayerProfileVO();
                            player.setTotal_points("0");
                            selectedPlayerList.add(player);
                        }
                        dbHandler.saveHistoryData(selectedPlayerList, match_id, user_id);
                    }
                }

                IS_METHOD_RUNNING = false;
            } catch (Exception e) {
                e.printStackTrace();

                IS_METHOD_RUNNING = false;

                if (dialog != null)
                    dialog.dismiss();
                tv_total_points.setText("No Record Found");
                Config.getAlert(context, "No Record Found");

            }
        }
    }

    public static void getAllImagesArray(Dialog dialog, String match_id) {

        iv_array_pp = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_pp_one),
                        (ImageView) dialog.findViewById(R.id.iv_pp_two),
                        (ImageView) dialog.findViewById(R.id.iv_pp_three),
                        (ImageView) dialog.findViewById(R.id.iv_pp_four),
                        (ImageView) dialog.findViewById(R.id.iv_pp_five),
                        (ImageView) dialog.findViewById(R.id.iv_pp_six),

                        (ImageView) dialog.findViewById(R.id.iv_pp_seven),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eight),
                        (ImageView) dialog.findViewById(R.id.iv_pp_nine),
                        (ImageView) dialog.findViewById(R.id.iv_pp_ten),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eleven),

                };


        iv_array_oc = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_oc_one),
                        (ImageView) dialog.findViewById(R.id.iv_oc_two),
                        (ImageView) dialog.findViewById(R.id.iv_oc_three),
                        (ImageView) dialog.findViewById(R.id.iv_oc_four),
                        (ImageView) dialog.findViewById(R.id.iv_oc_five),
                        (ImageView) dialog.findViewById(R.id.iv_oc_six),

                        (ImageView) dialog.findViewById(R.id.iv_oc_seven),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eight),
                        (ImageView) dialog.findViewById(R.id.iv_oc_nine),
                        (ImageView) dialog.findViewById(R.id.iv_oc_ten),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eleven),

                };

        iv_array_gg = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_gg_one),
                        (ImageView) dialog.findViewById(R.id.iv_gg_two),
                        (ImageView) dialog.findViewById(R.id.iv_gg_three),
                        (ImageView) dialog.findViewById(R.id.iv_gg_four),
                        (ImageView) dialog.findViewById(R.id.iv_gg_five),
                        (ImageView) dialog.findViewById(R.id.iv_gg_six),

                        (ImageView) dialog.findViewById(R.id.iv_gg_seven),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eight),
                        (ImageView) dialog.findViewById(R.id.iv_gg_nine),
                        (ImageView) dialog.findViewById(R.id.iv_gg_ten),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eleven),

                };

        iv_array_ip = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_Ip_one),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_two),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_three),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_four),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_five),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_six),

                        (ImageView) dialog.findViewById(R.id.iv_Ip_seven),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eight),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_nine),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_ten),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eleven),

                };

        iv_array_ts = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ts_one),
                        (ImageView) dialog.findViewById(R.id.iv_ts_two),
                        (ImageView) dialog.findViewById(R.id.iv_ts_three),
                        (ImageView) dialog.findViewById(R.id.iv_ts_four),
                        (ImageView) dialog.findViewById(R.id.iv_ts_five),
                        (ImageView) dialog.findViewById(R.id.iv_ts_six),

                        (ImageView) dialog.findViewById(R.id.iv_ts_seven),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eight),
                        (ImageView) dialog.findViewById(R.id.iv_ts_nine),
                        (ImageView) dialog.findViewById(R.id.iv_ts_ten),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eleven),

                };

        iv_array_ps = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ps_one),
                        (ImageView) dialog.findViewById(R.id.iv_ps_two),
                        (ImageView) dialog.findViewById(R.id.iv_ps_three),
                        (ImageView) dialog.findViewById(R.id.iv_ps_four),
                        (ImageView) dialog.findViewById(R.id.iv_ps_five),
                        (ImageView) dialog.findViewById(R.id.iv_ps_six),

                        (ImageView) dialog.findViewById(R.id.iv_ps_seven),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eight),
                        (ImageView) dialog.findViewById(R.id.iv_ps_nine),
                        (ImageView) dialog.findViewById(R.id.iv_ps_ten),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eleven),

                };

        tv_nameArray = new TextView[]{
                (TextView) dialog.findViewById(R.id.tv_player_one),
                (TextView) dialog.findViewById(R.id.tv_player_two),
                (TextView) dialog.findViewById(R.id.tv_player_three),
                (TextView) dialog.findViewById(R.id.tv_player_four),
                (TextView) dialog.findViewById(R.id.tv_player_five),
                (TextView) dialog.findViewById(R.id.tv_player_six),
                (TextView) dialog.findViewById(R.id.tv_player_seven),
                (TextView) dialog.findViewById(R.id.tv_player_eight),
                (TextView) dialog.findViewById(R.id.tv_player_nine),
                (TextView) dialog.findViewById(R.id.tv_player_ten),
                (TextView) dialog.findViewById(R.id.tv_player_eleven)
        };

        tv_priceArray = new TextView[]{
                (TextView) dialog.findViewById(R.id.tv_player_points_one),
                (TextView) dialog.findViewById(R.id.tv_player_points_two),
                (TextView) dialog.findViewById(R.id.tv_player_points_three),
                (TextView) dialog.findViewById(R.id.tv_player_points_four),
                (TextView) dialog.findViewById(R.id.tv_player_points_five),
                (TextView) dialog.findViewById(R.id.tv_player_points_six),
                (TextView) dialog.findViewById(R.id.tv_player_points_seven),
                (TextView) dialog.findViewById(R.id.tv_player_points_eight),
                (TextView) dialog.findViewById(R.id.tv_player_points_nine),
                (TextView) dialog.findViewById(R.id.tv_player_points_ten),
                (TextView) dialog.findViewById(R.id.tv_player_points_eleven)
        };

        layout_array = new LinearLayout[]{
                (LinearLayout) dialog.findViewById(R.id.player_layout_one),
                (LinearLayout) dialog.findViewById(R.id.player_layout_two),
                (LinearLayout) dialog.findViewById(R.id.player_layout_three),
                (LinearLayout) dialog.findViewById(R.id.player_layout_four),
                (LinearLayout) dialog.findViewById(R.id.player_layout_five),
                (LinearLayout) dialog.findViewById(R.id.player_layout_six),
                (LinearLayout) dialog.findViewById(R.id.player_layout_seven),
                (LinearLayout) dialog.findViewById(R.id.player_layout_eight),
                (LinearLayout) dialog.findViewById(R.id.player_layout_nine),
                (LinearLayout) dialog.findViewById(R.id.player_layout_ten),
                (LinearLayout) dialog.findViewById(R.id.player_layout_eleven)
        };

        for (int i = 0; i < layout_array.length; i++)
        {
            layout_array[i].setBackground(context.getResources().getDrawable(R.drawable.player_field_team_));
            iv_array_pp[i].setVisibility(View.GONE);
            iv_array_oc[i].setVisibility(View.GONE);
            iv_array_gg[i].setVisibility(View.GONE);
            iv_array_ip[i].setVisibility(View.GONE);
            iv_array_ps[i].setVisibility(View.GONE);
            iv_array_ts[i].setVisibility(View.GONE);
            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.green_dot, 0, 0, 0);
            tv_nameArray[i].setText("");
            tv_priceArray[i].setText("");
            tv_total_points.setText("Total Points: ");
        }

        try {
            new GetMatchWiseHistory().execute(sharedPreference.getString(Config.TEAM_ID, ""), match_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class ViewDialog extends AlertDialog {
        Context context;
        String fixturesTeam;
        ImageView iv_Teamone;
        ImageView iv_teamtwo;

        public ViewDialog(Context context) {
            super(context);
            this.context = context;
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(String match, String date_venue, String match_id) {
            dialog = new Dialog(context);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.history_lineup);
            try {
                /*TextView tv_date_venue = (TextView) dialog.findViewById(R.id.tv_date);
                tv_total_points = (TextView) dialog.findViewById(R.id.tv_points);
                pBar_history = (ProgressBar) dialog.findViewById(R.id.pBar_history);
                TextView team_name = (TextView) dialog.findViewById(R.id.tv_team_name_history);
                tv_date_venue.setText(date_venue);
                team_name.setText(sharedPreference.getString(Config.TEAM_NAME, "No Team"));

                iv_Teamone = (ImageView) dialog.findViewById(R.id.img_one);
                iv_teamtwo = (ImageView) dialog.findViewById(R.id.img_two);
                setTeamsFlag(match, iv_Teamone, iv_teamtwo, context);*/
                setMatchLayoutContents(date_venue, match);

                ImageView myFab = (ImageView) dialog.findViewById(R.id.fab_history);
                myFab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            checkWritePermission(context);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                ImageView nextArrow = (ImageView) dialog.findViewById(R.id.iv_right);
                ImageView backArrow = (ImageView) dialog.findViewById(R.id.iv_left);

                if(IS_METHOD_RUNNING) {
                    backArrow.setEnabled(false);
                    backArrow.setClickable(false);
                    nextArrow.setClickable(false);
                    nextArrow.setEnabled(false);

                    backArrow.setFocusable(false);
                    nextArrow.setFocusable(false);
                }
                else {
                    backArrow.setClickable(true);
                    nextArrow.setClickable(true);
                    nextArrow.setEnabled(true);
                    backArrow.setEnabled(true);

                    backArrow.setFocusable(true);
                    nextArrow.setFocusable(true);

                }

                nextArrow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        /*try {

                            row_id = row_id + 1;
                            if (datesDifference(Config.FIXTURES_LIST.get(row_id).getDate())) {
                                row_id = row_id - 1;
                                Config.getAlert(context, "No Record Found");
                            } else {
                                String match = Config.FIXTURES_LIST.get(row_id).getMatch();
                                String date_venue = Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[0] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[1] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[2] + " " + Config.FIXTURES_LIST.get(row_id).getVenue_name().replace("Cricket Stadium", "").replace("International", "");
                                String match_id = Config.FIXTURES_LIST.get(row_id).getId();
                                setMatchLayoutContents(date_venue, match);
                                getAllImagesArray(dialog, match_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/

                        try {
                            row_id = row_id - 1;

                            if (row_id >= 0) {
                                if (datesDifference(Config.FIXTURES_LIST.get(row_id).getDate())) {
                                    row_id = row_id + 1;
                                    Config.getAlert(context, "No Record Found");
                                } else {
                                    String match = Config.FIXTURES_LIST.get(row_id).getMatch();
                                    String date_venue = Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[0] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[1] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[2] + " " + Config.FIXTURES_LIST.get(row_id).getVenue_name().replace("Cricket Stadium", "").replace("International", "");
                                    String match_id = Config.FIXTURES_LIST.get(row_id).getId();
                                    setMatchLayoutContents(date_venue, match);
                                    getAllImagesArray(dialog, match_id);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(row_id < 0)
                            row_id =0;


                    }
                });

                backArrow.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        /*try {
                            row_id = row_id - 1;

                            if (row_id >= 0) {
                                if (datesDifference(Config.FIXTURES_LIST.get(row_id).getDate())) {
                                    row_id = row_id + 1;
                                    Config.getAlert(context, "No Record Found");
                                } else {
                                    String match = Config.FIXTURES_LIST.get(row_id).getMatch();
                                    String date_venue = Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[0] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[1] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[2] + " " + Config.FIXTURES_LIST.get(row_id).getVenue_name().replace("Cricket Stadium", "").replace("International", "");
                                    String match_id = Config.FIXTURES_LIST.get(row_id).getId();
                                    setMatchLayoutContents(date_venue, match);
                                    getAllImagesArray(dialog, match_id);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(row_id < 0)
                            row_id =0;*/

                        try {

                            row_id = row_id + 1;
                            if (datesDifference(Config.FIXTURES_LIST.get(row_id).getDate())) {
                                row_id = row_id - 1;
                                Config.getAlert(context, "No Record Found");
                            } else {
                                String match = Config.FIXTURES_LIST.get(row_id).getMatch();
                                String date_venue = Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[0] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[1] + " " + Config.FIXTURES_LIST.get(row_id).getDate().split(" ")[2] + " " + Config.FIXTURES_LIST.get(row_id).getVenue_name().replace("Cricket Stadium", "").replace("International", "");
                                String match_id = Config.FIXTURES_LIST.get(row_id).getId();
                                setMatchLayoutContents(date_venue, match);
                                getAllImagesArray(dialog, match_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                getAllImagesArray(dialog, match_id);

            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();

        }
    }

    public static void setTeamsFlag(String match, ImageView iv_Teamone, ImageView iv_teamtwo, Context context) {
        try {
            String team1 = match.split("VS")[0].trim();
            String team2 = match.split("VS")[1].trim();
            for (int k = 0; k < 6; k++) {
                if (team1.startsWith("Islamabad"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_islamabad_small));
                if (team1.startsWith("Karachi"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_karachi_small));
                if (team1.startsWith("Lahore"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_lahore_small));
                if (team1.startsWith("Peshawar"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_peshawar_small));
                if (team1.startsWith("Multan"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_multan_small));
                if (team1.startsWith("Quetta"))
                    iv_Teamone.setBackground(context.getResources().getDrawable(R.drawable.flag_quetta_small));

            }

            for (int k = 0; k < 6; k++) {
                if (team2.startsWith("Islamabad"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_islamabad_small));
                if (team2.startsWith("Karachi"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_karachi_small));
                if (team2.startsWith("Lahore"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_lahore_small));
                if (team2.startsWith("Peshawar"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_peshawar_small));
                if (team2.startsWith("Multan"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_multan_small));
                if (team2.startsWith("Quetta"))
                    iv_teamtwo.setBackground(context.getResources().getDrawable(R.drawable.flag_quetta_small));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    String getDate(String date) {

        String format = "";
        try {
            if (date.contains("/")) {
                int selectedYear = Integer.parseInt(date.split("/")[2]);
                int selectedDay = Integer.parseInt(date.split("/")[1]);
                int selectedMonth = (Integer.parseInt(date.split("/")[0]) - 1);

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, selectedYear);
                cal.set(Calendar.DAY_OF_MONTH, selectedDay);
                cal.set(Calendar.MONTH, selectedMonth);
                //format = new SimpleDateFormat("EEEE, dd MMM, yyyy").format(cal.getTime());
                format = new SimpleDateFormat("dd MMM, yyyy").format(cal.getTime());
            } else {
                format = date.replace(" ", ", ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return format;
    }


    public static boolean datesDifference(String date) {
        boolean var = false;
        try {
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");

            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            // for (int i = 0; i < Config.FIXTURES_LIST.size(); i++) {
            match_date = date;
            dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
            //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
            date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
            date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

            Date date11 = new SimpleDateFormat("dd-MM-yyyy").parse(currentDateTime);
            Date date22 = new SimpleDateFormat("dd-MM-yyyy").parse(dateAfterConvertMonth);// Date without time check


            if (date11.compareTo(date22) == 0) {
                IS_CURRENT_DAY = true;
                var = false;
            } else if (date2.after(date1)) {
                IS_CURRENT_DAY = false;
                var = true;
            } else {
                IS_CURRENT_DAY = false;
                var = false;
            }
            //}
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return var;
    }

    public static String replaceWithNumber(String string) {
        String result = "";
        try {

            result = string.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04").replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08").replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");

        } catch (Exception e) {
        }
        return result;
    }

    private void captureScreen() {
        try {

            View v = getActivity().getWindow().getDecorView().getRootView();
            v.setDrawingCacheEnabled(true);
            Bitmap bmp = Bitmap.createBitmap(v.getDrawingCache());
            v.setDrawingCacheEnabled(false);
            try {
                File file = new File(Environment
                        .getExternalStorageDirectory().toString(), "acl.png");

                if (file.exists()) {
                    file.delete();
                }

                FileOutputStream fos = new FileOutputStream(new File(Environment
                        .getExternalStorageDirectory().toString(), "acl.png"));
                bmp.compress(Bitmap.CompressFormat.JPEG.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, new File(Environment.getExternalStorageDirectory().toString() + "/acl.png"));
        shareIntent.setType("image/png");
        startActivity(Intent.createChooser(shareIntent, "share image"));
    }

    public static void initShareIntent(String type, String _text, Context context) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, _text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/acl.png")));  //optional//use this when you want to send an image
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "send"));
    }

    private static final int REQUEST_WRITE_STORAGE = 990;

    public static boolean checkWritePermission(Context activity) {
        boolean bool = false;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {

                try {
                    takeScreenshot(dialog);
                    initShareIntent(share_type, share_text, activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                //captureScreen();
                takeScreenshot(dialog);
                initShareIntent(share_type, share_text, getActivity());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void takeScreenshot(Dialog dialog) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/acl.png"; // use your desired path

            // create bitmap screen capture
            View v1 = dialog.getWindow().getDecorView().getRootView();

            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }


    public static void setMatchLayoutContents(String date_venue, String match) {
        try {
            TextView tv_date_venue = (TextView) dialog.findViewById(R.id.tv_date);
            tv_total_points = (TextView) dialog.findViewById(R.id.tv_points);
            pBar_history = (ProgressBar) dialog.findViewById(R.id.pBar_history);
            TextView team_name = (TextView) dialog.findViewById(R.id.tv_team_name_history);
            tv_date_venue.setText(date_venue);
            team_name.setText(sharedPreference.getString(Config.TEAM_NAME, "No Team"));

            ImageView iv_Teamone = (ImageView) dialog.findViewById(R.id.img_one);
            ImageView iv_teamtwo = (ImageView) dialog.findViewById(R.id.img_two);
            setTeamsFlag(match, iv_Teamone, iv_teamtwo, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}