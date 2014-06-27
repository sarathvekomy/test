/**
 * com.vekomy.vbooks.service.SyncService.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: 19-Aug-2013
 *
 * @author nkr
 *
 *
*/

package com.vekomy.vbooks.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import com.vekomy.vbooks.Constants;
import com.vekomy.vbooks.R;
import com.vekomy.vbooks.VbookApp;
import com.vekomy.vbooks.app.request.CustomersInfoRequest;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookCR;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteCR;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.app.request.JournalCR;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnCR;
import com.vekomy.vbooks.app.response.AllocatedProductList;
import com.vekomy.vbooks.app.response.CustomerAmountInfo;
import com.vekomy.vbooks.app.response.CustomerAmountInfoList;
import com.vekomy.vbooks.app.response.CustomerInfo;
import com.vekomy.vbooks.app.response.CustomerInfoList;
import com.vekomy.vbooks.app.response.CustomerProductsCostList;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.response.SystemNotification;
import com.vekomy.vbooks.app.response.SystemNotificationList;
import com.vekomy.vbooks.database.CustomerDao;
import com.vekomy.vbooks.database.SalesDao;
import com.vekomy.vbooks.restservices.RestTemplateFactory;
import com.vekomy.vbooks.utils.TimeUtils;
import com.vekomy.vbooks.utils.Utils;


/**
 * @author nkr
 *
 */
public class SyncService extends ABaseIntentService 
{
	protected static final String TAG = SyncService.class.getSimpleName();
	
	final String NOTIFICATION_URL 	= VbookApp.getInstance().getString(R.string.BASE_URL) + "getNotification?uname={uname}&orgID={orgID}";	
	final String DATE_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "getDayId";
	final String CUSTOMER_LIST_URL 	= VbookApp.getInstance().getString(R.string.BASE_URL) + "assignedcustomerlist?uname={uname}&orgID={orgID}";
	final String PRODUCT_LIST_URL   = VbookApp.getInstance().getString(R.string.BASE_URL) + "allocatedproductlist?uname={uname}&orgID={orgID}";
	final String PRODUCTS_COST_URL  = VbookApp.getInstance().getString(R.string.BASE_URL) + "customerproductsCostlist?uname={uname}&orgID={orgID}";
	final String CUSTOMER_INFO_URL  = VbookApp.getInstance().getString(R.string.BASE_URL) + "assignedcustomerInfolist";
	final String UPDATE_NOTIFI_URL  = VbookApp.getInstance().getString(R.string.BASE_URL) + "updateNotification?UID={uid}";
	
	final String DN_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "saveDN";
	final String SR_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "saveSR";
	final String JN_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "saveJN";
	final String DB_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "saveDB";
	final String CUST_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "saveCustomer";
	
	
	final String CR_DN_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "changeDN";
	final String CR_SR_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "changeSR";
	final String CR_JN_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "changeJN";
	final String CR_DB_URL 			= VbookApp.getInstance().getString(R.string.BASE_URL) + "changeDB";
	final String NEW_CUST_LIST_URL	= VbookApp.getInstance().getString(R.string.BASE_URL) + "newlyassignedcustomerlist";
	

	final String[] ALLOWED_APPS		= {"VBooks","Phone","Contacts","Contacts Storage","Email","Messanger","to do","Clock","Calculator","Camera","Settings","Settings Storage"};
	
	HttpHeaders headers = new HttpHeaders();
	
