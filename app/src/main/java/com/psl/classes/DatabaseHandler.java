package com.psl.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{


	private static final String LOG = "DatabaseHandler";
	// Database Version
	private static final int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "psl";

	// Table Names

    public static final String TABLE_HISTORY_PLAYERS = "tbl_history_players";
   	public static final String TABLE_SELECTED_PLAYERS = "tbl_selected_players";
	public static final String TABLE_FIXTURES = "tbl_fixtures";
	public static final String TABLE_JS_LOCATIONS= "tbl_js_locations";
	public static final String TABLE_JS_Boosters= "tbl_js_boosters";
	public static final String TABLE_Transaction_Details= "TABLE_Transaction_Details";


	private static final String Booster_Type = "booster_type";
	private static final String Booster_Points = "booster_points";
	private static final String Booster_Redeem = "booster_redeem";



	private static final String ID = "id";
	private static final String Employee_ID = "Emp_Id";
	private static final String t_id = "t_id";
	private static final String player_id = "player_id";
	private static final String player_name= "player_name";
	private static final String team_name= "team_name";
	private static final String team_id= "team_id";
	private static final String average= "average";
	private static final String price= "price";
	private static final String isCaptain= "isCaptain";
	private static final String isMom= "isMom";
	private static final String isIconic= "isIconic";
	private static final String isGoldengloves= "isGoldengloves";
	private static final String isPrurplecap= "isPrurplecap";
	private static final String isOrangecap= "isOrangecap";
	private static final String isSafety= "isSafety";
	private static final String isTeamSafety= "isTeamSafety";
	private static final String player_teamName= "player_teamName";
	private static final String player_category= "player_category";
	private static final String player_style= "player_style";
	private static final String bowling_strike_rate= "bowling_strike_rate";
	private static final String role= "role";
	private static final String battingStyle= "battingStyle";
	private static final String matchesPlayed= "matchesPlayed";
	private static final String innings= "innings";
	private static final String runs= "runs";
	private static final String hundreds= "hundreds";
	private static final String fifties= "fifties";
	private static final String thirties= "thirties";
	private static final String overs= "overs";
	private static final String run_conceded= "run_conceded";
	private static final String wickets= "wickets";
	private static final String maiden= "maiden";
	private static final String batting_strike= "batting_strike";
	private static final String economy= "economy";
	private static final String nationality= "nationality";
	private static final String age= "age";
	private static final String five_wickets= "five_wickets";
	private static final String isSelected= "isSelected";

	private static final String LOCATION_NAME= "loc_name";
	private static final String LATITUDE= "latitude";
	private static final String LONGITUDE= "longitude";
	private static final String ADDRESS= "adress";
	private static final String AREA= "area";
	private static final String CATEGORY= "category";

    private static final String MATCH_ID = "match_id";
    private static final String P_POINTS = "p_points";
    private static final String TOTAL_P_pOINTS = "total_points";
    private static final String user_id = "user_id";

    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
    SimpleDateFormat dateFormater = new SimpleDateFormat("MM-dd-yyyy");

	private static final String MATCH = "match";
	private static final String MATCH_DATE = "match_date";
	private static final String VENUE = "venue";
	private static final String MATCH_RESULT = "match_result";

	List<String> transactionsData = new ArrayList<String>();
	Context cntxt;

	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME, null, DATABASE_VERSION);
		this.cntxt = context;
	}

	private static final String CREATE_TABLE_JS_BOOSTERS = "CREATE TABLE "
			+ TABLE_JS_Boosters+" ( "
			+ ID + " INTEGER PRIMARY KEY,"
			+ Booster_Type + " TEXT,"
			+ Booster_Points + " TEXT,"
			+ Booster_Redeem + " TEXT " + ")";

	private static final String CREATE_TABLE_SELTECTED_PLAYERS = "CREATE TABLE "
			+ TABLE_SELECTED_PLAYERS+" ( "
			+ ID + " INTEGER PRIMARY KEY,"
			+ t_id + " TEXT,"
			+ player_id + " TEXT,"
			+ player_name + " TEXT,"
			+ team_name + " TEXT,"
			+ team_id + " TEXT,"
			+ average + " TEXT,"
			+ price + " TEXT,"
			+ isCaptain + " TEXT,"
			+ isMom + " TEXT,"
			+ isIconic + " TEXT,"
			+ isGoldengloves + " TEXT,"
			+ isPrurplecap + " TEXT,"
			+ isOrangecap + " TEXT,"
			+ isSafety + " TEXT,"
			+ isTeamSafety + " TEXT,"
			+ player_teamName + " TEXT,"
			+ player_category + " TEXT,"
			+ player_style + " TEXT,"
			+ bowling_strike_rate + " TEXT,"
			+ role + " TEXT,"
			+ matchesPlayed + " TEXT,"
			+ innings + " TEXT,"
			+ runs + " TEXT,"
			+ hundreds + " TEXT,"
			+ fifties + " TEXT,"
			+ thirties + " TEXT,"
			+ overs + " TEXT,"
			+ run_conceded + " TEXT,"
			+ wickets + " TEXT,"
			+ maiden + " TEXT,"
			+ batting_strike + " TEXT,"
			+ economy + " TEXT,"
			+ nationality + " TEXT,"
			+ age + " TEXT,"
			+ five_wickets + " TEXT,"
			+ isSelected + " TEXT,"
			+ battingStyle + " TEXT " + ")";


    private static final String CREATE_TABLE_HISTORY_PLAYERS = "CREATE TABLE "
            + TABLE_HISTORY_PLAYERS + " ( "
            + ID + " INTEGER PRIMARY KEY,"
            + t_id + " TEXT,"
            + user_id + " TEXT,"
            + player_id + " TEXT,"
            + MATCH_ID + " TEXT,"
            + player_name + " TEXT,"
            + team_name + " TEXT,"
            + team_id + " TEXT,"
            + average + " TEXT,"
            + price + " TEXT,"
            + isCaptain + " TEXT,"
            + isMom + " TEXT,"
            + isIconic + " TEXT,"
            + isGoldengloves + " TEXT,"
            + isPrurplecap + " TEXT,"
            + isOrangecap + " TEXT,"
            + isSafety + " TEXT,"
            + isTeamSafety + " TEXT,"
            + player_teamName + " TEXT,"
            + player_category + " TEXT,"
            + player_style + " TEXT,"
            + bowling_strike_rate + " TEXT,"
            + role + " TEXT,"
            + matchesPlayed + " TEXT,"
            + innings + " TEXT,"
            + runs + " TEXT,"
            + hundreds + " TEXT,"
            + fifties + " TEXT,"
            + thirties + " TEXT,"
            + overs + " TEXT,"
            + run_conceded + " TEXT,"
            + wickets + " TEXT,"
            + maiden + " TEXT,"
            + batting_strike + " TEXT,"
            + economy + " TEXT,"
            + nationality + " TEXT,"
            + age + " TEXT,"
            + five_wickets + " TEXT,"
            + isSelected + " TEXT,"
            + P_POINTS + " TEXT,"
            + TOTAL_P_pOINTS + " TEXT,"
            + battingStyle + " TEXT " + ")";


    private static final String CREATE_TABLE_JS_LOCATIONS = "CREATE TABLE "
            + TABLE_JS_LOCATIONS + " ( "
            + ID + " INTEGER PRIMARY KEY,"
            + LOCATION_NAME + " TEXT,"
            + LATITUDE + " TEXT,"
            + ADDRESS + " TEXT,"
            + AREA + " TEXT,"
            + CATEGORY + " TEXT,"
            + LONGITUDE + " TEXT " + ")";

	//////////////////////////////////////For Transactions///////////////////////////
	static String deal_of_the_day   ="deal_of_the_day"    ;
	static String TR_ID             ="TR_ID"    ;
	static String id                ="id"    ;
	static String bill_type         ="bill_type"    ;
	static String quantity          ="quantity"    ;
	static String jswallet          ="jswallet"    ;
	static String response          ="response"    ;
	static String amount            ="amount"    ;
	static String type              ="type"    ;
	static String transaction_id    ="transaction_id"    ;
	static String description       ="description"    ;
	static String request           ="request"    ;

	private static final String CREATE_TABLE_TRANSACTION_LIST = "CREATE TABLE "
			+ TABLE_Transaction_Details+" ( "
			+ TR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ deal_of_the_day 	+ " TEXT,"
			+ id             	+ " TEXT,"
			+ bill_type      	+ " TEXT,"
			+ quantity       	+ " TEXT,"
			+ user_id         	+ " TEXT,"
			+ jswallet       	+ " TEXT,"
			+ response       	+ " TEXT,"
			+ amount         	+ " TEXT,"
			+ type           	+ " TEXT,"
			+ transaction_id  	+ " TEXT,"
			+ description    	+ " TEXT,"
			+ request 			+ " TEXT " + ")";
