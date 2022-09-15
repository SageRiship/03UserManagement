package com.sage.bindings;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
public class User {

	private Integer userId;
	private String name;
	private Long mobile;
	private String email;
	private String password;
	private Character gender;
	private Long ssn;

}
