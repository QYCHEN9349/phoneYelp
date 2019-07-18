package cn.zju.group5.phoneyelp.service.impl;

import cn.zju.group5.phoneyelp.constant.Constant;
import cn.zju.group5.phoneyelp.constant.KeywordTypeEnum;
import cn.zju.group5.phoneyelp.dao.ElasticSearchDao;
import cn.zju.group5.phoneyelp.dao.PhoneDao;
import cn.zju.group5.phoneyelp.domain.entity.Phone;
import cn.zju.group5.phoneyelp.domain.entity.PhoneIndex;
import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;
import cn.zju.group5.phoneyelp.domain.vo.CommonException;
import cn.zju.group5.phoneyelp.domain.vo.RequestVo;
import cn.zju.group5.phoneyelp.service.DistributeLock;
import cn.zju.group5.phoneyelp.service.RankService;
import cn.zju.group5.phoneyelp.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 提供查询功能
 */
@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private PhoneDao phoneDao;
    private RankService rankService;
    private RedisTemplate<String, Object> redisTemplate;
    private ElasticSearchDao elasticSearchDao;

    @Autowired
    public SearchServiceImpl(PhoneDao phoneDao, RankService rankService, RedisTemplate<String, Object> redisTemplate,
                             ElasticSearchDao elasticSearchDao) {
        this.phoneDao = phoneDao;
        this.rankService = rankService;
        this.redisTemplate = redisTemplate;
        this.elasticSearchDao = elasticSearchDao;
    }

    /**
     * 查询分桶
     * @param requestVo
     * @return
     * @throws CommonException
     */
    public List<BriefPhoneInfo> search(RequestVo requestVo) throws CommonException {

        String redisKey = Constant.TYPE_PREFIX + requestVo.getType() + Constant.KEYWORD_PREFIX + requestVo.getKeyword();
        List<Object> list = redisTemplate.opsForList().range(redisKey, 0, -1);
        if (list != null && !list.isEmpty()) {
            return list.stream().map(t -> (BriefPhoneInfo) t).collect(Collectors.toList());
        }

        List<BriefPhoneInfo> result = new ArrayList<>();
        List<Phone> tempResult = new ArrayList<>();
        KeywordTypeEnum typeEnum = KeywordTypeEnum.getTypeByValue(requestVo.getType());
        if (typeEnum == null) {
            log.error("未知的搜索类型：" + requestVo);
            throw new CommonException(HttpStatus.BAD_REQUEST, "未知的搜索类型");
        }
        Long t1 = System.currentTimeMillis();
        switch (typeEnum) {
            default:
                break;
            case BRAND:
                tempResult = phoneDao.findAllByPhoneBrandLike(requestVo.getKeyword(), PageRequest.of(0, 100));
                break;
            case MODEL:
                tempResult = phoneDao.findAllByPhoneNameLike(requestVo.getKeyword(), PageRequest.of(0, 100));
                rankService.rankBySimilarty(tempResult, requestVo.getKeyword());
                break;
            case FAST:
                tempResult = phoneDao.findPhonesByText(requestVo.getKeyword(), PageRequest.of(0, 100));
                break;
            case ES:
                Long es1 = System.currentTimeMillis();
                List<PhoneIndex> indexList = elasticSearchDao.findAllByPhoneBrandLike(requestVo.getKeyword());
                Long es2 = System.currentTimeMillis();
                log.info("es召回耗时：" + (es2 - es1));
                for (PhoneIndex phoneIndex : indexList) {
                    tempResult.add(phoneDao.findByIdIndex(phoneIndex.getId()));
                }
                Long es3 = System.currentTimeMillis();
                log.info("mongo召回耗时" + (es3 - es2));
                break;
        }
        Long t2 = System.currentTimeMillis();
        log.info("数据召回耗时：" + (t2 - t1) + "----召回策略：" + requestVo.getType() + "----召回数量：" + tempResult.size());
        for (Phone phone : tempResult) {
            result.add(new BriefPhoneInfo(phone));
        }
        Long t3 = System.currentTimeMillis();
        try {
            result.forEach(t -> redisTemplate.opsForList().leftPush(redisKey, t));
            redisTemplate.opsForList().getOperations().expire(redisKey, Constant.SEARCH_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("搜索结果缓存异常--" + e);
        }
        Long t4 = System.currentTimeMillis();
        log.info("写入redis耗时：" + (t4 - t3));
        return result;
    }
}

