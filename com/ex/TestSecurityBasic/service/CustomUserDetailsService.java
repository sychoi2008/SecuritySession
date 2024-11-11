package com.ex.TestSecurityBasic.service;

import com.ex.TestSecurityBasic.dto.CustomUserDetails;
import com.ex.TestSecurityBasic.entity.UserEntity;
import com.ex.TestSecurityBasic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
DB에서 username으로 특정 유저 데이터를 가져와 CustomUserDetails 클래스에 넘겨줌
CustomUserDetails는 시큐리티에게 전달
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 후에 생성자 주입으로 바꿔야 한다
    @Autowired
    private UserRepository userRepository;


    // 파라미터 username(로그인 시 입력한 아이디)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByUsername(username);

        if(userData != null)  { // 로그인 성공
            return new CustomUserDetails(userData); // CustomUserDetails는 Spring security에 값을 보냄
        }
        return null; // 해당된 아이디가 db에 없음
    }
}
