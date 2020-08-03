package org.singhlee.admin.modules.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: admin-backend
 * @description:
 * @author: singhlee
 * @date: 2020-07-09 19:10
 **/
@Data
@ApiModel(value = "用户", description = "用户")
public class SysUserVo {

    @ApiModelProperty(value = "用户Id",required = true)
    private Long sysUserId;

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty(value = "商派用户id",required = true)
    private String shopexUserId;

    @ApiModelProperty(value = "状态",required = true)
    private Integer  status;

    @ApiModelProperty(value = "token",required = true)
    private String token ;

}
