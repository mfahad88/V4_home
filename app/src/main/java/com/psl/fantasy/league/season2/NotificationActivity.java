package com.psl.fantasy.league.season2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.JSUtils;
import com.psl.classes.NotificationClass;
import com.psl.transport.Connection;

public class NotificationActivity extends Fragment {

    LinearLayout layoutNotifMain;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notification);
        View view =  inflater.inflate(R.layout.activity_notification, container, false);
        layoutNotifMain=(LinearLayout)view.findViewById(R.id.layoutNotifMain);
        populateNotifications();
        return view;
    }
    void populateNotifications()
    {

        try {
            layoutNotifMain.removeAllViews();
            LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = null;//inflator.inflate(R.layout.activity_, null);
            for (int i = 0; i < JSUtils.notificationList.size(); i++) {
                myView = inflator.inflate(R.layout.main_notification_prototype, null);
                TextView txtDescription = myView.findViewById(R.id.textView32);
                TextView txtTitle = myView.findViewById(R.id.txtTitle);
                ImageView imageView = myView.findViewById(R.id.btnClear);
                TextView txtNotifDateTime=myView.findViewById(R.id.txtNotifDateTime);
                imageView.bringToFront();
                try {
                    final NotificationClass notificationClass = JSUtils.notificationList.get(i);
                    if (notificationClass != null) {
                        txtDescription.setText(notificationClass.getBody());
                        txtTitle.setText(notificationClass.getTitle());
                        txtNotifDateTime.setText(notificationClass.getDate());

                        final int pos = i;
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    JSUtils.notificationList.remove(pos);
                                    populateNotifications();
                                    Intent registrationComplete = new Intent(Config.PUSH_NOTIFICATION);
                                    registrationComplete.putExtra("message", "REFRESH_LOCAL");
                                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(registrationComplete);
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected void onPreExecute() {
                                            super.onPreExecute();
                                        }
                                        @Override
                                        protected Void doInBackground(Void... params) {

                                            try {
                                                Connection connection = new Connection(getActivity());
                                                connection.setNotificationsRead(notificationClass.getId(), "deleted");
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                            return null;
                                        }
                                    }.execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
                catch (Exception e) {
                    //  myView.findViewById(R.id.layout).setVisibility(View.GONE);
                }


                layoutNotifMain.addView(myView);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
