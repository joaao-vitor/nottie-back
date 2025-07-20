package com.nottie.controller;

import com.nottie.dto.request.note.*;
import com.nottie.dto.response.note.NoteCategoryValueDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.dto.response.note.SearchNoteDTO;
import com.nottie.dto.response.user.AuthenticatedUserDTO;
import com.nottie.service.NoteService;
import com.nottie.service.UserService;
import com.nottie.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/note")
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/search/workstation/{workstationId}")
    public ResponseEntity<?> searchNoteByWorkstation(@PathVariable("workstationId") Long workstationId, @RequestParam(required = true) String title) {
        List<SearchNoteDTO> searchNoteDTOS = noteService.searchNoteByWorkstation(title, workstationId);
        return ResponseUtil.buildSuccessResponse(searchNoteDTOS, "Notes found", HttpStatus.OK);
    }

    @GetMapping("/search/user")
    public ResponseEntity<?> searchNoteByUser(@RequestParam(required = true) String title) {
        List<SearchNoteDTO> searchNoteDTOS = noteService.searchNoteByUser(title);
        return ResponseUtil.buildSuccessResponse(searchNoteDTOS, "Notes found", HttpStatus.OK);
    }

    @GetMapping("/{noteId}")
    @PreAuthorize("@noteService.verifyAccess(#noteId)")
    public ResponseEntity<?> getSingleNote(@PathVariable Long noteId){
        NoteDTO noteDTO = noteService.getSingleNote(noteId);
        return ResponseUtil.buildSuccessResponse(noteDTO, "Note fetched successfully", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> createNote(@RequestBody NewNoteDTO newNoteDTO) {
        NoteDTO noteDTO = noteService.createNote(newNoteDTO);
        return ResponseUtil.buildSuccessResponse(noteDTO, "Note created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{noteId}/category/{categoryValueId}")
    public ResponseEntity<?> editSingleCategoryValue(@PathVariable Long noteId, @PathVariable Long categoryValueId, @RequestBody EditSingleCategoryValueDTO editSingleCategoryValueDTO){
        noteService.editSingleCategory(noteId, categoryValueId, editSingleCategoryValueDTO);
        return ResponseUtil.buildSuccessResponse("Notes Groups category updated successfully", HttpStatus.OK);
    }

    @PutMapping(value = "/{noteId}/content",     consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    @PreAuthorize("@noteService.verifyAccess(#noteId)")
    public ResponseEntity<?> editContent(@PathVariable Long noteId, @RequestBody byte[] content) {

        System.out.println("Editando conteudo da nota + " + noteId);
        System.out.println(Arrays.toString(content));
        noteService.editContent(noteId, content);
        return ResponseUtil.buildSuccessResponse("Content updated successfully", HttpStatus.OK);
    }

    @PatchMapping("/{noteId}/title")
    @PreAuthorize("@noteService.verifyAccess(#noteId)")
    public ResponseEntity<?> editNoteTtile(@PathVariable Long noteId, @RequestBody EditNoteDTO editNoteDTO) {
        noteService.editNoteTitle(noteId, editNoteDTO);
        return ResponseUtil.buildSuccessResponse("Note title updated successfully", HttpStatus.OK);
    }

    @GetMapping("/verify/{noteId}")
    public ResponseEntity<?> verifyAccess(@PathVariable Long noteId){
        noteService.verifyAccess(noteId);

        AuthenticatedUserDTO authenticatedUserDTO = userService.getCurrentUser();

        return ResponseUtil.buildSuccessResponse(authenticatedUserDTO, "Note access successfully", HttpStatus.OK);
    }

    @PostMapping("/{noteId}/category/{categoryId}")
    public ResponseEntity<?> addCategoryValue(@PathVariable Long noteId, @PathVariable Long categoryId, @RequestBody NewCategoryValueDTO noteCatValueDTO) {
        NoteCategoryValueDTO noteCategoryValueDTO = noteService.addCategoryValue(noteId, categoryId, noteCatValueDTO);
        return ResponseUtil.buildSuccessResponse(noteCategoryValueDTO, "Tag inserted successfully", HttpStatus.OK);
    }

    @DeleteMapping("/{noteId}/category/{categoryId}/{valueId}")
    public ResponseEntity<?> deleteCategoryValue(@PathVariable Long noteId, @PathVariable Long categoryId, @PathVariable Long valueId) {
        noteService.deleteCategoryValue(noteId, categoryId, valueId);
        return ResponseUtil.buildSuccessResponse("Category deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/{noteId}/category/{categoryId}/{valueId}")
    public ResponseEntity<?> editCategoryValue(@PathVariable Long noteId, @PathVariable Long categoryId, @PathVariable Long valueId, @RequestBody NewCategoryValueDTO noteCatValueDTO) {
        NoteCategoryValueDTO noteCategoryValueDTO = noteService.editCategoryValue(noteId, categoryId, noteCatValueDTO, valueId);
        return ResponseUtil.buildSuccessResponse(noteCategoryValueDTO, "Tag inserted successfully", HttpStatus.OK);
    }
}