////////////////////////////////////////////////////////////////////////////////////////////////////

	private static final String CREATE_TABLE_FIXTURES = "CREATE TABLE "
			+ TABLE_FIXTURES+" ( "
			+ ID + " INTEGER PRIMARY KEY,"
			+ MATCH + " TEXT,"
			+ MATCH_DATE + " TEXT,"
			+ VENUE + " TEXT,"
			+ MATCH_RESULT + " TEXT " + ")";


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_SELTECTED_PLAYERS);
		db.execSQL(CREATE_TABLE_FIXTURES);
		//db.execSQL(CREATE_TABLE_JS_LOCATIONS);
		db.execSQL(CREATE_TABLE_JS_BOOSTERS);
            db.execSQL(CREATE_TABLE_HISTORY_PLAYERS);
            db.execSQL(CREATE_TABLE_TRANSACTION_LIST);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {


				db.execSQL("DROP TABLE IF EXISTS " + TABLE_SELECTED_PLAYERS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIXTURES);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_PLAYERS);
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_JS_Boosters);

			onCreate(db);
				// create new tables
		//onCreate(db);
	}


	public long saveJsBooster(String B_ID,String booster_Type,String points){
		long processId = 0  ;
		SQLiteDatabase database = null;
		try {

			database = this.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put(ID, B_ID);
					values.put(Booster_Points, points);
					values.put(Booster_Type, booster_Type);
					values.put(Booster_Redeem,"NO");

					processId = database.insert(TABLE_JS_Boosters, null, values);

				}

		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(database!=null)
				if(database.isOpen())
					database.close();
		}
		return processId ;
	}

	public void deleteBooster(String id)
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+ TABLE_JS_Boosters+" WHERE ID = '"+id+"'");
			db.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void deleteTransactionAll()
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+ TABLE_Transaction_Details);
			db.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	public List<BoosterVO> getBooster(){
		List<BoosterVO> playerList = new ArrayList<BoosterVO>();
		Cursor c = null ;
		try {

			String query = "SELECT * FROM " + TABLE_JS_Boosters+" WHERE "+Booster_Redeem+" = 'NO'" ;

			SQLiteDatabase db = this.getReadableDatabase();
			c = db.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {

					BoosterVO fixture = new BoosterVO();
					fixture.setID(c.getString(c.getColumnIndex(ID))+"");
					fixture.setBoosterPoints(c.getString(c.getColumnIndex(Booster_Points)));
					fixture.setBoosterType(c.getString(c.getColumnIndex(Booster_Type)));


					playerList.add(fixture);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(c!=null)
			c.close();

		}
		return playerList;
	}
	public long saveJsLocations(List<JsLocationsVO> data){
		long processId = 0  ;
		SQLiteDatabase database = null;
		try {

			database = this.getWritableDatabase();
			for (int i = 0; i < data.size(); i++) {

				if (data != null) {

					ContentValues values = new ContentValues();
					values.put(ID, data.get(i).getId());
					values.put(LOCATION_NAME, data.get(i).getLoc_name());
					values.put(LATITUDE, data.get(i).getLatitude());
					values.put(LONGITUDE, data.get(i).getLongitude());

					values.put(ADDRESS, data.get(i).getAddress());
					values.put(AREA, data.get(i).getArea());
					values.put(CATEGORY, data.get(i).getCategory());

					processId = database.insert(TABLE_JS_LOCATIONS, null, values);

				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(database!=null)
				if(database.isOpen())
					database.close();
		}
		return processId ;
	}

	public long saveFixtures(List<FixturesVO> data){
		long processId = 0  ;
		SQLiteDatabase database = null;
		try {

			database = this.getWritableDatabase();
			for (int i = 0; i < data.size(); i++) {

				if (data != null) {

					ContentValues values = new ContentValues();
					values.put(ID, data.get(i).getId());
					values.put(MATCH, data.get(i).getMatch());
					values.put(MATCH_RESULT, data.get(i).getResult());
					values.put(MATCH_DATE, data.get(i).getDate());
					values.put(VENUE, data.get(i).getVenue_name());

					processId = database.insert(TABLE_FIXTURES, null, values);

				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(database!=null)
				if(database.isOpen())
					database.close();
		}
		return processId ;
	}

	public long saveTransactionDetails(List<Transaction_Details> data){
		long processId = 0  ;
		SQLiteDatabase database = null;
		try {

			database = this.getWritableDatabase();
			for (int i = 0; i < data.size(); i++) {

				Transaction_Details transaction_details=data.get(i);
				ContentValues values = new ContentValues();
				values.put(ID, data.get(i).getId());
				values.put(deal_of_the_day	,transaction_details.getDeal_of_the_day() );
				values.put(id             	,transaction_details.getId() );
				values.put(bill_type		,transaction_details.getBill_type() );
				values.put(quantity			,transaction_details.getQuantity() );
				values.put(user_id			,transaction_details.getUser_id() );
				values.put(jswallet			,transaction_details.getJswallet() );
				values.put(response			,transaction_details.getResponse() );
				values.put(amount         	,transaction_details.getAmount() );
				values.put(type           	,transaction_details.getType() );
				values.put(transaction_id 	,transaction_details.getTransaction_id() );
				values.put(description    	,transaction_details.getDescription() );
				values.put(request        	,transaction_details.getRequest() );



				processId = database.insert(TABLE_Transaction_Details, null, values);


			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			if(database!=null)
				if(database.isOpen())
					database.close();
		}
		return processId ;
	}

	public List<Transaction_Details> getTransactionDetails(){
		List<Transaction_Details> playerList = new ArrayList<Transaction_Details>();
		Cursor c = null ;
		try {

			String query = "SELECT * FROM " + TABLE_Transaction_Details +" ORDER BY "+transaction_id+" ASC";

			SQLiteDatabase db = this.getReadableDatabase();
			c = db.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {

					Transaction_Details fixture = new Transaction_Details();
					fixture.setDeal_of_the_day(c.getString(c.getColumnIndex( deal_of_the_day )));
					fixture.setId(c.getString(c.getColumnIndex( id  )));
					fixture.setBill_type(c.getString(c.getColumnIndex( bill_type )));
					fixture.setQuantity(c.getString(c.getColumnIndex( quantity )));
					fixture.setUser_id(c.getString(c.getColumnIndex( user_id )));
					fixture.setJswallet(c.getString(c.getColumnIndex( jswallet)));
					fixture.setResponse(c.getString(c.getColumnIndex( response)));
					fixture.setAmount(c.getString(c.getColumnIndex( amount)));
					fixture.setType(c.getString(c.getColumnIndex( type)));
					fixture.setTransaction_id(c.getString(c.getColumnIndex( transaction_id)));
					fixture.setDescription(c.getString(c.getColumnIndex( description)));
					fixture.setRequest(c.getString(c.getColumnIndex( request)));



					playerList.add(fixture);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(c!=null)
				c.close();

		}
		if(!playerList.isEmpty())
			deleteTransactionAll();
		return playerList;
	}
	public long saveSelectedPlayers(List<PlayerProfileVO> player){
		long processId = 0  ;
		SQLiteDatabase database = null;
		try {

			database = this.getWritableDatabase();
			for (int i = 0; i < player.size(); i++) {

				if (player != null) {

					ContentValues values = new ContentValues();

					values.put(t_id, player.get(i).getId());
					values.put(player_id, player.get(i).getPlayer_id());
					values.put(player_name, player.get(i).getPlayer_name());
					values.put(team_name, player.get(i).getTeam_name());
					values.put(team_id, player.get(i).getTeam_id());
					values.put(average, player.get(i).getAverage());
					values.put(price, player.get(i).getPrice());
					values.put(isCaptain, String.valueOf(player.get(i).getIsCaptain()));
					values.put(isMom, player.get(i).getIsMom());
					values.put(isIconic, player.get(i).getIsIconic());
					values.put(isGoldengloves, player.get(i).getIsGoldengloves());
					values.put(isPrurplecap, player.get(i).getIsPrurplecap());
					values.put(isOrangecap, player.get(i).getIsOrangecap());

					values.put(isSafety, player.get(i).getIsSafety());
					values.put(isTeamSafety, player.get(i).getIsTeamSafety());
					values.put(player_teamName, player.get(i).getPlayer_teamName());
					values.put(player_category, player.get(i).getPlayer_category());
					values.put(player_style, player.get(i).getPlayer_style());


					values.put(bowling_strike_rate, player.get(i).getBowling_strike_rate());
					values.put(role, player.get(i).getRole());
					values.put(battingStyle, player.get(i).getMatchesPlayed());
					values.put(innings, player.get(i).getInnings());
					values.put(runs, player.get(i).getRuns());

					values.put(hundreds, player.get(i).getHundreds());
					values.put(fifties, player.get(i).getFifties());
					values.put(thirties, player.get(i).getThirties());
					values.put(overs, player.get(i).getOvers());
					values.put(run_conceded, player.get(i).getRun_conceded());


					values.put(wickets, player.get(i).getWickets());
					values.put(maiden, player.get(i).getMaiden());
					values.put(batting_strike, player.get(i).getBatting_strike());
					values.put(economy, player.get(i).getEconomy());
					values.put(nationality, player.get(i).getNationality());


					values.put(age, player.get(i).getAge());
					values.put(five_wickets, player.get(i).getFifties());
					values.put(isSelected, player.get(i).getIsSelected());

					processId = database.insert(TABLE_SELECTED_PLAYERS, null, values);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                if (database.isOpen())
                    database.close();
        }
        return processId;
    }

    public long saveHistoryData(List<PlayerProfileVO> player, String match_id, String userid) {
        long processId = 0;
        SQLiteDatabase database = null;
        try {

            database = this.getWritableDatabase();
            for (int i = 0; i < player.size(); i++) {

                if (player != null) {

                    ContentValues values = new ContentValues();

                    values.put(t_id, player.get(i).getId());
                    values.put(player_id, player.get(i).getPlayer_id());
                    values.put(player_name, player.get(i).getPlayer_name());
                    values.put(MATCH_ID, match_id);
                    values.put(user_id, userid);
                    values.put(team_name, player.get(i).getTeam_name());
                    values.put(team_id, player.get(i).getTeam_id());
                    values.put(average, player.get(i).getAverage());
                    values.put(price, player.get(i).getPrice());
                    values.put(isCaptain, String.valueOf(player.get(i).getIsCaptain()));
                    values.put(isMom, player.get(i).getIsMom());
                    values.put(isIconic, player.get(i).getIsIconic());
                    values.put(isGoldengloves, player.get(i).getIsGoldengloves());
                    values.put(isPrurplecap, player.get(i).getIsPrurplecap());
                    values.put(isOrangecap, player.get(i).getIsOrangecap());

                    values.put(isSafety, player.get(i).getIsSafety());
                    values.put(isTeamSafety, player.get(i).getIsTeamSafety());
                    values.put(player_teamName, player.get(i).getPlayer_teamName());
                    values.put(player_category, player.get(i).getPlayer_category());
                    values.put(player_style, player.get(i).getPlayer_style());


                    values.put(bowling_strike_rate, player.get(i).getBowling_strike_rate());
                    values.put(role, player.get(i).getRole());
                    values.put(battingStyle, player.get(i).getMatchesPlayed());
                    values.put(innings, player.get(i).getInnings());
                    values.put(runs, player.get(i).getRuns());

                    values.put(hundreds, player.get(i).getHundreds());
                    values.put(fifties, player.get(i).getFifties());
                    values.put(thirties, player.get(i).getThirties());
                    values.put(overs, player.get(i).getOvers());
                    values.put(run_conceded, player.get(i).getRun_conceded());


                    values.put(wickets, player.get(i).getWickets());
                    values.put(maiden, player.get(i).getMaiden());
                    values.put(batting_strike, player.get(i).getBatting_strike());
                    values.put(economy, player.get(i).getEconomy());
                    values.put(nationality, player.get(i).getNationality());


                    values.put(age, player.get(i).getAge());
                    values.put(five_wickets, player.get(i).getFifties());
                    values.put(isSelected, player.get(i).getIsSelected());
                    values.put(P_POINTS, player.get(i).getP_points());
                    values.put(TOTAL_P_pOINTS, player.get(i).getTotal_points());

                    processId = database.insert(TABLE_HISTORY_PLAYERS, null, values);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null)
                if (database.isOpen())
                    database.close();
        }
        return processId;
    }

    public List<PlayerProfileVO> getSelectedMatchHistory(String match_id, String userid) {
        List<PlayerProfileVO> playerList = new ArrayList<PlayerProfileVO>();
        Cursor c = null;
        try {

            String query = "SELECT * FROM " + TABLE_HISTORY_PLAYERS + " WHERE " + MATCH_ID + " = '" + match_id + "' AND " + user_id + " = '" + userid +"'";

            SQLiteDatabase db = this.getReadableDatabase();
            c = db.rawQuery(query, null);

            if (c.moveToFirst()) {
                do {

                    PlayerProfileVO playerVo = new PlayerProfileVO();
                    playerVo.setId(c.getInt(c.getColumnIndex(t_id)) + "");
                    playerVo.setPlayer_id(c.getString(c.getColumnIndex(player_id)));
                    playerVo.setPlayer_name(c.getString(c.getColumnIndex(player_name)));
                    playerVo.setTeam_name(c.getString(c.getColumnIndex(team_name)));
                    playerVo.setTeam_id(c.getString(c.getColumnIndex(team_id)));
                    playerVo.setAverage(c.getString(c.getColumnIndex(average)));
                    playerVo.setPrice(c.getString(c.getColumnIndex(price)));
                    playerVo.setIsCaptain(c.getString(c.getColumnIndex(isCaptain)));
                    playerVo.setIsMom(c.getString(c.getColumnIndex(isMom)));
                    playerVo.setIsIconic(c.getString(c.getColumnIndex(isIconic)));
                    playerVo.setIsGoldengloves(c.getString(c.getColumnIndex(isGoldengloves)));
                    playerVo.setIsPrurplecap(c.getString(c.getColumnIndex(isPrurplecap)));
                    playerVo.setIsOrangecap(c.getString(c.getColumnIndex(isOrangecap)));

                    playerVo.setIsSafety(c.getString(c.getColumnIndex(isSafety)));
                    playerVo.setIsTeamSafety(c.getString(c.getColumnIndex(isTeamSafety)));
                    playerVo.setPlayer_teamName(c.getString(c.getColumnIndex(player_teamName)));
                    playerVo.setPlayer_category(c.getString(c.getColumnIndex(player_category)));
                    playerVo.setPlayer_style(c.getString(c.getColumnIndex(player_style)));
                    playerVo.setBowling_strike_rate(c.getString(c.getColumnIndex(bowling_strike_rate)));

                    playerVo.setRole(c.getString(c.getColumnIndex(role)));
                    playerVo.setBattingStyle(c.getString(c.getColumnIndex(battingStyle)));
                    playerVo.setMatchesPlayed(c.getString(c.getColumnIndex(matchesPlayed)));
                    playerVo.setInnings(c.getString(c.getColumnIndex(innings)));
                    playerVo.setRuns(c.getString(c.getColumnIndex(runs)));
                    playerVo.setHundreds(c.getString(c.getColumnIndex(hundreds)));


                    playerVo.setThirties(c.getString(c.getColumnIndex(thirties)));
                    playerVo.setOvers(c.getString(c.getColumnIndex(overs)));
                    playerVo.setRun_conceded(c.getString(c.getColumnIndex(run_conceded)));
                    playerVo.setWickets(c.getString(c.getColumnIndex(wickets)));
                    playerVo.setMaiden(c.getString(c.getColumnIndex(maiden)));
                    playerVo.setBatting_strike(c.getString(c.getColumnIndex(batting_strike)));

                    playerVo.setEconomy(c.getString(c.getColumnIndex(economy)));
                    playerVo.setNationality(c.getString(c.getColumnIndex(nationality)));
                    playerVo.setAge(c.getString(c.getColumnIndex(age)));
                    playerVo.setIsSelected(c.getString(c.getColumnIndex(isSelected)));
                    playerVo.setP_points(c.getString(c.getColumnIndex(P_POINTS)));
                    playerVo.setTotal_points(c.getString(c.getColumnIndex(TOTAL_P_pOINTS)));

                    playerList.add(playerVo);
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            c.close();
        }
        return playerList;
    }

    public List<PlayerProfileVO> getSelectedPlayers() {
        List<PlayerProfileVO> playerList = new ArrayList<PlayerProfileVO>();
        Cursor c = null;
        try {

			String query = "SELECT * FROM " + TABLE_SELECTED_PLAYERS ;

			SQLiteDatabase db = this.getReadableDatabase();
			c = db.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {

					PlayerProfileVO playerVo = new PlayerProfileVO();
					playerVo.setId(c.getInt(c.getColumnIndex(t_id))+"");
					playerVo.setPlayer_id(c.getString(c.getColumnIndex(player_id)));
					playerVo.setPlayer_name(c.getString(c.getColumnIndex(player_name)));
					playerVo.setTeam_name(c.getString(c.getColumnIndex(team_name)));
					playerVo.setTeam_id(c.getString(c.getColumnIndex(team_id)));
					playerVo.setAverage(c.getString(c.getColumnIndex(average)));
					playerVo.setPrice(c.getString(c.getColumnIndex(price)));
					playerVo.setIsCaptain(c.getString(c.getColumnIndex(isCaptain)));
					playerVo.setIsMom(c.getString(c.getColumnIndex(isMom)));
					playerVo.setIsIconic(c.getString(c.getColumnIndex(isIconic)));
					playerVo.setIsGoldengloves(c.getString(c.getColumnIndex(isGoldengloves)));
					playerVo.setIsPrurplecap(c.getString(c.getColumnIndex(isPrurplecap)));
					playerVo.setIsOrangecap(c.getString(c.getColumnIndex(isOrangecap)));

					playerVo.setIsSafety(c.getString(c.getColumnIndex(isSafety)));
					playerVo.setIsTeamSafety(c.getString(c.getColumnIndex(isTeamSafety)));
					playerVo.setPlayer_teamName(c.getString(c.getColumnIndex(player_teamName)));
					playerVo.setPlayer_category(c.getString(c.getColumnIndex(player_category)));
					playerVo.setPlayer_style(c.getString(c.getColumnIndex(player_style)));
					playerVo.setBowling_strike_rate(c.getString(c.getColumnIndex(bowling_strike_rate)));

					playerVo.setRole(c.getString(c.getColumnIndex(role)));
					playerVo.setBattingStyle(c.getString(c.getColumnIndex(battingStyle)));
					playerVo.setMatchesPlayed(c.getString(c.getColumnIndex(matchesPlayed)));
					playerVo.setInnings(c.getString(c.getColumnIndex(innings)));
					playerVo.setRuns(c.getString(c.getColumnIndex(runs)));
					playerVo.setHundreds(c.getString(c.getColumnIndex(hundreds)));


					playerVo.setThirties(c.getString(c.getColumnIndex(thirties)));
					playerVo.setOvers(c.getString(c.getColumnIndex(overs)));
					playerVo.setRun_conceded(c.getString(c.getColumnIndex(run_conceded)));
					playerVo.setWickets(c.getString(c.getColumnIndex(wickets)));
					playerVo.setMaiden(c.getString(c.getColumnIndex(maiden)));
					playerVo.setBatting_strike(c.getString(c.getColumnIndex(batting_strike)));

					playerVo.setEconomy(c.getString(c.getColumnIndex(economy)));
					playerVo.setNationality(c.getString(c.getColumnIndex(nationality)));
					playerVo.setAge(c.getString(c.getColumnIndex(age)));
					playerVo.setIsSelected(c.getString(c.getColumnIndex(isSelected)));

					playerList.add(playerVo);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			c.close();
		}
		return playerList;
	}


	public List<FixturesVO> getFixtures(){
		List<FixturesVO> playerList = new ArrayList<FixturesVO>();
		Cursor c = null ;
		try {

			String query = "SELECT * FROM " + TABLE_FIXTURES ;

			SQLiteDatabase db = this.getReadableDatabase();
			c = db.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {

					FixturesVO fixture = new FixturesVO();
					fixture.setId(c.getInt(c.getColumnIndex(ID))+"");
					fixture.setMatch(c.getString(c.getColumnIndex(MATCH)));
					fixture.setResult(c.getString(c.getColumnIndex(MATCH_RESULT)));
					fixture.setVenue_name(c.getString(c.getColumnIndex(VENUE)));
					fixture.setDate(c.getString(c.getColumnIndex(MATCH_DATE)));


					playerList.add(fixture);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			c.close();
		}
		return playerList;
	}

	public List<JsLocationsVO> getJsLocations(){
		List<JsLocationsVO> list = new ArrayList<JsLocationsVO>();
		Cursor c = null ;
		try {

			String query = "SELECT * FROM " + TABLE_JS_LOCATIONS ;

			SQLiteDatabase db = this.getReadableDatabase();
			c = db.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {

					JsLocationsVO location = new JsLocationsVO();
					location.setId(c.getInt(c.getColumnIndex(ID))+"");
					location.setLoc_name(c.getString(c.getColumnIndex(LOCATION_NAME)));
					location.setLatitude(c.getString(c.getColumnIndex(LATITUDE)));
					location.setLongitude(c.getString(c.getColumnIndex(LONGITUDE)));

					location.setAddress(c.getString(c.getColumnIndex(ADDRESS)));
					location.setArea(c.getString(c.getColumnIndex(AREA)));
					location.setCategory(c.getString(c.getColumnIndex(CATEGORY)));


					list.add(location);
				} while (c.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{

			if(c !=null)
				c.close();
		}
		return list;
	}
	public void deleteAll()
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+ TABLE_SELECTED_PLAYERS);
			db.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public void deleteAllFixtures()
	{
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			db.execSQL("delete from "+ TABLE_FIXTURES);
			db.close();
		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
