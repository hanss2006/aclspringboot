package com.ocrv.skimrv.backend.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FormSkimItDto {
    private Integer id;

    @NotNull(message = "dictRateId is required")
    private Integer dictRateId;

    @NotNull(message = "orgUnitDictionaryId is required")
    private Integer orgUnitDictionaryId;
}
