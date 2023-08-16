package com.asp.eiyu.ldap.security;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ClassUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.SignatureException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

/*  RestControllerAdvide For Handle Internal  */
//@RestControllerAdvice
public class SecurityControllerAdvice {


  
  
  
  /*  

  @ExceptionHandler(value = {NoHandlerFoundException.class})
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public RestError resourceNotFoundException(NoHandlerFoundException ex, WebRequest request) {
    RestError re = new RestError(HttpStatus.BAD_REQUEST.toString(), 
          "Authentication failed at controller advice");
    
    return re;
  }

  
 
    @ExceptionHandler({ NoHandlerFoundException.class })
    @ResponseBody
    public ResponseEntity<RestError> handleAuthenticationException(Exception ex) {

        RestError re = new RestError(HttpStatus.BAD_REQUEST.toString(), 
          "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re);
    }

    
    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<RestError> handleAuthenticationExceptionAuth(Exception ex) {

        RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(), 
          "Authentication failed at controller advice");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }
    */


    @Data
    private class RestError {
        private String  httpError;
        private  String msg;
        
        public RestError() {
            
            
        }
        public RestError(String httpError, String msg) {
            this.httpError = httpError;
            this.msg = msg;
        }

        
    }
}
