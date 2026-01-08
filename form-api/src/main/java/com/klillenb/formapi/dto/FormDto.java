package com.klillenb.formapi.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class FormDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotEmpty(message = "At least one sector must be selected")
    private List<@NotNull Long> sectors;

    @AssertTrue(message = "User must agree to terms")
    private boolean hasAgreed;
}
