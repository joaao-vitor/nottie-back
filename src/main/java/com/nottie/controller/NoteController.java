package com.nottie.controller;

import com.nottie.dto.request.note.NewNoteDTO;
import com.nottie.dto.request.notesgroup.EditSingleCategoryDTO;
import com.nottie.dto.request.note.EditSingleCategoryValueDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.service.NoteService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<?> getSingleNote(@PathVariable Long noteId){
        NoteDTO noteDTO = noteService.getSingleNote(noteId);
    }

    @PostMapping()
    public ResponseEntity<?> createNote(@RequestBody NewNoteDTO newNoteDTO) {
        noteService.createNote(newNoteDTO);
        return ResponseUtil.buildSuccessResponse("Note created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{noteId}/category/{categoryValueId}")
    public ResponseEntity<?> editSingleCategoryValue(@PathVariable Long noteId, @PathVariable Long categoryValueId, @RequestBody EditSingleCategoryValueDTO editSingleCategoryValueDTO){
        noteService.editSingleCategory(noteId, categoryValueId, editSingleCategoryValueDTO);
        return ResponseUtil.buildSuccessResponse("Notes Groups category updated successfully", HttpStatus.OK);
    }
}
