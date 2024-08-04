package live.ioteatime.frontservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin().disable()
                .httpBasic().disable()
                .csrf().ignoringRequestMatchers(request ->
                        StringUtils.containsIgnoreCase(request.getRequestURI(), "/notify") ||
                                StringUtils.containsIgnoreCase(request.getRequestURI(), "/sensors/mqtt/**")
                ).and()
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/js/**").permitAll() // 정적 리소스에 대한 접근 허용
                .anyRequest().permitAll();

        return http.build();
    }
}
