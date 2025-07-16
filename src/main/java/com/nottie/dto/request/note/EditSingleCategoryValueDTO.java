package com.nottie.dto.request.note;

import com.nottie.model.CategoryType;

public record EditSingleCategoryValueDTO(CategoryType type, String textValue, Boolean checkboxValue, String hexColor) {
}
