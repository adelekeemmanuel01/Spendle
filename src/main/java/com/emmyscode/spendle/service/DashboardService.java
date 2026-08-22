package com.emmyscode.spendle.service;

import com.emmyscode.spendle.dto.DashboardResponse;

import java.util.UUID;

public interface DashboardService {

    DashboardResponse getDashboard(UUID userId);

}
