/**
 * com.vekomy.vbooks.app.controller.RestController.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vekomy.vbooks.app.dao.AuthenticateDao;
import com.vekomy.vbooks.app.dao.DayBookDao;
import com.vekomy.vbooks.app.dao.DeliveryNoteDao;
import com.vekomy.vbooks.app.dao.JournalDao;
import com.vekomy.vbooks.app.dao.SalesReturnDao;
import com.vekomy.vbooks.app.dao.TransactionChangeRequestDao;
import com.vekomy.vbooks.app.exception.DataAccessException;
import com.vekomy.vbooks.app.request.CustomersInfoRequest;
import com.vekomy.vbooks.app.request.DayBook;
import com.vekomy.vbooks.app.request.DayBookCR;
import com.vekomy.vbooks.app.request.DeliveryNote;
import com.vekomy.vbooks.app.request.DeliveryNoteCR;
import com.vekomy.vbooks.app.request.Journal;
import com.vekomy.vbooks.app.request.JournalCR;
import com.vekomy.vbooks.app.request.LoginRequest;
import com.vekomy.vbooks.app.request.LoginRequestForCAU;
import com.vekomy.vbooks.app.request.PasswordChangeRequest;
import com.vekomy.vbooks.app.request.SalesReturn;
import com.vekomy.vbooks.app.request.SalesReturnCR;
import com.vekomy.vbooks.app.response.AllocatedProductList;
import com.vekomy.vbooks.app.response.CustomerAmountInfoList;
import com.vekomy.vbooks.app.response.CustomerInfo;
import com.vekomy.vbooks.app.response.CustomerInfoList;
import com.vekomy.vbooks.app.response.CustomerProductsCostList;
import com.vekomy.vbooks.app.response.LoginResponse;
import com.vekomy.vbooks.app.response.LoginResponseForCAU;
import com.vekomy.vbooks.app.response.Response;
import com.vekomy.vbooks.app.response.SystemNotificationList;
import com.vekomy.vbooks.app.utils.TimeUtils;

/**
 * @author NKR
 * 
 */
