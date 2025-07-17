package com.nottie.repository;

import com.nottie.model.NoteCategoryValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NoteCategoryValueRepository extends JpaRepository<NoteCategoryValue, Long> {
    Set<NoteCategoryValue> findAllByCategory_Id(Long categoryId);
}
