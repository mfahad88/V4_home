package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import static com.psl.classes.JSUtils.shopingCartList;


/**
 * Created by Belal on 18/09/16.
 */


public class InventoryPurchase extends Fragment {

    String app = "";
    TextView txtCartItems;
    Animation shake;
    Button btnProceed;
    //View view;
    LinearLayout layoutBoosters;
    SharedPreferences sharedPreferences;
    View mainView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_inventory_purchase, container, false);
        try{view.findViewById(R.id.textView31).bringToFront();} catch (Exception e){}

        mainView = view;
        layoutBoosters = view.findViewById(R.id.layoutBooster);
        txtCartItems = view.findViewById(R.id.txtCartCount);
        try {
            if (JSUtils.shopingCartList.size() > 0) {

                for (int i = 0; i < JSUtils.shopingCartList.size(); i++) {
                    InventoryClass ivClass = JSUtils.shopingCartList.get(i);
                    if (!ivClass.getDealOfDay().equals("0")) {
                        shopingCartList.clear();
                        break;
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        txtCartItems.setText(JSUtils.shopingCartList.size() + "");
        btnProceed = view.findViewById(R.id.btnProceed);
        btnProceed.setVisibility(View.GONE);

        //view.findViewById(R.id.textView11).bringToFront();
        Config.PopulateHeader(getActivity(), view.findViewById(R.id.helmet_layout));
        ((View) txtCartItems.getParent()).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JSUtils.shopingCartList.size() > 0) {
                    Fragment fragment = new ShopingCart();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);//yaqoob
                    transaction.commit();
                } else Toast.makeText(getActivity(), "Cart is empty", 1000).show();

            }
        });
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JSUtils.shopingCartList.size() > 0) {
                    Fragment fragment = new ShopingCart();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame, fragment);//yaqoob
                    transaction.commit();
                } else Toast.makeText(getActivity(), "Cart is empty", 1000).show();

            }
        });
        if (JSUtils.inventoryClassList.isEmpty())
            new MyAsynctask().execute();
        else {
            populateInventory();
            btnProceed.setVisibility(View.VISIBLE);

        }
        shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        shake.setRepeatCount(-1);
        shake.setRepeatMode(Animation.INFINITE);

        mainView.findViewById(R.id.layoutDeal).startAnimation(shake);
        mainView.findViewById(R.id.layoutDeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"Coming Soon...",1000).show();
                /*Intent intent=new Intent(getActivity(),DealOfDay.class);
                startActivity(intent);*/
                DealOfDay dealOfDay = new DealOfDay(getActivity());
                //dealOfDay.show();
            }
        });
        return view;
    }

    class MyAsynctask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Connection connection = new Connection(getActivity());
                res = connection.getAllItems();
            }
            catch (Exception e)
            {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                progressDialog.dismiss();
                if (res.startsWith("-1")) {
                    showAlert2(res.replace("-1", ""), "Failure");
                    btnProceed.setVisibility(View.GONE);
                } else {
                    XMLParser xmlParser = new XMLParser();
                    xmlParser.parse(res);
                    JSUtils.inventoryClassList = xmlParser.getInvetoryToShop();//new ArrayList<InventoryClass>();


                    populateInventory();
                    btnProceed.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void populateInventory() {
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = null;//inflator.inflate(R.layout.activity_, null);

        for (int i = 0; i < JSUtils.inventoryClassList.size(); i++) {
            myView = inflator.inflate(R.layout.activity_inventory_purchase_prototype, null);

            final TextView purchase = myView.findViewById(R.id.textView15);
            TextView txtName = myView.findViewById(R.id.txtBoosterType);
            TextView txtPrice = myView.findViewById(R.id.txtItemPrice);
            ImageView imgBooster = myView.findViewById(R.id.imageView14);
            TextView txtDescription = myView.findViewById(R.id.txtDescription);


            try {
                final InventoryClass boosterClass = JSUtils.inventoryClassList.get(i);

                if (boosterClass != null) {
                    txtName.setText(boosterClass.getName());
                    imgBooster.setImageResource(boosterClass.getImage());
                    txtPrice.setText("PKR " + boosterClass.getPrice());
                    txtDescription.setText(boosterClass.getDescription());
                }
                purchase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAndAddInventory(boosterClass);
                    }
                });

            } catch (Exception e) {
                //  myView.findViewById(R.id.layout).setVisibility(View.GONE);
            }


            layoutBoosters.addView(myView);
        }
    }

    void checkAndAddInventory(InventoryClass inventoryClass) {
        int totalCount = 0;
        boolean f = false;
        for (int i = 0; i < shopingCartList.size(); i++) {
            InventoryClass iv = JSUtils.shopingCartList.get(i);

            if (iv.getName().equals(inventoryClass.getName())) {
                inventoryClass.setItemCount(1);
                f = true;

            }
            totalCount += iv.getItemCount();
        }
        if (!f) {
            inventoryClass.setItemCount(1);
            totalCount += 1;
            JSUtils.shopingCartList.add(inventoryClass);
        }
        Toast.makeText(getActivity(), inventoryClass.getName() + " added to cart", 200).show();
        txtCartItems.setText(JSUtils.shopingCartList.size() + "");

        mainView.findViewById(R.id.layout000).startAnimation(shake);
    }

    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);

    }

    @Override
    public void onResume() {
        super.onResume();
        txtCartItems.setText(JSUtils.shopingCartList.size() + "");

    }
}