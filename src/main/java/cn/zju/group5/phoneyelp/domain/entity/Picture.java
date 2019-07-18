package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 手机图片展示，所有字段都是图片链接
 */
@Data
@Document(collection = "phoneList")
public class Picture {
    @Field(value = "产品图片")
    private List<String> pictureList;
}
