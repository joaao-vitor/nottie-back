package com.nottie.repository;

import com.nottie.model.NotesGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesGroupRepository extends JpaRepository<NotesGroup, Long> {



    List<NotesGroup> findAllByWorkstation_Id(Long workstationId);

    List<NotesGroup> findAllByUser_Id(Long userId);
}
