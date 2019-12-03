package com.justinn.company.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.justinn.company.model.Company;

@Repository
public interface CompaniesRepository extends CrudRepository<Company, Integer> {


}
