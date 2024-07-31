package roomescape.login.controller;

import static roomescape.util.LoginUtils.deleteToken;
import static roomescape.util.LoginUtils.setToken;

import java.net.URI;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.login.LoginMember;
import roomescape.login.dto.LoginRequest;
import roomescape.login.dto.LoginResponse;
import roomescape.login.service.LoginService;
import roomescape.member.MemberRole;

@Controller
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
        LoginMember loginMember = loginService.getLoginMember(loginRequest.getEmail(),
            loginRequest.getPassword());

        ResponseCookie cookie = setToken(loginService.createToken(loginMember));

        String uri = "/";

        if (loginMember.hasSameRole(MemberRole.ADMIN)) {
            uri = "/admin";
        }

        return ResponseEntity.ok().location(URI.create(uri))
            .header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> checkLogin(LoginMember loginMember) {
        return ResponseEntity.ok().body(new LoginResponse(loginMember.getName()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        deleteToken(response);

        return ResponseEntity.ok().build();
    }
}
