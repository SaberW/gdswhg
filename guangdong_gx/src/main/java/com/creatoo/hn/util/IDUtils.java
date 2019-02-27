package com.creatoo.hn.util;

import com.creatoo.hn.dao.mapper.WhgSysIdsCustomMapper;
import com.creatoo.hn.dao.model.WhgSysIds;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * ID生成器
 * Created by wangxl on 2017/7/17.
 */
public class IDUtils {
    //保存数据库中的ID的DAO
    private static WhgSysIdsCustomMapper whgSysIdsCustomMapper;
    public static void setWhgSysIdsCustomMapper(WhgSysIdsCustomMapper aWhgSysIdsCustomMapper){
        IDUtils.whgSysIdsCustomMapper = aWhgSysIdsCustomMapper;
    }
    public static WhgSysIdsCustomMapper getWhgSysIdsCustomMapper(){
        return IDUtils.whgSysIdsCustomMapper;
    }

    //日期格式对象
    private static SimpleDateFormat sdf_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat sdf_yyMMdd = new SimpleDateFormat("yyMMdd");

    //保存每天的ID
    private static Map<String, Map<String, String>> idMap = new HashMap<>();

    /**
     * 获取32位长度的ID字符串
     * @return ID
     */
    public static String getID32(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 生成16ID
     * @return ID
     */
    public static String getID(){
        //日期字符串yyyyMMdd
        String cdate = sdf_yyyyMMdd.format(System.currentTimeMillis());

        //主键格式  yyyyMMdd[8位随机数] 总共14位
        String keyVal = cdate + IDUtils._getRandom(8);

        return keyVal;
    }

    /**
     * 批量生成ID
     * @param size id个数
     * @return List<String>
     */
    public static List<String> getIds(int size){
        //
        List<String> idList = new ArrayList<>();

        //日期字符串yyyyMMdd
        String cdate = sdf_yyyyMMdd.format(System.currentTimeMillis());

        List<String> randomList = IDUtils._getRandoms(8, size);
        for(String random : randomList){
            //主键格式  yyyyMMdd[8位随机数] 总共16位
            String keyVal = cdate + random;
            idList.add(keyVal);
        }
        return idList;
    }

    /**
     * 生成12订单号: getOrderId(EnumOrderType.ORDER_ACT.getValue()),取活动订单号
     * @param type 订单类型, 详见EnumOrderType
     * @return 新的订单号
     * @throws Exception
     */
    public static String getOrderID(String type){
        //日期字符串yyMMdd
		String cdate = sdf_yyMMdd.format(System.currentTimeMillis());

		//主键格式  [type]yyMMdd[5位随机数] 总共12位
		String keyVal = type + cdate + IDUtils._getRandom(5);

		return keyVal;
    }

    /**
     * 获取指定长度的多少个ID
     * @param len id长度
     * @param size 多少个ID
     * @return
     */
    private synchronized static List<String> _getRandoms(int len, int size){
        List<String> randomList = new ArrayList<>();
        try {
            String yyyyMMdd = sdf_yyyyMMdd.format(System.currentTimeMillis());
            for(int i=0; i<size; i++){
                String random = null;
                random = IDUtils._random(len);
                while (random.length() != len || IDUtils._existId(random, yyyyMMdd)) {
                    random = IDUtils._random(len);
                }
                IDUtils.saveRandom(random, yyyyMMdd, false);
                randomList.add(random);
            }
            //批量保存到数据库
            IDUtils.saveRandom4Batch(yyyyMMdd, randomList);

        }catch (Exception e){}
        return randomList;
    }

    /**
     * 获取8位长度的随机数-纯数字
     * @return 指定长度的随机数字符串
     */
    private synchronized static String _getRandom(int len){
        String random = null;
        try {
            String yyyyMMdd = sdf_yyyyMMdd.format(System.currentTimeMillis());
            random = IDUtils._random(len);
            while (random.length() != len || IDUtils._existId(random, yyyyMMdd)) {
                random = IDUtils._random(len);
            }
            IDUtils.saveRandom(random, yyyyMMdd, true);
        }catch (Exception e){}
        return random;
    }

    /**
     * 根据长度生成随机数-纯数字
     * @param len 随机数长度
     * @return
     */
    private static String _random(int len){
        int result = 0;
        try{
            Integer[] array = {0,1,2,3,4,5,6,7,8,9};
            Random rand = new Random();
            for (int i = 10; i > 1; i--) {
                int index = rand.nextInt(i);
                int tmp = array[index];
                array[index] = array[i - 1];
                array[i - 1] = tmp;
            }
            for(int i = 0; i < len; i++){
                result = result * 10 + array[i];
            }
        }catch (Exception e){
        }
        return ""+result;
    }

    /**
     * 随机数是否重复
     * @param random 随机数
     * @return true-重复, false-不重复
     */
    private synchronized static boolean _existId(String random, String yyyyMMdd)throws Exception{
        boolean exist = false;

        //初始-
        if(!idMap.containsKey(yyyyMMdd)){
            //记录当日所有的随机数
            Map<String, String> randoms = new HashMap<>();
            randoms.put("szwhg", "szwhg");

            //删除非当前的随机数
            WhgSysIdsCustomMapper mapper = IDUtils.getWhgSysIdsCustomMapper();
            Example example = new Example(WhgSysIds.class);
            example.createCriteria().andNotEqualTo("iddate", yyyyMMdd);
            mapper.deleteByExample(example);

            //查询系统当日已经存在的随机数
            example.clear();
            example.createCriteria().andEqualTo("iddate", yyyyMMdd);
            List<WhgSysIds> randomList =  mapper.selectByExample(example);
            if(randomList != null){
                for(WhgSysIds wsi : randomList){
                    randoms.put(wsi.getIdval(), wsi.getIdval());
                }
            }

            idMap.put(yyyyMMdd, randoms);
        }

        //判断是否存在
        exist = idMap.get(yyyyMMdd).containsKey(random);

        return exist;
    }

    /**
     * 保存随机数
     * @param random
     * @param yyyyMMdd
     */
    private synchronized static void saveRandom(String random, String yyyyMMdd, boolean saveDB)throws Exception{
        //初始
        if(!idMap.containsKey(yyyyMMdd)){
            idMap.clear();
            Map<String, String> randoms = new HashMap<>();
            randoms.put(random, random);
            idMap.put(yyyyMMdd, randoms);
        }else{
            idMap.get(yyyyMMdd).put(random, random);
        }

        //保存到数据库
        if(saveDB) {
            WhgSysIdsCustomMapper mapper = IDUtils.getWhgSysIdsCustomMapper();
            WhgSysIds whgSysIds = new WhgSysIds();
            whgSysIds.setIddate(yyyyMMdd);
            whgSysIds.setIdval(random);
            mapper.insert(whgSysIds);
        }
    }

    /**
     * 批量保存随机数
     * @param randomList
     * @param yyyyMMdd
     * @throws Exception
     */
    private static void saveRandom4Batch(String yyyyMMdd, List<String> randomList)throws Exception{
        //保存到数据库
        if(randomList != null && randomList.size() > 0) {
            List<WhgSysIds> wsiList = new ArrayList<>();
            for(String random : randomList){
                WhgSysIds whgSysIds = new WhgSysIds();
                whgSysIds.setIddate(yyyyMMdd);
                whgSysIds.setIdval(random);
                wsiList.add(whgSysIds);
            }
            WhgSysIdsCustomMapper mapper = IDUtils.getWhgSysIdsCustomMapper();
            mapper.insertByBatch(wsiList);
        }
    }

    /*public static void tjarea()throws Exception{
        JSONArray province = new JSONArray();
        JSONArray city = new JSONArray();
        JSONArray area = new JSONArray();

        Document doc = Jsoup.connect("http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/index.html").timeout(180000).get();
        Elements provincetr = doc.getElementsByClass("provincetr");
        for(Element tr : provincetr){
            Elements tds = tr.getElementsByTag("td");
            for(Element td: tds){
                Elements as =  td.getElementsByTag("a");
                for(Element a: as){
                    String id = a.attr("href");
                    id = id.substring(0, id.indexOf("."));
                    String name = a.text();
                    province.add(JSON.parse("{\"id\":\""+id+"\", \"name\":\""+name+"\"}"));

                    //市
                    //http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/33.html
                    String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/"+id+".html";
                    tjarea_city(url, city, id, area, name);
                }
            }
        }
        //System.out.println(province.toJSONString());
        //System.out.println(city.toJSONString());
        //System.out.println(area.toJSONString());
        FileUtils.writeStringToFile(new File("D:\\sun3d\\area.js"), "var __PROVINCE = "+province.toJSONString()+";", "UTF-8");
        FileUtils.writeStringToFile(new File("D:\\sun3d\\area.js"), "var __CITY = "+city.toJSONString()+";", "UTF-8", true);
        FileUtils.writeStringToFile(new File("D:\\sun3d\\area.js"), "var __AREA = "+area.toJSONString()+";", "UTF-8", true);
        System.out.println("============END");
    }

    public static void tjarea_city(String url, JSONArray city, String pid, JSONArray area, String proname)throws Exception{
        //Thread.currentThread().sleep(1000);
        Document doc = Jsoup.connect(url).timeout(180000).get();
        Elements provincetr = doc.getElementsByClass("citytr");
        for(Element tr : provincetr){
            Elements tds = tr.getElementsByTag("td");
            for(Element td: tds){
                Elements as =  td.getElementsByTag("a");
                for(Element a: as){
                    String name = a.text();
                    if(!name.matches("\\d+")){
                        String id = a.attr("href");
                        id = id.substring(id.indexOf("/")+1, id.indexOf("."));
                        city.add(JSON.parse("{\"id\":\""+id+"\", \"name\":\""+name+"\", \"proid\":\"" + pid + "\", \"proname\":\"" + proname + "\"}"));

                        //区
                        String urls = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2016/"+pid+"/"+id+".html";
                        tjarea_area(urls, id, area, name);
                    }
                }
            }
        }
    }

    public static void tjarea_area(String url, String cityid, JSONArray area, String cityname)throws Exception{
        //Thread.currentThread().sleep(1000);
        Document doc = Jsoup.connect(url).timeout(180000).get();
        Elements provincetr = null;//doc.getElementsByClass("countytr");
        Elements tables = doc.getElementsByTag("table");
        for(Element table : tables){
            String tableClassName = table.className();
            if(StringUtils.isNotEmpty(tableClassName)){
                provincetr = table.getElementsByTag("tr");
            }
        }
        if(provincetr == null) return;

        for(Element tr : provincetr){
            String trClassName = tr.className();
            if(!trClassName.endsWith("tr")){
                continue;
            }
            Elements tds = tr.getElementsByTag("td");

            for(Element td: tds){
                Elements as =  td.getElementsByTag("a");
                for(Element a: as){
                    String name = a.text();
                    if(!name.matches("\\d+")){
                        String id = a.attr("href");
                        id = id.substring(id.indexOf("/")+1, id.indexOf("."));

                        area.add(JSON.parse("{\"id\":\""+id+"\", \"name\":\""+name+"\", \"cityid\":\"" + cityid + "\", \"cityname\":\"" + cityname + "\"}"));
                    }
                }
            }
        }
    }*/


    public static void main(String[] args)throws Exception {
        System.out.println(IDUtils.getID32());
    }
}
