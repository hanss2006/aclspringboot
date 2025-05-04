package com.ocrv.skimrv.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocrv.skimrv.backend.dto.InputOrgUnitDictionaryDto;
import com.ocrv.skimrv.backend.dto.InputOrgUnitDictionaryMapper;
import com.ocrv.skimrv.backend.dto.OrgUnitDictionaryMapper;
import com.ocrv.skimrv.backend.dictionaries.entities.simple.OrgUnitDictionary;
import com.ocrv.skimrv.backend.dto.OrgUnitDictionaryDto;
import com.ocrv.skimrv.backend.repository.InputOrgUnitDictionaryRepository;
import com.ocrv.skimrv.backend.repository.OrgUnitDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrgUnitDictionaryService {

    private final OrgUnitDictionaryMapper orgUnitDictionaryMapper;

    private final OrgUnitDictionaryRepository orgUnitDictionaryRepository;

    private final InputOrgUnitDictionaryRepository inputOrgUnitDictionaryRepository;

    private final ObjectMapper objectMapper;

    public Page<OrgUnitDictionaryDto> getAll(Pageable pageable) {
        List<OrgUnitDictionary> orgUnitDictionaries = orgUnitDictionaryRepository.findAll();
        Page<OrgUnitDictionary> orgUnitDictionariesPage = new PageImpl<>(orgUnitDictionaries, pageable, orgUnitDictionaries.size());
        return orgUnitDictionariesPage.map(orgUnitDictionaryMapper::toOrgUnitDictionaryDto);
    }

    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<InputOrgUnitDictionaryDto> getAllInput() {
        List<OrgUnitDictionary> orgUnitDictionaries = inputOrgUnitDictionaryRepository.findAll();
        InputOrgUnitDictionaryMapper mapper = Mappers.getMapper(InputOrgUnitDictionaryMapper.class);
        List<InputOrgUnitDictionaryDto> dtos = orgUnitDictionaries.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return dtos;
    }

    public OrgUnitDictionaryDto getOne(Integer id) {
        Optional<OrgUnitDictionary> orgUnitDictionaryOptional = orgUnitDictionaryRepository.findById(id);
        return orgUnitDictionaryMapper.toOrgUnitDictionaryDto(orgUnitDictionaryOptional.orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id))));
    }

    public List<OrgUnitDictionaryDto> getMany(List<Integer> ids) {
        List<OrgUnitDictionary> orgUnitDictionaries = orgUnitDictionaryRepository.findAllById(ids);
        return orgUnitDictionaries.stream()
                .map(orgUnitDictionaryMapper::toOrgUnitDictionaryDto)
                .toList();
    }

    public OrgUnitDictionaryDto create(OrgUnitDictionaryDto dto) {
        OrgUnitDictionary orgUnitDictionary = orgUnitDictionaryMapper.toEntity(dto);
        OrgUnitDictionary resultOrgUnitDictionary = orgUnitDictionaryRepository.save(orgUnitDictionary);
        return orgUnitDictionaryMapper.toOrgUnitDictionaryDto(resultOrgUnitDictionary);
    }

    public OrgUnitDictionaryDto patch(Integer id, JsonNode patchNode) throws IOException {
        OrgUnitDictionary orgUnitDictionary = orgUnitDictionaryRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));

        OrgUnitDictionaryDto orgUnitDictionaryDto = orgUnitDictionaryMapper.toOrgUnitDictionaryDto(orgUnitDictionary);
        objectMapper.readerForUpdating(orgUnitDictionaryDto).readValue(patchNode);
        orgUnitDictionaryMapper.updateWithNull(orgUnitDictionaryDto, orgUnitDictionary);

        OrgUnitDictionary resultOrgUnitDictionary = orgUnitDictionaryRepository.save(orgUnitDictionary);
        return orgUnitDictionaryMapper.toOrgUnitDictionaryDto(resultOrgUnitDictionary);
    }

    public List<Integer> patchMany(List<Integer> ids, JsonNode patchNode) throws IOException {
        Collection<OrgUnitDictionary> orgUnitDictionaries = orgUnitDictionaryRepository.findAllById(ids);

        for (OrgUnitDictionary orgUnitDictionary : orgUnitDictionaries) {
            OrgUnitDictionaryDto orgUnitDictionaryDto = orgUnitDictionaryMapper.toOrgUnitDictionaryDto(orgUnitDictionary);
            objectMapper.readerForUpdating(orgUnitDictionaryDto).readValue(patchNode);
            orgUnitDictionaryMapper.updateWithNull(orgUnitDictionaryDto, orgUnitDictionary);
        }

        List<OrgUnitDictionary> resultOrgUnitDictionaries = orgUnitDictionaryRepository.saveAll(orgUnitDictionaries);
        return resultOrgUnitDictionaries.stream()
                .map(OrgUnitDictionary::getId)
                .toList();
    }

    public OrgUnitDictionaryDto delete(Integer id) {
        OrgUnitDictionary orgUnitDictionary = orgUnitDictionaryRepository.findById(id).orElse(null);
        if (orgUnitDictionary != null) {
            orgUnitDictionaryRepository.delete(orgUnitDictionary);
        }
        return orgUnitDictionaryMapper.toOrgUnitDictionaryDto(orgUnitDictionary);
    }

    public void deleteMany(List<Integer> ids) {
        orgUnitDictionaryRepository.deleteAllById(ids);
    }
}
