package com.jeevlifeworks.Smart.Task.Manager.App.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskCountsDto {
	
	private long assigned;
    private long completed;
    private long pending;
    private long overdue;
}
