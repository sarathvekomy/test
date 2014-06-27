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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * @author koteswararao
 *
 */
public class ModulesAdapter extends BaseAdapter 
{
	// references to our images
    private Integer[] mImageIds = {
            R.drawable.profile,
            R.drawable.customers,
            R.drawable.daybook,
            R.drawable.reports//,R.drawable.settings
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

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ImageView imageView;
		// if it's not recycled, initialize some attributes
        if (convertView == null) 
        {  
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } 
        else 
        {
            imageView = (ImageView) convertView;
        }
        
        imageView.setImageBitmap(Utils.getBmpFromRes(mContext.getResources(), mImageIds[position]));
        
        return imageView;
        /*Bitmap b = null;
        Drawable d;
        try 
        {
            b = Bitmap.createBitmap(320,424,Bitmap.Config.RGB_565);
            b.eraseColor(0xFFFFFFFF);
            Rect r = new Rect(0, 0,320 , 424);
            Canvas c = new Canvas(b);
            Paint p = new Paint();
            p.setColor(0xFFC0C0C0);
            c.drawRect(r, p);
            d = mContext.getResources().getDrawable(mImageIds[position]);
            d.setBounds(r);
            d.draw(c);*/

         /*   BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inTempStorage = new byte[128*1024];
            b = BitmapFactory.decodeStream(mContext.getResources().openRawResource(mImageIds[position]), null, o2);
            o2.inSampleSize=16;
            o2.inPurgeable = true;*/
        /*}
        catch (Exception e){}
        imageView.setImageBitmap(b);
        //imageView.setImageResource(mThumbIds[position]);
        return imageView;*/
	}

}
