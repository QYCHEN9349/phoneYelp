package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class WarrantyInfo {
    @Field(value = "保修政策")
    private String policy;
    @Field(value = "质保时间")
    private String date;
    @Field(value = "质保备注")
    private String comments;
    @Field(value = "客服电话")
    private String phoneNumber;
    @Field(value = "电话备注")
    private String phoneRemarks;
    @Field(value = "详细内容")
    private String details;
}
