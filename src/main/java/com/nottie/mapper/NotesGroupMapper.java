package com.nottie.mapper;

import com.nottie.dto.response.notesgroup.GetAllNotesGroupDTO;
import com.nottie.dto.response.notesgroup.NotesGroupDTO;
import com.nottie.model.NotesGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface NotesGroupMapper {
    NotesGroupMapper INSTANCE = Mappers.getMapper(NotesGroupMapper.class);

    List<GetAllNotesGroupDTO> notesGroupToAllNotesGroupDTO(List<NotesGroup> notesGroups);

    @Mapping(target = "notes", ignore = true)
    NotesGroupDTO notesGroupToNotesGroupDTO(NotesGroup notesGroup);

}
