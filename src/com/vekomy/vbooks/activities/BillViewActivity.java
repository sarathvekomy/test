/**
 * com.vekomy.vbooks.activities.BillViewActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 06-Nov-2013
 *
 * @author nkr
 *
 *
 */

package com.vekomy.vbooks.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.database.ImagesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;

/**
 * @author nkr
 * 
 */
public class BillViewActivity extends ProgressActivity 
{
	ImageView 		img;
	ZoomControls 	zoom;
	Context 		context;
	ImagesDao		imagesDao;
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.bill_view);
			this.context = this;
			imagesDao	= new ImagesDao();
			
			img 	= (ImageView) findViewById(R.id.imgViewBill);
			zoom 	= (ZoomControls) findViewById(R.id.zoomControls);

			zoom.setOnZoomInClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					int w = img.getWidth();
					int h = img.getHeight();

					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w + 10, h + 10);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					img.setLayoutParams(params);
				}
			});
			
			zoom.setOnZoomOutClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					int w = img.getWidth();
					int h = img.getHeight();
					
					RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w - 10, h - 10);
					params.addRule(RelativeLayout.CENTER_IN_PARENT);
					img.setLayoutParams(params);
				}
			});
			//File file;	
	        //Bitmap myBitmap = BitmapFactory.decodeFile("");
	        //img.setImageBitmap(myBitmap);
	        
	        // Check for SD Card
	        //if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) 
	        //{
			//	Toast.makeText(this, "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
	        //} 
	        //else 
	        //{
	            // Locate the image folder in your SD Card
	        //    file = new File(Environment.getExternalStorageDirectory() + File.separator + "SDImageTutorial");
	            // Create a new folder if no folder named SDImageTutorial exist
	        //    file.mkdirs();
	        //}
	        findViewById(R.id.btnbillViewClose).setOnClickListener(new OnClickListener() {			
	        	@Override
	        	public void onClick(View v) {
	        		finish();
	        	}
	        });
			String File_id = getIntent().getExtras().getString(Constants.BILL_IMG_ID);

	        new LoadImagesFromDB().execute(File_id);
		}
		catch(Exception e)
		{
			Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void getFromSdcard()
	{
	    /*File file= new File(android.os.Environment.getExternalStorageDirectory(),"TMyFolder");
	    if (file.isDirectory())
	    {
	            listFile = file.listFiles();


	            for (int i = 0; i < listFile.length; i++)
	            {

	                f.add(listFile[i].getAbsolutePath());

	            }
	        }*/
	}
	
	public class LoadImagesFromDB  extends AsyncTask<String, Void, Bitmap> 
	{
		protected void onPreExecute(){showProgressDialog("Loading Image Information..... Please wait...");}
		
    	Bitmap bitmap = null;
    	
		// Call after onPreExecute method
        protected Bitmap doInBackground(String... fileids) 
        {
        	try
			{
        		String fileid = fileids[0];
        		if(fileid.startsWith(Constants.DB))
        		{
        			fileid = fileid.substring(3);        			
        			//if(fileid.endsWith(Constants.CR))
        			//{
        			//	fileid = fileid.substring(0,fileid.length()-3);
        			//}        			
        		}
        		bitmap = imagesDao.readBillImage(VbookApp.getDbInstance(), fileid);        		
			}
			catch (Exception scx){scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
			return bitmap;
         }
          
		protected void onPostExecute(Bitmap bitmap)
		{
			try
			{
				dismissProgressDialog();
                /**************  Decode an input stream into a bitmap. *********/
				if (bitmap != null) 
				{
					/********* Creates a new bitmap, scaled from an existing bitmap. ***********/
                    //newBitmap = Bitmap.createScaledBitmap(bitmap, 170, 170, true);
					img.setImageBitmap(bitmap);
				}
			}
			catch(Exception e)
			{
				Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}			
		}                 
     }
}
