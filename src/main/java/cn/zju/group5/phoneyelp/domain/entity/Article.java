package cn.zju.group5.phoneyelp.domain.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "phoneList")
public class Article {
    @Field(value = "articleID")
    private String id;
    @Field(value = "articleLink")
    private String link;
    @Field(value = "articleTitle")
    private String title;
    @Field(value = "articlePic")
    private String pic;
    @Field(value = "articlePara")
    private String para;
    @Field(value = "articleDate")
    private String date;
}
