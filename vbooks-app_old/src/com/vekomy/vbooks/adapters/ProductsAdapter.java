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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.helpers.ProductsView;

/**
 * @author nkr
 *
 */
public class ProductsAdapter  extends ArrayAdapter<ProductsView>{

	private final Context context;
	public List<ProductsView> mProductList;
	LayoutInflater mInflater;
	int intPosition;
	public int mSelectedPosition =-1;
	private TextView mlblAllProductTotalCost;
	static class ProductsDetails
	{
		public TextView 	lblsno;
		public TextView 	lblProductName;
		public TextView 	lblAvailableQty;
		public TextView 	lblProductCost;
		public EditText 	txProductQty;
		public EditText 	txbonusQty;
		public TextView 	lblTotalCost;
		public EditText 	txbonusReason;
	}

	public ProductsAdapter(Context context, int resource,List<ProductsView> productsInfoList, TextView lblTotalCost)
	{
		super(context,resource, productsInfoList);
		this.context  = context;
		mlblAllProductTotalCost = lblTotalCost;
		mProductList = productsInfoList;
		mInflater =(LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	public int getCount()
	{
		return mProductList.size();
	}

	public ProductsView getItem(int position)
	{
		return mProductList.get(position);
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
		View rowView = convertView;
		final ProductsDetails viewHolder;
		ProductsView product = mProductList.get(position);
		intPosition = getItemViewType(position);
		if (rowView == null)
		{
			rowView = mInflater.inflate(R.layout.delivery_note_product_row, null);
			viewHolder = new ProductsDetails();
			viewHolder.lblsno 			= (TextView) rowView.findViewById(R.id.lblSno);
			viewHolder.lblProductName 	= (TextView) rowView.findViewById(R.id.lblProductName);
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
					  
					float oldTotalCost 		= (float) (viewHolder.lblTotalCost.getText().toString().trim().isEmpty()?0.0:Float.parseFloat(viewHolder.lblTotalCost.getText().toString().trim()));
					
					if( nAvailableQty-nBonusQty < nEnterQty){
						viewHolder.txProductQty.setText(viewHolder.txProductQty.getText().toString().substring(0, entertext.length()-1));
					}else{
						float prodctCost = Float.parseFloat(viewHolder.lblProductCost.getText().toString().trim());
						viewHolder.lblTotalCost.setText(String.valueOf(prodctCost*nEnterQty));
						float newTotalCost 		= (float) (viewHolder.lblTotalCost.getText().toString().trim().isEmpty()?0.0:Float.parseFloat(viewHolder.lblTotalCost.getText().toString().trim()));
						if(newTotalCost!=oldTotalCost){
							float exsitingTotalCost =  (float) (mlblAllProductTotalCost.getText().toString().trim().isEmpty()?0.0:Float.parseFloat(mlblAllProductTotalCost.getText().toString().trim()));
							mlblAllProductTotalCost.setText(String.valueOf(exsitingTotalCost+(newTotalCost-oldTotalCost)));	
						}
					}
					int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
					System.out.println("" + pos + " = " + nEnterQty);
					System.out.println("" + pos + " oldTotalCost = " + oldTotalCost);
					Log.d("", "" + pos + " = " + nEnterQty);
					Log.d("","" + pos + " oldTotalCost = " + oldTotalCost);
					strEnterQty = viewHolder.txProductQty.getText().toString().trim();
					mProductList.get(pos).enteredQty 	=	strEnterQty.isEmpty()?0:Integer.parseInt(strEnterQty);
					mProductList.get(pos).totalCost		=	oldTotalCost;
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
						viewHolder.txbonusQty.setText(viewHolder.txbonusQty.getText().toString().substring(0, entertext.length()-1));
					}
					int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
					mProductList.get(pos).bonusEnteredQty	= nBonusQty;
					System.out.println("" + pos + " nBonusQty = " + nBonusQty);
					Log.d("","" + pos + " nBonusQty = " + nBonusQty);
				}
			});
			viewHolder.txbonusReason.addTextChangedListener(new TextWatcher()
			{

				@Override
				public void afterTextChanged(Editable s) {
					int pos = Integer.parseInt(viewHolder.lblsno.getText().toString());
					mProductList.get(pos).bonusReason	= s.toString();
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,int count, int after) {					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,int before, int count) {
					
				}
				
			});
			rowView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ProductsDetails) rowView.getTag();
		}
		if(product!=null)
		{
			viewHolder.lblsno.setText(String.valueOf(position));
			viewHolder.lblProductName.setText(product.productName);			
			viewHolder.lblAvailableQty.setText(Integer.toString(product.availableQty));
			viewHolder.lblProductCost.setText(Float.toString(product.productCost));
			viewHolder.txProductQty.setText("0");
			viewHolder.txbonusQty.setText("0");
			viewHolder.lblTotalCost.setText("0");
			viewHolder.txbonusReason.setText("");
		}
		return rowView;
	}
	
	public void refresh()
	{
		notifyDataSetChanged();
	}
}
