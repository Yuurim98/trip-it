package com.example.tripit.block.dto;

public class TotalElementsResponse {
    private long totalElements;

    public TotalElementsResponse(long totalElements) {
        this.totalElements = totalElements;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }
}
