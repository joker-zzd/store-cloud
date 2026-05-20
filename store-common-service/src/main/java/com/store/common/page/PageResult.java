package com.store.common.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Schema(description = "分页返回结果")
public class PageResult<T> {
    @Schema(description = "当前页码")
    private long pageNum;

    @Schema(description = "每页数量")
    private long pageSize;

    @Schema(description = "总页数")
    private long totalPage;

    @Schema(description = "总记录数")
    private long total;

    @Schema(description = "列表数据")
    private List<T> list;

    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setTotal(page.getTotal());
        result.setTotalPage(page.getPages());
        result.setList(page.getRecords() == null ? Collections.emptyList() : page.getRecords());
        return result;
    }
}
