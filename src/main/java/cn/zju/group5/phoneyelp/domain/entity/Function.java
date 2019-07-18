package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document
public class Function {
    @Field(value = "三防功能")
    private String resistance;
    @Field(value = "感应器功能")
    private String sensor;
    @Field(value = "视频支持")
    private String video;
    @Field(value = "音频支持")
    private String audio;
    @Field(value = "常用功能")
    private String muchUseFunc;
    @Field(value = "商务功能")
    private String business;
    @Field(value = "其他功能参数")
    private String others;
}
