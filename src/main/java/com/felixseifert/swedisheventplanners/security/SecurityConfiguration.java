package com.felixseifert.swedisheventplanners.security;

import com.felixseifert.swedisheventplanners.backend.model.Employee;
import com.felixseifert.swedisheventplanners.backend.model.enums.Role;
import com.felixseifert.swedisheventplanners.backend.repos.EmployeeRepository;
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

import java.util.ArrayList;
import java.util.List;

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

        List<Employee> employeesList = createEmployees();

        List<UserDetails> userDetails = new ArrayList<>();

        for (Employee employee : employeesList) {
            UserDetails user =
                    User.withUsername(employee.getName())
                    .password("{noop}password")
                    .roles(employee.getRoles().stream().map(Role::getValue).toArray(String[]::new))
                    .build();

            userDetails.add(user);
        }

        return new InMemoryUserDetailsManager(userDetails);
    }

    private List<Employee> createEmployees() {
        List<Employee> employees = new ArrayList<>();

        Employee mike = new Employee();
        mike.setName("Mike");
        mike.addRole(Role.ADMINISTRATION_MANAGER);
        employees.add(mike);

        Employee janet = new Employee();
        janet.setName("Janet");
        janet.addRole(Role.SENIOR_CUSTOMER_SERVICE_OFFICER);
        janet.addRole(Role.CLIENT_VIEWER);
        employees.add(janet);

        Employee cso = new Employee();
        cso.setName("CSO");
        cso.addRole(Role.CUSTOMER_SERVICE_OFFICER);
        employees.add(cso);

        Employee simon = new Employee();
        simon.setName("Simon");
        simon.addRole(Role.SENIOR_HR_MANAGER);
        simon.addRole(Role.HR_EMPLOYEE);
        simon.addRole(Role.EMPLOYEE_VIEWER);
        employees.add(simon);

        Employee maria = new Employee();
        maria.setName("Maria");
        maria.addRole(Role.HR_ASSISTANT);
        maria.addRole(Role.HR_EMPLOYEE);
        maria.addRole(Role.EMPLOYEE_VIEWER);
        employees.add(maria);

        Employee david = new Employee();
        david.setName("David");
        david.addRole(Role.MARKETING_OFFICER);
        david.addRole(Role.MARKETING_EMPLOYEE);
        david.addRole(Role.CLIENT_VIEWER);
        employees.add(david);

        Employee emma = new Employee();
        emma.setName("Emma");
        emma.addRole(Role.MARKETING_ASSISTANT);
        emma.addRole(Role.MARKETING_EMPLOYEE);
        emma.addRole(Role.CLIENT_VIEWER);
        employees.add(emma);

        Employee alice = new Employee();
        alice.setName("Alice");
        alice.addRole(Role.FINANCIAL_MANAGER);
        alice.addRole(Role.ACCOUNTANT);
        alice.addRole(Role.CLIENT_VIEWER);
        alice.addRole(Role.EMPLOYEE_VIEWER);
        employees.add(alice);

        Employee jack = new Employee();
        jack.setName("Jack");
        jack.addRole(Role.PRODUCTION_MANAGER);
        jack.addRole(Role.STAFF_VIEWER);
        employees.add(jack);

        Employee production = new Employee();
        production.setName("Production");
        production.addRole(Role.PRODUCTION_SUB_TEAM);
        employees.add(production);

        Employee natalie = new Employee();
        natalie.setName("Natalie");
        natalie.addRole(Role.SERVICES_MANAGER);
        natalie.addRole(Role.STAFF_VIEWER);
        employees.add(natalie);

        Employee services = new Employee();
        services.setName("Services");
        services.addRole(Role.SERVICES_SUB_TEAM);
        employees.add(services);

        Employee charlie = new Employee();
        charlie.setName("Charlie");
        charlie.addRole(Role.VICE_PRESIDENT);
        employees.add(charlie);

        Employee secretary = new Employee();
        secretary.setName("Secretary");
        secretary.addRole(Role.SECRETARY);
        secretary.addRole(Role.EMPLOYEE_VIEWER);
        employees.add(secretary);

        Employee client = new Employee();
        client.setName("Client");
        client.addRole(Role.CLIENT);
        employees.add(client);

        employeeRepository.saveAll(employees);

        return employees;
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
