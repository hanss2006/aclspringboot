package com.ocrv.skimrv.backend.dto;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import lombok.Data;

@Data
public class FormSkimItResponse {
    private Integer id;
    private DictRate dictRate;
    private OrgUnitDictionary orgUnitDictionary;
}
