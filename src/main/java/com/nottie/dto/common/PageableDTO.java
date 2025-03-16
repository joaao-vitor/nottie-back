package com.nottie.dto.common;

import java.util.Objects;

public class PageableDTO {
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public PageableDTO() {
    }

    public PageableDTO(int totalPages, long totalElements, int size, int number) {
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PageableDTO that = (PageableDTO) o;
        return totalPages == that.totalPages && totalElements == that.totalElements && size == that.size && number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPages, totalElements, size, number);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
