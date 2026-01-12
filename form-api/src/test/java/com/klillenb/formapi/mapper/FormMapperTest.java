package com.klillenb.formapi.mapper;

import com.klillenb.formapi.model.Form;
import com.klillenb.formapi.repository.FormSectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormMapperTest {

    @Mock
    private FormSectorRepository formSectorRepository;

    @InjectMocks
    private FormMapper target;

    private static final String TEST_NAME = "Test name";

    @Test
    void map_mapsFormToDto() {
        var form = new Form()
                .setId(1L)
                .setName(TEST_NAME)
                .setHasAgreed(true);

        when(formSectorRepository.findSectorIdsByFormId(1L))
                .thenReturn(List.of(10L, 20L));

        var dto = target.map(form);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo(TEST_NAME);
        assertThat(dto.isHasAgreed()).isTrue();
        assertThat(dto.getSectors()).containsExactly(10L, 20L);

        verify(formSectorRepository).findSectorIdsByFormId(1L);
    }

    @Test
    void map_mapsEmptySectorList() {
        var form = new Form()
                .setId(2L)
                .setName(TEST_NAME)
                .setHasAgreed(true);

        when(formSectorRepository.findSectorIdsByFormId(2L))
                .thenReturn(List.of());

        var dto = target.map(form);

        assertThat(dto.getSectors()).isEmpty();
    }
}
