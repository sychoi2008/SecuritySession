package com.ex.TestSecurityBasic.service;

import com.ex.TestSecurityBasic.dto.JoinDTO;
import com.ex.TestSecurityBasic.entity.UserEntity;
import com.ex.TestSecurityBasic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    // 필드 주입에서 나중에 꼭 생성자 주입으로 바꿀 것
    @Autowired
    private UserRepository userRepository;

    // password를 반드시 암호화해야 하므로 config > SecurityConfig에 있는 bCrypt 메소드(빈으로 등록되어 있음) 사용
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public void joinProcess(JoinDTO joinDTO) {

        // db에 이미 동일한 username을 가진 회원이 존재하는가? > userRepository의 메소드 커스텀
        boolean isUser = userRepository.existsByUsername(joinDTO.getUsername());
        if(isUser) return;

        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        // 해시 암호화 진행
        /*
        내가 만든 프로젝트에서는 프론트 단에서 salt 생성 -> 비밀번호와 붙여서 -> sha-256 진행
         */
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");

        userRepository.save(data);
    }
}
