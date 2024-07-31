package roomescape.util;

import static roomescape.util.CookieUtils.deleteCookie;
import static roomescape.util.CookieUtils.getCookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import roomescape.error.exception.AuthenticationException;

public class LoginUtils {

    public static String COOKIE_NAME_FOR_LOGIN = "token";

    public static String getToken(HttpServletRequest request) {
        return getCookie(request, COOKIE_NAME_FOR_LOGIN)
            .orElseThrow(AuthenticationException::new)
            .getValue();
    }

    public static ResponseCookie setToken(String token) {
        return ResponseCookie
            .from(COOKIE_NAME_FOR_LOGIN, token)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .build();
    }

    public static void deleteToken(HttpServletResponse response) {
        deleteCookie(response, COOKIE_NAME_FOR_LOGIN);
    }
}
