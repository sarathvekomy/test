/**
 * com.vekomy.vbooks.adapters.ModulesAdapter.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks.adapters;




import com.vekomy.vbooks.R;
import com.vekomy.vbooks.utils.Utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * @author koteswararao
 *
 */
public class ModulesAdapter extends BaseAdapter 
{
    private Integer[] mImageIds = 
    								{
							            R.drawable.manage_trxn,
							            R.drawable.customers,
							            R.drawable.daybook,
							            R.drawable.reports,
							            R.drawable.modifytrxn,
							            R.drawable.change_pwd
    								};
    
	private Context mContext;

    public ModulesAdapter(Context c) 
    {
        mContext = c;
    }

	public int getCount() 
	{
		return mImageIds.length;
	}

	public Object getItem(int position) 
	{
		//return null;
		 return mImageIds[position];
	}

	public long getItemId(int position) 
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) 
	{
		try
		{
			ImageView imageView;
	        if (convertView == null) 
	        {
	            imageView = new ImageView(mContext);
	            imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(4, 4, 4, 4);
	        }
	        else 
	        {
	            imageView = (ImageView) convertView;
	        }
	        imageView.setImageBitmap(Utils.getBmpFromRes(mContext.getResources(), mImageIds[position]));
	        return imageView;
		}
		catch(Exception e)
		{
			Toast.makeText(mContext, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return convertView;
	}
}
