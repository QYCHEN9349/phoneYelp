package cn.zju.group5.phoneyelp.service;

import cn.zju.group5.phoneyelp.constant.Constant;
import cn.zju.group5.phoneyelp.dao.ElasticSearchDao;
import cn.zju.group5.phoneyelp.dao.PhoneDao;
import cn.zju.group5.phoneyelp.domain.entity.Phone;
import cn.zju.group5.phoneyelp.domain.entity.PhoneIndex;
import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScheduleService {
    private PhoneDao phoneDao;
    private RedisTemplate<String, Object> redisTemplate;
    private DistributeLock distributeLock;
    private ElasticSearchDao elasticSearchDao;

    /**
     * 定时任务
     * @param phoneDao
     * @param redisTemplate
     * @param distributeLock
     * @param elasticSearchDao
     */
    @Autowired
    public ScheduleService(PhoneDao phoneDao, RedisTemplate<String, Object> redisTemplate,
                           DistributeLock distributeLock, ElasticSearchDao elasticSearchDao) {
        this.phoneDao = phoneDao;
        this.redisTemplate = redisTemplate;
        this.distributeLock = distributeLock;
        this.elasticSearchDao = elasticSearchDao;
    }

    /**
     * 定时向redis写入推荐材料
     */
    @Async
    @Scheduled(cron = "0 */1 * * * ?")
    public void recommendMaterialTask() {
        List<Phone> list = phoneDao.findALLByPhoneIdIsNotNull(PageRequest.of(0, Constant.RECOMMEND_SIZE));
        List<BriefPhoneInfo> result = new ArrayList<>();
        for (Phone phone : list) {
            result.add(new BriefPhoneInfo(phone));
        }
        String lockValue = Thread.currentThread().getName();
        //todo 魔法数字
        distributeLock.lock(Constant.LOCK_KEY, lockValue, Duration.ofSeconds(20));
        String key = Constant.RECOMMEND_KEY;
        Boolean hasKey = redisTemplate.opsForList().getOperations().hasKey(key);
        try {
            while (hasKey != null && hasKey && redisTemplate.opsForList().size(key) > 0) {
                redisTemplate.opsForList().leftPop(key);
            }
            result.forEach(t -> redisTemplate.opsForList().leftPush(Constant.RECOMMEND_KEY, t));
            redisTemplate.expire(Constant.RECOMMEND_KEY, Constant.EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("定时任务插入数据出错" + e);
        } finally {
            distributeLock.unlock(Constant.LOCK_KEY, lockValue);
        }
    }

    /**
     * 定时向elasticSearch同步mongo数据库中的数据
     */
    @Async
    @Scheduled(cron = "0 * */1 * * ?")
    public void MongoToEsMaterialTask() {
        Long t1 = System.currentTimeMillis();
        List<Phone> list = phoneDao.findAllData();
        for (Phone phone : list) {
            PhoneIndex phoneIndex = new PhoneIndex(phone.getId(), phone.getPhoneName(), phone.getPhoneBrand());
            elasticSearchDao.save(phoneIndex);
        }
        Long t2 = System.currentTimeMillis();
        log.info("mongo-es数据同步成功，耗时：" + (t2 - t1) + "同步大小：" + list.size());
    }


}
