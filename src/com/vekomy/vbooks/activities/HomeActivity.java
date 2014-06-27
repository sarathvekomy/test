/**
 * com.vekomy.vbooks.activities.HomeActivity.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 7, 2013
 */
package com.vekomy.vbooks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.adapters.ModulesAdapter;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.helpers.ProgressActivity;
import com.vekomy.vbooks.utils.Utils;

/**
 * @author koteswararao
 *
 */
public class HomeActivity  extends ProgressActivity implements OnClickListener,OnItemClickListener
{
	TextView txExecuteMsg;
	TextView txExecuteName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try
		{
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.home_screen);
			txExecuteName = (TextView) findViewById(R.id.txsalesexecuteName);
			txExecuteName.setText("Welcome : " + Utils.getData(getApplicationContext(), Constants.USER_ID, Constants.NA));
	
			txExecuteMsg = (TextView) findViewById(R.id.txsalesMsg);
			txExecuteMsg.startAnimation((Animation) AnimationUtils.loadAnimation(this, R.anim.marquee));
			txExecuteMsg.setText("Message : " + Utils.getData(getApplicationContext(), Constants.MSG, "Msg"));
	
			userID = Utils.getData(VbookApp.getInstance(), Constants.USER_ID, Constants.NA);
			final ImageButton btnlogout = (ImageButton) findViewById(R.id.logout);
			btnlogout.setOnClickListener(this);
			
			GridView gridview  	= (GridView) findViewById(R.id.gridservices);
			gridview.setAdapter(new ModulesAdapter(this));
			gridview.setOnItemClickListener(this);
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume() 
	{
		try
		{
			super.onResume();
			txExecuteMsg.setText("Message : " + Utils.getData(getApplicationContext(), Constants.MSG, "Msg"));
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onRestart() 
	{	try
		{
			super.onRestart();
			txExecuteMsg.setText("Message : " + Utils.getData(getApplicationContext(), Constants.MSG, "Msg"));
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}	
	}
	
	public void onClick(View v)
	{
		try
		{
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			System.gc();  
			startActivity(intent);
		} 
		catch (Exception ex2)
		{
			Toast.makeText(getApplicationContext(), ex2.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void onItemClick(AdapterView<?> parent, View v, int position, long id) 
	{
		try
		{
			Intent intent = null;
			switch (position)
			{
				case 0:
					if(new SalesDao().isDayBookClosed(VbookApp.getDbInstance(),userID,Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID, Constants.NA))==1)
					{
						Utils.displayDialog(HomeActivity.this, null, "Your Day Book has been Closed.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
						return;
					}
					else
						intent = new Intent(getApplicationContext(), SalesActivity.class);
					
					break;
				case 1:
					if(new SalesDao().isDayBookClosed(VbookApp.getDbInstance(),userID,Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID,Constants.NA))==1)
					{
						Utils.displayDialog(HomeActivity.this, null, "Your Day Book has been Closed.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
						return;
					}
					else
					{
						intent = new Intent(getApplicationContext(), CustomerActivity.class);
					}
					break;					
				case 2:
					
					if(new SalesDao().isDayBookClosed(VbookApp.getDbInstance(),userID,Utils.getData(VbookApp.getInstance(), userID + Constants.CYCLE_ID,Constants.NA))==1)
					{
						Utils.displayDialog(HomeActivity.this, null, "Your Day Book has been Closed.", Utils.DIALOG_OK, "Ok", null, null, null, null, null);
						return;
					}
					else
					{
						intent = new Intent(getApplicationContext(), DayBookActivity.class);
						intent.putExtra("MODE", Constants.DAY_BOOK);						
					}
					break;
				case 3:
					intent = new Intent(getApplicationContext(), ViewTrxnHistoryActivity.class);
					break;
				case 4:
					intent = new Intent(getApplicationContext(), ModifyTrxnHistoryActivity.class);
					break;
				case 5:
					Utils.displayPasswordChangeDialog(HomeActivity.this, Utils.getData(VbookApp.getInstance(), Constants.USER_PWD, Constants.NA));
					return;
			}
			startActivity(intent);
			System.gc();
		}
		catch (Exception ex2)
		{
			Toast.makeText(getApplicationContext(), ex2.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
}
