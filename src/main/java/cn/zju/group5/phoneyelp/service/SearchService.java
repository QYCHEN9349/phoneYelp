package cn.zju.group5.phoneyelp.service;

import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;
import cn.zju.group5.phoneyelp.domain.vo.CommonException;
import cn.zju.group5.phoneyelp.domain.vo.RequestVo;

import java.util.List;

public interface SearchService {
    public List<BriefPhoneInfo> search(RequestVo requestVo) throws CommonException;
}
