package cn.zju.group5.phoneyelp.service.impl;

import cn.zju.group5.phoneyelp.constant.Constant;
import cn.zju.group5.phoneyelp.dao.PhoneDao;
import cn.zju.group5.phoneyelp.domain.entity.Phone;
import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;
import cn.zju.group5.phoneyelp.service.DistributeLock;
import cn.zju.group5.phoneyelp.service.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 提供推荐功能
 */
@Slf4j
@Service
public class RecommendServiceImpl implements RecommendService {
    private PhoneDao phoneDao;
    private RedisTemplate<String, Object> redisTemplate;
    private DistributeLock distributeLock;

    @Autowired
    public RecommendServiceImpl(PhoneDao phoneDao, RedisTemplate<String, Object> redisTemplate, DistributeLock distributeLock) {
        this.phoneDao = phoneDao;
        this.redisTemplate = redisTemplate;
        this.distributeLock = distributeLock;
    }

    public List<BriefPhoneInfo> recommend() {
        List<BriefPhoneInfo> result = new ArrayList<>();
        List<Object> temp = redisTemplate.opsForList().range(Constant.RECOMMEND_KEY, 0, -1);
        if (temp == null || temp.isEmpty()) {
            List<Phone> list = phoneDao.findALLByPhoneIdIsNotNull(PageRequest.of(0, Constant.RECOMMEND_SIZE));
            for (Phone phone : list) {
                result.add(new BriefPhoneInfo(phone));
            }
            String lockValue = Thread.currentThread().getName();
            //todo 魔法数字，不想管了
            distributeLock.lock(Constant.LOCK_KEY, lockValue, Duration.ofSeconds(10));
            try {
                result.forEach(t->redisTemplate.opsForList().leftPush(Constant.RECOMMEND_KEY,t));
                redisTemplate.expire(Constant.RECOMMEND_KEY, Constant.EXPIRE_TIME, TimeUnit.SECONDS);
                return result.subList(0, Math.min(list.size(), Constant.RECOMMEND_SIZE));
            } catch (Exception e) {
                log.error("推荐材料插入出错" + e);
            } finally {
                distributeLock.unlock(Constant.LOCK_KEY, lockValue);
            }
        }
        temp = temp.subList(0, Math.min(temp.size(), Constant.RECOMMEND_SIZE));
        result = temp.stream().map(t -> (BriefPhoneInfo) t).collect(Collectors.toList());
        return result;
    }
}
