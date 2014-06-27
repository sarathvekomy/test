/**
 * com.vekomy.vbooks.BootReceiver.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: nafees
 * Created: Jun 8, 2013
 */
package com.vekomy.vbooks;

import com.vekomy.vbooks.service.SyncService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Boot receiver to launch service after device boots up
 * 
 * @see manifest: android.intent.action.BOOT_COMPLETED
 * 
 * @author NKR
 * 
 */
public class BootUpReceiver extends BroadcastReceiver 
{
	private static final String LOG_CAT = "BootReceiver";

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d(LOG_CAT, "bootReciever.onReciever");
		try 
		{
			if ((intent.getAction() != null) && (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")))
		    {
		        // Start the service or activity
				// context.startService(new Intent(context, LauncherService.class));
				
				Log.d("ScheduleReciver", "Schedule Reciver.");
		    	Intent serviceIntent = new Intent(context,SyncService.class);
		    	context.startService(serviceIntent);
		    }
			
			 // start root shell
            /*Shell shell = Shell.startRootShell();
            
            // simple commands
            SimpleCommand command0 = new SimpleCommand("echo this is a command","echo this is another command");
            SimpleCommand command1 = new SimpleCommand("toolbox ls");
            SimpleCommand command2 = new SimpleCommand("ls -la /system/etc/hosts");

            shell.add(command0).waitForFinish();
            shell.add(command1).waitForFinish();
            shell.add(command2).waitForFinish();

            Log.d(LOG_CAT, "Output of command2: " + command2.getOutput());
            Log.d(LOG_CAT, "Exit code of command2: " + command2.getExitCode());*/
            
            
		} 
		catch (Exception e) 
		{
			Log.e(LOG_CAT, "onReceive", e);
		}
	}
}