package com.ocrv.skimrv.backend.dto;

import lombok.Value;

/**
 * DTO for {@link com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary}
 */
@Value
public class OrgUnitDictionaryDto {
    Integer id;
    String fullName;
}