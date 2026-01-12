package com.klillenb.formapi.mapper;

import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.model.Form;
import com.klillenb.formapi.repository.FormSectorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormMapper {

    private final FormSectorRepository formSectorRepository;

    public FormDto map(Form form) {
        var sectorIds = formSectorRepository.findSectorIdsByFormId(form.getId());

        return new FormDto()
                .setId(form.getId())
                .setName(form.getName())
                .setHasAgreed(form.isHasAgreed())
                .setSectors(sectorIds);
    }
}
