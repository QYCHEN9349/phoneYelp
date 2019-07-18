package cn.zju.group5.phoneyelp.service;

import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;

import java.util.List;

public interface RecommendService {
    public List<BriefPhoneInfo> recommend();
}
