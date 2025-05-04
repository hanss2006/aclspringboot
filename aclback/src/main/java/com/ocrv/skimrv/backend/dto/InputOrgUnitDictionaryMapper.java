package com.ocrv.skimrv.backend.dto;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface InputOrgUnitDictionaryMapper {
    @Mapping(target = "id", source = "id") // Явное указание маппинга ID
    InputOrgUnitDictionaryDto toDto(OrgUnitDictionary entity);
}
