package com.justinn.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.justinn.company.model.Company;
import com.justinn.company.service.CompanyService;

@RestController
public class CompanyController {
	
    private CompanyService companyService;

    public CompanyController(@Autowired CompanyService companyService) {
        this.companyService = companyService;
    }
	
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Company> findCompany(@PathVariable(value="companyId") int companyId) {
        return new ResponseEntity<Company>( companyService.findCompany(companyId), HttpStatus.OK);
    }

    @PostMapping("/company")
    public ResponseEntity<Company> addCompany(@RequestBody Company company) {
        companyService.addCompany(company);
        return new ResponseEntity<Company>(company, HttpStatus.CREATED);
    }

    @PutMapping("/company/{companyId}")
    public ResponseEntity<Company> updateCompany(@PathVariable(value="companyId") int companyId,
    										  @RequestBody Company company
    											) {
        companyService.updateCompany(companyId, company);
        return new ResponseEntity<Company>(company, HttpStatus.OK);
    }
    
    @PutMapping("/company/status/{companyId}")
    public ResponseEntity<Company> updateStatusCompany(@PathVariable(value="companyId") int companyId,
    										  @RequestBody String status
    											) {
        return new ResponseEntity<Company>(companyService.updateStatusCompany(companyId, status), HttpStatus.OK);
    }    
    
}
