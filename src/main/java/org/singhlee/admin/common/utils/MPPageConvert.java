package org.singhlee.admin.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * @program: admin-backend
 * @description: 数据转换
 * @author: singhlee
 * @date: 2020-06-16 11:20
 **/
public class MPPageConvert {

    /**
     * 前台传过来的参数转换为MyBatis Plus的Page
     *
     * @param param
     * @param <T>
     * @return
     */
    public static <T> IPage<T> pageParamConvert(Map<String, Object> param) {
        int currPage = 1;
        int limit = 10;
        if (MapUtils.getInteger(param, "pageNo") != null) {
            currPage = MapUtils.getInteger(param, "pageNo");
        }
        if (MapUtils.getInteger(param, "pageSize") != null) {
            limit = MapUtils.getInteger(param, "pageSize");
        }
        IPage<T> page = new Page<>(currPage, limit);
        return page;
    }

    /**
     * @return java.util.HashMap
     * @Description 将MyBatis Plus 的Page 转换为前台能用的Page
     * @Param [page]
     *@param page */
    public static <T> PageResult<T> pageValueConvert(IPage<T> page) {
        PageResult<T> pageResult = new PageResult();
        pageResult.setPageNo(page.getCurrent());
        pageResult.setPageSize(page.getSize());
        pageResult.setTotalPage(page.getPages());
        pageResult.setTotalCount(page.getTotal());
        pageResult.setResults(page.getRecords());
        return pageResult;
    }

}
