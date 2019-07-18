package cn.zju.group5.phoneyelp.domain.vo;

import cn.zju.group5.phoneyelp.domain.entity.Phone;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class BriefPhoneInfo {
    private String id;
    private String phoneName;
    private String phoneIcon;
    private String phoneBrand;
    private String price;
    private Double score;

    public BriefPhoneInfo() {
    }

    public BriefPhoneInfo(Phone phone) {
        this.id = phone.getId();
        this.phoneName = phone.getPhoneName();
        this.phoneBrand = phone.getPhoneBrand();
        this.phoneIcon = phone.getPhoneIcon();
        this.score = phone.getPhoneGrade();
        try {
            this.price = phone.getPhoneParam().getBaseParam().getPrice();
        } catch (NullPointerException e) {
            log.error("手机价格信息不存在：" + e);
        }
    }
}
