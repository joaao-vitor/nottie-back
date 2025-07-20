package com.nottie.repository;

import com.nottie.model.TasksGroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TasksGroupCategoryRepository extends JpaRepository<TasksGroupCategory, Long> {
    Set<TasksGroupCategory> findAllByGroup_Id(Long groupId);
}
