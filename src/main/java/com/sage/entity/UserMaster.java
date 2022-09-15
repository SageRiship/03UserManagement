package com.sage.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="USER_MASTER")
public class UserMaster {
	@Id
	@GeneratedValue
	private Integer userId;
	private String name;
	private Long mobile;
	private String email;
	private String password;
	private Character gender;
	private Long ssn;
	private String accStatus;
	private LocalDate createdDate;
	private LocalDate updatedDate;
	private String createdBy;
	private String updatedBy;
}
