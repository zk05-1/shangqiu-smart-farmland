package com.sqnu.server.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * <p>
 * 使用 {@code @RestControllerAdvice} 拦截所有控制器抛出的异常，
 * 统一转换为 {@link CommonResult} 格式返回给客户端。
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理运行时异常
     *
     * @param e 运行时异常
     * @return 包含500状态码和异常消息的错误结果
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常", e);
        return CommonResult.error(500, "服务器内部错误：" + e.getMessage());
    }

    /**
     * 处理方法参数校验异常
     *
     * @param e 方法参数校验异常
     * @return 包含400状态码和字段校验错误信息的错误结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<Void> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("参数校验失败", e);
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("参数验证失败");
        return CommonResult.error(400, message);
    }

    /**
     * 处理访问拒绝异常（权限不足）
     *
     * @param e 访问拒绝异常
     * @return 包含403状态码的权限错误结果
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.warn("权限不足", e);
        return CommonResult.error(403, "没有访问权限");
    }

    /**
     * 处理所有未捕获的通用异常（兜底处理）
     *
     * @param e 通用异常
     * @return 包含500状态码和系统异常消息的错误结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return CommonResult.error(500, "系统异常：" + e.getMessage());
    }
}