//
//    private List<BriefPhoneInfo> searchByBrand(RequestVo requestVo) {
//        String redisKey = Constant.BRAND_KEY_PREFIX + requestVo.getKeyword();
//        List<Object> list = redisTemplate.opsForList().range(redisKey, 0, -1);
//        List<BriefPhoneInfo> result = new ArrayList<>();
//        if (list == null || list.isEmpty()) {
//            Long t1 = System.currentTimeMillis();
//            List<Phone> tempList = phoneDao.findPhonesByText(requestVo.getKeyword(), PageRequest.of(0, 100));
//            Long t2 = System.currentTimeMillis();
//            log.info("读取mongo数据耗时--" + (t2 - t1));
//            rankService.rankByDate(tempList);
//            Long t3 = System.currentTimeMillis();
//            log.info("排序耗时--" + (t3 - t2));
//            for (Phone phone : tempList) {
//                result.add(new BriefPhoneInfo(phone));
//            }
//            Long t4 = System.currentTimeMillis();
//            log.info("构造briefInfo耗时--" + (t4 - t3));
//            try {
//                result.forEach(t -> redisTemplate.opsForList().leftPush(redisKey, t));
//                redisTemplate.opsForList().getOperations().expire(redisKey, Constant.SEARCH_EXPIRE_TIME, TimeUnit.SECONDS);
//            } catch (Exception e) {
//                log.error("搜索结果缓存异常--" + e);
//            }
//            Long t5 = System.currentTimeMillis();
//            log.info("redis写入耗时--" + (t5 - t4));
//
//        } else {
//            result = list.stream().map(t -> (BriefPhoneInfo) t).collect(Collectors.toList());
//        }
//        return result;
//    }
//
//    private List<BriefPhoneInfo> searchByName(RequestVo requestVo) {
//
//        String redisKey = Constant.MODEL_KEY_PREFIX + requestVo.getKeyword();
//        List<Object> list = redisTemplate.opsForList().range(redisKey, 0, -1);
//        List<BriefPhoneInfo> result = new ArrayList<>();
//        if (list == null || list.isEmpty()) {
//            Long t1 = System.currentTimeMillis();
//            List<Phone> tempList = phoneDao.findPhonesByPhoneNameLike(requestVo.getKeyword(), PageRequest.of(0, 100));
//            Long t2 = System.currentTimeMillis();
//            log.info("读取mongo数据耗时--" + (t2 - t1));
//            rankService.rankBySimilarty(tempList, requestVo.getKeyword());
//            Long t3 = System.currentTimeMillis();
//            log.info("排序耗时--" + (t3 - t2));
//            for (Phone phone : tempList) {
//                result.add(new BriefPhoneInfo(phone));
//            }
//            Long t4 = System.currentTimeMillis();
//            log.info("构造briefInfo耗时--" + (t4 - t3));
//            try {
//                result.forEach(t -> redisTemplate.opsForList().leftPush(redisKey, t));
//                redisTemplate.opsForList().getOperations().expire(redisKey, Constant.SEARCH_EXPIRE_TIME, TimeUnit.SECONDS);
//            } catch (Exception e) {
//                log.error("搜索结果缓存异常--" + e);
//            }
//            Long t5 = System.currentTimeMillis();
//            log.info("redis写入耗时--" + (t5 - t4));
//        } else {
//            result = list.stream().map(t -> (BriefPhoneInfo) t).collect(Collectors.toList());
//        }
//        return result;
//    }