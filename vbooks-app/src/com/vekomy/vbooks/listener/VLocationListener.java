/**
 * com.vekomy.vbooks.listener.VLocationListener.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 11-Nov-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.listener;

import com.vekomy.vbooks.VbookApp;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author nkr
 *
 */
public class VLocationListener  implements LocationListener {

	@Override
	public void onLocationChanged(Location location) 
	{
		String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",location.getLongitude(), location.getLatitude());
		Toast.makeText(VbookApp.getInstance(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) 
	{
		Toast.makeText(VbookApp.getInstance(), "Provider status changed",Toast.LENGTH_LONG).show();	
	}

	@Override
	public void onProviderEnabled(String provider) 
	{
		 Toast.makeText(VbookApp.getInstance(),"Provider enabled by the user. GPS turned on",Toast.LENGTH_LONG).show();	
	}

	@Override
	public void onProviderDisabled(String provider) 
	{
		Toast.makeText(VbookApp.getInstance(),"Provider disabled by the user. GPS turned off",Toast.LENGTH_LONG).show();
	}

}
