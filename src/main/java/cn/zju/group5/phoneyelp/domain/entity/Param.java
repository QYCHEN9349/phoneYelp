package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Param {
    @Field(value = "基本参数")
    private BaseParam baseParam;
    @Field(value = "屏幕")
    private Screen screen;
    @Field(value = "硬件")
    private Hardware hardware;
    @Field(value = "网络与连接")
    private Network network;
    @Field(value = "摄像头")
    private Camera camera;
    @Field(value = "外观")
    private Appearance appearance;
    @Field(value = "功能与服务")
    private Function function;
    @Field(value = "保修信息")
    private WarrantyInfo warrantyInfo;
}
