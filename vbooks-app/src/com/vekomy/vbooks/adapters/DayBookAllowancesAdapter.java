/**
 * com.vekomy.vbooks.adapters.DayBookAllowancesAdapter.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 05-Sep-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.DayBookAllowances;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class DayBookAllowancesAdapter extends  ArrayAdapter<DayBookAllowances>{

	protected static final String LOG_TAG = DayBookAllowancesAdapter.class.getSimpleName();

	private List<DayBookAllowances> allowances;
	private Context context;
	LayoutInflater inflater;
	SalesDao salesDao;
	//private TextView lblTotalAllowances;
	private boolean isViewMode;
	//private boolean istmpSaveRequired;
	DayBookAllowances allowance;
	
	public static class AllowancesHolder 
	{
		TextView 	lblsno;
		TextView	lblAllowancesType;
		EditText	txAmt;
		EditText	txRemarks;
		ImageButton	imgbtnViewScanBill;
		ImageButton imgbtnAllowancesRemove;
	}
	
	public DayBookAllowancesAdapter(Context context, int layoutResourceId,List<DayBookAllowances> allowances,boolean isViewMode) 
	{
		super(context, layoutResourceId,allowances);
		this.context 	= context;
		this.allowances = allowances;
		inflater 		=	(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//this.lblTotalAllowances = lblTotalAllowances;
		salesDao 		= new SalesDao();
		this.isViewMode = isViewMode;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		try
		{
			View rowView = convertView;
			final AllowancesHolder viewHolder;
			allowance = allowances.get(position);
			if (rowView == null)
			{
				rowView = inflater.inflate(R.layout.daybook_allowances_row, null);
				viewHolder = new AllowancesHolder();
				viewHolder.lblsno 					= 	(TextView) rowView.findViewById(R.id.lblSno);
				viewHolder.lblAllowancesType		=	(TextView) rowView.findViewById(R.id.lblAllowancesType);
				viewHolder.txAmt					=	(EditText) rowView.findViewById(R.id.txAmt);			
				viewHolder.txRemarks				=	(EditText) rowView.findViewById(R.id.txRemarks);
				viewHolder.imgbtnViewScanBill		=	(ImageButton) rowView.findViewById(R.id.imgbtnViewScanBill);
				viewHolder.imgbtnAllowancesRemove	=	(ImageButton) rowView.findViewById(R.id.imgbtnAllowancesRemove);
				viewHolder.txAmt.setEnabled(false);
				viewHolder.txRemarks.setEnabled(false);
				viewHolder.txAmt.setBackgroundResource(R.drawable.txt_box_boarder);
				viewHolder.txRemarks.setBackgroundResource(R.drawable.txt_box_boarder);
				if( isViewMode )
				{
					viewHolder.imgbtnAllowancesRemove.setVisibility(View.GONE);
				}
				//if( isViewMode )
				//{
				//	viewHolder.txAmt.setBackgroundResource(R.drawable.txt_box_boarder);
				//	viewHolder.txRemarks.setBackgroundResource(R.drawable.txt_box_boarder);
				//	viewHolder.imgviewscan.setVisibility(View.GONE);
				//	viewHolder.imgbtnAllowancesRemove.setVisibility(View.GONE);
				//}
				//else
				//{
				//	 viewHolder.imgviewscan.setVisibility(View.VISIBLE);
			   	//	 viewHolder.imgbtnAllowancesRemove.setVisibility(istmpSaveRequired?View.VISIBLE:View.GONE);
				//}
				
				//viewHolder.imgviewscan.setOnClickListener(new OnClickListener() {
				//	@Override
				//	public void onClick(View v) 
				//	{
				//		if(viewHolder.txAmt.isEnabled())
				//		{
				//			int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
				//			float oldVal = allowances.get(pos).getAmt();
				//			float newVal = viewHolder.txAmt.getText().toString().isEmpty()? 0 : Utils.getNumberFromMillion(viewHolder.txAmt.getText().toString());
				//			lblTotalAllowances.setText(Utils.currencyInMillion(lblTotalAllowances.getText().toString().isEmpty()?0:Utils.getNumberFromMillion(lblTotalAllowances.getText().toString())+(newVal-oldVal)));
				//			allowances.get(pos).setAmt(viewHolder.txAmt.getText().toString().isEmpty()?0:Utils.getNumberFromMillion(viewHolder.txAmt.getText().toString()));
				//			viewHolder.txAmt.setText(Utils.currencyInMillion(newVal));
				//			if(viewHolder.lblAllowancesType.getText().toString().equals("Vehicle Fuel Expenses"))
				//			{
				//				int old=Integer.parseInt(allowances.get(pos).getRemarks());
				//				int new_value= viewHolder.txRemarks.getText().toString().isEmpty()?0:Integer.parseInt(viewHolder.txRemarks.getText().toString());
				//				if(new_value < old)
				//				{
				//					viewHolder.txRemarks.setError("Enter Higher than the given value("+new_value+")");
				//					viewHolder.txRemarks.setText(String.valueOf(old));
				//					viewHolder.txRemarks.requestFocus();
				//					return;
				//				}
				//				else
				//				{
				//					allowances.get(pos).setRemarks(viewHolder.txRemarks.getText().toString());
				//					if(istmpSaveRequired)
				//						salesDao.updateDayBookAllowance(VbookApp.getDbInstance(), allowances.get(pos));
				//				}
				//			}
				//			else
				//			{
				//				if(istmpSaveRequired)
				//					salesDao.updateDayBookAllowance(VbookApp.getDbInstance(), allowances.get(pos));
				//			}
				//			viewHolder.txAmt.setEnabled(false);
				//			viewHolder.txAmt.setBackgroundResource(R.drawable.txt_box_boarder);
				//			viewHolder.txRemarks.setEnabled(false);
				//			viewHolder.txRemarks.setBackgroundResource(R.drawable.txt_box_boarder);
				//			viewHolder.imgviewscan.setBackgroundResource(R.drawable.edit_icon);
				//		}
				//		else
				//		{
				//			viewHolder.txAmt.setEnabled(true);
				//			viewHolder.txAmt.setBackgroundResource(R.drawable.txt_box_boarder_yellow);
				//			if(viewHolder.lblAllowancesType.getText().toString().equals("OffLoading Charges")||viewHolder.lblAllowancesType.getText().toString().equals("Vehicle Fuel Expenses"))
				//				viewHolder.txRemarks.setEnabled(false);
				//			else
				//			{
				//				viewHolder.txRemarks.setEnabled(true);
				//				viewHolder.txRemarks.setBackgroundResource(R.drawable.txt_box_boarder_yellow);
				//			}
				//			viewHolder.imgviewscan.setBackgroundResource(R.drawable.update_icon);
				//			viewHolder.txAmt.requestFocus();
				//		}
				//	}
				//});*/
				rowView.setTag(viewHolder);			
			}
			else
			{
				viewHolder = (AllowancesHolder) rowView.getTag();
			}
			viewHolder.imgbtnViewScanBill.setTag(allowance);
			viewHolder.imgbtnAllowancesRemove.setTag(allowance);
			if(allowance != null)
			{
				viewHolder.lblsno.setText(String.valueOf(position));
				viewHolder.lblAllowancesType.setText(allowance.getAllowancesType());
				viewHolder.txAmt.setText(Utils.currencyInMillion(allowance.getAmt()));
				viewHolder.txRemarks.setText(allowance.getRemarks());
			}
			return rowView;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return convertView;
	}
}