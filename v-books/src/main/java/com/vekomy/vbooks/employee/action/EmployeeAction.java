/**
 * com.vekomy.vbooks.employee.action.EmployeeAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 10, 2013
 */
package com.vekomy.vbooks.employee.action;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeCommand;
import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.employee.dao.EmployeeDao;
import com.vekomy.vbooks.hibernate.model.VbEmployeeAddress;
import com.vekomy.vbooks.hibernate.model.VbEmployeeDetail;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.util.SearchFilterData;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 * This action class is responsible to process the employee activities.
 * 
 * @author Sudhakar
 * 
 * 
 */
public class EmployeeAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(EmployeeAction.class);
	
	/**
	 * HttpSession variable holds session.
	 */
	private HttpSession session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	public IResult process(Object form) throws Exception {
		EmployeeCommand employeeCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		if (form instanceof EmployeeCommand) {
			employeeCommand = (EmployeeCommand) form;
			EmployeeDao employeeDao = (EmployeeDao) getDao();
			if ("save-basic".equals(employeeCommand.getAction())) {
				session = request.getSession(Boolean.TRUE);
				String employeeNo = employeeDao.generateEmployeeNumber( getOrganization(), employeeCommand.getEmployeeType());
				employeeCommand.setEmployeeNumber(employeeNo);
				session.setAttribute("employee-basic", employeeCommand);
				resultSuccess.setMessage("Saved Successfully");
			} else if ("save-employee".equals(employeeCommand.getAction())) {
				session = request.getSession(Boolean.TRUE);
				VbOrganization organization = getOrganization();
				employeeDao.saveEmployee((EmployeeCommand) session.getAttribute("employee-basic"),
						(VbEmployeeDetail) session.getAttribute("employee-detail"),
						(VbEmployeeAddress) session.getAttribute("employee-address"),
						getUsername(), organization);

				// removing the session data.
				removeEmployeeDataFromSession();
				resultSuccess.setMessage("Saved Successfully");
			}
			
			else if ("save-cancel".equals(employeeCommand.getAction())) {
				removeEmployeeDataFromSession();
				resultSuccess.setMessage("Save canceled Succesfull");
			} else if ("search-employee".equals(employeeCommand.getAction())) {
				HashMap<String, SearchFilterData> filter = new HashMap<String, SearchFilterData>();
				filter.put("username", new SearchFilterData("username", employeeCommand.getUsername(), SearchFilterData.TYPE_STRING_STR));
				filter.put("firstName", new SearchFilterData("firstName", employeeCommand.getFirstName(), SearchFilterData.TYPE_STRING_STR));
				List<EmployeeResult> list = employeeDao.searchEmployee(employeeCommand, getOrganization());
			if (list.isEmpty()) {
				resultSuccess.setMessage("No Records Found");
			} else {
				resultSuccess.setData(list);
				resultSuccess.setMessage("Search Successfully");
			}
			} else if ("update-employee".equals(employeeCommand.getAction())) {
				employeeDao.updateEmployee((EmployeeCommand) session.getAttribute("employee-basic"),
						(VbEmployeeDetail) session.getAttribute("employee-detail"),
						(VbEmployeeAddress) session.getAttribute("employee-address"),
						getUsername(), getOrganization());
				// removing the session data.
				removeEmployeeDataFromSession();
				resultSuccess.setMessage("Updated Successfully");
			}else if ("delete-employee".equals(employeeCommand.getAction())) {
				employeeDao.deleteEmployee(employeeCommand , getOrganization());
				resultSuccess.setMessage("Deleted Successfully");
			}
		} else if (form instanceof VbEmployeeDetail) {
			VbEmployeeDetail employeeDetail = (VbEmployeeDetail) form;
			session = request.getSession(Boolean.TRUE);
			session.setAttribute("employee-detail", employeeDetail);
			resultSuccess.setMessage("Saved Successfully");
		} else if (form instanceof VbEmployeeAddress) {
			VbEmployeeAddress employeeAddress = (VbEmployeeAddress) form;
			session = request.getSession(Boolean.TRUE);
			session.setAttribute("employee-address", employeeAddress);
			resultSuccess.setMessage("Saved Successfully");
		}
		
		if (_logger.isDebugEnabled()) {
			_logger.debug("resultSuccess: {}", resultSuccess);
		}
		return resultSuccess;
	}
	
	/*
	 * This method is responsible to remove data from session. 
	 */
	private void removeEmployeeDataFromSession(){
		session = request.getSession(Boolean.TRUE);
		session.removeAttribute("employee-basic");
		session.removeAttribute("employee-detail");
		session.removeAttribute("employee-address");
	}
	
	
	/**
	 * This inner class is to perform customized sorting on {@link EmployeeResult}. 
	 * 
	 * @author Sudhakar
	 *
	 */
	class EmployeeComparator implements Comparator<EmployeeResult> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(EmployeeResult str1, EmployeeResult str2) {
			String str3 = str1.getFirstName();
			String str4 = str2.getFirstName();
			return str3.compareToIgnoreCase(str4);
		}

	}

}
