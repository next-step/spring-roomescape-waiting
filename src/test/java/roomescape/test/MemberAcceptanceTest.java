package roomescape.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.error.RoomescapeErrorMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.member.initializer.MemberInitializer.FIRST_USER_EMAIL;
import static roomescape.step.MemberStep.회원_등록;

@DisplayName("회원 관련 api 호출 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberAcceptanceTest {

    private final String email = "new@test.com";
    private final String password = "newPassword";
    private final String name = "new";

    @Test
    void 회원_등록_성공() {
        ExtractableResponse<Response> response = 회원_등록(email, password, name);

        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.jsonPath().get("email").toString()).isEqualTo(email);
        assertThat(response.jsonPath().get("name").toString()).isEqualTo(name);
    }

    @Test
    void 존재하는_회원의_이메일로_회원_등록_실패() {
        ExtractableResponse<Response> response = 회원_등록(FIRST_USER_EMAIL, password, name);

        assertThat(response.statusCode()).isEqualTo(409);
        assertThat(response.jsonPath().get("message").toString())
            .isEqualTo("해당하는 고객" + RoomescapeErrorMessage.ALREADY_EXISTS_EXCEPTION);
    }

    @Test
    void 회원_조회_성공() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/members")
                .then().log().all()
                .statusCode(200);
    }
}
