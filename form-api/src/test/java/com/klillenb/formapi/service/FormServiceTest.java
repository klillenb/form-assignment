package com.klillenb.formapi.service;

import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.mapper.FormMapper;
import com.klillenb.formapi.mapper.SectorMapper;
import com.klillenb.formapi.model.Form;
import com.klillenb.formapi.model.Sector;
import com.klillenb.formapi.repository.FormRepository;
import com.klillenb.formapi.repository.FormSectorRepository;
import com.klillenb.formapi.repository.SectorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FormServiceTest {

    @Mock
    private SectorRepository sectorRepository;

    @Mock
    private FormRepository formRepository;

    @Mock
    private FormSectorRepository formSectorRepository;

    @Mock
    private SectorMapper sectorMapper;

    @Mock
    private FormMapper formMapper;

    @InjectMocks
    private FormService target;

    private static final String TEST_NAME = "Test name";

    @Test
    void findAllSectors_returnsMappedSectors() {
        var sector = new Sector()
                .setId(1L)
                .setName(TEST_NAME);

        var dto = new SectorDto()
                .setId(1L)
                .setName(TEST_NAME);

        when(sectorRepository.findByParentIsNull()).thenReturn(List.of(sector));
        when(sectorMapper.map(sector)).thenReturn(dto);

        List<SectorDto> result = target.findAllSectors();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo(TEST_NAME);
        verify(sectorMapper, times(1)).map(sector);
    }

    @Test
    void getForm_returnsMappedFormDto() {
        var form = new Form()
                .setId(1L)
                .setName(TEST_NAME)
                .setHasAgreed(true);

        var dto = new FormDto()
                .setId(1L)
                .setName(TEST_NAME)
                .setHasAgreed(true)
                .setSectors(List.of(1L, 2L));

        when(formRepository.findFirst()).thenReturn(Optional.of(form));
        when(formMapper.map(form)).thenReturn(dto);

        Optional<FormDto> result = target.getForm();

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(TEST_NAME);
        verify(formMapper).map(form);
    }

    @Test
    void getForm_returnsEmpty_whenNoFormExists() {
        when(formRepository.findFirst()).thenReturn(Optional.empty());

        Optional<FormDto> result = target.getForm();

        assertThat(result).isEmpty();
        verifyNoInteractions(formMapper);
    }

    @Test
    void save_persistsFormAndFormSectors() {
        var dto = new FormDto()
                .setName(TEST_NAME)
                .setHasAgreed(true)
                .setSectors(List.of(1L, 2L));

        when(formRepository.save(any(Form.class)))
                .thenAnswer(invocation -> {
                    Form form = invocation.getArgument(0);
                    form.setId(10L);
                    return form;
                });

        when(sectorRepository.getReferenceById(1L)).thenReturn(new Sector().setId(1L));
        when(sectorRepository.getReferenceById(2L)).thenReturn(new Sector().setId(2L));

        Long id = target.save(dto);

        assertThat(id).isEqualTo(10L);
        verify(formRepository).save(any(Form.class));
        verify(formSectorRepository).saveAll(anyList());
    }

    @Test
    void update_returnsOptionalWithValue_whenFormExists() {
        var formId = 5L;

        FormDto dto = new FormDto()
                .setName(TEST_NAME)
                .setHasAgreed(true)
                .setSectors(List.of(3L));

        Form existingForm = new Form()
                .setId(formId)
                .setName("Old Name")
                .setHasAgreed(false);

        when(formRepository.findById(formId)).thenReturn(Optional.of(existingForm));
        when(sectorRepository.getReferenceById(3L)).thenReturn(new Sector().setId(3L));

        var result = target.update(formId, dto);

        assertThat(result).isTrue();
        assertThat(existingForm.getName()).isEqualTo(TEST_NAME);
        assertThat(existingForm.isHasAgreed()).isTrue();

        verify(formSectorRepository, times(1)).deleteByFormId(formId);
        verify(formSectorRepository, times(1)).saveAll(anyList());
    }

    @Test
    void update_returnsEmptyOptional_whenFormDoesNotExist() {
        when(formRepository.findById(99L)).thenReturn(Optional.empty());

        var dto = new FormDto()
                .setName(TEST_NAME)
                .setHasAgreed(true)
                .setSectors(List.of(1L));

        var result = target.update(99L, dto);

        assertThat(result).isFalse();

        verify(formSectorRepository, never()).deleteByFormId(anyLong());
        verify(formSectorRepository, never()).saveAll(anyList());
    }
}
