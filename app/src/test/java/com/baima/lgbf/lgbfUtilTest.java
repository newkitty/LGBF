package com.baima.lgbf;

import com.baima.lgbf.enums.BaXue;
import com.baima.lgbf.util.LGBFUtil;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class lgbfUtilTest {
    @Test
    //根据给定干支获取八穴
    public void getBaXueByGZTest() {
        String s = "丙,寅,乙,未";
        String[] split = s.split(",");
        String rtg = split[0];
        String rdz = split[1];
        String stg = split[2];
        String sdz = split[3];
        BaXue[] baXues = LGBFUtil.getBaXues(rtg, rdz, stg, sdz);
        for (BaXue baXue : baXues) {
            System.out.println(baXue.name);
            System.out.println(baXue.location);
            System.out.println(baXue.features);
            System.out.println(baXue.clinical);
        }
    }

@Test
//根据给定时间戳获取穴位
public void getBaXuesByTimestampTest(){
    Calendar calendar = Calendar.getInstance();
calendar.set(2020,10,19,23  ,00);
    BaXue[] baXues = LGBFUtil.getBaXues(calendar.getTimeInMillis());
    for (BaXue baXue : baXues) {
        System.out.println(baXue.name);
        System.out.println(baXue.location);
        System.out.println(baXue.features);
        System.out.println(baXue.clinical);
    }
}
    @Test
    //获取 日时干支
    public void getRSGZTest() {
        Calendar calendar = Calendar.getInstance();
        for (int x = 0; x < 100; x++) {
            int y = new Random().nextInt(2020 - 1945 + 1) + 1945;
            int m = new Random().nextInt(12);
            int d = new Random().nextInt(28) + 1;
            int h = new Random().nextInt(23);
            int mm = new Random().nextInt(60);
            int s = 0;
            calendar.set(
                    y, m, d, h, mm, s
            );

            System.out.println(calendar.getTime().toLocaleString());
            //获取 日时干支
            String[] rgz = LGBFUtil.getRGZ(calendar.getTimeInMillis());
            String[] sgz = LGBFUtil.getSGZ(calendar.getTimeInMillis());

            String rtg = rgz[0];
            String rdz = rgz[1];
            String stg = sgz[0];
            String sdz = sgz[1];

            System.out.println(String.format("%s%s日%s%s时", rtg, rdz, stg, sdz));
            System.out.println(LGBFUtil.getBaGua(rtg, rdz, stg, sdz) + "卦");


            System.out.println();
        }
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}