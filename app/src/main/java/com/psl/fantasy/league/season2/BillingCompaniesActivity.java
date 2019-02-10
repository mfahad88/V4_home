package com.psl.fantasy.league.season2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.psl.fantasy.league.season2.R;;
public class BillingCompaniesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_companies);


    }

  public void bill_cmp_click(View v)
    {
        Intent intent=new Intent(BillingCompaniesActivity.this,BillPaymentActivity.class);
        intent.putExtra("ID",v.getId());
        startActivity(intent);
        finish();
    }
}
