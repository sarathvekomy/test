/**
 * com.vekomy.validation.annotations.Checked.java
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

import com.vekomy.validation.Rules;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Checked {
    public int order();
    public boolean checked()    default true;
    public String message()     default Rules.EMPTY_STRING;
    public int messageResId()   default 0;
}
