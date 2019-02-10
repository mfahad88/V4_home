package com.psl.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.psl.classes.Config;
import com.psl.classes.JSUtils;
import com.psl.classes.XMLParser;
import com.psl.fantasy.league.season2.R;
import com.psl.fantasy.league.season2.MainActivity;
import com.psl.transport.Connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;



public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";
    SharedPreferences sharedPreferences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat
        sharedPreferences=getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Map<String,String> hashMap = remoteMessage.getData();
        String action=hashMap.get("action").toLowerCase();
        switch (action){
            case "notification":
                String body=hashMap.get("body");
                String title=hashMap.get("title");
                createNotification2(body,title);
                JSUtils.notificationList.clear();
                Intent registrationComplete = new Intent(Config.PUSH_NOTIFICATION);
                registrationComplete.putExtra("message", "REFRESH");
                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
               /* new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new NotificationLoadAsync().execute();
                    }
                }).start();*/
                break;

        }

    }
    class NotificationLoadAsync extends AsyncTask<Void,Void,Void>
    {

        String res="";
        @Override
        protected Void doInBackground(Void... params) {

            Connection connection=new Connection(MyFirebaseMessagingService.this);
            res=connection.getMyNotifications();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(!res.startsWith("-1")) {
                JSUtils.notificationList.clear();
                    XMLParser xmlParser = new XMLParser();
                    xmlParser.parse(res);
                    JSUtils.notificationList=xmlParser.getNotifications();

            }
        }
    }


    private void createNotification( String messageBody,String title) {
        Intent intent = new Intent( this , MainActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(JSUtils.capitalize(title))
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
    public  void createNotification2(String messageBody,String title)
    {
        RemoteViews contentView = new RemoteViews(getPackageName(),R.layout.custom_notification_prototype);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
        contentView.setTextViewText(R.id.title, title.replace("*",""));
        contentView.setTextViewText(R.id.text, messageBody);
        long yourmilliseconds=0;
        try {
            yourmilliseconds= System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date resultdate = new Date(yourmilliseconds);

            contentView.setTextViewText(R.id.textView37, sdf.format(resultdate));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Intent notificationIntent = new Intent(this,MainActivity.class);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(contentView)
                .setContentIntent(pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBuilder.setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if(title.contains("*")) {
            notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.apni_audio);        }
        else {
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)yourmilliseconds, notification);
    }
}