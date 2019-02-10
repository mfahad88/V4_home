package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

public class InventoryHelpDialog extends  AlertDialog {

    LinearLayout layoutDealItems;
    ImageView btn_close;
    Button btnPurchase;
    Context ctx;
    SharedPreferences sharedPreferences;
    protected InventoryHelpDialog(@NonNull Context context) {
        super(context);
        ctx=context;
        if(JSUtils.inventoryClassList.isEmpty())
            new MyAsynctask().execute();
        else {
            show();
            populateInventory();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_help);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        layoutDealItems = (LinearLayout) findViewById(R.id.layoutDealItems);
        sharedPreferences = ctx.getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        btn_close = (ImageView) findViewById(R.id.btn_close);
        btn_close.bringToFront();
        btnPurchase = (Button) findViewById(R.id.btnPurchase);



        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
        class MyAsynctask extends AsyncTask<Void,Void,Void>
        {

            ProgressDialog progressDialog;
            String res="";
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(ctx);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                Connection connection = new Connection(ctx);
                res=connection.getAllItems();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
                show();
                if(res.startsWith("-1"))
                {
                    Config.getAlert(ctx,res.replace("-1",""),"Error");
                }
                else
                {
                    XMLParser xmlParser = new XMLParser();
                    xmlParser.parse(res);
                    JSUtils.inventoryClassList =xmlParser.getInvetoryToShop();//new ArrayList<InventoryClass>();


                    populateInventory();



                }
            }
        }

    void populateInventory()
    {
        LayoutInflater inflator = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = null;//inflator.inflate(R.layout.activity_, null);

        for (int i = 0; i < JSUtils.inventoryClassList.size(); i++) {
            myView = inflator.inflate(R.layout.activity_inventory_help_prototype, null);

            TextView txtName = myView.findViewById(R.id.txtBoosterType);
            TextView txtPrice = myView.findViewById(R.id.txtItemPrice);
            ImageView imgBooster = myView.findViewById(R.id.imageView14);
            TextView txtDescription=myView.findViewById(R.id.txtDescription);




            try {
                final InventoryClass boosterClass = JSUtils.inventoryClassList.get(i);

                if (boosterClass != null) {
                    txtName.setText(boosterClass.getName());
                    imgBooster.setImageResource(boosterClass.getImage());
                    txtPrice.setText("PKR " + boosterClass.getPrice());
                    txtDescription.setText(boosterClass.getDescription());
                }

            }
            catch(Exception e)
            {
                //  myView.findViewById(R.id.layout).setVisibility(View.GONE);
            }




            layoutDealItems.addView(myView);
        }
    }


}