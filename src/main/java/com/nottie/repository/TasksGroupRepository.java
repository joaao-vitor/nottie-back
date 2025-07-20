package com.nottie.repository;

import com.nottie.model.TasksGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TasksGroupRepository extends JpaRepository<TasksGroup, Long> {
    List<TasksGroup> findAllByWorkstation_Id(Long workstationId);

    List<TasksGroup> findAllByCreator_IdAndWorkstation_IdIsNull(Long creatorId);
}
