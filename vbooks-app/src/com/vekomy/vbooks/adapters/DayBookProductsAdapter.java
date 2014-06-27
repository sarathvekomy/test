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
import com.vekomy.vbooks.app.request.DayBookProduct;

/**
 * @author nkr
 *
 */
public class DayBookProductsAdapter  extends ArrayAdapter<DayBookProduct>
{
	private final Context context;
	public List<DayBookProduct> mProductList;
	LayoutInflater mInflater;
	boolean isView;
	
	static class ProductsDetails
	{
		public TextView 	lblsno;
		public TextView 	lblProductName;
		public TextView 	lblBatchNo;
		public TextView 	lblAllocatedProducts;
		public TextView 	lblReturnQty;
		public TextView 	lblproductsToCustomer;
		public EditText 	txtProductsReturnToFactory;
		public TextView 	lblCloseingQty;
	}

	public DayBookProductsAdapter(Context context, int resource,List<DayBookProduct> productsInfoList,boolean isView)
	{
		super(context,resource, productsInfoList);
		this.context  = context;
		mProductList = productsInfoList;
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isView	=	isView;
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

	public DayBookProduct getItem(int position)
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
    public int getItemViewType(int position) {
        return position;
    }
    
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		try
		{
			View rowView = convertView;
			final ProductsDetails viewHolder;
			DayBookProduct product = mProductList.get(position);
			if (rowView == null)
			{
				rowView = mInflater.inflate(R.layout.daybook_products_row, null);
				viewHolder = new ProductsDetails();
				viewHolder.lblsno 				= (TextView) rowView.findViewById(R.id.lblDBSno);
				viewHolder.lblProductName 		= (TextView) rowView.findViewById(R.id.lblProductName);
				viewHolder.lblBatchNo			= (TextView) rowView.findViewById(R.id.lblBatchNo);
				viewHolder.lblAllocatedProducts	= (TextView) rowView.findViewById(R.id.lblAllocatedProducts);
				viewHolder.lblReturnQty 		= (TextView) rowView.findViewById(R.id.lblReturnQty);
				viewHolder.lblproductsToCustomer= (TextView) rowView.findViewById(R.id.lblproductsToCustomer);
				viewHolder.txtProductsReturnToFactory= (EditText) rowView.findViewById(R.id.txtProductsReturnToFactory);
				if(isView)
				{
					viewHolder.txtProductsReturnToFactory.setEnabled(false);
					viewHolder.txtProductsReturnToFactory.setBackgroundResource(R.color.table_bg);
				}
				viewHolder.lblCloseingQty		= (TextView) rowView.findViewById(R.id.lblCloseingQty);
				
				viewHolder.txtProductsReturnToFactory.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {					
					}				
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {									
					}				
					@Override
					public void afterTextChanged(Editable entertext) {
						
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						int nclosedQty 			= mProductList.get(pos).getCloseingStockTemp();
						
						String strEnterQty 		= entertext.toString().trim();
						int nEnterQty			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
						
						if( nclosedQty < nEnterQty){
							if(viewHolder.txtProductsReturnToFactory.getText().toString().length() > 0)
								viewHolder.txtProductsReturnToFactory.setText(viewHolder.txtProductsReturnToFactory.getText().toString().substring(0, entertext.length()-1));
						}
						strEnterQty 		= viewHolder.txtProductsReturnToFactory.getText().toString().trim();
						nEnterQty 			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);					
						viewHolder.lblCloseingQty.setText(String.valueOf(nclosedQty - nEnterQty));	
					}
				});
				
				viewHolder.txtProductsReturnToFactory.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
							int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
							String val 	= viewHolder.txtProductsReturnToFactory.getText().toString().trim();
							String val1 = viewHolder.lblCloseingQty.getText().toString().trim();
							mProductList.get(pos).setReturnFactoryStockQty(val.isEmpty()?0:Integer.parseInt(val));
							mProductList.get(pos).setCloseingStockQty((val1.isEmpty()?0:Integer.parseInt(val1)));
							}
						return false;
					}
				});
				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ProductsDetails) rowView.getTag();			
			}
			viewHolder.txtProductsReturnToFactory.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						String val 	= viewHolder.txtProductsReturnToFactory.getText().toString().trim();
						String val1 = viewHolder.lblCloseingQty.getText().toString().trim();
						mProductList.get(pos).setReturnFactoryStockQty(val.isEmpty()?0:Integer.parseInt(val));
						mProductList.get(pos).setCloseingStockQty((val1.isEmpty()?0:Integer.parseInt(val1)));
					}
				}
			});
			
			if(product!=null)
			{
				viewHolder.lblsno.setText(String.valueOf(position));
				viewHolder.lblProductName.setText(product.getProductName());
				viewHolder.lblBatchNo.setText(product.getBatchNumber());
				viewHolder.lblAllocatedProducts.setText(String.valueOf(product.getOpeningStockQty()));
				viewHolder.lblReturnQty.setText(String.valueOf(product.getSalesReturnStockQty()));
				viewHolder.lblproductsToCustomer.setText(String.valueOf(mProductList.get(position).getSoldStockQty()));
				viewHolder.txtProductsReturnToFactory.setText(String.valueOf(mProductList.get(position).getReturnFactoryStockQty()));
				viewHolder.lblCloseingQty.setText(String.valueOf(mProductList.get(position).getCloseingStockQty()));
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
