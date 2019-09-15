package com.atguigu.gmall.manage;

import com.gmall.common.util.SequenceHandler;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Mock
    private SequenceHandler sequenceHandler;

    @Test
    public void random() throws Exception {
        //获取主键12位
        long timeStanp = System.currentTimeMillis();
        String datePattern = "yyyyMMddHHmmssSSS";
        String id = DateFormatUtils.format(timeStanp, datePattern);
        Long seqId = Long.parseLong(id);
        System.out.println(seqId);
    }

}
