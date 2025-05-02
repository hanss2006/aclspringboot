package com.ocrv.skimrv.backend.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.ocrv.skimrv.backend.dto.OrgUnitDictionaryDto;
import com.ocrv.skimrv.backend.service.OrgUnitDictionaryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/form-skim-it/orgUnitDictionaries")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Организации", description = "Организации")
public class OrgUnitDictionaryController {

    private final OrgUnitDictionaryService orgUnitDictionaryService;

    @GetMapping
    public Page<OrgUnitDictionaryDto> getAll(Pageable pageable) {
        return orgUnitDictionaryService.getAll(pageable);
    }

    @GetMapping("/input")
    public Page<OrgUnitDictionaryDto> getAllInput(Pageable pageable) {
        return orgUnitDictionaryService.getAllInput(pageable);
    }


    @GetMapping("/{id}")
    public OrgUnitDictionaryDto getOne(@PathVariable Integer id) {
        return orgUnitDictionaryService.getOne(id);
    }

    @GetMapping("/by-ids")
    public List<OrgUnitDictionaryDto> getMany(@RequestParam List<Integer> ids) {
        return orgUnitDictionaryService.getMany(ids);
    }

    @PostMapping
    public OrgUnitDictionaryDto create(@RequestBody OrgUnitDictionaryDto dto) {
        return orgUnitDictionaryService.create(dto);
    }

    @PatchMapping("/{id}")
    public OrgUnitDictionaryDto patch(@PathVariable Integer id, @RequestBody JsonNode patchNode) throws IOException {
        return orgUnitDictionaryService.patch(id, patchNode);
    }

    @PatchMapping
    public List<Integer> patchMany(@RequestParam List<Integer> ids, @RequestBody JsonNode patchNode) throws IOException {
        return orgUnitDictionaryService.patchMany(ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public OrgUnitDictionaryDto delete(@PathVariable Integer id) {
        return orgUnitDictionaryService.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestParam List<Integer> ids) {
        orgUnitDictionaryService.deleteMany(ids);
    }
}
