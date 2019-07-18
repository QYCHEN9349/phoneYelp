package cn.zju.group5.phoneyelp.constant;

public class Constant {
    //推荐素材rediskey
    public static final String RECOMMEND_KEY = "recommend";
    //推荐素材大小
    public static final Integer RECOMMEND_SIZE = 4;
    //推荐素材过期时间
    public static final Long EXPIRE_TIME = 86400L;
    //推荐任务redis分布式锁
    public static final String LOCK_KEY = RECOMMEND_KEY + "lock";
    //rediskey前缀
    public static final String  TYPE_PREFIX = "type";
    //rediskey前缀
    public static final String KEYWORD_PREFIX = "model";
    //搜索结果缓存时间
    public static final Long SEARCH_EXPIRE_TIME = 300L;
}
