package roomescape.reservation.presentation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.UserId;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.UserReservationCreateRequest;
import roomescape.waiting.application.ReservationWaitingService;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationWaitingService reservationWaitingService;

    public ReservationController(ReservationService reservationService,
                                 ReservationWaitingService reservationWaitingService) {
        this.reservationService = reservationService;
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> saveReservation(@RequestBody @Valid UserReservationCreateRequest request,
                                                               @UserId Long userId) {
        ReservationCreateRequest reservationCreateRequest = request.toReservationCreateRequest(userId);
        ReservationResponse response = reservationService.save(reservationCreateRequest, userId);
        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        List<ReservationResponse> responses = reservationService.getReservations();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> getMyReservations(@UserId Long userId) {
        List<MyReservationResponse> responses = new ArrayList<>(reservationService.getMyReservations(userId));
        List<MyReservationResponse> waitingResponses = reservationWaitingService.getMyWaiting(userId);
        responses.addAll(waitingResponses);
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }
}
