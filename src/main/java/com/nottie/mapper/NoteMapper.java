package com.nottie.mapper;

import com.nottie.dto.response.note.NoteCategoryValueDTO;
import com.nottie.dto.response.note.NoteDTO;
import com.nottie.dto.response.note.SearchNoteDTO;
import com.nottie.dto.response.timeline.NoteTimelinePostDTO;
import com.nottie.model.Note;
import com.nottie.model.NoteCategoryValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper
public interface NoteMapper {
    NoteMapper INSTANCE = Mappers.getMapper(NoteMapper.class);

    @Mapping(target = "categories", ignore = true)
    NoteDTO noteToNoteDTO(Note note);

    NoteCategoryValueDTO noteCategoryValueToNoteCategoryValueDTO(NoteCategoryValue noteCategoryValue);

    Set<NoteCategoryValueDTO> noteCategoryValueListToNoteCategoryValueDTOS(Set<NoteCategoryValue> noteCategoryValue);

    NoteTimelinePostDTO noteToNoteTimelinePostDTO(Note note);

    List<SearchNoteDTO> noteToSearchNoteDTOS(List<Note> notes);
}
