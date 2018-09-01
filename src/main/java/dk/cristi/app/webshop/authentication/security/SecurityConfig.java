package dk.cristi.app.webshop.authentication.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
/* From: http://www.baeldung.com/oauth-api-testing-with-spring-mvc
 * Starting with Spring Boot version 1.5.0 the security adapter takes priority over the OAuth resource adapter,
 * so in order to reverse the order, we have to annotate the WebSecurityConfigurerAdapter class with @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER).
 * Otherwise, Spring will attempt to access requested URLs based on the Spring Security rules instead of Spring OAuth rules, and we would receive a 403 error
 * when using token authentication.*/
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CorsConfig corsConfig;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(User.withDefaultPasswordEncoder().username("john.carnell").password("password").roles("USER").build())
                .withUser(User.withDefaultPasswordEncoder().username("william").password("hispass").roles("ADMIN").build());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .cors();
        http.addFilterBefore(corsConfig, BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/token").permitAll()
                .anyRequest().authenticated();

    }

    // CORS
//    @Bean
//    FilterRegistrationBean corsFilter(
//            @Value("${tagit.origin:http://localhost:4200}") String origin) {
//        return new FilterRegistrationBean(new Filter() {
//            public void doFilter(ServletRequest req, ServletResponse res,
//                                 FilterChain chain) throws IOException, ServletException {
//                HttpServletRequest request = (HttpServletRequest) req;
//                HttpServletResponse response = (HttpServletResponse) res;
//                String method = request.getMethod();
//                // this origin value could just as easily have come from a database
//                response.setHeader("Access-Control-Allow-Origin", origin);
//                response.setHeader("Access-Control-Allow-Methods",
//                        "POST,GET,OPTIONS,DELETE");
//                response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
//                response.setHeader("Access-Control-Allow-Credentials", "true");
//                response.setHeader(
//                        "Access-Control-Allow-Headers",
//                        "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
//                if ("OPTIONS".equals(method)) {
//                    response.setStatus(HttpStatus.OK.value());
//                } else {
//                    chain.doFilter(req, res);
//                }
//            }
//
//            public void init(FilterConfig filterConfig) {
//            }
//
//            public void destroy() {
//            }
//        });
//    }

    @Component
    public static class CorsConfig extends GenericFilterBean {

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.addHeader("Access-Control-Allow-Headers",
                    "Access-Control-Allow-Origin,"
                            + "Authorization,"
                            + "Origin, Accept,"
                            + "X-Requested-With,"
                            + "Content-Type,"
                            + "Access-Control-Request-Method,"
                            + "Access-Control-Request-Headers");

            response.addHeader("Access-Control-Allow-Origin", "*");
            filterChain.doFilter(servletRequest, response);
        }
    }
}
