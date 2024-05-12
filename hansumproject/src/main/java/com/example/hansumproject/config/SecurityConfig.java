package com.example.hansumproject.config;

import com.example.hansumproject.jwt.CustomLogoutFilter;
import com.example.hansumproject.jwt.JWTFilter;
import com.example.hansumproject.jwt.JWTUtil;
import com.example.hansumproject.jwt.LoginFilter;
import com.example.hansumproject.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguraion 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JWTUtil jwtUtil;

    private RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // 패스워드를 인코딩하기 위한 BCryptPasswordEncoder
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //CORS 설정
//        http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
//
//            @Override
//            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//
//                CorsConfiguration configuration = new CorsConfiguration();
//
//                3000포트 허용
//                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//                모든 메소드 허용
//                configuration.setAllowedMethods(Collections.singletonList("*"));
//                configuration.setAllowCredentials(true);
//                configuration.setAllowedHeaders(Collections.singletonList("*"));
//                configuration.setMaxAge(3600L);
//
//                configuration.setExposedHeaders(Collections.singletonList("Authorization"));
//
//                return configuration;
//            }
//        })));


        //csrf disable
        //세션을 사용할 때는 csrf가 필수이지만 JWT 사용 시는 csrf에 대해서 방어하지 않아됨
        http.csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        //FormLogin을 disable 시키면 UsernamePasswordAuthenticationFilter가 동작을 안한다.
        //그래서 우리는 로그인 검증을 하기 위해 UsernamePasswordAuthenticationFilter를 상속받아 LoginFilter로 만들어 Filter를 추가함.
        http.formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        // "/login", "/", "/join"에 모든 허용 권한 사용
        // "/admin" 은 "ADMIN" 권한을 가진 사용자만 사용
        // anyRequest => 그 외에 다른 사이트는 로그인 한 사용자만 사용 가능
        http
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join", "/image/**").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .requestMatchers("/reissue").permitAll()
                .anyRequest().authenticated());


        //JWT token 검증 필터
        http.addFilterAt(new JWTFilter(jwtUtil), LoginFilter.class);

        //Login 필터
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        //Logout 필터
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        //세션 설정
        //JWT를 통한 인증/인가를 위해 STATELESS 상태로 설정
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
