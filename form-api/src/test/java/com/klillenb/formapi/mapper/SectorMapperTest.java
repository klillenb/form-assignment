package com.klillenb.formapi.mapper;

import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.model.Sector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SectorMapperTest {

    @InjectMocks
    private SectorMapper target;

    private static final String FIRST_LEVEL = "Main sector";
    private static final String SECOND_LEVEL = "Sub sector";
    private static final String THIRD_LEVEL = "Sub-sub sector";

    @Test
    void map_mapsSectorWithoutChildren() {
        Sector sector = new Sector();
        sector.setId(1L);
        sector.setName(FIRST_LEVEL);
        sector.setChildren(List.of());

        SectorDto dto = target.map(sector);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo(FIRST_LEVEL);
        assertThat(dto.getChildren()).isEmpty();
    }

    @Test
    void map_mapsSectorWithChildrenRecursively() {
        var child = new Sector()
                .setId(2L)
                .setName(SECOND_LEVEL)
                .setChildren(List.of());

        var parent = new Sector()
                .setId(1L)
                .setName(FIRST_LEVEL)
                .setChildren(List.of(child));

        SectorDto dto = target.map(parent);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo(FIRST_LEVEL);
        assertThat(dto.getChildren()).hasSize(1);

        SectorDto childDto = dto.getChildren().getFirst();
        assertThat(childDto.getId()).isEqualTo(2L);
        assertThat(childDto.getName()).isEqualTo(SECOND_LEVEL);
        assertThat(childDto.getChildren()).isEmpty();
    }

    @Test
    void map_mapsDeepHierarchyCorrectly() {
        var grandChild = new Sector()
                .setId(3L)
                .setName(THIRD_LEVEL)
                .setChildren(List.of());

        var child = new Sector()
                .setId(2L)
                .setName(SECOND_LEVEL)
                .setChildren(List.of(grandChild));

        var parent = new Sector()
                .setId(1L)
                .setName(FIRST_LEVEL)
                .setChildren(List.of(child));

        SectorDto dto = target.map(parent);

        assertThat(dto.getChildren()).hasSize(1);
        assertThat(dto.getChildren().getFirst().getChildren()).hasSize(1);
        assertThat(dto.getChildren().getFirst().getChildren().getFirst().getName()).isEqualTo(THIRD_LEVEL);
    }
}
