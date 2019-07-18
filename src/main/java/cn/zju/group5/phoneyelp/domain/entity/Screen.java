package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Screen {
    @Field(value = "触摸屏类型")
    private String touchscreenType;
    @Field(value = "主屏尺寸")
    private String size;
    @Field(value = "主屏材质")
    private String material;
    @Field(value = "主屏分辨率")
    private String resolution;
    @Field(value = "窄边框")
    private String narrowBezel;
    @Field(value = "屏幕占比")
    private String screenProportions;
    @Field(value = "其他屏幕参数")
    private String other;
}
