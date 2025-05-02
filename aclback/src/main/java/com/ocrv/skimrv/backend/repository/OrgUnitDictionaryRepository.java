package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgUnitDictionaryRepository extends JpaRepository<OrgUnitDictionary, Integer> {
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<OrgUnitDictionary> findAll();
}
