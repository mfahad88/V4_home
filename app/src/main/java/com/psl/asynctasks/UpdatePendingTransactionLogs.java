package com.psl.asynctasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.psl.classes.DatabaseHandler;
import com.psl.classes.Transaction_Details;
import com.psl.transport.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aamir.shehzad on 2/28/2018.
 */

public class UpdatePendingTransactionLogs extends AsyncTask<Void,Void,Void> {

    Context mcContext;
    List<Transaction_Details> mTransactionDetails;
    DatabaseHandler dbHandler;
    ProgressDialog mproProgressDialog;
    ArrayList<String> distinctTrID;
   public UpdatePendingTransactionLogs(Context context)
    {
        mcContext=context;
        mproProgressDialog=new ProgressDialog(mcContext);
        distinctTrID=new ArrayList<String>();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbHandler=new DatabaseHandler(mcContext);
        mproProgressDialog.setMessage("Loading...");
        mproProgressDialog.setCancelable(false);
        mproProgressDialog.show();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mproProgressDialog.dismiss();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        try{

            mTransactionDetails=dbHandler.getTransactionDetails();
            if(mTransactionDetails !=null && mTransactionDetails.size()>0) {
                for (int i = 0; i < mTransactionDetails.size(); i++) {
                    if (!distinctTrID.contains(mTransactionDetails.get(i).getTransaction_id())) {
                        distinctTrID.add(mTransactionDetails.get(i).getTransaction_id());

                    }
                }

                String prevTransaction="";
                for(int i=0;i<distinctTrID.size();i++) {

                    prevTransaction=distinctTrID.get(i);
                    List<Transaction_Details> tempDetail=new ArrayList<Transaction_Details>();
                    for (int j=0;j<mTransactionDetails.size();j++) {
                        Transaction_Details trDetails = mTransactionDetails.get(j);
                        if (prevTransaction.equals(trDetails.getTransaction_id())) {
                            tempDetail.add(trDetails);
                        }
                    }
                        if (tempDetail.size() > 0) {
                            Connection connection = new Connection(mcContext);
                            connection.UpdateTransactionNew(tempDetail);
                        }

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
