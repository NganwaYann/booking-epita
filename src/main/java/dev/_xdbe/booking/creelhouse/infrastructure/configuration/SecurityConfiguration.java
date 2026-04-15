package dev._xdbe.booking.creelhouse.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/dashboard").hasRole("ADMIN")
            .anyRequest().permitAll()
        )
        .formLogin(withDefaults())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
        .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.builder()
            .username("admin")
            .password("{bcrypt}$2a$10$gGPFhBZApLv4ypGPwX/s1OxN4B6hx7xtEtWNaFEi1von4W/c/usBC")
            .roles("ADMIN")
            .build();

        UserDetails guest = User.builder()
            .username("guest")
            .password("{bcrypt}$2a$10$dH13mt4hjT2BvyLbPd7ehuhyCno5SzCK6yW/FKplSiqHyDmW9bF52")
            .roles("GUEST")
            .build();

        return new InMemoryUserDetailsManager(admin, guest);
    }

}
