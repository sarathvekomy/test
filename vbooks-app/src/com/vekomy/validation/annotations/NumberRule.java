/**
 * com.vekomy.validation.annotations.NumberRule.java
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
public @interface NumberRule {
    public int order();
    public NumberType type();
    public double gt()          default Double.MAX_VALUE;
    public double lt()          default Double.MIN_VALUE;
    public double eq()          default Double.MAX_VALUE;
    public String message()     default Rules.EMPTY_STRING;
    public int messageResId()   default 0;

    public enum NumberType {
        INTEGER, LONG, FLOAT, DOUBLE
    }
}