	SalesDao salesDao;
	CustomerDao customerDao;
	public SyncService() 
	{
		super("SyncService");
		salesDao = new SalesDao();
		customerDao = new CustomerDao();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	@Override
    public void onDestroy() 
	{
		super.onDestroy();
		salesDao.close();
    }
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		Log.d("SyncService", "SyncService Service Started.");
		try
		{
			// 1. un install not usefull to sales Executive Apps
			// uninstallApps();
			
			if(!(saleEmpId.equals(Constants.NA) || orgId.equals("0")))
			{
				// 2. check Internet connection
				if(Utils.checkNetworkStatus(VbookApp.getInstance()))
				{
					// 2.1 send Pending trxn(Not end yet server)
					sendPendingTrxn();

					// 2.2 update date
					// updateDayId(false);
					
					// 2.3 check any pending/unread Notification at server(if Yes)
					checkPendingNotification();
				}				
			}

		}
		catch(Exception e)
		{
			Utils.displayNotification(this,"Error at Service.",e.getMessage());
			//Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 
	 * @return
	 */
	private int sendPendingTrxn()
	{
		try
		{
			List<String[]> pendingids = salesDao.readPendingIds(VbookApp.getDbforServices());
			RestTemplate restTemplate = RestTemplateFactory.getInstance(VbookApp.getInstance());
			String[] pendingId = null;
			
			// 1. sending DN..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();) 
			{
				try
				{
					pendingId = iterator.next();
					if(pendingId[0].startsWith(Constants.DN) && !pendingId[0].endsWith(Constants.CR))
					{
						DeliveryNote deliveryNote = salesDao.readDeliveryNote(VbookApp.getDbforServices(),pendingId[0],pendingId[1],true);				
						try
						{
						    // The POST request.
							Log.d(TAG,"Sending DN request to server...." + DN_URL);
							Response response = restTemplate.postForObject(DN_URL, new HttpEntity<DeliveryNote>(deliveryNote, headers), Response.class);
							if(response!=null && response.getStatusCode() == 200)
							{
								salesDao.setcommited(VbookApp.getDbforServices(),pendingId[0]);
								iterator.remove();
								Log.d(TAG,"DN Saved successfully.");						
								continue;
							}
							Log.d(TAG,"DN Saved Failed.");
							return -1;
						}
						catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
						catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
						break;
					}
				}
				catch(Exception e)
				{
					Utils.displayNotification(this,"Error at DN sending Failed.",e.getMessage());
					Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			// 2. sending SR..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();)
			{
				try
				{				
					pendingId = iterator.next();
					if(pendingId[0].startsWith(Constants.SR) && !pendingId[0].endsWith(Constants.CR))
					{
						SalesReturn deliveryNote = salesDao.readSalesReturn(VbookApp.getDbforServices(),pendingId[0],pendingId[1]);				
						try
						{
						    // The POST request.
							Log.d(TAG,"Sending SR request to server...." + SR_URL);
							Response response = restTemplate.postForObject(SR_URL, new HttpEntity<SalesReturn>(deliveryNote, headers), Response.class);
							if(response!=null && response.getStatusCode() == 200)
							{
								salesDao.setcommited(VbookApp.getDbforServices(),pendingId[0]);
								iterator.remove();												
								Log.d(TAG,"SR Saved successfully.");
								continue;
							}
							Log.d(TAG,"SR Saved Failed.");
							return -1;
						}
						catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
						catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}					
					}
				}
				catch(Exception e)
				{
					Utils.displayNotification(this,"Error at SR sending  Failed.",e.getMessage());
					Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}					
			}
			// 3. sending JN..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();) 
			{
				try
				{
					pendingId = iterator.next();
					if(pendingId[0].startsWith(Constants.JN) && !pendingId[0].endsWith(Constants.CR))
					{
						Journal journal = salesDao.readJournal(VbookApp.getDbforServices(),pendingId[0],pendingId[1]);				
						try
						{
						    // The POST request.
							Log.d(TAG,"Sending JN request to server...." + JN_URL);
							Response response = restTemplate.postForObject(JN_URL, new HttpEntity<Journal>(journal, headers), Response.class);
							if(response!=null && response.getStatusCode() == 200)
							{
								salesDao.setcommited(VbookApp.getDbforServices(),pendingId[0]);
								iterator.remove();
								Log.d(TAG,"JN Saved successfully.");
								continue;
							}
							Log.d(TAG,"JN Saved Failed.");
							return -1;
						}
						catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
						catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}					
					}
				}
				catch(Exception e)
				{
					Utils.displayNotification(this,"Error at JN sending  Failed.",e.getMessage());
					Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}				
			}
			// 3. sending CR..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();) 
			{
				pendingId = iterator.next();
				if(pendingId[0].endsWith(Constants.CR))
				{
					sendCRTrxns(restTemplate,pendingId);
				}
			}
			// 3. sending DB..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();) 
			{
				try
				{	
					pendingId = iterator.next();
					if(pendingId[0].startsWith(Constants.DB) && !pendingId[0].endsWith(Constants.CR))
					{
						DayBook dayBook = salesDao.readDayBook(VbookApp.getDbforServices(),pendingId[0],pendingId[1]);				
						try
						{
						    // The POST request.
							Log.d(TAG,"Sending DB request to server...." + DB_URL);
							Response response = restTemplate.postForObject(DB_URL, new HttpEntity<DayBook>(dayBook, headers), Response.class);
							if(response!=null && response.getStatusCode() == 200)
							{
								salesDao.setcommited(VbookApp.getDbforServices(),pendingId[0]);
								iterator.remove();
								Log.d(TAG,"DB Saved successfully.");
								continue;						
							}
							else
							{
								Log.d(TAG,"DB Saved Failed.");
						    }
						}
						catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
						catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
					}
				}
				catch(Exception e)
				{
					Utils.displayNotification(this,"Error at Day Book sending  Failed.",e.getMessage());
					Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}
			}
			// 3. sending DB CRs..........
			for (Iterator<String[]> iterator = pendingids.iterator(); iterator.hasNext();) 
			{
				try
				{
					pendingId = iterator.next();
					if(pendingId[0].startsWith(Constants.DB) && pendingId[0].endsWith(Constants.CR))
					{
						DayBookCR dayBookCR = new DayBookCR();
						DayBook dayBook = salesDao.readDayBook(VbookApp.getDbforServices(),pendingId[0],pendingId[1]);
						dayBookCR.setNewDayBook(dayBook);
						dayBook = salesDao.readDayBook(VbookApp.getDbforServices(),pendingId[0].substring(0, pendingId[0].length()-3),pendingId[1]);
						dayBookCR.setOldDayBook(dayBook);
						dayBookCR.setSalesExecutive(pendingId[1]);
						dayBookCR.setOrganizationId(Integer.parseInt(orgId));
						try
						{
						    // The POST request.
							Log.d(TAG,"Sending DB Change request to server...." + CR_DB_URL);
							Response response = restTemplate.postForObject(CR_DB_URL, new HttpEntity<DayBookCR>(dayBookCR, headers), Response.class);
							if(response!=null && response.getStatusCode() == 200)
							{
								salesDao.setcommited(VbookApp.getDbforServices(),pendingId[0]);
								iterator.remove();
								Log.d(TAG,"DB Change Request Saved successfully.");
								continue;						
							}
							else
							{
								Log.d(TAG,"DB Change Request Saved Failed.");
						    }
						}
						catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
						catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
					}
				}
				catch(Exception e)
				{
					Utils.displayNotification(this,"Error at Day Book change Request sending  Failed.",e.getMessage());
					Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
				}					
			}
			
	    	List<CustomerAmountInfo> custmourList = salesDao.readAssiginCustomers(VbookApp.getDbforServices(),saleEmpId,Utils.getData(this, saleEmpId +Constants.CYCLE_ID, Constants.NA));
		    if(custmourList.size() > 0)
		    {
	    		try
	    		{
	    			// 	get newly Assigned Customers
			    	CustomersInfoRequest customersListReq = new CustomersInfoRequest();
			    	List<String> businessNamesList = customerDao.getNewlyAssignedCustomers(VbookApp.getDbforServices(),custmourList);
			    	if(businessNamesList.size() > 0)
			    	{
			    		customersListReq.setOrgId(Integer.parseInt(orgId));
			    		customersListReq.setBusinessNames(businessNamesList);
			    		CustomerInfoList resultList = restTemplate.postForObject(CUSTOMER_INFO_URL, new HttpEntity<CustomersInfoRequest>(customersListReq, headers), CustomerInfoList.class);
			    		if(resultList!=null)
			    		{
			    			for (int index = 0; index < resultList.getCustomerInfoList().size(); index++) {
			    				customerDao.insertOrUpdateCustomer(VbookApp.getDbforServices(),saleEmpId, resultList.getCustomerInfoList().get(index),true);
			    			}
			    		}
			    		Utils.displayNotification(this,ENotificationTypes.ASSIGN_CUSTOMER.name(),"Customer Details update Successfully ");
			    	}
	    		}
		    	catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Customer change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
		    }
			// Customer change requests 
			try
			{
				List<CustomerInfo> customerlist = customerDao.readCustomerChangeInfo(VbookApp.getDbforServices(),orgId,saleEmpId);
				CustomerInfo customer;
				for (Iterator<CustomerInfo> iterator = customerlist.iterator(); iterator.hasNext();)
				{
					try
					{
						customer = iterator.next();
						Log.d(TAG,"Sending Customer info request to server...." + CUST_URL);
						Response response = restTemplate.postForObject(CUST_URL, new HttpEntity<CustomerInfo>(customer, headers), Response.class);
						if(response!=null)
						{
							customerDao.setcommited(VbookApp.getDbforServices(),customer.getBusinessName());
							Log.d(TAG,"Customer Saved successfully.");
							continue;						
						}
						else
						{
							Log.d(TAG,"DB Saved Failed.");
						}
					}catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
					catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
				}
			}
			catch(Exception e)
			{
				Utils.displayNotification(this,"Error at Customer info reading Failed.",e.getMessage());
				Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e)
		{
			Utils.displayNotification(this,"Error at Sending Trxns.",e.getMessage());
			//Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();		
		}
		return 0;
	}
	/**
	 * 
	 * @param restTemplate
	 * @param changeReqId
	 * @return
	 */
	private int sendCRTrxns(RestTemplate restTemplate,String[] changeReqId)
	{
		if(changeReqId[0].startsWith(Constants.DN))
		{
			try
			{			
				DeliveryNoteCR dnCR = new DeliveryNoteCR();
				DeliveryNote deliveryNote = salesDao.readDeliveryNote(VbookApp.getDbforServices(),changeReqId[0],changeReqId[1],true);
				dnCR.setNewDeliveryNote(deliveryNote);
				deliveryNote = salesDao.readDeliveryNote(VbookApp.getDbforServices(),changeReqId[0].substring(0, changeReqId[0].length()-3),changeReqId[1],true);
				dnCR.setOldDeliveryNote(deliveryNote);
				dnCR.setSalesExecutive(changeReqId[1]);
				dnCR.setOrganizationId(Integer.parseInt(orgId));
				try
				{
				    // The POST request.
					Log.d(TAG,"Sending DN Change request to server...." + CR_DN_URL);
					Response response = restTemplate.postForObject(CR_DN_URL, new HttpEntity<DeliveryNoteCR>(dnCR, headers), Response.class);
					if(response!=null && response.getStatusCode() == 200)
					{
						salesDao.setcommited(VbookApp.getDbforServices(),changeReqId[0]);
						Log.d(TAG,"Change Request DN Saved successfully.");
						return 0;
					}
					Log.d(TAG,"DN Saved Failed.");
				}
				catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
				catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
				return -1;
			}
			catch(Exception e)
			{
				Utils.displayNotification(this,"Error at change Request DN sending Failed.",e.getMessage());
				Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		else if(changeReqId[0].startsWith(Constants.SR))
		{
			try
			{
				SalesReturnCR srCR = new SalesReturnCR();
				SalesReturn deliveryNote = salesDao.readSalesReturn(VbookApp.getDbforServices(),changeReqId[0],changeReqId[1]);
				srCR.setNewSalesReturn(deliveryNote);
				deliveryNote = salesDao.readSalesReturn(VbookApp.getDbforServices(),changeReqId[0].substring(0, changeReqId[0].length()-3),changeReqId[1]);
				srCR.setOldSalesReturn(deliveryNote);
				srCR.setSalesExecutive(changeReqId[1]);
				srCR.setOrganizationId(Integer.parseInt(orgId));
				try
				{
				    // The POST request.
					Log.d(TAG,"Sending SR Change request to server...." + CR_SR_URL);
					Response response = restTemplate.postForObject(CR_SR_URL, new HttpEntity<SalesReturnCR>(srCR, headers), Response.class);
					if(response!=null && response.getStatusCode() == 200)
					{
						salesDao.setcommited(VbookApp.getDbforServices(),changeReqId[0]);
						Log.d(TAG,"Change Request SR Saved successfully.");
						return 0;
					}
					Log.d(TAG,"SR Saved Failed.");
				}
				catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
				catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
				return -1;
			}
			catch(Exception e)
			{
				Utils.displayNotification(this,"Error at change Request SR sending Failed.",e.getMessage());
				Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		else if(changeReqId[0].startsWith(Constants.JN))
		{
			try
			{
				JournalCR jnCR = new JournalCR();
				Journal journal = salesDao.readJournal(VbookApp.getDbforServices(),changeReqId[0],changeReqId[1]);
				jnCR.setNewJournal(journal);
				journal = salesDao.readJournal(VbookApp.getDbforServices(),changeReqId[0].substring(0, changeReqId[0].length()-3),changeReqId[1]);
				jnCR.setOldJournal(journal);
				jnCR.setSalesExecutive(changeReqId[1]);
				jnCR.setOrganizationId(Integer.parseInt(orgId));
				try
				{
				    // The POST request.
					Log.d(TAG,"Sending JN Change request to server...." + CR_JN_URL);
					Response response = restTemplate.postForObject(CR_JN_URL, new HttpEntity<JournalCR>(jnCR, headers), Response.class);
					if(response!=null && response.getStatusCode() == 200)
					{
						salesDao.setcommited(VbookApp.getDbforServices(),changeReqId[0]);
						Log.d(TAG,"Change Request JN Saved successfully.");
						return 0;
					}
					Log.d(TAG,"JN Saved Failed.");
				}
				catch (RestClientException  e)	{e.printStackTrace();  Log.e(TAG, e.getLocalizedMessage(),  e  );} 
				catch (Exception scx)			{scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(),scx);}
				return -1;
			}
			catch(Exception e)
			{
				Utils.displayNotification(this,"Error at change Request JN sending Failed.",e.getMessage());
				Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		return 0;
	}
	/**
	 * 
	 * @return
	 */
	private int checkPendingNotification()
	{
		SystemNotificationList response = null;
		RestTemplate restTemplate = null;
		try
		{
			restTemplate = RestTemplateFactory.getInstance(VbookApp.getInstance());
		    // The POST request.
		    Log.d(TAG,"Sending request for Notification" + NOTIFICATION_URL);
		    response = restTemplate.getForObject(NOTIFICATION_URL, SystemNotificationList.class,saleEmpId,orgId);
		    if(response == null)
		    {
		    	return 0;
		    }
		}
		catch (RestClientException  e)
		{
			e.printStackTrace();
			Log.e(TAG, e.getLocalizedMessage(), e);
			return 0;
		} 
		catch (Exception scx)
		{
			scx.printStackTrace();
			Log.e(TAG, scx.getLocalizedMessage(), scx);
			return 0;
		}
		
		List<SystemNotification> notifications = response.getNoticationList();
		
		SystemNotification notification;
		String sale_ref_id;
		//	ALLOTTED,
		//	ALLOTMENT_UPDATE,
		//	ASSIGN_CUSTOMER,
		//	CUSTOMER_CR,
		//	DN_TXN_CR,
		//	DN_PAYMENTS_TXN_CR,
		//	SR_TXN_CR,
		//	DB_TXN_CR,
		//	JOURNAL_TXN_CR
		boolean isSucess = false;		
		for (int i = 0; i < notifications.size(); i++)
		{
			isSucess = false;
			notification = notifications.get(i);
			if(notification == null || notification.getNotificationType() == null)
				continue;
			
	    	if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.ALLOTTED.name()))
	    	{
	    		if(updateAllotmentId(notification.getInvoiceNo())==false)
	    		{
	    			continue;
	    		}
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
		    		// 1. get Assign Customer info 
	    			CustomerAmountInfoList custmourList = restTemplate.getForObject(CUSTOMER_LIST_URL,CustomerAmountInfoList.class,saleEmpId,orgId);
	    			if(custmourList!=null)
	    			{
					    for (int j = 0; j < custmourList.getCustmourList().size(); j++) 
					    {
					    	salesDao.insertcustomerBF(VbookApp.getDbforServices(),custmourList.getCustmourList().get(j),saleEmpId,false,"");
						}
					    
			    		// 2. get allocated products info			    
					    AllocatedProductList salesInfo = restTemplate.getForObject(PRODUCT_LIST_URL,AllocatedProductList.class,saleEmpId,orgId);
					    
					    if(salesInfo!=null)
					    {
					    	CustomerAmountInfo saleExecutive = new CustomerAmountInfo();
					    	Float amt = salesInfo.getAdvanceAmt();
					    	if(amt > 0)
					    	{
					    		saleExecutive.setCreditAmount(amt);
					    		saleExecutive.setAdvanceAmount(0f);
					    	}
					    	else
					    	{
					    		saleExecutive.setAdvanceAmount(amt);
					    		saleExecutive.setCreditAmount(0f);
					    	}
					    	saleExecutive.setUid(saleEmpId);
					    	saleExecutive.setInvoiceName("0|0");
					    	saleExecutive.setBusinessName(saleEmpId);
					    	// sales Executive opening Balances
					    	salesDao.insertcustomerBF(VbookApp.getDbforServices(),saleExecutive,saleEmpId,true,Constants.NA);
					    	
						    for (int j = 0; j < salesInfo.getProductList().size(); j++) {
						    	salesDao.insertproductsQty(VbookApp.getDbforServices(),salesInfo.getProductList().get(j),saleEmpId);
							}
						    // 3. Read customer product costs from server
						    CustomerProductsCostList customerProductsCostList = restTemplate.getForObject(PRODUCTS_COST_URL,CustomerProductsCostList.class,saleEmpId,orgId);
						    if(customerProductsCostList!=null)
						    {
						    	customerDao.insertCustomerProductCosts(VbookApp.getDbforServices(),customerProductsCostList.getCustproductsCostList());						    
						    // finally end
						    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
						    if(result != null && result.getStatusCode()==200 )
						    	VbookApp.getDbforServices().setTransactionSuccessful();						    	
						    	Utils.saveData(this, saleEmpId + Constants.IS_CYCLE_CLOSED, "NO");
						    	isSucess = true;
						    }
					    }
	    			}
	    		}
	    		catch (RestClientException  e){e.printStackTrace();	Log.e(TAG, e.getLocalizedMessage(), e);} 
	    		catch (Exception scx)		  {scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    		if(isSucess)
	    			Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"Successfully ");	    		
	    		else
	    			Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"Failed.");
	    		continue;	    		
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.ALLOTMENT_UPDATE.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
	    			// 1. allocated products info			    
	    			AllocatedProductList salesInfo = restTemplate.getForObject(PRODUCT_LIST_URL,AllocatedProductList.class,saleEmpId,orgId);
	    			
	    			if(salesInfo!=null)
	    			{
	    				CustomerAmountInfo saleExecutive = new CustomerAmountInfo();
	    				Float amt = salesInfo.getAdvanceAmt();
	    				if(amt > 0)
	    				{
	    					saleExecutive.setCreditAmount(amt);
	    					saleExecutive.setAdvanceAmount(0f);
	    				}
	    				else
	    				{
	    					saleExecutive.setAdvanceAmount(amt);
	    					saleExecutive.setCreditAmount(0f);
	    				}
	    				saleExecutive.setUid(saleEmpId);
	    				saleExecutive.setInvoiceName("0|0");
				    	saleExecutive.setBusinessName(saleEmpId);
				    	salesDao.updateBForCF(VbookApp.getDbforServices(),saleExecutive,saleEmpId,true,Constants.NA);
				    	
					    for (int j = 0; j < salesInfo.getProductList().size(); j++) {
					    	salesDao.updateproductsQty(VbookApp.getDbforServices(),salesInfo.getProductList().get(j),saleEmpId);
						}
					    // 3. Read customer product costs from server
					    CustomerProductsCostList customerProductsCostList = restTemplate.getForObject(PRODUCTS_COST_URL,CustomerProductsCostList.class,saleEmpId,orgId);
					    if(customerProductsCostList!=null)
					    {
					    	customerDao.insertCustomerProductCosts(VbookApp.getDbforServices(),customerProductsCostList.getCustproductsCostList());						    
					    // finally end
					    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
					    if(result != null && result.getStatusCode()==200 )
					    	VbookApp.getDbforServices().setTransactionSuccessful();
					    	isSucess = true;
					    }
	    			}
	    		}
	    		catch (RestClientException  e){e.printStackTrace();	Log.e(TAG, e.getLocalizedMessage(), e);} 
	    		catch (Exception scx)		  {scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    		if(isSucess)
	    			Utils.displayNotification(this,ENotificationTypes.ALLOTMENT_UPDATE.name(),"Successfully ");
	    		else
	    			Utils.displayNotification(this,ENotificationTypes.ALLOTMENT_UPDATE.name(),"Failed.");
	    		continue;
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.ASSIGN_CUSTOMER.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
	    			// 	get newly Assigned Customers Balance Info
			    	CustomersInfoRequest customersListReq = new CustomersInfoRequest();
			    	String businessNames = notification.getBusinessName();
			    	if(businessNames!=null)
			    	{
				    	List<String> businessNamesList = Arrays.asList(businessNames.split(","));
				    	if(businessNamesList.size() > 0)
				    	{
				    		customersListReq.setOrgId(Integer.parseInt(orgId));
				    		customersListReq.setBusinessNames(businessNamesList);
				    		CustomerAmountInfoList custmourList = restTemplate.postForObject(NEW_CUST_LIST_URL,new HttpEntity<CustomersInfoRequest>(customersListReq, headers),CustomerAmountInfoList.class);
				    		if(custmourList!=null)
				    		{
							    for (int index = 0; index < custmourList.getCustmourList().size(); index++) 
							    {
							    	salesDao.insertcustomerBF(VbookApp.getDbforServices(),custmourList.getCustmourList().get(index),saleEmpId,false,"");
								}
							    // here we get customer product cost
							    CustomerProductsCostList customerProductsCostList = restTemplate.getForObject(PRODUCTS_COST_URL,CustomerProductsCostList.class,saleEmpId,orgId);
							    if(customerProductsCostList!=null)
							    {
							    	customerDao.insertCustomerProductCosts(VbookApp.getDbforServices(),customerProductsCostList.getCustproductsCostList());						    
								    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
								    if(result != null && result.getStatusCode()==200 )
								    {
								    	VbookApp.getDbforServices().setTransactionSuccessful();
								    	isSucess = true;
								    }
							    }
				    		}
				    	}	
			    	}
	    		}
	    		catch (RestClientException  e){e.printStackTrace();	Log.e(TAG, e.getLocalizedMessage(), e);} 
	    		catch (Exception scx)		  {scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    		if(isSucess)
	    			Utils.displayNotification(this,ENotificationTypes.ASSIGN_CUSTOMER.name(),"Successfully ");
	    		else
	    			Utils.displayNotification(this,ENotificationTypes.ASSIGN_CUSTOMER.name(),"Failed.");
	    		continue;
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.DEASSIGN_CUSTOMER.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
	    			String businessNames = notification.getBusinessName();
			    	if(businessNames!=null)
			    	{
			    		if(salesDao.deassigncustomer(VbookApp.getDbforServices(), businessNames,saleEmpId) <= 0)
			    		{
			    			//Utils.displayNotification(this,ENotificationTypes.DEASSIGN_CUSTOMER.name(),"given customer is not available in local Db.");
			    		}
					    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
					    if(result != null && result.getStatusCode()==200 )
					    {
					    	VbookApp.getDbforServices().setTransactionSuccessful();
					    	isSucess = true;
					    }
			    	}
	    		}
	    		catch (RestClientException  e){e.printStackTrace();	Log.e(TAG, e.getLocalizedMessage(), e);} 
	    		catch (Exception scx)		  {scx.printStackTrace();Log.e(TAG, scx.getLocalizedMessage(), scx);}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    		if(isSucess)
	    			Utils.displayNotification(this,ENotificationTypes.DEASSIGN_CUSTOMER.name(),"Successfully ");
	    		else
	    			Utils.displayNotification(this,ENotificationTypes.DEASSIGN_CUSTOMER.name(),"Failed.");
	    		continue;
	    	}
	    	if(notification.getInvoiceNo() == null || notification.getNotificationStatus()==null)
	    		continue;
	    	
	    	if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.SALES_RETURN.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
		    		int index = notification.getInvoiceNo().indexOf("#");
		    		index = index == -1?0:index+1;
		    		sale_ref_id = Constants.SR + notification.getInvoiceNo().substring(index);
		    		if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
		    		{
		    			salesDao.trxnApproved(VbookApp.getDbforServices(),sale_ref_id);
		    			Utils.displayNotification(this,ENotificationTypes.SALES_RETURN.name(),"Sales Return Approved Successfully ");
		    		}
		    		else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
		    		{
		    			salesDao.trxnDeclined(VbookApp.getDbforServices(),sale_ref_id);
		    			Utils.displayNotification(this,ENotificationTypes.SALES_RETURN.name(),"Sales Return Declined Successfully ");
		    		}
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }	    		
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Sales Returns change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.JOURNAL.name()) )
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{	    		
	    			int index = notification.getInvoiceNo().indexOf("#");
	    			index = index == -1?0:index+1;
	    			sale_ref_id = Constants.JN + notification.getInvoiceNo().substring(index);
	    			if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
	    			{
	    				salesDao.trxnApproved(VbookApp.getDbforServices(),sale_ref_id);
	    				Utils.displayNotification(this,ENotificationTypes.JOURNAL.name(),"Journal Approved Successfully ");
	    			}
	    			else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
	    			{
	    				salesDao.trxnDeclined(VbookApp.getDbforServices(),sale_ref_id);
	    				Utils.displayNotification(this,ENotificationTypes.JOURNAL.name(),"Journal Declined Successfully ");
	    			}
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Journal change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.CUSTOMER_CR.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
		    		sale_ref_id = notification.getInvoiceNo();
		    		if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
		    		{
		    			customerDao.changeRequestApproved(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id,notification.getBusinessName());
					    CustomerProductsCostList customerProductsCostList = restTemplate.getForObject(PRODUCTS_COST_URL,CustomerProductsCostList.class,saleEmpId,orgId);
					    if(customerProductsCostList!=null)
					    {
					    	customerDao.insertCustomerProductCosts(VbookApp.getDbforServices(),customerProductsCostList.getCustproductsCostList());						    
						    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
						    if(result != null && result.getStatusCode()==200 )
						    {
						    	VbookApp.getDbforServices().setTransactionSuccessful();
						    	isSucess = true;
						    }
					    }
		    		}
		    		else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
		    		{
		    			customerDao.changeRequestDeclined(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id,notification.getBusinessName());
		    			Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
		    			if(result != null && result.getStatusCode()==200 )
		    			{
		    				VbookApp.getDbforServices().setTransactionSuccessful();
		    				isSucess = true;
		    			}
		    		}
		    		Utils.displayNotification(this,ENotificationTypes.CUSTOMER_CR.name(),"Customer change requested update Successfully ");
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Customer change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.DN_TXN_CR.name()) || notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.DN_PAYMENTS_TXN_CR.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
		    		int index = notification.getInvoiceNo().indexOf("#");
		    		index = index == -1?0:index+1;
		    		sale_ref_id = Constants.DN + notification.getInvoiceNo().substring(index);
		    		if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
		    			salesDao.changeRequestApproved(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id);	
		    		else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
		    			salesDao.changeRequestDeclined(VbookApp.getDbforServices(),sale_ref_id+Constants.CR);
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }
		    		Utils.displayNotification(this,ENotificationTypes.DN_TXN_CR.name(),"Delivery note change requested update Successfully ");
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Delivery Note change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.SR_TXN_CR.name()))
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
		    		int index = notification.getInvoiceNo().indexOf("#");
		    		index = index == -1?0:index+1;
		    		sale_ref_id = Constants.SR + notification.getInvoiceNo().substring(index);
		    		if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
		    			salesDao.changeRequestApproved(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id);	
		    		else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
		    			salesDao.changeRequestDeclined(VbookApp.getDbforServices(),sale_ref_id+Constants.CR);
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }
		    		Utils.displayNotification(this,ENotificationTypes.SR_TXN_CR.name(),"Sales Return change requested update Successfully ");
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Sales Returns change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.JOURNAL_TXN_CR.name()) )
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{	    		
	    			int index = notification.getInvoiceNo().indexOf("#");
	    			index = index == -1?0:index+1;
	    			sale_ref_id = Constants.JN + notification.getInvoiceNo().substring(index);
	    			if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
	    				salesDao.changeRequestApproved(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id);	
	    			else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
	    				salesDao.changeRequestDeclined(VbookApp.getDbforServices(),sale_ref_id+Constants.CR);
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }
	    			Utils.displayNotification(this,ENotificationTypes.JOURNAL_TXN_CR.name(),"Journal change requested update Successfully ");
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Journal change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
	    	else if(notification.getNotificationType().equalsIgnoreCase(ENotificationTypes.DB_TXN_CR.name()) )
	    	{
	    		VbookApp.getDbforServices().beginTransaction();
	    		try
	    		{
	    			int index = notification.getInvoiceNo().indexOf("#");
	    			index = index == -1?0:index+1;
	    			sale_ref_id = notification.getInvoiceNo().substring(index);
	    			if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.APPROVED.name()))
	    				salesDao.changeRequestApproved(VbookApp.getDbforServices(),sale_ref_id+Constants.CR,sale_ref_id);	
	    			else if(notification.getNotificationStatus().equalsIgnoreCase(CRStatus.DECLINE.name()))
	    				salesDao.changeRequestDeclined(VbookApp.getDbforServices(),sale_ref_id+Constants.CR);
				    Response result = restTemplate.getForObject(UPDATE_NOTIFI_URL,Response.class,notification.getId());
				    if(result != null && result.getStatusCode()==200 )
				    {
				    	VbookApp.getDbforServices().setTransactionSuccessful();
				    	isSucess = true;
				    }
	    			Utils.displayNotification(this,ENotificationTypes.DB_TXN_CR.name(),"Day book change requested update Successfully ");
	    		}
	    		catch(Exception e)
	    		{
	    			Utils.displayNotification(this,"Error at Day book change Request Approved Failed.",e.getMessage());
	    			Toast.makeText(VbookApp.getInstance(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	    		}
	    		finally {
	    			VbookApp.getDbforServices().endTransaction();
	            }
	    	}
		}
		return 0;
    }// checkPendingNotification		

	/**
	 * 
	 */
	/*private boolean updateDayId(boolean isAtAllottedTime)
	{
		try
		{
			if(Utils.getData(this, saleEmpId + Constants.IS_CYCLE_CLOSED, "NO").equalsIgnoreCase("YES") || isAtAllottedTime)
			{
				RestTemplate restTemplate = RestTemplateFactory.getInstance(VbookApp.getInstance());
				Log.d(TAG,"Sending DN request to server...." + DATE_URL);
				String dateId =  restTemplate.getForObject(DATE_URL, String.class);
				if(dateId!= null)
				{
					// write code while changing Day ID
					Utils.saveData(this, Constants.CYCLE_ID,dateId);
					return true;
				}
			}
		}
		catch (RestClientException  e)	{e.printStackTrace();	Log.e(TAG, e.getLocalizedMessage(), e);	} 
		catch (Exception scx)			
		{
			Utils.displayNotification(this,"Error at synchronization Day ID.",scx.getMessage());
			Toast.makeText(VbookApp.getInstance(), scx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}*/
	
	/**
	 * 
	 */
	private boolean updateAllotmentId(String saleBookId)
	{
		try
		{
			// vbook server sale id validation
			if(saleBookId == null || saleBookId.isEmpty())
			{
				Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"Sales book cycle ID not available.");
				return false;
			}
							
			String[] strArray = saleBookId.split(Constants.SALES_BOOK_SEPARATOR);
			if(strArray.length!=4)
			{
				Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"Sale cycle ID Formating Failed.");
				return false;
			}
			String dayId = TimeUtils.getDateId();
			if(dayId == null)
			{
				Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"wrong App Date format.");
				return false;
			}
			String  cycleID = dayId + strArray[3];			
			String cyclestatus = Utils.getData(this, saleEmpId + Constants.IS_CYCLE_CLOSED, "NA"); 
			if(cyclestatus.equalsIgnoreCase("NA") || cyclestatus.equalsIgnoreCase("YES")) // test running Cycle id closed or not
				Utils.saveData(this, saleEmpId + Constants.CYCLE_ID,cycleID);
			else
			{
				Utils.displayNotification(this,ENotificationTypes.ALLOTTED.name(),"please close Day book new Allotment is Ready.");
				return false;
			}				
			return true;
		}
		catch (Exception ex)			
		{
			Utils.displayNotification(this,"Error at synchronization Sales Book.",ex.getMessage());
			//Toast.makeText(VbookApp.getInstance(), scx.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	
	/*class PInfo 
	{
		private String appname = "";
		private String pname = "";
		private String versionName = "";
		private int versionCode = 0;
	}
	
	private void  uninstallApps()
	{
		Shell shell = null;
		try 
		{
			if(RootCommands.rootAccessGiven())
				Toast.makeText(VbookApp.getInstance(), "root Access Given", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(VbookApp.getInstance(), "no Root permession", Toast.LENGTH_LONG).show();
			shell = Shell.startRootShell();
			ArrayList<PInfo> allInstalledApps  = getInstalledApps();
			for (int i = 0; i < allInstalledApps.size(); i++) 
			{
				int j = 0;
				for (; j < ALLOWED_APPS.length; j++) 
				{
					if(allInstalledApps.get(i).appname.equalsIgnoreCase(ALLOWED_APPS[j]))
					{
						break;
					}
				}
				if(j == ALLOWED_APPS.length )
				{
					// uninstall App here or Disable if it is System app p.versionName == null
	
					SimpleBinaryCommand cmd = new SimpleBinaryCommand(VbookApp.getInstance(),"pm","uninstall " + allInstalledApps.get(i).pname);
					Toast.makeText(VbookApp.getInstance(), cmd.getOutput(), Toast.LENGTH_SHORT).show();
					
				}
			}
		} 
		catch (RootAccessDeniedException e){e.printStackTrace();} 
		catch (IOException e) {	e.printStackTrace();}
		finally{
			if(shell!=null)
				try {
					shell.close();
				} catch (IOException e){
					e.printStackTrace();
				}
		}
	}
	
	private ArrayList<PInfo> getInstalledApps() 
    {
        ArrayList<PInfo> res = new ArrayList<PInfo>();        
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) 
        {
            PackageInfo p = packs.get(i);
            PInfo newInfo 		= new PInfo();
            newInfo.appname 	= p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname 		= p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            //newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());
            res.add(newInfo);
        }
        return res; 
    }*/
}