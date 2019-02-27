package com.creatoo.hn.util;

import com.alibaba.druid.sql.visitor.functions.Now;

import java.text.SimpleDateFormat;
import java.util.*;

public class WeekDayUtil {


    /**
     * 输入一个日期的时间段，以及相应的星期数，获得这些星期的日期
     */
    private static Map<Integer, Integer> weekNumberMap = new HashMap<Integer, Integer>();
    static {
        weekNumberMap.put(7,1);
        weekNumberMap.put(1,2);
        weekNumberMap.put(2,3);
        weekNumberMap.put(3,4);
        weekNumberMap.put(4,5);
        weekNumberMap.put(5,6);
        weekNumberMap.put(6,7);


    }


    public static List<String> getDates(String dateFrom, String dateEnd, List<Integer> weekDays) {
        long time;
        long perDayMilSec = 24L * 60 * 60 * 1000;
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 需要查询的星期系数
        String strWeekNumber = weekForNum(weekDays);
        try {
            dateFrom = sdf.format(sdf.parse(dateFrom).getTime() - perDayMilSec);
            while (true) {
                time = sdf.parse(dateFrom).getTime();
                time = time + perDayMilSec;
                Date date = new Date(time);
                dateFrom = sdf.format(date);
                if (dateFrom.compareTo(dateEnd) <= 0) {
                    // 查询的某一时间的星期系数
                    Integer weekDay = dayForWeek(date);
                    // 判断当期日期的星期系数是否是需要查询的
                    if (strWeekNumber.contains(weekDay.toString())) {
                        dateList.add(dateFrom);
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateList;
    }


    // 等到当期时间的周系数。星期日：1，星期一：2，星期二：3，星期三：4，星期四：5，星期五：6，星期六：7
    public static Integer dayForWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /**
     * 得到对应星期的系数 0：1，星期
     */
    public static String weekForNum(List<Integer> weekDays) {
        // 返回结果为组合的星期系数
        String weekNumber = "";


        for (Integer weekDay : weekDays) {
            weekNumber = weekNumber + "" + getWeekNum(weekDay).toString();
        }
        return weekNumber;


    }


    // 将星期转换为对应的系数 0,星期日; 1,星期一; 2....
    public static Integer getWeekNum(int strWeek) {
        return weekNumberMap.get(strWeek);
    }

    /**
     * 由给定时间获取由当前时间到指定时间的相差天时分秒
     * @param time 时间
     * @return
     */
    public static String[] getRestInfo(Date time){
        String[] times = new String[2];

        String timedesc = "";
        String unit = "";

        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//如2016-08-10 20:40
        Date now = new Date();
        long resttime = (time.getTime()-now.getTime())/1000;
        if(resttime > 0){
            if(resttime < 60){
                timedesc = resttime+"";
                unit = "秒";
            }else{
                resttime = resttime/60;//分钟
                if(resttime < 60){
                    timedesc = resttime+"";
                    unit = "分钟";
                }else{
                    resttime = resttime/60;//小时
                    if(resttime < 24){
                        timedesc = resttime+"";
                        unit = "小时";
                    }else{
                        resttime = resttime/24;//天
                        timedesc = resttime+"";
                        unit = "天";
                    }
                }
            }
        }else{
            timedesc = "0";
            unit = "秒";
        }
        times[0] = timedesc;
        times[1] = unit;

        return times;
    }

    //由出生日期获得年龄
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            }else{
                age--;
            }
        }
        return age;
    }

    /**
     * 显示: yyyy-MM-dd 星期几
     * @param date
     * @return
     */
    public static String getWeekStr(Date date){
        String weekStr = "";
        if(date != null){
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            weekStr = sdf.format(date);
            String[] weekArr = new String[]{"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
            java.util.Calendar c = java.util.Calendar.getInstance(Locale.CHINA);
            weekStr += " "+weekArr[c.get(Calendar.DAY_OF_WEEK)-1];
        }
        return weekStr;
    }


    public static void main(String[] args)throws Exception {
        Date now = new Date();

        System.out.println( getWeekStr(now)  );


        String timeStr = "2017-10-17 13:19:21";
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String[] resttime = getRestInfo(simpleFormat.parse(timeStr));
        System.out.println( "------"+resttime[0]+resttime[1]+"----------------" );

        System.out.println( getAge(simpleFormat.parse("1981-10-10 13:19:21")) );
//        //输出从2015-01-01到2015-01-21之间的所有星期一和星期二的日期。
//        List<Integer> daysOfOneWeek = new ArrayList<Integer>();
//        daysOfOneWeek.add(1);
//        daysOfOneWeek.add(2);
//        List<String> daysNeedBookList = getDates("2015-01-01", "2015-01-21", daysOfOneWeek);
//        for (String s : daysNeedBookList) {
//            System.out.println(s);
//        }
    }
}