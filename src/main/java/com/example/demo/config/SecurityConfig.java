package com.example.demo.config;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins; // 從 application.properties 中讀取

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource())) // 設置 CORS 配置
            // 關閉 CSRF 驗證.csrf(AbstractHttpConfigurer::disable)
            .requiresChannel(channel -> channel.anyRequest().requiresSecure()) // 強制 HTTPS
            .authorizeHttpRequests(authroize -> authroize
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/page/login", "/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**, /api/users/**").authenticated()
                .requestMatchers("/api/admin/users/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/users/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")            
                .requestMatchers("/page/**").authenticated()
                .anyRequest().permitAll())
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/page/login")
                .defaultSuccessUrl("/page/success", true)
                .permitAll()
            )
            // 設置 Session 管理策略
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 僅在需要時創建 Session
            )
            .addFilterBefore(jwtAuthenticationFilterForApi(), UsernamePasswordAuthenticationFilter.class)
            .formLogin(form -> form
                .loginPage("/page/login") // 自定義登入頁面
                .loginProcessingUrl("/api/auth/login") // 登入請求的路徑
                .defaultSuccessUrl("/page/success", true) // 登入成功後跳轉的頁面
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/page/login?logout=true") // 登出後跳轉的頁面
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // 配置 JdbcUserDetailsManager 並設置查詢語句（如果需要自定義）
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // 默認查詢語句（可省略，只有當資料表結構不一致時才需修改）
        jdbcUserDetailsManager.setUsersByUsernameQuery(
            "SELECT username, password, enabled FROM users WHERE username = ?");
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
            "SELECT username, authority FROM authorities WHERE username = ?");

        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilterForApi() {
        return new JwtAuthenticationFilter() {
            @Override
            protected boolean shouldNotFilter(HttpServletRequest request) {
                // 僅對 /api/** 的請求應用此過濾器
                String path = request.getRequestURI();
                return !path.startsWith("/api/");
            }
        };
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
