package com.psl.classes;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.psl.fantasy.league.season2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FixturesAdapter extends ArrayAdapter<FixturesVO>{

	Context context;
	List<FixturesVO> list;
	String city_id = "";
	String sector_id = "";
	ArrayList<FixturesVO> filterList ;
	String from_where_to_call = "";

	public FixturesAdapter(Context context, List<FixturesVO> list, String call){
		super(context, R.layout.custom_fixture_row,list);
		this.list = list ;
		this.context = context;
		this.sector_id = sector_id;
		this.from_where_to_call = call;
		this.filterList = new ArrayList<FixturesVO>();
		this.filterList.addAll(list);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		try {
			if (view == null) {
				LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				if(from_where_to_call.equalsIgnoreCase("history"))

					view = inflator.inflate(R.layout.custom_fixture_row_history, parent,false);
				else
					view = inflator.inflate(R.layout.custom_fixture_row, parent,false);
			}

			TextView tvdate = (TextView)view.findViewById(R.id.tv_date);
			TextView tvTeams = (TextView)view.findViewById(R.id.tv_match);
			TextView tvVenue = (TextView)view.findViewById(R.id.tv_venue);

			LinearLayout parentLayout = (LinearLayout)view.findViewById(R.id.row);
			if(from_where_to_call.equalsIgnoreCase("history"))
			{
				parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_new));
				tvdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
				tvTeams.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
				tvVenue.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
			}else {
				if ((position % 2) == 0) {
					parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_without_arrow));
				} else {
					parentLayout.setBackground(context.getResources().getDrawable(R.drawable.fixture_row_without_arrow));
				}
			}


			tvdate.setText(list.get(position).getDate().split(" ")[0] + " " + list.get(position).getDate().split(" ")[1] + " " + list.get(position).getDate().split(" ")[2] );
			tvTeams.setText(list.get(position).getMatch().replace("<br/>", "").replace("Sultan", "").replace("Zalmi", "").replace("kings", "").replace("Qalanders", "").replace("United", "").replace("Gladiators", ""));
			tvVenue.setText(list.get(position).getVenue_name().replace("Cricket Stadium", "").replace("International", ""));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	String getDate(String date){

		String format = "";
		try {
			if (date.contains("/")) {
				int selectedYear = Integer.parseInt(date.split("/")[2]);
				int selectedDay = Integer.parseInt(date.split("/")[1]);
				int selectedMonth = (Integer.parseInt(date.split("/")[0])-1);

				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.YEAR, selectedYear);
				cal.set(Calendar.DAY_OF_MONTH, selectedDay);
				cal.set(Calendar.MONTH, selectedMonth);
				//format = new SimpleDateFormat("EEEE, dd MMM, yyyy").format(cal.getTime());
				format = new SimpleDateFormat("dd MMM, yyyy").format(cal.getTime());
			}else
			{
				format = date.replace(" ", ", ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return format;
	}

//	public void filter(String charText, String type) {
//		charText = charText.toLowerCase(Locale.getDefault());
//		list.clear();
//		if (charText.equalsIgnoreCase("ALL")) {
//			list.addAll(list);
//		}
//		else
//		{
//			for (ProjectVO vo : filterList)
//			{
//				if(type.equalsIgnoreCase("sector"))
//				{
//					if (vo.getCity().
//							toLowerCase(Locale.getDefault()).contains(charText))
//					{
//						list.add(vo);
//					}
//				}else
//				{
//					if (vo.getsName()
//							.toLowerCase(Locale.getDefault()).contains(charText))
//					{
//						list.add(vo);
//					}
//				}
//
//			}
//		}
//		notifyDataSetChanged();
//	}
}
