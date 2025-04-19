package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictRateRepository extends JpaRepository<DictRate, Integer> {
}
