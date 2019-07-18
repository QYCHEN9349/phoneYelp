package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Hardware {
    @Field(value = "核心数")
    private String coreNums;
    @Field(value = "RAM容量")
    private String ram;
    @Field(value = "ROM容量")
    private String rom;
    @Field(value = "电池类型")
    private String batteryType;
    @Field(value = "电池容量")
    private String capacity;
    @Field(value = "续航时间")
    private String endurance;
    @Field(value = "电池充电")
    private String chargingMode;

}
