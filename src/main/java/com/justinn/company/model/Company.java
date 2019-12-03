package com.justinn.company.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companies")
public class Company {
	
	@Id
	@Column(name="companyid")
	public int companyId;
	
	@Column(name="companyname")
	public String companyName;
	
	@Column(name="status")
	public String status;
	
	@Column(name="date")
	public Date date;
	
	
	public Company() {
		
	}
	
	public Company(int companyId, String companyName, String status,  Date date) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.status = status;
		this.date = date;
	}
	
	public int getCompanyId() {
		return companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public String getStatus() {
		return status;
	}
	public Date getDate() {
		return date;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
