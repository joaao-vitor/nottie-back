package com.nottie.repository;

import com.nottie.model.TaskCategoryValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskCategoryValueRepository extends JpaRepository<TaskCategoryValue, Long> {
    List<TaskCategoryValue> findAllByCategory_Id(Long categoryId);
}
