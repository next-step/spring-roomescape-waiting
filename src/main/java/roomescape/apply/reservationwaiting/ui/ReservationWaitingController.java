package roomescape.apply.reservationwaiting.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.apply.auth.application.annotation.NeedMemberRole;
import roomescape.apply.auth.ui.dto.LoginMember;
import roomescape.apply.member.domain.MemberRoleName;
import roomescape.apply.reservationwaiting.application.ReservationWaitingSaver;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingRequest;
import roomescape.apply.reservationwaiting.ui.dto.ReservationWaitingResponse;

@RestController
@RequestMapping("/reservation-waiting")
public class ReservationWaitingController {

    private final ReservationWaitingSaver reservationWaitingSaver;

    public ReservationWaitingController(ReservationWaitingSaver reservationWaitingSaver) {
        this.reservationWaitingSaver = reservationWaitingSaver;
    }

    @PostMapping
    @NeedMemberRole({MemberRoleName.ADMIN, MemberRoleName.GUEST})
    public ResponseEntity<ReservationWaitingResponse> addReservationWaiting(@RequestBody ReservationWaitingRequest request,
                                                                            LoginMember loginMember
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationWaitingSaver.saveReservationWaitingBy(request, loginMember));
    }

}
