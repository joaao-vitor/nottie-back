package com.nottie.dto.request.workstation;

import com.nottie.dto.common.PageableDTO;
import com.nottie.dto.response.workstation.WorkstationMemberDTO;

import java.util.List;
import java.util.Objects;

public class GetLeadersDTO extends PageableDTO {
    private Long workstationId;
    private List<WorkstationLeaderDTO> leaders;

    public GetLeadersDTO(int totalPages, long totalElements, int size, int number, Long workstationId, List<WorkstationLeaderDTO> leaders) {
        super(totalPages, totalElements, size, number);
        this.workstationId = workstationId;
        this.leaders = leaders;
    }

    public GetLeadersDTO(Long workstationId, List<WorkstationLeaderDTO> leaders) {
        this.workstationId = workstationId;
        this.leaders = leaders;
    }

    public GetLeadersDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GetLeadersDTO that = (GetLeadersDTO) o;
        return Objects.equals(workstationId, that.workstationId) && Objects.equals(leaders, that.leaders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), workstationId, leaders);
    }

    public Long getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(Long workstationId) {
        this.workstationId = workstationId;
    }

    public List<WorkstationLeaderDTO> getLeaders() {
        return leaders;
    }

    public void setLeaders(List<WorkstationLeaderDTO> leaders) {
        this.leaders = leaders;
    }
}
