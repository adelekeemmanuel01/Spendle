package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dto.DashboardResponse;
import com.emmyscode.spendle.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable UUID userId) {
        return ResponseEntity.ok(dashboardService.getDashboard(userId));
    }
}
