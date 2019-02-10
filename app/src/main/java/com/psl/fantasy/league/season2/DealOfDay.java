package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.TeamInventory;
import com.psl.classes.XMLParser;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.HashMap;

public class DealOfDay extends  AlertDialog {

    LinearLayout layoutDealItems;
    TeamInventory dealOftheDay;
    ImageView btn_close;
    Button btnPurchase;
    TextView txtDiscount,txtTotalPrice,txtTotalPrice2,txtActualPrice;
    Context ctx;
    SharedPreferences sharedPreferences;
    ArrayList<InventoryClass> dealItems=new ArrayList<InventoryClass>();
    protected DealOfDay(@NonNull Context context) {
        super(context);
        ctx=context;
        new MyAsynctask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_of_day);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        layoutDealItems = (LinearLayout) findViewById(R.id.layoutDealItems);
        sharedPreferences=ctx.getSharedPreferences(Config.SHARED_PREF,Context.MODE_PRIVATE);
        btn_close=(ImageView)findViewById(R.id.btn_close) ;
        btn_close.bringToFront();
        btnPurchase=(Button)findViewById(R.id.btnPurchase);

        txtDiscount=(TextView)findViewById(R.id.txtDiscount);
        txtTotalPrice=(TextView)findViewById(R.id.txtTotalPrice);
        txtActualPrice=(TextView)findViewById(R.id.txtActualPrice);
        txtTotalPrice2=(TextView)findViewById(R.id.txtTotalPrice2);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean walletAccount=sharedPreferences.getBoolean(Config.JS_WALLET_ACCOUNT,false);
                if(!walletAccount)
                {
                    /*Intent intent=new Intent(ctx,CreateWalletAccount.class);
                    ctx.startActivity(intent);
                    dismiss();*/
                    dismiss();
                    Fragment fragment = new CreateWalletAccount();
                    FragmentTransaction ft =((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    Config.prevFragment=new InventoryPurchase();
                }
                else {
                    if(dealOftheDay.getAmount().equals("0") || dealOftheDay.getAmount().trim().equals(""))
                    {
                        Toast.makeText(ctx,"Deal not available for today",5000).show();
                        dismiss();
                        return;
                    }
                    JSUtils.shopingCartList.clear();
                    InventoryClass iv=new InventoryClass();
                    iv.setPrice(dealOftheDay.getAmount());
                    iv.setDealOfDay(dealOftheDay.getId());
                    iv.setName("Deal of day");
                    JSUtils.shopingCartList.add(iv);
                    dismiss();
                    Fragment fragment = new JSPurchaseItem();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DealItems",dealItems);
                    bundle.putString(Config.JS_Item_Purchase_Price, dealOftheDay.getAmount() + "");
                    bundle.putString(Config.JS_Item_Purchase_Name, "");
                    bundle.putInt(Config.JS_Item_Purchase_Res, R.drawable.deal_img);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft =((FragmentActivity) ctx).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();

                }

            }
        });

    }

    class MyAsynctask extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Connection connection = new Connection(ctx);
            res = connection.getDealOfDay();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                Config.getAlert(ctx, res.replace("-1", ""),"Error");
            } else {
                show();
                XMLParser xmlParser = new XMLParser();
                xmlParser.parse(res);
                dealOftheDay = xmlParser.getDealOfDay();//new ArrayList<InventoryClass>();
                populateDeal();

               try
                {
                    txtDiscount.setText("Discount : "+dealOftheDay.getDiscount()+" %");
                    float discoutPrice=Float.parseFloat(dealOftheDay.getAmount());
                    float discountPer=Float.parseFloat(dealOftheDay.getDiscount());
                    float actualP=(discoutPrice)/(1f-(discountPer/100f));



                    txtTotalPrice2.setText("Total Price : PKR "+String.format("%.2f", discoutPrice));
                    txtTotalPrice.setText(      " ( Saving PKR "+String.format("%.2f",(actualP-discoutPrice))+" )");

                    String content_price="  Actual Price : PKR "+String.format("%.2f",actualP)+"  ";
                    txtActualPrice.setText(content_price);
                    txtActualPrice.setPaintFlags(txtActualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }



            }
        }
    }

    void populateDeal() {
        LayoutInflater inflator = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = null;//inflator.inflate(R.layout.activity_, null);
        HashMap<String, String> hashMap = dealOftheDay.getAllItems();

        String[] array = JSUtils.getArray(hashMap);
        for (int i = 0; i < array.length; i = i + 2) {
            myView = inflator.inflate(R.layout.activity_dealofday_prototype, null);

            TextView txtName = myView.findViewById(R.id.txtBoosterName);
            TextView txtremain = myView.findViewById(R.id.txtremain);
            ImageView imgBooster = myView.findViewById(R.id.imgBooster);

            TextView txtName2 = myView.findViewById(R.id.txtBoosterName2);
            ImageView imgBooster2 = myView.findViewById(R.id.imgBooster2);
            TextView txtremain2 = myView.findViewById(R.id.txtremain2);
            InventoryClass ivClass=new InventoryClass();


            try {
                txtremain.setText(hashMap.get(array[i]) + "");
                txtName.setText(JSUtils.capitalize(array[i].replace("_", " ")));
                imgBooster.setImageResource(JSUtils.getItemResID(txtName.getText().toString()));
                String name=JSUtils.capitalize(array[i].replace("_", " "));
                ivClass.setName(name);
                ivClass.setItemCount(Integer.parseInt(hashMap.get(array[i])));

                dealItems.add(ivClass);

            } catch (Exception e) {

                myView.findViewById(R.id.layout1).setVisibility(View.GONE);
            }
            try {
                ivClass=new InventoryClass();
                txtremain2.setText(hashMap.get(array[i + 1]) + "");
                txtName2.setText(JSUtils.capitalize(array[i + 1].replace("_", " ")));
                imgBooster2.setImageResource(JSUtils.getItemResID(txtName2.getText().toString()));
                String name=JSUtils.capitalize(array[i+1].replace("_", " "));
                ivClass.setName(name);
                ivClass.setItemCount(Integer.parseInt(hashMap.get(array[i+1])));


                dealItems.add(ivClass);
            } catch (Exception e) {

                myView.findViewById(R.id.layout2).setVisibility(View.GONE);

            }


            layoutDealItems.addView(myView);

        }
    }
}