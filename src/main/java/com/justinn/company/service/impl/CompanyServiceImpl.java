package com.justinn.company.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.justinn.company.exception.CompanyAlreadyExistsException;
import com.justinn.company.exception.CompanyNotFoundException;
import com.justinn.company.model.Company;
import com.justinn.company.repository.CompaniesRepository;
import com.justinn.company.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	
	
    @Autowired
    private CompaniesRepository companiesRepository;
	
    public Company findCompany(int companyId) {
		return companiesRepository.findById(companyId)
				.filter(p -> p.getCompanyId()==companyId)
				.orElseThrow(CompanyNotFoundException::new);
	}
	
	public void addCompany(Company company) {
		boolean doesCompanyExist = companiesRepository.existsById(company.getCompanyId());
		if (doesCompanyExist) {
			throw new CompanyAlreadyExistsException();
		}
		companiesRepository.save(company);
	}
	
	public Company updateCompany(int companyId, Company company) {
		return companiesRepository.findById(companyId)
				.map(cmp -> {
					cmp.setCompanyId(company.getCompanyId());
					cmp.setCompanyName(company.getCompanyName());
					cmp.setDate(company.getDate());
					cmp.setStatus(company.getStatus());
					companiesRepository.save(cmp);
					return cmp;
				})				
				.orElseThrow(CompanyNotFoundException::new);
	}
	
	public Company updateStatusCompany(int companyId, String status) {
		return companiesRepository.findById(companyId)
				.map(cmp -> {
					cmp.setStatus(status);
					companiesRepository.save(cmp);
					return cmp;
				})
				.orElseThrow(CompanyNotFoundException::new);
	}

}
