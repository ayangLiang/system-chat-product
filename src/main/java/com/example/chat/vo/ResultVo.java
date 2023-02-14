package com.example.chat.vo;

import com.example.chat.enmus.ErrorConstantEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ResultVo<T> implements Serializable {
    private static final long serialVersionUID = -1L;
    // 响应业务状态
    private Integer code;

    // 响应消息
    private String message;

    // 响应中的数据
    private T data;

    private Boolean success;

    public boolean isSuccess() {
        if (null == success) {
            success = Objects.equals(code, ErrorConstantEnum.SUCCESS.getErrCode());
        }
        return success;
    }

    public static ResultVo build(ErrorConstantEnum errorConstantEnum) {
        ResultVo resultVo = new ResultVo(errorConstantEnum.getErrCode(), errorConstantEnum.getErrMsg());
        if (ErrorConstantEnum.SUCCESS.getErrCode().equals(errorConstantEnum.getErrCode())) {
            resultVo.setSuccess(true);
        } else {
            resultVo.setSuccess(false);
        }
        return resultVo;
    }


    public static ResultVo buildFailse(String message) {

        ResultVo resultVo = new ResultVo(
            ErrorConstantEnum.FAILURE.getErrCode(), StringUtils.isEmpty(message) ? ErrorConstantEnum.FAILURE.getErrMsg() : message
        );
        resultVo.setSuccess(false);
        return resultVo;
    }

    public static ResultVo buildFailse() {

        ResultVo resultVo = new ResultVo(ErrorConstantEnum.FAILURE.getErrCode(), ErrorConstantEnum.FAILURE.getErrMsg());
        resultVo.setSuccess(false);
        return resultVo;
    }

    public ResultVo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultVo buildSuccess(Supplier supplier) {
        ResultVo result = ResultVo.build(ErrorConstantEnum.SUCCESS);
        result.setData(supplier.get());
        result.setSuccess(true);
        return result;
    }

    public static <T> ResultVo<T> buildSuccess(T data) {
        ResultVo result = ResultVo.build(ErrorConstantEnum.SUCCESS);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static ResultVo buildSuccess() {
        return ResultVo.build(ErrorConstantEnum.SUCCESS);
    }

    public static ResultVo buildSuccess(String message) {
        ResultVo result = ResultVo.build(ErrorConstantEnum.SUCCESS);
        result.setMessage(message);
        result.setSuccess(true);
        return result;
    }

    public boolean success() {
        return isSuccess();
    }

    public boolean notSuccess() {
        return !success();
    }
}
