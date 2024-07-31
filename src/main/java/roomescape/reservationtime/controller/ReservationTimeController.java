package roomescape.reservationtime.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import roomescape.reservationtime.dto.AvailableTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.reservationtime.dto.ReservationTimeRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;

@RestController
@RequestMapping("/times")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @PostMapping
    public ResponseEntity<ReservationTimeResponse> saveReservationTime(
        @RequestBody @Valid ReservationTimeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationTimeService.saveReservationTime(request));
    }

    @GetMapping
    public ResponseEntity<List<ReservationTimeResponse>> getReservationTimes() {
        return ResponseEntity.ok().body(reservationTimeService.findReservationTimes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable Long id) {
        reservationTimeService.deleteReservationTime(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableTimeResponse>> getAvailableReservationTime(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok().body(reservationTimeService.findAvailableReservationTimes(date, themeId));
    }
}
