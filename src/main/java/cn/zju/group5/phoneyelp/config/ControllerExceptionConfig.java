package cn.zju.group5.phoneyelp.config;

import cn.zju.group5.phoneyelp.domain.vo.CommonException;
import cn.zju.group5.phoneyelp.domain.vo.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ControllerExceptionConfig {
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleCommonException(CommonException e) {
        return new ResponseEntity<>(new CommonResponse(e.getCode().value(), e.getMsg()), e.getCode());
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleAllExceptions(Throwable t) {
        log.error(t.getMessage(), t);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), t.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
