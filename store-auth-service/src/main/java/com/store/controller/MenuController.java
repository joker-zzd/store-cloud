package com.store.controller;

import com.store.common.resultvo.ResultVO;
import com.store.dto.MenuSaveRequest;
import com.store.service.MenuService;
import com.store.vo.MenuTreeVO;
import com.store.vo.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
@Tag(name = "Menu Management", description = "Menu CRUD APIs")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    @Operation(summary = "Menu list", description = "Query menu list by name or type")
    public ResultVO<List<MenuVO>> list(@RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "menuType", required = false) String menuType) {
        return ResultVO.success(menuService.listMenus(name, menuType));
    }

    @GetMapping("/tree")
    @Operation(summary = "Menu tree", description = "Return tree menu structure")
    public ResultVO<List<MenuTreeVO>> tree() {
        return ResultVO.success(menuService.listMenuTree());
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "Menu detail", description = "Query menu detail by id")
    public ResultVO<MenuVO> detail(@PathVariable("id") Long id) {
        return ResultVO.success(menuService.getMenuById(id));
    }

    @PostMapping("/create")
    @Operation(summary = "Create menu", description = "Create a new menu")
    public ResultVO<Long> create(@Valid @RequestBody MenuSaveRequest request) {
        return ResultVO.success(menuService.createMenu(request));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update menu", description = "Update menu by id")
    public ResultVO<Void> update(@PathVariable("id") Long id,
                                 @Valid @RequestBody MenuSaveRequest request) {
        menuService.updateMenu(id, request);
        return ResultVO.success();
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete menu", description = "Delete menu by id")
    public ResultVO<Void> delete(@PathVariable("id") Long id) {
        menuService.deleteMenu(id);
        return ResultVO.success();
    }
}