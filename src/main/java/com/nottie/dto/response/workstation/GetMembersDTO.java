package com.nottie.dto.response.workstation;

import java.util.List;
import java.util.Objects;

public class GetMembersDTO {
    private Long workstationId;
    private List<WorkstationMemberDTO> members;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;

    public GetMembersDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GetMembersDTO that = (GetMembersDTO) o;
        return totalPages == that.totalPages && totalElements == that.totalElements && size == that.size && number == that.number && Objects.equals(workstationId, that.workstationId) && Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workstationId, members, totalPages, totalElements, size, number);
    }

    public Long getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(Long workstationId) {
        this.workstationId = workstationId;
    }

    public List<WorkstationMemberDTO> getMembers() {
        return members;
    }

    public void setMembers(List<WorkstationMemberDTO> members) {
        this.members = members;
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
