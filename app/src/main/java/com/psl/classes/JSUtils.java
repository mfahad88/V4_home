package com.psl.classes;

import com.psl.fantasy.league.season2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aamir.shehzad on 1/13/2018.
 */

public class JSUtils {

    public  static List<InventoryClass> shopingCartList=new ArrayList<InventoryClass>();
    public  static ArrayList<Transaction_Details> Transaction_Details_List=new ArrayList<Transaction_Details>();
    public static List<InventoryClass> inventoryClassList = new ArrayList<InventoryClass>();
    public static List<NotificationClass> notificationList = new ArrayList<NotificationClass>();
    public static HashMap<String,String> get_biller_company_info()
   {


        HashMap<String,String> biller_company_info=new HashMap<>();

       biller_company_info.put( " Select Company     " , "0");
       biller_company_info.put( "Qubee Consumer Bill" , "QUBEE001")                ;
       biller_company_info.put( "Qubee Distributor Bill" , "QUBEE002")             ;
       biller_company_info.put( "PTCL Landline Bill" , "PTCL0010")                 ;
       biller_company_info.put( "SSGC Bill" , "SSGC0001")                          ;
       biller_company_info.put( "SNGPL Bill" , "SNGPL001")                         ;
       biller_company_info.put( "LESCO Bill" , "LESCO001")                         ;
       biller_company_info.put( "HESCO Bill" , "HESCO001")                         ;
       biller_company_info.put( "GEPCO Bill" , "GEPCO001")                         ;
       biller_company_info.put( "IESCO Bill" , "IESCO")                            ;
       biller_company_info.put( "KESC Bill" , "KESC0001")                          ;
       biller_company_info.put( "MEPCO Bill" , "MEPCO001")                         ;
       biller_company_info.put( "FESCO Bill" , "FESCO")                            ;
       biller_company_info.put( "PESCO Bill" , "PESCO")                            ;
       biller_company_info.put( "QESCO Bill" , "QESCO")                            ;
       biller_company_info.put( "TESCO Bill" , "TESCO001")                         ;
       biller_company_info.put( "SEPCO Bill" , "SEPCO")                            ;
       biller_company_info.put( "KWSB Bill" , "KWSB")                              ;
       biller_company_info.put( "LWASA Bill" , "LWASA")                            ;
       biller_company_info.put( "RWASA Bill" , "RWASA")                            ;
       biller_company_info.put( "MWASA Bill" , "MWASA")                            ;
       biller_company_info.put( "BWASA Bill" , "BWASA")                            ;
       biller_company_info.put( "FWASA Bill" , "FWASA")                            ;
       biller_company_info.put( "GWASA Bill" , "GWASA")                            ;
       biller_company_info.put( "SCO Bill" , "SCO")                                ;
       biller_company_info.put( "CDGK Bill" , "CDGK")                              ;
       biller_company_info.put( "Vfone Bill" , "PTCL0015")                         ;
       biller_company_info.put( "Wateen Internet Bill" , "WATEEN01")               ;
       biller_company_info.put( "WiTribe Bill" , "WTRIBE01")                       ;
       biller_company_info.put( "PTCL EVO Prepaid Bill" , "PTCL0011")              ;
       biller_company_info.put( "PTCL EVO Postpaid Bill" , "PTCL0012")             ;
       biller_company_info.put( "PTCL Defaulter Bill" , "PTCL0013")                ;



       return  biller_company_info;
   }

    public static HashMap<String,String> get_Mobile_Companies_info()
    {
        HashMap<String,String> biller_company_info=new HashMap<>();
        biller_company_info.put( " Select Company     " , "0");
        biller_company_info.put( "Mobilink Prepaid Bill" , "MBLINK01");
        biller_company_info.put( "Mobilink Postpaid Bill" , "MBLINK02");
        biller_company_info.put( "Zong Prepaid Bill" , "ZONG0001");
        biller_company_info.put( "Zong Postpaid Bill" , "ZONG0002");
        biller_company_info.put( "Warid Prepaid Bill" , "WARID001");
        biller_company_info.put( "Warid Postpaid Bill" , "WARID002");
        biller_company_info.put( "Ufone Prepaid Bill" , "UFONE001");
        biller_company_info.put( "Ufone Postpaid Bill" , "UFONE002");
        biller_company_info.put( "Telenor Prepaid Bill" , "TELNOR01");
        biller_company_info.put( "Telenor Postpaid Bill" , "TELNOR02");
        return  biller_company_info;

    }

