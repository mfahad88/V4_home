package com.psl.fantasy.league.season2;

import com.psl.fantasy.league.season2.R;;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesVO;
import com.psl.classes.PlayerAttributes;
import com.psl.classes.PlayerProfileVO;
import com.psl.classes.TeamInventory;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.psl.classes.Config.SELECTED_PLAYERS_FILE_NAME;


/**
 * Created by Yaqoob.Khan on 09/01/18.
 */

public class Team extends Fragment {

    boolean selected = false;
    RecyclerView recyclerView;
    ArrayList<String> Number;
    LinearLayoutManager HorizontalLayout;
    View ChildView;
    int RecyclerViewItemPosition;
    HorizontalScrollView scrollView;
    ScrollView scrollViewPlayers;
    LinearLayout parent_laout;
    Spinner spinner_team;
    List<PlayerProfileVO> playersList = new ArrayList<PlayerProfileVO>();
    LayoutInflater inflater1;
    View myView;
    List<PlayerProfileVO> selectedPlayerList;
    TextView[] tv_nameArray, tv_priceArray, tv_nameArray_captain, tv_priceArray_captain, tv_nameArray_inventory, tv_priceArray_inventory;
    LinearLayout[] layout_array, layout_array_captain, layout_array_inventory;
    long userPoints = 0;
    TextView budgetTextview;
    SharedPreferences sharedPreference;
    SharedPreferences.Editor editor;
    long tempPoints;
    FileOutputStream fileOutputStream;
    //ImageView iv_tick;
    String spinner_row_id = "1001";
    String team_id;
    int swap_count;
    boolean IS_EDITING = false;

    int bowler_count = 3;
    int wicket_keeper_count = 1;

