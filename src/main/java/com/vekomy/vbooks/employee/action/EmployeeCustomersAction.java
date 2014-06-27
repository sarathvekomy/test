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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCustomerResult;
import com.vekomy.vbooks.employee.command.EmployeeCustomersCommand;
import com.vekomy.vbooks.employee.dao.EmployeeCustomerDao;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.Msg.MsgEnum;
import com.vekomy.vbooks.util.OrganizationUtils;

/**
 * @author Vinay
 *
 */
public class EmployeeCustomersAction extends BaseAction {
	
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeCustomersAction.class);
	
	
	
	/* (non-Javadoc)
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) {
		try {
			EmployeeCustomersCommand employeeCustomersCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			if(form instanceof EmployeeCustomersCommand){
				VbOrganization organization = getOrganization();
				String loginUser = getUsername();
				String resultSuccessStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
				String resultSuccessMessage = Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE);
				employeeCustomersCommand =(EmployeeCustomersCommand) form;
				String action=employeeCustomersCommand.getAction();
				EmployeeCustomerDao employeeCustomerDao = (EmployeeCustomerDao) getDao();
				if("get-employee-names".equals(action)){
					String employeeName = request.getParameter("employeeNameVal");
					List <String> employeeList = employeeCustomerDao.getEmployees(organization,employeeName);
					
					resultSuccess.setData(employeeList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if ("get-localities".equals(action)) {
					List <String> localityList = employeeCustomerDao.getLocalityList(organization);
					
					resultSuccess.setData(localityList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("get-customer".equals(action)){
					String locality = request.getParameter("localityVal");
					List<String> customers = employeeCustomerDao.getCustomerLocalities(organization,locality);
					
					resultSuccess.setData(customers);
					resultSuccess.setStatus(resultSuccessStatus);
				    resultSuccess.setMessage(resultSuccessMessage);	
				} else if("assign-customers".equals(action)){
					String userName = request.getParameter("userName");
					String businessNameString = request.getParameter("businessArray");
					Boolean saved = employeeCustomerDao.saveSingleEmployeeCustomer(userName,businessNameString,organization, loginUser);
					
					if(saved == Boolean.FALSE){
						resultSuccess.setStatus(resultSuccessStatus);
						resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_FAILURE_MESSAGE));
					}else{
						resultSuccess.setStatus(resultSuccessStatus);
						resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));		
					}
				} else if("search-assigned-customers".equals(action)){
					List<EmployeeCustomerResult> employeeList = employeeCustomerDao.searchAssignedCustomer(employeeCustomersCommand, organization);
					
					resultSuccess.setData(employeeList);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(resultSuccessMessage);
				} else if("delete-employeecustomer".equals(action)){
					String customerIdList=request.getParameter("idList");
					String salesExecutive=request.getParameter("salesExecutive");
					employeeCustomerDao.deleteEmployeeCustomer(customerIdList, salesExecutive, loginUser, organization);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("deassign-customer".equals(action)){	
					employeeCustomerDao.deAssignEmployeeCustomer(employeeCustomersCommand.getBusinessName(),
							employeeCustomersCommand.getSalesExecutive(),organization, loginUser);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("get-customers-by-locality".equals(action)){
					String locality = request.getParameter("locality");
					List<String> businessNames = employeeCustomerDao.getBusinessNames(locality, organization);
					
					resultSuccess.setData(businessNames);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-customernames-by-id".equals(action)){
					String userName=request.getParameter("userName");
					Integer id = Integer.parseInt(request.getParameter("id"));
					List<String> businessnames = employeeCustomerDao.getBusinessNamesByEmployee(userName, organization, id);
					
					resultSuccess.setData(businessnames);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("update-assigned-customer".equals(action)){
					String businessName=request.getParameter("businessName");
					employeeCustomerDao.updateEmployeeCustomer(employeeCustomersCommand, organization, businessName, loginUser);
					
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("check-availability-for-businessName".equals(action)){
					String businessName=request.getParameter("businessName");
					String salesExecutive=request.getParameter("salesExecutive");
					Boolean result = employeeCustomerDao.checkBusinessNameAvailability(businessName, salesExecutive, organization);
					
					resultSuccess.setData(result);
					resultSuccess.setStatus(resultSuccessStatus);
					if(result){
						resultSuccess.setMessage(Msg.get(MsgEnum.ASSIGN_CUSTOMERS_BUSINESS_NAME_EXISTS_MESSAGE));
					}else{
						resultSuccess.setMessage(Msg.get(MsgEnum.ASSIGN_CUSTOMERS_BUSINESS_NAME_NOT_EXISTS_MESSAGE));
					}
				} else if("check-for-selected-businessName".equals(action)){
					String businessName=request.getParameter("businessName");
					Boolean userExists = employeeCustomerDao.getEmployeesForSelectedBusinessName(businessName, organization);
					
					resultSuccess.setData(userExists);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}else if("check-for-disabled-salesExecutives".equals(action)){
					String businessName=request.getParameter("businessName");
					Boolean isEnabledEmployeeExist = employeeCustomerDao.isEnabledEmployeeExist(businessName, organization);
					
					resultSuccess.setData(isEnabledEmployeeExist);
					resultSuccess.setStatus(resultSuccessStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				}
			}
			
			if (_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("resultError: {}", resultError);
			}
			return resultError;
		}
	}
}