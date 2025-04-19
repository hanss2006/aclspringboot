package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgUnitDictionaryRepository extends JpaRepository<OrgUnitDictionary, Integer> {
}
