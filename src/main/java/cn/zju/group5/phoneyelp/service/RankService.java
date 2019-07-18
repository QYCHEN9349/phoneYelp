package cn.zju.group5.phoneyelp.service;

import cn.zju.group5.phoneyelp.domain.entity.Phone;

import java.util.List;

public interface RankService {
    public void rankBySimilarty(List<Phone> phoneList, String keyword);
    public void rankByDate(List<Phone> phoneList);
}
