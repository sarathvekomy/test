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
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
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
public class ProductsPreviewAdapter  extends ArrayAdapter<DeliveryNoteProduct>{

	private final Context				context;
	public List<DeliveryNoteProduct>	deliveryNoteProducts;
	LayoutInflater                      mInflater;
	public 	boolean 					isViewMode;
	DeliveryNoteProduct                	product;
	private TextView 					mlblAllProductTotalCost;
	private float 						oldBal;
	public 	float 						totalProductsPayable;
	
	static class DeliveryNoteProductsHolder
	{
		public TextView 	lblsno;
		public TextView 	lblProductName;
		public TextView 	lblbatchNumber;
		public EditText 	txProductQty;
		public EditText 	txbonusQty;
		public TextView 	lblProductCost;
		public TextView 	lblTotalCost;
		public TextView 	lblAvailableQty;
	}
	public ProductsPreviewAdapter(Context context, int resource,List<DeliveryNoteProduct> deliveryNoteProducts,boolean isViewMode,TextView lblTotalCost,float oldBal)
	{
		super(context,resource);
		this.context  = context;
		this.deliveryNoteProducts = deliveryNoteProducts;
		mlblAllProductTotalCost = lblTotalCost;
		this.isViewMode = isViewMode;
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.oldBal	=	oldBal;
	}
	public int getCount()
	{
		try
		{
			return deliveryNoteProducts.size();
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
			return deliveryNoteProducts.get(position);
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
			product = deliveryNoteProducts.get(position);
			
			final DeliveryNoteProductsHolder viewHolder;
			if (rowView == null)
			{
				rowView = mInflater.inflate(R.layout.sales_delivery_note_preview_row, null);
				viewHolder = new DeliveryNoteProductsHolder();
				viewHolder.lblsno 			= (TextView) rowView.findViewById(R.id.lblSno);
				viewHolder.lblProductName 	= (TextView) rowView.findViewById(R.id.lblProductNamePreview);
				viewHolder.lblbatchNumber 	= (TextView) rowView.findViewById(R.id.lblBatchNoPreview);
				viewHolder.txProductQty 	= (EditText) rowView.findViewById(R.id.txProductQtyPreview);
				viewHolder.txbonusQty 		= (EditText) rowView.findViewById(R.id.txbonusQtyPreview);
				viewHolder.lblProductCost	= (TextView) rowView.findViewById(R.id.lblproductCostPreview);
				viewHolder.lblTotalCost		= (TextView) rowView.findViewById(R.id.lbltotalCostPreview);
				viewHolder.lblAvailableQty 	= (TextView) rowView.findViewById(R.id.lblAvailableQty);
				if(isViewMode)
				{
					viewHolder.txProductQty.setEnabled(false);
					viewHolder.txProductQty.setBackgroundResource(R.color.table_bg);
					viewHolder.txbonusQty .setEnabled(false);
					viewHolder.txbonusQty.setBackgroundResource(R.color.table_bg);
				}
				else
				{
					viewHolder.txProductQty.setEnabled(true);
					viewHolder.txbonusQty .setEnabled(true);
					
					viewHolder.txProductQty.addTextChangedListener(new TextWatcher() 
					{
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {}				
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count,	int after) {}				
						@Override
						public void afterTextChanged(Editable entertext) {
							
							String strAvailableQty 	= viewHolder.lblAvailableQty.getText().toString().trim();
							int nAvailableQty 		= strAvailableQty.isEmpty()?0:Integer.parseInt(strAvailableQty);
							
							String strBonusQty 		= viewHolder.txbonusQty.getText().toString().trim();
							int nBonusQty 			= strBonusQty.isEmpty()?0:Integer.parseInt(strBonusQty);
							
							String strEnterQty 		= entertext.toString().trim();
							int nEnterQty			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
							
							if( nAvailableQty < nEnterQty + nBonusQty ){
								if(viewHolder.txProductQty.getText().toString().length() > 0)
									viewHolder.txProductQty.setText(viewHolder.txProductQty.getText().toString().substring(0, entertext.length()-1));
								viewHolder.txProductQty.setError("You have only "+nAvailableQty+" Products are available.");
							}
							float prodctCost 	= viewHolder.lblProductCost.getText().toString().trim().isEmpty()?0:Utils.getNumberFromMillion(viewHolder.lblProductCost.getText().toString().trim());					
							strEnterQty 		= viewHolder.txProductQty.getText().toString().trim();					
							nEnterQty 			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);					
							viewHolder.lblTotalCost.setText(Utils.currencyInMillion(prodctCost*nEnterQty));
							totalProductsPayable = oldBal;
							int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
							for (int i = 0; i < deliveryNoteProducts.size(); i++) {
								if(pos!=i)
									totalProductsPayable+=deliveryNoteProducts.get(i).getTotalCost();
							}
							totalProductsPayable += deliveryNoteProducts.get(pos).getProductCost()*nEnterQty;
							mlblAllProductTotalCost.setText(Utils.currencyInMillion(totalProductsPayable));
						}
					});
					viewHolder.txProductQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT ) {
								int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());					
								String val = viewHolder.txProductQty.getText().toString().trim();
								deliveryNoteProducts.get(pos).setProductQty(val.isEmpty()?0:Integer.parseInt(val));					
								val = viewHolder.lblTotalCost.getText().toString().trim();
								deliveryNoteProducts.get(pos).setTotalCost(val.isEmpty()?0:Utils.getNumberFromMillion(val));
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
							
							String strProductQty	= viewHolder.txProductQty.getText().toString().trim();
							int nProductQty			= strProductQty.isEmpty()?0:Integer.parseInt(strProductQty);
							
							String strEnterQty 		= entertext.toString().trim();
							int nEnterQty 			= strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
							
							if( nAvailableQty < nEnterQty + nProductQty){
								if(viewHolder.txbonusQty.getText().toString().length() > 0)
									viewHolder.txbonusQty.setText(viewHolder.txbonusQty.getText().toString().substring(0, entertext.length()-1));
								viewHolder.txbonusQty.setError("You have only "+nAvailableQty+" Products are available.");
							}
						}
					});
					viewHolder.txbonusQty.setOnEditorActionListener(new EditText.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
								int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());					
								String val = viewHolder.txbonusQty.getText().toString().trim();
								deliveryNoteProducts.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));
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
				viewHolder = (DeliveryNoteProductsHolder) rowView.getTag();
			}
			viewHolder.txProductQty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());					
						String val = viewHolder.txProductQty.getText().toString().trim();
						deliveryNoteProducts.get(pos).setProductQty(val.isEmpty()?0:Integer.parseInt(val));					
						val = viewHolder.lblTotalCost.getText().toString().trim();
						deliveryNoteProducts.get(pos).setTotalCost(val.isEmpty()?0:Utils.getNumberFromMillion(val));
					}
				}
			});
			viewHolder.txbonusQty.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if(!hasFocus){
						int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());					
						String val = viewHolder.txbonusQty.getText().toString().trim();
						deliveryNoteProducts.get(pos).setBonusQty(val.isEmpty()?0:Integer.parseInt(val));				
					}
				}
			});
			if(product!=null)
			{
				viewHolder.lblsno.setText(String.valueOf(position));
				viewHolder.lblAvailableQty.setText(String.valueOf(product.getAvailableQty()));
				viewHolder.lblProductName.setText(product.getProductName());
				viewHolder.lblbatchNumber.setText(product.getBatchNumber());
				viewHolder.txProductQty.setText(String.valueOf(product.getProductQty()));
				viewHolder.txbonusQty.setText(String.valueOf(product.getBonusQty()));
				viewHolder.lblProductCost.setText(Utils.currencyInMillion(product.getProductCost()));
				viewHolder.lblTotalCost.setText(Utils.currencyInMillion(product.getTotalCost()));
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
