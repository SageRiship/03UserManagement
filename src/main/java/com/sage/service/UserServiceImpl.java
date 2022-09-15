package com.sage.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.sage.bindings.Login;
import com.sage.bindings.User;
import com.sage.entity.UserMaster;
import com.sage.repo.UserRepo;
import com.sage.request.ActivateRequest;
import com.sage.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepo userrepo;

	@Autowired
	EmailUtils emailUtils;

	@Override
	public boolean activateAccount(ActivateRequest request) {
		UserMaster entity = new UserMaster();
		entity.setEmail(request.getEmail());
		entity.setPassword(request.getTempPassword());

		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userrepo.findAll(of);
		if (findAll.isEmpty()) {
			return false;
		} else {
			UserMaster userMaster = findAll.get(0);
			userMaster.setPassword(request.getNewPassword());
			userMaster.setAccStatus("Active");
			userrepo.save(userMaster);
			return true;
		}

	}

	@Override
	public String login(Login login) {
		UserMaster entity = new UserMaster();
		entity.setEmail(login.getEmail());
		entity.setPassword(login.getPassword());

		Example<UserMaster> of = Example.of(entity);
		List<UserMaster> findAll = userrepo.findAll(of);

		if (findAll.isEmpty()) {
			return "Invalid credentials";
		} else {
			UserMaster userMaster = findAll.get(0);
			if (userMaster.getAccStatus().equals("Active")) {
				return "SUCCESS";
			} else {
				return "Account not activated";
			}
		}

	}

	@Override
	public boolean createUser(User user) {
		UserMaster entity = new UserMaster();
		BeanUtils.copyProperties(user, entity);

		entity.setPassword("random");
		entity.setAccStatus("In-Active");
		UserMaster save = userrepo.save(entity);
		// to do generate random password and email sending
		String subject = "Your registration is Successfull";
		String fileName = "REG-EMAIL-BODY.txt";
		String body = readRegEmailBody(entity.getName(), entity.getPassword(), fileName);
		emailUtils.sendEmail(user.getEmail(), subject, body);
		return save.getUserId() != null;
	}

	@Override
	public User getUserById(Integer userId) {
		Optional<UserMaster> findById = userrepo.findById(userId);
		if (findById.isPresent()) {
			User user = new User();
			UserMaster userMaster = findById.get();
			BeanUtils.copyProperties(userMaster, user);
			return user;
		}
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		List<UserMaster> findAll = userrepo.findAll();

		List<User> users = new ArrayList<>();
		for (UserMaster entity : findAll) {
			User user = new User();
			BeanUtils.copyProperties(entity, user);
			users.add(user);
		}
		return users;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		try {
			userrepo.deleteById(userId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String forgotPassword(String email) {
		UserMaster entity = userrepo.findByEmail(email);
		if (entity == null) {
			return "Invalid Email";
		}
		String subject = "FORGOT Password";
		String fileName = "RECOVER-MAIL-BODY.txt";
		String body = readRegEmailBody(entity.getName(), entity.getPassword(), fileName);
		boolean sendEmail = emailUtils.sendEmail(email, subject, body);
		if (sendEmail) {
			return "Password sent to your registered email";
		}
		return null;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String status) {
		Optional<UserMaster> findById = userrepo.findById(userId);
		if (findById.isPresent()) {

			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(status);
			return true;

		}
		return false;
	}

	private String readRegEmailBody(String fullName, String tempPwd, String fileName) {

		String url = "";
		String mailBody = null;
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			StringBuffer buffer = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				buffer.append(line);
				line = br.readLine();
			}
			mailBody = buffer.toString();
			mailBody = mailBody.replace("{FULLNAME}", fullName);
			mailBody = mailBody.replace("{TEMP-PWD}", tempPwd);
			mailBody = mailBody.replace("{URL}", url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mailBody;
	}

}
