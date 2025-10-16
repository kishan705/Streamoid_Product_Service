package com.StreamoidProductService.streamoid.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FailedRow {
    private long rowNumber;
    private String error;
    private String rowData;
}
