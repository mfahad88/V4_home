package com.psl.fantasy.league.season2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.fantasy.league.season2.R;;


public class ShopingCart extends Fragment {

    SharedPreferences sharedPreferences;
  //  List<InventoryClass> mShopingCart;
    LinearLayout layoutBoosters,editLayout,okCancelLayout;
    TextView txtTotalPrice;
    int totalPrice=0;
    Button btneditcart,btnpurchase,btnOK,btnCancel;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sharedPreferences=getActivity().getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        View view =  inflater.inflate(R.layout.activity_shoping_cart, container, false);
        try{view.findViewById(R.id.textView11).bringToFront();} catch (Exception e){}

        Config.PopulateHeader(getActivity(),view.findViewById(R.id.helmet_layout));
        layoutBoosters=view.findViewById(R.id.layoutBooster);
        editLayout=view.findViewById(R.id.editLayout);
        okCancelLayout=view.findViewById(R.id.okCancelLayout);

        txtTotalPrice=view.findViewById(R.id.txtTotalPrice);
        btneditcart=view.findViewById(R.id.button4);
        btnpurchase=view.findViewById(R.id.button5);
        btnOK=view.findViewById(R.id.btn_ok);
        btnCancel=view.findViewById(R.id.btn_cancel);


        editLayout.setVisibility(View.VISIBLE);
        okCancelLayout.setVisibility(View.GONE);
       // mShopingCart=new ArrayList<>();
       // mShopingCart.addAll(JSUtils.shopingCartList);
       // JSUtils.copyArrays(mShopingCart,JSUtils.shopingCartList);
        populateInventory(true);
        txtTotalPrice.setText("PKR "+totalPrice);
        btneditcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.GONE);
                okCancelLayout.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                populateInventory(true);
            }
        });

        btnpurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalPrice==0)
                {
                    Toast.makeText(getActivity(), "Shopping cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean walletAccount=sharedPreferences.getBoolean(Config.JS_WALLET_ACCOUNT,false);
                if(!walletAccount)
                {
                        /*Intent intent=new Intent(getActivity(),CreateWalletAccount.class);
                        startActivity(intent);*/
                        Fragment fragment = new CreateWalletAccount();
                        Config.prevFragment=new ShopingCart();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();


                }
                else {
                    Fragment fragment = new JSPurchaseItem();
                    Bundle bundle = new Bundle();
                    bundle.putString(Config.JS_Item_Purchase_Price, totalPrice + "");
                    bundle.putString(Config.JS_Item_Purchase_Name, "");
                    bundle.putInt(Config.JS_Item_Purchase_Res, R.drawable.cart_total_icon);
                    fragment.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.VISIBLE);
                okCancelLayout.setVisibility(View.GONE);
            //    JSUtils.shopingCartList.clear();

          //      JSUtils.shopingCartList.addAll(mShopingCart);
               // JSUtils.copyArrays(JSUtils.shopingCartList,mShopingCart);
                populateInventory(false);
                if(JSUtils.shopingCartList.size()==0)
                {
                    Fragment fragment = new InventoryPurchase();
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                    //okCancelLayout.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Shopping cart is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLayout.setVisibility(View.VISIBLE);
                okCancelLayout.setVisibility(View.GONE);
//                mShopingCart.clear();
  //              mShopingCart.addAll(JSUtils.shopingCartList);
               // JSUtils.copyArrays(mShopingCart,JSUtils.shopingCartList);
                populateInventory(false);
            }
        });
        return view;
    }


    void populateInventory(boolean editMode)
    {
        if(JSUtils.shopingCartList.size()==0) {
            Toast.makeText(getActivity(), "Shopping cart is empty", Toast.LENGTH_SHORT).show();
            Fragment fragment = new InventoryPurchase();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            return;
        }
        layoutBoosters.removeAllViews();
        totalPrice=0;
        txtTotalPrice.setText("PKR "+totalPrice);

        LayoutInflater inflator = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = null;//inflator.inflate(R.layout.activity_, null);

        for (int i = 0; i < JSUtils.shopingCartList.size(); i++) {
            myView = inflator.inflate(R.layout.activity_cart_prototype, null);


            final TextView txtQty = myView.findViewById(R.id.textView15);
            TextView txtName = myView.findViewById(R.id.txtBoosterType);
            TextView txtPrice = myView.findViewById(R.id.txtItemPrice);
            ImageView imgBooster = myView.findViewById(R.id.imageView14);

            ImageView upbtn=myView.findViewById(R.id.btnUp);
            ImageView downbtn=myView.findViewById(R.id.btnDown);
            ImageView delBtn=myView.findViewById(R.id.btnDelItem);

            if(editMode)
            {
                upbtn.setVisibility(View.VISIBLE);
                downbtn.setVisibility(View.VISIBLE);
                delBtn.setVisibility(View.VISIBLE);
            }
            else {
                upbtn.setVisibility(View.GONE);
                downbtn.setVisibility(View.GONE);
                delBtn.setVisibility(View.GONE);
            }



            final InventoryClass ivClass=JSUtils.shopingCartList.get(i);
            txtName.setText(ivClass.getName());
            txtPrice.setText("PKR "+ivClass.getPrice());
            txtQty.setText(ivClass.getItemCount()+"");
            imgBooster.setImageResource(ivClass.getImage());
            try {
                totalPrice += Integer.parseInt(ivClass.getPrice()) * ivClass.getItemCount();
            }
            catch (Exception e)
            {

            }

            final int pos=i;
            upbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            int val=ivClass.getItemCount();//Integer.parseInt(txtQty.getText().toString());
                    if(val>0)
                    {
                        ivClass.setItemCount(-1);
                       if(ivClass.getItemCount()==0)
                       {
                           JSUtils.shopingCartList.remove(pos);
                       }
                        populateInventory(true);
                    }

                }
            });
            downbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int val = ivClass.getItemCount();//Integer.parseInt(txtQty.getText().toString());
                    ivClass.setItemCount(1);
                    populateInventory(true);
                }
            });
             delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSUtils.shopingCartList.remove(pos);
                    populateInventory(true);
                    btnCancel.setVisibility(View.VISIBLE);

                }});
            txtTotalPrice.setText("PKR "+totalPrice);

            layoutBoosters.addView(myView);
        }
    }
}
