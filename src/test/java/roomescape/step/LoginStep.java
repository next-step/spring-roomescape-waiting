package roomescape.step;

import static roomescape.util.LoginUtils.COOKIE_NAME_FOR_LOGIN;
import static roomescape.member.initializer.MemberInitializer.DUMMY_ADMIN_EMAIL;
import static roomescape.member.initializer.MemberInitializer.DUMMY_ADMIN_PASSWORD;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_EMAIL;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_PASSWORD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomescape.login.dto.LoginRequest;

public class LoginStep {

    public static Cookie 회원_토큰_생성() {
        return 로그인(FIRST_USER_EMAIL, FIRST_USER_PASSWORD).detailedCookie(COOKIE_NAME_FOR_LOGIN);
    }

    public static Cookie 관리자_토큰_생성() {
        return 로그인(DUMMY_ADMIN_EMAIL, DUMMY_ADMIN_PASSWORD).detailedCookie(COOKIE_NAME_FOR_LOGIN);
    }

    public static Cookie 토큰_생성(String email, String password) {
        return 로그인(email, password).detailedCookie(COOKIE_NAME_FOR_LOGIN);
    }

    public static ExtractableResponse<Response> 로그인(String email, String password) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그아웃() {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().post("/logout")
                .then().log().all()
                .extract();
    }
}
