package com.psl.fantasy.league.season2;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesVO;
import com.psl.classes.PlayerProfileVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob on 13/01/18.
 */

public class CurrentLineup extends Fragment {
    SharedPreferences sharedPreference;
    TextView[] tv_nameArray, tv_priceArray;
    DatabaseHandler dbHandler;
    List<FixturesVO> fixturesList;
    long seconds_countdown = 0;
    TextView tv_countdownTimer;
    CountDownTimer timer;
    LinearLayout[] layout_array;
    String fixturesTeam;
    ImageView iv_Teamone;
    ImageView iv_teamtwo;
    List<PlayerProfileVO> list;
    ImageView[] iv_array_pp, iv_array_oc, iv_array_gg, iv_array_ip, iv_array_ts, iv_array_ps;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_current_lineup, container, false);
            dbHandler = new DatabaseHandler(getActivity());
            TextView budgetTextview = (TextView) view.findViewById(R.id.tv_budget);
            TextView rankTextview = (TextView) view.findViewById(R.id.user_rank_team);
            tv_countdownTimer = (TextView) view.findViewById(R.id.tv_timer);
            sharedPreference = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
            String budget = sharedPreference.getString(Config.USER_BUDGET, "100000");
            budgetTextview.setText(Config.format(budget));
            rankTextview.setText(sharedPreference.getString(Config.USER_RANK, "100"));

            Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Black.ttf");
            budgetTextview.setTypeface(custom_font);
            rankTextview.setTypeface(custom_font);

            iv_Teamone = (ImageView) view.findViewById(R.id.team_one);
            iv_teamtwo = (ImageView) view.findViewById(R.id.team_two);
            getAllImagesArray(view);

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

            DatabaseHandler dbhandler = new DatabaseHandler(getActivity());
            list = dbhandler.getSelectedPlayers();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    tv_nameArray[i].setText(list.get(i).getPlayer_name());
                    tv_priceArray[i].setText(list.get(i).getPrice() + Config.getTeamName(list.get(i).getTeam_id()));


                    if(list.get(i).getRole().equalsIgnoreCase("All Rounder"))
                        tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.allrounder_field_icon, 0, 0, 0);

                    if(list.get(i).getRole().equalsIgnoreCase("Bowler"))
                        tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.bowler_field_icon, 0, 0, 0);

                    if(list.get(i).getRole().equalsIgnoreCase("Batsman"))
                        tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.batsman_field_icon, 0, 0, 0);

                    if(list.get(i).getRole().equalsIgnoreCase("Wicket Keeper"))
                        tv_nameArray[i].setCompoundDrawablesWithIntrinsicBounds( R.drawable.wicket_keeper_field_icon, 0, 0, 0);


                    if (list.get(i).getIsCaptain().equals("1") && list.get(i).getIsMom().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_captain_field_large));
                   else if (list.get(i).getIsCaptain().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.captain_field));
                    else if (list.get(i).getIsMom().equals("1"))
                        layout_array[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.mom_field));
                }
            }
            View main_view = getActivity().findViewById(R.id.iv_nc);
            main_view.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int h = 0; h < list.size(); h++) {
            if (list.get(h).getIsPrurplecap().equals("1"))
                iv_array_pp[h].setVisibility(View.VISIBLE);
            if (list.get(h).getIsOrangecap().equals("1"))
                iv_array_oc[h].setVisibility(View.VISIBLE);
            if (list.get(h).getIsGoldengloves().equals("1"))
                iv_array_gg[h].setVisibility(View.VISIBLE);
            if (list.get(h).getIsIconic().equals("1"))
                iv_array_ip[h].setVisibility(View.VISIBLE);
            //if (list.get(h).getIsTeamSafety().equals("1"))
            //iv_array_ts[h].setVisibility(View.VISIBLE);
            if (list.get(h).getIsSafety().equals("1"))
                iv_array_ps[h].setVisibility(View.VISIBLE);
        }


        try {
            datesDifference();
            startCountDown(view);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ((ImageView) view.findViewById(R.id.btn_lets_play)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Fragment fragment = new Dashboard();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ((ImageView) view.findViewById(R.id.btn_edit_team)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                   /* long diff = Config.datesDifference(getActivity());
                    long post_match_diff = Config.getPostDifference(getActivity());

                    if (post_match_diff == 0)
                        post_match_diff = 30;

                    if (diff > Config.match_time && post_match_diff > Config.post_match_time) {*/

                        Fragment fragment = new Team();
                        Bundle args = new Bundle();
                        args.putString("Is_assign", "");
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                   /* }else
                    {
                        Config.getAlert(getActivity(), "You cannot edit the team 30 minutes before and after match start time.");
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            /*grand RadioGroup radiogroup = getActivity().findViewById(R.id.radio_group);
            radiogroup.clearCheck();*/
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
                    return;
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
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

                tv_countdownTimer.setText("" + str_display);

                //tv_countdownTimer.setText("In " + days + " Days " + hours + " Hours " + minutes + " Minutes " + secs + " Seconds");
            /*            converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews             */
                //days.setText(TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+"");
                //hours.setText((TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)))+"");
                //mins.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+"");
                //seconds.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+"");
            }

            @Override

            public void onFinish() {
            /*
                    clearing all fields and displaying countdown finished message             */
                try {
                    Toast.makeText(getActivity(), "Match started, have an entertaining match !!!", Toast.LENGTH_LONG).show();
                    tv_countdownTimer.setText("Match is in progress ");
                    timer.cancel();
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

}