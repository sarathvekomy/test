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
/**
 * 
 */


import java.util.ArrayList;
import java.util.List;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.response.DeliveryNoteResponse;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author koteswararao
 * 
 */
public class CustmourAdapter extends ArrayAdapter<DeliveryNoteResponse>
{
	private final Context context;
	public ArrayList<DeliveryNoteResponse> mCustInfoList;
	LayoutInflater mInflater;
	int[] mBtnRadioArry;
	int intPosition;
	public int mSelectedPosition =-1;

	static class custInfoListDetails
	{		
		public RadioButton 	rbtnsno;
		public TextView 	tvBusinessName;
		public TextView 	tvCreditAmount;
		public TextView 	tvCustNum;
		public TextView 	tvAdvanceAmount;
		public TextView 	tvInvoiceName;
	}

	public CustmourAdapter(Context context, int resource,List<DeliveryNoteResponse> custInfoList)
	{
		super(context,resource, custInfoList);
		this.context = context;
		mCustInfoList = (ArrayList<DeliveryNoteResponse>) custInfoList;
		//mInflater = LayoutInflater.from(context);
		mBtnRadioArry	= new int[mCustInfoList.size()];
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount()
	{
		return mCustInfoList.size();
	}

	public DeliveryNoteResponse getItem(int position)
	{
		return mCustInfoList.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

    @Override
    public int getItemViewType(int position) {
        return position;
    }
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View rowView = convertView;
		custInfoListDetails viewHolder;
		DeliveryNoteResponse custmour = mCustInfoList.get(position);
		intPosition = getItemViewType(position);
		if (rowView == null)
		{
			rowView = mInflater.inflate(R.layout.customer_list_row, null);
			viewHolder = new custInfoListDetails();
			viewHolder.rbtnsno 			= (RadioButton) rowView.findViewById(R.id.rbtnsno);
			viewHolder.tvBusinessName 	= (TextView) rowView.findViewById(R.id.tvbname);
			viewHolder.tvCreditAmount 	= (TextView) rowView.findViewById(R.id.tvcredit);
			viewHolder.tvAdvanceAmount 	= (TextView) rowView.findViewById(R.id.tvadvance);
			viewHolder.tvInvoiceName 	= (TextView) rowView.findViewById(R.id.tvinvoicename);
			rowView.setTag(viewHolder);
			viewHolder.rbtnsno.setId(intPosition);
			viewHolder.rbtnsno.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for (int i = 0; i < mCustInfoList.size(); i++) {
                        if (i == v.getId()) {    
                        	mBtnRadioArry[i] = 1;
                        } else {
                        	mBtnRadioArry[i] = 0;
                        }
                    }
                    notifyDataSetChanged();
                }
            });
		}
		else
		{
			viewHolder = (custInfoListDetails) rowView.getTag();
		}		
		if(custmour!=null)
		{
			viewHolder.tvBusinessName.setText(custmour.getBusinessName());			
			viewHolder.tvCreditAmount.setText(Double.toString(custmour.getCreditAmount()));
			viewHolder.tvAdvanceAmount.setText(Double.toString(custmour.getAdvanceAmount()));
			viewHolder.tvInvoiceName.setText(custmour.getInvoiceName());
		}
		if (mBtnRadioArry[intPosition] == 1) {
        	mSelectedPosition=intPosition;
			viewHolder.rbtnsno.setChecked(true);
        } else {
        	viewHolder.rbtnsno.setChecked(false);
        }
		return rowView;
	}
}