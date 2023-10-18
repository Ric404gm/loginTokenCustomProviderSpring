package com.asp.eiyu.ldap.mockserver;

import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.asp.eiyu.ldap.dto.LoginLdapResponse;
import com.fasterxml.jackson.databind.ObjectMapper;



/**
 * Official Docuemntation
 * https://stackoverflow.com/questions/67091263/how-to-set-up-different-responses-for-the-same-request-to-mockserver
 */
public class MockServerLdap {
    
    private static final Integer MOCK_SERVER_PORT = 1001;
    private final ClientAndServer clientAndServer;
    private final MockServerClient mockServerClient = new MockServerClient("localhost", MOCK_SERVER_PORT);
    private static final String LDAP_ENDPOINT = "/AdminSegWS/capacontrol/loginGlobal";

    public MockServerLdap () {
        this.clientAndServer =  ClientAndServer.startClientAndServer(MOCK_SERVER_PORT);
    }

    
    public void whenMockServerLdap(){

        LdapMockResponse ldapMockResponse =new LdapMockResponse();
        ldapMockResponse.setNumempleado("404");
        ldapMockResponse.setIdUsuario("5347");
        ldapMockResponse.setCodeEstatus("0");
        ldapMockResponse.setMessageEstatus("msgstatus");
        ldapMockResponse.setMensaje("MensajeExtra");
        ldapMockResponse.setData("dataextra"); 
        

        this.mockServerClient
                .when( HttpRequest.request()
                                    .withMethod("POST")
                                    .withPath(LDAP_ENDPOINT), Times.exactly(1) )
                .respond( HttpResponse.response()
                                    .withStatusCode(200)
                                    .withHeaders(  new Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                                        .withBody(getObjectAsString(ldapMockResponse)));

        
        this.mockServerClient
                .when( HttpRequest.request()
                                    .withMethod("POST")
                                    .withPath(LDAP_ENDPOINT), Times.exactly(1) )
                .respond( HttpResponse.response()
                                    .withStatusCode(200)
                                    .withHeaders(  new Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                                        .withBody(getObjectAsString(ldapMockResponse)));                                       

            var ldapMockResponse401 = new LdapMockResponse();
            ldapMockResponse401.setNumempleado("0");
            ldapMockResponse401.setIdUsuario("0");
            ldapMockResponse401.setCodeEstatus("1");
            ldapMockResponse401.setMessageEstatus("Error de autenticacion Ldap");
            ldapMockResponse401.setMensaje("");
            ldapMockResponse401.setData(""); 
        
    
            
            this.mockServerClient
                .when( HttpRequest.request()
                                    .withMethod("POST")
                                    .withPath(LDAP_ENDPOINT), Times.exactly(1) )
                .respond( HttpResponse.response()
                                    .withStatusCode(401)
                                    .withHeaders( new Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                                        .withBody(getObjectAsString(ldapMockResponse401)));
                 
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


    public void verifyMockServer(){
        mockServerClient.verify( HttpRequest.request()
            .withMethod(HttpMethod.POST.name())
                .withPath(this.LDAP_ENDPOINT) );
    }
    
    public void stopServer(){
        this.clientAndServer.stop();   
    }


    private   class LdapMockResponse  extends LoginLdapResponse{
                

    } 

}
