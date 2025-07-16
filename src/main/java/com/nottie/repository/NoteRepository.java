package com.nottie.repository;

import com.nottie.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findAllByNotesGroup_Id(Long notesGroupId);
}
