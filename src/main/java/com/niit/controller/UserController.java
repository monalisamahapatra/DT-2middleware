package com.niit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.niit.controllers.HttpSession;
import com.niit.dao.UserDao;
import com.niit.model.ErrorClazz;
import com.niit.model.User;

@Controller
public class UserController {
	@Autowired
	private UserDao userDao;

	public UserController() {
		System.out.println("UserController bean is created");
	}

	@RequestMapping(value="/registeruser", method=RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User user)
	{
		System.out.println(user);
		if(!userDao.isEmailUnique(user.getEmail()))
		{
			ErrorClazz error = new ErrorClazz(1, "Email Already exits...please enter new Email id");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.CONFLICT);
		}
		
		try
		{
			userDao.registerUser(user);
		}
		catch(Exception e)
		{
			ErrorClazz error = new ErrorClazz(2, "Some required fields are empty"+e.getMessage());
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<User>(user,HttpStatus.OK);
		
	}
	@RequestMapping(value="/getuser",method=RequestMethod.GET)
	public ResponseEntity<?> getUser(javax.servlet.http.HttpSession session)
	{
		String email=(String) session.getAttribute("loginId");
		if(email==null)
		{
			ErrorClazz error=new ErrorClazz(5, "Unauthorized acess");
			return new ResponseEntity<ErrorClazz>(error,HttpStatus.UNAUTHORIZED);
		}
		User user=userDao.getUser(email);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
}