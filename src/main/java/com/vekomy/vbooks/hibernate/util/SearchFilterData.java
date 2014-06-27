package com.vekomy.vbooks.hibernate.util;

public class SearchFilterData {


    public static final int COMPARISON_EQ = 0;
    public static final int COMPARISON_GT = 1;
    public static final String COMPARISON_GT_STR = "gt";
    public static final int COMPARISON_LT = 2;
    public static final String COMPARISON_LT_STR = "lt";

    public static final int TYPE_NUMERIC = 1;
    public static final String TYPE_NUMERIC_STR = "numeric";
    public static final int TYPE_DATE = 2;
    public static final String TYPE_DATE_STR = "date";
    public static final int TYPE_STRING = 3;
    public static final String TYPE_STRING_STR = "string";
    public static final int TYPE_BOOLEAN = 4;
    public static final String TYPE_BOOLEAN_STR = "boolean";
    
    public static final String SUB_DATA_COMPARISON = "comparison";
    public static final String SUB_DATA_TYPE = "type";
    public static final String SUB_DATA_VALUE = "value";
    public static final String SUB_DATA_FIELD = "field";
    
    /**
     * To hold comparison
     */
    public int comparison; 
    
    /**
     * To hold type
     */
    public int type;
    
    /**
     * To hold value
     */
    public Object value;
    
    /**
     * To hold field
     */
    public String field;
    
    /**
     * Constructor for GridFilterData
     *
     */
    public SearchFilterData() {
        
    }
    
    /**
     * Constructor for GridFilterData
     *
     */
    public SearchFilterData(String field, Object value, String typeStr) {
        setField(field);
        setValue(value);
        setType(typeStr);
    }
    
    public String toString() {
        return "field:"+field+" comparison:"+comparison+" type:"+type+" value:"+value;
    }
    
    /**
     * Setter method for comparison
     * 
     * @param comparison the comparison to set
     */
    public void setComparison(String comparisonStr) {
        if (comparisonStr.equals(COMPARISON_GT_STR)) {
            this.comparison = COMPARISON_GT;
        } else if (comparisonStr.equals(COMPARISON_LT_STR)) {
            this.comparison = COMPARISON_LT;
        }
    }


    
    /**
     * Setter method for type
     * 
     * @param type the type to set
     */
    public void setType(String typeStr) {
        if (typeStr.equals(TYPE_NUMERIC_STR)) {
            this.type = TYPE_NUMERIC;
        } else if (typeStr.equals(TYPE_DATE_STR)) {
            this.type = TYPE_DATE;
        } else if (typeStr.equals(TYPE_STRING_STR)) {
            this.type = TYPE_STRING;
        } else if (typeStr.equals(TYPE_BOOLEAN_STR)) {
            this.type = TYPE_BOOLEAN;
        }
    }


    /**
     * Getter method for comparison
     * 
     * @return the comparison
     */
    public int getComparison() {
        return comparison;
    }

    /**
     * Setter method for comparison
     * 
     * @param comparison the comparison to set
     */
    public void setComparison(int comparison) {
        this.comparison = comparison;
    }

    /**
     * Getter method for type
     * 
     * @return the type
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method for type
     * 
     * @param type the type to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Getter method for value
     * 
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Setter method for value
     * 
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Getter method for field
     * 
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Setter method for field
     * 
     * @param field the field to set
     */
    public void setField(String field) {
        this.field = field;
    }
    

	
}
