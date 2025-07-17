package com.nottie.repository;

import com.nottie.model.NotesGroupCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesGroupCategoryRepository extends JpaRepository<NotesGroupCategory, Long> {
    List<NotesGroupCategory> findAllByGroup_Id(Long groupId);
}
