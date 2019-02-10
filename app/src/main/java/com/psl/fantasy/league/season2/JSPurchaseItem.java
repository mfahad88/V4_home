package com.psl.fantasy.league.season2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.psl.classes.Config;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.transport.Connection;
import com.psl.fantasy.league.season2.R;;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class JSPurchaseItem extends Fragment {

    String strItemName, strOTP, strMobNmbr, strAuthHeader, strAmount, strCharges, userID;
    TextView txtItemName, txtItemPrice, txtTxn;
    Button btnNext, btnPay;
    int itemResId;
    ImageView imgItem;
    SharedPreferences sharedPreferences;
    ArrayList<InventoryClass> dealItems;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_purchase_item, container, false);//setContentView(R.layout.activity_purchase_item);
        sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        strMobNmbr = sharedPreferences.getString(Config.JS_Mobile_Number, "");
        userID = sharedPreferences.getString(Config.USER_ID, "");
        Config.PopulateHeader(getActivity(), view.findViewById(R.id.helmet_layout));

        strAmount = getArguments().getString(Config.JS_Item_Purchase_Price);
        strItemName = getArguments().getString(Config.JS_Item_Purchase_Name);
        itemResId = getArguments().getInt(Config.JS_Item_Purchase_Res, 0);

        try {
            dealItems = (ArrayList<InventoryClass>) getArguments().getSerializable("DealItems");
        } catch (Exception e) {

        }

        txtItemName = (TextView) view.findViewById(R.id.txtItemName);
        txtItemPrice = (TextView) view.findViewById(R.id.txtItemPrice);
        txtTxn = (TextView) view.findViewById(R.id.textView7);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnPay = (Button) view.findViewById(R.id.btnPay);
        btnPay.setVisibility(View.GONE);
        imgItem = (ImageView) view.findViewById(R.id.imgItem);

        //  txtItemName.setText(strItemName);
        txtItemPrice.setText("PKR " + strAmount);
        imgItem.setImageResource(itemResId);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new MyAyncTaskPaymentINQ().execute();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyAyncTaskPayBill().execute();
            }
        });
        return view;
    }


    class MyAyncTaskPaymentINQ extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";
        String encrptedOTP;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(getActivity());
            res = conn.JSAuth();

            if (!res.startsWith("-1") && !res.trim().isEmpty()) {
                {
                    strAuthHeader = res;
                    res = conn.JSPurchaseItemINQ(strAuthHeader, strMobNmbr, strAmount);
                }

            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");

            } else {

                strCharges = res;
                Intent intent = new Intent(getActivity(), OTPActivity.class);
                intent.putExtra(Config.JS_Mobile_Number, strMobNmbr);
                intent.putExtra(Config.JS_Auth_Header, strAuthHeader);
                intent.putExtra(Config.JS_Action, 1);
                startActivityForResult(intent, OTPActivity.REQUEST_OTP);


            }


        }
    }

    class MyAyncTaskPayBill extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Connection conn = new Connection(getActivity());
            res = conn.JSPurchaseItemPayment(strAuthHeader, strMobNmbr, strAmount, strOTP, strCharges, strItemName);


            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            if (res.startsWith("-1")) {
                showAlert2(res.replace("-1", ""), "Failure");

            } else {
                if (dealItems == null) {
                    for (int i = 0; i < JSUtils.shopingCartList.size(); i++) {

                        InventoryClass ivClass = JSUtils.shopingCartList.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        String item = "";
                        switch (name.toLowerCase()) {
                            case InventoryClass.GoldenGloves:
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;

                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {

                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                } else {
                    for (int i = 0; i < dealItems.size(); i++) {
                        String item = "";
                        InventoryClass ivClass = dealItems.get(i);
                        String name = ivClass.getName();
                        int itemCount = 0;
                        switch (name.toLowerCase()) {
                            case "golden golves":
                                item = Config.USER_GOLDEN_GLOVES;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_GOLDEN_GLOVES, "0"));
                                break;


                            case InventoryClass.OrangeCap:
                                item = Config.USER_ORANGE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ORANGE_CAP, "0"));
                                break;

                            case InventoryClass.PurpleCap:
                                item = Config.USER_PURPLE_CAP;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PURPLE_CAP, "0"));
                                break;

                            case InventoryClass.SafetyCap:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                            case InventoryClass.TeamSafety:
                                item = Config.USER_TEAM_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_TEAM_SAFETY, "0"));
                                break;

                            case InventoryClass.Swaps:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Swaps10:
                                item = Config.SWAP_COUNT;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.SWAP_COUNT, "0"));
                                break;
                            case InventoryClass.Iconic:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;

                            case InventoryClass.IconPlayer:
                                item = Config.USER_ICONIC_PLAYER;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_ICONIC_PLAYER, "0"));
                                break;
                            case InventoryClass.PlayerSafety:
                                item = Config.USER_PLAYER_SAFETY;
                                itemCount = Integer.parseInt(sharedPreferences.getString(Config.USER_PLAYER_SAFETY, "0"));
                                break;
                        }

                        int itemPurchse = 0;
                        try {
                            itemPurchse = ivClass.getItemCount();
                        } catch (Exception e) {
                        }
                        sharedPreferences.edit().putString(item, String.valueOf(itemCount + itemPurchse)).commit();

                    }
                }
                JSUtils.shopingCartList.clear();
                showAlert2("You have successfully purchased item(s)" + strItemName + ".\n" + Config.JS_TransactionID + " : " + res, "Success");

                for (int i = 0; i < JSUtils.inventoryClassList.size(); i++) {
                    JSUtils.inventoryClassList.get(i).clearItemCount();
                }


            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OTPActivity.REQUEST_OTP) {

            if (resultCode == -1) {

                String message = data.getStringExtra("message");
                showAlert2(message, "Failure");
            } else if (resultCode == -2) {

                btnNext.performClick();
            } else if (resultCode == 100) {
                strOTP = data.getStringExtra(Config.JS_Encrypted_OTP);
                txtTxn.setText("Transaction Charges : " + strCharges + " Rs.");
                txtTxn.setVisibility(View.INVISIBLE);
                btnPay.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.GONE);

            }
        }
    }


    void showAlert(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    void showAlert2(String message, final String title) {
        Config.getAlert(getActivity(), message, title);

        if (title.toLowerCase().equals("success")) {

            final RadioButton radiogroup1 = getActivity().findViewById(R.id.iv_qr);
            radiogroup1.setChecked(true);
                   /* Fragment fragment=new MyInventory();

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();*/
        }


    }
}

