package nextstep.reservation.controller;

import auth.AuthenticationException;
import java.net.URI;
import nextstep.member.LoginMember;
import nextstep.member.Member;
import nextstep.reservation.dto.ReservationWaitingRequest;
import nextstep.reservation.service.ReservationWaitingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/reservation-waitings")
@RestController
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(
        ReservationWaitingService reservationWaitingService
    ) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping
    public ResponseEntity<Void> putOnWaitingList(
        @LoginMember Member member,
        @RequestBody ReservationWaitingRequest request
    ) {
        Long waitingId = reservationWaitingService.putOnWaitingList(member, request);
        return ResponseEntity.created(URI.create("/reservation-waitings/" + waitingId)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFromWaitingList(
        @LoginMember Member member,
        @PathVariable Long id
    ) {
        reservationWaitingService.deleteFromWaitingListById(member, id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> onException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> onAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
