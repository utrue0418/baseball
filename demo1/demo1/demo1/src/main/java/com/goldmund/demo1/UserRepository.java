package com.goldmund.demo1;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 💡 로그인할 때 아이디로 회원을 찾는 마법의 메서드 (스프링이 이름만 보고 알아서 쿼리를 짜줍니다!)
    Optional<User> findByUsername(String username);
}
