package com.nottie.dto.request.task;

public record NewTaskCategoryValueDTO(String textValue, boolean checkboxValue, String hexColor, Long categoryId) {
}
