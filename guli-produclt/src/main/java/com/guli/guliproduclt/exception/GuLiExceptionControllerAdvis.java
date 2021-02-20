package com.guli.guliproduclt.exception;

import com.guli.common.exception.BizCodeEnume;
import com.guli.common.utils.R;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.guli.guliproduclt.controller")
public class GuLiExceptionControllerAdvis {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVavildExcepion(MethodArgumentNotValidException e) {
        log.error("数据检验异常" + e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errormap = new HashMap();
        bindingResult.getFieldErrors().forEach((finalize)->{

            errormap.put(finalize.getField(),finalize.getDefaultMessage());
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",errormap);
}
    @ExceptionHandler(value = Throwable.class)
    public R handerException(Exception e){
        System.out.println("异常日志："+e.getMessage());
        return R.error(BizCodeEnume.UNKNOW_EXCETION.getCode(),BizCodeEnume.UNKNOW_EXCETION.getMsg());
    }

}
