package com.justinn.company.service;

import com.justinn.company.model.Company;


public interface CompanyService {
	public Company findCompany(int companyId);	
	public void addCompany(Company company);
	public Company updateCompany(int companyId, Company company);
	public Company updateStatusCompany(int companyId, String status);
}
