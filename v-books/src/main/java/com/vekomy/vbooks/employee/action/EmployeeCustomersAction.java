/**
 * com.vekomy.vbooks.customer.action.CustomerAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: may 20, 2013
 */
package com.vekomy.vbooks.employee.action;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCustomerResult;
import com.vekomy.vbooks.employee.command.EmployeeCustomersCommand;
import com.vekomy.vbooks.employee.dao.EmployeeCustomerDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * @author priyanka
 *
 */
public class EmployeeCustomersAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeCustomersAction.class);
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;
	
	/**
	 * HashMap<String,String> variable holds assigncustomerMap.
	 */
	private HashMap<String, String> assigncustomerMap = new HashMap<String, String>();
	
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IResult process(Object form) throws Exception {
		EmployeeCustomersCommand employeeCustomersCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		if(form instanceof EmployeeCustomersCommand){
			employeeCustomersCommand =(EmployeeCustomersCommand) form;
			String action=employeeCustomersCommand.getAction();
			EmployeeCustomerDao employeeCustomerDao = (EmployeeCustomerDao) getDao();
			if("get-employee-names".equals(action)){
				String employeeName = request.getParameter("employeeNameVal");
				List <String> employeeList = employeeCustomerDao.getEmployees(getOrganization(),employeeName);
				resultSuccess.setData(employeeList);
				resultSuccess.setMessage("Employee Names Retrieved Successfully");
			} else if ("get-localities".equals(action)) {
				List <String> localityList = employeeCustomerDao.getLocalityList(getOrganization());
				resultSuccess.setData(localityList);
			}
			else if("get-customer".equals(action)){
				String locality = request.getParameter("localityVal");
				List<String> customers = employeeCustomerDao.getCustomerLocalities(getOrganization(),locality);
				resultSuccess.setData(customers);
			    resultSuccess.setMessage("CustomerNames Are Retrieved SuccessFully");	
			} else if ("assign-customers-multiple".equals(action)) {
				String businessNameString = request.getParameter("businessNameString");
				String locality = request.getParameter("locality");
				assigncustomerMap.put(locality, businessNameString);
				session = request.getSession(Boolean.TRUE);
				session.setAttribute("assignCustomersMap", assigncustomerMap);
			} else if("assign-customers".equals(action)){
				String userName = request.getParameter("userName");
				String locality = request.getParameter("locality");
				String businessNameString = request.getParameter("businessArray");
				session = request.getSession(Boolean.TRUE);
				HashMap<String, String> sessionBusinessNames = (HashMap<String, String>) session.getAttribute("assignCustomersMap");
				if(sessionBusinessNames == null){
					boolean saved = employeeCustomerDao.saveSingleEmployeeCustomer(userName,businessNameString,getOrganization(), getUsername());
					if(saved == Boolean.FALSE){
						resultSuccess.setData(saved);
						resultSuccess.setMessage("This Combination Has Already Assinged");
					}else{
						resultSuccess.setData(saved);
						resultSuccess.setMessage("Customers Assigned Successfully");
					}
				} else {
					assigncustomerMap.put(locality, businessNameString);
					boolean saved = employeeCustomerDao.saveEmployeeCustomer(userName,(HashMap<String, String>) session.getAttribute("assignCustomersMap"),getOrganization(), getUsername());
					if(saved == Boolean.FALSE){
						resultSuccess.setData(saved);
						resultSuccess.setMessage("This Combination Has Already Assinged");
					}else{
						resultSuccess.setData(saved);
						resultSuccess.setMessage("Customers Assigned Successfully");
					}
					
					removeEmployeeCustomerDataFromSession(session);
					assigncustomerMap.clear();
				}
			} else if("session-availability-for-businessName".equals(action)){
				Boolean flag=Boolean.FALSE;
				String businessName = request.getParameter("businessNameSession");
				String locality = request.getParameter("locality");
				session = request.getSession(Boolean.TRUE);
				HashMap<String, String> sessionBusinessNames = (HashMap<String, String>) session.getAttribute("assignCustomersMap");
				if(sessionBusinessNames != null){
				String businessNames =sessionBusinessNames.get(locality);
				if(businessNames != null){
				String[] businessNamesArray = businessNames.split(",");
				List<String> businessNameList = Arrays.asList(businessNamesArray);
				for(String businessNameSession : businessNameList){
					if(businessNameSession.equals(businessName)){
						flag=Boolean.TRUE;
						break;
					} else{
						flag=Boolean.FALSE;
					}
				}
				}else{
					flag = Boolean.FALSE;
				}
				}else{
					flag = Boolean.FALSE;
				}
				resultSuccess.setData(flag);
			} else if ("remove-session".equals(action)) {
				if(!assigncustomerMap.isEmpty()){
				session = request.getSession(Boolean.TRUE);
				assigncustomerMap.clear();
				removeEmployeeCustomerDataFromSession(session);
				}
			}else if("search-assigned-customers".equals(action)){
				List<EmployeeCustomerResult>employeeList = employeeCustomerDao.searchAssignedCustomer(employeeCustomersCommand, getOrganization());
				resultSuccess.setData(employeeList);
				resultSuccess.setMessage("Search Completed Successfully");
			}
			else if("delete-employeecustomer".equals(action)){
				String customerIdList=request.getParameter("idList");
				employeeCustomerDao.deleteEmployeeCustomer(customerIdList);
				resultSuccess.setMessage("Deleted Successfully");
			}else if("deassign-customer".equals(action)){
				employeeCustomerDao.deAssignEmployeeCustomer(employeeCustomersCommand.getBusinessName(),getOrganization());
				resultSuccess.setMessage("Deleted Successfully");
			}else if("get-customers-by-locality".equals(action)){
				String locality = request.getParameter("locality");
				List<String> businessNames=employeeCustomerDao.getBusinessNames(locality, getOrganization());
				resultSuccess.setData(businessNames);
				resultSuccess.setMessage("Customers Are Retrieved Successfully");
			}else if("get-customernames-by-id".equals(action)){
				String userName=request.getParameter("userName");
				int id = Integer.parseInt(request.getParameter("id"));
				List<String> businessnames=employeeCustomerDao.getBusinessNamesByEmployee(userName, getOrganization(), id);
				resultSuccess.setData(businessnames);
				resultSuccess.setMessage("BusinessNames Are Retrieved Successfully");
			}else if("update-assigned-customer".equals(action)){
				String businessName=request.getParameter("businessName");
				if(businessName != null){
				employeeCustomerDao.updateEmployeeCustomer(employeeCustomersCommand,getOrganization(),businessName,getUsername());
				resultSuccess.setMessage("updated Successfully");
				}
			}else if("check-availability-for-businessName".equals(action)){
				String businessName=request.getParameter("businessName");
				String salesExecutiveName=employeeCustomerDao.checkBusinessNameAvailability(businessName,getOrganization());
				resultSuccess.setData(salesExecutiveName);
				if(salesExecutiveName != null){
					resultSuccess.setMessage("Business Name Exists");
				}else{
					resultSuccess.setMessage("Business Name is Not Exist.You Can have it");
				}
			}
		}
		if(_logger.isDebugEnabled()){
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}
	
	/*
	 * This method is responsible to remove data from session. 
	 */
	private void removeEmployeeCustomerDataFromSession(HttpSession session){
		session.removeAttribute("assignCustomersMap");
	}

}