package com.store.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.store.common.exception.BusinessException;
import com.store.domain.Menu;
import com.store.domain.RoleMenu;
import com.store.dto.MenuSaveRequest;
import com.store.mapper.MenuMapper;
import com.store.service.MenuService;
import com.store.service.RoleMenuService;
import com.store.vo.MenuTreeVO;
import com.store.vo.MenuVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private static final long ROOT_PARENT_ID = 0L;

    private final RoleMenuService roleMenuService;

    public MenuServiceImpl(RoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    @Override
    public List<MenuVO> listMenus(String name, String menuType) {
        try {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<Menu>()
                    .like(StringUtils.hasText(name), Menu::getName, name == null ? null : name.trim())
                    .eq(StringUtils.hasText(menuType), Menu::getMenuType, menuType == null ? null : menuType.trim())
                    .orderByAsc(Menu::getParentId)
                    .orderByAsc(Menu::getId);
            return this.list(queryWrapper).stream()
                    .map(this::toMenuVO)
                    .toList();
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Failed to query menu list. Please check datasource and sys_menu table.", exception);
        }
    }

    @Override
    public List<MenuTreeVO> listMenuTree() {
        try {
            List<Menu> menus = this.list(new LambdaQueryWrapper<Menu>()
                    .orderByAsc(Menu::getParentId)
                    .orderByAsc(Menu::getId));
            return buildTree(menus);
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Failed to query menu tree. Please check datasource and sys_menu table.", exception);
        }
    }

    @Override
    public MenuVO getMenuById(Long id) {
        try {
            return toMenuVO(getRequiredMenu(id));
        } catch (BusinessException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BusinessException("Failed to query menu detail. Please check datasource and sys_menu table.", exception);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createMenu(MenuSaveRequest request) {
        validateParent(request.getParentId(), null);
        Menu menu = new Menu();
        fillMenu(menu, request);
        Date now = new Date();
        menu.setCreateTime(now);
        menu.setUpdateTime(now);
        if (!this.save(menu)) {
            throw new BusinessException("Create menu failed");
        }
        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(Long id, MenuSaveRequest request) {
        Menu existingMenu = getRequiredMenu(id);
        validateParent(request.getParentId(), id);
        fillMenu(existingMenu, request);
        existingMenu.setUpdateTime(new Date());
        if (!this.updateById(existingMenu)) {
            throw new BusinessException("Update menu failed");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(Long id) {
        Menu menu = getRequiredMenu(id);

        long childCount = this.count(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, menu.getId()));
        if (childCount > 0) {
            throw new BusinessException("Child menus exist, delete is not allowed");
        }

        long roleMenuCount = roleMenuService.count(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menu.getId()));
        if (roleMenuCount > 0) {
            throw new BusinessException("Menu is assigned to roles, delete is not allowed");
        }

        if (!this.removeById(id)) {
            throw new BusinessException("Delete menu failed");
        }
    }

    private void validateParent(Long parentId, Long currentId) {
        if (parentId == null) {
            throw new BusinessException("Parent menu id must not be null");
        }
        if (Objects.equals(parentId, currentId)) {
            throw new BusinessException("Parent menu cannot be current menu");
        }
        if (Objects.equals(parentId, ROOT_PARENT_ID)) {
            return;
        }

        Menu parentMenu = this.getById(parentId);
        if (parentMenu == null) {
            throw new BusinessException("Parent menu does not exist");
        }
        if ("F".equals(parentMenu.getMenuType())) {
            throw new BusinessException("Button menu cannot be used as parent menu");
        }
    }

    private Menu getRequiredMenu(Long id) {
        if (id == null) {
            throw new BusinessException("Menu id must not be null");
        }
        Menu menu = this.getById(id);
        if (menu == null) {
            throw new BusinessException("Menu does not exist");
        }
        return menu;
    }

    private void fillMenu(Menu menu, MenuSaveRequest request) {
        menu.setName(request.getName().trim());
        menu.setParentId(request.getParentId());
        menu.setPath(trimToNull(request.getPath()));
        menu.setComponent(trimToNull(request.getComponent()));
        menu.setPerms(trimToNull(request.getPerms()));
        menu.setMenuType(request.getMenuType().trim());
    }

    private List<MenuTreeVO> buildTree(List<Menu> menus) {
        Map<Long, MenuTreeVO> nodeMap = new LinkedHashMap<>();
        List<MenuTreeVO> roots = new ArrayList<>();
        for (Menu menu : menus) {
            MenuTreeVO node = toMenuTreeVO(menu);
            nodeMap.put(node.getId(), node);
        }

        for (MenuTreeVO node : nodeMap.values()) {
            if (Objects.equals(node.getParentId(), ROOT_PARENT_ID) || !nodeMap.containsKey(node.getParentId())) {
                roots.add(node);
            } else {
                nodeMap.get(node.getParentId()).getChildren().add(node);
            }
        }
        sortTree(roots);
        return roots;
    }

    private void sortTree(List<MenuTreeVO> nodes) {
        nodes.sort(Comparator.comparing(MenuTreeVO::getId));
        for (MenuTreeVO node : nodes) {
            if (!node.getChildren().isEmpty()) {
                sortTree(node.getChildren());
            }
        }
    }

    private MenuVO toMenuVO(Menu menu) {
        MenuVO menuVO = new MenuVO();
        BeanUtils.copyProperties(menu, menuVO);
        return menuVO;
    }

    private MenuTreeVO toMenuTreeVO(Menu menu) {
        MenuTreeVO menuTreeVO = new MenuTreeVO();
        BeanUtils.copyProperties(menu, menuTreeVO);
        return menuTreeVO;
    }

    private String trimToNull(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}