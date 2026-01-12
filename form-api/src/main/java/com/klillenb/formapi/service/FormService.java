package com.klillenb.formapi.service;

import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.mapper.FormMapper;
import com.klillenb.formapi.mapper.SectorMapper;
import com.klillenb.formapi.model.Form;
import com.klillenb.formapi.model.FormSector;
import com.klillenb.formapi.repository.FormRepository;
import com.klillenb.formapi.repository.FormSectorRepository;
import com.klillenb.formapi.repository.SectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final SectorRepository sectorRepository;

    private final FormRepository formRepository;

    private final FormSectorRepository formSectorRepository;

    private final SectorMapper sectorMapper;

    private final FormMapper formMapper;

    public List<SectorDto> findAllSectors() {
        return sectorRepository
                .findByParentIsNull()
                .stream()
                .map(sectorMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<FormDto> getForm() {
        return formRepository.findFirstByOrderByIdAsc()
                .map(formMapper::map);
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
    public boolean update(Long id, FormDto dto) {
        return formRepository.findById(id)
                .map(form -> {
                    form.setName(dto.getName())
                            .setHasAgreed(dto.isHasAgreed());

                    formSectorRepository.deleteByFormId(id);

                    var selections = dto.getSectors().stream()
                            .map(sectorId -> new FormSector()
                                    .setForm(form)
                                    .setSector(sectorRepository.getReferenceById(sectorId)))
                            .toList();

                    formSectorRepository.saveAll(selections);
                    return true;
                })
                .orElse(false);
    }
}
