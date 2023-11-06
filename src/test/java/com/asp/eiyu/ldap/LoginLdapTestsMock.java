package com.asp.eiyu.ldap;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.Assert;

import com.asp.eiyu.ldap.controller.JwtAuthenticationController;
import com.asp.eiyu.ldap.dto.LoginLdapRequest;
import com.asp.eiyu.ldap.dto.LoginRequest;
import com.asp.eiyu.ldap.mockserver.MockServerLdap;
import com.asp.eiyu.ldap.service.ILdapConsumingService;
import com.fasterxml.jackson.databind.ObjectMapper;


import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginLdapTestsMock {
    

    @Autowired
    private ILdapConsumingService consumingService;
    

	private MockServerLdap mockServerLdap;


    @Autowired
	private MockMvc mockMvc; // For MockTestRest Services

    
	@BeforeAll
	public void startServer(){

		
		mockServerLdap = new MockServerLdap();
		mockServerLdap.whenMockServerLdap();

	}


    @Test
    @Order(1)
    @DisplayName("  * Try Mock Service Ldap")
	void  whenMockRestEndPoint_thenCallFromService() throws Exception {
        
        LoginLdapRequest ldapRequest = new LoginLdapRequest();
        ldapRequest.setUsuario("AMCARRIZOZA");
        ldapRequest.setContrasena("b7da45996a47a61031fd341c3d898c41b600609d47de2f32a297580fe1612002");
        ldapRequest.setIdUsuario("5347");
        ldapRequest.setIdSesion("0");
        ldapRequest.setEstatus(1);
        ldapRequest.setIdAplicativo(7);

        Assert.hasText(this.consumingService.processLdapValidation(ldapRequest).getCodeEstatus(),"0"); 
		
	}

    @Test
    @Order(2)
    @DisplayName("  * Try mock rest ")
    void whenOkRequestToMockRestThenOkToken() throws Exception{
        
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("7KYE5kgtrAwLUUDtxQ/Xcg==");
        loginRequest.setPassword("qkYJbWaqnvlbDPxg1d73ow==");



        this.mockMvc.perform( post("http://localhost:8070/authenticate").contentType(
            (MediaType.APPLICATION_JSON_VALUE.toString()))
        //.content("{\"username\":\"7KYE5kgtrAwLUUDtxQ/Xcg==\",\"password\":\"qkYJbWaqnvlbDPxg1d73ow==\"}"))
        .content(getObjectAsString(loginRequest) ))
        .andExpect( status().isOk()  )
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect( jsonPath("$.token").isNotEmpty())
        .andDo(MockMvcResultHandlers.print());
        


        

        this.mockMvc.perform( post("http://localhost:8070/authenticate").contentType(
            (MediaType.APPLICATION_JSON_VALUE.toString()))
        .content(getObjectAsString(loginRequest) ))
        .andExpect( status().isUnauthorized()  );
        
        

    }


    @AfterEach
    public void verify() {
        mockServerLdap.verifyMockServer();
    }
    @AfterAll
    public void stop() {
		mockServerLdap.stopServer();
    }

    private String getObjectAsString(Object obj){
        try{
            
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);

        }
        catch (Exception e){
            return "";
        }
    }



}
