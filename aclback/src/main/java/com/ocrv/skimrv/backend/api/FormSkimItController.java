package com.ocrv.skimrv.backend.api;

import com.ocrv.skimrv.backend.dto.FormSkimItDto;
import com.ocrv.skimrv.backend.dto.FormSkimItResponse;
import com.ocrv.skimrv.backend.service.FormSkimItService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/form-skim-it")
public class FormSkimItController {

    private final FormSkimItService formSkimItService;

    public FormSkimItController(FormSkimItService formSkimItService) {
        this.formSkimItService = formSkimItService;
    }

    @PostMapping
    public ResponseEntity<FormSkimItResponse> create(@Valid @RequestBody FormSkimItDto dto) {
        FormSkimItResponse response = formSkimItService.create(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormSkimItResponse> update(@PathVariable Integer id, @Valid @RequestBody FormSkimItDto dto) {
        FormSkimItResponse response = formSkimItService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormSkimItResponse> getById(@PathVariable Integer id) {
        FormSkimItResponse response = formSkimItService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<FormSkimItResponse>> getAll() {
        List<FormSkimItResponse> responses = formSkimItService.getAll();
        return ResponseEntity.ok(responses);
    }
}
