package com.productivity.backend.calendar;

import com.productivity.backend.calendar.dto.PlannedEventRequest;
import com.productivity.backend.calendar.dto.PlannedEventResponse;
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
@RequestMapping("/v1/events")
@RequiredArgsConstructor
public class PlannedEventController {

    private final PlannedEventService plannedEventService;

    @PostMapping
    public ResponseEntity<PlannedEventResponse> create(@Valid @RequestBody PlannedEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plannedEventService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<PlannedEventResponse>> list(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
        return ResponseEntity.ok(plannedEventService.list(start, end));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlannedEventResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(plannedEventService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlannedEventResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody PlannedEventRequest request) {
        return ResponseEntity.ok(plannedEventService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        plannedEventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
