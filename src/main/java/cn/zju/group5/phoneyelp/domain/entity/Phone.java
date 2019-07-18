package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * 手机总体信息
 */
@Data
@Document(collection = "phoneList")
public class Phone {
    @Id
    private String id;
    @Field(value = "phonePic")
    private List<String> phonePic;
    @Field(value = "phoneName")
    private String phoneName;
    @Field(value = "phoneID")
    private String phoneId;
    @Field(value = "phoneParam")
    private Param phoneParam;
    @Field(value = "phoneBrand")
    private String phoneBrand;
    @Field(value = "phoneIcon")
    private String phoneIcon;
    @Field(value = "phoneGrade")
    private Double phoneGrade;
    @Field(value = "phoneCTimes")
    private Integer phoneCTimes;
    @Field(value = "phoneNews")
    private Article[] phoneNews;
    @Field(value = "phoneEval")
    private Article[] phoneEval;
}