    public static HashMap<String,String> get_Telecom_Companies_info()
    {
        HashMap<String,String> biller_company_info=new HashMap<>();
        biller_company_info.put( "SCO Bill" , "SCO")                                ;
        biller_company_info.put( "CDGK Bill" , "CDGK")                              ;
        biller_company_info.put( "Vfone Bill" , "PTCL0015")                         ;
        biller_company_info.put( "Wateen Internet Bill" , "WATEEN01")               ;
        biller_company_info.put( "WiTribe Bill" , "WTRIBE01")                       ;
        biller_company_info.put( "PTCL EVO Prepaid Bill" , "PTCL0011")              ;
        biller_company_info.put( "PTCL EVO Postpaid Bill" , "PTCL0012")             ;
        biller_company_info.put( "PTCL Defaulter Bill" , "PTCL0013")                ;
        return  biller_company_info;

    }

    public static HashMap<String,String> get_Electric_Companies_info() {
        HashMap<String, String> biller_company_info = new HashMap<>();
        biller_company_info.put( "LESCO Bill" , "LESCO001") ;
        biller_company_info.put( "HESCO Bill" , "HESCO001") ;
        biller_company_info.put( "GEPCO Bill" , "GEPCO001") ;
        biller_company_info.put( "IESCO Bill" , "IESCO")    ;
        biller_company_info.put( "KESC Bill" , "KESC0001")  ;
        biller_company_info.put( "MEPCO Bill" , "MEPCO001") ;
        biller_company_info.put( "FESCO Bill" , "FESCO")    ;
        biller_company_info.put( "PESCO Bill" , "PESCO")    ;
        biller_company_info.put( "QESCO Bill" , "QESCO")    ;
        biller_company_info.put( "TESCO Bill" , "TESCO001") ;
        biller_company_info.put( "SEPCO Bill" , "SEPCO")    ;
        return biller_company_info;
    }


    public static HashMap<String,String> get_GAS_Companies_info() {
        HashMap<String, String> biller_company_info = new HashMap<>();
        biller_company_info.put( "SSGC Bill" , "SSGC0001")                          ;
        biller_company_info.put( "SNGPL Bill" , "SNGPL001")                         ;

        return biller_company_info;
    }
    public static HashMap<String,String> get_Water_Companies_info() {
        HashMap<String, String> biller_company_info = new HashMap<>();
        biller_company_info.put("KWSB Bill", "KWSB");
        biller_company_info.put("LWASA Bill", "LWASA");
        biller_company_info.put("RWASA Bill", "RWASA");
        biller_company_info.put("MWASA Bill", "MWASA");
        biller_company_info.put("BWASA Bill", "BWASA");
        biller_company_info.put("FWASA Bill", "FWASA");
        biller_company_info.put("GWASA Bill", "GWASA");
        return biller_company_info;
    }
        public  static String[] getArray(HashMap<String,String> hMap) {
       String[] list = new String[hMap.size()];//new ArrayList<String>();
        int i=0;
       for (String val : hMap.keySet()) {

           list[i]=val;
           i++;
       }

       return list;
   }

  public static int getItemResID(String name)
   {
       switch (name.toLowerCase())
       {
           case InventoryClass.GoldenGloves:
               return R.drawable.iv_gg_small;

           case InventoryClass.GoldenGolves:
               return R.drawable.iv_gg_small;

           case InventoryClass.OrangeCap:
               return R.drawable.iv_oc_small;


           case InventoryClass.PurpleCap:
               return R.drawable.iv_pp_small;


           case InventoryClass.SafetyCap:
               return R.drawable.iv_ps_small;

           case InventoryClass.TeamSafety:
               return R.drawable.iv_ts_small;


           case InventoryClass.Swaps:
               return R.drawable.iv_small_treansfers;



           case InventoryClass.Swaps10:
               return R.drawable.iv_small_treansfers;

           case InventoryClass.Iconic:
               return R.drawable.iv_ip_small;


           case InventoryClass.IconPlayer:
               return R.drawable.iv_ip_small;

           case InventoryClass.PlayerSafety:
               return R.drawable.iv_ps_small;

       }

       return 0;

   }
    public static String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }

public static void copyArrays(List<InventoryClass> dest, List<InventoryClass> src)
{
    dest.clear();
    for (int i=0;i<src.size();i++) {
        InventoryClass item=new InventoryClass();
        item= src.get(i);
        dest.add(item);
    }
}
}
