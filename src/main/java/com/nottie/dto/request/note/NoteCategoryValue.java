package com.nottie.dto.request.note;

import com.nottie.dto.response.notesgroup.NotesGroupCategoryDTO;

public record NoteCategoryValue(String name, String textValue, Boolean checkboxValue, String hexColor, NotesGroupCategoryDTO category) {
}
