package com.productivity.backend.actual;

import com.productivity.backend.actual.dto.ActualEventRequest;
import com.productivity.backend.actual.dto.ActualEventResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/actual-events")
@RequiredArgsConstructor
public class ActualEventController {

    private final ActualEventService actualEventService;

    @PostMapping
    public ResponseEntity<ActualEventResponse> create(@Valid @RequestBody ActualEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actualEventService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ActualEventResponse>> list(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return ResponseEntity.ok(actualEventService.list(start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActualEventResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(actualEventService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActualEventResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ActualEventRequest request) {
        return ResponseEntity.ok(actualEventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        actualEventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
