package org.corefine.common.web.mvc;

import org.corefine.common.web.constant.CodeConstant;
import org.corefine.common.web.controller.OAuthException;
import org.corefine.common.web.service.ServiceException;
import org.corefine.common.web.vo.MessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object handleException(HttpServletResponse response, Exception e) {
        if (e instanceof OAuthException) {
            logger.info("用户未登录：{}", e.getMessage());
            response.setStatus(CodeConstant.LOGIN_AGAIN);
            return new MessageVo(CodeConstant.LOGIN_AGAIN, e.getMessage());
        } else if (e instanceof ServiceException) {
            ServiceException es = (ServiceException) e;
            logger.info("业务异常：code={},message={}", es.getCode(), es.getMessage());
            response.setStatus(CodeConstant.SERVICE_ERROR);
            return new MessageVo(es.getCode(), es.getMessage());
        } else if (e instanceof BindException) {
            BindException me = (BindException) e;
            StringBuilder sb = new StringBuilder();
            List<ObjectError> errors = me.getBindingResult().getAllErrors();
            if (errors.size() > 1) {
                sb.append("共").append(errors.size()).append("个错误：");
            }
            for (ObjectError error : errors) {
                sb.append(error.getDefaultMessage()).append("，");
            }
            sb.delete(sb.length() - 1, sb.length());
            String s = sb.toString();
            logger.info("参数非法：{}", s);
            response.setStatus(CodeConstant.PARAMETER_ERROR);
            return new MessageVo(CodeConstant.PARAMETER_ERROR, s);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
            StringBuilder sb = new StringBuilder();
            List<ObjectError> errors = me.getBindingResult().getAllErrors();
            if (errors.size() > 1) {
                sb.append("共").append(errors.size()).append("个错误：");
            }
            for (ObjectError error : errors) {
                sb.append(error.getDefaultMessage()).append("，");
            }
            sb.delete(sb.length() - 1, sb.length());
            String s = sb.toString();
            logger.info("参数非法：{}", s);
            response.setStatus(CodeConstant.PARAMETER_ERROR);
            return new MessageVo(CodeConstant.PARAMETER_ERROR, s);
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException es = (MissingServletRequestParameterException) e;
            String s = "参数非法：" + es.getParameterName() + "不能为空";
            logger.info(s);
            response.setStatus(CodeConstant.PARAMETER_ERROR);
            return new MessageVo(CodeConstant.PARAMETER_ERROR, s);
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException violationException = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> error = violationException.getConstraintViolations();
            List<String> list = new ArrayList<>(error.size());
            for (ConstraintViolation<?> cv : error) {
                list.add(cv.getMessage());
            }
            String msg = String.join(",", list);
            logger.warn("参数{}校验异常", msg);
            response.setStatus(CodeConstant.PARAMETER_ERROR);
            return new MessageVo(CodeConstant.PARAMETER_ERROR, msg);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            HttpRequestMethodNotSupportedException ex = (HttpRequestMethodNotSupportedException) e;
            logger.warn("不支持{}请求，支持{}", ex.getMethod(), ex.getSupportedHttpMethods());
            response.setStatus(CodeConstant.METHOD_ERROR);
            return new MessageVo(CodeConstant.METHOD_ERROR, "不支持" + ex.getMethod() + "请求");
        } else if (e instanceof HttpMessageNotReadableException) {
            //HttpMessageNotReadableException ex = (HttpMessageNotReadableException)e;
            logger.warn("请传入body");
            response.setStatus(CodeConstant.PARAMETER_ERROR);
            return new MessageVo(CodeConstant.PARAMETER_ERROR, "请传入body");
        } else {
            logger.error("系统异常", e);
            response.setStatus(CodeConstant.SYSTEM_ERROR);
            return new MessageVo(CodeConstant.SYSTEM_ERROR, "系统异常");
        }
    }
}
