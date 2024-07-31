package roomescape.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import roomescape.error.exception.AuthenticationException;

public class CookieUtils {
    public static Cookie[] getCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            throw new AuthenticationException();
        }

        return cookies;
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = getCookies(request);

        return Arrays.stream(cookies)
            .filter(cookie -> name.equals(cookie.getName()) && !cookie.getValue().isBlank())
            .findFirst();
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }
}
