package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yaqoob on 18/09/16.
 */

public class Faqs extends Fragment {
    DatabaseHandler dbhandler;
    List<FixturesVO> fixtureList;
    long seconds;
    ArrayList<String> listAnswer;

    TextView one, two, three, four, five, six, seveven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, nineteen, twenty, twentyone, twentytwo, twentythree, twentyfour, twentyfive, twentysix, twentyseven, twentyeight, twentynine, thirty, thirtyone, thirtytwo, thirtythree, thirtyfour, thirtyfive, thirtysix, thirtyseven, thirtyeight, thirtynine, fourty, fourtyone, fourtytwo;
    TextView[] array_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faqs, container, false);

        try {
            TextView tv = (TextView) view.findViewById(R.id.tv_help);
            tv.setMovementMethod(new ScrollingMovementMethod());

            String data = Config.readFile(getActivity(), "faqs.txt");
            tv.setText(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAnswers();
        getAllTextviewID(view);
        return view;
    }

    void setAnswers() {
        try {
            listAnswer = new ArrayList<String>();

            listAnswer.add("Apni Cricket league.season2 is an online cricket game where participants create virtual teams of real players and compete based on the performance of those real players in actual games.");
            listAnswer.add("There are no charges of using the app.");
            listAnswer.add("Yes, the app provides a safe and secure way to carry out transactions:\n" +
                    "i.\tNo information is stored on your mobile phone which means your username and password are never exposed even if your mobile phone is lost or stolen\n" +
                    "ii.\tAll transactions are secured through advanced encryption methodologies\n");

            listAnswer.add("Register yourself using your mobile number and creating a unique password. Complete your registration by filling out a short form or instantly signing in with your Facebook or Google ID.");
            listAnswer.add("Simply register using your mobile number.");

            listAnswer.add("The app can be downloaded from Google Play Store or App Store (Apple).");
            listAnswer.add("Android 5.0 and above. iOS 6 and above");

            listAnswer.add("Click on forget password, enter your mobile number, create a new password and provide the OTP sent on your registered mobile number.");
           /* listAnswer.add("The app can be downloaded from Google Play Store or App Store (Apple).");*/
            listAnswer.add("No, you can play without opening a wallet.");
            listAnswer.add("You can create one account per email id.");


            listAnswer.add("Wallet is a banking account maintained in JS Bank and its JCash agents.  It can be used for merchant transactions, transfer money to other wallet users and bank accounts, anytime, anywhere.");
            listAnswer.add("You can deposit money through our more than 30,000 authorized JCash agents. Agent locator is available within the app.");
            listAnswer.add("The deposited money will reflect in your wallet instantly.");


            listAnswer.add("Yes, you can withdraw money through the JCash agents by providing your mobile phone number.");
            listAnswer.add("Wallet balance can be checked through the balance inquiry feature by entering the OTP sent on your registered mobile number.");
            listAnswer.add("Daily transaction limit for the Level 1 account is Rs. 50,000.");

            listAnswer.add("No.");
            listAnswer.add("You can perform the following transactions through your wallet:\n" +
                    "i.\tBalance inquiry\n" +
                    "ii.\tBill payment\n" +
                    "iii.\tMobile Top-up\n" +
                    "iv.\tRetail purchase thru MasterPass QR\n" +
                    "v.\tIn-app purchases");
            listAnswer.add("You will get a SMS after every transaction.");


            listAnswer.add("Follow these simple steps to play on ACL App:\n" +
                    "i.\tRegister/ Log into your account\n" +
                    "ii.\tClick on create team\n" +
                    "iii.\tCreate your team of 11 players within the allocated budget of 100,000 points\n" +
                    "iv.\tMake in-app purchases in order to multiply your points and increase the chances of winning (for wallet account holders only)\n" +
                    "v.\tOnce the live match starts, your team starts earning points based on actual performances of the players selected by you. Final points, ranks and winners are declared after the end of the match\n");
            listAnswer.add("You will get 100,000 points as an in-app budget for funding purchases of team players");
            listAnswer.add("After logging in, click on create team and select 11 players of your choice.");
            listAnswer.add("11 players are required to create a team. There are two formations and one of them needs to be selected");
            listAnswer.add("You can create only one team");
            listAnswer.add("Yes, you require a stable broadband, 3G/4G internet connection to play");
            listAnswer.add("You are required to open a Wallet account, in order to buy inventory for your team ");
            listAnswer.add("•\tPurple Cap: can be assigned to the bowler which will double the  points\n" +
                    "•\tOrange Cap: can be assigned to the batsman which will double the points\n" +
                    "•\tGolden Gloves: can be assigned to the wicketkeeper which will double the points\n" +
                    "•\tSafety Cap: can be purchased to protect a player wherein no negative scoring will be applied\n" +
                    "•\tTeam Safety: can be purchased to protect your team from negative scoring\n" +
                    "•\tPlayers Transfer/ Swapping: can be used to change current players with others available in the league.season2\n" +
                    "•\tMan of the Match: will triple (x3) the points for the selected player\n" +
                    "•\tIcon Player: assigned player will earn triple (x3) points\n" +
                    "•\tDeal of the Day: look out for special combinations on daily basis\n");
            listAnswer.add("Choose your team carefully! Any player chosen by you who does not feature in the round does not get any points.");
            listAnswer.add("Yes, you can edit your team prior to the start of the match");
            listAnswer.add("Once you make changes to your team, the changes are updated immediately");
            listAnswer.add("You will score points depending on how the players in your team perform in the live match");
            listAnswer.add("Boosters are points that will be awarded whenever you perform any wallet based transactions.");
            listAnswer.add("Points are calculated depending on how your chosen player performs in the actual match. These could be multiplied using boosters or making in-app purchases.");
            listAnswer.add("You can redeem your booster points by clicking on the booster feature available below the app.");
            listAnswer.add("The points get updated immediately.");
            listAnswer.add("You can cross check your points through your dashboard.");
            listAnswer.add("The minimum balance required to acquire for a super booster is Rs. 2000.");
            listAnswer.add("You can send a ‘Request a booster’ request to a friend. Request will be received in the inbox. Once the request is acknowledged by your friend, a booster will be transferred to your account.");
            listAnswer.add("Yes, if your selected player is awarded Man of the Match, you will get (x3 times) the points of the player.");
            listAnswer.add("The winner is decided based on the highest points calculated at the end of a match. Top players will receive special prizes. Refer to the prize section for the prize details.");
            listAnswer.add("You will receive an in-app notification and announcement on GEO Super Eye show will be done at the end of a match.");
            listAnswer.add("You can deposit money through more than 30,000 JCash agents and through transferring funds from any bank account via internet banking or ATM to your JS wallet account.");
            listAnswer.add("Your team is awarded points on the basis of the real life PSL cricketers performance at the end of a designated match, round and tournament. The team which has achieved the highest aggregate points shall be declared a winner for daily, weekly, and grand prizes.");
            listAnswer.add("Please go to Contact Us page, and share your complains or queries with us. Our customer service team shall respond to your query within 48 hours.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAllTextviewID(View view) {
        try {

            final Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Black.ttf");

            array_tv = new TextView[]{

                    (TextView) view.findViewById(R.id.question_one),
                    (TextView) view.findViewById(R.id.question_two),
                    (TextView) view.findViewById(R.id.question_three),
                    (TextView) view.findViewById(R.id.question_four),
                    (TextView) view.findViewById(R.id.question_five),
                    (TextView) view.findViewById(R.id.question_six),
                    (TextView) view.findViewById(R.id.question_seven),
                    (TextView) view.findViewById(R.id.question_eight),
                    (TextView) view.findViewById(R.id.question_nine),
                    (TextView) view.findViewById(R.id.question_ten),
                    (TextView) view.findViewById(R.id.question_eleven),
                    (TextView) view.findViewById(R.id.question_twelve),
                    (TextView) view.findViewById(R.id.question_thirteen),
                    (TextView) view.findViewById(R.id.question_fourteen),
                    (TextView) view.findViewById(R.id.question_fifteen),
                    (TextView) view.findViewById(R.id.question_sixteen),
                    (TextView) view.findViewById(R.id.question_seventeen),
                    (TextView) view.findViewById(R.id.question_eighteen),
                    (TextView) view.findViewById(R.id.question_ninteen),
                    (TextView) view.findViewById(R.id.question_twenty),
                    (TextView) view.findViewById(R.id.question_twentyone),
                    (TextView) view.findViewById(R.id.question_twentytwo),
                    (TextView) view.findViewById(R.id.question_twentythree),
                    //(TextView) view.findViewById(R.id.question_twentyfour),
                    (TextView) view.findViewById(R.id.question_twentyfive),
                    (TextView) view.findViewById(R.id.question_twentysix),
                    (TextView) view.findViewById(R.id.question_twentyseven),
                    (TextView) view.findViewById(R.id.question_twentyeight),
                    (TextView) view.findViewById(R.id.question_twentynine),
                    (TextView) view.findViewById(R.id.question_thirty),
                    (TextView) view.findViewById(R.id.question_thirtyone),
                    (TextView) view.findViewById(R.id.question_thirtytwo_one),
                    (TextView) view.findViewById(R.id.question_thirtytwo),
                    (TextView) view.findViewById(R.id.question_thirtythree),
                    (TextView) view.findViewById(R.id.question_thirtyfour),
                    (TextView) view.findViewById(R.id.question_thirtyfive),
                    (TextView) view.findViewById(R.id.question_thirtysix),
                    (TextView) view.findViewById(R.id.question_thirtyseven),
                    (TextView) view.findViewById(R.id.question_thirtyeight),
                    (TextView) view.findViewById(R.id.question_thirtynine),
                    (TextView) view.findViewById(R.id.question_fourty),
                    (TextView) view.findViewById(R.id.question_fourtyone),
                    (TextView) view.findViewById(R.id.question_fourtytwo),
                    (TextView) view.findViewById(R.id.question_fourtythree),
                    (TextView) view.findViewById(R.id.question_fourtyfour)
            };

            final TextView[] layout_fields = new TextView[array_tv.length];
            for (int i = 0; i < array_tv.length; i++) {
                layout_fields[i] = array_tv[i];
                layout_fields[i].setId(i);
                layout_fields[i].setTag(i);
                //layout_fields[i].setTypeface(custom_font);
                layout_fields[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            int tag = (Integer) v.getTag();

                            new ViewDialog(getActivity()).showDialog(getActivity(), tag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class ViewDialog extends AlertDialog {
        String option = "";

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, int tag) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(true);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.setContentView(R.layout.faq_answer_layout);
            TextView tv = (TextView) dialog.findViewById(R.id.tv_answer);
            Button button= (Button) dialog.findViewById(R.id.btn_done);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            tv.setMovementMethod(new ScrollingMovementMethod());
            try {
                String data = "";

                tv.setText(listAnswer.get(tag));
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();

        }
    }
}