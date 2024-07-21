package roomescape.waiting.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.theme.domain.Theme;
import roomescape.theme.domain.repository.ThemeRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.repository.ReservationTimeRepository;
import roomescape.user.domain.User;
import roomescape.user.domain.repository.UserRepository;
import roomescape.waiting.domain.ReservationWaiting;
import roomescape.waiting.domain.repository.ReservationWaitingRepository;
import roomescape.waiting.dto.ReservationWaitingRequest;
import roomescape.waiting.dto.ReservationWaitingResponse;

@ExtendWith(MockitoExtension.class)
class ReservationWaitingServiceTest {

    @Mock
    private ReservationWaitingRepository reservationWaitingRepository;

    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReservationWaitingService reservationWaitingService;

    @Test
    void 예약_대기를_한다() {
        // given
        User user = new User(1L, "브라운", "brown@email.com", "password", "USER");
        ReservationTime time = new ReservationTime(1L, "10:00");
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        LocalDate date = LocalDate.now();
        ReservationWaiting reservationWaiting = new ReservationWaiting(1L, date, user, time, theme);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(reservationTimeRepository.findById(anyLong())).willReturn(Optional.of(time));
        given(themeRepository.findById(anyLong())).willReturn(Optional.of(theme));
        given(reservationWaitingRepository.existsByUserAndDateAndTimeAndTheme(any(), any(), any(), any())).willReturn(false);
        given(reservationWaitingRepository.save(any())).willReturn(reservationWaiting);

        ReservationWaitingRequest request = new ReservationWaitingRequest(date, 1L, 1L);

        // when
        ReservationWaitingResponse reservationWaitingResponse = reservationWaitingService.addWaiting(request, 1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(reservationWaitingResponse.id()).isEqualTo(1L);
            softly.assertThat(reservationWaitingResponse.theme()).isEqualTo("레벨1 탈출");
            softly.assertThat(reservationWaitingResponse.date()).isEqualTo(date.toString());
            softly.assertThat(reservationWaitingResponse.time()).isEqualTo("10:00");
            softly.assertThat(reservationWaitingResponse.status()).isEqualTo("예약대기");
        });
    }

    @Test
    void 이미_에약했다면_예약_대기에_실패한다() {
        // given
        User user = new User(1L, "브라운", "brown@email.com", "password", "USER");
        ReservationTime time = new ReservationTime(1L, "10:00");
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(reservationTimeRepository.findById(anyLong())).willReturn(Optional.of(time));
        given(themeRepository.findById(anyLong())).willReturn(Optional.of(theme));
        given(reservationWaitingRepository.existsByUserAndDateAndTimeAndTheme(any(), any(), any(), any())).willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                reservationWaitingService.addWaiting(new ReservationWaitingRequest(LocalDate.now(), 1L, 1L), 1L));
    }

    @Test
    void 예약_대기를_삭제한다() {
        // given
        User user = new User(1L, "브라운", "brown@email.com", "password", "USER");
        ReservationTime time = new ReservationTime(1L, "10:00");
        Theme theme = new Theme(1L, "레벨1 탈출", "우테코 레벨1을 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        ReservationWaiting reservationWaiting = new ReservationWaiting(1L, LocalDate.now(), user, time, theme);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(reservationWaitingRepository.findById(anyLong())).willReturn(Optional.of(reservationWaiting));
        willDoNothing().given(reservationWaitingRepository).delete(any());

        // when
        reservationWaitingService.deleteWaiting(1L, 1L);

        // then
        verify(reservationWaitingRepository, times((1))).delete(reservationWaiting);
    }
}
