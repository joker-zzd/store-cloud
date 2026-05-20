package com.store.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "Menu detail")
public class MenuVO {

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

    @Schema(description = "Create time")
    private java.util.Date createTime;

    @Schema(description = "Update time")
    private java.util.Date updateTime;
}