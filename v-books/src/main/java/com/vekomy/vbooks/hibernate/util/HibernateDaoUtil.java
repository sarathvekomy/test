package com.vekomy.vbooks.hibernate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sudhakar
 *
 * 
 */
public class HibernateDaoUtil {
	
	/**
	 * Logger variable holdes _logger.
	 */
	private static final Logger _logger = LoggerFactory.getLogger(HibernateDaoUtil.class);
    /**
     * SimpleDateFormat variable holdes simpleDateFormatForDB.
     */
    private static SimpleDateFormat simpleDateFormatForDB = new SimpleDateFormat("dd-MMM-yy");
    /**
     * SimpleDateFormat variable holdes uiDateFormat.
     */
    private static SimpleDateFormat uiDateFormat = new SimpleDateFormat("MM/dd/yyyy");

	
    public HibernateDaoUtil() {
	    super();
    }
    
    public static void appendGridFilter(StringBuffer queryBuffer, HashMap gridFilters) throws ParseException {
        Iterator iterator = gridFilters.keySet().iterator();

        boolean hasNext = iterator.hasNext();
        SearchFilterData filterData = null;
        while (hasNext) {
            filterData = (SearchFilterData) gridFilters.get(iterator.next());

            queryBuffer.append(filterData.getField());

            switch (filterData.getType()) {
            case SearchFilterData.TYPE_NUMERIC:
                switch (filterData.getComparison()) {
                case SearchFilterData.COMPARISON_GT:
                    queryBuffer.append(" > ");
                    break;
                case SearchFilterData.COMPARISON_LT:
                    queryBuffer.append(" < ");
                    break;
                case SearchFilterData.COMPARISON_EQ:
                default:
                    queryBuffer.append(" = ");
                    break;
                }
                queryBuffer.append(filterData.getValue());
                break;

            case SearchFilterData.TYPE_DATE:
                switch (filterData.getComparison()) {
                case SearchFilterData.COMPARISON_GT:
                    queryBuffer.append(" > ");
                    break;
                case SearchFilterData.COMPARISON_LT:
                    queryBuffer.append(" < ");
                    break;
                case SearchFilterData.COMPARISON_EQ:
                default:
                    queryBuffer.append(" = ");
                    break;
                }
                Date inputDate = uiDateFormat.parse(filterData.getValue().toString());
                queryBuffer.append("'");
                queryBuffer.append(simpleDateFormatForDB.format(inputDate));
                queryBuffer.append("'");
                break;

            case SearchFilterData.TYPE_STRING:
                queryBuffer.append(" LIKE '%");
                queryBuffer.append(filterData.getValue());
                queryBuffer.append("%'");
                break;

            case SearchFilterData.TYPE_BOOLEAN:
                queryBuffer.append(" = '");
                boolean bol = Boolean.parseBoolean(filterData.getValue().toString());
                if (bol) {
                    queryBuffer.append("Y");
                } else {
                    queryBuffer.append("N");
                }
                queryBuffer.append("'");
                break;

            default:
                break;
            }
            hasNext = iterator.hasNext();
            if (hasNext) {
                queryBuffer.append(" and ");
            }
        }

    }


}
