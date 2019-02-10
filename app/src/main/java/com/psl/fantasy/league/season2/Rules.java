package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.transport.Connection;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Yaqoob .
 */


public class Rules extends Fragment {

    TextView tv;
    ProgressBar pBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rules, container, false);
        tv = (TextView) view.findViewById(R.id.tv);
        pBar = (ProgressBar) view.findViewById(R.id.pBar);
        tv.setMovementMethod(new ScrollingMovementMethod());

        try {
            new GetRuleBook().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* try {
           // new ViewDialog(getActivity()).showDialog(getActivity(), "rules_book.txt");
        }catch (Exception e)
        {
            e.printStackTrace();
        }*/


        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles 
        getActivity().setTitle("Menu 1");
    }

    public class ViewDialog extends AlertDialog {
        String option = "";

        //boolean IS_EDITED;
        public ViewDialog(Context context) {
            super(context);
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        public void showDialog(Activity activity, String name) {
            final Dialog dialog = new Dialog(activity);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            dialog.setContentView(R.layout.rule_book_view);
            TextView tv = (TextView) dialog.findViewById(R.id.tv);
            tv.setMovementMethod(new ScrollingMovementMethod());
            try {
                String data = Config.readFile(getActivity(), "rules_book.txt");
                tv.setText(data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            dialog.show();

        }
    }

    private class GetRuleBook extends AsyncTask<String, String, String> {
        String mResult;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection("rulebook_text", getActivity());
                connection.addProperties("w_username", Config.w1);
                connection.addProperties("w_password", Config.w2);
                connection.ConnectForSingleNode();

                SoapObject object = connection.Result();
                mResult = object.getPropertyAsString("rulebook_textResult");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (mResult != null) {
                    Config.RULE_BOOK_TEXT = mResult;
                    pBar.setVisibility(View.GONE);

                    String data = "";
                    if (Config.RULE_BOOK_TEXT.equalsIgnoreCase(""))
                        data = Config.readFile(getActivity(), "rules_book.txt");
                    else
                        data = Config.RULE_BOOK_TEXT;

                    tv.setText(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}