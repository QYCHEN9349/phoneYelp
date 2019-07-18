package cn.zju.group5.phoneyelp.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CommonException extends RuntimeException {
    private HttpStatus code;
    private String msg;
    public CommonException(HttpStatus code,String msg){
        this.code=code;
        this.msg=msg;
    }
}
