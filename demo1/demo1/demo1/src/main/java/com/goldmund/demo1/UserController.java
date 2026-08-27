package com.goldmund.demo1;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. 회원가입
    @PostMapping("/api/user/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 아이디입니다.");
        }
        userRepository.save(user);
        return ResponseEntity.ok("회원가입 성공!");
    }

    // 2. 로그인
    @PostMapping("/api/user/login")
    // 💡 HttpSession: 접속한 브라우저마다 부여되는 개인 캐비닛(세션)입니다.
    public ResponseEntity<String> login(@RequestBody User loginUser, HttpSession session) {
        return userRepository.findByUsername(loginUser.getUsername())
                .filter(user -> user.getPassword().equals(loginUser.getPassword()))
                .map(user -> {
                    // 비밀번호가 맞으면 세션 캐비닛에 "loggedInUser"라는 이름으로 아이디를 넣어둡니다. (로그인 증표)
                    session.setAttribute("loggedInUser", user.getUsername());
                    return ResponseEntity.ok(user.getUsername());
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디나 비밀번호가 틀렸습니다."));
    }

    // 3. 현재 로그인 상태 확인
    @GetMapping("/api/user/me")
    public String checkLogin(HttpSession session) {
        // 세션 캐비닛을 열어보고 증표가 있으면 아이디를, 없으면 빈 글자를 반환합니다.
        Object user = session.getAttribute("loggedInUser");
        return user != null ? user.toString() : "";
    }

    // 4. 로그아웃
    @PostMapping("/api/user/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 캐비닛을 완전히 부숴버립니다. (로그아웃)
        return "로그아웃 되었습니다.";
    }
}
