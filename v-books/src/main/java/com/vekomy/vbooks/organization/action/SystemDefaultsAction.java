/**
 * com.vekomy.vbooks.organization.action.SystemDefaultsAction.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Apr 19, 2013
 */
package com.vekomy.vbooks.organization.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vekomy.vbooks.employee.command.EmployeeResult;
import com.vekomy.vbooks.exception.DataAccessException;
import com.vekomy.vbooks.hibernate.model.VbAddressTypes;
import com.vekomy.vbooks.hibernate.model.VbJournalTypes;
import com.vekomy.vbooks.hibernate.model.VbOrganization;
import com.vekomy.vbooks.hibernate.model.VbPaymentTypes;
import com.vekomy.vbooks.hibernate.model.VbRole;
import com.vekomy.vbooks.organization.command.SystemDefaultsCommand;
import com.vekomy.vbooks.organization.command.SystemDefaultsResult;
import com.vekomy.vbooks.organization.dao.SyStemDefaultsDao;
import com.vekomy.vbooks.spring.action.BaseAction;
import com.vekomy.vbooks.spring.action.IResult;
import com.vekomy.vbooks.spring.action.ResultError;
import com.vekomy.vbooks.spring.action.ResultSuccess;
import com.vekomy.vbooks.util.Msg;
import com.vekomy.vbooks.util.OrganizationUtils;
import com.vekomy.vbooks.util.Msg.MsgEnum;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vekomy.vbooks.spring.action.BaseAction#process(java.lang.Object)
	 */
	@Override
	public IResult process(Object form) {
		try {
			SystemDefaultsCommand systemDefaultsCommand = null;
			ResultSuccess resultSuccess = new ResultSuccess();
			String successStatus = OrganizationUtils.RESULT_STATUS_SUCCESS;
			SyStemDefaultsDao syStemDefaultsDao = (SyStemDefaultsDao)getDao();
			VbOrganization organization = getOrganization();
			String userName = getUsername();
	 		if(form instanceof SystemDefaultsCommand){
	 			systemDefaultsCommand=(SystemDefaultsCommand)form;
	 			String action = systemDefaultsCommand.getAction();
				if("add-payment-type".equals(action)){
					String val = systemDefaultsCommand.getValue();
					String description = systemDefaultsCommand.getDescription();
					syStemDefaultsDao.addPaymentTypes(val, description, userName, organization);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if("Check-Invoice-Number-Existence".equals(action)){
					Boolean isAvailable = syStemDefaultsDao.checkInvoiceNumberAvailability(organization);
					
					resultSuccess.setData(isAvailable);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-all-payment-types".equals(action)){
					List<String>paymentTypesList = syStemDefaultsDao.getAllPaymentTypes(organization);
					
					resultSuccess.setData(paymentTypesList);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-all-employee-types".equals(action)){
					List<String> employeetypesList = syStemDefaultsDao.getAllEmployeeTypes(organization);
					
					resultSuccess.setData(employeetypesList);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-all-address-types".equals(action)){
					List<EmployeeResult> addressTypesList = syStemDefaultsDao.getCrAddressTypes(organization);
					
					resultSuccess.setData(addressTypesList);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-existed-invoice-numbers".equals(action)){
					SystemDefaultsResult invoiceNumbers = syStemDefaultsDao.getExistedInvoiceNumbers(organization);
					
					resultSuccess.setData(invoiceNumbers);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("delete-invoice-numbers".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.deleteInvoiceNumber(id);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("update-invoice-numbers".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.updateInvoiceNumber(id,systemDefaultsCommand.getValue(),
							systemDefaultsCommand.getDescription(),systemDefaultsCommand.getPeriod());
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("get-all-journal-types".equals(action)){
					List<String>journalTypesList = syStemDefaultsDao.getJournalTypes(organization);
					
					resultSuccess.setData(journalTypesList);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("add-address-type".equals(action)){
					String val = systemDefaultsCommand.getValue();
					String description = systemDefaultsCommand.getDescription();
					syStemDefaultsDao.addAddressTypes(val, description, organization, userName);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if("add-employee-type".equals(action)){
					String value = systemDefaultsCommand.getValue();
					String description = systemDefaultsCommand.getDescription();
					syStemDefaultsDao.addemployeeTypes(value, description, organization, userName);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if("add-journals".equals(action)){
					String value = systemDefaultsCommand.getValue();
					String description = systemDefaultsCommand.getDescription();
					String invoiceNo = request.getParameter("invoiceNo");
					syStemDefaultsDao.addJournalTypes(value, description, organization, userName, invoiceNo);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if("add-invoice-period".equals(action)){
					String value = systemDefaultsCommand.getValue();
					String description = systemDefaultsCommand.getDescription();
					String invoiceNoPeriod = request.getParameter("invoiceNoPeriod");
					syStemDefaultsDao.addInvoicePeriod(value, description, organization, userName, invoiceNoPeriod);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				} else if("delete-pay-types".equals(action)){
					String description = request.getParameter("description");
					syStemDefaultsDao.deletePaymentTypes(description, organization);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("delete-emp-types".equals(action)){
					String description = request.getParameter("description");
					syStemDefaultsDao.deleteEmployeeTypes(description, organization);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("delete-add-types".equals(action)){
					String value =request.getParameter("description");
					syStemDefaultsDao.deleteAddressTypes(value, organization);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				} else if("update-emp-types".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.updateEmployeeTypes(id, organization, systemDefaultsCommand.getValue(),
							systemDefaultsCommand.getDescription());
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("update-add-types".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.updateAddressTypes(id, organization, systemDefaultsCommand.getValue(), 
							systemDefaultsCommand.getDescription(), userName);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("update-pay-types".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.updatePayTypes(id, organization, systemDefaultsCommand.getValue(), 
							systemDefaultsCommand.getDescription(), userName);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("get-emp-id".equals(action)){
					Integer id = syStemDefaultsDao.getEmpId(systemDefaultsCommand.getDescription(), organization);
					
					resultSuccess.setData(id);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-add-id".equals(action)){
					Integer id=syStemDefaultsDao.getAddressTypeId(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(id);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-pay-id".equals(action)){
					Integer id = syStemDefaultsDao.getPaymentTypeId(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(id);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-journal-id".equals(action)){
					Integer id = syStemDefaultsDao.getJournalId(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(id);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("validate-address-type".equals(action)){
					List<VbAddressTypes> vbAddressTypes = syStemDefaultsDao.validateAddressType(systemDefaultsCommand.getValue(),organization);
					
					resultSuccess.setData(vbAddressTypes);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("validate-payment-type".equals(action)){
					List<VbPaymentTypes> vbPaymentTypes = syStemDefaultsDao.validatePaymentType(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(vbPaymentTypes);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("validate-employee-type".equals(action)){
					List<VbRole> vbRole = syStemDefaultsDao.validateEmployeeType(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(vbRole);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("validate-journal-type".equals(action)){
					List<VbJournalTypes> vbJournalTypes = syStemDefaultsDao.validateJournalTypes(systemDefaultsCommand.getValue(), organization);
					
					resultSuccess.setData(vbJournalTypes);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("get-address-types".equals(action)){
					List<EmployeeResult> addressType=(List<EmployeeResult>) syStemDefaultsDao.getCrAddressTypes(organization);
					
					resultSuccess.setData(addressType);
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.RESULT_SUCCESS_MESSAGE));
				} else if("update-journal-types".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					String invoiceNo = request.getParameter("invoiceNo");
					syStemDefaultsDao.updateJournalTypes(id, organization, systemDefaultsCommand.getValue(), 
							systemDefaultsCommand.getDescription(), invoiceNo);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				} else if("delete-journal-types".equals(action)){
					String value = request.getParameter("value");
					syStemDefaultsDao.deleteJournalTypes(value, organization);
					
					resultSuccess.setStatus(successStatus);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				}else if("check-invoiceno-availability".equals(action)){
					String invoiceNo = request.getParameter("invoiceNo");
					Boolean avilability = syStemDefaultsDao.checkInvoiceNumber(invoiceNo,organization);
					resultSuccess.setData(avilability);
				}else if ("save-product-categories".equals(action)){
					String productCategory = request.getParameter("value");
					String description = request.getParameter("description"); 
					syStemDefaultsDao.saveProductCategories(productCategory,description,organization,userName);
					resultSuccess.setMessage(Msg.get(MsgEnum.PERSISTING_SUCCESS_MESSAGE));
				}else if("get-all-product-categories".equals(action)){
					@SuppressWarnings("unchecked")
					List<SystemDefaultsResult> ProductCategoriesList = syStemDefaultsDao.getExistedProductCategories();
					resultSuccess.setData(ProductCategoriesList);
				}else if("update-product-categories".equals(action)){
					String productCategory = request.getParameter("value");
					String description = request.getParameter("description");
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.updateProductCategories(id,productCategory,description,userName);
					resultSuccess.setMessage(Msg.get(MsgEnum.UPDATE_SUCCESS_MESSAGE));
				}else if("delete-product-categories".equals(action)){
					Integer id = Integer.parseInt(request.getParameter("id"));
					syStemDefaultsDao.deleteProductCategories(id);
					resultSuccess.setMessage(Msg.get(MsgEnum.DELETE_SUCCESS_MESSAGE));
				}else if("check-product-category-existance".equals(action)){
					String prodCategory = request.getParameter("value");
					Boolean isAvailable = syStemDefaultsDao.checkProductCategoryExistance(prodCategory,organization);
					resultSuccess.setData(isAvailable);
					
				}
			}
	 		
	 		if(_logger.isDebugEnabled()) {
				_logger.debug("ResultSuccess: {}", resultSuccess);
			}
			return resultSuccess;
		} catch (DataAccessException exception) {
			ResultError resultError = getResultError(exception.getMessage());
			
			if(_logger.isErrorEnabled()) {
				_logger.error("ResultError: {}", resultError);
			}
			return resultError;
		}
	}
}