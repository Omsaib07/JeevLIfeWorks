package com.jeevlifeworks.Smart.Task.Manager.App.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jeevlifeworks.Smart.Task.Manager.App.Entity.Task;
import com.jeevlifeworks.Smart.Task.Manager.App.Entity.User;

/**
 * Repository interface for Task entity.
 * Provides CRUD and custom query methods for task management.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
	/**
     * Finds all tasks assigned to a specific user (assignee).
     * Uses a JPQL JOIN between Task and its assignees.
     *
     * Accepts parameter userId ID of the user (assignee)
     * return List of tasks assigned to the user
     */
    @Query("SELECT t FROM Task t JOIN t.assignees a WHERE a.id = :userId")
    List<Task> findByAssigneeId(@Param("userId") Long userId);

    /**
     * Finds all tasks created/managed by a specific manager.
     *
     * Accepts parameter manager User entity representing the manager
     * return List of tasks managed by the manager
     */
    List<Task> findByManager(User manager);

    /**
     * Finds all overdue tasks that are not completed.
     *
     * Accepts parameter date The date to compare task due dates against
     * Accepts parameter status Status to exclude (e.g., "Completed")
     * return List of overdue tasks excluding completed ones
     */
    List<Task> findByDueDateBeforeAndStatusIsNot(LocalDate date, String status);

    /**
     * Filters tasks based on optional criteria: status, priority,
     * assignee ID, and due date.
     *
     * If any parameter is null, it is ignored in filtering.
     *
     * Accepts parameter status Task status filter
     * Accepts parameter priority Task priority filter
     * Accepts parameter assigneeId User ID of assignee filter
     * Accepts parameter dueDate Task due date filter
     * return List of tasks matching the filter criteria
     */
    @Query("SELECT DISTINCT t FROM Task t LEFT JOIN t.assignees a WHERE " +
           "(:status IS NULL OR t.status = :status) AND " +
           "(:priority IS NULL OR t.priority = :priority) AND " +
           "(:assigneeId IS NULL OR a.id = :assigneeId) AND " +
           "(:dueDate IS NULL OR t.dueDate = :dueDate)")
    List<Task> findByFilterCriteria(@Param("status") String status,
                                    @Param("priority") String priority,
                                    @Param("assigneeId") Long assigneeId,
                                    @Param("dueDate") LocalDate dueDate);
}
