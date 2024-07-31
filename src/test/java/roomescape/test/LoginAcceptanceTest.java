package roomescape.test;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.util.LoginUtils.COOKIE_NAME_FOR_LOGIN;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_EMAIL;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_NAME;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_PASSWORD;
import static roomescape.step.LoginStep.로그아웃;
import static roomescape.step.LoginStep.로그인;
import static roomescape.step.LoginStep.토큰_생성;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.error.RoomescapeErrorMessage;

@DisplayName("로그인 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginAcceptanceTest {

    @Test
    void 로그인_성공() {
        ExtractableResponse<Response> response = 로그인(FIRST_USER_EMAIL, FIRST_USER_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.cookie(COOKIE_NAME_FOR_LOGIN)).isNotBlank();
    }

    @Test
    void 등록되지_않은_이메일을_입력할경우_로그인_실패() {
        ExtractableResponse<Response> response = 로그인("new@email.com", FIRST_USER_PASSWORD);

        assertThat(response.statusCode()).isEqualTo(404);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo("해당하는 고객" + RoomescapeErrorMessage.NOT_EXISTS_EXCEPTION);
    }

    @Test
    void 일치하지_않는_비밀번호를_입력할_경우_로그인_실패() {
        ExtractableResponse<Response> response = 로그인(FIRST_USER_EMAIL, "newPassword");

        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo(RoomescapeErrorMessage.NOT_MATCHED_PASSWORD_EXCEPTION);
    }

    @Test
    void 로그인_후_정보_조회_성공() {
        ExtractableResponse<Response> response = 로그인_정보_조회(토큰_생성(FIRST_USER_EMAIL, FIRST_USER_PASSWORD));

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().get("name").toString()).isEqualTo(FIRST_USER_NAME);
    }

    @Test
    void 로그아웃_성공() {
        ExtractableResponse<Response> response = 로그아웃();

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.cookie(COOKIE_NAME_FOR_LOGIN)).isBlank();
    }

    @Test
    void 로그아웃_후_정보_조회_시_실패() {
        로그인(FIRST_USER_EMAIL, FIRST_USER_PASSWORD);

        Cookie token = 로그아웃().detailedCookie(COOKIE_NAME_FOR_LOGIN);

        ExtractableResponse<Response> response = 로그인_정보_조회(token);

        assertThat(response.statusCode()).isEqualTo(401);
    }

    private ExtractableResponse<Response> 로그인_정보_조회(Cookie token) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .cookie(token)
            .when().get("/login/check")
            .then().log().all()
            .extract();
    }
}
