package org.moviematchers.moviematch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	@Order(1)
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		security
				.cors(Customizer.withDefaults())   // Enable CORS with default settings
				.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection
				.authorizeHttpRequests((authz) -> authz
				.requestMatchers(HttpMethod.POST, "/user/register").permitAll()
				.anyRequest().authenticated()
				)
				.httpBasic(withDefaults()); // Enable HTTP Basic Authentication // Enable HTTP Basic Authentication

		return security.build();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}