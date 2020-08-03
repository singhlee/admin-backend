package org.singhlee.admin.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
@ApiModel("分页返回数据")
public class PageResult<T> {
    @ApiModelProperty(value = "页码")
    private long pageNo;
    @ApiModelProperty(value = "页面记录数")
    private long pageSize;

    @ApiModelProperty(value = "总记录数")
    private long totalCount;

    @ApiModelProperty(value = "总页数")
    private long totalPage;

    @ApiModelProperty(value = "分页记录")
    private List<T> results;

    public long getPageNo() {
        if (pageNo <= 0) {
            return 1;
        } else {
            return pageNo > totalPage ? totalPage : pageNo;
        }
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize <= 0L ? 10L : pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
        totalPage = totalCount % pageSize == 0L ? totalCount / pageSize
                : totalCount / pageSize + 1L;
    }


    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}
