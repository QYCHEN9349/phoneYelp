package cn.zju.group5.phoneyelp.constant;

import lombok.Getter;

public enum KeywordTypeEnum {
    MODEL(1), //型号
    BRAND(2), //品牌
    FAST(3),  //快速
    ES(4);//elastic-search

    @Getter
    private Integer val;

    KeywordTypeEnum(Integer val){
        this.val=val;
    }
    public static KeywordTypeEnum getTypeByValue(Integer value){
        for (KeywordTypeEnum enums:KeywordTypeEnum.values()){
            if(enums.getVal().equals(value)){
                return enums;
            }
        }
        return null;
    }
}
