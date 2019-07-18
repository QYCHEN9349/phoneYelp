package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Appearance {
    @Field(value = "造型设计")
    private String design;
    @Field(value = "手机尺寸")
    private String size;
    @Field(value = "手机重量")
    private String weight;
    @Field(value = "机身材质")
    private String material;
    @Field(value = "操作类型")
    private String operationMode;
}
