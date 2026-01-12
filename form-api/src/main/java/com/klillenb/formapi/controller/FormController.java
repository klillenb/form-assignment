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
        return ResponseEntity.ok(formService.findAllSectors());
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody FormDto form) {
        var id = formService.save(form);
        var location = URI.create("/forms/" + id);

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<FormDto> getForm() {
        return formService.getForm()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @Valid @RequestBody FormDto form) {
        return formService.update(id, form)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