@Controller
@RequestMapping("/app")
public class RestController {

	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@Inject
	private AuthenticateDao 			loginDao;
	@Inject
	private DeliveryNoteDao 			deliveryNoteDao;
	@Inject
	private SalesReturnDao 				salesReturnDao;
	@Inject
	private JournalDao					journalDao;	
	@Inject
	private DayBookDao 					dayBookDao;
	@Inject
	private TransactionChangeRequestDao trxnChangeDao;
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		logger.info("Vekomy Vbooks Android Application");
		return "index";
	}

	// http://localhost:8080/vbooks-app-server/app/LoginAuth
	@RequestMapping(value = { "/LoginAuth" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public LoginResponse validateUser(@ModelAttribute("loginUser") LoginRequest loginUser) {
		try {
			return loginDao.authenticate(loginUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/////////////////////////////THIS IS A TEMP METHOD FOR WORK AROUND---->START///////////////////////////////
	// http://localhost:8080/vbooks-app-server/app/LoginAuthForCAU
		@RequestMapping(value = { "/LoginAuthForCAU" }, method = RequestMethod.POST, produces = { "application/json" })
		@ResponseBody
		public LoginResponseForCAU authentication(@RequestBody LoginRequestForCAU loginUser) {
			try {
				return loginDao.authenticateForCAU(loginUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	/////////////////////////////THIS IS A TEMP METHOD FOR WORK AROUND---->END///////////////////////////////
		
	@RequestMapping(value = { "/passwordchange" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response passwordchange(@RequestBody PasswordChangeRequest request) {
		try {
			return loginDao.changePassword(request);
		}
		catch (DataAccessException dbex) {
			Response response = new Response();
			response.setMessage(dbex.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	//
	//************************************** Daily Allotment *****************************************************
	//	
	
	@RequestMapping(value = { "/getNotification" }, method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody	
	public SystemNotificationList getAllSystemNotications(@RequestParam("uname") String salesExecutive, @RequestParam("orgID") Integer organizationId) {
		try {
			return loginDao.getAllSystemNotications(organizationId,salesExecutive);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value = { "/getDayId" }, method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody	
	public String getDayId() {
		try {
			return TimeUtils.getDateId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// http://localhost:8080/vbooks-app-server/app/customerlist?uname=12&orgID=12
	@RequestMapping(value = { "/assignedcustomerlist" }, method = RequestMethod.GET)
	@ResponseBody
	public CustomerAmountInfoList getAssignedcustomerlist(@RequestParam("uname") String userName, @RequestParam("orgID") Integer organizationId) {
		try {
			return new CustomerAmountInfoList(deliveryNoteDao.getBusinessName(userName, organizationId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/newlyassignedcustomerlist" }, method = RequestMethod.POST)
	@ResponseBody
	public CustomerAmountInfoList getNewlyAssignedcustomerlist(@RequestBody CustomersInfoRequest request) {
		try {
			return new CustomerAmountInfoList(deliveryNoteDao.getnewlyAssignedBusinessName(request));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// http://localhost:8080/vbooks-app-server/app/productlist?uname=s2&orgID=1&bname=c2
	@RequestMapping(value = { "/allocatedproductlist" }, method = RequestMethod.GET)
	@ResponseBody
	public AllocatedProductList getAllocatedproductlist(@RequestParam("uname") String userName, @RequestParam("orgID") Integer organizationId) {
		try {
			return deliveryNoteDao.getProductList(userName, organizationId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping(value = { "/customerproductsCostlist" }, method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody	
	public CustomerProductsCostList getcustomerproductsCostlist(@RequestParam("uname") String salesExecutive, @RequestParam("orgID") Integer organizationId) {
		try {
			return new CustomerProductsCostList(deliveryNoteDao.getCustomerProductsCost(salesExecutive, organizationId));			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/assignedcustomerInfolist" }, method = RequestMethod.POST,produces = { "application/json" })
	@ResponseBody
	public CustomerInfoList getAssignedcustomerInfolist(@RequestBody CustomersInfoRequest request) {
		try {
			return deliveryNoteDao.getAssignedcustomerInfolist(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/updateNotification" }, method = RequestMethod.GET, produces = { "application/json" })
	@ResponseBody	
	public Response updateNotications(@RequestParam("UID") Integer id) {
		try {
			return loginDao.updateStatus(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//
	//************************************** Save Transactions *****************************************************
	//	
	@RequestMapping(value = { "/saveDN" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response saveDeliveryNote(@RequestBody DeliveryNote deliveryNote) {
		try {
			return deliveryNoteDao.saveDeliveryNote(deliveryNote);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/saveSR" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response saveSaleaReturn(@RequestBody SalesReturn deliveryNote) {
		try {
			return salesReturnDao.saveSalesReturn(deliveryNote);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/saveJN" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response saveJournal(@RequestBody Journal journal) {
		try {
			return journalDao.saveJournal(journal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/saveDB" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response saveDayBook(@RequestBody DayBook dayBook) {
		try {
			return dayBookDao.saveDayBook(dayBook, dayBook.getSaleExeName(), Integer.parseInt(dayBook.getOrgId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/saveCustomer" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response saveCustomer(@RequestBody CustomerInfo customer) {
		try {
			return deliveryNoteDao.saveCustomerInfo(customer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//
	//************************************** Change Request *****************************************************
	//
	@RequestMapping(value = { "/changeDN" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response changeDeliveryNote(@RequestBody DeliveryNoteCR request) {
		try {
			return trxnChangeDao.saveDeliveryNoteCR(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/changeSR" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response changeSaleaReturn(@RequestBody SalesReturnCR request) {
		try {
			return trxnChangeDao.saveSalesReturnCR(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/changeJN" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response changeJournal(@RequestBody JournalCR request) {
		try {
			return trxnChangeDao.saveJournalCR(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = { "/changeDB" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response changeDayBook(@RequestBody DayBookCR request) {
		try {
			return trxnChangeDao.saveDayBookCr(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*@RequestMapping(value = { "/changeCust" }, method = RequestMethod.POST, produces = { "application/json" })
	@ResponseBody
	public Response changeCustomer(@RequestBody CustomerCR request) {
		try {
			//return trxnChangeDao.saveDayBookCr(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
}