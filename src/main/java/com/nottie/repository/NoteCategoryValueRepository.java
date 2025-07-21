package com.nottie.repository;

import com.nottie.model.NoteCategoryValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteCategoryValueRepository extends JpaRepository<NoteCategoryValue, Long> {
    List<NoteCategoryValue> findAllByCategory_Id(Long categoryId);
}
