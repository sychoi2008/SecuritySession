package com.ex.TestSecurityBasic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 이 클래스가 스프링 시큐리티에게서도 관리가 됨
public class SecurityConfig {

    // 이 메소드를 어디에서든 호출해서 사용할 수 있도록 빈 등록
    // 회원가입할 때 사용
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // 빈으로 등록해서 자동으로 필터에다가 시큐리티 설정을 커스텀할 수 있음
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/", "/login", "/join", "/joinProc").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated()
                );


        http
                .httpBasic(Customizer.withDefaults());
//                .formLogin((auth) -> auth.loginPage("/login") //loginPage : 로그인 페이지 경로
//                        .loginProcessingUrl("/loginProc") // 프론트에서 받은 아이디, 비밀번호를 전달할 경로
//                        .permitAll() // 해당 경로에서 누구나 접근 가능
//                );

        //http // 사이트 위변조 방지 설정이 시큐리티에는 자동으로 설정되어 있음
       //         .csrf((auth) -> auth.disable());

        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1) // 동시 로그인 가능한 개수 
                        .maxSessionsPreventsLogin(true)); // 다중 로그인 개수를 초과했을 때 처리 방법 -> true : 새로운 로그인 차단


        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId()); // 해커가 만든 세션 id를 바꿔버림 


        return http.build();

    }
}