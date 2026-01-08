package com.klillenb.formapi.controller;

import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/forms")
public class FormController {

    private final FormService formService;

    @GetMapping("/sectors")
    public ResponseEntity<List<SectorDto>> getSectors() {
        List<SectorDto> sectors = formService.findAllSectors();

        return sectors.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(sectors);
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody FormDto form) {
        var id = formService.save(form);

        return ResponseEntity.created(URI.create("/forms/" + id)).build();
    }

    @GetMapping
    public ResponseEntity<FormDto> getForm() {
        return formService.getForm()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody FormDto form) {
        formService.update(id, form);

        return ResponseEntity.noContent().build();
    }
}
