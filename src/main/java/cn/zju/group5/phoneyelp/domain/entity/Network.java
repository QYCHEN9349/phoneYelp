package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Network {
    @Field(value = "4G网络")
    private String fourthGeneration;
    @Field(value = "3G网络")
    private String thirdGeneration;
    @Field(value = "支持频段")
    private String frequencyRange;
    @Field(value = "SIM卡类型")
    private String simCard;
    @Field(value = "WLAN功能")
    private String wLan;
    @Field(value = "导航")
    private String navigation;
    @Field(value = "连接与共享")
    private String connectAndShare;
    @Field(value = "蓝牙")
    private String blueTeeth;
    @Field(value = "NFC")
    private String nfc;
    @Field(value = "机身接口")
    private String phoneInterface;
}
