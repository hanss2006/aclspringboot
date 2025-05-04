package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InputOrgUnitDictionaryRepository  extends JpaRepository<OrgUnitDictionary, Integer> {
}
