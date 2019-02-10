package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesAdapter;
import com.psl.classes.FixturesVO;
import com.psl.classes.LeaderboarPositionVO;
import com.psl.classes.PlayerProfileVO;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob .
 */

public class Dashboard extends Fragment {
    SharedPreferences sharedPreference;
    TextView[] tv_nameArray, tv_priceArray;
    DatabaseHandler dbHandler;
    List<FixturesVO> fixturesList;
    long seconds_countdown = 0;
    TextView tv_countdownTimer;
    LinearLayout[] layout_array;
    CountDownTimer timer;
    String fixturesTeam;
    ImageView iv_profile;
    ImageView iv_Teamone;
    ImageView iv_teamtwo;
    Handler handler = null;
    // Leader board data ///
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
    String team_id = "";
    ScrollView players_scrollview;
    TextView tv_curlineup_tile;
    TextView tv_team_name;
    LinearLayout build_team_layout;
    TextView tv_team_name_in_slice;
    ProgressBar pb_topPlayers, pb_fixture;
    View mView;
    ImageView btnCreate;
    ImageView[] iv_array_pp, iv_array_oc, iv_array_gg, iv_array_ip, iv_array_ts, iv_array_ps;
    TextView rank_field;
    int match_time = 29;
    int post_match_time = 29;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mView = view;
        try {
            dbHandler = new DatabaseHandler(getActivity());
            sharedPreference = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            tv_countdownTimer = (TextView) view.findViewById(R.id.tv_countdownTimer);
            TextView user_name = (TextView) view.findViewById(R.id.user_field);
            rank_field = (TextView) view.findViewById(R.id.star_field);
            TextView coins_field = (TextView) view.findViewById(R.id.coin_field);
            build_team_layout = (LinearLayout) view.findViewById(R.id.build_team_slice);
            players_scrollview = (ScrollView) view.findViewById(R.id.scrollview_players);
            tv_curlineup_tile = (TextView) view.findViewById(R.id.textView22);
            tv_team_name_in_slice = (TextView) view.findViewById(R.id.tv_name);
            btnCreate = (ImageView) view.findViewById(R.id.btn_create);
            tv_team_name = (TextView) view.findViewById(R.id.tv_cur_lineup_username);
            getAllImagesArray(view);

            pb_fixture = (ProgressBar) view.findViewById(R.id.progressBar_fixtures);
            pb_topPlayers = (ProgressBar) view.findViewById(R.id.progressBar_topPlayers);


            List<PlayerProfileVO> p_list = dbHandler.getSelectedPlayers();
            int player_list_size = p_list.size();

            String team_name = sharedPreference.getString(Config.TEAM_NAME, "No Team");

            iv_Teamone = (ImageView) view.findViewById(R.id.team_one);
            iv_teamtwo = (ImageView) view.findViewById(R.id.team_two);

            LinearLayout iv_right = (LinearLayout) view.findViewById(R.id.his);
            iv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        RadioButton rb = (RadioButton) getActivity().findViewById(R.id.iv_fake);
                        rb.setChecked(true);
                        Fragment fragment = new History();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            if (!team_name.equals("")) {
                tv_team_name.setText(team_name);
                tv_team_name_in_slice.setText(team_name);
            } else {
                tv_team_name_in_slice.setText("No Team");
            }

            tv_team_name_in_slice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getAlert("inner");
                }
            });

            if (player_list_size == 0) {
                btnCreate.setBackground(getResources().getDrawable(R.drawable.btn_build_team));
            } else {
                tv_curlineup_tile.setVisibility(View.VISIBLE);
                //tv_team_name.setVisibility(View.VISIBLE);
                players_scrollview.setVisibility(View.VISIBLE);
                btnCreate.setBackground(getResources().getDrawable(R.drawable.circle_my_team));
            }

            String name = sharedPreference.getString(Config.NAME, "");
            if (name.equals(""))
                name = sharedPreference.getString(Config.FIRST_NAME, "") + " " + sharedPreference.getString(Config.LAST_NAME, "");
            user_name.setText(name);
            rank_field.setText(sharedPreference.getString(Config.USER_RANK, ""));
            coins_field.setText(Config.format(sharedPreference.getString(Config.USER_BUDGET, "100000")));

            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Black.ttf");
            user_name.setTypeface(custom_font);
            rank_field.setTypeface(custom_font);
            coins_field.setTypeface(custom_font);
            tv_team_name.setTypeface(custom_font);

          /*  ((ImageView) view.findViewById(R.id.btn_share)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                         //checkWritePermission();
                         *//*TakeScreenshot(mView);
                         captureScreen();*//*
                         //initShareIntent("text", "acl app");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });*/


            iv_profile = (RoundedImageView) view.findViewById(R.id.ivUserImage);
            iv_profile.bringToFront();
            user_name.bringToFront();
            try {
                //String fb_image = sharedPreferences.getString(Config.PICTURE, "");
                String base64 = sharedPreference.getString(Config.IMAGE_DATA, "");
                byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                if (decodedByte != null) {
                    iv_profile.setBackground(null);
                    iv_profile.setImageBitmap(decodedByte);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            iv_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     final RadioButton rb = (RadioButton) getActivity().findViewById(R.id.iv_fake);
                     rb.setChecked(true);
                    Fragment fragment = new Profile();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            tv_nameArray = new TextView[]{
                    (TextView) view.findViewById(R.id.tv_player_one),
                    (TextView) view.findViewById(R.id.tv_player_two),
                    (TextView) view.findViewById(R.id.tv_player_three),
                    (TextView) view.findViewById(R.id.tv_player_four),
                    (TextView) view.findViewById(R.id.tv_player_five),
                    (TextView) view.findViewById(R.id.tv_player_six),
                    (TextView) view.findViewById(R.id.tv_player_seven),
                    (TextView) view.findViewById(R.id.tv_player_eight),
                    (TextView) view.findViewById(R.id.tv_player_nine),
                    (TextView) view.findViewById(R.id.tv_player_ten),
                    (TextView) view.findViewById(R.id.tv_player_eleven)
            };

            tv_priceArray = new TextView[]{
                    (TextView) view.findViewById(R.id.tv_player_points_one),
                    (TextView) view.findViewById(R.id.tv_player_points_two),
                    (TextView) view.findViewById(R.id.tv_player_points_three),
                    (TextView) view.findViewById(R.id.tv_player_points_four),
                    (TextView) view.findViewById(R.id.tv_player_points_five),
                    (TextView) view.findViewById(R.id.tv_player_points_six),
                    (TextView) view.findViewById(R.id.tv_player_points_seven),
                    (TextView) view.findViewById(R.id.tv_player_points_eight),
                    (TextView) view.findViewById(R.id.tv_player_points_nine),
                    (TextView) view.findViewById(R.id.tv_player_points_ten),
                    (TextView) view.findViewById(R.id.tv_player_points_eleven)
            };

            layout_array = new LinearLayout[]{
                    (LinearLayout) view.findViewById(R.id.player_layout_one),
                    (LinearLayout) view.findViewById(R.id.player_layout_two),
                    (LinearLayout) view.findViewById(R.id.player_layout_three),
                    (LinearLayout) view.findViewById(R.id.player_layout_four),
                    (LinearLayout) view.findViewById(R.id.player_layout_five),
                    (LinearLayout) view.findViewById(R.id.player_layout_six),
                    (LinearLayout) view.findViewById(R.id.player_layout_seven),
                    (LinearLayout) view.findViewById(R.id.player_layout_eight),
                    (LinearLayout) view.findViewById(R.id.player_layout_nine),
                    (LinearLayout) view.findViewById(R.id.player_layout_ten),
                    (LinearLayout) view.findViewById(R.id.player_layout_eleven)
            };


            // Leader Board Data ///


            parent_laout = (LinearLayout) view.findViewById(R.id.casts_container);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            cb_today = (RadioButton) view.findViewById(R.id.rb_today);
            cb_overall = (RadioButton) view.findViewById(R.id.rb_overall);
            tv_noRecord = (TextView) view.findViewById(R.id.tv_norecord);
            progressBar.bringToFront();
            //sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);

            user_id = sharedPreference.getString(Config.USER_ID, "");
            team_id = sharedPreference.getString(Config.TEAM_ID, "");
            try {
                if (player_list_size == 0) {
                    new GetUserFantasyTeam().execute();
                }
                 else {
                    List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            tv_nameArray[i].setText(list.get(i).getPlayer_name());
                            tv_priceArray[i].setText(list.get(i).getPrice() + Config.getTeamName(list.get(i).getTeam_id()));

                            if (list.get(i).getIsCaptain().equals("1") && list.get(i).getIsMom().equals("1"))
                                layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                            else if (list.get(i).getIsCaptain().equals("1"))
                                layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                            else if (list.get(i).getIsMom().equals("1"))
                                layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));


                            if (list.get(i).getRole().equalsIgnoreCase("All Rounder"))
                                tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.allrounder_field_icon, 0, 0, 0);

                            if (list.get(i).getRole().equalsIgnoreCase("Bowler"))
                                tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.bowler_field_icon, 0, 0, 0);

                            if (list.get(i).getRole().equalsIgnoreCase("Batsman"))
                                tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.batsman_field_icon, 0, 0, 0);

                            if (list.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                                tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                            if (list.get(i).getIsPrurplecap().equals("1"))
                                iv_array_pp[i].setVisibility(View.VISIBLE);
                            if (list.get(i).getIsOrangecap().equals("1"))
                                iv_array_oc[i].setVisibility(View.VISIBLE);
                            if (list.get(i).getIsGoldengloves().equals("1"))
                                iv_array_gg[i].setVisibility(View.VISIBLE);
                            if (list.get(i).getIsIconic().equals("1"))
                                iv_array_ip[i].setVisibility(View.VISIBLE);
                            //if (list.get(i).getIsTeamSafety().equals("1"))
                            // iv_array_ts[i].setVisibility(View.VISIBLE);
                            if (list.get(i).getIsSafety().equals("1"))
                                iv_array_ps[i].setVisibility(View.VISIBLE);


                        }
                    }

                    new getLeaderboardPosistion().execute("today", sharedPreference.getString(Config.TEAM_ID, "0"));
                }


                //new getLeaderboardPosistion().execute("today", user_id);
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
                            new getLeaderboardPosistion().execute("today", sharedPreference.getString(Config.TEAM_ID, "0"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (checkedId == R.id.rb_overall) {
                        try {
                            progressBar.setVisibility(View.VISIBLE);
                            cb_today.setChecked(false);
                            IS_INTERNAL_CALL = true;
                            new getLeaderboardPosistion().execute("", sharedPreference.getString(Config.TEAM_ID, "0"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            ListView lv = (ListView) view.findViewById(R.id.lv_fixtures);
            /*lv.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });*/

            /// Leader board Data ///
            Config.homeFragment = this;
            btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*long diff = datesDifference("");
                    long post_match_diff = getPostDifference();

                    if (post_match_diff == 0)
                        post_match_diff = 31;

                    if (diff > match_time && post_match_diff > post_match_time) {*/
                    if (sharedPreference.getString(Config.TEAM_NAME, "").equalsIgnoreCase("")) {
                        getAlert("");
                    } else {
                        Fragment fragment = new FormationTeam();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                    /*}else
                    {
                        Config.getAlert(getActivity(), "You cannot edit the team 30 minutes before and after match start time.");
                    }*/

                    /*if (sharedPreference.getString(Config.TEAM_NAME, "").equalsIgnoreCase("")) {
                        getAlert("");
                    } else {
                        Fragment fragment = new FormationTeam();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }*/
                }
            });

            try {
                datesDifference();
                startCountDown(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private String blockCharacterSet = "~#^|$%&*!',";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    void getAlert(final String id) {
        final EditText edittext = new EditText(getActivity());
        edittext.setHint("Enter your team name here");
        edittext.setMaxLines(1);
        edittext.setMaxEms(15);
        // edittext.setRawInputType(InputType.TYPE_CLASS_TEXT);
        //edittext.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.digits_to_allow_team_name)));
        //edittext.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME );
        // edittext.setFilters(new InputFilter[] { filter });
        edittext.setText(sharedPreference.getString(Config.TEAM_NAME, ""));

       /* edittext.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               *//*if(charSequence.equals(""))
                   return;*//*
                //if(blockCharacterSet.contains("!"))
                //{
                   //edittext.setText(edittext.getText().toString().replace("!","").replace("'", "").replace("`","").replace("%","").replace("$","").replace(";","").replace(",",""));
              //  }
            }

            @Override
            public void afterTextChanged(Editable s) {
                edittext.setText(edittext.getText().toString().replace("!","").replace("'", "").replace("`","").replace("%","").replace("$","").replace(";","").replace(",",""));
                return;
                //Here you can check amountOfReturns > MAX_RETURN_COUNT
                //and do what you need, for example, prevent to change the text
            }
        });*/

       /* edittext.setFilters(new InputFilter[] {
                new InputFilter() {
                    public CharSequence filter(CharSequence src, int start,
                                               int end, Spanned dst, int dstart, int dend) {
                        if(src.equals("")){ // for backspace
                            return src;
                        }
                        if(src.toString().matches("[a-zA-Z0-9@_ ]+")){
                            return src;
                        }
                        return "";
                    }
                }
        });*/


        int maxLength = 15;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edittext.setFilters(fArray);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(edittext)
                .setTitle("Team Name")
                .setPositiveButton("Done", null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        // grand Button bb = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String value = edittext.getText().toString();
                        value = value.replace("'", "").replace(",", "").replace("`", "").replace("!", "").replace(";", "").replace("^", "").replace("\"", "").replace("?", "");

                        if (!value.trim().equals("")) {

                            SharedPreferences.Editor editor = sharedPreference.edit();
                            editor.putString(Config.TEAM_NAME, value);
                            editor.commit();


                            if (id.equals("inner")) {
                                tv_team_name_in_slice.setText(sharedPreference.getString(Config.TEAM_NAME, ""));
                                dialog.dismiss();
                                return;
                            }

                            //grand RadioButton rb = getActivity().findViewById(R.id.iv_fake);
                            //rb.setChecked(true);

                            Fragment fragment = new FormationTeam();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            /*grand RadioButton rb = getActivity().findViewById(R.id.iv_fake);
                            rb.setChecked(true);*/
                            dialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "Enter your team name", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                });
            }
        });

        dialog.show();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Menu 1");
    }

    void datesDifference() {
        try {
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");

            fixturesList = dbHandler.getFixtures();
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixturesList.size(); i++) {
                match_date = fixturesList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                seconds_countdown = (date2.getTime() - date1.getTime());
                if (date2.after(date1)) {
                    fixturesTeam = fixturesList.get(i).getMatch();
                    String team1 = fixturesTeam.split("VS")[0].trim();
                    String team2 = fixturesTeam.split("VS")[1].trim();
                    for (int k = 0; k < 6; k++) {
                        if (team1.startsWith("Islamabad"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_islamabad));
                        if (team1.startsWith("Karachi"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_karachi));
                        if (team1.startsWith("Lahore"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_lahore));
                        if (team1.startsWith("Peshawar"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_peshawar));
                        if (team1.startsWith("Multan"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_multan));
                        if (team1.startsWith("Quetta"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_quetta));

                    }

                    for (int k = 0; k < 6; k++) {
                        if (team2.startsWith("Islamabad"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_islamabad));
                        if (team2.startsWith("Karachi"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_karachi));
                        if (team2.startsWith("Lahore"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_lahore));
                        if (team2.startsWith("Peshawar"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_peshawar));
                        if (team2.startsWith("Multan"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_multan));
                        if (team2.startsWith("Quetta"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_quetta));
                    }
                    return;
                }


            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
    }


    long datesDifference(String check) {
        long minutes_difference = 0;
        try {
            String fixturesTeam = "";
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");

            List<FixturesVO> fixturesList = dbHandler.getFixtures();
            //ImageView iv_Teamone = (ImageView) myView.findViewById(R.id.team_one);
            //ImageView iv_teamtwo = (ImageView) myView.findViewById(R.id.team_two);
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixturesList.size(); i++) {
                match_date = fixturesList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                //seconds_countdown = (date2.getTime() - date1.getTime());

                if (date2.after(date1)) {
                    fixturesTeam = fixturesList.get(i).getMatch();
                    String team1 = fixturesTeam.split("VS")[0].trim();
                    String team2 = fixturesTeam.split("VS")[1].trim();

                    long diff = date2.getTime() - date1.getTime();
                    long seconds = diff / 1000;
                    minutes_difference = seconds / 60;

                    return minutes_difference;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return minutes_difference;
    }

    private class GetTimer extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                fixturesList = dbHandler.getFixtures();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                datesDifference();
                startCountDown(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    String replaceWithNumber(String string) {
        String result = "";
        try {

            result = string.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04").replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08").replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");

        } catch (Exception e) {
        }
        return result;
    }

    public void startCountDown(View view) {
    /*    creating object for all text views    */

        //grand TextView days = (TextView)view.findViewById(R.id.days);
        //grand TextView hours = (TextView)view.findViewById(R.id.hours);
        //grand TextView mins = (TextView)view.findViewById(R.id.minutes);
        //grand TextView seconds = (TextView)view.findViewById(R.id.seconds);

        timer = new CountDownTimer(seconds_countdown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                String days = TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)) + "";
                String hours = (TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished))) + "";
                String minutes = ((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))) + "");
                String secs = (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))) + "";

                String str_display = "In ";

                if (days.equals("") || days.equals("0") || days.equals("00"))
                    str_display = str_display;
                else
                    str_display += days + " days ";

                if (hours.equals("") || hours.equals("0") || hours.equals("00"))
                    str_display = str_display;
                else
                    str_display += hours + " hours ";

                if (minutes.equals("") || minutes.equals("0") || minutes.equals("00"))
                    str_display = str_display;
                else
                    str_display += minutes + " minutes ";

                str_display += secs + " Seconds";

                tv_countdownTimer.setText(str_display);

                //tv_countdownTimer.setText("In " + days + " Days " + hours + " Hours " + minutes + " Minutes " + secs + " Seconds");
            /*            converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews             */
                //days.setText(TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+"");
                //hours.setText((TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)))+"");
                //mins.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+"");
                //seconds.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+"");
            }

            @Override

            public void onFinish() {
            /*            clearing all fields and displaying countdown finished message
                     *  */
                try {
                    //Toast.makeText(getActivity(), "Match started, have an entertaining match !!!", Toast.LENGTH_LONG).show();

                    // if()

                    tv_countdownTimer.setText("Match is in progress ");
                    if(timer!=null) {
                        timer.cancel();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //days.setText("Count down completed");
                // hours.setText("");
                //mins.setText("");
                // seconds.setText("");
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
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

            //pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
            pb_fixture.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                fixruresList = dbHandler.getFixtures();
                if (fixturesList.size() == 0) {

                    Connection connection = new Connection(getActivity());
                    mResult = connection.getFixturesData("fixtures");
                    fixruresList = new ArrayList<FixturesVO>();
                    if (mResult != null && !mResult.equals("")) {
                        XMLParser xmp = new XMLParser();
                        xmp.parse(mResult);
                        fixruresList = xmp.getFixturesData();
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
                pb_fixture.setVisibility(View.GONE);
                List<FixturesVO> list = dbHandler.getFixtures();
                if (list == null || list.size() == 0) {
                    dbHandler.deleteAllFixtures();
                    dbHandler.saveFixtures(fixruresList);
                }
                FixturesAdapter adapter = new FixturesAdapter(getActivity(), fixruresList, "");
                ((ListView) getActivity().findViewById(R.id.lv_fixtures)).setAdapter(adapter);

                if (!IS_INTERNAL_CALL)
                    new GetPlayersAsync().execute();//yaqoob

                IS_INTERNAL_CALL = false;


               /* try {
                    datesDifference();
                    startCountDown(mView);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                //new GetTimer().execute();

               /* try {
                    Thread.sleep(3000);
                    datesDifference();
                    startCountDown(mView);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

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
        String set_or_not = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //if(!IS_INTERNAL_CALL)
            //  pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
            // else
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getLeaderboardPositions(params[0], params[1]);
                set_or_not = params[0];
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
            if (pDialog != null)
                pDialog.dismiss();

            if (progressBar != null)
                progressBar.setVisibility(View.GONE);

            try {

                LinearLayout layout = getActivity().findViewById(R.id.leader_par_layout);
                inflater1 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                layout.removeAllViews();

                if (leaderData.size() > 0) {
                    tv_noRecord.setVisibility(View.GONE);
                    for (int i = 0; i < leaderData.size(); i++) {
                        View rowView = inflater1.inflate(R.layout.custom_row_leaderboard, null);
                        TextView tvname = (TextView) rowView.findViewById(R.id.tv_user_name);
                        TextView tvPosition = (TextView) rowView.findViewById(R.id.tv_position);
                        RelativeLayout realtivebar = (RelativeLayout) rowView.findViewById(R.id.leaderBoard_bar);

                        if (leaderData.get(i).getUsername() == null)
                            tvname.setText(leaderData.get(i).getId() + " (" + leaderData.get(i).getPoints() + ")");
                        else
                            tvname.setText(leaderData.get(i).getUsername() + " (" + leaderData.get(i).getPoints() + ")");


                        if (set_or_not.equalsIgnoreCase("")) {
                            if (leaderData.get(i).getId().equals(team_id)) {
                                editor = sharedPreference.edit();


                                rank_field.setText(leaderData.get(i).getPosition());

                                editor.putString(Config.USER_RANK, leaderData.get(i).getPosition());
                                editor.commit();
                                realtivebar.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_poistion_bar));
                            }
                        } else {
                            if (leaderData.get(i).getId().equals(team_id))
                                realtivebar.setBackgroundDrawable(getResources().getDrawable(R.drawable.user_poistion_bar));

                        }

                        tvPosition.setText(leaderData.get(i).getPosition()); //+ "st");
                        /*if (i == 0)
                            tvPosition.setText(String.valueOf(i + 1)); //+ "st");
                        if (i == 1)
                            tvPosition.setText(String.valueOf(i + 1)); //+ "nd");
                        if (i == 2)
                            tvPosition.setText(String.valueOf(i + 1)); //+ "rd");
                        if (i > 2)
                            tvPosition.setText(String.valueOf(i + 1)); //+ "th");*/
                        layout.addView(rowView);
                    }
                } else {
                    tv_noRecord.setVisibility(View.VISIBLE);
                }

                try {
                    new GetFixturesAsync().execute();
                } catch (Exception e) {
                    e.printStackTrace();

                }


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
            pb_topPlayers.setVisibility(View.VISIBLE);
            // pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
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
            //pDialog.dismiss();
            pb_topPlayers.setVisibility(View.GONE);
            try {
                displayList();
            } catch (Exception e) {
                e.printStackTrace();
            }

          /*  try {
                new GetFixturesAsync().execute();
            } catch (Exception e) {
                e.printStackTrace();

            }*/
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
                    tv_average.setText(playersList.get(i).getPoints());
                    /*if (player.getRole().equalsIgnoreCase("Bowler")) {
                        tv_average.setText("Wkts: " + player.getWickets());
                        tv_strike_rate.setText("Econ:" + player.getEconomy());
                    } else if (player.getRole().equalsIgnoreCase("All Rounder")) {
                        tv_average.setText("Econ: " + player.getEconomy());
                        tv_strike_rate.setText("SR:" + player.getBatting_strike());
                    } else {
                        tv_average.setText("Avg: " + player.getAverage());
                        tv_strike_rate.setText("SR:" + player.getBatting_strike());
                    }*/
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

    private class GetUserFantasyTeam extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<PlayerProfileVO> selectedPlayerList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
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
                if (pDialog != null)
                    pDialog.dismiss();

                dbHandler.deleteAll();
                if (selectedPlayerList != null && selectedPlayerList.size() > 0) {
                    dbHandler.saveSelectedPlayers(selectedPlayerList);
                    players_scrollview.setVisibility(View.VISIBLE);
                }

                Config.SHOULD_REFRESH_TEAM = false;

                List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();

                if (list != null && list.size() > 0) {
                    btnCreate.setBackground(getResources().getDrawable(R.drawable.circle_my_team));
                    for (int i = 0; i < list.size(); i++) {
                        tv_nameArray[i].setText(list.get(i).getPlayer_name());
                        tv_priceArray[i].setText(list.get(i).getPrice() + Config.getTeamName(list.get(i).getTeam_id()));

                        if (list.get(i).getRole().equalsIgnoreCase("All Rounder"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.allrounder_field_icon, 0, 0, 0);

                        if (list.get(i).getRole().equalsIgnoreCase("Bowler"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.bowler_field_icon, 0, 0, 0);

                        if (list.get(i).getRole().equalsIgnoreCase("Batsman"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.batsman_field_icon, 0, 0, 0);

                        if (list.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                            tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                        if (list.get(i).getIsCaptain().equals("1") && list.get(i).getIsMom().equals("1"))
                            layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                        else if (list.get(i).getIsCaptain().equals("1"))
                            layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                        else if (list.get(i).getIsMom().equals("1"))
                            layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));

                        if (list.get(i).getIsPrurplecap().equals("1"))
                            iv_array_pp[i].setVisibility(View.VISIBLE);
                        if (list.get(i).getIsOrangecap().equals("1"))
                            iv_array_oc[i].setVisibility(View.VISIBLE);
                        if (list.get(i).getIsGoldengloves().equals("1"))
                            iv_array_gg[i].setVisibility(View.VISIBLE);
                        if (list.get(i).getIsIconic().equals("1"))
                            iv_array_ip[i].setVisibility(View.VISIBLE);
                        //if (list.get(i).getIsTeamSafety().equals("1"))
                        //iv_array_ts[i].setVisibility(View.VISIBLE);
                        if (list.get(i).getIsSafety().equals("1"))
                            iv_array_ps[i].setVisibility(View.VISIBLE);
                    }

                    tv_curlineup_tile.setVisibility(View.VISIBLE);
                    //tv_team_name.setVisibility(View.VISIBLE);
                }
                new getLeaderboardPosistion().execute("today", sharedPreference.getString(Config.TEAM_ID, "0"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getAllImagesArray(View dialog) {

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

    private void initShareIntent(String type, String _text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, _text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/acl.png")));  //optional//use this when you want to send an image
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            RadioButton rb = getActivity().findViewById(R.id.iv_team);
            rb.setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private static grand int REQUEST_WRITE_STORAGE = 990;

    public boolean checkWritePermission() {
        boolean bool = false;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {
                //TakeScreenshot(mView);

                captureScreen();
                initShareIntent("text", "https://play.google.com/store/apps/details?id=com.psl.fantasy.league #ACL");
               // openFilePicker();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bool;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try{
                //TakeScreenshot(mView);
                captureScreen();
                initShareIntent("text", "https://play.google.com/store/apps/details?id=com.psl.fantasy.league #ACL");
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }*/

    void TakeScreenshot(View view) {

        RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.helmet_layout_2);


        //ImageView snapImage = (ImageView)findViewById(R.id.imageView);


        layout.setDrawingCacheEnabled(true);


        //specifying the dimensions of view


        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));


        layout.layout(0, 0, layout.getMeasuredWidth(), layout.getMeasuredHeight());


        layout.buildDrawingCache(true);


        Bitmap b = Bitmap.createBitmap(layout.getDrawingCache());


        layout.setDrawingCacheEnabled(false); // clear drawing cache


        //snapImage.setImageBitmap(b);


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();


        b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        //create a new file in sdcard name it as "testimage.jpg"


        File fileImage = new File(Environment.getExternalStorageDirectory()


                + File.separator + "acl.png");

        //write the bytes in file


        FileOutputStream fo = null;


        try {


            fileImage.createNewFile();


            fo = new FileOutputStream(fileImage);


            fo.write(bytes.toByteArray());


        } catch (FileNotFoundException e) {


            e.printStackTrace();


        } catch (IOException e) {


            e.printStackTrace();


        }

    }

    /*public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }*/


    long getPostDifference() {
        long minutes_difference = 0;
        try {
            String fixturesTeam = "";
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");

            List<FixturesVO> fixturesList = dbHandler.getFixtures();
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixturesList.size(); i++) {
                match_date = fixturesList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                //seconds_countdown = (date2.getTime() - date1.getTime());

                if (date2.after(date1)) {
                    int k = 0;
                    k = i - 1;
                    if (k >= 0) {

                        match_date = fixturesList.get(k).getDate();
                        dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];

                        fixturesTeam = fixturesList.get(k).getMatch();
                        String team1 = fixturesTeam.split("VS")[0].trim();
                        String team2 = fixturesTeam.split("VS")[1].trim();
                        date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                        date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                        long diff = date1.getTime() - date2.getTime();
                        long seconds = diff / 1000;
                        minutes_difference = seconds / 60;

                        return Math.abs(minutes_difference);
                    }
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return Math.abs(minutes_difference);
    }


}