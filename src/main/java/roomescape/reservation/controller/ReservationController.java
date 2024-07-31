package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import roomescape.login.LoginMember;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok().body(reservationService.findReservations());
    }

    @GetMapping("/mine")
    public ResponseEntity<List<MyReservationResponse>> getReservations(LoginMember loginMember) {
        return ResponseEntity.ok().body(reservationService.findReservationsByMember(loginMember.getId()));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> saveReservation(LoginMember loginMember,
        @RequestBody @Valid ReservationRequest request) {
        return ResponseEntity.ok()
            .body(reservationService.saveReservation(loginMember.getId(), request));
    }

    @PostMapping("/waiting")
    public ResponseEntity<ReservationResponse> saveWaitingReservation(LoginMember loginMember,
        @RequestBody @Valid ReservationRequest request) {
        return ResponseEntity.ok()
            .body(reservationService.saveWaitingReservation(loginMember.getId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(Long.parseLong(id));
        return ResponseEntity.noContent().build();
    }
}
