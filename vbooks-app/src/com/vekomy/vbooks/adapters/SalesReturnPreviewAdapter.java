/**
 * com.vekomy.vbooks.adapters.ProductsAdapter.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 23-Jul-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.adapters;

import java.util.List;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.SalesReturnProduct;

/**
 * @author nkr
 *
 */
public class SalesReturnPreviewAdapter  extends ArrayAdapter<SalesReturnProduct>
{
	private final Context context;
	public List<SalesReturnProduct> mProductList;
	LayoutInflater mInflater;
	private boolean isViewMode;
	
	static class ProductsDetails
	{
		public TextView 	lblsno;
		public TextView 	lblProductName;
		public TextView 	lblBatchNo;
		public EditText 	txtDamagedQty;
		public EditText 	txtReSaleQty;
		public TextView 	lblTotalQty;
		public EditText 	txRemarks;
	}

	public SalesReturnPreviewAdapter(Context context, int resource,List<SalesReturnProduct> productsInfoList,boolean isView)
	{
		super(context,resource, productsInfoList);
		this.context  	= 	context;
		mProductList 	= 	productsInfoList;
		mInflater 		=	(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isViewMode	=	isView;
	}
	public int getCount()
	{
		try
		{
			return mProductList.size();
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return 0;
	}

	public SalesReturnProduct getItem(int position)
	{
		try
		{
			return mProductList.get(position);
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
			View rowView = convertView;
			final ProductsDetails viewHolder;
			SalesReturnProduct product = mProductList.get(position);
			if (rowView == null)
			{
				rowView = mInflater.inflate(R.layout.sales_returns_products_preview_row, null);
				viewHolder = new ProductsDetails();
				viewHolder.lblsno 			= (TextView) rowView.findViewById(R.id.lblSno);
				viewHolder.lblProductName 	= (TextView) rowView.findViewById(R.id.lblProductName);
				viewHolder.lblBatchNo		= (TextView) rowView.findViewById(R.id.lblBatchNo);
				viewHolder.txtDamagedQty	= (EditText) rowView.findViewById(R.id.txtDamagedQty);
				viewHolder.txtReSaleQty		= (EditText) rowView.findViewById(R.id.txtReSaleQty);
				viewHolder.lblTotalQty 		= (TextView) rowView.findViewById(R.id.lblTotalQty);
				viewHolder.txRemarks		= (EditText) rowView.findViewById(R.id.txRemarks);
				
				if(isViewMode)
				{
					viewHolder.txtDamagedQty.setEnabled(false);
					viewHolder.txtDamagedQty.setBackgroundResource(R.color.table_bg);
					viewHolder.txtReSaleQty.setEnabled(false);
					viewHolder.txtReSaleQty.setBackgroundResource(R.color.table_bg);
					viewHolder.txRemarks.setEnabled(false);
					viewHolder.txRemarks.setBackgroundResource(R.color.table_bg);
				}
				else
				{
					viewHolder.txtDamagedQty.addTextChangedListener(new TextWatcher() 
					{
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}				
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
						@Override
						public void afterTextChanged(Editable entertext) {
							String strDamagedQty 	= viewHolder.txtDamagedQty.getText().toString().trim();					
							int nDamagedQty 		= strDamagedQty.isEmpty()?0:Integer.parseInt(strDamagedQty);						
							String strResaleQty 	= viewHolder.txtReSaleQty.getText().toString().trim();
							int nResaleQty 			= strResaleQty.isEmpty()?0:Integer.parseInt(strResaleQty);				
							viewHolder.lblTotalQty.setText(String.valueOf(Math.round(nDamagedQty+nResaleQty)));
						}
					});
					viewHolder.txtDamagedQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
								int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
								String val = viewHolder.txtDamagedQty.getText().toString().trim();
								mProductList.get(pos).setDamagedQty(val.isEmpty()?0:Integer.parseInt(val));
								mProductList.get(pos).setTotalQty(viewHolder.lblTotalQty.getText().toString().isEmpty()?0:Integer.parseInt(viewHolder.lblTotalQty.getText().toString()));
							}
							return false;
						}
					});					
					viewHolder.txtReSaleQty.addTextChangedListener(new TextWatcher() 
					{
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}
						
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}
						
						@Override
						public void afterTextChanged(Editable entertext) {
							
							String strDamagedQty 	= viewHolder.txtDamagedQty.getText().toString().trim();					
							int nDamagedQty 		= strDamagedQty.isEmpty()?0:Integer.parseInt(strDamagedQty);
							
							String strResaleQty 	= viewHolder.txtReSaleQty.getText().toString().trim();
							int nResaleQty 			= strResaleQty.isEmpty()?0:Integer.parseInt(strResaleQty);
							
							viewHolder.lblTotalQty.setText(String.valueOf(Math.round(nDamagedQty+nResaleQty)));
						}
					});
					viewHolder.txtReSaleQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
								int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
								String val = viewHolder.txtReSaleQty.getText().toString().trim();
								mProductList.get(pos).setResaleQty(val.isEmpty()?0:Integer.parseInt(val));	
								mProductList.get(pos).setTotalQty(viewHolder.lblTotalQty.getText().toString().isEmpty()?0:Integer.parseInt(viewHolder.lblTotalQty.getText().toString()));								
							}
							return false;
						}
					});	
					viewHolder.txRemarks.setOnEditorActionListener(new EditText.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
								int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());									
								mProductList.get(pos).setBonusReason(viewHolder.txRemarks.getText().toString());
							}
							return false;
						}
					});	
					viewHolder.lblsno.setTag(position);
				}
				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ProductsDetails) rowView.getTag();			
			}
			viewHolder.txtDamagedQty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						String val = viewHolder.txtDamagedQty.getText().toString().trim();
						mProductList.get(pos).setDamagedQty(val.isEmpty()?0:Integer.parseInt(val));
						mProductList.get(pos).setTotalQty(viewHolder.lblTotalQty.getText().toString().isEmpty()?0:Integer.parseInt(viewHolder.lblTotalQty.getText().toString()));
					}
				}
			});
			viewHolder.txtReSaleQty.setOnFocusChangeListener(new OnFocusChangeListener() {			
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						String val = viewHolder.txtReSaleQty.getText().toString().trim();
						mProductList.get(pos).setResaleQty(val.isEmpty()?0:Integer.parseInt(val));	
						mProductList.get(pos).setTotalQty(viewHolder.lblTotalQty.getText().toString().isEmpty()?0:Integer.parseInt(viewHolder.lblTotalQty.getText().toString()));
					}
				}
			});
			viewHolder.txRemarks.setOnFocusChangeListener(new OnFocusChangeListener() {			
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());									
						mProductList.get(pos).setBonusReason(viewHolder.txRemarks.getText().toString());				
					}
				}
			});
			if(product!=null)
			{
				viewHolder.lblsno.setText(String.valueOf(position));
				viewHolder.lblProductName.setText(product.getProductName());
				viewHolder.lblBatchNo.setText(product.getBatchNumber());
				viewHolder.txtReSaleQty.setText(String.valueOf(product.getResaleQty()));
				viewHolder.txtDamagedQty.setText(String.valueOf(product.getDamagedQty()));
				viewHolder.lblTotalQty.setText(String.valueOf(product.getTotalQty()));
				viewHolder.txRemarks.setText(mProductList.get(position).getBonusReason());
			}
			return rowView;
		}
		catch(Exception e)
		{
			Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return convertView;
	}

	public void refresh()
	{
		notifyDataSetChanged();
	}
}
