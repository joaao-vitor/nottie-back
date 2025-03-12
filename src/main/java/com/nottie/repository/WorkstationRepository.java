package com.nottie.repository;

import com.nottie.model.Workstation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkstationRepository extends JpaRepository<Workstation, Long> {

    Optional<Workstation> getWorkstationsByUsername(String username);

    Optional<Workstation> getWorkstationsById(Long id);

}
