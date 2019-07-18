package cn.zju.group5.phoneyelp.controller;

import cn.zju.group5.phoneyelp.dao.ElasticSearchDao;
import cn.zju.group5.phoneyelp.dao.PhoneDao;
import cn.zju.group5.phoneyelp.domain.entity.Phone;
import cn.zju.group5.phoneyelp.domain.vo.BriefPhoneInfo;
import cn.zju.group5.phoneyelp.domain.vo.CommonException;
import cn.zju.group5.phoneyelp.domain.vo.CommonResponse;
import cn.zju.group5.phoneyelp.domain.vo.RequestVo;
import cn.zju.group5.phoneyelp.service.RecommendService;
import cn.zju.group5.phoneyelp.service.SearchService;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
public class WebController {
    private SearchService searchService;
    private RecommendService recommendService;
    private PhoneDao phoneDao;
    private ElasticSearchDao elasticSearchDao;
    private GridFsTemplate gridFsTemplate;

    @Value("${server.port}")
    private String port;

    @Autowired
    public WebController(SearchService searchService, RecommendService recommendService,
                         PhoneDao phoneDao, ElasticSearchDao elasticSearchDao, GridFsTemplate gridFsTemplate) {
        this.searchService = searchService;
        this.recommendService = recommendService;
        this.phoneDao = phoneDao;
        this.elasticSearchDao = elasticSearchDao;
        this.gridFsTemplate = gridFsTemplate;
    }

    @GetMapping("/search")
    public CommonResponse search(@NotEmpty @RequestParam(name = "keyword") String keyword,
                                 @Min(value = 1) @Max(value = 4) @RequestParam(name = "type") Integer type,
                                 @Min(value = 1) @RequestParam(name = "page") Integer page,
                                 @Min(value = 1) @RequestParam(name = "pageSize") Integer pageSize) {
        RequestVo requestVo = new RequestVo(keyword, type, page - 1, pageSize);
        log.info("request:" + requestVo);
        List<BriefPhoneInfo> result = searchService.search(requestVo);
        int start = Math.min(result.size(), requestVo.getPage() * requestVo.getPageSize());
        int end = Math.min(result.size(), start + requestVo.getPageSize());
        return new CommonResponse(HttpStatus.OK.value(), String.valueOf(result.size()), result.subList(start, end));
    }

    @GetMapping("/recommend")
    public CommonResponse recommend() {
        List<BriefPhoneInfo> result = recommendService.recommend();
        return new CommonResponse(HttpStatus.OK.value(), result);
    }

    @GetMapping("/phone")
    public CommonResponse getPhone(@NotNull @RequestParam String id) {
        Phone result = phoneDao.findPhoneById(id);
        return new CommonResponse(HttpStatus.OK.value(), result);
    }

    @PostMapping("/score")
    public CommonResponse score(@RequestBody Map<String, String> requestMap) throws CommonException {
        Phone phone = phoneDao.findPhoneById(requestMap.get("id"));
        Double inputScore=Double.valueOf(requestMap.get("score"));
        if (inputScore < 0 || inputScore > 5) {
            throw new CommonException(HttpStatus.BAD_REQUEST, "评分只能在0-5");
        }
        Double score = phone.getPhoneGrade() * phone.getPhoneCTimes() + inputScore;
        phone.setPhoneCTimes(phone.getPhoneCTimes() + 1);
        score = score / phone.getPhoneCTimes();
        phone.setPhoneGrade(score);
        phoneDao.save(phone);
        return CommonResponse.SUCCESS;
    }

    @RequestMapping("/health")
    public CommonResponse health(){
        return new CommonResponse(200,port);
    }

    @GetMapping(value = "/img", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getPic(@RequestParam String fileName) throws CommonException {
        GridFSFile file = gridFsTemplate.findOne(new Query().addCriteria(Criteria.where("filename").is(fileName)));
        if (file == null) {
            throw new CommonException(HttpStatus.BAD_REQUEST, "文件名未找到");
        }
        GridFsResource resource = gridFsTemplate.getResource(file);
        byte[] result =null;
        try {
            InputStream inputStream = resource.getInputStream();
            result = IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            log.error("读取品牌图标异常" + e);
        }
        return result;
    }

}
