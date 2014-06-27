/**
 * com.vekomy.vbooks.adapters.CustmourAdapter.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;

/**
 * @author koteswararao
 * 
 */
public class CustomerModifyAdapter extends ArrayAdapter<CustomerAmountInfo>
{
	private final Context context;
	public ArrayList<CustomerAmountInfo> mCustInfoList;
	LayoutInflater mInflater;
	public int[] mBtnRadioArry;
	int intPosition;
	public int mSelectedPosition =-1;
	View rowView;

	static class CustModifyInfoListDetails
	{		
		public RadioButton 	rbtnsno;
		public TextView 	tvBusinessName;
		public TextView 	tvInvoiceName;
		public ImageButton  imgbtnforward;
	
	}

	public CustomerModifyAdapter(Context context, int resource,List<CustomerAmountInfo> custInfoList)
	{
		super(context,resource, custInfoList);
		this.context = context;
		mCustInfoList = (ArrayList<CustomerAmountInfo>) custInfoList;
		mBtnRadioArry	= new int[mCustInfoList.size()];
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount()
	{
		try
		{
			return mCustInfoList.size();
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return 0;
	}

	public CustomerAmountInfo getItem(int position)
	{
		try
		{
			return mCustInfoList.get(position);
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return null;
	}

	public long getItemId(int position)
	{
		return position;
	}

    @Override
    public int getItemViewType(int position) 
    {
        return position;
    }
    
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		try
		{
			rowView = convertView;
			CustModifyInfoListDetails viewHolder;
			CustomerAmountInfo custmour = mCustInfoList.get(position);
			intPosition = getItemViewType(position);
			if (rowView == null)
			{
				rowView = mInflater.inflate(R.layout.customer_list_modify_row, null);
				viewHolder = new CustModifyInfoListDetails();
				viewHolder.imgbtnforward    = (ImageButton) rowView.findViewById(R.id.btnImgNext);
				viewHolder.tvBusinessName 	= (TextView) rowView.findViewById(R.id.lblBName);
				viewHolder.tvInvoiceName 	= (TextView) rowView.findViewById(R.id.lblInvoiceName);
				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (CustModifyInfoListDetails) rowView.getTag();
			}
			if(custmour!=null)
			{
				viewHolder.tvBusinessName.setText(custmour.getBusinessName());			
				viewHolder.tvInvoiceName.setText(custmour.getInvoiceName());
				custmour.setPos(position);
			}
			if (mBtnRadioArry[intPosition] == 1) 
			{
	        	rowView.setBackgroundResource(R.drawable.grid_border_yellow);
	        } 
			else 
			{
	        	rowView.setBackgroundResource(R.drawable.bluegradient);
	        }
			
			rowView.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					for (int i = 0; i < mCustInfoList.size(); i++) 
					{
	                    if (i == position) 
	                    {    
	                    	mBtnRadioArry[i] = 1;
	                    	rowView.setBackgroundResource(R.drawable.grid_border_yellow);
	                    
	                    } else 
	                    {
	                    	mBtnRadioArry[i] = 0;
	                    	rowView.setBackgroundResource(R.drawable.bluegradient);
	                    }
	                }
					mSelectedPosition = position;
	                notifyDataSetChanged();
				}
			});
			viewHolder.imgbtnforward.setTag(custmour);
			return rowView;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return convertView;
	}
}