package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.domain.FormSkimIt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormSkimItRepository extends JpaRepository<FormSkimIt, Integer> {
}
