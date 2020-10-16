package com.felixseifert.swedisheventplanners.security;

import com.felixseifert.swedisheventplanners.model.Employee;
import com.felixseifert.swedisheventplanners.model.enums.Role;
import com.felixseifert.swedisheventplanners.repos.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LOGIN_FAILURE_URL = "/login?error";
    private static final String LOGIN_URL = "/login";
    private static final String LOGOUT_SUCCESS_URL = "/login";

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .requestCache().requestCache(new CustomRequestCache())
                .and().authorizeRequests()
                .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                .anyRequest().authenticated()

                .and().formLogin()
                .loginPage(LOGIN_URL).permitAll()
                .loginProcessingUrl(LOGIN_PROCESSING_URL)
                .failureUrl(LOGIN_FAILURE_URL)
                .and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {

        // Do NOT configure users directly in code for applications in production!

        Employee employeeMike = employeeRepository.findByNameIgnoreCase("mike").get(0);
        Employee employeeJanet = employeeRepository.findByNameIgnoreCase("janet").get(0);

        UserDetails mike =
                User.withUsername(employeeMike.getName())
                        .password("{noop}password")
                        .roles(employeeMike.getRoles().stream().map(Role::getValue).toArray(String[]::new))
                        .build();

        UserDetails janet =
                User.withUsername(employeeJanet.getName())
                        .password("{noop}password")
                        .roles(employeeJanet.getRoles().stream().map(Role::getValue).toArray(String[]::new))
                        .build();

        UserDetails cso =
                User.withUsername("cso")
                        .password("{noop}password")
                        .roles(Role.CUSTOMER_SERVICE_OFFICER.getValue())
                        .build();

        UserDetails simon =
                User.withUsername("simon")
                        .password("{noop}password")
                        .roles(Role.SENIOR_HR_MANAGER.getValue(),
                                Role.HR_EMPLOYEE.getValue(),
                                Role.EMPLOYEE_VIEWER.getValue())
                        .build();

        UserDetails maria =
                User.withUsername("maria")
                        .password("{noop}password")
                        .roles(Role.HR_ASSISTANT.getValue(),
                                Role.HR_EMPLOYEE.getValue(),
                                Role.EMPLOYEE_VIEWER.getValue())
                        .build();

        UserDetails david =
                User.withUsername("david")
                        .password("{noop}password")
                        .roles(Role.MARKETING_OFFICER.getValue(),
                                Role.MARKETING_EMPLOYEE.getValue(),
                                Role.CLIENT_VIEWER.getValue())
                        .build();

        UserDetails emma =
                User.withUsername("emma")
                        .password("{noop}password")
                        .roles(Role.MARKETING_ASSISTANT.getValue(),
                                Role.MARKETING_EMPLOYEE.getValue(),
                                Role.CLIENT_VIEWER.getValue())
                        .build();

        UserDetails alice =
                User.withUsername("alice")
                        .password("{noop}password")
                        .roles(Role.FINANCIAL_MANAGER.getValue(),
                                Role.ACCOUNTANT.getValue(),
                                Role.CLIENT_VIEWER.getValue(),
                                Role.EMPLOYEE_VIEWER.getValue())
                        .build();

        UserDetails jack =
                User.withUsername("jack")
                        .password("{noop}password")
                        .roles(Role.PRODUCTION_MANAGER.getValue(),
                                Role.STAFF_VIEWER.getValue())
                        .build();

        UserDetails production =
                User.withUsername("production")
                        .password("{noop}password")
                        .roles(Role.PRODUCTION_SUB_TEAM.getValue())
                        .build();

        UserDetails natalie =
                User.withUsername("natalie")
                        .password("{noop}password")
                        .roles(Role.SERVICES_MANAGER.getValue(),
                                Role.STAFF_VIEWER.getValue())
                        .build();

        UserDetails services =
                User.withUsername("services")
                        .password("{noop}password")
                        .roles(Role.SERVICES_SUB_TEAM.getValue())
                        .build();

        UserDetails charlie =
                User.withUsername("charlie")
                        .password("{noop}password")
                        .roles(Role.VICE_PRESIDENT.getValue())
                        .build();

        UserDetails secretary =
                User.withUsername("secretary")
                        .password("{noop}password")
                        .roles(Role.SECRETARY.getValue(),
                                Role.EMPLOYEE_VIEWER.getValue())
                        .build();

        UserDetails client =
                User.withUsername("client")
                        .password("{noop}password")
                        .roles(Role.CLIENT.getValue())
                        .build();

        return new InMemoryUserDetailsManager(mike, janet, cso, simon, maria, david, emma, alice, jack,
                production, natalie, services, charlie, secretary, client);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/VAADIN/**",
                "/favicon.ico",
                "/robots.txt",
                "/manifest.webmanifest",
                "/sw.js",
                "/offline.html",
                "/icons/**",
                "/images/**",
                "/styles/**",
                "/h2-console/**");
    }
}
