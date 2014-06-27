package com.vekomy.vbooks.spring.views;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.json.JsonView;

public class VbooksJsonView extends JsonView {
	/**
	 * Logger variable holds _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(VbooksJsonView.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.json.JsonView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("rawtypes")
	public void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			super.renderMergedOutputModel(model, request, response);
		} catch (LazyInitializationException exception) {
			if(_logger.isErrorEnabled()) {
				_logger.error(exception.getMessage());
			}
		} catch (Exception exception) {
			if(_logger.isErrorEnabled()) {
				_logger.error(exception.getMessage());
			}
		} catch (Error error) {
			if(_logger.isErrorEnabled()) {
				_logger.error(error.getMessage());
			}
		}
	}
}
