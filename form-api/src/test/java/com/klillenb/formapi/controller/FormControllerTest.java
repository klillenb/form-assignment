package com.klillenb.formapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.klillenb.formapi.dto.FormDto;
import com.klillenb.formapi.dto.SectorDto;
import com.klillenb.formapi.service.FormService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FormController.class)
@AutoConfigureMockMvc(addFilters = false)
public class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FormService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String FORM_URI = "/api/v1/forms";
    private static final String SECTORS_URI = "/api/v1/forms/sectors";

    private static final String FIRST_LEVEL = "Main sector";
    private static final String SECOND_LEVEL = "Sub sector";
    private static final String RANDOM_NAME = "Random";

    @Test
    void getSectors_returnsOk() throws Exception {
        var dto = new SectorDto()
                .setId(1L)
                .setName(FIRST_LEVEL);

        when(service.findAllSectors()).thenReturn(List.of(dto));

        mockMvc.perform(get(SECTORS_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(FIRST_LEVEL));
    }

    @Test
    void create_returnsCreated() throws Exception {
        var formDto = new FormDto()
                .setName(RANDOM_NAME)
                .setSectors(List.of(1L))
                .setHasAgreed(true);

        when(service.save(any(FormDto.class))).thenReturn(10L);

        mockMvc.perform(post(FORM_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/forms/10"));
    }

    @Test
    void create_returnsBadRequest_whenInvalid() throws Exception {
        var invalidForm = new FormDto()
                .setName("")
                .setSectors(List.of())
                .setHasAgreed(false);

        mockMvc.perform(post(FORM_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidForm)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getForm_returnsOk_whenFormExists() throws Exception {
        var formDto = new FormDto()
                .setId(1L)
                .setName(RANDOM_NAME)
                .setSectors(List.of(1L))
                .setHasAgreed(true);

        when(service.getForm()).thenReturn(Optional.of(formDto));

        mockMvc.perform(get(FORM_URI))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(RANDOM_NAME));
    }

    @Test
    void getForm_returnsOk_whenNoForm() throws Exception {
        when(service.getForm()).thenReturn(Optional.empty());

        mockMvc.perform(get(FORM_URI))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void update_returnsNoContent_whenUpdateSucceeds() throws Exception {
        var formDto = new FormDto()
                .setName(RANDOM_NAME)
                .setSectors(List.of(1L))
                .setHasAgreed(true);

        when(service.update(eq(1L), any(FormDto.class)))
                .thenReturn(true);

        mockMvc.perform(put(FORM_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void update_returnsNotFound_whenFormDoesNotExist() throws Exception {
        var formDto = new FormDto()
                .setName(RANDOM_NAME)
                .setSectors(List.of(1L))
                .setHasAgreed(true);

        when(service.update(eq(99L), any(FormDto.class)))
                .thenReturn(false);

        mockMvc.perform(put(FORM_URI + "/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(formDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_returnsBadRequest_whenValidationFails() throws Exception {
        var invalid = new FormDto()
                .setName("")
                .setSectors(List.of())
                .setHasAgreed(false);

        mockMvc.perform(put(FORM_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
