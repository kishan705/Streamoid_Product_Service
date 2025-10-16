package com.StreamoidProductService.streamoid.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {
    private int stored;
    private List<FailedRow> failed;
}
