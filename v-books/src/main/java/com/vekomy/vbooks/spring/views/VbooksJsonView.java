package com.vekomy.vbooks.spring.views;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.json.JsonView;

public class VbooksJsonView extends JsonView {

	public VbooksJsonView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.view.json.JsonView#renderMergedOutputModel
	 * (java.util.Map, javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	public void renderMergedOutputModel(Map model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			super.renderMergedOutputModel(model, request, response);
		} catch (org.hibernate.LazyInitializationException le) {
			// eat it
			// le.printStackTrace();
		} catch (Exception e) {
			// throw e;
		} catch (Error e) {
			// throw e;
		}
	}

}
