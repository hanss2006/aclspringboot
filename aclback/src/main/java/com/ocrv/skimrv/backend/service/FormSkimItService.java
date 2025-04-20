package com.ocrv.skimrv.backend.service;

import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dictionaries.entities.skim.DictRate;
import com.ocrv.skimrv.backend.domain.FormSkimIt;
import com.ocrv.skimrv.backend.dto.FormSkimItDto;
import com.ocrv.skimrv.backend.dto.FormSkimItResponse;
import com.ocrv.skimrv.backend.repository.DictRateRepository;
import com.ocrv.skimrv.backend.repository.FormSkimItRepository;
import com.ocrv.skimrv.backend.repository.OrgUnitDictionaryRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormSkimItService {

    private final FormSkimItRepository formSkimItRepository;
    private final DictRateRepository dictRateRepository;
    private final OrgUnitDictionaryRepository orgUnitDictionaryRepository;

    public FormSkimItService(FormSkimItRepository formSkimItRepository,
                             DictRateRepository dictRateRepository,
                             OrgUnitDictionaryRepository orgUnitDictionaryRepository) {
        this.formSkimItRepository = formSkimItRepository;
        this.dictRateRepository = dictRateRepository;
        this.orgUnitDictionaryRepository = orgUnitDictionaryRepository;
    }

    @Transactional
    @PreAuthorize("hasPermission(#id, 'com.ocrv.skimrv.backend.domain.FormSkimIt', 'CREATE')")
    public FormSkimItResponse create(FormSkimItDto dto) {
        DictRate dictRate = dictRateRepository.findById(dto.getDictRateId())
                .orElseThrow(() -> new RuntimeException("DictRate not found with id: " + dto.getDictRateId()));

        OrgUnitDictionary orgUnit = orgUnitDictionaryRepository.findById(dto.getOrgUnitDictionaryId())
                .orElseThrow(() -> new RuntimeException("OrgUnitDictionary not found with id: " + dto.getOrgUnitDictionaryId()));

        FormSkimIt entity = new FormSkimIt();
        entity.setDictRate(dictRate);
        entity.setOrgUnitDictionary(orgUnit);

        FormSkimIt saved = formSkimItRepository.save(entity);
        return convertToResponse(saved);
    }

    @Transactional
    @PreAuthorize("hasPermission(#id, 'com.ocrv.skimrv.backend.domain.FormSkimIt', 'WRITE')")
    public FormSkimItResponse update(Integer id, FormSkimItDto dto) {
        FormSkimIt existing = formSkimItRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormSkimIt not found with id: " + id));

        if (dto.getDictRateId() != null) {
            DictRate dictRate = dictRateRepository.findById(dto.getDictRateId())
                    .orElseThrow(() -> new RuntimeException("DictRate not found with id: " + dto.getDictRateId()));
            existing.setDictRate(dictRate);
        }

        if (dto.getOrgUnitDictionaryId() != null) {
            OrgUnitDictionary orgUnit = orgUnitDictionaryRepository.findById(dto.getOrgUnitDictionaryId())
                    .orElseThrow(() -> new RuntimeException("OrgUnitDictionary not found with id: " + dto.getOrgUnitDictionaryId()));
            existing.setOrgUnitDictionary(orgUnit);
        }

        FormSkimIt updated = formSkimItRepository.save(existing);
        return convertToResponse(updated);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#id, 'com.ocrv.skimrv.backend.domain.FormSkimIt', 'READ')")
    public FormSkimItResponse getById(Integer id) {
        FormSkimIt entity = formSkimItRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FormSkimIt not found with id: " + id));
        return convertToResponse(entity);
    }

    @Transactional(readOnly = true)
    @PostFilter("hasPermission(filterObject, 'READ')") // Фильтрация после выполнения метода
    public List<FormSkimItResponse> getAll() {
        return formSkimItRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private FormSkimItResponse convertToResponse(FormSkimIt entity) {
        FormSkimItResponse response = new FormSkimItResponse();
        response.setId(entity.getId());
        response.setDictRate(entity.getDictRate());
        response.setOrgUnitDictionary(entity.getOrgUnitDictionary());
        return response;
    }
}
