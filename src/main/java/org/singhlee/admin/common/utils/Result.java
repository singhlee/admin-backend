package org.singhlee.admin.common.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.singhlee.admin.common.enums.ReturnCode;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("返回对象")
public class Result<T> implements Serializable {
    @ApiModelProperty(value = "返回数据")
    private T datas;
    @ApiModelProperty(value = "返回码")
    private Integer respCode;
    @ApiModelProperty(value = "返回信息")
    private String respMsg;

    public static <T> Result<T> succeed(String msg) {
        return succeed(null, ReturnCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model) {
        return succeed(model, ReturnCode.SUCCESS.getCode(), "");
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeed(model, ReturnCode.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T datas, Integer code, String msg) {
        return new Result<T>(datas, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failed(null, ReturnCode.SYSTEM_FAILURE.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return failed(model, ReturnCode.SYSTEM_FAILURE.getCode(), msg);
    }

    public static <T> Result<T> failed(T datas, Integer code, String msg) {
        return new Result<T>(datas, code, msg);
    }
    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }



}
