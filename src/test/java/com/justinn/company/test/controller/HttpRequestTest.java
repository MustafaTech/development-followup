package com.justinn.company.test.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justinn.company.model.Company;
import com.justinn.company.repository.CompaniesRepository;
import com.justinn.company.service.CompanyService;
import com.justinn.company.service.impl.CompanyServiceImpl;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
	
    @LocalServerPort
    private int port;

    
    @Mock
    private RestTemplate restTemplate;
    
	@Mock
	private CompaniesRepository companiesRepository;
    
    @InjectMocks
    private CompanyService companyService = new CompanyServiceImpl();
    
    @Before
    public void setup() {
    	Date date = new Date();
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    	Company company = new Company(1, "Adam Company 1", "V", date);
    	when(companiesRepository.findById(1)).thenReturn(Optional.of(company));
    }
    
    @Test
    public void shouldReturnCompanyDetails() throws Exception {
    	
    	Date date = new Date();
    	
        Company company = new Company(1, "Adam Company 1", "V", date);
        Mockito
          .when(restTemplate.getForEntity(
        		  "http://localhost:" + port + "/company/1", Company.class))
          .thenReturn(new ResponseEntity<Company>(company, HttpStatus.OK));

    	Company comp = companiesRepository.findById(1).get();
        Assert.assertEquals(company, comp);
    	
    }
}
