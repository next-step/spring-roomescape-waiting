package nextstep.config;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import nextstep.member.MemberDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        return new LoginService(memberDao, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}