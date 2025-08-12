package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import java.util.List;

import lombok.Data;

@Data
public class DashboardAnalyticsDto {
	
	private long assignedCount;
    private long completedCount;
    private long pendingCount;
    private long overdueCount;
    private List<TaskDistributionDto> taskDistribution;
}
