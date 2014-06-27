/**
 * com.vekomy.vbooks.app.config.spring.DaoConfig.java
 *
 * This is proprietary work of Vekomy Technologies. Any kind of copying or 
 * duplication is subject to Legal proceeding. All the rights on this work 
 * are reserved to Vekomy Technologies.
 *
 * Created on: Jul 16, 2013
 */
package com.vekomy.vbooks.app.config.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author NKR
 * 
 */
@Configuration
@EnableTransactionManagement
@ImportResource("classpath:META-INF/dao.xml")
public class DaoConfig {

}
