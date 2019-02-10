package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import com.psl.fantasy.league.season2.R;;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.psl.classes.*;
import com.psl.transport.Connection;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob on 18/09/16.
 */


public class FormationTeam extends Fragment {
    SharedPreferences sharedPreferences;
    ImageView iv_profile;
    String user_id;
    DatabaseHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View view = inflater.inflate(R.layout.fragment_formation, container, false);
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        ImageView btnLeft = (ImageView) view.findViewById(R.id.iv_left_formation);
        ImageView btnRight = (ImageView) view.findViewById(R.id.iv_right_formation);

        iv_profile = (ImageView) view.findViewById(R.id.ivUserImage);
        iv_profile.bringToFront();

        TextView user_name = (TextView) view.findViewById(R.id.user_field);
        user_name.bringToFront();
        TextView rank_field = (TextView) view.findViewById(R.id.star_field);
        TextView coins_field = (TextView) view.findViewById(R.id.coin_field);
        String name = sharedPreferences.getString(Config.NAME, "");

        user_id = sharedPreferences.getString(Config.USER_ID, "");

        if (name.equals(""))
            name = sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, "");
        user_name.setText(name);
        rank_field.setText(sharedPreferences.getString(Config.USER_RANK, "100"));
        coins_field.setText(Config.format(sharedPreferences.getString(Config.USER_BUDGET, "100000")));

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Black.ttf");
        user_name.setTypeface(custom_font);
        rank_field.setTypeface(custom_font);
        coins_field.setTypeface(custom_font);


        try {
            dbHandler = new DatabaseHandler(getActivity());
            //String fb_image = sharedPreferences.getString(Config.PICTURE, "");
            String base64 = sharedPreferences.getString(Config.IMAGE_DATA, "");
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (decodedByte != null) {
                iv_profile.setBackground(null);
                iv_profile.setImageBitmap(decodedByte);

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) user_name.getLayoutParams();
                params.bottomMargin = 30;
                user_name.setLayoutParams(params);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.TEAM_FORMATION, "4~3");
                editor.commit();

                final RadioButton rb = getActivity().findViewById(R.id.iv_fake);
                rb.setChecked(true);

                Fragment fragment = new Team();
                Bundle args = new Bundle();
                args.putString("Is_assign", "");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Config.TEAM_FORMATION, "3~4");
                editor.commit();

                Fragment fragment = new Team();
                Bundle args = new Bundle();
                args.putString("Is_assign", "");
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (Config.homeFragment != null) {
                        Fragment fragment = Config.homeFragment;
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }
                    MainActivity.back_order = "main";
                    return true;
                }
                return false;
            }
        });

        try {
            new GetUserFantasyTeam().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
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
                }
                Config.SHOULD_REFRESH_TEAM = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}