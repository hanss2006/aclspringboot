package com.ocrv.skimrv.backend.dto;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrgUnitDictionaryMapper {
    OrgUnitDictionary toEntity(OrgUnitDictionaryDto orgUnitDictionaryDto);

    OrgUnitDictionaryDto toOrgUnitDictionaryDto(OrgUnitDictionary orgUnitDictionary);

    OrgUnitDictionary updateWithNull(OrgUnitDictionaryDto orgUnitDictionaryDto, @MappingTarget OrgUnitDictionary orgUnitDictionary);
}