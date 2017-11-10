package in.techbeat.palapa.configuration;

import in.techbeat.palapa.configuration.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public Filter jwtAuthFilter() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Construct the custom filter chain
        httpSecurity.addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        // Configure the URI patterns and the roles needed to access them
        httpSecurity.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // Requests to ping login and register should be unrestricted
                .antMatchers("/ping", "/login", "/register").permitAll()
                // Requests to admin APIs must have role admin
                .antMatchers("/admin/**").hasRole("ADMIN")
                // All other requests must be authenticated
                .anyRequest().authenticated();
        httpSecurity.headers().cacheControl();
    }
}