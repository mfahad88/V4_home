package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import com.psl.fantasy.league.season2.R;;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.FixturesAdapter;
import com.psl.classes.FixturesVO;
import com.psl.classes.JSUtils;
import com.psl.classes.NotificationClass;
import com.psl.classes.User;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    SharedPreferences sharedPreferences;
    public static List<User> mFrndList;
    public static boolean invalidate = false;
    NavigationView navigationView;
    BroadcastReceiver mBroadcastReceiver, mRegistrationBroadcastReceiver;
    RelativeLayout relative;
    android.support.v7.app.ActionBar actionBar;
    LinearLayout layoutNotifications, layoutChildNotifications;
    TextView txtNotifCount;
    int[] mFlags = new int[]{
            R.drawable.drawer_home,//1
            R.drawable.drawer_profile,//2
            R.drawable.drawer_rules,//3
            //R.drawable.drawer_team,//current lineup 4
            R.drawable.drawer_edit_team,//5
            R.drawable.drawer_inventory,//6
            //R.drawable.assign_inventory,//assign inventory//7
            //R.drawable.drawer_leaderboard,//8
            //R.drawable.drawer_dashboard,//dashboard//6
            R.drawable.drawer_shop,//Dashboard//7
            R.drawable.drawer_booster,//8
            R.drawable.drawer_wallet,//9
            //R.drawable.drawer_history,//10
            R.drawable.drawer_about,//11
            R.drawable.drawer_prizes,//12
            R.drawable.drawer_contact_us,//12
            R.drawable.drawer_logout // logout//13

    };

     static String back_order = "main";
    boolean IS_ALL_CLEAR = false;

    String[] mCount = new String[]{
            "", "", "", "", "",
            "", "", "", "", "", "", ""};

    ////////////////////////////


    /* Time Out Work start*/ //300000
    //public static grand long DISCONNECT_TIMEOUT = 1800000;// 300000; // 30 sec = 30 * 1000 ms//1800000
    public static final long DISCONNECT_TIMEOUT_WALLET = 300000;

   /* private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };*/
    /*int match_time = 29;
    int post_match_time = 29;*/

    public static Handler disconnectHandlerWallet = new Handler() {
        public void handleMessage(Message msg) {
        }
    };


    /*    private Runnable disconnectCallback = new Runnable() {
            @Override
            public void run() {

                try {
                    grand String guid = sharedPreferences.getString(Config.GUID, "");

                    DatabaseHandler dbHandler = new DatabaseHandler(getBaseContext());
                    dbHandler.deleteAll();
                    dbHandler.deleteAllFixtures();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear().commit();
                    editor.putString(Config.GUID, guid);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (guid.trim().equals(""))
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                finish();
                            }

                        }
                    }).start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };*/
    public static Runnable disconnectCallbackWallet = new Runnable() {
        @Override
        public void run() {
            try {
                Config.WALLET_TIMEOUT = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


   /* public void resetDisconnectTimer() {
        try {
            disconnectHandler.removeCallbacks(disconnectCallback);
            disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public static void resetDisconnectTimerWallet() {
        try {
            disconnectHandlerWallet.removeCallbacks(disconnectCallbackWallet);
            disconnectHandlerWallet.postDelayed(disconnectCallbackWallet, DISCONNECT_TIMEOUT_WALLET);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public void stopDisconnectTimer() {
        try {
            disconnectHandler.removeCallbacks(disconnectCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void stopDisconnectTimerWallet() {
        try {
            disconnectHandlerWallet.removeCallbacks(disconnectCallbackWallet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUserInteraction() {
        try {
            //resetDisconnectTimer();
            resetDisconnectTimerWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }*/

    @Override
    public void onStop() {
        super.onStop();
        try {
            //stopDisconnectTimer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*Time out work end*/


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private LinearLayout mDrawer;
    private List<HashMap<String, String>> mList;
    private SimpleAdapter mAdapter;
    final private String COUNTRY = "country";
    final private String FLAG = "flag";
    final private String COUNT = "count";


    int mPosition = -1;
    String mTitle = "";

    // Array of strings storing country names
    //String[] mCountries ;

    ////////////////////////////////////


    public void incrementHitCount(int position) {
        HashMap<String, String> item = mList.get(position);
        String count = item.get(COUNT);
        item.remove(COUNT);
        if (count.equals("")) {
            count = "  1  ";
        } else {
            int cnt = Integer.parseInt(count.trim());
            cnt++;
            count = "  " + cnt + "  ";
        }
        item.put(COUNT, count);
        mAdapter.notifyDataSetChanged();
    }

    public void highlightSelectedCountry() {
        int selectedItem = mDrawerList.getCheckedItemPosition();

        if (selectedItem > 4)
            mDrawerList.setItemChecked(mPosition, true);
        else
            mPosition = selectedItem;

        if (mPosition != -1)
            getSupportActionBar().setTitle(mCount[mPosition]);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    LinearLayout iv_nc;

    void initFCM() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {

                try {
                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        //Toast.makeText(getApplicationContext(), "Push notification: " + intent.getStringExtra("token"), Toast.LENGTH_LONG).show();
                        //      Config.getAlert(MainActivity.this,"Push Notification"+ intent.getStringExtra("token"));
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        //FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        //  Toast.makeText(getApplicationContext(), "Registeration Complete! ", Toast.LENGTH_LONG).show();

                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                Connection connection = new Connection(MainActivity.this);
                                String uid = sharedPreferences.getString(Config.USER_ID, "");
                                String guid = intent.getStringExtra("token");
                                String packageName = getPackageName();
                                String version = "";
                                try {
                                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                    version = pInfo.versionName;
                                } catch (Exception e) {
                                }
                                connection.UpdateGUID(uid, guid, packageName, version);
                                return null;
                            }
                        }.execute();


                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");
                        if (message.equals("REFRESH")) {
                            new NotificationLoadAsync().execute();
                        }
                        if (message.equals("REFRESH_LOCAL")) {
                            populateNotifications();//new NotificationLoadAsync().execute();
                        }

                        //  Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                    }
                } catch (Exception e) {

                }
            }
        };

        try {
            // register GCM registration complete receiver
            LocalBroadcastManager.getInstance(this).

                    registerReceiver(mRegistrationBroadcastReceiver,
                            new IntentFilter(Config.REGISTRATION_COMPLETE));

            // register new push message receiver
            // by doing this, the activity will be notified each time a new message arrives
            LocalBroadcastManager.getInstance(this).

                    registerReceiver(mRegistrationBroadcastReceiver,
                            new IntentFilter(Config.PUSH_NOTIFICATION));
        } catch (Exception e) {
        }
    }

    void updateAppVersion() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Connection connection = new Connection(MainActivity.this);
                String uid = sharedPreferences.getString(Config.USER_ID, "");
                String guid = sharedPreferences.getString(Config.GUID, "");
                String packageName = getPackageName();
                String version = "";
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                } catch (Exception e) {
                }
                connection.UpdateGUID(uid, guid, packageName, version);
                return null;
            }
        }.execute();


    }
    DatabaseHandler dbHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        String userID = sharedPreferences.getString(Config.USER_ID, "");
        if (userID.trim().equals("")) {
            startActivity(new Intent(MainActivity.this, SplashScreen.class));
            finish();
            return;
        }
        try {
            initFCM();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        dbHandler = new DatabaseHandler(getBaseContext());

        ImageView myFab = (ImageView) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    checkWritePermission();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        iv_nc = (LinearLayout) findViewById(R.id.iv_nc);
        txtNotifCount = (TextView) findViewById(R.id.txtNotifCount);
        layoutNotifications = (LinearLayout) findViewById(R.id.layoutNotifications);
        layoutChildNotifications = (LinearLayout) findViewById(R.id.layoutNotificationChild);
        new NotificationLoadAsync().execute();


        layoutChildNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutNotifications.setVisibility(View.VISIBLE);

            }
        });
        layoutNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutNotifications.setVisibility(View.GONE);
            }
        });

        iv_nc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_order = "";
                if (layoutNotifications.getVisibility() == View.GONE || layoutNotifications.getVisibility() == View.INVISIBLE) {
                    new NotificationLoadAsync().execute();
                    layoutNotifications.setVisibility(View.VISIBLE);
                } else if (layoutNotifications.getVisibility() == View.VISIBLE)
                    layoutNotifications.setVisibility(View.GONE);
                if (JSUtils.notificationList.size() > 0) {

                    int unread = 0;
                    for (int i = 0; i < JSUtils.notificationList.size(); i++) {
                        NotificationClass notificationClass = JSUtils.notificationList.get(i);
                        if (notificationClass.getIsRead().equals("0"))
                            unread++;
                        notificationClass.setIsRead("1");
                    }
                    txtNotifCount.setText("0");
                    if (unread > 0) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Connection connection = new Connection(MainActivity.this);
                                connection.setNotificationsRead("", "");
                                return null;
                            }
                        }.execute();
                    }
                }
            }
        });


        // Title of the activity
        mTitle = (String) getTitle();

        // Getting a reference to the drawer listview
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        // Getting a reference to the sidebar drawer ( Title + ListView )
        mDrawer = (LinearLayout) findViewById(R.id.drawer);

        // Each row in the list stores country name, count and flag
        mList = new ArrayList<HashMap<String, String>>();


        for (int i = 0; i < mCount.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put(COUNTRY, mCount[i]);
            hm.put(COUNT, mCount[i]);
            hm.put(FLAG, Integer.toString(mFlags[i]));
            mList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {FLAG, COUNTRY, COUNT};

        // Ids of views in listview_layout
        int[] to = {R.id.flag, R.id.country, R.id.count};

        // Instantiating an adapter to store each items
        // R.layout.drawer_layout defines the layout of each item
        mAdapter = new SimpleAdapter(this, mList, R.layout.drawer_row_layout, from, to);

        // Getting reference to DrawerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Creating a ToggleButton for NavigationDrawer with drawer event listener
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when drawer is closed */
            public void onDrawerClosed(View view) {
                highlightSelectedCountry();
                supportInvalidateOptionsMenu();
            }

            /** Called when a drawer is opened */
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle("Select a Country");
                supportInvalidateOptionsMenu();
            }
        };

        // Setting event listener for the drawer
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // ItemClick event handler for the drawer items
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // Increment hit count of the drawer list item
                layoutNotifications.setVisibility(View.GONE);
                incrementHitCount(position);

                Fragment fragment = null;
                iv_nc.setVisibility(View.VISIBLE);

                //initializing the fragment object which is selected
                switch (position) {
                    case 0:
                        final RadioButton rb = (RadioButton) findViewById(R.id.iv_team);
                        rb.setChecked(true);
                        mDrawerLayout.closeDrawer(mDrawer);
                        break;
                    case 1:
                        fragment = new Profile();
                        final RadioButton radiogroup1 = (RadioButton) findViewById(R.id.iv_fake);
                        radiogroup1.setChecked(true);
                        break;
                    case 2:
                        fragment = new Rules();
                        break;
                    //case 3:
                    //   fragment = new CurrentLineup();
                    //  break;
                    case 3: {
                        /*long diff = datesDifference();
                        long post_match_diff = getPostDifference();

                        if (post_match_diff == 0)
                            post_match_diff = 31;

                         if (diff > Config.match_time && post_match_diff > Config.post_match_time) {*/
                             fragment = new FormationTeam();
                             Bundle arg = new Bundle();
                             arg.putString("Is_assign", "");
                             fragment.setArguments(arg);
                             iv_nc.setVisibility(View.GONE);
                        /* }else
                         {
                             Config.getAlert(MainActivity.this, "You cannot edit the team 30 minutes before and after match start time.");
                         }*/
                    }
                        break;

                    case 4:
                        //fragment = new MyInventory();
                        final RadioButton rb1 = (RadioButton) findViewById(R.id.iv_qr);
                        rb1.setChecked(true);
                        mDrawerLayout.closeDrawer(mDrawer);
                        break;

                    /*case 6:
                        fragment = new Team();
                        Bundle args = new Bundle();
                        args.putString("Is_assign", "Yes");
                        fragment.setArguments(args);
                        break;*/

                    /*case 7:
                        fragment = new Leaderboard();
                        break;*/
                    /*case 5:
                        fragment = new Dashboard();
                        break;*/

                    case 5:
                        // startActivity(new Intent(getBaseContext(), MyInventory.class));
                        //fragment = new InventoryPurchase();
                        final RadioButton rb5 = (RadioButton) findViewById(R.id.iv_shop);
                        rb5.setChecked(true);
                        mDrawerLayout.closeDrawer(mDrawer);
                        break;

                    case 6:
                        //fragment = new MyBoosters();
                        final RadioButton rb2 = (RadioButton) findViewById(R.id.iv_booster);
                        rb2.setChecked(true);
                        mDrawerLayout.closeDrawer(mDrawer);
                        break;

                    case 7:
                        boolean walletAccount = sharedPreferences.getBoolean(Config.JS_WALLET_ACCOUNT, false);
                        final RadioButton rb4 = (RadioButton) findViewById(R.id.iv_wallet);
                        rb4.setChecked(true);
                        mDrawerLayout.closeDrawer(mDrawer);
                       /* if (walletAccount) {
                            fragment = new MyWallet();

                        } else {
                            fragment = new CreateWalletAccount();
//                            startActivity(new Intent(getBaseContext(), CreateWalletAccount.class));
                        }*/
                        break;

                 /*   case 8:

                        fragment = new History();
                        mDrawerLayout.closeDrawer(mDrawer);
                        break;*/

                    case 8:
                        fragment = new About();
                        break;
                    case 9:
                        fragment = new Prizes();
                        break;
                    case 10:
                        fragment = new ContactUs();
                        break;
                    case 11:
                        getAlert("Are you sure you want to log out?");
                        if (iv_nc.getVisibility() == View.VISIBLE)
                            iv_nc.setVisibility(View.VISIBLE);
                        else
                            iv_nc.setVisibility(View.GONE);
                        break;
                }

                //replacing the fragment
                if (fragment != null) {

                    try {
                        if (fragment instanceof Dashboard)
                            back_order = "main";
                        else
                            back_order = "";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getBaseContext(), back_order, Toast.LENGTH_LONG).show();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    //radioGroup.clearCheck();
                    try {
                        final RadioButton radiogroup1 = (RadioButton) findViewById(R.id.iv_fake);
                        radiogroup1.setChecked(true);
                        IS_ALL_CLEAR = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ft.commit();
                }


                // Closing the drawer
                mDrawerLayout.closeDrawer(mDrawer);
                //return;
                //return;
            }
        });


        mDrawerList.setAdapter(mAdapter);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment selectedFragment = null;
                iv_nc.setVisibility(View.VISIBLE);
                layoutNotifications.setVisibility(View.GONE);

                if (checkedId == R.id.iv_team) {

                    selectedFragment = new Dashboard();
                    // grand RadioButton radiobuton = (RadioButton) findViewById(R.id.iv_team);
                    //radiobuton.setSelected(true);

                    /*selectedFragment = new Team();
                    Bundle args = new Bundle();
                    args.putString("Is_assign", "");
                    selectedFragment.setArguments(args);
                    iv_nc.setVisibility(View.GONE);*/
                }
                if (checkedId == R.id.iv_booster) {
                    selectedFragment = new MyBoosters();
                }
                if (checkedId == R.id.iv_qr) {
                    selectedFragment = new MyInventory();
                }
                if (checkedId == R.id.iv_shop) {
                    selectedFragment = new InventoryPurchase();
                }
                if (checkedId == R.id.iv_wallet) {
                    boolean walletAccount = sharedPreferences.getBoolean(Config.JS_WALLET_ACCOUNT, false);
                    if (walletAccount) {
                        selectedFragment = new MyWallet();

                    } else {
                        selectedFragment = new CreateWalletAccount();
                        //startActivity(new Intent(getBaseContext(), CreateWalletAccount.class));
                    }
                }
                if (checkedId == R.id.iv_agentlocator) {
                    selectedFragment = new AgentLocator();
                }
                //Debug.waitForDebugger();
                //if (selectedFragment != null && Config.GLOBAL_VAR.equals("")) {
                if (selectedFragment != null) {

                    try {
                        if (selectedFragment instanceof Dashboard)
                            back_order = "main";
                        else
                            back_order = "";
                        if (IS_ALL_CLEAR) {
                            back_order = "";
                            IS_ALL_CLEAR = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //Toast.makeText(getBaseContext(), back_order+ "34", Toast.LENGTH_LONG).show();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, selectedFragment);
                    transaction.commit();
                }else
                {
                    back_order = "";
                }
                // Debug.waitForDebugger();
                //Config.GLOBAL_VAR = "";
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        relative = (RelativeLayout) findViewById(R.id.main_frame);

        //Manually displaying the first fragment - one time only
        //Toast.makeText(getBaseContext(), "Called", Toast.LENGTH_LONG).show();

       /* String bundle_value = "";
        Intent intent = getIntent();
        if(intent !=null)
            bundle_value = intent.getStringExtra("what");

        if(bundle_value == null || bundle_value.equals("")) {*/
        //Toast.makeText(getBaseContext(), back_order, Toast.LENGTH_LONG).show();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, new Dashboard());//yaqoob
        transaction.commit();
       /* }else if(bundle_value.equalsIgnoreCase("call_team"))
        {
            try {
                try {
                    grand RadioButton radiobuton = (RadioButton) findViewById(R.id.iv_team);
                    radiobuton.setChecked(false);
                    //radiobuton.setButtonDrawable(getResources().getDrawable(R.drawable.team_selector));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Fragment fragment = new Team();
                Bundle arg = new Bundle();
                arg.putString("Is_assign", "");
                fragment.setArguments(arg);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                //radioGroup.clearCheck();

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }*//*else
        {
            try {
                Fragment fragment = new Team();
                Bundle arg = new Bundle();
                arg.putString("Is_assign", "Yes");
                fragment.setArguments(arg);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                radioGroup.clearCheck();
                //grand RadioButton radiobuton = (RadioButton) findViewById(R.id.iv_team);
                //radiobuton.setChecked(true);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu 1");


        actionBar = getSupportActionBar();

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                Connection connection = new Connection(MainActivity.this);
                String uid = sharedPreferences.getString(Config.USER_ID, "");

                String guid = sharedPreferences.getString(Config.GUID, "");
                String packageName = getPackageName();
                String version = "";
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    version = pInfo.versionName;
                } catch (Exception e) {
                }
                if (!guid.trim().equals("")) {
                    connection.UpdateGUID(uid, guid, packageName, version);
                }
                return null;
            }
        }.execute();
        /*sharedPreferences.edit().putBoolean(Config.JS_WALLET_ACCOUNT,true).commit();
        sharedPreferences.edit().putString(Config.JS_Mobile_Number,"03355100049").commit();*/
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ImageView drawerIcon = (ImageView) findViewById(R.id.iv_drawericon);
        drawerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        TextView tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert("Do you want to logout ?");
            }
        });

        View includedView = findViewById(R.id.included_view);
        navigationView = (NavigationView) includedView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView name = (TextView) includedView.findViewById(R.id.txtUserFullName);
        TextView email = (TextView) includedView.findViewById(R.id.txtEmail);
        RoundedImageView pImage = (RoundedImageView) includedView.findViewById(R.id.imgUserPic);
        name.setText(sharedPreferences.getString(Config.NAME, ""));
        email.setText(sharedPreferences.getString(Config.EMAIL, ""));
        String strBase64 = sharedPreferences.getString(Config.PICTURE, "");

        TextView tv_name = (TextView) findViewById(R.id.tv_name);
        if (sharedPreferences.getString(Config.FIRST_NAME, "").equals("")) {
            tv_name.setText(sharedPreferences.getString(Config.NAME, ""));
        } else {
            tv_name.setText("Welcome " + sharedPreferences.getString(Config.FIRST_NAME, "") + " " + sharedPreferences.getString(Config.LAST_NAME, ""));
        }
        byte[] decodedString = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        if (decodedByte != null)
            pImage.setImageBitmap(decodedByte);
        includedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(MainActivity.this, RegisterActivity.class);
                mIntent.putExtra("ACTION", "VIEW");
                startActivity(mIntent);

            }
        });

        changeStatusBarColor("");

        /*try {
            new GetFixturesAsync().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        //Toast.makeText(this, FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_SHORT).show();
    }

    private class GetFixturesAsync extends AsyncTask<String, String, String> {
        String objResult;
        ProgressDialog pDialog;
        String mResult;
        List<FixturesVO> fixruresList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = ProgressDialog.show(MainActivity.this, "Loading Data", "Please wait...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Connection connection = new Connection(MainActivity.this);
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
            pDialog.dismiss();
            try {

                FixturesAdapter adapter = new FixturesAdapter(MainActivity.this, fixruresList,"");
                ((ListView) findViewById(R.id.lv_fixtures)).setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void getAlert(String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setMessage(message);

        adb.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String guid = sharedPreferences.getString(Config.GUID, "");
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().commit();
                editor.putString(Config.GUID, guid).commit();
                DatabaseHandler dbHandler = new DatabaseHandler(getBaseContext());
                dbHandler.deleteAll();
                dbHandler.deleteAllFixtures();
                finish();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (guid.trim().equals(""))
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            System.exit(0);

                        }

                    }
                }).start();
                //startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });
        adb.show();
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new Home();
                break;
            case R.id.nav_profile:
                fragment = new Profile();
                break;
            case R.id.nav_Rules:
                fragment = new Rules();
                break;
            case R.id.nav_Team:
                fragment = new CurrentLineup();
                break;
            case R.id.nav_editTeam:
                fragment = new Team();
                Bundle arg = new Bundle();
                arg.putString("Is_assign", "");
                fragment.setArguments(arg);
                break;


            case R.id.nav_Leagues:
                //fragment = new Team();
                break;

            case R.id.nav_agent_locator:
                fragment = new AgentLocator();
                break;

            case R.id.action_settings:
                fragment = new Rules();
                break;
            case R.id.nav_leaderboard:
                fragment = new Leaderboard();
                break;
            case R.id.nav_dashboard:
                fragment = new Dashboard();
                break;

            case R.id.nav_about:
                fragment = new About();
                break;
            case R.id.logOut:
                getAlert("Are your sure you want to log out?");
                break;

            case R.id.nav_Boosters:
                // startActivity(new Intent(getBaseContext(), MyInventory.class));
                fragment = new MyBoosters();
                break;
            case R.id.nav_Inventory:
                // startActivity(new Intent(getBaseContext(), MyInventory.class));
                fragment = new MyInventory();
                break;
            case R.id.nav_Shop:
                // startActivity(new Intent(getBaseContext(), MyInventory.class));
                fragment = new InventoryPurchase();
                break;
            case R.id.nav_Wallet:
                //sharedPreferences=getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
                boolean walletAccount = sharedPreferences.getBoolean(Config.JS_WALLET_ACCOUNT, false);
                if (walletAccount) {
                    fragment = new MyWallet();

                } else {
                    startActivity(new Intent(getBaseContext(), CreateWalletAccount.class));
                }
                //finish();
                break;

            case R.id.nav_assign_inventroy:
                fragment = new Team();
                Bundle args = new Bundle();
                args.putString("Is_assign", "Yes");
                fragment.setArguments(args);
                break;


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            DatabaseHandler dbHandler = new DatabaseHandler(getBaseContext());
            dbHandler.deleteAll();
            dbHandler.deleteAllFixtures();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Toast.makeText(getApplicationContext(),"I am destroyed",Toast.LENGTH_LONG).show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to access your location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();

        try {
            String userID = sharedPreferences.getString(Config.USER_ID, "");
            if (userID.trim().equals("")) {
                startActivity(new Intent(MainActivity.this, SplashScreen.class));
                finish();
                return;
            }

            //resetDisconnectTimerWallet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //  initFCM();
    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        try {
            /*if (doubleBackToExitPressedOnce) {
                // super.onBackPressed();
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);*/
            if (back_order.equals("")) {
                layoutNotifications.setVisibility(View.GONE);
                final RadioButton rb = (RadioButton) findViewById(R.id.iv_team);
                rb.setChecked(true);
                back_order = "main";
               /* FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new Dashboard());//yaqoob
                transaction.commit();*/
            }
            else if(back_order.equals("formation"))
            {

            }
            else {
                getAlert("Are you sure you want to log out?");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);

   /*     if (id == R.id.home) {
     *//*       AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setMessage("Log me out?");
            adb.setTitle("Confirm");
            adb.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedPreferences.edit().clear().commit();
                    try {
                        LoginManager.getInstance().logOut();
                     //   Auth.GoogleSignInApi.signOut();
                    }
                    catch(Exception exp){}

                    finish();
                }
            });
            adb.setNegativeButton("Cancel", null);
            adb.show();
            // Handle the camera action
        *//*} else if (id == R.id.nav_profile) {
            Intent myIntent = new Intent(MainActivity.this, UserDashboard.class);
            startActivity(myIntent);
        }else if (id == R.id.nav_Boosters) {

            Intent myIntent = new Intent(MainActivity.this, ViewFixtures.class);
            startActivity(myIntent);
        }else if (id == R.id.nav_Shop) {

            Intent myIntent = new Intent(MainActivity.this, UserDashboard.class);
            startActivity(myIntent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);*/
        return true;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    public static Bitmap loadBitmapFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    private void changeStatusBarColor(String color) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(getBaseContext(), R.color.lv_fix_row_title_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class NotificationLoadAsync extends AsyncTask<Void, Void, Void> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            layoutChildNotifications.removeAllViews();
            findViewById(R.id.textView35).setVisibility(View.GONE);

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                if (JSUtils.notificationList.isEmpty()) {
                    Connection connection = new Connection(MainActivity.this);

                    res = connection.getMyNotifications();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {


                findViewById(R.id.progressBar2).setVisibility(View.GONE);

                if (res.equals("") || res.startsWith("-1")) {
                    populateNotifications();
                } else {
                    JSUtils.notificationList.clear();
                    XMLParser xmlParser = new XMLParser();
                    xmlParser.parse(res);
                    JSUtils.notificationList = xmlParser.getNotifications();
                    populateNotifications();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    void populateNotifications() {
        try {

            int unreadCount = 0;

            if (JSUtils.notificationList.size() <= 0) {
                findViewById(R.id.textView35).setVisibility(View.VISIBLE);
                layoutChildNotifications.setVisibility(View.GONE);
            } else {
                findViewById(R.id.textView35).setVisibility(View.GONE);
                layoutChildNotifications.setVisibility(View.VISIBLE);
            }
            layoutChildNotifications.removeAllViews();

            LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = null;//inflator.inflate(R.layout.activity_, null);


            for (int i = 0; i < JSUtils.notificationList.size(); i++) {
                myView = inflator.inflate(R.layout.mini_notification_prototype, null);

                TextView txtDescription = myView.findViewById(R.id.txtNdescript);


                try {
                    final NotificationClass notificationClass = JSUtils.notificationList.get(i);

                    String isRead = notificationClass.getIsRead();
                    if (isRead.equals("0")) {
                        unreadCount++;
                        txtDescription.setTypeface(null, Typeface.BOLD);

                    } else {
                        txtDescription.setTypeface(null, Typeface.NORMAL);
                    }
                    if (notificationClass != null) {
                        txtDescription.setText(notificationClass.getBody());
                    }
                    myView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layoutNotifications.setVisibility(View.GONE);
                            final RadioButton radiogroup1 = (RadioButton) findViewById(R.id.iv_fake);
                            radiogroup1.setChecked(true);
                            Fragment fragment = new NotificationActivity();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.content_frame, fragment);
                            ft.commit();
                        }
                    });

                } catch (Exception e) {
                    //  myView.findViewById(R.id.layout).setVisibility(View.GONE);
                }


                layoutChildNotifications.addView(myView);
            }
            //txtNotifCount.bringToFront();
            txtNotifCount.setText(unreadCount + "");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureScreen() {
        try {

            View v = getWindow().getDecorView().getRootView();
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

    private static final int REQUEST_WRITE_STORAGE = 990;

    public boolean checkWritePermission() {
        boolean bool = false;
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
            } else {
                //TakeScreenshot(mView);

                captureScreen();
                initShareIntent("text", "https://play.google.com/store/apps/details?id=com.psl.fantasy.league.season2 #JSApniCricketLeague #JSBank");
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
            try {
                //TakeScreenshot(mView);
                captureScreen();
                initShareIntent("text", "https://play.google.com/store/apps/details?id=com.psl.fantasy.league.season2 #JSApniCricketLeague #JSBank");
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
                    // long hours = minutes / 60;
                    //long days = hours / 24;

                  /*  for (int k = 0; k < 6; k++) {
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
                    }*/
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

}
