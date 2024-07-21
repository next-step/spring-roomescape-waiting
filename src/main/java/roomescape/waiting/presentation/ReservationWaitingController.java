package roomescape.waiting.presentation;

import java.net.URI;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.UserId;
import roomescape.waiting.application.ReservationWaitingService;
import roomescape.waiting.dto.ReservationWaitingRequest;
import roomescape.waiting.dto.ReservationWaitingResponse;

@RestController
public class ReservationWaitingController {

    private final ReservationWaitingService reservationWaitingService;

    public ReservationWaitingController(ReservationWaitingService reservationWaitingService) {
        this.reservationWaitingService = reservationWaitingService;
    }

    @PostMapping("/waiting")
    public ResponseEntity<ReservationWaitingResponse> addWaiting(@RequestBody @Valid ReservationWaitingRequest request, @UserId Long userId) {
        ReservationWaitingResponse response = reservationWaitingService.addWaiting(request, userId);
        return ResponseEntity.created(URI.create("/waiting/" + response.id())).body(response);
    }

    @DeleteMapping("/waiting/{waitingId}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long waitingId, @UserId Long userId) {
        reservationWaitingService.deleteWaiting(waitingId, userId);
        return ResponseEntity.noContent().build();
    }
}
