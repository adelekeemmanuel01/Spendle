package com.emmyscode.spendle.controller;

import com.emmyscode.spendle.dtos.DashboardResponseDTO;
import com.emmyscode.spendle.model.User;
import com.emmyscode.spendle.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GET /api/dashboard
 *
 * Returns financial summary totals for the authenticated user.
 * The user id is NEVER taken from the request — it comes from @AuthenticationPrincipal.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(dashboardService.getDashboard(currentUser));
    }
}
