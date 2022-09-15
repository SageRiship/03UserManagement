package com.sage.service;

import java.util.List;

import com.sage.bindings.Login;
import com.sage.bindings.User;
import com.sage.entity.UserMaster;
import com.sage.request.ActivateRequest;

public interface UserService {
	public boolean activateAccount(ActivateRequest request);
	
	public String login(Login login);

	public boolean createUser(User user);

	public User getUserById(Integer userId);

	public List<User> getAllUsers();

	//public boolean updateUser(UserMaster user);

	public boolean deleteUserById(Integer userId);
	
	public String forgotPassword(String email);
	
	public boolean changeAccountStatus(Integer userId , String status);
}
