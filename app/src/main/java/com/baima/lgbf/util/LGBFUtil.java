package com.baima.lgbf.util;

import com.baima.lgbf.enums.BaXue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class LGBFUtil {
    /**
     * 天干
     */
    public static final String[] tgs = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    /**
     * 地支
     */
    public static final String[] dzs = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

    /**
     * 日天干基数
     */
    public static final int[] rtgjss = {10, 9, 7, 8, 7, 10, 9, 7, 8, 7};
    /**
     * 日地支基数
     */
    public static final int[] rdzjss = {7, 10, 8, 8, 10, 7, 7, 10, 9, 9, 10, 7};
    /**
     * 时天干基数
     */
    public static final int[] stgjss = {9, 8, 7, 6, 5, 9, 8, 7, 6, 5};
    /**
     * 时地支基数
     */
    public static final int[] sdzjss = {9, 8, 7, 6, 5, 4, 9, 8, 7, 6, 5, 4};

    /**
     * 对应八卦
     */
    public static final String[] bgs = {"坎", "坤", "震", "巽", "坤", "乾", "兑", "艮", "离"};
    /**
     * 对应穴位
     */
    public static final String[] xws = {"申脉", "照海", "外关", "临泣", "女:内关,男:照海", "公孙", "后溪", "内关", "列缺"};
    //日上起时表，日天干/时辰
    public static final String[][][] tableRSQSB = new String[10][12][2];

    static {
        for (int i = 0; i < 10; i++) {
            //每一行第一个日天干索引 ，0246802468
            int indexFirstTg = i * 2 % 10;
            for (int j = 0; j < 12; j++) {
                //日天干索引 递增，到10变为0，
                int indexGan = (indexFirstTg + j) % 10;
                //时辰索引 等于j
//                System.out.print(Gan[indexGan]+Zhi[j]+", ");

                tableRSQSB[i][j][0] = tgs[indexGan];
                tableRSQSB[i][j][1] = dzs[j];
            }
//            System.out.println();
        }
    }


    public static String getBaGua(String rtg, String rdz, String stg, String sdz) {
        return bgs[getRemainder(rtg, rdz, stg, sdz) - 1];
    }

    public static String getBaGua(long timestamp) {
        String[] rgz = getRGZ(timestamp);
        String[] sgz = getSGZ(timestamp);
        return getBaGua(rgz[0], rgz[1], sgz[0], sgz[1]);
    }

    /**
     * 根据日时干支获取穴位
     *
     * @param rtg
     * @param rdz
     * @param stg
     * @param sdz
     * @return 有可能 返回两个穴位
     */
    public static BaXue[] getBaXues(String rtg, String rdz, String stg, String sdz) {
        int baXueIndex = getRemainder(rtg, rdz, stg, sdz) - 1;
        if (baXueIndex == 4) {
            return new BaXue[]{
                    BaXue.nei_guan, BaXue.zhao_hai
            };
        }
        return new BaXue[]{
                BaXue.values()[baXueIndex]
        };
    }

    /**
     * 根据时间戳获取穴位
     *
     * @param timestamp
     * @return 有可能返回两个
     */
    public static BaXue[] getBaXues(long timestamp) {
        String[] rgz = getRGZ(timestamp);
        String[] sgz = getSGZ(timestamp);
        return getBaXues(rgz[0], rgz[1], sgz[0], sgz[1]);
    }

    /**
     * 根据日时干支求余数
     *
     * @param rtg
     * @param rdz
     * @param stg
     * @param sdz
     * @return
     */
    private static int getRemainder(String rtg, String rdz, String stg, String sdz) {
        int remainder = -1;
        int rtgIndex = getTGIndex(rtg);
        int rdzIndex = getDZIndex(rdz);
        int stgIndex = getTGIndex(stg);
        int sdzIndex = getDZIndex(sdz);

        int i = -1;
        if (rtgIndex % 2 == 0 && rdzIndex % 2 == 0) {
            //阳日
            i = 9;
        } else if (rtgIndex % 2 == 1 && rdzIndex % 2 == 1) {
            //阴日
            i = 6;
        }

        //根据干支索引获取基数并求和求余数
        remainder = (rtgjss[rtgIndex] + rdzjss[rdzIndex] + stgjss[stgIndex] + sdzjss[sdzIndex]) % i;
        return remainder == 0 ? i : remainder;
    }

    private static int getDZIndex(String dz) {
        return Arrays.asList(dzs).indexOf(dz);
    }

    private static int getTGIndex(String tg) {
        return Arrays.asList(tgs).indexOf(tg);
    }

    /**
     * 根据天干获取对应的地支集合
     *
     * @param tgIndex
     * @return
     */
    public static List<String> getDZsByTGIndex(int tgIndex) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < dzs.length; i++) {
            if (i % 2 == tgIndex % 2) {
                list.add(dzs[i]);
            }
        }
        return list;
    }

    /**
     * 获取干支
     * 可以获取日干支，
     *
     * @param timestamp
     * @param mills     要获取 的干支类型对应的时间戳，日对应1000*3600*24
     * @param indexGan
     * @param indexZhi
     * @return
     */
    private static String[] getGZ(long timestamp, long mills, int indexGan, int indexZhi) {
        //先知道某天的干支，再计算给定时间戳到那天的距离，再计算余数，得到干支
//        2000年2月5日23:00-23:59时
//        庚辰年 戊寅月
// 癸巳日 壬子时
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 1, 5, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long timeInMillis = calendar.getTimeInMillis();

        int iGan;
        int iZhi;
        if (timestamp >= timeInMillis) {
            //如果 是往后的时间，不足一天不算，可以直接取余数
            long l = (timestamp - timeInMillis) / mills;
            iGan = (int) ((indexGan + l) % 10);
            iZhi = (int) ((indexZhi + l) % 12);
        } else {
            //如果是往前的时间，不足一天算一天，刚好一天是一天，
            long quotient = (timestamp - timeInMillis) / mills;
            long remainder = (timestamp - timeInMillis) % mills;
            //如果 有余数商减1
            long l = remainder == 0 ? quotient : quotient - 1;

            iGan = (int) ((indexGan + l) % 10);
            iZhi = (int) ((indexZhi + l) % 12);

            //如果得到的是小于0，要加上对应数
            if (iGan < 0) {
                iGan += 10;
            }
            if (iZhi < 0) {
                iZhi += 12;
            }
        }
        return new String[]{tgs[iGan], dzs[iZhi]};
    }

    /**
     * 根据时间戳获取日干支
     *
     * @param timestamp
     * @return
     */
    public static String[] getRGZ(long timestamp) {
        return getGZ(timestamp, 1000 * 3600 * 24, getTGIndex("癸"), getDZIndex("巳"));
    }

    /**
     * 根据给定时间戳获取时干支
     *
     * @param timestamp
     * @return
     */
    public static String[] getSGZ(long timestamp) {
        //先获取日干支，再根据日干和时辰获取时干支
        String[] rgz = getRGZ(timestamp);
        int rtgIndex = getTGIndex(rgz[0]);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int indexSC = getIndexSC(hour);
        String[] sgz = tableRSQSB[rtgIndex][indexSC];
        return new String[]{sgz[0], sgz[1]};
    }

    /**
     * 根据日干索引获取对应的时干支的二维数组
     * 一维数组的0索引是时干， 1索引是时支
     *
     * @param indexRTG
     * @return
     */
    public static String[][] getSGZs(int indexRTG) {
        return tableRSQSB[indexRTG];
    }

    /**
     * 根据小时获取十二时辰索引
     *
     * @param hour_24
     * @return
     */
    private static int getIndexSC(int hour_24) {
        int indexSC = 0;
        if (hour_24 > 0 && hour_24 < 23) {
            //有余数就加1
            indexSC = hour_24 % 2 == 0 ? hour_24 / 2 : hour_24 / 2 + 1;
        }
        return indexSC;
    }
}