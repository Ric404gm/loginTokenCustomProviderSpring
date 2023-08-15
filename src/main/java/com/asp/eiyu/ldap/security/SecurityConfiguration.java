package com.asp.eiyu.ldap.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    
    
	@Autowired
    private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private ProviderService providerService;


    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        return httpSecurity.csrf( (c) -> c.disable())
            .authorizeHttpRequests((request) -> request.requestMatchers("/authenticate").permitAll()
            .anyRequest().authenticated() )
            .authenticationProvider(this.authenticationProvider())

            //NotWorking
            .exceptionHandling( (ex) -> ex.authenticationEntryPoint(authenticationEntryPoint) )
            //NotWorking
            .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();

    } 


    /*
     * Old Version But works it
     * @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        AuthenticationManagerBuilder authenticationManagerBuilder = 
            httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(providerService);
        authenticationManager = authenticationManagerBuilder.build();
   
        return httpSecurity.csrf( (c) -> c.disable())
            .authorizeHttpRequests((request) ->
             request.requestMatchers("/authenticate").permitAll()
             .anyRequest().authenticated().and()
                .authenticationManager(authenticationManager) )
             .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class).build();
   
    } 
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {   
        return this.providerService;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
