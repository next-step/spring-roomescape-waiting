package roomescape.waiting.presentation;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.auth.dto.LoginRequest;
import roomescape.waiting.dto.ReservationWaitingRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationWaitingControllerTest {

    private String accessToken;

    @BeforeEach
    void setUp() {
        LoginRequest loginRequest = new LoginRequest("user@email.com", "password");

        accessToken = RestAssured.given().log().all()
                .body(loginRequest)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    @Test
    void 예약_대기를_한다() {
        ReservationWaitingRequest request = new ReservationWaitingRequest(LocalDate.now().plusDays(1), 1L, 1L);

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/waiting")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 예약_대기를_취소한다() {
        // given
        예약_대기를_한다();

        // when & then
        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .when().delete("/waiting/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
