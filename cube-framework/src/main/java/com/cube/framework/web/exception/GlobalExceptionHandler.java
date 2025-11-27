package com.cube.framework.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.cube.common.constant.HttpStatus;
import com.cube.common.core.domain.AjaxResult;
import com.cube.common.core.text.Convert;
import com.cube.common.exception.DemoModeException;
import com.cube.common.exception.ServiceException;
import com.cube.common.utils.StringUtils;
import com.cube.common.utils.html.EscapeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * openAI相关的错误处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());
        error.put("type", "invalid_request_error");
        error.put("param", "model");
        error.put("code", "model_not_found");
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        return new ResponseEntity<>(response, org.springframework.http.HttpStatus.BAD_REQUEST);
    }

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public AjaxResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return AjaxResult.error(HttpStatus.FORBIDDEN, "没有权限，请联系管理员授权");
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
            HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public AjaxResult handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return StringUtils.isNotNull(code) ? AjaxResult.error(code, e.getMessage()) : AjaxResult.error(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public AjaxResult handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public AjaxResult handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value))
        {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return AjaxResult.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), e.getRequiredType().getName(), value));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public AjaxResult handleRuntimeException(RuntimeException e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        
        // 判断是否为 Redis 超时异常
        if (e.getMessage() != null && e.getMessage().contains("Redis command timed out")) {
            log.warn("请求地址'{}',Redis连接超时", requestURI);
            return AjaxResult.error(HttpStatus.ERROR, "服务暂时繁忙，请稍后重试");
        }
        
        // 判断是否为 Redis 连接异常
        if (e.getMessage() != null && (e.getMessage().contains("Unable to connect to Redis") 
            || e.getMessage().contains("Connection refused"))) {
            log.warn("请求地址'{}',Redis连接失败", requestURI);
            return AjaxResult.error(HttpStatus.ERROR, "服务暂时不可用，请稍后重试");
        }
        
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e, HttpServletRequest request)
    {
        String requestURI = request.getRequestURI();
        
        // 简化常见的无关紧要异常日志
        if (isMinorException(e)) {
            log.warn("🔧 [简化日志] {} - {} | 客户端连接问题，已自动处理", requestURI, e.getClass().getSimpleName());
            
            // 对于连接已断开的异常，不尝试返回响应，避免二次异常
            if (isConnectionBrokenException(e)) {
                return null; // 返回null，让Spring知道不需要写入响应
            }
        } else {
            log.error("请求地址'{}',发生系统异常.", requestURI, e);
        }
        
        return AjaxResult.error(e.getMessage());
    }
    
    /**
     * 判断是否为无关紧要的异常
     */
    private boolean isMinorException(Exception e) {
        String message = e.getMessage();
        String className = e.getClass().getSimpleName();
        
        // 连接重置异常
        if (className.contains("AsyncRequestNotUsableException") || 
            (message != null && message.contains("Connection reset by peer"))) {
            return true;
        }
        
        // HTTP消息转换异常
        if (className.contains("HttpMessageNotWritableException")) {
            return true;
        }
        
        // 客户端中断异常
        if (className.contains("ClientAbortException")) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 判断是否为连接已断开的异常
     */
    private boolean isConnectionBrokenException(Exception e) {
        String message = e.getMessage();
        String className = e.getClass().getSimpleName();
        
        // 连接重置异常 - 客户端已断开连接
        if (className.contains("AsyncRequestNotUsableException") && 
            message != null && message.contains("Connection reset by peer")) {
            return true;
        }
        
        // 客户端中断异常
        if (className.contains("ClientAbortException")) {
            return true;
        }
        
        return false;
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
    {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return AjaxResult.error(message);
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException.class)
    public AjaxResult handleDemoModeException(DemoModeException e)
    {
        return AjaxResult.error("演示模式，不允许操作");
    }
}
