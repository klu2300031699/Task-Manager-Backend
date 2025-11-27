package com.example.taskmanager;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCreatedBy(Long createdBy);
    List<Task> findByAssignedTo(Long assignedTo);
    List<Task> findByCreatedByOrAssignedTo(Long createdBy, Long assignedTo);
}
