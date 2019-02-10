package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesAdapter;
import com.psl.classes.FixturesVO;
import com.psl.classes.PlayerProfileVO;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Yaqoob on 18/09/16.
 */
 

public class Home extends Fragment {
SharedPreferences sharedPreferences;
    ImageView iv_profile;
    List<PlayerProfileVO> selectedPlayerList;
    String team_name = "";
    String team_id = "";
    String user_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences= getActivity().getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        ImageView btnCreate = (ImageView) view.findViewById(R.id.btn_create);
        TextView tv_team_name = (TextView) view.findViewById(R.id.tv_name);
        TextView user_name = (TextView) view.findViewById(R.id.user_field);

        iv_profile = (RoundedImageView) view.findViewById(R.id.ivUserImage);
        iv_profile.bringToFront();
        user_name.bringToFront();
        try
        {
            //String fb_image = sharedPreferences.getString(Config.PICTURE, "");
            String base64 = sharedPreferences.getString(Config.IMAGE_DATA, "");
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            if (decodedByte != null) {
                iv_profile.setBackground(null);
                iv_profile.setImageBitmap(decodedByte);

                ViewGroup.MarginLayoutParams params = ( ViewGroup.MarginLayoutParams) user_name.getLayoutParams();
                params.bottomMargin = 30;
                user_name.setLayoutParams(params);
            }
            team_id = sharedPreferences.getString(Config.TEAM_ID, "");
            team_name = sharedPreferences.getString(Config.TEAM_NAME, "");
            user_id = sharedPreferences.getString(Config.USER_ID, "");
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new Profile();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //user_name.bringToFront();
        TextView rank_field = (TextView) view.findViewById(R.id.star_field);
        TextView coins_field = (TextView) view.findViewById(R.id.coin_field);
        String name =  sharedPreferences.getString(Config.NAME, "");
                if(name.equals(""))
                    name = sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, "");
        user_name.setText(name);
        rank_field.setText(sharedPreferences.getString(Config.USER_RANK, "100"));
        coins_field.setText(sharedPreferences.getString(Config.USER_BUDGET, "100000"));

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "fonts/Montserrat-Black.ttf");
        user_name.setTypeface(custom_font);
        rank_field.setTypeface(custom_font);
        coins_field.setTypeface(custom_font);

        String team_name = sharedPreferences.getString(Config.TEAM_NAME, "No Team");
        if(!team_name.equals("")) {
            tv_team_name.setText(team_name);
            btnCreate.setBackground(getResources().getDrawable(R.drawable.circle_my_team));
        }
        else {
            tv_team_name.setText("No Team");
        }



        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getString(Config.TEAM_NAME, "").equalsIgnoreCase(""))
                {
                    getAlert();
                }else
                {
                    Fragment fragment = new FormationTeam();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        try {
            new GetFixturesAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
    private class GetFixturesAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<FixturesVO> fixruresList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(getActivity(), "Loading Data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(getActivity());
                mResult = connection.getFixturesData("fixtures");
                fixruresList = new ArrayList<FixturesVO>();
                if (mResult != null && !mResult.equals("")) {
                    XMLParser xmp = new XMLParser();
                    xmp.parse(mResult);
                    fixruresList = xmp.getFixturesData();
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

                DatabaseHandler dbhanHandler = new DatabaseHandler(getActivity());
                List<FixturesVO> list = dbhanHandler.getFixtures();
                //if(list == null || list.size() == 0 )
                {
                        dbhanHandler.deleteAllFixtures();
                        dbhanHandler.saveFixtures(fixruresList);
                }

                FixturesAdapter adapter = new FixturesAdapter(getActivity(), fixruresList,"");
                ((ListView) getActivity().findViewById(R.id.lv_fixtures)).setAdapter(adapter);

                selectedPlayerList = dbhanHandler.getSelectedPlayers();

                pDialog.dismiss();
                if(!team_name.equals("") && !team_id.equals("")
                        && (selectedPlayerList.size() == 0))
                    new GetUserFantasyTeam().execute();
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

    void getAlert1()
    {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        try {

            final EditText edittext = new EditText(getActivity());
            edittext.setHint("Enter your team name here");
            alert.setTitle("Team Name");
            edittext.setMaxLines(1);
            edittext.setMaxEms(20);

            alert.setView(edittext);

            alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //What ever you want to do with the value
                    String value = edittext.getText().toString();

                    if(!value.trim().equals("")) {

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Config.TEAM_NAME, edittext.getText().toString());
                        editor.commit();

                        Fragment fragment = new FormationTeam();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }else
                    {
                        Toast toast = Toast.makeText(getActivity(), "Enter your team name", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            });

            /*alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });*/
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        alert.show();
    }

    void getAlert()
    {
        final EditText edittext = new EditText(getActivity());
        edittext.setHint("Enter your team name here");
        edittext.setMaxLines(1);
        edittext.setMaxEms(20);
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
                Button b = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String value = edittext.getText().toString();

                        if(!value.trim().equals("")) {

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.TEAM_NAME, edittext.getText().toString());
                            editor.commit();

                            Fragment fragment = new FormationTeam();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            dialog.dismiss();
                        }else
                        {
                            Toast toast = Toast.makeText(getActivity(), "Enter your team name", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }

                    }
                });
            }
        });

        dialog.show();
    }

    private class GetUserFantasyTeam extends AsyncTask<String, String, String> {
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
                DatabaseHandler dbHandler = new DatabaseHandler(getActivity());
                dbHandler.saveSelectedPlayers(selectedPlayerList);
                pDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}