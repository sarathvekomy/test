/**
 * com.vekomy.validation.AnnotationToRuleConverter.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 4, 2013
 */
package com.vekomy.validation;

import android.view.View;
import android.widget.Checkable;

/**
 * Abstract class that allows to define validation rules for {@link View}s.
 * 
 * @author NKR
 * 
 * @param <T>
 *            Usually the {@link View} this rule is applicable for. Sometimes
 *            may be interfaces such as {@link Checkable} which are indeed
 *            {@link View} components or widgets.
 */
public abstract class Rule<T> {

	private String mFailureMessage;

	@SuppressWarnings("unused")
	private Rule() { /* Cannot instantiate */
	}

	/**
	 * Creates a new validation Rule.
	 * 
	 * @param failureMessage
	 *            The failure message associated with the Rule.
	 */
	public Rule(String failureMessage) {
		mFailureMessage = failureMessage;
	}

	/**
	 * Returns the failure message associated with the rule.
	 * 
	 * @return Returns the failure message associated with the rule
	 */
	public String getFailureMessage() {
		return mFailureMessage;
	}

	/**
	 * Sets the failure message for the Rule.
	 * 
	 * @param failureMessage
	 *            The failure message associated with the Rule.
	 */
	public void setFailureMessage(String failureMessage) {
		this.mFailureMessage = failureMessage;
	}

	/**
	 * Checks whether the Rule is valid for the associated {@link View}.
	 * 
	 * @param view
	 *            The view associated with this Rule.
	 * @return True if validation succeeds, false otherwise.
	 */
	public abstract boolean isValid(T view);

}
