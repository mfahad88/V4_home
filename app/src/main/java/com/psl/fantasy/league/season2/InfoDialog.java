package com.psl.fantasy.league.season2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.psl.fantasy.league.season2.R;;

public class InfoDialog extends  AlertDialog {


    ImageView btn_close,transfer_icon;
    ImageView btnPurchase;
    Context ctx;
    String message,title;
    TextView txtMessage;
    boolean finishAct=false;

    public InfoDialog(@NonNull Context context,String msg,String ttl) {
        super(context);
        ctx=context;
        message=msg;
        title=ttl;
    }
    public InfoDialog(@NonNull Context context,String msg,String ttl,boolean finish) {
        super(context);
        ctx=context;
        message=msg;
        title=ttl;
        finishAct=finish;
    }
    public InfoDialog(@NonNull Context context,String msg) {
        super(context);
        ctx=context;
        message=msg;
        title="";
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_dialog);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_close = (ImageView) findViewById(R.id.btn_close);
        transfer_icon=(ImageView)findViewById(R.id.transfer_icon);
        btn_close.bringToFront();
        btnPurchase = (ImageView) findViewById(R.id.btnPurchase);
        txtMessage=(TextView)findViewById(R.id.txtMessage) ;
        txtMessage.setText(message);

        if(title.toLowerCase().equals("info"))
        {
            transfer_icon.setImageResource(R.drawable.imark);
        }
      else  if(title.toLowerCase().equals("success"))
        {
            transfer_icon.setImageResource(R.drawable.tick_big);
        }
        else  transfer_icon.setImageResource(R.drawable.cross_bg);

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

    @Override
    public void dismiss() {
        super.dismiss();
        if(finishAct)
            ((Activity) ctx).finish();
    }
}