package cn.zju.group5.phoneyelp.dao;

import cn.zju.group5.phoneyelp.domain.entity.Phone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneDao extends MongoRepository<Phone, String> {

    @Query(value = "{}", fields = "{'id':1,'phoneName':1,'phoneBrand':1}")
    public List<Phone> findAllData();

    @Query(value = "{'phoneBrand':{'$regex':?0}}", fields = "{'id':1,'phoneName':1,'phoneBrand':1,'phoneIcon':1," +
            "'phoneGrade':1,'phoneParam.基本参数.电商报价':1,'phoneParam.基本参数.上市日期':1}")
    public List<Phone> findAllByPhoneBrandLike(String brand, Pageable pageable);

    @Query(value = "{'phoneName':{'$regex':?0}}", fields = "{'id':1,'phoneName':1,'phoneBrand':1,'phoneIcon':1," +
            "'phoneGrade':1,'phoneParam.基本参数.电商报价':1,'phoneParam.基本参数.上市日期':1}")
    public List<Phone> findAllByPhoneNameLike(String name, Pageable pageable);

    public List<Phone> findALLByPhoneIdIsNotNull(Pageable pageable);

    public Phone findPhoneById(String id);

    @Query(value = "{'$text':{'$search':?0}}", fields = "{'id':1,'phoneName':1,'phoneBrand':1,'phoneIcon':1," +
            "'phoneGrade':1,'phoneParam.基本参数.电商报价mvn':1,'phoneParam.基本参数.上市日期':1}")
    public List<Phone> findPhonesByText(String keyword, Pageable pageable);

    @Query(value = "{'_id':?0}", fields = "{'id':1,'phoneName':1,'phoneBrand':1,'phoneIcon':1," +
            "'phoneGrade':1,'phoneParam.基本参数.电商报价':1,'phoneParam.基本参数.上市日期':1}")
    public Phone findByIdIndex(String id);
}