    int all_rounders_count = 3;
    int batsman_count = 4;
    RelativeLayout captainView;
    boolean IS_CAPTAIN_SELECTION = false;
    String user_id = "";
    ImageView iv;
    ImageView[] iv_array_pp, iv_array_oc, iv_array_gg, iv_array_ip, iv_array_ts, iv_array_ps;
    ImageView[] iv_array_pp_team_view, iv_array_oc_team_view, iv_array_gg_team_view, iv_array_ip_team_view, iv_array_ts_team_view, iv_array_ps_team_view;
    ImageView[] iv_array_pp_captain_view, iv_array_oc_captain_view, iv_array_gg_captain_view, iv_array_ip_captain_view, iv_array_ts_captain_view, iv_array_ps_captain_view;
    TeamInventory inventoryFields;
    public Integer[] count_array = new Integer[6];
    String tellProcesstoWebmethod = "";
    DatabaseHandler dbHandler;
    TextView tv_swaps_count;
    int players_size = 0;
    //int match_time = 29;
    //int post_match_time = 29;
    TextView tv_allrounder, tv_batsman, tv_bowler, tv_wicketkeeper;
    boolean shouldApplyteamSafety = false;
    int oc_count = 0, pp_count = 0, gg_count = 0, ip_count = 0, team_safety_count = 0, ps_count = 0;
    CountDownTimer timer;
    TextView tv_countdownTimer;
    long seconds_countdown = 0;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_team, container, false);
        mView = view;

        final View main_view = getActivity().findViewById(R.id.iv_nc);
        main_view.setVisibility(View.GONE);

        tv_allrounder = (TextView) view.findViewById(R.id.tv_allrounder);
        tv_batsman = (TextView) view.findViewById(R.id.tv_batsman);
        tv_bowler = (TextView) view.findViewById(R.id.tv_bowler);
        tv_wicketkeeper = (TextView) view.findViewById(R.id.tv_wicket_keeper);

        myView = view;
        dbHandler = new DatabaseHandler(getActivity());
        getAllArrays(view);

        scrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollview);
        scrollViewPlayers = (ScrollView) view.findViewById(R.id.scrollview_players);
        parent_laout = (LinearLayout) view.findViewById(R.id.casts_container);
        budgetTextview = (TextView) view.findViewById(R.id.tv_budget);
        TextView user_rank = (TextView) view.findViewById(R.id.user_rank_team);
        tv_swaps_count = (TextView) view.findViewById(R.id.user_swaps_);
        tv_swaps_count.bringToFront();
        sharedPreference = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        editor = sharedPreference.edit();
        String budget = sharedPreference.getString(Config.USER_BUDGET, "100000");
        captainView = (RelativeLayout) view.findViewById(R.id.captain_view);
        tv_countdownTimer = (TextView) view.findViewById(R.id.tv_countdownTimer_team);

        String team_name = sharedPreference.getString(Config.TEAM_NAME, "");
        if (team_name == null || team_name.equals(""))
            getAlert();

        String rank = sharedPreference.getString(Config.USER_RANK, "");
        user_rank.setText(rank);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Black.ttf");
        budgetTextview.setTypeface(custom_font);
        user_rank.setTypeface(custom_font);
        tv_swaps_count.setTypeface(custom_font);

        final ImageView btn_mom = (ImageView) view.findViewById(R.id.btn_select_mom);
        final ImageView btn_done = (ImageView) view.findViewById(R.id.btn_Done_team);


        final LinearLayout[] layout_captain_fields = new LinearLayout[layout_array_captain.length];
        ((ImageView) view.findViewById(R.id.btn_select_captain)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //long diff = datesDifference();
                //long post_match_diff = getPostDifference();

               // if (post_match_diff == 0)
                   // post_match_diff = 31; // Just for the first match
                //if(players_size == 0 || diff > 10) {
               // if (diff > match_time && post_match_diff > post_match_time) {

                    for (int i = 0; i < tv_nameArray_captain.length; i++) {
                        tv_nameArray_captain[i].setText("");
                        tv_priceArray_captain[i].setText("");
                        tv_nameArray_captain[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.green_dot, 0, 0, 0);
                    }

                    for (int i = 0; i < selectedPlayerList.size(); i++) {
                        tv_nameArray_captain[i].setText(selectedPlayerList.get(i).getPlayer_name());
                        tv_priceArray_captain[i].setText(selectedPlayerList.get(i).getPrice() + Config.getTeamName(selectedPlayerList.get(i).getTeam_id()));

                        if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("All Rounder"))
                            tv_nameArray_captain[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Bowler"))
                            tv_nameArray_captain[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Batsman"))
                            tv_nameArray_captain[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                            tv_nameArray_captain[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                        if (selectedPlayerList.get(i).getIsCaptain().equals("1") && selectedPlayerList.get(i).getIsMom().equals("1"))
                            layout_captain_fields[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                        else if (selectedPlayerList.get(i).getIsMom().equals("1"))
                            layout_captain_fields[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                        else if (selectedPlayerList.get(i).getIsCaptain().equals("1"))
                            layout_captain_fields[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                        else
                            layout_captain_fields[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                    }
                    if(selectedPlayerList.size() < 11)
                    {
                        if(selectedPlayerList.size() == 0)
                        {
                            for (int i = selectedPlayerList.size(); i < 11; i++) {
                                layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                            }
                        }else if(selectedPlayerList.size() > 0) {
                            for (int i = selectedPlayerList.size(); i < 11; i++) {
                                layout_captain_fields[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                            }
                        }
                    }
                    //Toast.makeText(getActivity(), ""+selectedPlayerList.size(), Toast.LENGTH_LONG).show();

                    scrollViewPlayers.setVisibility(View.GONE);
                    captainView.bringToFront();
                    captainView.setVisibility(View.VISIBLE);
                    btn_mom.setVisibility(View.VISIBLE);
                    btn_done.setVisibility(View.GONE);
                    IS_CAPTAIN_SELECTION = true;

                    getAllImagesArrayCaptainViewIcons(myView);

                //} else {
                   // Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                //}

            }
        });
        btn_mom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IS_CAPTAIN_SELECTION = false;
                //captainView.setBackground(getResources().getDrawable(R.drawable.bg_select_mom));
                btn_mom.setVisibility(View.GONE);
                btn_done.setVisibility(View.VISIBLE);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollViewPlayers.setVisibility(View.VISIBLE);
                //captainView.bringToFront();
                captainView.setVisibility(View.GONE);
                IS_CAPTAIN_SELECTION = false;
                btn_done.setVisibility(View.GONE);

                for (int i = 0; i < selectedPlayerList.size(); i++) {
                    if (selectedPlayerList.get(i).getIsCaptain().equals("1") && selectedPlayerList.get(i).getIsMom().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                    else if (selectedPlayerList.get(i).getIsCaptain().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                    else if (selectedPlayerList.get(i).getIsMom().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                    else
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                }
            }
        });

        /*String formation = sharedPreference.getString(Config.TEAM_FORMATION, "3~4");
        if (!formation.equals("") && formation.toString().contains("~")) {
            all_rounders_count = Integer.parseInt(formation.split("~")[0]);
            batsman_count = Integer.parseInt(formation.split("~")[1]);
        }*/


        userPoints = Long.parseLong(budget);
        swap_count = Integer.parseInt(sharedPreference.getString(Config.SWAP_COUNT, "100"));
        budgetTextview.setText(budget);
        budgetTextview.bringToFront();
        user_rank.bringToFront();
        tv_swaps_count.setText(String.valueOf(swap_count));
        user_id = sharedPreference.getString(Config.USER_ID, "");
        try {

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.iv_isb) {
                        spinner_row_id = "1001";
                    }
                    if (checkedId == R.id.iv_karchi) {
                        spinner_row_id = "1002";
                    }
                    if (checkedId == R.id.iv_lahore) {
                        spinner_row_id = "1003";
                    }
                    if (checkedId == R.id.iv_multan) {
                        spinner_row_id = "1004";
                    }
                    if (checkedId == R.id.iv_peshawar) {
                        spinner_row_id = "1005";
                    }
                    if (checkedId == R.id.iv_quetta) {
                        spinner_row_id = "1006";
                    }
                    displayList(spinner_row_id);
                }
            });


            fileOutputStream = getActivity().openFileOutput(SELECTED_PLAYERS_FILE_NAME, getActivity().MODE_PRIVATE);
            selectedPlayerList = new ArrayList<PlayerProfileVO>();


            for (int i = 0; i < layout_array_captain.length; i++) {
                layout_captain_fields[i] = layout_array_captain[i];
                layout_captain_fields[i].setId(i);
                layout_captain_fields[i].setTag(i);
                layout_captain_fields[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int tag = (Integer) v.getTag();

                            if (IS_CAPTAIN_SELECTION) {

                                for (int ii = 0; ii < selectedPlayerList.size(); ii++) {
                                    selectedPlayerList.get(ii).setIsCaptain("0");

                                    if (selectedPlayerList.get(ii).getIsMom().equals("1"))
                                        layout_captain_fields[ii].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                    else
                                        layout_captain_fields[ii].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                                }

                                selectedPlayerList.get(tag).setIsCaptain("1");
                                layout_captain_fields[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));

                                if (selectedPlayerList.get(tag).getIsMom().equals("1"))
                                    layout_captain_fields[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));


                            } else {
                                /*if (selectedPlayerList.get(tag).getIsCaptain().equals("1")) {
                                    Config.getAlert(getActivity(), "Try another player for Man of the Match");
                                    return;
                                }*/

                                for (int ii = 0; ii < selectedPlayerList.size(); ii++) {
                                    selectedPlayerList.get(ii).setIsMom("0");

                                    if (selectedPlayerList.get(ii).getIsCaptain().equals("1"))
                                        layout_captain_fields[ii].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                    else
                                        layout_captain_fields[ii].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                }

                                selectedPlayerList.get(tag).setIsMom("1");
                                layout_captain_fields[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));

                                if (selectedPlayerList.get(tag).getIsCaptain().equals("1"))
                                    layout_captain_fields[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            LinearLayout[] layout = new LinearLayout[layout_array.length];
            for (int i = 0; i < layout_array.length; i++) {
                layout[i] = layout_array[i];
                layout[i].setId(i);
                layout[i].setTag(i);
                layout[i].setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            int tag = (Integer) v.getTag();

                            if (IS_EDITING)
                                new ViewDialog(getActivity()).showDialog(getActivity(), selectedPlayerList.get(tag).getPlayer_name(), tag, iv, "remove");
                            else
                                removeMethod(tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       /* try {
                            long diff = datesDifference();
                            long post_match_diff = getPostDifference();

                            if(post_match_diff == 0)
                                post_match_diff = 31; // Just for the first match
                            //if(players_size == 0 || diff > 10) {
                            if(diff > match_time && post_match_diff > post_match_time) {
                                int tag = (Integer) v.getTag();
                                for (int k = 0; k < selectedPlayerList.size(); k++) {
                                    //if (playersList.get(tag).getPlayer_id().equals(selectedPlayerList.get(k).getPlayer_id())) {
                                    for (Iterator<PlayerProfileVO> iter = selectedPlayerList.listIterator(); iter.hasNext(); ) {
                                        PlayerProfileVO a = iter.next();
                                        if (a.getPlayer_id().equals(selectedPlayerList.get(tag).getPlayer_id())) {

                                            userPoints = userPoints + Long.parseLong(selectedPlayerList.get(tag).getPrice());
                                            *//*editor.putString(Config.USER_BUDGET, String.valueOf(userPoints));
                                            editor.commit();*//*

                                            budgetTextview.setText(String.valueOf(userPoints));
                                            iter.remove();
                                            break;
                                        }
                                    }

                                    for (int l = 0; l < tv_nameArray.length; l++) {
                                        tv_nameArray[l].setText("");
                                        tv_priceArray[l].setText("");
                                    }
                                    for (int j = 0; j < selectedPlayerList.size(); j++) {
                                        tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                                        tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice());

                                        if (selectedPlayerList.get(j).getIsCaptain().equals("1"))
                                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                        else if (selectedPlayerList.get(j).getIsMom().equals("1"))
                                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                        else
                                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                    }
                                    displayList(spinner_row_id);
                                *//*for (int jj = 0; jj < playersList.size(); jj++)
                                {
                                    for (int pp = 0; pp < selectedPlayerList.size(); pp++) {
                                        if (playersList.get(jj).getPlayer_id().equalsIgnoreCase(selectedPlayerList.get(pp).getPlayer_id())) {
                                            iv_tick.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }*//*
                                    *//*editor.putString(Config.USER_BUDGET, String.valueOf(userPoints));
                                    editor.commit();*//*
                                    budgetTextview.setText(String.valueOf(userPoints));
                                    Toast.makeText(getActivity(), "Player removed", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                            else
                            {
                                Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    }
                });

               /* layout[i].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try {
                            int tag = (Integer) v.getTag();
                            getAlertiwthCheckboxes(selectedPlayerList.get(tag).getPlayer_name(), tag);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return false;
                    }
                });*/

            }

            try{
                startCountDown(view);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            new GetMatchTime().execute();
            /*String argument = getArguments().getString("Is_assign");
            if (argument != null && !argument.equals("")) {

                selectedPlayerList = dbHandler.getSelectedPlayers();
                displayList("1001");
                new GetInventoryCount().execute();
            } else {
                new GetPlayersAsync().execute();
            }*/
            datesDifference();

            getPostDifference();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ((ImageView) view.findViewById(R.id.btn_createTeam)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //long diff = datesDifference();
                    //long post_match_diff = getPostDifference();

                    //if (post_match_diff == 0)
                        //post_match_diff = 31; // Just for the first match
                    //if(players_size == 0 || diff > 10) {
                    //if (diff > match_time && post_match_diff > post_match_time) {

                        if (selectedPlayerList.size() < 11) {
                            //Toast.makeText(getActivity(), "Please complete 11 players list", Toast.LENGTH_LONG).show();
                            Config.getAlert(getActivity(), "Please complete 11 players list");
                            return;
                        }
                        boolean is_selected = false;

                        for (int i = 0; i < selectedPlayerList.size(); i++) {
                            if (selectedPlayerList.get(i).getIsCaptain().equals("1")) {
                                is_selected = true;
                                break;
                            }
                        }

                        boolean is__mom_selected = false;

                        for (int i = 0; i < selectedPlayerList.size(); i++) {
                            if (selectedPlayerList.get(i).getIsMom().equals("1")) {
                                is__mom_selected = true;
                                break;
                            }
                        }

                        String formation = sharedPreference.getString(Config.TEAM_FORMATION, "3~4");
                        if (!formation.equals("") && formation.toString().contains("~")) {
                            all_rounders_count = Integer.parseInt(formation.split("~")[0]);
                            batsman_count = Integer.parseInt(formation.split("~")[1]);
                        }
                        int allrounder = 0, batsman = 0;
                        for (int i = 0; i < selectedPlayerList.size(); i++) {
                            if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("All Rounder")) {
                                allrounder++;
                                if (allrounder > all_rounders_count) {
                                    Config.getAlert(getActivity(), "Your team is not according to formation");
                                    return;
                                }
                            }
                            if (selectedPlayerList.get(i).getRole().equalsIgnoreCase("Batsman")) {
                                batsman++;
                                if (batsman > batsman_count) {
                                    Config.getAlert(getActivity(), "Your team is not according to formation");
                                    return;
                                }
                            }
                        }

                        if (is_selected == false) {
                            Config.getAlert(getActivity(), "Please select your team captain");
                        } else if (is__mom_selected == false) {
                            Config.getAlert(getActivity(), "Please select your man of the match");
                        } else {
                            new SaveTeamAsync().execute("inventory");
                        }
                    /*} else {
                        Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ((ImageView) view.findViewById(R.id.iv_back_team)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new ViewDialog(getActivity()).showDialog(getActivity(), "", 0, iv, "formation");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (dbHandler.getSelectedPlayers() != null)
            players_size = dbHandler.getSelectedPlayers().size();

        //setFormation("");

        /*if(players_size > 0)
        {
            tv_allrounder.setText(String.valueOf(0));
            tv_batsman.setText(String.valueOf(0));
            tv_bowler.setText(String.valueOf(0));
            tv_wicketkeeper.setText(String.valueOf(0));

        }else
        {
            tv_allrounder.setText(String.valueOf(all_rounders_count));
            tv_batsman.setText(String.valueOf(batsman_count));
            tv_bowler.setText(String.valueOf(bowler_count));
            tv_wicketkeeper.setText(String.valueOf(wicket_keeper_count));
        }*/
        MainActivity.back_order="formation";
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN )
                {

                        Fragment fragment = new FormationTeam();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();


                    return false;
                }
                return false;
            }
        } );
        return view;
    }

    void removeMethod(int tag) {
        try {
            boolean apply_team_safety = false;
            long diff = datesDifference();
            long post_match_diff = getPostDifference();

            if (post_match_diff == 0)
                post_match_diff = 31; // Just for the first match
            //if(players_size == 0 || diff > 10) {
            //if (diff > match_time && post_match_diff > post_match_time) {
                //int tag = (Integer) v.getTag();
                for (int k = 0; k < selectedPlayerList.size(); k++) {

                    String role = "";//selectedPlayerList.get(tag).getRole();

                    for (Iterator<PlayerProfileVO> iter = selectedPlayerList.listIterator(); iter.hasNext(); ) {
                        PlayerProfileVO a = iter.next();
                        if (a.getPlayer_id().equals(selectedPlayerList.get(tag).getPlayer_id())) {

                            userPoints = userPoints + Long.parseLong(selectedPlayerList.get(tag).getPrice());
                            role = selectedPlayerList.get(tag).getRole();
                            budgetTextview.setText(String.valueOf(userPoints));
                            shouldApplyteamSafety = reformInventory(tag);
                            iter.remove();
                            setFormation("-");
                            break;
                        }
                    }

                    for (int l = 0; l < tv_nameArray.length; l++) {
                        tv_nameArray[l].setText("");
                        tv_priceArray[l].setText("");

                        tv_nameArray[l].setCompoundDrawablesWithIntrinsicBounds( R.drawable.green_dot, 0, 0, 0);
                    }
                    for (int j = 0; j < selectedPlayerList.size(); j++) {
                        tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                        tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice() + Config.getTeamName(selectedPlayerList.get(j).getPrice()));


                        if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("All Rounder"))
                            tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Bowler"))
                            tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Batsman"))
                            tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Wicket Keeper"))
                            tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                        if (selectedPlayerList.get(j).getIsCaptain().equals("1") && selectedPlayerList.get(j).getIsMom().equals("1"))
                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                        else if (selectedPlayerList.get(j).getIsCaptain().equals("1"))
                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                        else if (selectedPlayerList.get(j).getIsMom().equals("1"))
                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                        else
                            layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                    }
                    displayList(spinner_row_id);

                    budgetTextview.setText(String.valueOf(userPoints));
                    setInventoryIconsAfterRemovePlayer();




                    /*int mCounter = 0;
                    if(role.equalsIgnoreCase("All Rounder"))
                    {
                        mCounter = Integer.parseInt(tv_allrounder.getText().toString());
                        mCounter++;

                        tv_allrounder.setText(String.valueOf(mCounter));
                    }else  if(role.equalsIgnoreCase("Batsman"))
                    {
                        mCounter = Integer.parseInt(tv_batsman.getText().toString());
                        mCounter++;

                        tv_batsman.setText(String.valueOf(mCounter));
                    }else if(role.equalsIgnoreCase("Bowler"))
                    {
                        mCounter = Integer.parseInt(tv_bowler.getText().toString());
                        mCounter++;

                        tv_bowler.setText(String.valueOf(mCounter));
                    }else  if(role.equalsIgnoreCase("Wicket Keeper"))
                    {
                        mCounter = Integer.parseInt(tv_wickerkeeper.getText().toString());
                        mCounter++;

                        tv_wickerkeeper.setText(String.valueOf(mCounter));
                    }*/

                    Toast.makeText(getActivity(), "Player removed", Toast.LENGTH_LONG).show();
                    return;
                }
            /*} else {
                Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
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
            pDialog = ProgressDialog.show(getActivity(), "Loading Players Profile", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getPlayersData("all_players");

                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    playersList = xmp.getPlayersData("");
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
                List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();
                if (list != null && list.size() > 0) {
                    selectedPlayerList = list;
                    IS_EDITING = true;
                    //Config.getAlert(getActivity(), "You have " + swap_count + " swaps count available");
                } else {
                    IS_EDITING = false;
                }

                //displayList("1001");
                //scrollViewPlayers.setVisibility(View.VISIBLE);
                //------------------------------------------
                if (selectedPlayerList == null || selectedPlayerList.size() == 0) {
                    new GetUserFantasyTeam().execute();
                } else {
                    displayList("1001");
                    scrollViewPlayers.setVisibility(View.VISIBLE);
                    try {
                        setDefaultFormation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    setFormation("");
                    getAllImagesArrayTeamIcons(myView);
                }

                count_array[0] = Integer.parseInt(sharedPreference.getString(Config.USER_PURPLE_CAP, "0"));
                count_array[1] = Integer.parseInt(sharedPreference.getString(Config.USER_ORANGE_CAP, "0"));
                count_array[2] = Integer.parseInt(sharedPreference.getString(Config.USER_GOLDEN_GLOVES, "0"));
                count_array[3] = Integer.parseInt(sharedPreference.getString(Config.USER_ICONIC_PLAYER, "0"));
                count_array[4] = Integer.parseInt(sharedPreference.getString(Config.USER_TEAM_SAFETY, "0"));
                count_array[5] = Integer.parseInt(sharedPreference.getString(Config.USER_PLAYER_SAFETY, "0"));

                //--------------------------------------------------

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private class GetMatchTime extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Loading User Fantasy Team", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection("create_team_check", getActivity());

                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.getPropertyAsString("create_team_checkResult");
//                mResult = object.getPropertyAsString("create_team_check");
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

                if (mResult.contains("success")) {
                    String argument = getArguments().getString("Is_assign");
                    if (argument != null && !argument.equals("")) {

                        selectedPlayerList = dbHandler.getSelectedPlayers();
                        displayList("1001");
                        new GetInventoryCount().execute();
                    } else {
                        new GetPlayersAsync().execute();
                    };
                } else {
                    RadioButton rb = getActivity().findViewById(R.id.iv_team);
                    rb.setChecked(true);
                    Config.getAlert(getActivity(), mResult);
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    private class GetUserFantasyTeam extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //inflater1 = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
            pDialog = ProgressDialog.show(getActivity(), "Loading User Fantasy Team", "Please wait...");
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
            pDialog.dismiss();
            try {
                dbHandler.saveSelectedPlayers(selectedPlayerList);
                IS_EDITING = false;// Changed after masla
                displayList("1001");
                scrollViewPlayers.setVisibility(View.VISIBLE);

                try {
                    setDefaultFormation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setFormation("");
                getAllImagesArrayTeamIcons(myView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveTeamAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<PlayerAttributes> playerattributeList;
        List<PlayerAttributes> playeraTempList;
        String user_id;
        String team_name;
        String team_id;
        String m_swap_count;
        SharedPreferences sharedPreferences;
        String process;
        String remaining_points = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            user_id = sharedPreferences.getString(Config.USER_ID, "");
            team_name = sharedPreferences.getString(Config.TEAM_NAME, "");
            team_id = sharedPreferences.getString(Config.TEAM_ID, "");
            //if (team_id.equals(""))
                //team_id = "";
            m_swap_count = sharedPreferences.getString(Config.SWAP_COUNT, "100");
            remaining_points = budgetTextview.getText().toString();
            pDialog = ProgressDialog.show(getActivity(), "Saving", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            process = params[0];
            playerattributeList = new ArrayList<PlayerAttributes>();
            try {

                for (int i = 0; i < selectedPlayerList.size(); i++) {
                    PlayerAttributes attributes = new PlayerAttributes();
                    attributes.setFc_team_id(selectedPlayerList.get(i).getTeam_id());
                    attributes.setPlayer_id(selectedPlayerList.get(i).getPlayer_id());

                    if (selectedPlayerList.get(i).getIsCaptain().equals("null") || selectedPlayerList.get(i).getIsCaptain() == null || selectedPlayerList.get(i).getIsCaptain().equals(""))
                        attributes.setIs_captain("0");
                    else
                        attributes.setIs_captain(selectedPlayerList.get(i).getIsCaptain());

                    if (selectedPlayerList.get(i).getIsMom().equals("null") || selectedPlayerList.get(i).getIsMom() == null || selectedPlayerList.get(i).getIsMom().equals(""))
                        attributes.setIs_mom("0");
                    else
                        attributes.setIs_mom(selectedPlayerList.get(i).getIsMom());

                    if (selectedPlayerList.get(i).getIsGoldengloves().equals("null") || selectedPlayerList.get(i).getIsGoldengloves() == null || selectedPlayerList.get(i).getIsGoldengloves().equals(""))
                        attributes.setIs_gg("0");
                    else
                        attributes.setIs_gg(selectedPlayerList.get(i).getIsGoldengloves());

                    if (selectedPlayerList.get(i).getIsIconic().equals("null") || selectedPlayerList.get(i).getIsIconic() == null || selectedPlayerList.get(i).getIsIconic().equals(""))
                        attributes.setIs_iconic("0");
                    else
                        attributes.setIs_iconic(selectedPlayerList.get(i).getIsIconic());

                    if (selectedPlayerList.get(i).getIsOrangecap().equals("null") || selectedPlayerList.get(i).getIsOrangecap() == null || selectedPlayerList.get(i).getIsOrangecap().equals(""))
                        attributes.setIs_orange_cap("0");
                    else
                        attributes.setIs_orange_cap(selectedPlayerList.get(i).getIsOrangecap());

                    if (selectedPlayerList.get(i).getIsTeamSafety().equals("null") || selectedPlayerList.get(i).getIsTeamSafety() == null || selectedPlayerList.get(i).getIsTeamSafety().equals(""))
                        attributes.setIs_team_safety("0");
                    else
                        attributes.setIs_team_safety(selectedPlayerList.get(i).getIsTeamSafety());

                    if (selectedPlayerList.get(i).getIsPrurplecap().equals("null") || selectedPlayerList.get(i).getIsPrurplecap() == null || selectedPlayerList.get(i).getIsPrurplecap().equals(""))
                        attributes.setIs_purple_cap("0");
                    else
                        attributes.setIs_purple_cap(selectedPlayerList.get(i).getIsPrurplecap());

                    if (selectedPlayerList.get(i).getIsSafety().equals("null") || selectedPlayerList.get(i).getIsSafety() == null || selectedPlayerList.get(i).getIsSafety().equals("")) {
                        attributes.setIs_safety("0");
                    } else {
                        attributes.setIs_safety(selectedPlayerList.get(i).getIsSafety());
                    }

                    if (selectedPlayerList.get(i).getIsTeamSafety().equals("1"))
                        attributes.setIs_safety(selectedPlayerList.get(i).getIsTeamSafety());


                    if (count_array[0] != null && count_array[0] > 0)
                        attributes.setIs_purple_cap_count(String.valueOf(count_array[0]));
                    else
                        attributes.setIs_purple_cap_count("0");

                    if (count_array[1] != null && count_array[1] > 0)
                        attributes.setIs_orange_cap_count(String.valueOf(count_array[1]));
                    else
                        attributes.setIs_orange_cap_count("0");

                    if (count_array[2] != null && count_array[2] > 0)
                        attributes.setIs_gg_count(String.valueOf(count_array[2]));
                    else
                        attributes.setIs_gg_count("0");

                    if (count_array[3] != null && count_array[3] > 0)
                        attributes.setIs_iconic_count(String.valueOf(count_array[3]));
                    else
                        attributes.setIs_iconic_count("0");

                    if (count_array[4] != null && count_array[4] > 0) {
                        attributes.setIs_team_safety_count(String.valueOf(count_array[4]));
                    } else {
                        attributes.setIs_team_safety_count("0");
                    }

                    if (count_array[5] != null && count_array[5] > 0) {
                        attributes.setIs_safety_count(String.valueOf(count_array[5]));
                    } else {
                        attributes.setIs_safety_count("0");
                    }


                    playerattributeList.add(attributes);
                }

                if (!team_id.equals(""))
                    tellProcesstoWebmethod = "inventory";
                else
                    tellProcesstoWebmethod = "";

                Connection connection = new Connection(getActivity());
                mResult = connection.createTeam(playerattributeList, team_id, team_name, user_id, String.valueOf(swap_count), tellProcesstoWebmethod, remaining_points);

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
                Config.SHOULD_REFRESH_TEAM = true;
                SharedPreferences.Editor editor = sharedPreference.edit();
                if (mResult.toString().toLowerCase().contains("success")) {
                    editor.putString(Config.USER_BUDGET, String.valueOf(userPoints));
                    editor.putString(Config.SWAP_COUNT, String.valueOf(swap_count));
                    //editor.commit();
                    editor.putString(Config.TEAM_ID, mResult.toString().split("~")[1]);
                    editor.commit();
                    if (IS_EDITING) {
                        getAlert(getActivity(), "Your team has been updated for next match." + " Now You have " + swap_count + " swaps count available.", process);
                        tv_swaps_count.setText(String.valueOf(swap_count));
                    } else {
                        getAlert(getActivity(), "Your team has been created for next match.", process);
                    }
                    try {
                        List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();
                        if (list.size() > 0)
                            dbHandler.deleteAll();
                        dbHandler.saveSelectedPlayers(selectedPlayerList);

                        if (editor != null)
                            commitInventory();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                {
                    //Config.getAlert(getActivity(), "Facing some technical problem");
                    Config.getAlert(getActivity(), mResult.toString());
                    RadioButton rb = (RadioButton)getActivity().findViewById(R.id.iv_team);
                    rb.setChecked(true);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void displayList(String rowNumber) {

        try {

            //grand SharedPreferences.Editor editor = sharedPreference.edit();
            parent_laout.removeAllViews();
            for (int i = 0; i < playersList.size(); i++) {
                if (playersList.get(i).getTeam_id().equals(rowNumber)) {
                    final PlayerProfileVO player = playersList.get(i);
                    View myView = inflater1.inflate(R.layout.player_view, null);
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

                    for (int jj = 0; jj < selectedPlayerList.size(); jj++) {
                        tv_nameArray[jj].setText(selectedPlayerList.get(jj).getPlayer_name());
                        tv_priceArray[jj].setText(selectedPlayerList.get(jj).getPrice() + Config.getTeamName(selectedPlayerList.get(jj).getTeam_id()));

                        if(selectedPlayerList.get(jj).getRole().equalsIgnoreCase("All Rounder"))
                            tv_nameArray[jj].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(jj).getRole().equalsIgnoreCase("Bowler"))
                            tv_nameArray[jj].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(jj).getRole().equalsIgnoreCase("Batsman"))
                            tv_nameArray[jj].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                        if(selectedPlayerList.get(jj).getRole().equalsIgnoreCase("Wicket Keeper"))
                            tv_nameArray[jj].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);



                        if (selectedPlayerList.get(jj).getIsCaptain().equals("1") && selectedPlayerList.get(jj).getIsMom().equals("1"))
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                        else if (selectedPlayerList.get(jj).getIsMom().equals("1"))
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                        else if (selectedPlayerList.get(jj).getIsCaptain().equals("1"))
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                        else
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                        if (playersList.get(i).getPlayer_id().equalsIgnoreCase(selectedPlayerList.get(jj).getPlayer_id())) {
                            iv_tick.setVisibility(View.VISIBLE);
                        }

                      /*  if (selectedPlayerList.get(jj).getIsCaptain().equals("1"))
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                        else if (selectedPlayerList.get(jj).getIsMom().equals("1"))
                            layout_array[jj].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));*/

                    }

                    tv_name.setText(player.getPlayer_name());

                    if (player.getRole().equalsIgnoreCase("Bowler")) {
                        tv_average.setText("Wkts: " + player.getWickets());
                        tv_strike_rate.setText("Econ:" + player.getEconomy());
                    } else if (player.getRole().equalsIgnoreCase("All Rounder")) {
                        tv_average.setText("Econ: " + player.getEconomy());
                        tv_strike_rate.setText("SR:" + player.getBatting_strike());
                    } else {
                        tv_average.setText("Avg: " + player.getAverage());
                        tv_strike_rate.setText("SR:" + player.getBatting_strike());
                    }

                    tv_price.setText(player.getPrice());
                    parent_laout.addView(myView);
                    myView.setTag(i);
                    myView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long diff = datesDifference();

                            long post_match_diff = getPostDifference();

                            if (post_match_diff == 0)
                                post_match_diff = 31; // Just for the first match
                            //if(players_size == 0 || diff > 10) {
                            //if (diff > match_time && post_match_diff > post_match_time) {
                                if (swap_count > 0) {
                                    int tag = (Integer) v.getTag();
                                    if (selectedPlayerList.size() <= 11) {

                                        boolean IS_RETURN = false;
                                        for (int k = 0; k < selectedPlayerList.size(); k++) {
                                            if (playersList.get(tag).getPlayer_id().equals(selectedPlayerList.get(k).getPlayer_id())) {
                                                IS_RETURN = true;
                                                break;
                                            }
                                        }

                                        if (IS_EDITING == true && IS_RETURN == true) {
                                            new ViewDialog(getActivity()).showDialog(getActivity(), playersList.get(tag).getPlayer_name(), tag, iv_tick, "remove-1");
                                            return;
                                        } else if (IS_EDITING == false && IS_RETURN == true) {
                                            removeMethodFromCircle(tag, iv_tick);
                                        }
                                        if (IS_RETURN)
                                            return;
                                       /* for (int k = 0; k < selectedPlayerList.size(); k++) {
                                            if (playersList.get(tag).getPlayer_id().equals(selectedPlayerList.get(k).getPlayer_id())) {
                                                for (Iterator<PlayerProfileVO> iter = selectedPlayerList.listIterator(); iter.hasNext(); ) {
                                                    PlayerProfileVO a = iter.next();
                                                    if (a.getPlayer_id().equals(playersList.get(tag).getPlayer_id())) {
                                                        iter.remove();
                                                    }
                                                }
                                                for (int l = 0; l < tv_nameArray.length; l++) {
                                                    tv_nameArray[l].setText("");
                                                    tv_priceArray[l].setText("");
                                                }
                                                for (int j = 0; j < selectedPlayerList.size(); j++) {
                                                    tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                                                    tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice());


                                                    if (selectedPlayerList.get(j).getIsCaptain().equals("1"))
                                                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                                    else if (selectedPlayerList.get(j).getIsMom().equals("1"))
                                                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                                    else
                                                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                                }
                                                userPoints = userPoints + Long.parseLong(playersList.get(tag).getPrice());

                                                budgetTextview.setText(String.valueOf(userPoints));
                                                Toast.makeText(getActivity(), "Player removed", Toast.LENGTH_LONG).show();
                                                iv_tick.setVisibility(View.GONE);
                                                return;
                                            } else {
                                                //Toast.makeText(getActivity(), "You can select only 11 players for one team", Toast.LENGTH_LONG).show();
                                                //return;
                                            }
                                        }
                                        */


                                        int bowlers = 0;
                                        int allrounders = 0;
                                        int wicket_keeper = 0;
                                        int batsman = 0;
                                        for (int kk = 0; kk < selectedPlayerList.size(); kk++) {
                                            if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("All Rounder"))
                                                allrounders++;
                                            if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Batsman"))
                                                batsman++;
                                            if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Bowler"))
                                                bowlers++;
                                            if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Wicket Keeper"))
                                                wicket_keeper++;

                                        }


                                   /* if (playersList.get(tag).getRole().equalsIgnoreCase("Batsman") && batsman == 3 && allrounders == 4) {
                                        Toast.makeText(getActivity(), "You can't select more than three Batsman with Four All Rounders", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    if (playersList.get(tag).getRole().equalsIgnoreCase("All Rounder") && allrounders == 3 && batsman == 4) {
                                        Toast.makeText(getActivity(), "You can't select more than three All Rounders with Four Batsman", Toast.LENGTH_LONG).show();
                                        return;
                                    }*/

                                        if (selectedPlayerList.size() < 11) {

                                            if (playersList.get(tag).getRole().equalsIgnoreCase("Wicket Keeper") && wicket_keeper == 1) {
                                                //Toast.makeText(getActivity(), "You can't select more than one Wicket Keeper", Toast.LENGTH_LONG).show();
                                                Config.getAlert(getActivity(), "You can't select more than one Wicket Keeper");
                                                return;
                                            }
                                            if (playersList.get(tag).getRole().equalsIgnoreCase("Bowler") && bowlers == 3) {
                                                //Toast.makeText(getActivity(), "You can't select more than three Bowler", Toast.LENGTH_LONG).show();
                                                Config.getAlert(getActivity(), "You can't select more than three Bowler");
                                                return;
                                            }

                                            if (playersList.get(tag).getRole().equalsIgnoreCase("All Rounder") && allrounders == all_rounders_count) {
                                                //Toast.makeText(getActivity(), "You can't select more than " + allrounders +" All Rounders", Toast.LENGTH_LONG).show();
                                                Config.getAlert(getActivity(), "You can't select more than " + allrounders + " All Rounders");
                                                return;
                                            }
                                            if (playersList.get(tag).getRole().equalsIgnoreCase("Batsman") && batsman == batsman_count) {
                                                //Toast.makeText(getActivity(), "You can't select more than "+ batsman +" Batsman", Toast.LENGTH_LONG).show();
                                                Config.getAlert(getActivity(), "You can't select more than " + batsman + " Batsman");
                                                return;
                                            }
                                        /*tempPoints = userPoints - Long.parseLong(playersList.get(tag).getPrice());
                                        if (tempPoints < 1) {
                                            Toast.makeText(getActivity(), "You can't select this player in remaining budget", Toast.LENGTH_SHORT).show();

                                            budgetTextview.setText(String.valueOf(userPoints));
                                            return;
                                        }
                                        selectedPlayerList.add(playersList.get(tag));
                                        userPoints = userPoints - Long.parseLong(playersList.get(tag).getPrice());

                                        if (IS_EDITING)
                                            swap_count--;

                                        budgetTextview.setText(String.valueOf(userPoints));
                                        iv_tick.setVisibility(View.VISIBLE);
                                        for (int j = 0; j < selectedPlayerList.size(); j++) {
                                            tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                                            tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice());
                                        }*/
                                            if (IS_EDITING) {
                                                new ViewDialog(getActivity()).showDialog(getActivity(), playersList.get(tag).getPlayer_name(), tag, iv_tick, "Swap");
                                            } else {
                                                callMethod(tag, iv_tick);
                                            }

                                        } else {
                                            Config.getAlert(getActivity(), "You can select only 11 players");
                                            // Toast.makeText(getActivity(), "You can select only 11 players for one team", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Config.getAlert(getActivity(), "You can select only 11 players");
                                        //Toast.makeText(getActivity(), "You can select only 11 players for one team", Toast.LENGTH_SHORT).show();
                                    }
                                }else
                                {
                                    Config.getAlert(getActivity(), "Transfer limit exhausted.");
                                }
                            /*} else {
                                Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                            }*/
                        }

                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void removeMethodFromCircle(int tag, ImageView iv_tick) {
        // boolean RETURN = false;
        for (int k = 0; k < selectedPlayerList.size(); k++) {
            if (playersList.get(tag).getPlayer_id().equals(selectedPlayerList.get(k).getPlayer_id())) {
                int findTagtoRemove = 0;
                for (Iterator<PlayerProfileVO> iter = selectedPlayerList.listIterator(); iter.hasNext(); ) {
                    PlayerProfileVO a = iter.next();
                    findTagtoRemove++;
                    if (a.getPlayer_id().equals(playersList.get(tag).getPlayer_id())) {
                        shouldApplyteamSafety = reformInventory(findTagtoRemove - 1);
                        iter.remove();
                        setFormation("-");
                        break;
                    }
                }
                for (int l = 0; l < tv_nameArray.length; l++) {
                    tv_nameArray[l].setText("");
                    tv_priceArray[l].setText("");
                    tv_nameArray[l].setCompoundDrawablesWithIntrinsicBounds( R.drawable.green_dot, 0, 0, 0);
                }
                for (int j = 0; j < selectedPlayerList.size(); j++) {
                    tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                    tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice() + Config.getTeamName(selectedPlayerList.get(j).getTeam_id()));

                    if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("All Rounder"))
                        tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Bowler"))
                        tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Batsman"))
                        tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Wicket Keeper"))
                        tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                    if (selectedPlayerList.get(j).getIsCaptain().equals("1") && selectedPlayerList.get(j).getIsMom().equals("1"))
                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                   else if (selectedPlayerList.get(j).getIsCaptain().equals("1"))
                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                    else if (selectedPlayerList.get(j).getIsMom().equals("1"))
                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                    else
                        layout_array[j].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                }
                userPoints = userPoints + Long.parseLong(playersList.get(tag).getPrice());

                budgetTextview.setText(String.valueOf(userPoints));
                Toast.makeText(getActivity(), "Player removed", Toast.LENGTH_LONG).show();
                //tv_swaps_count.setText();
                iv_tick.setVisibility(View.GONE);
                setInventoryIconsAfterRemovePlayer();

                return;
            } else {
                //Toast.makeText(getActivity(), "You can select only 11 players for one team", Toast.LENGTH_LONG).show();
                //return;
            }
        }
    }

    void callMethod(int tag, ImageView iv_tick) {

        try {
            tempPoints = userPoints - Long.parseLong(playersList.get(tag).getPrice());
            if (tempPoints < 0) {
                //Toast.makeText(getActivity(), "You can't select this player in remaining budget", Toast.LENGTH_SHORT).show();
                Config.getAlert(getActivity(), "You can't select this player in remaining budget");
                budgetTextview.setText(String.valueOf(userPoints));
                return;
            }
            selectedPlayerList.add(playersList.get(tag));
            userPoints = userPoints - Long.parseLong(playersList.get(tag).getPrice());

            if (IS_EDITING)
                swap_count--;

            budgetTextview.setText(String.valueOf(userPoints));
            iv_tick.setVisibility(View.VISIBLE);
            for (int j = 0; j < selectedPlayerList.size(); j++) {
                tv_nameArray[j].setText(selectedPlayerList.get(j).getPlayer_name());
                tv_priceArray[j].setText(selectedPlayerList.get(j).getPrice() + Config.getTeamName(selectedPlayerList.get(j).getTeam_id()));

                if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("All Rounder"))
                    tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Bowler"))
                    tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Batsman"))
                    tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                if(selectedPlayerList.get(j).getRole().equalsIgnoreCase("Wicket Keeper"))
                    tv_nameArray[j].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);

            }

            setFormation("");
            if(IS_EDITING)
                setInventoryIconsAfterRemovePlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAlert(Context context, String message, final String process) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(message);
        alert.setCancelable(false);
        try {

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try {
                        /*if (!process.equals("")) {
                            new GetInventoryCount().execute();
                            //new ViewDialog(getActivity()).showDialog(getActivity(), "", 0, iv, "assign");
                        } else*/
                        {
                            /*Fragment fragment = new CurrentLineup();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();*/

                            //dialog.dismiss();
                            RadioButton rb = getActivity().findViewById(R.id.iv_team);
                            rb.setChecked(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        alert.show();
    }

    void getAlertiwthCheckboxes(final String player_name, final int tag) {
        Dialog dialog;

        final String[] items = {"Captain", "Golden Gloves"};

        final ArrayList itemsSelected = new ArrayList();
        boolean[] checkedValues = new boolean[items.length];
        if (selectedPlayerList.get(tag).getIsCaptain().equals("1"))
            checkedValues[0] = true;
        /*if(selectedPlayerList.get(tag).getIsGoldengloves().equals("1"))
            checkedValues[1] = true;*/

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(player_name);

        builder.setMultiChoiceItems(items, checkedValues,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            if (selectedItemId == 0) {
                                for (int i = 0; i < selectedPlayerList.size(); i++) {
                                    selectedPlayerList.get(i).setIsCaptain("0");
                                }
                                selected = true;
                                selectedPlayerList.get(tag).setIsCaptain("1");
                            }
                            if (selectedItemId == 1) {
                                for (int i = 0; i < selectedPlayerList.size(); i++) {
                                    selectedPlayerList.get(i).setIsGoldengloves("0");
                                }
                                selected = false;
                                selectedPlayerList.get(tag).setIsGoldengloves("1");
                            }

                            //itemsSelected.add(selectedItemId);
                        } else if (itemsSelected.contains(selectedItemId)) {
                            if (selectedItemId == 0)
                                selectedPlayerList.get(tag).setIsCaptain("0");
                            if (selectedItemId == 1)
                                selectedPlayerList.get(tag).setIsGoldengloves("0");
                            //itemsSelected.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Done!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if (selected)
                            Toast.makeText(getActivity(), player_name + " is yout team captain", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        dialog = builder.create();

        dialog.show();
    }

    void getFormationAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        try {

            // grand EditText edittext = new EditText(getActivity());
            //edittext.setHint("");
            alert.setTitle("Team Formations");
            alert.setMessage("Formation 1:\n4 all-rounders, 3 batsmen, 3 bowlers and a wicket keeper.\n\nFormation 2:\n4 batsmen, 3 all-rounders, 3 bowlers and a wicket keeper. ");
            //alert.setView(edittext);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        alert.show();
    }

    public class ViewDialog extends AlertDialog {
        String option = "";

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, String name, final int tag, final ImageView iv_tick, String action) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


            if (action.equals("formation")) {
                dialog.setContentView(R.layout.popup_formation);
                dialog.setCancelable(true);
                ImageView left_formation = (ImageView) dialog.findViewById(R.id.iv_left_formation);
                left_formation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Your team formation has changed", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPreference.edit();
                        editor.putString(Config.TEAM_FORMATION, "4~3");
                        editor.commit();

                        String formation = sharedPreference.getString(Config.TEAM_FORMATION, "3~4");
                        if (!formation.equals("") && formation.toString().contains("~")) {
                            all_rounders_count = Integer.parseInt(formation.split("~")[0]);
                            batsman_count = Integer.parseInt(formation.split("~")[1]);
                        }
                        setFormation("");
                        dialog.dismiss();

                    }
                });

                ImageView right_formation = (ImageView) dialog.findViewById(R.id.iv_right_formation);
                right_formation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "Your team formation has changed", Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = sharedPreference.edit();
                        editor.putString(Config.TEAM_FORMATION, "3~4");
                        editor.commit();

                        String formation = sharedPreference.getString(Config.TEAM_FORMATION, "3~4");
                        if (!formation.equals("") && formation.toString().contains("~")) {
                            all_rounders_count = Integer.parseInt(formation.split("~")[0]);
                            batsman_count = Integer.parseInt(formation.split("~")[1]);
                        }
                        setFormation("");
                        dialog.dismiss();
                    }
                });
            } else if (action.equalsIgnoreCase("Swap")) {
                dialog.setContentView(R.layout.select_captain_view);
                ImageView closeBtn = (ImageView) dialog.findViewById(R.id.btn_close);
                TextView tv_name = (TextView) dialog.findViewById(R.id.tv_player_name_add);
                tv_name.setText(name);


                closeBtn.bringToFront();
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callMethod(tag, iv_tick);
                        dialog.dismiss();
                    }
                });
                Button backButton = (Button) dialog.findViewById(R.id.btn_back);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else if (action.equalsIgnoreCase("remove")) {
                dialog.setContentView(R.layout.remove_player_view);
                ImageView closeBtn = (ImageView) dialog.findViewById(R.id.btn_close);
                TextView tv_name = (TextView) dialog.findViewById(R.id.tv_player_name_remove);
                tv_name.setText(name);

                closeBtn.bringToFront();
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMethod(tag);
                        dialog.dismiss();
                    }
                });
                Button backButton = (Button) dialog.findViewById(R.id.btn_back);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else if (action.equalsIgnoreCase("remove-1")) {
                dialog.setContentView(R.layout.remove_player_view);
                ImageView closeBtn = (ImageView) dialog.findViewById(R.id.btn_close);
                TextView tv_name = (TextView) dialog.findViewById(R.id.tv_player_name_remove);
                tv_name.setText(name);

                closeBtn.bringToFront();
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Button dialogButton = (Button) dialog.findViewById(R.id.btn_ok);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeMethodFromCircle(tag, iv_tick);
                        dialog.dismiss();
                    }
                });
                Button backButton = (Button) dialog.findViewById(R.id.btn_back);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            } else {//Assign Inventory Box

                dialog.setCancelable(true);
                dialog.setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        Fragment fragment = new CurrentLineup();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        dialog.dismiss();
                    }
                });
                dialog.setContentView(R.layout.assign_inventory_board);
                ImageView closeBtn = (ImageView) dialog.findViewById(R.id.btn_close);
                closeBtn.bringToFront();
                ImageView save_button = (ImageView) dialog.findViewById(R.id.btn_Done_inventory);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new CurrentLineup();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        dialog.dismiss();
                    }
                });

                final TextView[] tv_count = new TextView[]
                        {
                                (TextView) dialog.findViewById(R.id.txtPurple_count),
                                (TextView) dialog.findViewById(R.id.txOrangecap_count),
                                (TextView) dialog.findViewById(R.id.txtGoldenGlove_count),
                                (TextView) dialog.findViewById(R.id.txtIcon_player_count),
                                (TextView) dialog.findViewById(R.id.txSafetyCap_count),
                                (TextView) dialog.findViewById(R.id.txtPlayersaefty_count),
                        };

                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            long diff = datesDifference();
                            long post_match_diff = getPostDifference();

                            if (post_match_diff == 0)
                                post_match_diff = 31; // Just for the first match
                            //if(players_size == 0 || diff > 10) {
                            /*if (diff > match_time && post_match_diff > post_match_time) {*/
                                //if(IS_EDITED) {
                                count_array[0] = Integer.parseInt(tv_count[0].getText().toString());
                                count_array[1] = Integer.parseInt(tv_count[1].getText().toString());
                                count_array[2] = Integer.parseInt(tv_count[2].getText().toString());
                                count_array[3] = Integer.parseInt(tv_count[3].getText().toString());
                                count_array[4] = Integer.parseInt(tv_count[4].getText().toString());
                                count_array[5] = Integer.parseInt(tv_count[5].getText().toString());
                                tellProcesstoWebmethod = "inventory";

                               /* editor.putString(Config.USER_PURPLE_CAP, tv_count[0].getText().toString());
                                editor.putString(Config.USER_ORANGE_CAP, tv_count[1].getText().toString());
                                editor.putString(Config.USER_GOLDEN_GLOVES, tv_count[2].getText().toString());
                                editor.putString(Config.USER_ICONIC_PLAYER, tv_count[3].getText().toString());
                                editor.putString(Config.USER_TEAM_SAFETY, tv_count[4].getText().toString());
                                editor.putString(Config.USER_PLAYER_SAFETY, tv_count[5].getText().toString());
                                editor.commit();*/
                                
                                new SaveTeamAsync().execute("");
                                dialog.dismiss();
                                //}
                           /* else {
                                Config.getAlert(getActivity(), "No changes made");
                            }*/
                            /*} else {
                                Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                            }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                if (inventoryFields == null || inventoryFields.getId().equals("")) {
                    count_array[0] = 0;
                    count_array[1] = 0;
                    count_array[2] = 0;
                    count_array[3] = 0;
                    count_array[4] = 0;
                    count_array[5] = 0;
                }

                getAllImagesArray(dialog);
                final TextView tv_indication = (TextView) dialog.findViewById(R.id.tv_indication);

                for (int m = 0; m < tv_count.length; m++) {
                    tv_count[m].setText(String.valueOf(count_array[m]));
                }
                layout_array_inventory = new LinearLayout[]{
                        (LinearLayout) dialog.findViewById(R.id.player_layout_one_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_two_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_three_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_four_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_five_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_six_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_seven_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_eight_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_nine_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_ten_captain),
                        (LinearLayout) dialog.findViewById(R.id.player_layout_eleven_captain)
                };

                tv_nameArray_inventory = new TextView[]{
                        (TextView) dialog.findViewById(R.id.tv_player_one_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_two_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_three_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_four_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_five_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_six_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_seven_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_eight_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_nine_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_ten_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_eleven_captain)
                };

                tv_priceArray_inventory = new TextView[]{
                        (TextView) dialog.findViewById(R.id.tv_player_points_one_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_two_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_three_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_four_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_five_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_six_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_seven_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_eight_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_nine_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_ten_captain),
                        (TextView) dialog.findViewById(R.id.tv_player_points_eleven_captain)
                };

                LinearLayout[] circles_layout = new LinearLayout[]
                        {
                                (LinearLayout) dialog.findViewById(R.id.layout000),
                                (LinearLayout) dialog.findViewById(R.id.layout0001),
                                (LinearLayout) dialog.findViewById(R.id.layout0002),
                                (LinearLayout) dialog.findViewById(R.id.layout0003),
                                (LinearLayout) dialog.findViewById(R.id.layout0004),
                                (LinearLayout) dialog.findViewById(R.id.layout0005),
                        };
                final LinearLayout[] layout_fields = new LinearLayout[circles_layout.length];
                for (int k = 0; k < layout_fields.length; k++) {
                    layout_fields[k] = circles_layout[k];
                    layout_fields[k].setId(k);
                    layout_fields[k].setTag(k);
                    layout_fields[k].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long diff = datesDifference();
                            long post_match_diff = getPostDifference();

                            if (post_match_diff == 0)
                                post_match_diff = 31; // Just for the first match
                            //if(players_size == 0 || diff > 10) {
                           /* if (diff > match_time && post_match_diff > post_match_time) {*/
                                option = "";
                                int tag = (Integer) v.getTag();
                                for (int l = 0; l < layout_fields.length; l++) {
                                    layout_fields[l].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_assign_inventory));
                                }
                                layout_fields[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_selected));


                                if (tag == 0)
                                    option = "Purple Cap";
                                else if (tag == 1)
                                    option = "Orange Cap";
                                else if (tag == 2)
                                    option = "Golden Gloves";
                                else if (tag == 3)
                                    option = "Icon Player";
                                else if (tag == 4)
                                    option = "Team safety";
                                else if (tag == 5)
                                    option = "Player safety";

                                if (option.equalsIgnoreCase("Purple Cap")) {
                                    for (int u = 0; u < layout_array_inventory.length; u++) {
                                        if (selectedPlayerList.get(u).getRole().equalsIgnoreCase("Bowler") || selectedPlayerList.get(u).getRole().equalsIgnoreCase("All Rounder")) {
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_));

                                            if(selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_mom));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_captain));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_cap_mom));

                                        } else {
                                            //layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                            if (selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                                            else if (selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                            else if (selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                            else
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                        }
                                    }
                                } else if (option.equalsIgnoreCase("Orange Cap")) {
                                    for (int u = 0; u < layout_array_inventory.length; u++) {
                                        if (selectedPlayerList.get(u).getRole().equalsIgnoreCase("Batsman") || selectedPlayerList.get(u).getRole().equalsIgnoreCase("All Rounder") || selectedPlayerList.get(u).getRole().equalsIgnoreCase("Wicket keeper")) {
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_));

                                            if(selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_mom));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_captain));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_cap_mom));


                                        } else {
                                            //layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                            if (selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                                            else if (selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                            else if (selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                            else
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                                        }
                                    }
                                } else if (option.equalsIgnoreCase("Golden Gloves")) {
                                    for (int u = 0; u < layout_array_inventory.length; u++) {
                                        if (selectedPlayerList.get(u).getRole().equalsIgnoreCase("Wicket Keeper")) {
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_));

                                            if(selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_mom));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_captain));
                                            if(selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_cap_mom));

                                        } else {
                                            //layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                                            if (selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                                            else if (selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                                            else if (selectedPlayerList.get(u).getIsMom().equals("1"))
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                                            else
                                                layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                                        }
                                    }
                                } else {
                                    for (int u = 0; u < layout_array_inventory.length; u++) {
                                        layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_));

                                        if(selectedPlayerList.get(u).getIsMom().equals("1"))
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_mom));
                                        if(selectedPlayerList.get(u).getIsCaptain().equals("1"))
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_captain));
                                        if(selectedPlayerList.get(u).getIsCaptain().equals("1") && selectedPlayerList.get(u).getIsMom().equals("1"))
                                            layout_array_inventory[u].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_select_cap_mom));

                                    }

                                }

                                tv_indication.setText("Tap on any player to assign " + option + "");
                           /* } else {
                                Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                            }*/
                        }
                    });
                }

                final LinearLayout[] layout_inventory_fields = new LinearLayout[layout_array_inventory.length];

                for (int h = 0; h < selectedPlayerList.size(); h++) {
                    if (selectedPlayerList.get(h).getIsPrurplecap().equals("1"))
                        iv_array_pp[h].setVisibility(View.VISIBLE);
                    if (selectedPlayerList.get(h).getIsOrangecap().equals("1"))
                        iv_array_oc[h].setVisibility(View.VISIBLE);
                    if (selectedPlayerList.get(h).getIsGoldengloves().equals("1"))
                        iv_array_gg[h].setVisibility(View.VISIBLE);
                    if (selectedPlayerList.get(h).getIsIconic().equals("1"))
                        iv_array_ip[h].setVisibility(View.VISIBLE);
                    //if (selectedPlayerList.get(h).getIsTeamSafety().equals("1"))
                    //iv_array_ts[h].setVisibility(View.VISIBLE);
                    if (selectedPlayerList.get(h).getIsSafety().equals("1"))
                        iv_array_ps[h].setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < layout_array_inventory.length; i++) {
                    layout_inventory_fields[i] = layout_array_inventory[i];
                    layout_inventory_fields[i].setId(i);
                    layout_inventory_fields[i].setTag(i);
                    layout_inventory_fields[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                long diff = datesDifference();
                                long post_match_diff = getPostDifference();

                                if (post_match_diff == 0)
                                    post_match_diff = 31; // Just for the first match
                                //if(players_size == 0 || diff > 10) {
                                /*if (diff > match_time && post_match_diff > post_match_time) {*/
                                    int tag = (Integer) v.getTag();

                                    if (option.equalsIgnoreCase("Purple Cap")) {

                                        if (selectedPlayerList.get(tag).getRole().equalsIgnoreCase("Wicket Keeper")
                                                || selectedPlayerList.get(tag).getRole().equalsIgnoreCase("Batsman")) {
                                            Config.getAlert(getActivity(), "Purple cap can't be assign to wicket keeper & batsman");
                                            return;
                                        }

                                        if (iv_array_pp[tag].getVisibility() == View.VISIBLE) {
                                            //IS_EDITED = false;
                                            iv_array_pp[tag].setVisibility(View.GONE);
                                            selectedPlayerList.get(tag).setIsPrurplecap("0");

                                            int count = Integer.parseInt(tv_count[0].getText().toString());
                                            count++;
                                            tv_count[0].setText(String.valueOf(count));

                                        } else {
                                            if (tv_count[0].getText().toString().equals("0")) {
                                                Config.getAlert(getActivity(), "No Purple cap available");
                                                return;
                                            }

                                            //IS_EDITED = true;
                                            iv_array_pp[tag].setVisibility(View.VISIBLE);
                                            selectedPlayerList.get(tag).setIsPrurplecap("1");

                                            int count = Integer.parseInt(tv_count[0].getText().toString());
                                            count--;
                                            tv_count[0].setText(String.valueOf(count));
                                        }

                                    } else if (option.equalsIgnoreCase("Orange Cap")) {

                                        if ( selectedPlayerList.get(tag).getRole().equalsIgnoreCase("Bowler")) {
                                            Config.getAlert(getActivity(), "Orange cap can't be assign to bowler");
                                            return;
                                        }

                                        if (iv_array_oc[tag].getVisibility() == View.VISIBLE) {
                                            //IS_EDITED = false;
                                            iv_array_oc[tag].setVisibility(View.GONE);
                                            selectedPlayerList.get(tag).setIsOrangecap("0");

                                            int count = Integer.parseInt(tv_count[1].getText().toString());
                                            count++;
                                            tv_count[1].setText(String.valueOf(count));

                                        } else {

                                            if (tv_count[1].getText().toString().equals("0")) {
                                                Config.getAlert(getActivity(), "No Orange cap available");
                                                return;
                                            }

                                            //IS_EDITED = true;
                                            iv_array_oc[tag].setVisibility(View.VISIBLE);
                                            selectedPlayerList.get(tag).setIsOrangecap("1");

                                            int count = Integer.parseInt(tv_count[1].getText().toString());
                                            count--;
                                            tv_count[1].setText(String.valueOf(count));

                                        }
                                    } else if (option.equalsIgnoreCase("Golden Gloves")) {

                                        if (!selectedPlayerList.get(tag).getRole().equalsIgnoreCase("Wicket Keeper")) {
                                            Config.getAlert(getActivity(), "Golden gloves can only be assign to wicket keeper");
                                            return;
                                        }
                                        if (iv_array_gg[tag].getVisibility() == View.VISIBLE) {
                                            //IS_EDITED = false;
                                            iv_array_gg[tag].setVisibility(View.GONE);
                                            selectedPlayerList.get(tag).setIsGoldengloves("0");

                                            int count = Integer.parseInt(tv_count[2].getText().toString());
                                            count++;
                                            tv_count[2].setText(String.valueOf(count));

                                        } else {

                                            if (tv_count[2].getText().toString().equals("0")) {
                                                Config.getAlert(getActivity(), "No Golden gloves available");
                                                return;
                                            }

                                            //IS_EDITED = true;
                                            iv_array_gg[tag].setVisibility(View.VISIBLE);
                                            selectedPlayerList.get(tag).setIsGoldengloves("1");
                                            int count = Integer.parseInt(tv_count[2].getText().toString());
                                            count--;
                                            tv_count[2].setText(String.valueOf(count));

                                        }
                                    } else if (option.equalsIgnoreCase("Icon Player")) {
                                        if (iv_array_ip[tag].getVisibility() == View.VISIBLE) {
                                            //IS_EDITED = false;
                                            iv_array_ip[tag].setVisibility(View.GONE);
                                            selectedPlayerList.get(tag).setIsIconic("0");

                                            int count = Integer.parseInt(tv_count[3].getText().toString());
                                            count++;
                                            tv_count[3].setText(String.valueOf(count));

                                        } else {

                                            if (tv_count[3].getText().toString().equals("0")) {
                                                Config.getAlert(getActivity(), "No Icon player available");
                                                return;
                                            }

                                            //IS_EDITED = true;
                                            iv_array_ip[tag].setVisibility(View.VISIBLE);
                                            selectedPlayerList.get(tag).setIsIconic("1");

                                            int count = Integer.parseInt(tv_count[3].getText().toString());
                                            count--;
                                            tv_count[3].setText(String.valueOf(count));
                                        }
                                    } else if (option.equalsIgnoreCase("Team safety")) {

                                        int r = 0;
                                        for (int y = 0; y < selectedPlayerList.size(); y++) {
                                            if (selectedPlayerList.get(y).getIsSafety().equals("1")) {
                                                r++;
                                            } // if 11 it means players have team safety
                                        }
/*
                                        for (int tr = 0; tr < selectedPlayerList.size(); tr++) {

                                            if (iv_array_ps[tr].getVisibility() == View.VISIBLE) {
                                                int count = Integer.parseInt(tv_count[5].getText().toString());
                                                count++;
                                                tv_count[5].setText(String.valueOf(count));

                                                iv_array_ps[tr].setVisibility(View.GONE);
                                                selectedPlayerList.get(tr).setIsSafety("0");
                                            }
                                        }*/

                                        if (iv_array_ts[tag].getVisibility() == View.VISIBLE || r == 11) {
                                            //IS_EDITED = false;
                                            for (int ts = 0; ts < iv_array_ts.length; ts++) {
                                                iv_array_ts[ts].setVisibility(View.GONE);
                                                iv_array_ps[ts].setVisibility(View.GONE);
                                                selectedPlayerList.get(ts).setIsTeamSafety("0");
                                                selectedPlayerList.get(ts).setIsSafety("0");
                                            }

                                            int count = Integer.parseInt(tv_count[4].getText().toString());
                                            count++;
                                            tv_count[4].setText(String.valueOf(count));

                                        } else {


                                            int rr = 0;
                                            for (int y = 0; y < selectedPlayerList.size(); y++) {
                                                if (selectedPlayerList.get(y).getIsSafety().equals("1")) {
                                                    rr++;
                                                } // if 11 it means players have team safety
                                            }

                                            if (tv_count[4].getText().toString().equals("0")) {
                                                Config.getAlert(getActivity(), "No Team safety available");
                                                return;
                                            }

                                            for (int ts = 0; ts < iv_array_ts.length; ts++) {
                                                iv_array_ts[ts].setVisibility(View.VISIBLE);
                                                selectedPlayerList.get(ts).setIsTeamSafety("1");


                                                /*if (selectedPlayerList.get(ts).getIsSafety().equals("1")) {
                                                    int count = Integer.parseInt(tv_count[5].getText().toString());
                                                    count++;
                                                    tv_count[5].setText(String.valueOf(count));

                                                    iv_array_ps[ts].setVisibility(View.GONE);
                                                    selectedPlayerList.get(ts).setIsSafety("0");
                                                }*/
                                            }
                                            for (int tr = 0; tr < selectedPlayerList.size(); tr++) {
                                                selectedPlayerList.get(tr).setIsSafety("1");
                                                iv_array_ps[tr].setVisibility(View.GONE);
                                            }
                                            count_array[5] = Integer.parseInt(inventoryFields.getPlayerSafety()) + rr;
                                            tv_count[5].setText(String.valueOf(count_array[5]));


                                            //IS_EDITED = true;
                                            //iv_array_ts[tag].setVisibility(View.VISIBLE);
                                            //selectedPlayerList.get(tag).setIsTeamSafety("1");// In loop check
                                            int count = Integer.parseInt(tv_count[4].getText().toString());
                                            count--;
                                            tv_count[4].setText(String.valueOf(count));
                                        }
                                    } else if (option.equalsIgnoreCase("Player safety")) {

                                        int r = 0;
                                        for (int y = 0; y < selectedPlayerList.size(); y++) {
                                            if (selectedPlayerList.get(y).getIsSafety().equals("1")) {
                                                r++;
                                            } // if 11 it means players have team safety
                                        }

                                        if (r < 11) {

                                            if (iv_array_ps[tag].getVisibility() == View.VISIBLE) {
                                                //IS_EDITED = false;
                                                iv_array_ps[tag].setVisibility(View.GONE);
                                                selectedPlayerList.get(tag).setIsSafety("0");

                                                int count = Integer.parseInt(tv_count[5].getText().toString());
                                                count++;
                                                tv_count[5].setText(String.valueOf(count));
                                                inventoryFields.setPlayerSafety(String.valueOf(count));

                                            } else {

                                                if (tv_count[5].getText().toString().equals("0")) {
                                                    Config.getAlert(getActivity(), "No player safety available");
                                                    return;
                                                }

                                                //IS_EDITED = true;
                                                iv_array_ps[tag].setVisibility(View.VISIBLE);
                                                selectedPlayerList.get(tag).setIsSafety("1");

                                                int count = Integer.parseInt(tv_count[5].getText().toString());
                                                count--;
                                                tv_count[5].setText(String.valueOf(count));
                                            }
                                        } else {
                                            Config.getAlert(getActivity(), "Remove Team safety first to assign Player safety");
                                        }
                                    } else {
                                        Config.getAlert(getActivity(), "Please select item from inventory");
                                    }
                                /*} else {
                                    Config.getAlert(getActivity(), "You are not allowed to update your team for this match.");
                                }*/
                            } catch (Exception e) {
                            }
                        }
                    });
                }

                for (int i = 0; i < tv_count.length; i++) {
                    tv_count[i].setText(String.valueOf(count_array[i]));
                }

                for (int i = 0; i < selectedPlayerList.size(); i++) {
                    tv_nameArray_inventory[i].setText(selectedPlayerList.get(i).getPlayer_name());
                    tv_priceArray_inventory[i].setText(selectedPlayerList.get(i).getPrice() + Config.getTeamName(selectedPlayerList.get(i).getTeam_id()));


                    if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("All Rounder"))
                        tv_nameArray_inventory[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Bowler"))
                        tv_nameArray_inventory[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Batsman"))
                        tv_nameArray_inventory[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                    if(selectedPlayerList.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                        tv_nameArray_inventory[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                    if (selectedPlayerList.get(i).getIsCaptain().equals("1") && selectedPlayerList.get(i).getIsMom().equals("1"))
                        layout_array_inventory[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                    else if (selectedPlayerList.get(i).getIsMom().equals("1"))
                        layout_array_inventory[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                    else if (selectedPlayerList.get(i).getIsCaptain().equals("1"))
                        layout_array_inventory[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                    else
                        layout_array_inventory[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                }

            }

            dialog.show();

        }
    }

    void getAlert() {
        final EditText edittext = new EditText(getActivity());
        edittext.setHint("Enter your team name here");
        edittext.setMaxLines(1);
        edittext.setMaxEms(15);
        edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        //edittext.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.digits_to_allow_team_name)));

        int maxLength = 15;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edittext.setFilters(fArray);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(edittext)
                .setTitle("Team Name")
                .setCancelable(false)
                .setPositiveButton("Done", null)
                //.setNegativeButton(android.R.string.cancel, null)
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
                        value = value.replace("'", "").replace(",", "").replace("`", "").replace("!", "").replace(";", "").replace("^", "").replace("\"","").replace("?","");
                        if (!value.trim().equals("")) {

                            SharedPreferences.Editor editor = sharedPreference.edit();
                            editor.putString(Config.TEAM_NAME, value);
                            editor.commit();

                            Fragment fragment = new FormationTeam();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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

    public void getAllImagesArray(Dialog dialog) {

        iv_array_pp = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_pp_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_pp_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eleven_captain),

                };


        iv_array_oc = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_oc_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_oc_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eleven_captain),

                };

        iv_array_gg = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_gg_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_gg_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eleven_captain),

                };

        iv_array_ip = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_Ip_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_Ip_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eleven_captain),

                };

        iv_array_ts = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ts_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_ts_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eleven_captain),

                };

        iv_array_ps = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ps_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_ps_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eleven_captain),

                };

    }


    public void getAllImagesArrayCaptainViewIcons(View dialog) {

        iv_array_pp_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_pp_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_pp_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_pp_eleven_captain),

                };


        iv_array_oc_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_oc_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_oc_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_oc_eleven_captain),

                };

        iv_array_gg_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_gg_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_gg_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_gg_eleven_captain),

                };

        iv_array_ip_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_Ip_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_Ip_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_Ip_eleven_captain),

                };

        iv_array_ts_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ts_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_ts_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ts_eleven_captain),

                };

        iv_array_ps_captain_view = new ImageView[]
                {
                        (ImageView) dialog.findViewById(R.id.iv_ps_one_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_two_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_three_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_four_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_five_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_six_captain),

                        (ImageView) dialog.findViewById(R.id.iv_ps_seven_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eight_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_nine_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_ten_captain),
                        (ImageView) dialog.findViewById(R.id.iv_ps_eleven_captain),

                };

        for (int h = 0; h < selectedPlayerList.size(); h++) {
            if (selectedPlayerList.get(h).getIsPrurplecap().equals("1"))
                iv_array_pp_captain_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_pp_captain_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsOrangecap().equals("1"))
                iv_array_oc_captain_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_oc_captain_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsGoldengloves().equals("1"))
                iv_array_gg_captain_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_gg_captain_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsIconic().equals("1"))
                iv_array_ip_captain_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_ip_captain_view[h].setVisibility(View.GONE);
            //if (list.get(h).getIscaptainSafety().equals("1"))
            //iv_array_ts[h].setVisibility(View.VISIBLE);
            if (selectedPlayerList.get(h).getIsSafety().equals("1"))
                iv_array_ps_captain_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_ps_captain_view[h].setVisibility(View.GONE);
        }

    }

    public void getAllImagesArrayTeamIcons(View dialog) {

        iv_array_pp_team_view = new ImageView[]
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


        iv_array_oc_team_view = new ImageView[]
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

        iv_array_gg_team_view = new ImageView[]
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

        iv_array_ip_team_view = new ImageView[]
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

        iv_array_ts_team_view = new ImageView[]
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

        iv_array_ps_team_view = new ImageView[]
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

        for (int h = 0; h < selectedPlayerList.size(); h++) {
            if (selectedPlayerList.get(h).getIsPrurplecap().equals("1"))
                iv_array_pp_team_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_pp_team_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsOrangecap().equals("1"))
                iv_array_oc_team_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_oc_team_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsGoldengloves().equals("1"))
                iv_array_gg_team_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_gg_team_view[h].setVisibility(View.GONE);
            if (selectedPlayerList.get(h).getIsIconic().equals("1"))
                iv_array_ip_team_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_ip_team_view[h].setVisibility(View.GONE);
            //if (list.get(h).getIsTeamSafety().equals("1"))
            //iv_array_ts[h].setVisibility(View.VISIBLE);
            if (selectedPlayerList.get(h).getIsSafety().equals("1"))
                iv_array_ps_team_view[h].setVisibility(View.VISIBLE);
            else
                iv_array_ps_team_view[h].setVisibility(View.GONE);
        }

    }

    private class GetInventoryCount extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(getActivity(), "Loading Inventory", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getInventryCount(user_id);

                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    inventoryFields = xmp.getInventoryV2();
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

                if (inventoryFields != null) {
                    if (!inventoryFields.getPurpleCap().equals(""))
                        count_array[0] = Integer.parseInt(inventoryFields.getPurpleCap());
                    if (!inventoryFields.getOrangeCap().equals(""))
                        count_array[1] = Integer.parseInt(inventoryFields.getOrangeCap());
                    if (!inventoryFields.getGoldenGloves().equals(""))
                        count_array[2] = Integer.parseInt(inventoryFields.getGoldenGloves());
                    if (!inventoryFields.getIconic().equals(""))
                        count_array[3] = Integer.parseInt(inventoryFields.getIconic());
                    if (!inventoryFields.getTeamSafety().equals(""))
                        count_array[4] = Integer.parseInt(inventoryFields.getTeamSafety());
                    if (!inventoryFields.getPlayerSafety().equals(""))
                        count_array[5] = Integer.parseInt(inventoryFields.getPlayerSafety());

                    editor.putString(Config.USER_ORANGE_CAP, inventoryFields.getOrangeCap());
                    editor.putString(Config.USER_PURPLE_CAP, inventoryFields.getPurpleCap());
                    editor.putString(Config.USER_GOLDEN_GLOVES, inventoryFields.getGoldenGloves());
                    editor.putString(Config.USER_ICONIC_PLAYER, inventoryFields.getIconic());
                    editor.putString(Config.USER_TEAM_SAFETY, inventoryFields.getTeamSafety());
                    editor.putString(Config.USER_PLAYER_SAFETY, inventoryFields.getPlayerSafety());
                    editor.commit();
                }
                new ViewDialog(getActivity()).showDialog(getActivity(), "", 0, iv, "assign");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    long datesDifference() {
        long minutes_difference = 0;
        try {
            String fixturesTeam = "";
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");
            List<FixturesVO> fixturesList = dbHandler.getFixtures();
            ImageView iv_Teamone = (ImageView) myView.findViewById(R.id.team_one);
            ImageView iv_teamtwo = (ImageView) myView.findViewById(R.id.team_two);
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
                    // long hours = minutes / 60;
                    //long days = hours / 24;

                    for (int k = 0; k < 6; k++) {
                        if (team1.startsWith("Islamabad"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_islamabad_small));
                        if (team1.startsWith("Karachi"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_karachi_small));
                        if (team1.startsWith("Lahore"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_lahore_small));
                        if (team1.startsWith("Peshawar"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_peshawar_small));
                        if (team1.startsWith("Multan"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_multan_small));
                        if (team1.startsWith("Quetta"))
                            iv_Teamone.setBackground(getResources().getDrawable(R.drawable.flag_quetta_small));

                    }

                    for (int k = 0; k < 6; k++) {
                        if (team2.startsWith("Islamabad"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_islamabad_small));
                        if (team2.startsWith("Karachi"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_karachi_small));
                        if (team2.startsWith("Lahore"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_lahore_small));
                        if (team2.startsWith("Peshawar"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_peshawar_small));
                        if (team2.startsWith("Multan"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_multan_small));
                        if (team2.startsWith("Quetta"))
                            iv_teamtwo.setBackground(getResources().getDrawable(R.drawable.flag_quetta_small));
                    }
                    return minutes_difference;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return minutes_difference;
    }

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

    String replaceWithNumber(String string) {
        String result = "";
        try {

            result = string.replace("Jan", "01").replace("Feb", "02").replace("Mar", "03").replace("Apr", "04").replace("May", "05").replace("Jun", "06").replace("Jul", "07").replace("Aug", "08").replace("Sep", "09").replace("Oct", "10").replace("Nov", "11").replace("Dec", "12");

        } catch (Exception e) {
        }
        return result;
    }

    void setFormation(String process) {
        try {

            int bowlers = 0;
            int allrounders = 0;
            int wicket_keeper = 0;
            int batsman = 0;

           /* List<PlayerProfileVO> list = null;
            if(selectedPlayerList.size() == 0) {
                selectedPlayerList = list = dbHandler.getSelectedPlayers();
            }*/


            if (selectedPlayerList.size() == 0) {
                tv_allrounder.setText(String.valueOf("0" + "/" + all_rounders_count));
                tv_batsman.setText(String.valueOf("0" + "/" + batsman_count));
                tv_bowler.setText(String.valueOf("0" + "/" + bowler_count));
                tv_wicketkeeper.setText(String.valueOf("0" + "/" + wicket_keeper_count));
            } else {
                for (int kk = 0; kk < selectedPlayerList.size(); kk++) {
                    if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("All Rounder")) {
                        allrounders++;
                    }
                    if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Batsman")) {
                        batsman++;
                    }
                    if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Bowler")) {
                        bowlers++;
                    }
                    if (selectedPlayerList.get(kk).getRole().equalsIgnoreCase("Wicket Keeper")) {
                        wicket_keeper++;
                    }
                }

                if (allrounders > all_rounders_count) {
                    tv_allrounder.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_circle_small));
                } else {
                    tv_allrounder.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circle));
                }

                if (batsman > batsman_count) {
                    tv_batsman.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_circle_small));
                } else {
                    tv_batsman.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_circle));
                }

                tv_allrounder.setText(String.valueOf(allrounders + "/" + all_rounders_count));
                tv_batsman.setText(String.valueOf(batsman + "/" + batsman_count));
                tv_bowler.setText(String.valueOf(bowlers + "/" + bowler_count));
                tv_wicketkeeper.setText(String.valueOf(wicket_keeper + "/" + wicket_keeper_count));

            }


        /*    if(process.equals("-")) {
                tv_allrounder.setText(String.valueOf(all_rounders_count - allrounders));
                tv_batsman.setText(String.valueOf(batsman_count - batsman));
                tv_bowler.setText(String.valueOf(bowler_count - bowlers));
                tv_wicketkeeper.setText(String.valueOf(wicket_keeper_count - wicket_keeper));
            }else
            {
                tv_allrounder.setText(String.valueOf(all_rounders_count - allrounders));
                tv_batsman.setText(String.valueOf(batsman_count - batsman));
                tv_bowler.setText(String.valueOf(bowler_count - bowlers));
                tv_wicketkeeper.setText(String.valueOf(wicket_keeper_count - wicket_keeper));

               *//* tv_allrounder.setText(String.valueOf(allrounders));
                tv_batsman.setText(String.valueOf(batsman));
                tv_bowler.setText(String.valueOf(bowlers));
                tv_wickerkeeper.setText(wicket_keeper);*//*
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDefaultFormation() {

        try {
            int bowlers = 0;
            int allrounders = 0;
            int wicket_keeper = 0;
            int batsman = 0;


            List<PlayerProfileVO> list = dbHandler.getSelectedPlayers();
            int size = list.size();
            if (size == 0) {
                String formation = sharedPreference.getString(Config.TEAM_FORMATION, "3~4");
                if (!formation.equals("") && formation.toString().contains("~")) {
                    all_rounders_count = Integer.parseInt(formation.split("~")[0]);
                    batsman_count = Integer.parseInt(formation.split("~")[1]);
                }
            } else {
                for (int kk = 0; kk < list.size(); kk++) {
                    if (list.get(kk).getRole().equalsIgnoreCase("All Rounder")) {
                        allrounders++;
                    }
                    if (list.get(kk).getRole().equalsIgnoreCase("Batsman")) {
                        batsman++;
                    }


                }
                all_rounders_count = allrounders;
                batsman_count = batsman;

                if (allrounders == 3)
                    editor.putString(Config.TEAM_FORMATION, "3~4");
                else
                    editor.putString(Config.TEAM_FORMATION, "4~3");

                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    boolean reformInventory(int tag) {
        boolean APPLY_TEAM_SAFETY = false;
        try {
            if (selectedPlayerList.get(tag).getIsPrurplecap().equals("1")) {
                int value = count_array[0];//Integer.parseInt(sharedPreference.getString(Config.USER_PURPLE_CAP, "0"));
                pp_count = value + 1;
                iv_array_pp_team_view[tag].setVisibility(View.GONE);

            }else
            {
                pp_count = count_array[0];//Integer.parseInt(sharedPreference.getString(Config.USER_PURPLE_CAP, "0"));
            }

            if (selectedPlayerList.get(tag).getIsOrangecap().equals("1")) {
                int value = count_array[1];//Integer.parseInt(sharedPreference.getString(Config.USER_ORANGE_CAP, "0"));
                oc_count = value + 1;
                iv_array_oc_team_view[tag].setVisibility(View.GONE);
            }else
            {
                oc_count = count_array[1];//Integer.parseInt(sharedPreference.getString(Config.USER_ORANGE_CAP, "0"));
            }

            if (selectedPlayerList.get(tag).getIsGoldengloves().equals("1")) {
                int value = count_array[2];//Integer.parseInt(sharedPreference.getString(Config.USER_GOLDEN_GLOVES, "0"));
                gg_count = value + 1;
                iv_array_gg_team_view[tag].setVisibility(View.GONE);

            }else
            {
                gg_count = count_array[2];//Integer.parseInt(sharedPreference.getString(Config.USER_GOLDEN_GLOVES, "0"));
            }

            if (selectedPlayerList.get(tag).getIsIconic().equals("1")) {
                int value = count_array[3];//Integer.parseInt(sharedPreference.getString(Config.USER_ICONIC_PLAYER, "0"));
                ip_count = value + 1;
                iv_array_ip_team_view[tag].setVisibility(View.GONE);
            }else
            {
                ip_count = count_array[3];//Integer.parseInt(sharedPreference.getString(Config.USER_ICONIC_PLAYER, "0"));
            }


            layout_array[tag].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
            if (selectedPlayerList.get(tag).getIsSafety().equals("1")) {
                int count_of_tema_safety = 0;
                for (int i = 0; i < selectedPlayerList.size(); i++) {
                    if (selectedPlayerList.get(i).getIsSafety().equals("1"))
                        count_of_tema_safety++;
                }

                if (count_of_tema_safety < 11 && shouldApplyteamSafety == false) {
                    int value = count_array[5];//Integer.parseInt(sharedPreference.getString(Config.USER_PLAYER_SAFETY, "0"));
                    ps_count = value + 1;
                    iv_array_ps_team_view[tag].setVisibility(View.GONE);

                } else {
                    ps_count = count_array[5];//Integer.parseInt(sharedPreference.getString(Config.USER_PLAYER_SAFETY, "0"));
                    APPLY_TEAM_SAFETY = true;
                }
            }
            count_array[0] = pp_count;
            count_array[1] = oc_count;
            count_array[2] = gg_count;
            count_array[3] = ip_count;
            count_array[4] = Integer.parseInt(sharedPreference.getString(Config.USER_TEAM_SAFETY, "0"));
            count_array[5] = ps_count;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return APPLY_TEAM_SAFETY;
    }

    void commitInventory() {
        try {
            editor.putString(Config.USER_PURPLE_CAP, String.valueOf(count_array[0]));
            editor.putString(Config.USER_ORANGE_CAP, String.valueOf(count_array[1]));
            editor.putString(Config.USER_GOLDEN_GLOVES, String.valueOf(count_array[2]));
            editor.putString(Config.USER_ICONIC_PLAYER, String.valueOf(count_array[3]));
            editor.putString(Config.USER_TEAM_SAFETY, String.valueOf(count_array[4]));
            editor.putString(Config.USER_PLAYER_SAFETY, String.valueOf(count_array[5]));
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setInventoryIconsAfterRemovePlayer() {
        try {
            for (int h = 0; h < selectedPlayerList.size(); h++) {
                if (selectedPlayerList.get(h).getIsPrurplecap().equals("1"))
                    iv_array_pp_team_view[h].setVisibility(View.VISIBLE);
                else
                    iv_array_pp_team_view[h].setVisibility(View.GONE);

                if (selectedPlayerList.get(h).getIsOrangecap().equals("1"))
                    iv_array_oc_team_view[h].setVisibility(View.VISIBLE);
                else
                    iv_array_oc_team_view[h].setVisibility(View.GONE);

                if (selectedPlayerList.get(h).getIsGoldengloves().equals("1"))
                    iv_array_gg_team_view[h].setVisibility(View.VISIBLE);
                else
                    iv_array_gg_team_view[h].setVisibility(View.GONE);

                if (selectedPlayerList.get(h).getIsIconic().equals("1"))
                    iv_array_ip_team_view[h].setVisibility(View.VISIBLE);
                else
                    iv_array_ip_team_view[h].setVisibility(View.GONE);
                //if (list.get(h).getIsTeamSafety().equals("1"))
                //iv_array_ts[h].setVisibility(View.VISIBLE);
                if (selectedPlayerList.get(h).getIsSafety().equals("1"))
                    iv_array_ps_team_view[h].setVisibility(View.VISIBLE);
                else
                    iv_array_ps_team_view[h].setVisibility(View.GONE);

                /*if(selectedPlayerList.get(h).getIsCaptain().equals("0"))
                    layout_array[h].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));

                if(selectedPlayerList.get(h).getIsMom().equals("0"))
                    layout_array[h].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));*/

                layout_array[layout_array.length-1].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));



                if (shouldApplyteamSafety) {
                    selectedPlayerList.get(h).setIsSafety("1");
                    iv_array_ps_team_view[h].setVisibility(View.VISIBLE);
                }
            }

            if(selectedPlayerList.size() < 11)
            {
                if(selectedPlayerList.size() == 0)
                {
                    for (int i = selectedPlayerList.size(); i < 11; i++) {
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                    }
                }else if(selectedPlayerList.size() > 0) {
                    for (int i = selectedPlayerList.size(); i < 11; i++) {
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.player_field_team_));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAllArrays(View view)
    {
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

        layout_array_captain = new LinearLayout[]{
                (LinearLayout) view.findViewById(R.id.player_layout_one_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_two_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_three_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_four_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_five_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_six_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_seven_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_eight_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_nine_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_ten_captain),
                (LinearLayout) view.findViewById(R.id.player_layout_eleven_captain)
        };

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

        tv_nameArray_captain = new TextView[]{
                (TextView) view.findViewById(R.id.tv_player_one_captain),
                (TextView) view.findViewById(R.id.tv_player_two_captain),
                (TextView) view.findViewById(R.id.tv_player_three_captain),
                (TextView) view.findViewById(R.id.tv_player_four_captain),
                (TextView) view.findViewById(R.id.tv_player_five_captain),
                (TextView) view.findViewById(R.id.tv_player_six_captain),
                (TextView) view.findViewById(R.id.tv_player_seven_captain),
                (TextView) view.findViewById(R.id.tv_player_eight_captain),
                (TextView) view.findViewById(R.id.tv_player_nine_captain),
                (TextView) view.findViewById(R.id.tv_player_ten_captain),
                (TextView) view.findViewById(R.id.tv_player_eleven_captain)
        };

        tv_priceArray_captain = new TextView[]{
                (TextView) view.findViewById(R.id.tv_player_points_one_captain),
                (TextView) view.findViewById(R.id.tv_player_points_two_captain),
                (TextView) view.findViewById(R.id.tv_player_points_three_captain),
                (TextView) view.findViewById(R.id.tv_player_points_four_captain),
                (TextView) view.findViewById(R.id.tv_player_points_five_captain),
                (TextView) view.findViewById(R.id.tv_player_points_six_captain),
                (TextView) view.findViewById(R.id.tv_player_points_seven_captain),
                (TextView) view.findViewById(R.id.tv_player_points_eight_captain),
                (TextView) view.findViewById(R.id.tv_player_points_nine_captain),
                (TextView) view.findViewById(R.id.tv_player_points_ten_captain),
                (TextView) view.findViewById(R.id.tv_player_points_eleven_captain)
        };

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
                    tv_countdownTimer.setText("Match is in progress ");
                    if(timer!=null){
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

}