package cn.zju.group5.phoneyelp.domain.vo;

import lombok.Data;

@Data
public class CommonResponse {
    private Integer code;
    private Object data;
    private String msg;

    public CommonResponse(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }
    public CommonResponse(Integer code,String msg,Object data){
        this.code=code;
        this.data=data;
        this.msg=msg;
    }
    public CommonResponse(Integer code,Object data){
        this.code=code;
        this.data=data;
    }
    public static final CommonResponse SUCCESS=new CommonResponse(200,"成功");
}
