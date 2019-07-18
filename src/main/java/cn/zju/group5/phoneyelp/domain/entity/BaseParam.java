package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class BaseParam {
    @Field(value = "上市日期")
    private String launchDate;
    @Field(value = "电商报价")
    private String price;
    @Field(value = "手机类型")
    private String phoneType;
    @Field(value = "出厂系统内核")
    private String kernel;
    @Field(value = "操作系统")
    private String os;
    @Field(value = "CPU型号")
    private String cpu;
    @Field(value = "机身颜色")
    private String color;
    @Field(value = "解锁方式")
    private String unlockingMOde;
}
