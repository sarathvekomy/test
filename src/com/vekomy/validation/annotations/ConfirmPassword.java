/**
 * com.vekomy.validation.annotations.ConfirmPassword.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 4, 2013
 */
package com.vekomy.validation.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {
    public int order();
    public String message()     default "Passwords don't match";
    public int messageResId()   default 0;
}
