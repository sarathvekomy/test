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

import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VBookApp;
import com.vekomy.vbooks.adapters.ModulesAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author koteswararao
 *
 */
public class HomeActivity  extends Activity implements OnClickListener,OnItemClickListener
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
			txExecuteName.setText("Welcome to: " + VBookApp.Name);
	
			txExecuteMsg = (TextView) findViewById(R.id.txsalesMsg);
			txExecuteMsg.startAnimation((Animation) AnimationUtils.loadAnimation(this, R.anim.marquee));
			txExecuteMsg.setText("Message : " + VBookApp.msg);
	
			final Button btnlogout = (Button) findViewById(R.id.logout);
			btnlogout.setOnClickListener(this);
			
			GridView gridview  	= (GridView) findViewById(R.id.gridservices);
			gridview.setAdapter(new ModulesAdapter(this));
			gridview.setOnItemClickListener(this);
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Unable to load Screen your Device Memory full.", Toast.LENGTH_SHORT).show();
			ex.printStackTrace();
		}
	}

	@Override
	protected void onResume() 
	{
		try
		{
			super.onResume();
			txExecuteMsg.setText("Message : " + VBookApp.msg);
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Unable to load Screen your Device Memory full.", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onRestart() 
	{	try
		{
			super.onRestart();
			txExecuteMsg.setText("Message : " + VBookApp.msg);
		}
		catch(Exception ex)
		{
			Toast.makeText(getApplicationContext(), "Error : Unable to load Screen your Device Memory full.", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getApplicationContext(), "Error : Invalid selection", Toast.LENGTH_SHORT).show();
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
					intent = new Intent(getApplicationContext(), SalesActivity.class);
					break;
				case 1:
					intent = new Intent(getApplicationContext(), CustmourActivity.class);
					break;
				case 2:
					intent = new Intent(getApplicationContext(), SalesActivity.class);
					break;
				case 3:
					intent = new Intent(getApplicationContext(), SalesActivity.class);
					break;					
			}
			startActivity(intent);
			System.gc();
		}
		catch (Exception ex2)
		{
			Toast.makeText(getApplicationContext(), "Error : Invalid selection", Toast.LENGTH_SHORT).show();
		}		
	}
}
