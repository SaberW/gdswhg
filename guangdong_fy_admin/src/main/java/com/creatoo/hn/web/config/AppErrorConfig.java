package com.creatoo.hn.web.config;

import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.bean.ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常配置
 * Created by wangxl on 2017/7/14.
 */
@ControllerAdvice
@ResponseBody
public class AppErrorConfig {
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 系统异常处理，比如：404,500
     * @param req
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler
    public Object defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView view = new ModelAndView("error");
        String uri = req.getRequestURI();
        if(uri != null && uri.startsWith("/api")){
            ApiResultBean rb = new ApiResultBean();
            rb.setCode(901);
            String message = e.getMessage();
            if(e instanceof MethodArgumentNotValidException){
                MethodArgumentNotValidException manve = (MethodArgumentNotValidException)e;
                List<ObjectError> errors = manve.getBindingResult().getAllErrors();
                for(ObjectError oe : errors){
                    String name = oe.getObjectName();
                    if(oe instanceof FieldError){
                        name = ((FieldError) oe).getField();
                    }
                    String msg = oe.getDefaultMessage();
                    message = name + msg;
                    break;
                }
            }else if(e instanceof MissingServletRequestParameterException){
                MissingServletRequestParameterException te = (MissingServletRequestParameterException)e;
                message = te.getMessage();
            }
            rb.setMsg(message);
            return rb;
        }else{
            if (req.getHeader("x-requested-with") != null && req.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){ //如果是ajax请求响应头会有x-requested-with
                ResponseBean rb = new ResponseBean();
                rb.setSuccess(ResponseBean.FAIL);
                String message = e.getMessage();
                if(e instanceof MethodArgumentNotValidException){
                    MethodArgumentNotValidException manve = (MethodArgumentNotValidException)e;
                    List<ObjectError> errors = manve.getBindingResult().getAllErrors();
                    for(ObjectError oe : errors){
                        String name = oe.getObjectName();
                        if(oe instanceof FieldError){
                            name = ((FieldError) oe).getField();
                        }
                        String msg = oe.getDefaultMessage();
                        message = name + msg;
                        break;
                    }
                }
                rb.setErrormsg(message);
                return rb;
            }else{
                view.addObject("exception", e);
            }
        }
        return view;
    }
}
