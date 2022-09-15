package com.sage.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sage.entity.UserMaster;

public interface UserRepo extends JpaRepository<UserMaster, Integer> {
	public UserMaster findByEmail(String email);
}
