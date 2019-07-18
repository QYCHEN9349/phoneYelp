package cn.zju.group5.phoneyelp.service.impl;

import cn.zju.group5.phoneyelp.domain.entity.Phone;
import cn.zju.group5.phoneyelp.service.RankService;
import cn.zju.group5.phoneyelp.service.SimilarityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class RankServiceImpl implements RankService {
    @Autowired
    private SimilarityService similarityService;

    /**
     * 根据发布日期倒序
     *
     * @param phoneList
     */
    public void rankByDate(List<Phone> phoneList) {
        Collections.sort(phoneList, new Comparator<Phone>() {
            @Override
            public int compare(Phone o1, Phone o2) {
                //对日期字符串去除首尾空格
                String s1, s2;
                try {
                    s1 = o1.getPhoneParam().getBaseParam().getLaunchDate().replaceAll("\\s*", "");
                } catch (NullPointerException e) {
                    s1 = "";
                    log.error("id:" + o1.getPhoneId() + "-发售日期不存在：" + e);
                }
                try {
                    s2 = o2.getPhoneParam().getBaseParam().getLaunchDate().replaceAll("\\s*", "");
                } catch (NullPointerException e) {
                    s2 = "";
                    log.error("id:" + o2.getPhoneId() + ":发售日期不存在：" + e);
                }
                return s1.compareTo(s2);
            }
        });
    }

    /**
     * 根据名字相似度排序
     *
     * @param phoneList
     * @param keyword
     */
    public void rankBySimilarty(List<Phone> phoneList, String keyword) {
        Collections.sort(phoneList, new Comparator<Phone>() {
            @Override
            public int compare(Phone o1, Phone o2) {
                double val1 = similarityService.textSimilarityCompute(o1.getPhoneName(), keyword);
                double val2 = similarityService.textSimilarityCompute(o2.getPhoneName(), keyword);
                return val1 > val2 ? 1 : 0;
            }
        });
    }
}
