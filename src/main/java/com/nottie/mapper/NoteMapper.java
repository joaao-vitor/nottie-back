package com.nottie.mapper;

import com.nottie.dto.response.note.NoteDTO;
import com.nottie.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    List<NoteDTO> noteListToDTO(List<Note> noteList);

    NoteDTO noteToNoteDTO(Note note);
}
