package com.vekomy.vbooks.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.response.TrxnHistory;
import com.vekomy.vbooks.utils.Utils;

public class TrxnHistoryAdapter extends ArrayAdapter<TrxnHistory>  
{
	static class custInfoListDetails
	{
		public TextView 	lblNo;
		public TextView 	lblDate;
		public TextView 	lblName;
		public TextView 	lblBal;
		
		public TextView 	tvNo;
		public TextView 	tvNocolen;
		public TextView 	tvDate;
		public TextView 	tvDatecolen;
		public TextView 	tvName;
		public TextView 	tvNamecolen;
		public TextView 	tvBal;
		public TextView 	tvBalcolen;
	}
	
	private final Context context;
	public List<TrxnHistory> alltrxnViewList;
	List<TrxnHistory> dntrxnViewList;
	List<TrxnHistory> srtrxnViewList;
	List<TrxnHistory> jntrxnViewList;
	List<TrxnHistory> dbtrxnViewList;
	public List<TrxnHistory> filteredList;
	LayoutInflater trxnInflater;
	
	public TrxnHistoryAdapter(Context context, int viewTrxnDisplayRow,List<TrxnHistory> result) 
	{
		super(context, viewTrxnDisplayRow,result);
		this.context	=	context;
		alltrxnViewList	= 	(ArrayList<TrxnHistory>) result;
		trxnInflater 	=	(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		filteredList 	= 	new ArrayList<TrxnHistory>();
		filteredList.addAll(alltrxnViewList);
		dntrxnViewList	= 	new ArrayList<TrxnHistory>(); 
		srtrxnViewList	= 	new ArrayList<TrxnHistory>();
		jntrxnViewList	= 	new ArrayList<TrxnHistory>();
		dbtrxnViewList	= 	new ArrayList<TrxnHistory>();
		for (int i = 0; i < alltrxnViewList.size(); i++) 
		{
			if(alltrxnViewList.get(i).getId().startsWith(Constants.DN))
				dntrxnViewList.add(alltrxnViewList.get(i));
		}
		for (int i = 0; i < alltrxnViewList.size(); i++) 
		{
			if(alltrxnViewList.get(i).getId().startsWith(Constants.SR))
				srtrxnViewList.add(alltrxnViewList.get(i));
		}
		for (int i = 0; i < alltrxnViewList.size(); i++) 
		{
			if(alltrxnViewList.get(i).getId().startsWith(Constants.JN))
				jntrxnViewList.add(alltrxnViewList.get(i));
		}
		for (int i = 0; i < alltrxnViewList.size(); i++) 
		{
			if(alltrxnViewList.get(i).getId().startsWith(Constants.DB))
				dbtrxnViewList.add(alltrxnViewList.get(i));
		}
	}

	public void reloadAdapter(List<TrxnHistory> result) 
	{
		try
		{
			alltrxnViewList.clear();
			alltrxnViewList.addAll(result);
			dntrxnViewList.clear();
			srtrxnViewList.clear();
			jntrxnViewList.clear();
			dbtrxnViewList.clear();
			for (int i = 0; i < alltrxnViewList.size(); i++) 
			{
				if(alltrxnViewList.get(i).getId().startsWith(Constants.DN))
					dntrxnViewList.add(alltrxnViewList.get(i));
			}
			for (int i = 0; i < alltrxnViewList.size(); i++) 
			{
				if(alltrxnViewList.get(i).getId().startsWith(Constants.SR))
					srtrxnViewList.add(alltrxnViewList.get(i));
			}
			for (int i = 0; i < alltrxnViewList.size(); i++) 
			{
				if(alltrxnViewList.get(i).getId().startsWith(Constants.JN))
					jntrxnViewList.add(alltrxnViewList.get(i));
			}
			for (int i = 0; i < alltrxnViewList.size(); i++) 
			{
				if(alltrxnViewList.get(i).getId().startsWith(Constants.DB))
					dbtrxnViewList.add(alltrxnViewList.get(i));
			}
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public int getCount() 
	{
		try
		{
			return filteredList.size();
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return 0;
	}

	@Override
	public TrxnHistory getItem(int position) 
	{
		try
		{
			return filteredList.get(position);
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return null;
	}


	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		try
		{
			View rowView = convertView;
			custInfoListDetails viewHolder;
			TrxnHistory trxn = filteredList.get(position);
			if (rowView == null)
			{
				rowView = trxnInflater.inflate(R.layout.trxn_history_row, null);
				viewHolder = new custInfoListDetails();
				viewHolder.lblNo 	= (TextView) rowView.findViewById(R.id.lblNo);
				viewHolder.lblDate 	= (TextView) rowView.findViewById(R.id.lblDate);
				viewHolder.lblName 	= (TextView) rowView.findViewById(R.id.lblName);
				viewHolder.lblBal 	= (TextView) rowView.findViewById(R.id.lblBal);	
				
				viewHolder.tvNo			= (TextView) rowView.findViewById(R.id.tvNo);
				viewHolder.tvNocolen	= (TextView) rowView.findViewById(R.id.tvNocolen);
				viewHolder.tvDate		= (TextView) rowView.findViewById(R.id.tvDate);
				viewHolder.tvDatecolen	= (TextView) rowView.findViewById(R.id.tvDatecolen);
				viewHolder.tvName		= (TextView) rowView.findViewById(R.id.tvName);
				viewHolder.tvNamecolen	= (TextView) rowView.findViewById(R.id.tvNamecolen);
				viewHolder.tvBal		= (TextView) rowView.findViewById(R.id.tvBal);
				viewHolder.tvBalcolen	= (TextView) rowView.findViewById(R.id.tvBalcolen);

				rowView.setTag(viewHolder);			
			}
			else
			{
				viewHolder = (custInfoListDetails) rowView.getTag();
			}		
			if(trxn != null)
			{
				viewHolder.lblNo.setText(trxn.getId());			
				viewHolder.lblDate.setText(trxn.getDate());
				viewHolder.lblName.setText(trxn.getName());
				if(trxn.getBal().startsWith("-")){
					viewHolder.lblBal.setText(Utils.currencyInMillion(Float.parseFloat(trxn.getBal())).replace("-", context.getResources().getString(R.string.adv)));	
				}
				else
					viewHolder.lblBal.setText(Utils.currencyInMillion(Float.parseFloat(trxn.getBal())));	
				
				if(viewHolder.lblNo.getText().toString().endsWith(Constants.CR))
				{
					/*viewHolder.lblNo.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.lblDate.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.lblName.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.lblBal.setBackgroundColor(Color.parseColor("#fff200"));
					
					viewHolder.tvNo.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvNocolen.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvDate.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvDatecolen.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvName.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvNamecolen.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvBal.setBackgroundColor(Color.parseColor("#fff200"));
					viewHolder.tvBalcolen.setBackgroundColor(Color.parseColor("#fff200"));*/
					//rowView.setBackgroundColor(Color.parseColor("#fff200"));
					
					//viewHolder.lblNo.setBackgroundColor(Color.parseColor("#2183b0"));
					
					rowView.setBackgroundResource(R.drawable.grid_border_yellow_thick);
				}
				else
				{
					/*viewHolder.lblNo.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.lblDate.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.lblName.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.lblBal.setBackgroundColor(Color.parseColor("#f4f2f3"));
					
					viewHolder.tvNo.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvNocolen.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvDate.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvDatecolen.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvName.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvNamecolen.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvBal.setBackgroundColor(Color.parseColor("#f4f2f3"));
					viewHolder.tvBalcolen.setBackgroundColor(Color.parseColor("#f4f2f3"));
					rowView.setBackgroundColor(Color.parseColor("#f4f2f3"));*/
					
					rowView.setBackgroundResource(R.drawable.grid_border_blue);
					
				}
				
			}
			return rowView;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return convertView;
	}
	
	public void filter(String trxnType,String trxnDate,String trxnName) 
	{
		try
		{
			filteredList.clear();
			if(trxnType.equals(Constants.DELIVERY_NOTE))
				filteredList.addAll(dntrxnViewList);
			else if(trxnType.equals(Constants.SALES_RETURN))
				filteredList.addAll(srtrxnViewList);
			else if(trxnType.equals(Constants.JOURNAL))
				filteredList.addAll(jntrxnViewList);
			else if(trxnType.equals(Constants.DAY_BOOK))
				filteredList.addAll(dbtrxnViewList);
			else
				filteredList.addAll(alltrxnViewList);
			
			if(!trxnDate.equals(Constants.VIEW_DATE))
				filterDateWise(filteredList,trxnDate);
			
			if(!trxnName.equals(Constants.VIEW_NAME))
				filterNamewise(filteredList,trxnName);
			notifyDataSetChanged();		
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private List<TrxnHistory>  filterDateWise(List<TrxnHistory> trxnList,String trxnDate) 
	{
		try
		{
			for (Iterator<TrxnHistory> iterator = trxnList.iterator(); iterator.hasNext();) 
			{
				if(!iterator.next().getDateID().equals(trxnDate))
					iterator.remove();
			}
			return trxnList;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return trxnList;
	}
	private List<TrxnHistory>  filterNamewise(List<TrxnHistory> trxnList,String trxnName) 
	{
		try
		{
			for (Iterator<TrxnHistory> iterator = trxnList.iterator(); iterator.hasNext();) 
			{
				if(!iterator.next().getName().equals(trxnName))
					iterator.remove();
			}
			return trxnList;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return trxnList;
	}	
	public void refresh()
	{
		notifyDataSetChanged();
	}
}
