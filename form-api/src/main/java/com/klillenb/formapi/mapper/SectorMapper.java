package com.klillenb.formapi.mapper;

import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.model.Sector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SectorMapper {

    public SectorDto map(Sector sector) {
        return new SectorDto()
                .setId(sector.getId())
                .setName(sector.getName())
                .setChildren(sector.getChildren()
                        .stream()
                        .map(this::map)
                        .toList()
                );
    }
}
