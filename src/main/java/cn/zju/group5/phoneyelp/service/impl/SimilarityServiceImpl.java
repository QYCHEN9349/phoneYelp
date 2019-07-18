package cn.zju.group5.phoneyelp.service.impl;

import cn.zju.group5.phoneyelp.service.SimilarityService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenqingyuan
 * @date 20190710
 * 相似度计算
 */
@Service
public class SimilarityServiceImpl implements SimilarityService {
    /**
     * 余弦相似度计算，以评价文本相似度。
     * @param s1
     * @param s2
     * @return
     */
    public Double textSimilarityCompute(String s1, String s2) {
        if(s1==null||s2==null){
            return 0.0;
        }
        //去除空格
        s1=s1.replaceAll("\\s*","");
        s2=s2.replaceAll("\\s*","");
        //去重
        Character[] chars1 = s1.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        Character[] chars2 = s2.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        Set<Character> set = new HashSet<Character>();
        Collections.addAll(set, chars1);
        Collections.addAll(set, chars2);
        Character[] charSet = new Character[set.size()];
        set.toArray(charSet);

        //构建向量
        int[] vector1 = new int[charSet.length];
        int[] vector2 = new int[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            Character c = charSet[i];
            for (Character c1 : chars1) {
                if (c.equals(c1)) {
                    vector1[i]++;
                }
            }
            for (Character c2 : chars2) {
                if (c.equals(c2)) {
                    vector2[i]++;
                }
            }
        }

        //计算
        double sum1 = 0;
        double sum2 = 0;
        double product = 0;
        for (int i = 0; i < charSet.length; i++) {
            product += vector1[i] * vector2[i];
            sum1+=vector1[i]*vector1[i];
            sum2+=vector2[i]*vector2[i];
        }
        return product/(Math.sqrt(sum1)*Math.sqrt(sum2));
    }
}
