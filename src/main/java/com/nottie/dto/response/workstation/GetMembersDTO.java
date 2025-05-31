package com.nottie.dto.response.workstation;

import com.nottie.dto.common.PageableDTO;

import java.util.List;
import java.util.Objects;

public class GetMembersDTO extends PageableDTO {
    private Long workstationId;
    private List<WorkstationMemberDTO> members;
    private WorkstationMemberDTO creator;

    public GetMembersDTO() {
    }

    public WorkstationMemberDTO getCreator() {
        return creator;
    }

    public void setCreator(WorkstationMemberDTO creator) {
        this.creator = creator;
    }

    public GetMembersDTO(Long workstationId, List<WorkstationMemberDTO> members) {
        this.workstationId = workstationId;
        this.members = members;
    }

    public GetMembersDTO(int totalPages, long totalElements, int size, int number, Long workstationId, List<WorkstationMemberDTO> members) {
        super(totalPages, totalElements, size, number);
        this.workstationId = workstationId;
        this.members = members;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GetMembersDTO that = (GetMembersDTO) o;
        return Objects.equals(workstationId, that.workstationId) && Objects.equals(members, that.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), workstationId, members);
    }
}
