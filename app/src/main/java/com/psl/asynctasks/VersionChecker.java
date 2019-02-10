package com.psl.asynctasks;

/**
 * Created by aamir.shehzad on 2/28/2018.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import org.jsoup.Jsoup;
public class VersionChecker extends AsyncTask<Void, String, String> {
    String newVersion;
    Context mContext;

    public VersionChecker(Context ctx)
    {
        mContext=ctx;
    }
ProgressDialog progressDialog;
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        progressDialog=new ProgressDialog(mContext);
       // progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        String newVersion = null;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.psl.fantasy.league" + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newVersion;
    }
    /*@Override
    protected String doInBackground(Void... params)
    {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.psl.fantasy.league" + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first()
                    .ownText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newVersion;
    }*/


}
