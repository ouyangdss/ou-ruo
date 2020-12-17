package test;

import com.ruoyi.system.StringSequenceGenerator;
import com.ruoyi.system.string.EaioStringSequenceGenerator;
import com.ruoyi.system.string.JvmStringSequenceGenerator;
import com.ruoyi.system.utils.SequenceUtil;
import com.ruoyi.system.utils.SequenceUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className TestSequenceUtil.java
 * @createTime 2020年12月17日 09:20:00
 */
public class TestSequenceUtil {


    @Test
    public void test1() {
        //测试 ip自旋
        List<String> orderNos = Collections.synchronizedList(new ArrayList<String>());
        IntStream.range(0, 800000).parallel().forEach(i -> {
            orderNos.add(SequenceUtils.getStringSequence());
        });
        printLists(orderNos);
    }
    @Test
    public void test2(){
        //测试系统UUID
        StringSequenceGenerator sequenceGenerator = new JvmStringSequenceGenerator();
        List<String> orderNos = Collections.synchronizedList(new ArrayList<String>());
        IntStream.range(0, 800000).parallel().forEach(i -> {
            orderNos.add(sequenceGenerator.getStringSequence().replace("-",""));
        });
        printLists(orderNos);
    }
    @Test
    public void test3(){
        //测试创建UUID的方式
        StringSequenceGenerator sequenceGenerator = new EaioStringSequenceGenerator();
        List<String> orderNos = Collections.synchronizedList(new ArrayList<String>());
        IntStream.range(0, 80000).parallel().forEach(i -> {
            orderNos.add(sequenceGenerator.getStringSequence().replace("-",""));
        });
        printLists(orderNos);
    }



    private void printLists(List<String> orderNos){
        List<String> filterOrderNos = orderNos.stream().distinct().collect(Collectors.toList());
        System.out.println("序列号样例：" + orderNos.get(22));
        System.out.println("生成序列号数：" + orderNos.size());
        System.out.println("过滤重复后序列号数：" + filterOrderNos.size());
        System.out.println("重复序列号数：" + (orderNos.size() - filterOrderNos.size()));
    }

}
