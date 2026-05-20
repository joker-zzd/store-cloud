package com.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(description = "Menu tree node")
public class MenuTreeVO {

    @Schema(description = "Menu id")
    private Long id;

    @Schema(description = "Menu name")
    private String name;

    @Schema(description = "Parent menu id")
    private Long parentId;

    @Schema(description = "Route path")
    private String path;

    @Schema(description = "Component path")
    private String component;

    @Schema(description = "Permission code")
    private String perms;

    @Schema(description = "Menu type")
    private String menuType;

    @Schema(description = "Child menus")
    private List<MenuTreeVO> children = new ArrayList<>();
}