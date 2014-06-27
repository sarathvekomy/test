/**
 * 
 */
package com.vekomy.vbooks.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vekomy.vbooks.app.dao.AuthenticateDao;
import com.vekomy.vbooks.app.request.LoginRequest;
import com.vekomy.vbooks.app.response.LoginResponse;

/**
 * @author nkr
 *
 */
@Controller
@RequestMapping({ "/app" })
public class RestController {
	
	private static final Logger logger = LoggerFactory.getLogger(RestController.class);

	@Autowired
	AuthenticateDao loginDao;

	// http://localhost:8080/vbooks-app-server/app/LoginAuth
	@RequestMapping(value = { "/LoginAuth" }, method = RequestMethod.POST)
	@ResponseBody
	public LoginResponse validateUser(@ModelAttribute("loginUser") LoginRequest loginUser) {
		LoginResponse loginUserRespose = null;
		try {
			return this.loginDao.authenticate(loginUser);
		} catch (Exception e) {
			e.printStackTrace();
			loginUserRespose = new LoginResponse();
			loginUserRespose.setStatusCode(10);
		}
		return loginUserRespose;
	}

	/*@RequestMapping(value = { "/" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public LoginUser validateUser1() {
		return new LoginUser();
	}*/

	// http://localhost:8080/vbooks-app-server/app/LoginAuth?uname=aa&pswd=aa
	/*@RequestMapping(value = { "/LoginAuth" }, method = RequestMethod.GET,produces = { "application/json" })
	@ResponseBody
    public LoginResponse validateUserGet(@RequestParam("uname") String userName,@RequestParam("pswd") String pasword) {
		LoginRequest loginUser = new LoginRequest();
		loginUser.setUserName(userName);
		loginUser.setPassword(pasword);
		LoginResponse loginUserRespose = null;
		try {
				return this.loginDao.authenticate(loginUser);
			} catch (Exception e) {
				e.printStackTrace();
				loginUserRespose = new LoginResponse();
				loginUserRespose.setStatusCode(10);
			}
			return loginUserRespose;
		}*/
}
