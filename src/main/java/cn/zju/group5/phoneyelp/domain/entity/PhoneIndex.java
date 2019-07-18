package cn.zju.group5.phoneyelp.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Document(indexName = "phone_index",type = "blog")
public class PhoneIndex implements Serializable {
    private static final long serialVersionUID = 4564729518133694581L;
    @Id
    private String id;
    private String phoneName;
    private String phoneBrand;
    public PhoneIndex(){}
}
