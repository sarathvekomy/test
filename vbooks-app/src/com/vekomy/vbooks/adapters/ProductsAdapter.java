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
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.app.request.DeliveryNoteProduct;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author nkr
 *
 */
public class ProductsAdapter  extends ArrayAdapter<DeliveryNoteProduct>
{
	private final Context context;
	public List<DeliveryNoteProduct> mProductList;
	LayoutInflater mInflater;
	int intPosition;
	public int mSelectedPosition =-1;
	private TextView mlblAllProductTotalCost;

	static class ProductsDetails
	{
		public TextView 	lblsno;
		public TextView 	lblProductName;
		public TextView 	lblBatchNo;
		public TextView 	lblAvailableQty;
		public TextView 	lblProductCost;
		public EditText 	txProductQty;
		public EditText 	txbonusQty;
		public TextView 	lblTotalCost;
		public EditText 	txbonusReason;
	}

	public ProductsAdapter(Context context, int resource,List<DeliveryNoteProduct> productsInfoList, TextView lblTotalCost)
	{
		super(context,resource, productsInfoList);
		this.context  = context;
		mlblAllProductTotalCost = lblTotalCost;
		mProductList = productsInfoList;
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
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

	public DeliveryNoteProduct getItem(int position)
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
			DeliveryNoteProduct product = mProductList.get(position);
			intPosition = getItemViewType(position);
			if (rowView == null)
			{
				rowView = mInflater.inflate(R.layout.sales_delivery_note_product_row, null);
				viewHolder = new ProductsDetails();
				viewHolder.lblsno 			= (TextView) rowView.findViewById(R.id.lblSno);
				viewHolder.lblProductName 	= (TextView) rowView.findViewById(R.id.lblProductName);
				viewHolder.lblBatchNo		= (TextView) rowView.findViewById(R.id.lblBatchNo);
				viewHolder.lblAvailableQty 	= (TextView) rowView.findViewById(R.id.lblAvailableQty);
				viewHolder.lblProductCost 	= (TextView) rowView.findViewById(R.id.lblProductCost);
				viewHolder.txProductQty 	= (EditText) rowView.findViewById(R.id.txProductQty);
				viewHolder.txbonusQty		= (EditText) rowView.findViewById(R.id.txbonusQty);
				viewHolder.lblTotalCost		= (TextView) rowView.findViewById(R.id.lblTotalCost);
				viewHolder.txbonusReason	= (EditText) rowView.findViewById(R.id.txbonusReason);
				viewHolder.txProductQty.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {					
					}				
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {									
					}				
					@Override
					public void afterTextChanged(Editable entertext) {
						
						String strAvailableQty 	= viewHolder.lblAvailableQty.getText().toString().trim();					
						int nAvailableQty 		= strAvailableQty.isEmpty()?0:Integer.parseInt(strAvailableQty);
						
						String strBonusQty 		= viewHolder.txbonusQty.getText().toString().trim();
						int nBonusQty 			= strBonusQty.isEmpty()?0:Integer.parseInt(strBonusQty);
						
						String strEnterQty 		= entertext.toString().trim();
						int nEnterQty			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
						
						if( nAvailableQty-nBonusQty < nEnterQty){
							if(viewHolder.txProductQty.getText().toString().length() > 0)
								viewHolder.txProductQty.setText(viewHolder.txProductQty.getText().toString().substring(0, entertext.length()-1));
						}
						float	prodctCost = Utils.getNumberFromMillion(viewHolder.lblProductCost.getText().toString().trim());
											
						strEnterQty 		= viewHolder.txProductQty.getText().toString().trim();					
						nEnterQty 			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);					
						viewHolder.lblTotalCost.setText(Utils.currencyInMillion(prodctCost*nEnterQty));
						float totalCost = 0;
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						for (int i = 0; i < mProductList.size(); i++) {
							if(pos!=i)
								totalCost+=mProductList.get(i).getTotalCost();
						}
						
						mlblAllProductTotalCost.setText(Utils.currencyInMillion(totalCost+prodctCost*nEnterQty));	
					}
				});
				viewHolder.txProductQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
							int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
							String val = viewHolder.txProductQty.getText().toString().trim();
							mProductList.get(pos).setProductQty(val.isEmpty()?0:Integer.parseInt(val));
							float change = Utils.getNumberFromMillion(viewHolder.lblTotalCost.getText().toString().trim());
							mProductList.get(pos).setTotalCost(val.isEmpty()?0:change);
							viewHolder.txProductQty.setNextFocusDownId(viewHolder.txbonusQty.getId());			
						}
						return false;
					}
				});
				viewHolder.txbonusQty.addTextChangedListener(new TextWatcher() 
				{
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {	
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count,	int after) {										
					}
					
					@Override
					public void afterTextChanged(Editable entertext) {
						
						String strAvailableQty 	= viewHolder.lblAvailableQty.getText().toString().trim();					
						int nAvailableQty 		= strAvailableQty.isEmpty()?0:Integer.parseInt(strAvailableQty);
						
						String strEnterQty		= viewHolder.txProductQty.getText().toString().trim();
						int nEnterQty			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
						
						String strBonusQty 		= entertext.toString().trim();
						int nBonusQty 			= strBonusQty.isEmpty()?0:Integer.parseInt(strBonusQty);
						
						if( nAvailableQty-nEnterQty < nBonusQty){
							if(viewHolder.txbonusQty.getText().toString().length() > 0)
								viewHolder.txbonusQty.setText(viewHolder.txbonusQty.getText().toString().substring(0, entertext.length()-1));
						}
					}
				});
				viewHolder.txbonusQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
							int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());							
							String val = viewHolder.txbonusQty.getText().toString().trim();
							mProductList.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));
							//viewHolder.txbonusQty.setNextFocusDownId(viewHolder.txbonusReason.getId());
						}
						return false;
					}
				});
				/*viewHolder.txbonusReason.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
							int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());									
							mProductList.get(pos).setBonusReason(viewHolder.txbonusReason.getText().toString());
						}
						return false;
					}
				});*/
				viewHolder.txProductQty.setTag(position);
				rowView.setTag(viewHolder);
			}
			else
			{
				viewHolder = (ProductsDetails) rowView.getTag();			
			}
			viewHolder.txProductQty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						
						String val = viewHolder.txProductQty.getText().toString().trim();
						mProductList.get(pos).setProductQty(val.isEmpty()?0:Integer.parseInt(val));
						float change = Utils.getNumberFromMillion(viewHolder.lblTotalCost.getText().toString().trim());
						mProductList.get(pos).setTotalCost(val.isEmpty()?0:change);
					}
				}
			});
			viewHolder.txbonusQty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						
						String val = viewHolder.txbonusQty.getText().toString().trim();
						mProductList.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));				
					}
				}
			});
			viewHolder.txbonusQty.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_DEL){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
						String val = viewHolder.txbonusQty.getText().toString().trim();
						mProductList.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));
					}
					return false;
				}
			});
			viewHolder.txProductQty.setOnKeyListener(new OnKeyListener() {
							
							@Override
							public boolean onKey(View v, int keyCode, KeyEvent event) {
								if(keyCode == KeyEvent.KEYCODE_DEL){
									int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
									String val = viewHolder.txbonusQty.getText().toString().trim();
									mProductList.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));
								}
								return false;
				}
			});

			viewHolder.txbonusReason.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());									
						mProductList.get(pos).setBonusReason(viewHolder.txbonusReason.getText().toString());				
					}else{
						/*if(viewHolder.txbonusQty.getText().toString().isEmpty()){
							viewHolder.txbonusReason.setError("Bonus Qty is Empty");
							viewHolder.txbonusReason.setText("");	
						}*/
						viewHolder.txbonusReason.requestFocus();
					}
				}
			});
			
			if(product!=null)
			{
				viewHolder.lblsno.setText(String.valueOf(position));			
				viewHolder.lblProductName.setText(product.getProductName());
				viewHolder.lblBatchNo.setText(product.getBatchNumber());
				viewHolder.lblAvailableQty.setText(String.valueOf(product.getAvailableQty()));
				viewHolder.lblProductCost.setText(Utils.currencyInMillion(product.getProductCost()));
				viewHolder.lblTotalCost.setText(Utils.currencyInMillion(mProductList.get(position).getTotalCost()));
				viewHolder.txProductQty.setText(mProductList.get(position).getProductQty()==0?"":String.valueOf(mProductList.get(position).getProductQty()));
				viewHolder.txbonusQty.setText(mProductList.get(position).getBonusQty()==0?"":String.valueOf(mProductList.get(position).getBonusQty()));
				viewHolder.txbonusReason.setText(mProductList.get(position).getBonusReason());
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
