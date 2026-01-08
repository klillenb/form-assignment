package com.klillenb.formapi.service;

import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.mapper.SectorMapper;
import com.klillenb.formapi.model.Form;
import com.klillenb.formapi.model.FormSector;
import com.klillenb.formapi.repository.FormRepository;
import com.klillenb.formapi.repository.FormSectorRepository;
import com.klillenb.formapi.repository.SectorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FormService {

    private final SectorRepository sectorRepository;

    private final FormRepository formRepository;

    private final FormSectorRepository formSectorRepository;

    private final SectorMapper mapper;

    public List<SectorDto> findAllSectors() {
        return sectorRepository
                .findByParentIsNull()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<FormDto> getForm() {
        return formRepository.findAll()
                .stream()
                .findFirst()
                .map(form -> {
                    List<Long> sectorIds = formSectorRepository.findSectorIdsByFormId(form.getId());

                    return new FormDto()
                            .setId(form.getId())
                            .setName(form.getName())
                            .setHasAgreed(form.isHasAgreed())
                            .setSectors(sectorIds);
                });
    }

    @Transactional
    public Long save(FormDto dto) {
        var form = new Form()
                .setName(dto.getName())
                .setHasAgreed(dto.isHasAgreed());

        formRepository.save(form);

        List<FormSector> selections = dto
                .getSectors()
                .stream()
                .map(sectorId -> new FormSector()
                        .setForm(form)
                        .setSector(sectorRepository.getReferenceById(sectorId))
                )
                .toList();

        formSectorRepository.saveAll(selections);

        return form.getId();
    }

    @Transactional
    public void update(Long id, FormDto dto) {
        var form = formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));

        form.setName(dto.getName())
                .setHasAgreed(dto.isHasAgreed());

        formSectorRepository.deleteByFormId(id);

        List<FormSector> selections = dto.getSectors()
                .stream()
                .map(sectorId -> new FormSector()
                        .setForm(form)
                        .setSector(sectorRepository.getReferenceById(sectorId))
                )
                .toList();

        formSectorRepository.saveAll(selections);
    }
}
