package com.psl.fantasy.league.season2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.psl.fantasy.league.season2.R;;
import com.psl.classes.Config;

import static android.content.Context.MODE_PRIVATE;

public class MyWallet extends Fragment implements View.OnClickListener {
    SharedPreferences sharedPreference;
    String password;
    boolean IS_ALERT_DISPLAYED = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.activity_my_wallet, container, false);
        view.findViewById(R.id.button6).setOnClickListener(this);
        view.findViewById(R.id.button7).setOnClickListener(this);
        view.findViewById(R.id.button8).setOnClickListener(this);
        view.findViewById(R.id.button9).setOnClickListener(this);
        view.findViewById(R.id.button10).setOnClickListener(this);
        sharedPreference = getActivity().getSharedPreferences(Config.SHARED_PREF, MODE_PRIVATE);
        password = sharedPreference.getString(Config.USER_KEY, "");
        Config.PopulateHeader2(getActivity(), view.findViewById(R.id.helmet_layout));
        view.findViewById(R.id.button0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Coming Soon....", Toast.LENGTH_LONG).show();
            }
        });
/*
        if(Config.WALLET_TIMEOUT)
            getAlert();*/
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.button6: //payment
                intent = new Intent(getActivity(), JSPurchaseItem.class);
                intent.putExtra(Config.JS_Item_Purchase_Price, "100");
                intent.putExtra(Config.JS_Item_Purchase_Res, R.drawable.cap);
                intent.putExtra(Config.JS_Item_Purchase_Name, "Golden Cap");


                startActivity(intent);
                break;
            case R.id.button7://bill payment
                intent = new Intent(getActivity(), BillingCompaniesActivity.class);
                startActivity(intent);
                break;
            case R.id.button8://mobile topup
                intent = new Intent(getActivity(), MobileTopupActivity.class);
                startActivity(intent);
                break;
            case R.id.button9://bal query
                intent = new Intent(getActivity(), BalanceInquiryActivity.class);
                startActivity(intent);
                break;
            case R.id.button10://bal query
                intent = new Intent(getActivity(), MiniStatementActivity.class);
                startActivity(intent);
                break;
        }
    }
//03335688851
    @Override
    public void onResume() {
        super.onResume();
        try {
            if (Config.WALLET_TIMEOUT) {
                if (!IS_ALERT_DISPLAYED) {
                    getAlert();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAlert() {
        final EditText edittext = new EditText(getActivity());
        edittext.setHint("Please enter your password");
        edittext.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        edittext.setMaxLines(1);
        edittext.setMaxEms(30);
        IS_ALERT_DISPLAYED = true;
        //edittext.setKeyListener(DigitsKeyListener.getInstance(getResources().getString(R.string.digits_to_allow_password)));

        int maxLength = 30;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        edittext.setFilters(fArray);

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(edittext)
                .setTitle("Password")
                .setCancelable(false)
                .setPositiveButton("Done", null)
                .setNegativeButton("Cancel", null)
                .create();

        // grand Button bb = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button b2 = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        String value = edittext.getText().toString();

                        if (value.trim().equalsIgnoreCase(password)) {
                            Config.WALLET_TIMEOUT = false;
                            IS_ALERT_DISPLAYED = false;
                            MainActivity.resetDisconnectTimerWallet();
                            dialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                });

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            RadioButton rb = getActivity().findViewById(R.id.iv_team);
                            rb.setChecked(true);
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

}
