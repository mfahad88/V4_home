package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesAdapter;
import com.psl.classes.FixturesVO;
import com.psl.classes.LeaderboarPositionVO;
import com.psl.classes.PlayerProfileVO;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob
 */
public class Leaderboard extends Fragment {
    List<PlayerProfileVO> playersList = new ArrayList<PlayerProfileVO>();
    LayoutInflater inflater1;
    LinearLayout parent_laout;
    ProgressBar progressBar;
    RadioButton cb_today;
    RadioButton cb_overall;
    String user_id = "";
    boolean IS_INTERNAL_CALL = false;
    TextView tv_noRecord;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    String team_id = "";
    ScrollView mainscScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        parent_laout = (LinearLayout) view.findViewById(R.id.casts_container);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        cb_today = (RadioButton) view.findViewById(R.id.rb_today);
        cb_overall = (RadioButton) view.findViewById(R.id.rb_overall);
        tv_noRecord = (TextView)view.findViewById(R.id.tv_norecord);
        progressBar.bringToFront();
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);

        mainscScrollView = (ScrollView) view.findViewById(R.id.scroller);

        user_id = sharedPreferences.getString(Config.USER_ID, "");
        team_id = sharedPreferences.getString(Config.TEAM_ID, "");
        try {
            new getLeaderboardPosistion().execute("today", user_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.rg_leaderboard);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_today) {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        IS_INTERNAL_CALL = true;
                        cb_overall.setChecked(false);
                        new getLeaderboardPosistion().execute("today", user_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (checkedId == R.id.rb_overall) {
                    try {
                        progressBar.setVisibility(View.VISIBLE);
                        cb_today.setChecked(false);
                        IS_INTERNAL_CALL = true;
                        new getLeaderboardPosistion().execute("", user_id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        return view;
    }

    private class GetFixturesAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<FixturesVO> fixruresList;
        DatabaseHandler dbhanHandler = new DatabaseHandler(getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                /*Connections connection = new Connections(getActivity());
                mResult = connection.getFixturesData("fixtures");
                fixruresList = new ArrayList<FixturesVO>();
                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);*/
                fixruresList = new ArrayList<FixturesVO>();
                fixruresList = dbhanHandler.getFixtures();
                //fixruresList = xmp.getFixturesData();
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            try {
                FixturesAdapter adapter = new FixturesAdapter(getActivity(), fixruresList,"");
                ((ListView) getActivity().findViewById(R.id.lv_fixtures)).setAdapter(adapter);


                mainscScrollView.fullScroll(ScrollView.FOCUS_UP);
                //new GetPlayersAsync().execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class getLeaderboardPosistion extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<LeaderboarPositionVO> leaderData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(!IS_INTERNAL_CALL)
                pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
            else
                progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getLeaderboardPositions(params[0], params[1]);
                leaderData = new ArrayList<LeaderboarPositionVO>();
                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    leaderData = xmp.getLeaderboardPoistionData();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if(pDialog !=null)
                pDialog.dismiss();

            if(progressBar !=null)
                progressBar.setVisibility(View.GONE);
            try {


                LinearLayout layout = getActivity().findViewById(R.id.leader_par_layout);
                inflater1 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                layout.removeAllViews();

                if(leaderData.size() > 0) {
                    tv_noRecord.setVisibility(View.GONE);
                    for (int i = 0; i < leaderData.size(); i++) {
                        View rowView = inflater1.inflate(R.layout.custom_row_leaderboard, null);
                        TextView tvname = (TextView) rowView.findViewById(R.id.tv_user_name);
                        TextView tvPosition = (TextView) rowView.findViewById(R.id.tv_position);
                        RelativeLayout realtivebar = (RelativeLayout) rowView.findViewById(R.id.leaderBoard_bar);

                        if(leaderData.get(i).getUsername() ==null )
                            tvname.setText(leaderData.get(i).getId() + " (" + leaderData.get(i).getPoints() + ")");
                        else
                            tvname.setText(leaderData.get(i).getUsername() + " (" + leaderData.get(i).getPoints() + ")");


                        if(leaderData.get(i).getId().equals(team_id))
                        {
                            editor = sharedPreferences.edit();
                            editor.putString(Config.USER_RANK, leaderData.get(i).getPosition());
                            editor.commit();
                            realtivebar.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_poistion_bar));
                        }

                        if (i == 0)
                            tvPosition.setText(String.valueOf(i + 1) + "st");
                        if (i == 1)
                            tvPosition.setText(String.valueOf(i + 1) + "nd");
                        if (i == 2)
                            tvPosition.setText(String.valueOf(i + 1) + "rd");
                        if (i > 2)
                            tvPosition.setText(String.valueOf(i + 1) + "th");
                        layout.addView(rowView);
                    }
                }else
                {
                    tv_noRecord.setVisibility(View.VISIBLE);
                }
                if(!IS_INTERNAL_CALL)
                    new GetPlayersAsync().execute();

                    IS_INTERNAL_CALL = false;
                mainscScrollView.fullScroll(ScrollView.FOCUS_UP);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class GetPlayersAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inflater1 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getTopPlayers();

                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    playersList = xmp.getPlayersData("top_ten");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            try {
                displayList();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                new GetFixturesAsync().execute();
                mainscScrollView.fullScroll(ScrollView.FOCUS_UP);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        void displayList() {
            try {
                for (int i = 0; i < playersList.size(); i++) {
                    //if (playersList.get(i).getTeam_id().equals(rowNumber)) {
                    final PlayerProfileVO player = playersList.get(i);
                    View myView = inflater1.inflate(R.layout.player_view_small, null);
                    TextView tv_name = (TextView) myView.findViewById(R.id.tv_playername);
                    TextView tv_average = (TextView) myView.findViewById(R.id.tv_average);
                    TextView tv_strike_rate = (TextView) myView.findViewById(R.id.tv_strikerate);
                    final ImageView iv_tick = (ImageView) myView.findViewById(R.id.iv_tick);
                    final TextView tv_price = (TextView) myView.findViewById(R.id.tv_price);
                    final ImageView iv_player = (ImageView) myView.findViewById(R.id.iv_player);

                    if (playersList.get(i).getRole().equalsIgnoreCase("Batsman")) {
                        iv_player.setBackground(getResources().getDrawable(R.drawable.bat_icon));

                    } else if (playersList.get(i).getRole().equalsIgnoreCase("All Rounder")) {
                        iv_player.setBackground(getResources().getDrawable(R.drawable.allrounder_icon));

                    } else if (playersList.get(i).getRole().equalsIgnoreCase("Bowler")) {
                        iv_player.setBackground(getResources().getDrawable(R.drawable.ball_icon));
                    } else if (playersList.get(i).getRole().equalsIgnoreCase("Wicket Keeper")) {
                        iv_player.setBackground(getResources().getDrawable(R.drawable.wicket_icon));
                    }


                    tv_name.setText(player.getPlayer_name());
                    tv_average.setText("Avg: " + player.getAverage());
                    tv_strike_rate.setText("SR:" + player.getBatting_strike());
                    tv_price.setText(player.getPrice());
                    parent_laout.addView(myView);
                    myView.setTag(i);
                    //}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Menu 1");
    }
}