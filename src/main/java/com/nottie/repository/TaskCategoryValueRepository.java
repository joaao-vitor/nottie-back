package com.nottie.repository;

import com.nottie.model.NoteCategoryValue;
import com.nottie.model.TaskCategoryValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TaskCategoryValueRepository extends JpaRepository<TaskCategoryValue, Long> {
    Set<NoteCategoryValue> findAllByCategory_Id(Long categoryId);
}
