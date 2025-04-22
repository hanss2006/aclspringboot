package com.ocrv.skimrv.backend.repository;

import com.ocrv.skimrv.backend.domain.FormSkimIt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormSkimItRepository extends JpaRepository<FormSkimIt, Integer> {
    @Override
    @PreAuthorize("hasPermission(#entity, 'WRITE') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
    <S extends FormSkimIt> S save(S entity);

    @Override
    @PreAuthorize("hasPermission(#entity, 'DELETE') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
    void delete(FormSkimIt entity);

    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
    Optional<FormSkimIt> findById(Integer integer);

    @Override
    @PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')")
    List<FormSkimIt> findAll();
}
