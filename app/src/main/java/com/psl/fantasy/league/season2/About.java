package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesVO;
import com.psl.fantasy.league.season2.R;;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yaqoob on 18/09/16.
 */

public class About extends Fragment implements View.OnClickListener {
    DatabaseHandler dbhandler;
    List<FixturesVO> fixtureList;
    long seconds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        try {

            view = inflater.inflate(R.layout.fragment_about, container, false);
            //getTime();
            dbhandler = new DatabaseHandler(getActivity());
            TextView tv_version = (TextView) view.findViewById(R.id.tv_version);

            ((ImageView) view.findViewById(R.id.iv_points)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new ViewDialog(getActivity()).showDialog(getActivity(), "");
                    Fragment fragment1 = new Rules();
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.content_frame, fragment1);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                }
            });

            ((ImageView) view.findViewById(R.id.iv_redeem)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new ViewDialog(getActivity()).showDialog(getActivity(), "");
                }
            });

            ((ImageView) view.findViewById(R.id.iv_prize)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Fragment fragment_ = new Prizes();
                        FragmentManager fragmentManager_ = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction_ = fragmentManager_.beginTransaction();
                        fragmentTransaction_.replace(R.id.content_frame, fragment_);
                        fragmentTransaction_.addToBackStack(null);
                        fragmentTransaction_.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            ((ImageView) view.findViewById(R.id.iv_faqs)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Fragment fragment = new Faqs();
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

            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                String version = pInfo.versionName;
                tv_version.setText("App version: " + version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            //readFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    public void Images_OnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_points:
                //new ViewDialog(getActivity()).showDialog(getActivity(), "");
                Fragment fragment1 = new Rules();
                FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.content_frame, fragment1);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;
            case R.id.iv_redeem:
                //readFile();
                break;
            case R.id.iv_prize:
                Fragment fragment_ = new Prizes();
                FragmentManager fragmentManager_ = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction_ = fragmentManager_.beginTransaction();
                fragmentTransaction_.replace(R.id.content_frame, fragment_);
                fragmentTransaction_.addToBackStack(null);
                fragmentTransaction_.commit();
                break;
            case R.id.iv_faqs:
                Fragment fragment = new Faqs();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //new ViewDialog(getActivity()).showDialog(getActivity(), "faqs");
                //readFile();
                break;
            case R.id.iv_settings:
                //readFile();
                break;
        }
    }

    public void startCountDown(View view) {
    /*    creating object for all text views    */

        // grand TextView days = (TextView)view.findViewById(R.id.days);
        //grand TextView hours = (TextView)view.findViewById(R.id.hours);
        //grand TextView mins = (TextView)view.findViewById(R.id.minutes);
        //grand TextView seconds = (TextView)view.findViewById(R.id.seconds);

        new CountDownTimer(this.seconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            /*            converting the milliseconds into days, hours, minutes and seconds and displaying it in textviews             */
                //days.setText(TimeUnit.HOURS.toDays(TimeUnit.MILLISECONDS.toHours(millisUntilFinished))+"");
                // hours.setText((TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)))+"");
                // mins.setText((TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)))+"");
                // seconds.setText((TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))+"");
            }

            @Override

            public void onFinish() {
            /*            clearing all fields and displaying countdown finished message             */

                // days.setText("Count down completed");
                // hours.setText("");
                // mins.setText("");
                // seconds.setText("");
            }
        }.start();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Menu 1");
    }

    /*  public static String readFile() {
          String buffer_row="";
          try {
              AssetManager am = getActivity().getAssets();
              java.io.InputStream fs = am.open("terms_conditions.txt");
              BufferedReader reader=new BufferedReader(new InputStreamReader(fs));
              String datarow="";

              while((datarow=reader.readLine())!=null){
                  buffer_row+=datarow+"\n";

              }

              //getAlert(buffer_row);
              reader.close();

          }
          catch (Exception e)
          {

          }
          return; buffer_row;
      }*/
    void datesDifference() {
        try {
            Date date1 = null;
            Date date2 = null; //new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2012-06-20 16:00:00");

            fixtureList = dbhandler.getFixtures();
            String match_date = "";
            String dateAfterConvertMonth = "";
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String currentDateTime = df.format(new Date());
            long difference = 0;
            for (int i = 0; i < fixtureList.size(); i++) {
                match_date = fixtureList.get(i).getDate();
                dateAfterConvertMonth = match_date.split(" ")[0] + "-" + replaceWithNumber(match_date.split(" ")[1]) + "-" + match_date.split(" ")[2] + " " + match_date.split(" ")[3];
                //dateAfterConvertMonth = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(match_date);
                date1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(currentDateTime);
                date2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(dateAfterConvertMonth);

                seconds = (date2.getTime() - date1.getTime());
                if (date2.after(date1)) {
                    Toast.makeText(getActivity(), fixtureList.get(i).getMatch(), Toast.LENGTH_LONG).show();
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

    void getAlert(String text) {
        new AlertDialog.Builder(getActivity())
                .setTitle("License agreement")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setMessage(text)
                .show();
    }

    @Override
    public void onClick(View v) {

    }


    public class ViewDialog extends AlertDialog {
        String option = "";

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, String name) {
            try {
                final Dialog dialog = new Dialog(activity);
                dialog.setCancelable(true);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                dialog.setContentView(R.layout.fragment_rules);
                TextView tv = (TextView) dialog.findViewById(R.id.tv);
                tv.setMovementMethod(new ScrollingMovementMethod());
                try {
                    String data = "";

                    if (name.equalsIgnoreCase("faqs")) {
                        data = Config.readFile(getActivity(), "faqs.txt");
                    }
                    else {
                        data = Config.readFile(getActivity(), "rules_book.txt");
                        if(Config.RULE_BOOK_TEXT.equalsIgnoreCase(""))
                            data = Config.readFile(getActivity(), "rules_book.txt");
                        else
                            data = Config.RULE_BOOK_TEXT;
                    }

                    tv.setText(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}