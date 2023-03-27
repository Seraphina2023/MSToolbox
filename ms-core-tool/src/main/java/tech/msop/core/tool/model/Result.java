package tech.msop.core.tool.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import tech.msop.core.tool.utils.ObjectUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * 统一返回工具类
 *
 * @author ruozhuliufeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    /**
     * 承载数据
     */
    private T data;
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 构造函数 Result
     *
     * @param data    承载数据
     * @param code    业务代码
     * @param message 消息
     */
    public Result(T data, int code, String message) {
        this.data = data;
        this.code = code;
        this.msg = message;
        this.success = ResultCode.SUCCESS.code == code;
    }

    /**
     * 构造函数 Result
     *
     * @param resultCode 业务代码
     */
    public Result(IResultCode resultCode) {
        this(null, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 构造函数 Result
     *
     * @param resultCode 业务代码
     * @param message    消息
     */
    public Result(IResultCode resultCode, String message) {
        this(null, resultCode.getCode(), message);
    }

    /**
     * 构造函数 Result
     *
     * @param resultCode 业务代码
     * @param data       承载数据
     */
    public Result(IResultCode resultCode, T data) {
        this(data, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 构造函数 Result
     *
     * @param resultCode 业务代码
     * @param data       承载数据
     * @param message    消息
     */
    public Result(IResultCode resultCode, T data, String message) {
        this(data, resultCode.getCode(), message);
    }

    /**
     * 判断返回是否成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isSuccess(@Nullable Result<?> result) {
        return Optional.ofNullable(result)
                .map(x -> ObjectUtil.nullSafeEquals(ResultCode.SUCCESS.code, x.code))
                .orElse(Boolean.FALSE);
    }

    /**
     * 判断返回是否成功
     *
     * @param result Result
     * @return 是否成功
     */
    public static boolean isNotSuccess(@Nullable Result<?> result) {
        return !Result.isSuccess(result);
    }

    /**
     * 判断是否成功
     *
     * @param flag 成功状态
     * @return Result
     */
    public static <T> Result<T> status(boolean flag) {
        return flag ? succeed() : failed();
    }

    /**
     * 成功返回 Result
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(String msg) {
        return of(null, ResultCode.SUCCESS.code, msg);
    }

    /**
     * 成功返回 Result
     *
     * @param model 承载数据
     * @param msg   消息
     * @param <T>   T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, ResultCode.SUCCESS.code, msg);
    }

    /**
     * 成功返回 Result
     *
     * @param model 承载数据
     * @param <T>   T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(T model) {
        return of(model, ResultCode.SUCCESS.code, ResultCode.SUCCESS.message);
    }

    /**
     * 成功返回 Result
     *
     * @param <T> T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed() {
        return succeed("操作成功");
    }

    /**
     * 成功返回 Result
     *
     * @param code 业务编码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(Integer code, String msg) {
        return of(null, code, msg);
    }

    /**
     * 成功返回 Result
     *
     * @param code  业务编码
     * @param model 承载数据
     * @param msg   消息
     * @param <T>   T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(Integer code, T model, String msg) {
        return of(model, code, msg);
    }

    /**
     * 成功返回 Result
     *
     * @param resultCode 业务枚举
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(IResultCode resultCode) {
        return of(null, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 成功返回 Result
     *
     * @param resultCode 业务枚举
     * @param message    消息
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(IResultCode resultCode, String message) {
        return of(null, resultCode.getCode(), message);
    }

    /**
     * 成功返回 Result
     *
     * @param data       承载数据
     * @param resultCode 业务枚举
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(T data, IResultCode resultCode) {
        return of(data, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 成功返回 Result
     *
     * @param data       承载数据
     * @param resultCode 业务枚举
     * @param message    消息
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> succeed(T data, IResultCode resultCode, String message) {
        return of(data, resultCode.getCode(), message);
    }

    /**
     * 成功返回 Result
     *
     * @param data 承载数据
     * @param <T>  T 泛型
     * @return Result
     */
    public static <T> Result<T> data(T data) {
        return succeed(data, ResultCode.SUCCESS);
    }

    /**
     * 数据格式化
     *
     * @param data 承载数据
     * @param code 业务代码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return Result
     */
    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }

    /**
     * 失败返回 Result
     *
     * @param msg 消息
     * @param <T> T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(String msg) {
        return of(null, ResultCode.FAILURE.code, msg);
    }

    /**
     * 失败返回 Result
     *
     * @param model 承载数据
     * @param msg   消息
     * @param <T>   T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(T model, String msg) {
        return of(model, ResultCode.FAILURE.code, msg);
    }

    /**
     * 失败返回 Result
     *
     * @param code  业务代码
     * @param msg   消息
     * @param model 承载数据
     * @param <T>   T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(Integer code, String msg, T model) {
        return of(model, code, msg);
    }

    /**
     * 失败返回 Result
     *
     * @param <T> T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed() {
        return failed("操作失败");
    }

    /**
     * 失败返回 Result
     *
     * @param code 业务编码
     * @param msg  消息
     * @param <T>  T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(Integer code, String msg) {
        return of(null, code, msg);
    }

    /**
     * 失败返回 Result
     *
     * @param resultCode 业务枚举
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(IResultCode resultCode) {
        return of(null, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 失败返回 Result
     *
     * @param resultCode 业务枚举
     * @param message    消息
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(IResultCode resultCode, String message) {
        return of(null, resultCode.getCode(), message);
    }

    /**
     * 失败返回 Result
     *
     * @param data       承载数据
     * @param resultCode 业务枚举
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(T data, IResultCode resultCode) {
        return of(data, resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 失败返回 Result
     *
     * @param data       承载数据
     * @param resultCode 业务枚举
     * @param message    消息
     * @param <T>        T 泛型标记
     * @return Result
     */
    public static <T> Result<T> failed(T data, IResultCode resultCode, String message) {
        return of(data, resultCode.getCode(), message);
    }


}
