package com.store.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.store.domain.Menu;
import com.store.dto.MenuSaveRequest;
import com.store.vo.MenuTreeVO;
import com.store.vo.MenuVO;

import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 菜单列表
     */
    List<MenuVO> listMenus(String name, String menuType);

    /**
     * 菜单树
     */
    List<MenuTreeVO> listMenuTree();

    /**
     * 菜单详情
     */
    MenuVO getMenuById(Long id);

    /**
     * 新增菜单
     */
    Long createMenu(MenuSaveRequest request);

    /**
     * 修改菜单
     */
    void updateMenu(Long id, MenuSaveRequest request);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
}
