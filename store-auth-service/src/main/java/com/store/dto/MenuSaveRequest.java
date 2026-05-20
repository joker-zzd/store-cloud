package com.store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Menu save request")
public class MenuSaveRequest {

    @Schema(description = "Menu name", example = "System")
    @NotBlank(message = "Menu name must not be blank")
    private String name;

    @Schema(description = "Parent menu id, root is 0", example = "0")
    @NotNull(message = "Parent menu id must not be null")
    private Long parentId;

    @Schema(description = "Route path", example = "/system")
    private String path;

    @Schema(description = "Component path", example = "system/index")
    private String component;

    @Schema(description = "Permission code", example = "system:menu:list")
    private String perms;

    @Schema(description = "Menu type: M directory, C menu, F button", example = "M")
    @NotBlank(message = "Menu type must not be blank")
    @Pattern(regexp = "M|C|F", message = "Menu type must be one of M, C, F")
    private String menuType;
}