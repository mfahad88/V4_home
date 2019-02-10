package com.psl.transport;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.psl.classes.Config;
import com.psl.classes.DatabaseHandler;
import com.psl.classes.InventoryClass;
import com.psl.classes.JSUtils;
import com.psl.classes.PlayerAttributes;
import com.psl.classes.Transaction_Details;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Connection {

	public  String Soap_Action="";
	public  final static String NameSpace="http://tempuri.org/";
	private SoapObject object,result;
	private SoapSerializationEnvelope envelop;
	public  boolean isValidUser= true;
	String name ;
	String userNameHeader ;
	String passwordHeader ;
	static boolean isAvail;
	static String URL ;
	Context myContext;
	String res="";
	String w_username ="";
	String w_password ="";
	String methodName;
	String userID;
	//static String URL = "https://42.83.84.170/AMS_Service1.asmx" ;
	SharedPreferences sharedPreferences;
	public Connection(Context ctx){
		URL = Config.w0+"MyService.asmx";
		myContext=ctx;
		sharedPreferences=ctx.getSharedPreferences(Config.SHARED_PREF,Context.MODE_PRIVATE);
		userID=sharedPreferences.getString(Config.USER_ID,"");
		w_username=Config.w1;
		w_password=Config.w2;
	}


	public Connection(String method, Context ctx){
		//SharedPreferences sp=ctx.getSharedPreferences(Constant.title, ctx.MODE_WORLD_READABLE);
		URL= Config.w0+"suoicodilaipxecitsiligarfilacrepus_jd_sam_danny_timmy.asmx";
//		URL="http://172.28.28.19:50144/suoicodilaipxecitsiligarfilacrepus_jd_sam_danny_timmy.asmx";
		methodName=method;
		Soap_Action=NameSpace+methodName;
		object=new SoapObject(NameSpace, methodName);
		myContext=ctx;
		envelop=new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelop.dotNet=true;

		//envelop.headerOut = new Element[1];
		//	envelop.headerOut[0] =  buildAuthHeader();

	}

	public String CreateUser(String u_name,String password,String fullname,String email,String contact,String picture,String cnic,String jswalletno, String firstname, String lastname, String gender, String age, String register_via) {
		String result="";
		try {
			String MethodName = "userRegistration";
			SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
			soapRequest.addProperty("w_username", w_username);
			soapRequest.addProperty("w_password", w_password);
			soapRequest.addProperty("firstname", firstname);
			soapRequest.addProperty("lastname", lastname);
			soapRequest.addProperty("gender", gender);
			soapRequest.addProperty("age", age);
			soapRequest.addProperty("username", email);
		//	soapRequest.addProperty("fullname", fullname);
			soapRequest.addProperty("email", email);
			soapRequest.addProperty("contact", contact);
			soapRequest.addProperty("cnic", cnic);
			soapRequest.addProperty("picurl", "");
			soapRequest.addProperty("jswalletno", jswalletno);
			soapRequest.addProperty("password", password);
			soapRequest.addProperty("registered_via", register_via);
			soapRequest.addProperty("operating_system", "Android");


			result=getResult(soapRequest,MethodName);
		}catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
	public String UpdateGUID(String user_id,String guid,String package_name,String app_version) {
		String result="";

		String MethodName = "update_guid";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("user_id", user_id);
		soapRequest.addProperty("guid", guid);
		soapRequest.addProperty("package_name", package_name);
		soapRequest.addProperty("app_version", app_version);


		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String UpdateUserProfile(String u_name,String fullname,String contact,String picture,String cnic,String jswalletno) {
		String result="";

		String MethodName = "updateUserProfile";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", w_username);
		soapRequest.addProperty("w_password", w_password);
		soapRequest.addProperty("username", u_name);
		soapRequest.addProperty("fullname", fullname);

		soapRequest.addProperty("contact", contact);
		soapRequest.addProperty("cnic", cnic);
		soapRequest.addProperty("picurl", picture);
		soapRequest.addProperty("jswalletno", jswalletno);



		result=getResult(soapRequest,MethodName);
		return result;
	}


	public String VerifyUser(String email,String password) {
		String result="";

		String MethodName = "Login";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", w_username);
		soapRequest.addProperty("w_password", w_password);
		soapRequest.addProperty("username", email);
		soapRequest.addProperty("password", password);
		soapRequest.addProperty("operatingSystem", "Android");
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String getJsLocators(String lati,String longi) {
		String result="";

		String MethodName = "get_agents";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", w_username);
		soapRequest.addProperty("w_password", w_password);
		soapRequest.addProperty("lat", lati);
		soapRequest.addProperty("lng", longi);
		soapRequest.addProperty("dist", "5");
		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String checkdate(String duedate) {
		String result="";

		String MethodName = "checkdate";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", w_username);
		soapRequest.addProperty("w_password", w_password);
		soapRequest.addProperty("duedate", duedate);

		result=getResult(soapRequest,MethodName);
		return result;
	}

///////////////////////////////////////////////// JS METHODS/////////////////////////////////////////////////////////////////


	public String JSAuth()
	{
		String authToken = Config.w3 + ":" + Config.w4;
		authToken = Base64.encodeToString(authToken.getBytes(), Base64.DEFAULT);
		String authHead="Basic " + authToken;
		HttpURLConnection connection = null;
		try {
			String html = "";

//		Config.w5+"?grant_type=client_credentials"
			java.net.URL url = new URI(Config.w5 + "/oauth/v1/generate?grant_type=client_credentials").toURL();


			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Authorization", authHead);
			connection.connect();

			StringBuilder stringBuilder = new StringBuilder();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStreamReader streamReader = new InputStreamReader(
						connection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(
						streamReader);
				String response = null;
				while ((response = bufferedReader.readLine()) != null) {
					stringBuilder.append(response + "\n");
				}
				bufferedReader.close();
				Log.d("Result Value ", stringBuilder.toString());

				JSONObject jsonObject = new JSONObject(stringBuilder.toString());
				//  JSONArray jsonArray = jsonObject.get("access_token");
				String token_num=(String) jsonObject.get("Token");
				return "Bearer "+token_num;//;stringBuilder.toString();
			} else {
				return "-1 " + connection.getResponseMessage();

			}
		}
		catch (Exception e) {

			e.printStackTrace();
			if (e.getClass().equals(new ConnectException().getClass()) || e.getClass().equals(new UnknownHostException().getClass())) {
				res = "-1Please check your internet connection and try again.";
			} else if (e.getClass().equals(new SocketTimeoutException().getClass())) {
				/*retry++;
				if (retry <= 3) {
					return JSAuth();
				} else {
					retry = 0;*/
					res = "-1Request timeout. Please try again.";
			//	}
			} else
				res = "-1An error occured. Please try again";
		}
			finally {
			if (connection != null) {
				connection.disconnect();
			}

		}
		return  res;
	}

	public String JSVerifyAccount(String authHead,String CNIC,String mobNumber,String tType)
	{
		try {
			java.net.URL url = new URI(Config.w5 + "/mb/verifyaccount/v1/request").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"Cnic\":\"" + CNIC + "\"," +
							"\"MobileNumber\":\"" + mobNumber+ "\"," +
							"\"TransactionType\":\"" + tType +
							"\"}");
			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				res=(String)jsonObj.get("ResponseCode");
				return "success";
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			return "-1An error occured. Please try again";
		}
	}

	public String JSBalanceInquiry(String authHead,String mobNumber,String otp)
	{
		String strJSONBody="{\"OtpPin\":\"" + otp + "\"," +
				"\"MobileNumber\":\"" + mobNumber +
				"\"}";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strJSONBody
				);
		try {
			java.net.URL url = new URI(Config.w5 + "/balance/v1/inquiry").toURL();
			HashMap<String, String> params=new HashMap<>();

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1")) {
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Balance Inquiry",mobNumber,strJSONBody,res.replace("-1",""),"Fail","");
				UpdateTransactionNew(transaction_detailses);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Balance Inquiry",mobNumber,strJSONBody,res,"SUCCESS","");
				UpdateTransactionNew(transaction_detailses);
				res=(String)jsonObj.get("Balance");
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{

				res="-1"+(String)jsonObj.get("errorMessage");
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Balance Inquiry",mobNumber,strJSONBody,res,"Fail","");
				UpdateTransactionNew(transaction_detailses);
				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Balance Inquiry",mobNumber,strJSONBody,"Exception : "+e.getMessage(),"Fail","");
			UpdateTransactionNew(transaction_detailses);
			return "-1An error occured. Please try again";

		}
	}
	public String JSMiniStatement(String authHead,String mobNumber,String otp)
	{
		String strJSONBody=	"{\"OtpPin\":\"" + otp + "\"," +
				"\"MobileNumber\":\"" + mobNumber +
				"\"}";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strJSONBody
			);

		try {
			java.net.URL url = new URI(Config.w5 + "/ministatement/v0/inquiry").toURL();
			HashMap<String, String> params=new HashMap<>();
			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate Mini Statement. Please try again";
			if(res.startsWith("-1")) {
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Mini Statement",mobNumber,strJSONBody,res.replace("-1",""),"Fail","");
				UpdateTransactionNew(transaction_detailses);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Mini Statement",mobNumber,strJSONBody,res,"SUCCESS","");
				UpdateTransactionNew(transaction_detailses);
				//res=(String)jsonObj.get("Balance");
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Mini Statement",mobNumber,strJSONBody,res.replace("-1",""),"Fail","");
				UpdateTransactionNew(transaction_detailses);
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detailses = prepareTransList("0","0","Mini Statement",mobNumber,strJSONBody,"Exception : "+e.getMessage(),"Fail","");
			UpdateTransactionNew(transaction_detailses);
			return "-1An error occured. Please try again";
		}
	}
	public String JSGenerateOTP(String authHead,String mobNumber,String OtpPurpose)
	{
		try {
			java.net.URL url = new URI(Config.w5 + "/otp/v1/generate").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"OtpPurpose\":\"" + OtpPurpose + "\"," +
							"\"MobileNumber\":\"" + mobNumber +
							"\"}");
			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				res=(String)jsonObj.get("ResponseCode");
				return "success";
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			return "-1An error occured. Please try again";

		}
	}
	public String JSVerifyOTP(String authHead,String cnic,String mobNumber,String Otp)
	{
		try {
			java.net.URL url = new URI(Config.w5 + "/mb/otp/v1/verify").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"MobileNumber\":\"" + mobNumber + "\"," +
						"\"Cnic\":\"" + cnic + "\"," +
						"\"Otp\":\"" + Otp +
						"\"}");
			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				res=(String)jsonObj.get("ResponseCode");
				return "success";
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;
		}
		catch(Exception e)
		{
			return "-1An error occured. Please try again";
		}
	}


	public String JSBillPaymentInquiry(String authHead,String mobNumber,String amount,String Productid,String consumerNo)
	{

		try {
			java.net.URL url = new URI(Config.w5 + "/bill/v1/inquiry").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"MobileNumber\":\"" + mobNumber + "\"," +
							"\"Amount\":\"" + amount+ "\"," +
							"\"ProductId\":\"" + Productid+ "\"," +
							"\"ConsumerNo\":\"" + consumerNo +
							"\"}");

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				//res=(String)jsonObj.get("Charges");
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			Log.e(this.getClass().getName()+"---->",e.getMessage());
			e.printStackTrace();
			return "-1An error occured. Please try again";

		}
	}
	public String JSBillPayment(String authHead,String mobNumber,String amount,String Productid,String consumerNo,String otp,String biller)
	{
		amount=amount.replace(",","");

		String strJSONBody="{\"MobileNumber\":\"" + mobNumber + "\"," +
				"\"Amount\":\"" + amount+ "\"," +
				"\"ProductId\":\"" + Productid+ "\"," +
				"\"ConsumerNo\":\"" + consumerNo+ "\"," +
				"\"OtpPin\":\"" + otp +
				"\"}";
		MediaType mediaType = MediaType.parse("application/json");

		RequestBody body = RequestBody.create(mediaType,strJSONBody);

		try {
			java.net.URL url = new URI(Config.w5 + "/bill/v1/payment").toURL();
			HashMap<String, String> params=new HashMap<>();

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals("")) {
				return "-1Fail to Pay Bill. Please try again";
			}
			if(res.startsWith("-1"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Bill Payment",mobNumber,strJSONBody,res.replace("-1",""),"Fail",biller);
				UpdateTransactionNew(transaction_detailses);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				String response=res;
				res=(String)jsonObj.get("TransactionCode");
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Bill Payment",mobNumber,strJSONBody,response,"SUCCESS",biller);
				UpdateTransactionNew(transaction_detailses);
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Bill Payment",mobNumber,strJSONBody,res.replace("-1",""),"Fail",biller);
				UpdateTransactionNew(transaction_detailses);
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Bill Payment",mobNumber,strJSONBody,"Exception : "+e.getMessage(),"Fail",biller);
			UpdateTransactionNew(transaction_detailses);

			return "-1An error occured. Please try again";

		}
	}



	public String JSTopupInquiry(String authHead,String mobNumber,String amount,String Productid,String consumerNo)
	{
		try {
			java.net.URL url = new URI(Config.w5 + "/topup/v1/inquiry").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"MobileNumber\":\"" + mobNumber + "\"," +
							"\"Amount\":\"" + amount+ "\"," +
							"\"ProductId\":\"" + Productid+ "\"," +
							"\"ConsumerNo\":\"" + consumerNo +
							"\"}");

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to generate OTP. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				//res=(String)jsonObj.get("Charges");
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			return "-1An error occured. Please try again";

		}
	}
	public String JSTopupPayment(String authHead,String mobNumber,String amount,String Productid,String consumerNo,String otp,String biller)
	{
		String strJSONBody="{\"MobileNumber\":\"" + mobNumber + "\"," +
				"\"Amount\":\"" + amount+ "\"," +
				"\"ProductId\":\"" + Productid+ "\"," +
				"\"ConsumerNo\":\"" + consumerNo+ "\"," +
				"\"OtpPin\":\"" + otp +
				"\"}";
		MediaType mediaType = MediaType.parse("application/json");

		RequestBody body = RequestBody.create(mediaType,strJSONBody
		);

		try {
			java.net.URL url = new URI(Config.w5 + "/topup/v1/payment").toURL();
			HashMap<String, String> params=new HashMap<>();

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals("")) {
				return "-1Fail to Pay Bill. Please try again";
			}
			if(res.startsWith("-1"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Mobile Topup",mobNumber,strJSONBody,res.replace("-1",""),"Fail",biller);

				UpdateTransactionNew(transaction_detailses);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				String response=res;
				res=(String)jsonObj.get("TransactionCode");
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Mobile Topup",mobNumber,strJSONBody,response,"SUCCESS",biller);
				UpdateTransactionNew(transaction_detailses);

				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Mobile Topup",mobNumber,strJSONBody,res.replace("-1",""),"Fail",biller);
				UpdateTransactionNew(transaction_detailses);
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detailses = prepareTransList(res,amount,"Bill Payment",mobNumber,strJSONBody,"Exception : "+e.getMessage(),"Fail",biller);
			UpdateTransactionNew(transaction_detailses);
			return "-1An error occured. Please try again";
		}
	}
	public String JSPurchaseItemINQ(String authHead,String mobNumber,String amount)
	{
		try {
			java.net.URL url = new URI(Config.w5 + "/payment/v1/inquiry").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,
					"{\"MobileNumber\":\"" + mobNumber + "\"," +
							"\"Amount\":\"" + amount +
							"\"}");

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to Pay Bill. Please try again";
			if(res.startsWith("-1"))
				return res;
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				res=(String)jsonObj.get("Charges");
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			return "-1An error occured. Please try again";

		}
	}
	public String JSPurchaseItemPayment(String authHead,String mobNumber,String amount,String otp,String charges,String itemName)
	{
		String strJSONBody=	"{\"MobileNumber\":\"" + mobNumber + "\"," +
				"\"Amount\":\"" + amount+ "\"," +
				"\"MPIN\":\"" + otp+ "\"," +
				"\"Charges\":\"" + charges +
				"\"}";
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType,strJSONBody);

		try {
			java.net.URL url = new URI(Config.w5 + "/payment/v1/request").toURL();
			HashMap<String, String> params=new HashMap<>();

			String res=getResultHTTP(authHead,url,body);
		//	String res="{\"ResponseCode\":\" 00 \"," +"\"TransactionCode\":\"" + 100 +"\"}";
			if(res.trim().equals(""))
				return "-1Fail to Pay Bill. Please try again";
			if(res.startsWith("-1")) {
				List<Transaction_Details> transaction_detail =
						prepareTransListPurchaseItem(res,mobNumber,userID,strJSONBody,res.replace("-1",""),"Fail");
				String test=UpdateTransactionNew(transaction_detail);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{
				String response=res;
				res=(String)jsonObj.get("TransactionCode");
				List<Transaction_Details> transaction_detail =
						prepareTransListPurchaseItem(res,mobNumber,userID,strJSONBody,res.replace("-1",""),"SUCCESS");
				String test=UpdateTransactionNew(transaction_detail);
				return res;
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransListPurchaseItem("0",mobNumber,userID,strJSONBody,res,"Fail");

				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				List<Transaction_Details> transaction_detail =
						prepareTransListPurchaseItem(res,mobNumber,userID,strJSONBody,res.replace("-1",""),"Fail");
				String test=UpdateTransactionNew(transaction_detail);
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detail =
					prepareTransListPurchaseItem(res,mobNumber,userID,strJSONBody,res.replace("-1",""),"Fail");
			String test=UpdateTransactionNew(transaction_detail);
			return "-1An error occured. Please try again";

		}
	}



///////////////////////////////////////////////////////Transaction Arrays////////////////////////////////////////



	public List<Transaction_Details> prepareTransList(String tr_id, String amount, String type, String mobNumber, String strJSONBody
			, String response, String DESCRIPTION, String biller)

	{

		List<Transaction_Details> transaction_detailsList = new ArrayList<>();

			Transaction_Details transaction_details = new Transaction_Details();


			transaction_details.setTransaction_id(tr_id);
			transaction_details.setAmount(amount);
			transaction_details.setType(type);
			transaction_details.setJswallet(mobNumber);
			transaction_details.setUser_id(userID);
			transaction_details.setRequest(strJSONBody);
			transaction_details.setResponse(response);
			transaction_details.setDescription(DESCRIPTION);
			transaction_details.setBill_type(biller);
			transaction_detailsList.add(transaction_details);

		return transaction_detailsList;
	}
	List<Transaction_Details> prepareTransListPurchaseItem(String tr_id, String mobNumber, String uID, String strJSONBody
			, String response, String DESCRIPTION)
	{

		List<Transaction_Details> transaction_detailsList = new ArrayList<>();
		for (int i = 0; i< JSUtils.shopingCartList.size(); i++)
		{

			InventoryClass iv= JSUtils.shopingCartList.get(i);

			Transaction_Details transaction_details = new Transaction_Details();
			String amt=(Integer.parseInt(iv.getPrice())*iv.getItemCount())+"";
			transaction_details.setAmount(amt);
			transaction_details.setId(iv.getId());
			transaction_details.setDeal_of_the_day(iv.getDealOfDay());
			transaction_details.setTransaction_id(tr_id);
			transaction_details.setType("Item Purchase("+iv.getName()+")");
			transaction_details.setQuantity(iv.getItemCount()+"");
			transaction_details.setJswallet(mobNumber);
			transaction_details.setUser_id(uID);
			transaction_details.setRequest(strJSONBody);
			transaction_details.setResponse(response);
			transaction_details.setDescription(DESCRIPTION);
			transaction_detailsList.add(transaction_details);
		}
		return transaction_detailsList;
	}
	public String JSOpenAccount(String authHead,String OTP,String CNIC,String mobNumber,String ConsumerName,
								String AccountTitle,String BirthPlace,String PresentAddress,String CnicStatus,String CnicExpiry,String DOB,
								String FatherHusbandName,String MotherMaiden,String Gender,String AccountType)
	{
		String strJSONBody="{\"Cnic\":\"" + CNIC + "\"," +
				"\"OTP\":\"" + OTP+ "\"," +
				"\"MobileNumber\":\"" + mobNumber+ "\"," +
				"\"ConsumerName\":\"" + ConsumerName+ "\"," +
				"\"AccountTitle\":\"" + AccountTitle+ "\"," +
				"\"BirthPlace\":\"" + BirthPlace+ "\"," +
				"\"PresentAddress\":\"" + PresentAddress+ "\"," +
				"\"CnicStatus\":\"" + CnicStatus+ "\"," +
				"\"CnicExpiry\":\"" + CnicExpiry+ "\"," +
				"\"DOB\":\"" + DOB+ "\"," +
				"\"FatherHusbandName\":\"" + FatherHusbandName+ "\"," +
				"\"MotherMaiden\":\"" + MotherMaiden+ "\"," +
				"\"Gender\":\"" + Gender+ "\"," +
				"\"AccountType\":\"" + AccountType+
				"\"}";
		try {
			java.net.URL url = new URI(Config.w5 + "/mb/account/v1/create").toURL();
			HashMap<String, String> params=new HashMap<>();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType,strJSONBody
					);

			String res=getResultHTTP(authHead,url,body);

			if(res.trim().equals(""))
				return "-1Fail to create account. Please try again";
			if(res.startsWith("-1")) {
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0", "NEW_USER",mobNumber,strJSONBody,res.replace("-1",""),"FAIL","");
				String test=UpdateTransactionNew(transaction_detailses);
				return res;
			}
			JSONObject jsonObj=new JSONObject(res);
			if(!jsonObj.isNull("ResponseCode"))
			{

				List<Transaction_Details> transaction_detailses = prepareTransList("0","0", "NEW_USER",mobNumber,strJSONBody,res,"SUCCESS","");
				String test=UpdateTransactionNew(transaction_detailses);
				res=(String)jsonObj.get("ResponseCode");
				return "success";
			}
			else if(!jsonObj.isNull("errorCode"))
			{
				List<Transaction_Details> transaction_detailses = prepareTransList("0","0", "NEW_USER",mobNumber,strJSONBody,res,"FAIL","");
				String test=UpdateTransactionNew(transaction_detailses);
				res="-1"+(String)jsonObj.get("errorMessage");

				res+=" Code : "+(String)jsonObj.get("errorCode");
				return res;
			}
			return res;

		}

		catch(Exception e)
		{
			List<Transaction_Details> transaction_detailses = prepareTransList("0","0", "NEW_USER",mobNumber,strJSONBody,"Exception : "+e.getMessage(),"FAIL","");
			String test=UpdateTransactionNew(transaction_detailses);
			return "-1An error occured. Please try again";

		}
	}

	String getResultHTTP(String authHead,java.net.URL url,RequestBody requestBody) {
		try {
			OkHttpClient client = new OkHttpClient.Builder()
					.connectTimeout(3, TimeUnit.MINUTES)
					.writeTimeout(3, TimeUnit.MINUTES)
					.readTimeout(3, TimeUnit.MINUTES)
					.build();
			MediaType mediaType = MediaType.parse("application/json");


			Request request = new Request.Builder()
					.url(url)
					.post(requestBody)
					.addHeader("authorization", authHead)
					.addHeader("content-type", "application/json")

					.build();

			Response response = client.newCall(request).execute();
			return  response.body().string();//.toString();//.toString();
		}
		catch (Exception e)
		{

			e.printStackTrace();
			if(e.getClass().equals(new ConnectException().getClass()))
			{
				res="-1Please check your internet connection and try again.";
			}
			else if(e.getClass().equals(new SocketTimeoutException().getClass()))
			{/*
				retry++;
				if(retry<=3)
				{
					return getResultHTTP( authHead,url,requestBody);
				}
				else
				{
					retry=0;*/
					res="-1Request timeout. Please try again.";
				//}
			}

			else
				res="-1An error occured. Please try again";
		}
		return  res;
	}



	/*public String UpdateTransaction(String transaction_id,String amount,String tr_type,String jswallet,
									String request,String response,String description,String bill_type)
	{
		String result="";
		String MethodName = "transaction_log";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("transaction_id",transaction_id );
		soapRequest.addProperty("type", tr_type);
		soapRequest.addProperty("amount", amount);
		soapRequest.addProperty("description", description);
		soapRequest.addProperty("request", request);
		soapRequest.addProperty("response", response);
		soapRequest.addProperty("jswallet", jswallet);
		soapRequest.addProperty("quantity", "1");
		soapRequest.addProperty("bill_type", bill_type);

		result=getResult(soapRequest,MethodName);
		return result;

	}
*/
int retry=0;
	public String UpdateTransactionNew(List<Transaction_Details> transaction_detail) {
		String result="";
		String MethodName = "transaction_log";
		SoapObject soapAddRequest = new SoapObject(NameSpace, MethodName);

		//customers Parameter
		try
		{
			SoapObject soapDetails = new SoapObject(NameSpace, "tr");
			//soapDetails.addProperty("team_id",team_id);
			//soapDetails.addProperty("team_name",team_name);
			//soapDetails.addProperty("user_id","5");
			SoapObject soapDetail[] = new SoapObject[transaction_detail.size()];

			for (int i=0;i<transaction_detail.size();i++)
			{
				soapDetail[i]= new SoapObject(NameSpace, "Transaction_Details");
				Transaction_Details temp=transaction_detail.get(i);
				soapDetail[i].addProperty("w_username", Config.w1);
				soapDetail[i].addProperty("w_password", Config.w2);

				soapDetail[i].addProperty("item_id",temp.getId());
				soapDetail[i].addProperty("deal_of_the_day",temp.getDeal_of_the_day());
				soapDetail[i].addProperty("quantity",temp.getQuantity() );
				soapDetail[i].addProperty("bill_type",temp.getBill_type());
				soapDetail[i].addProperty("user_id",temp.getUser_id());
				soapDetail[i].addProperty("jswallet", temp.getJswallet());
				soapDetail[i].addProperty("response",temp.getResponse());
				soapDetail[i].addProperty("amount", temp.getAmount());
				soapDetail[i].addProperty("type",temp.getType());
				soapDetail[i].addProperty("transaction_id",temp.getTransaction_id());
				soapDetail[i].addProperty("description",temp.getDescription());
				soapDetail[i].addProperty("request",temp.getRequest());
				soapDetails.addSoapObject(soapDetail[i]);
			}

			soapAddRequest.addSoapObject(soapDetails);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapAddRequest);
			envelope.addMapping(NameSpace, "Transaction_Details", transaction_detail.getClass());
			if(URL.startsWith("https"))
			{
				HttpsTransporSE HttpTransportSE = new HttpsTransporSE(URL, 0, "", 120000);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
			else{
				HttpTransportSE HttpTransportSE = new HttpTransportSE(URL);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();


			}
		} catch (Exception e) {
			e.printStackTrace();
			//if(e.getClass().equals(new ConnectException().getClass()) || (e.getClass().equals(new UnknownHostException())) || e.getClass().equals(new SocketTimeoutException().getClass())
			//		|| e.getClass().equals(new NoRouteToHostException().getClass())){

				/*retry++;
				if(retry<=3)
				{
					return UpdateTransactionNew(transaction_detail);
				}
				else
				{*/
		//			DatabaseHandler databaseHandler=new DatabaseHandler(myContext);
			//		databaseHandler.saveTransactionDetails(transaction_detail);
				//	retry=0;
					result="-1"+e.getMessage();

			}

		//}
		if(!result.trim().toLowerCase().equals("success"))
		{
			DatabaseHandler databaseHandler=new DatabaseHandler(myContext);
			databaseHandler.saveTransactionDetails(transaction_detail);


		}
		return result;
	}


	public String GetMyTransaction(String wallet_no)
	{

		String result="";

		String MethodName = "get_transaction_logs";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("jswallet", wallet_no);
		result=getResult(soapRequest,MethodName);
		return result;
	}




	public String TranslateData(String name,String birthPlace,String motherName,String presentAddress,String fathername,String wallet_id )
	{

		String result="";
		String MethodName = "UtoEServiceV2";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("ws_username", "fsl");
		soapRequest.addProperty("ws_password", "fsl123");
		soapRequest.addProperty("name",name );
		soapRequest.addProperty("birthPlace",birthPlace );
		soapRequest.addProperty("motherName", motherName);
		soapRequest.addProperty("fathername", fathername);
		soapRequest.addProperty("wallet_id",wallet_id );
		soapRequest.addProperty("presentAddress",presentAddress );



		result=getResult(soapRequest,MethodName,"http://translator.tecthis.com/service1.asmx");
		return result;
	}
//////////////////////////////////////////////////////////////////END OF JS METHODS//////////////////////////////////////////////////



	public String getPlayersData(String action) {
		String result="";

		String MethodName = "get_fixtures";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("action", action);
        result=getResult(soapRequest,MethodName);
		return result;
	}

    public String getInventryCount(String user_id) {
        String result="";

        String MethodName = "get_inventory";
        SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

        soapRequest.addProperty("w_username", Config.w1);
        soapRequest.addProperty("w_password", Config.w2);
        soapRequest.addProperty("user_id", user_id);//
        result=getResult(soapRequest,MethodName);
        return result;
    }

	public String getUserFantasyTeam(String user_id) {
		String result="";

		String MethodName = "get_user_fantasy_team";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("user_id", user_id);// need to change
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String getMatchWiseInventory(String team_id, String match_id) {
		String result="";

		String MethodName = "get_matchwise_inventory";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("team_id", team_id);
		soapRequest.addProperty("match_id", match_id);
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String getAllItems() {
		String result="";

		String MethodName = "get_all_items";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String getMyNotifications() {
		String result="";

		String MethodName = "get_user_notifications";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("user_id", userID);
		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String setNotificationsRead(String notifID,String action) {
		String result="";

		String MethodName = "update_notification_read";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("user_id", userID);
		soapRequest.addProperty("notification_id", notifID);
		soapRequest.addProperty("action", action);

		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String getDealOfDay() {
		String result="";

		String MethodName = "get_deal_of_the_day";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		result=getResult(soapRequest,MethodName);
		return result;
	}


	public String getMyBoosters(String user_id) {
		String result="";

		String MethodName = "get_user_boosters";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("user_id", user_id);
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String redeemBoosters(String booster_id,String user_id) {
		String result="";

		String MethodName = "redeem_boosters";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("booster_id", booster_id);
		soapRequest.addProperty("user_id", user_id);

		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String getTopPlayers() {
		String result="";

		String MethodName = "get_top_ten_players";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		//soapRequest.addProperty("action", action);
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String getFixturesData(String action) {
		String result="";
		String MethodName = "get_fixtures";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("action", action);
		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String getFixturesDataAll(String action) {
		String result="";
		String MethodName = "get_fixtures";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);
		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("action", action);
		result=getResult(soapRequest,MethodName);
		return result;
	}
	public String getLeaderboardPositions(String date, String user_id) {
		String result="";

		String MethodName = "get_leaderboard_stats";
		SoapObject soapRequest = new SoapObject(NameSpace, MethodName);

		soapRequest.addProperty("w_username", Config.w1);
		soapRequest.addProperty("w_password", Config.w2);
		soapRequest.addProperty("date", date);
        soapRequest.addProperty("team", user_id);
		result=getResult(soapRequest,MethodName);
		return result;
	}

	public String createTeam(List<PlayerAttributes> player, String team_id, String team_name, String user_id, String swap_count, String process, String points) {
		String result="";
		String MethodName = "create_team";
		SoapObject soapAddRequest = new SoapObject(NameSpace, MethodName);

		//customers Parameter
		try
		{
			SoapObject soapDetails = new SoapObject(NameSpace, "player");
			//soapDetails.addProperty("team_id",team_id);
			//soapDetails.addProperty("team_name",team_name);
			//soapDetails.addProperty("user_id","5");
			SoapObject soapDetail[] = new SoapObject[player.size()];

			for (int i=0;i<player.size();i++){
				soapDetail[i]= new SoapObject(NameSpace, "PlayerAttributes");
				PlayerAttributes temp=player.get(i);
				soapDetail[i].addProperty("w_username", Config.w1);
				soapDetail[i].addProperty("w_password", Config.w2);
                soapDetail[i].addProperty("team_id",team_id);
                soapDetail[i].addProperty("team_name",team_name);
                soapDetail[i].addProperty("user_id",user_id);
				soapDetail[i].addProperty("fc_team_id", player.get(i).getFc_team_id());
				soapDetail[i].addProperty("player_id",player.get(i).getPlayer_id());
				soapDetail[i].addProperty("is_captain", player.get(i).getIs_captain());
				soapDetail[i].addProperty("is_mom",player.get(i).getIs_mom());
				soapDetail[i].addProperty("is_gg", player.get(i).getIs_gg());
				soapDetail[i].addProperty("is_iconic",player.get(i).getIs_iconic());
				soapDetail[i].addProperty("is_purple_cap", player.get(i).getIs_purple_cap());
				soapDetail[i].addProperty("is_orange_cap",player.get(i).getIs_orange_cap());
				soapDetail[i].addProperty("is_safety",player.get(i).getIs_safety());
				soapDetail[i].addProperty("is_team_safety",player.get(i).getIs_team_safety());

				soapDetail[i].addProperty("is_assign",process);

				soapDetail[i].addProperty("is_gg_counter", player.get(i).getIs_gg_count());
				soapDetail[i].addProperty("is_iconic_counter",player.get(i).getIs_iconic_count());
				soapDetail[i].addProperty("is_purple_cap_counter", player.get(i).getIs_purple_cap_count());
				soapDetail[i].addProperty("is_orange_cap_counter",player.get(i).getIs_orange_cap_count());
				soapDetail[i].addProperty("is_safety_counter",player.get(i).getIs_safety_count());
				soapDetail[i].addProperty("is_team_safety_counter",player.get(i).getIs_team_safety_count());

				soapDetail[i].addProperty("number_swaps",swap_count);
				soapDetail[i].addProperty("points",points);
				soapDetails.addSoapObject(soapDetail[i]);
			}

			soapAddRequest.addSoapObject(soapDetails);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope( SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(soapAddRequest);
			envelope.addMapping(NameSpace, "PlayerAttributes", player.getClass());
			if(URL.startsWith("https"))
			{
				HttpsTransporSE HttpTransportSE = new HttpsTransporSE(URL, 0, "", 30000);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
			else{
				HttpTransportSE HttpTransportSE = new HttpTransportSE(URL);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
		} catch (Exception e) {
			Writer writer = new StringWriter();
			PrintWriter printWriter = new PrintWriter(writer);
			e.printStackTrace(printWriter);
			String exception = writer.toString();
			e.printStackTrace();
			return "";

		}
		return result;
	}
	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry : params.entrySet()){
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}
	//int retry=0;
	private  String getResult2(SoapObject soapRequest, String MethodName, String mURL)
	{

		String result="";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapRequest);
		try{

			if(mURL.startsWith("https"))
			{
				HttpsTransporSE HttpTransportSE = new HttpsTransporSE(mURL, 0, "", 30000);
				//HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
			else{
				HttpTransportSE HttpTransportSE = new HttpTransportSE(mURL);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getClass().equals(new ConnectException().getClass()))
			{
				result="-1Please check your internet connection and try again.";
			}
			else if(e.getClass().equals(new SocketTimeoutException().getClass()))
			{
				/*retry++;
				if(retry<=3)
				{
					return getResult2( soapRequest,  MethodName,mURL);
				}
				else
				{
					retry=0;*/
					result="-1Request timeout. Please try again.";
				//}
			}

			else
				result="-1An error occured. Please try again";
		}
		return result;
	}
	private  String getResult(SoapObject soapRequest, String MethodName)
	{

		String result="";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapRequest);
		try{

			if(URL.startsWith("https"))
			{
				HttpsTransporSE HttpTransportSE = new HttpsTransporSE(URL, 0, "", 30000);
				//HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
			else{
				HttpTransportSE HttpTransportSE = new HttpTransportSE(URL, 30000);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getClass().equals(new ConnectException().getClass()))
			{
				result="-1Please check your internet connection and try again.";
			}
			else if(e.getClass().equals(new SocketTimeoutException().getClass()))
			{
				/*retry++;
				if(retry<=3)
				{
					return getResult( soapRequest,  MethodName);
				}
				else
				{
					retry=0;*/
					result="-1Request timeout. Please try again.";
			//	}
			}

			else
				result="-1An error occured. Please try again";
		}
		return result;
	}
	public void ConnectForSingleNode(){
		try {
			envelop.setOutputSoapObject(object);
			if(URL.startsWith("https"))
			{
				HttpsTransporSE androidHttpTransport = new HttpsTransporSE(URL, 0, "", 30000);
				androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				androidHttpTransport.debug=true;
				androidHttpTransport.call( Soap_Action, envelop); 
			}
			else
			{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL) ; //for Local
				androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				androidHttpTransport.debug=true;
				androidHttpTransport.call( Soap_Action, envelop);
			}

			try {
				result=(SoapObject)envelop.bodyIn;
				//result=(SoapObject)result.getProperty(0);

			}
			catch (Exception e) {
				e.printStackTrace();
			}

		} 
		catch (Exception e) {

		}

	}
	private  String getResult(SoapObject soapRequest, String MethodName, String URL2)
	{

		String result="";
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(soapRequest);
		try{

			if(URL2.startsWith("https"))
			{
				HttpsTransporSE HttpTransportSE = new HttpsTransporSE(URL2, 0, "", 30000);
				//HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
			else{
				HttpTransportSE HttpTransportSE = new HttpTransportSE(URL2);
				HttpTransportSE.debug = true ;
				HttpTransportSE.call(NameSpace + MethodName, envelope);
				result = envelope.getResponse().toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getClass().equals(new ConnectException().getClass()))
			{
				result="-1Please check your internet connection and try again.";
			}
			else if(e.getClass().equals(new SocketTimeoutException().getClass()))
			{
				/*retry++;
				if(retry<=3)
				{
					return getResult( soapRequest,  MethodName,URL2);
				}
				else
				{
					retry=0;*/
					result="-1Request timeout. Please try again.";
				//}
			}

			else
				result="-1An error occured. Please try again";
		}
		return result;
	}
	public SoapObject Result(){
		return result;
	} 
	public void addProperties(String name,Object value){  

		object.addProperty(name, value);  

	}
	public void Connect(){
		try { 
			envelop.setOutputSoapObject(object);           
			if(URL.startsWith("https"))
			{
				HttpsTransporSE androidHttpTransport = new HttpsTransporSE(URL, 0, "", 300000);
				androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				androidHttpTransport.debug=true;
				androidHttpTransport.call( Soap_Action, envelop); 
			}
			else
			{
				HttpTransportSE androidHttpTransport = new HttpTransportSE(null,URL,300000) ; //for Local
				
				androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
				androidHttpTransport.debug=true;
				androidHttpTransport.call( Soap_Action, envelop); 
			}


		//	Constants.IS_CONNECTION_FAILED = false;
			try {
				result=(SoapObject)envelop.bodyIn;
				result=(SoapObject)result.getProperty(0);
				result=(SoapObject)result.getProperty(1);
				result=(SoapObject)result.getProperty(0);
				if(!isValidUser){
					isValidUser = true ;
				}
			}    
			catch (Exception e) {   	 
				isValidUser = false;
			} 
		}
		catch (Exception e) {

	//		Constants.IS_CONNECTION_FAILED = true;
		}
	}

	public static boolean isNetworkAvailable(Context context){


		ConnectivityManager connectivityManager 
		= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
	}

}

	
