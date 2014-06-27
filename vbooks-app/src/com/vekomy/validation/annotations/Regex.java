/**
 * com.vekomy.validation.annotations.Regex.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or
 * duplication is subject to Legal proceeding. All the rights on this work
 * are reserved to Vekomy Technologies.
 *
 * Author: NKR
 * Created: Jun 4, 2013
 */
package com.vekomy.validation.annotations;

import com.vekomy.validation.Rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {
    public int order();
    public String pattern()     default ".";
    public int patternResId()   default 0;
    public boolean trim()       default true;
    public String message()     default Rules.EMPTY_STRING;
    public int messageResId()   default 0;
}
