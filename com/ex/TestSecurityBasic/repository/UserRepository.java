package com.ex.TestSecurityBasic.repository;

import com.ex.TestSecurityBasic.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

// 이 repository에 종속될 entity, entity의 id 타입
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

}
