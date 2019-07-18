package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Camera {
    @Field(value = "摄像头总数")
    private String num;
    @Field(value = "后置摄像头")
    private String back;
    @Field(value = "前置摄像头")
    private String front;
    @Field(value = "传感器类型")
    private String sensorType;
    @Field(value = "闪光灯")
    private String flashBulb;
    @Field(value = "光圈")
    private String aperture;
    @Field(value = "视频拍摄")
    private String videoFunc;
    @Field(value = "拍照功能")
    private String photoFunc;
}
