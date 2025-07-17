package com.nottie.repository;

import com.nottie.model.Note;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    @EntityGraph(attributePaths = {"creator", "collaborators", "categoriesValues"})
    @Query("SELECT n FROM Note n WHERE n.notesGroup.id = :notesGroupId")
    List<Note> findSummaryByNotesGroup_Id(@Param("notesGroupId") Long notesGroupId);
}
