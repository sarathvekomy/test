package com.vekomy.vbooks.organization.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.organization.command.SystemDefaultsCommand;
import com.vekomy.vbooks.organization.dao.SyStemDefaultsDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultSuccess;

/**
 *  This action class is responsible to perform SystemDefaults  operations.
 *  
 * @author priyanka
 *
 */
public class SystemDefaultsAction extends BaseAction {

	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(SystemDefaultsAction.class);
	@Override
	public IResult process(Object form) throws Exception {
	
		SystemDefaultsCommand systemDefaultsCommand = null;
		ResultSuccess resultSuccess = new ResultSuccess();
		SyStemDefaultsDao syStemDefaultsDao = (SyStemDefaultsDao)getDao();
 		if(form instanceof SystemDefaultsCommand){
 			systemDefaultsCommand=(SystemDefaultsCommand)form;
 			String action = systemDefaultsCommand.getAction();
			if("add-payment-type".equals(action)){
				String val = systemDefaultsCommand.getValue();
				String description = systemDefaultsCommand.getDescription();
				syStemDefaultsDao.addPaymentTypes(val,description,getUsername(),getOrganization());
				resultSuccess.setMessage("Payment type added succecssfully");
			} else if("get-all-payment-types".equals(action)){
				List<String>paymentTypesList = syStemDefaultsDao.getAllPaymentTypes(getOrganization());
				resultSuccess.setData(paymentTypesList);
				resultSuccess.setMessage("Payment Types Retrieved Successfully");
			} else if("get-all-employee-types".equals(action)){
				List<String> employeetypesList = syStemDefaultsDao.getAllEmployeeTypes(getOrganization());
				resultSuccess.setData(employeetypesList);
				resultSuccess.setMessage("Employee Types Retrieved Successfully");
			} else if("get-all-address-types".equals(action)){
				List<String>addressTypesList = syStemDefaultsDao.getCrAddressTypes(getOrganization());
				resultSuccess.setData(addressTypesList);
				resultSuccess.setMessage("Address Types Retrieved Successfully");
			}else if("get-all-journal-types".equals(action)){
				List<String>journalTypesList = syStemDefaultsDao.getJournalTypes(getOrganization());
				resultSuccess.setData(journalTypesList);
				resultSuccess.setMessage("Journal Types Retrieved Successfully");
			}else if("add-address-type".equals(action)){
				String val = systemDefaultsCommand.getValue();
				String description = systemDefaultsCommand.getDescription();
				syStemDefaultsDao.addAddressTypes(val,description,getOrganization(),getUsername());
				resultSuccess.setMessage("Address type added successfully");
			} else if("add-employee-type".equals(action)){
				String value = systemDefaultsCommand.getValue();
				String description = systemDefaultsCommand.getDescription();
				syStemDefaultsDao.addemployeeTypes(value,description,getOrganization(),getUsername());
				resultSuccess.setMessage("Employee type added successfully");
			}else if("add-journals".equals(action)){
				String value = systemDefaultsCommand.getValue();
				String description = systemDefaultsCommand.getDescription();
				String invoiceNo = request.getParameter("invoiceNo");
				syStemDefaultsDao.addJournalTypes(value,description,getOrganization(),getUsername(),invoiceNo);
				resultSuccess.setMessage("Journal type added successfully");
			}else if("delete-pay-types".equals(action)){
				String description = request.getParameter("description");
				syStemDefaultsDao.deletePaymentTypes(description,getOrganization());
				resultSuccess.setMessage("Payment type deleted successfully");
			} else if("delete-emp-types".equals(action)){
				String description = request.getParameter("description");
				syStemDefaultsDao.deleteEmployeeTypes(description,getOrganization());
				resultSuccess.setMessage("Employee type deleted successfully");
			} else if("delete-add-types".equals(action)){
				String value =request.getParameter("description");
				syStemDefaultsDao.deleteAddressTypes(value,getOrganization());
				resultSuccess.setMessage("Address type deleted successfully");
			} else if("update-emp-types".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				syStemDefaultsDao.updateEmployeeTypes(id,getOrganization(),systemDefaultsCommand.getValue(),systemDefaultsCommand.getDescription());
				resultSuccess.setMessage("Employee type updated successfully");
			} else if("update-add-types".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				syStemDefaultsDao.updateAddressTypes(id,getOrganization(),systemDefaultsCommand.getValue(),systemDefaultsCommand.getDescription(),getUsername());
				resultSuccess.setMessage("Address type updated successfully");
			} else if("update-pay-types".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				syStemDefaultsDao.updatePayTypes(id,getOrganization(),systemDefaultsCommand.getValue(),systemDefaultsCommand.getDescription(),getUsername());
				resultSuccess.setMessage("Payment type updated successfully");
			} else if("get-emp-id".equals(action)){
				int id=syStemDefaultsDao.getEmpId(systemDefaultsCommand.getDescription(),getOrganization());
				resultSuccess.setData(id);
				resultSuccess.setMessage("Id Has Retrieved Successfully");
			} else if("get-add-id".equals(action)){
				int id=syStemDefaultsDao.getAddId(systemDefaultsCommand.getValue(),getOrganization());
				resultSuccess.setData(id);
				resultSuccess.setMessage("Id Has Retrieved Successfully");
			} else if("get-pay-id".equals(action)){
				int id = syStemDefaultsDao.getPayId(systemDefaultsCommand.getValue(),getOrganization());
				if(id!=0){
					resultSuccess.setData(id);
					resultSuccess.setMessage("Id Has Retrieved Successfully");
				}
			} else if("get-journal-id".equals(action)){
				int id = syStemDefaultsDao.getJournalId(systemDefaultsCommand.getValue(),getOrganization());
				if(id!=0){
					resultSuccess.setData(id);
					resultSuccess.setMessage("Id Has Retrieved Successfully");
				}
			}
			else if("validate-address-type".equals(action)){
				List<VbAddressTypes> vbAddressTypes =syStemDefaultsDao.validateAddressType(systemDefaultsCommand.getValue(),getOrganization());
					resultSuccess.setData(vbAddressTypes);
					resultSuccess.setMessage("This Address Type Is Already Present");
			} else if("validate-payment-type".equals(action)){
				List<VbPaymentTypes> vbPaymentTypes=syStemDefaultsDao.validatePaymentType(systemDefaultsCommand.getValue(),getOrganization());
				resultSuccess.setData(vbPaymentTypes);
				resultSuccess.setMessage("This Payment Type is Already Present");
			} else if("validate-employee-type".equals(action)){
				List<VbRole> vbRole = syStemDefaultsDao.validateEmployeeType(systemDefaultsCommand.getValue(),getOrganization());
				resultSuccess.setData(vbRole);
				resultSuccess.setMessage("This Employee Type is Already Present");
			} else if("validate-journal-type".equals(action)){
				List<VbJournalTypes> vbJournalTypes = syStemDefaultsDao.validateJournalTypes(systemDefaultsCommand.getValue(),getOrganization());
				resultSuccess.setData(vbJournalTypes);
				resultSuccess.setMessage("This Journal Type is Already Present");
			}
			else if("get-address-types".equals(action)){
				List<String> addressType=syStemDefaultsDao.getCrAddressTypes(getOrganization());
				if(addressType != null){
					resultSuccess.setData(addressType);
				}
			}else if("update-journal-types".equals(action)){
				int id = Integer.parseInt(request.getParameter("id"));
				String invoiceNo = request.getParameter("invoiceNo");
				syStemDefaultsDao.updateJournalTypes(id,getOrganization(),systemDefaultsCommand.getValue(),systemDefaultsCommand.getDescription(),invoiceNo);
				resultSuccess.setMessage("Journal type updated successfully");
			}else if("delete-journal-types".equals(action)){
				String value = request.getParameter("value");
				syStemDefaultsDao.deleteJournalTypes(value,getOrganization());
				resultSuccess.setMessage("Journal type  deleted successfully");
			}
			if (_logger.isDebugEnabled()) {
				_logger.debug("resultSuccess: {}", resultSuccess);
			}
		}
		return resultSuccess;
	}

}
