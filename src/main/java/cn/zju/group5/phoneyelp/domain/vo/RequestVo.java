package cn.zju.group5.phoneyelp.domain.vo;

import lombok.Data;


@Data
public class RequestVo {
    private String keyword;
    private Integer type;
    private Integer page;
    private Integer pageSize;

    public RequestVo(String keyword, Integer type, Integer page, Integer pageSize) {
        this.keyword = keyword;
        this.type = type;
        this.page = page;
        this.pageSize = pageSize;
    }
}
