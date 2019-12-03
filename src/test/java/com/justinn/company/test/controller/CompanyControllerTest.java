package com.justinn.company.test.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justinn.company.Application;
import com.justinn.company.exception.CompanyAlreadyExistsException;
import com.justinn.company.exception.CompanyNotFoundException;
import com.justinn.company.model.Company;
import com.justinn.company.service.CompanyService;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.MOCK, classes={ Application.class })
public class CompanyControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
//    @Autowired
//    private WebApplicationContext webApplicationContext;

    @MockBean
    private CompanyService companyService;
    
//	@BeforeEach
//	public void setUp() {
//		 this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//	}
    
    @Test
    public void shouldGetACompanyFromService() throws Exception {
    	Date date = new Date();
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        String jsonMessageBody = objectMapper.writeValueAsString(new Company(1, "Adam 1", "V", date));

        when(companyService.findCompany(1)).thenReturn(new Company(1, "Adam 1", "V", date));
        this.mockMvc.perform(get("/company/1")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON).content(jsonMessageBody))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("companyId").exists())
            .andExpect(jsonPath("companyName").value("Adam 1"))
            .andExpect(jsonPath("status").value("V"));
            //.andExpect(jsonPath("date").value("2019-11-20 21:14"));
    }
    
    @Test
    public void shouldThrowExceptionCompanyNotFoundService() throws Exception {
        when(companyService.findCompany(anyInt())).thenThrow(CompanyNotFoundException.class);
        this.mockMvc.perform(get("/company/1")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(status().reason("Company not found"));
    }
    
    @Test
    public void shouldAddACompanyToDatabase() throws Exception {
    	Date date = new Date();
    	ObjectMapper objectMapper = new ObjectMapper();
        String jsonMessageBody = objectMapper.writeValueAsString(new Company(1, "Adam 1", "V", date));

        this.mockMvc.perform(post("/company")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON)
        		.content(jsonMessageBody))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.companyId",is(1)))
            .andExpect(jsonPath("$.companyName", is("Adam 1")))
            .andExpect(jsonPath("status", is("V")))
            .andDo(MockMvcResultHandlers.print());
        
        verify(companyService, times(1)).addCompany(any());

    }
    
    @Test
    public void shouldThrowExceptionCompanyAlreadyExistsForAddCompanyService() throws Exception {
    	Date date = new Date();
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        String jsonMessageBody = objectMapper.writeValueAsString(new Company(1, "Adam 1", "V", date));
        doThrow(CompanyAlreadyExistsException.class).when(companyService).addCompany(any());
        this.mockMvc.perform(post("/company").contentType(MediaType.APPLICATION_JSON).content(jsonMessageBody))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(status().reason("Company already exists"));
    	
    }
    
    @Test
    public void shouldUpdateCompany() throws Exception {
    	Date date = new Date();
    	Company company = new Company(2, "Adam 1", "V", date);
    	company.setCompanyName("Adam 2");
    	
        this.mockMvc.perform(put("/company/2")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON)
        	    .content(new ObjectMapper().writeValueAsString(company)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.companyId",is(2)))
            .andExpect(jsonPath("$.companyName", is("Adam 2")))
            .andExpect(jsonPath("status", is("V")))
            .andDo(MockMvcResultHandlers.print());

    }
    
    @Test
    public void shouldThrowExceptionCompanyNotFoundForUpdateCompanyService() throws Exception {
    
    	Date date = new Date();
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        String jsonMessageBody = objectMapper.writeValueAsString(new Company(2, "Adam 1", "V", date));
        
        when(companyService.updateCompany(anyInt(), any())).thenThrow(CompanyNotFoundException.class);
        this.mockMvc.perform(put("/company/2")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON).content(jsonMessageBody))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(status().reason("Company not found"));
        
    }
    
    
    @Test
    public void shouldUpdateStatusCompany() throws Exception {
    	
    	Date date = new Date();
        when(companyService.updateStatusCompany(anyInt(), any())).thenReturn(new Company(1, "Adam 1", "N", date));
        
        this.mockMvc.perform(put("/company/status/1")
        	    .accept(MediaType.APPLICATION_JSON)
        	    .contentType(MediaType.APPLICATION_JSON)
        	    .content("V"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.companyId",is(1)))
            .andExpect(jsonPath("$.companyName", is("Adam 1")))
            .andExpect(jsonPath("$.status", is("N")))
            .andDo(MockMvcResultHandlers.print());

    }

}
