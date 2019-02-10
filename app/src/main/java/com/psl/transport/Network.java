package com.psl.transport;

/**
 * Created by aamir.shehzad on 6/12/2017.
 */

public class Network{
   /*static java.net.URL url = new java.net.URL("http://combopk99.somee.com/WebSite10/GPS_Tracker_Service.asmx/");
    public static String SaveProfile(String name,String email,String pic,String uuid,String fbid){
        HttpURLConnection urlConnection = null;
        String result="Request not completed";

            try {
           // url+="CreateUser?name="+name+"";
         //   http://172.16.91.112:880/GPS/users.php?name=waheed%20mus&email=&picture=asdasdasdadasdasddsadasd&uuid=asdasd12e112312312313213&fbid=12312&action=update&userid=32
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder r = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                r.append(line);
            }

            result=r.toString();
        }
        catch(Exception e){
            if(e.getClass().equals(new UnknownHostException().getClass()))
            {
                result="You are not connected to internet";
            }
            else if(e.getClass().equals(new FileNotFoundException().getClass()))
            {
                result="An error occured while booking the order. Please go back to home screen and try again.";
            }

        }
        finally {
            if( urlConnection!=null)
                urlConnection.disconnect();
        }
        return result;
    }*/

}
