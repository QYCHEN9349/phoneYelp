package cn.zju.group5.phoneyelp.dao;

import cn.zju.group5.phoneyelp.domain.entity.PhoneIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElasticSearchDao extends ElasticsearchRepository<PhoneIndex,String> {
    public List<PhoneIndex> findAllByPhoneBrandLike(String keyword);
}
