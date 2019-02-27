package com.creatoo.hn.services.comm;

import com.creatoo.hn.dao.mapper.WhgVisitDataMapper;
import com.creatoo.hn.dao.model.WhgVisitData;
import com.creatoo.hn.util.IDUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.java2d.pipe.ValidatePipe;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangxl on 2017/6/19.
 */
@Service
public class VisitService {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * Visit Data DAO
     */
    @Autowired
    private WhgVisitDataMapper whgVisitDataMapper;

    public long getVisitCount4Url(String urlfag) throws Exception{
        if (urlfag==null || urlfag.isEmpty()){
            return 0;
        }
        String purlfag = "%"+urlfag+"%";
        BigDecimal sumcount = this.whgVisitDataMapper.selectSumCount4Url(purlfag);
        if (sumcount==null){
            return 0;
        }
        long count = sumcount.longValue();
        return count;
    }

    /**
     * 保存浏览记录
     * @param vType 访问类型(1:PC,2:weixin,3:Android,4:IOS)
     * @param vDate 访问日期(yyyy-MM-dd)
     * @param vIp 访问IP(127.0.0.1)
     * @param visitor 访客(访客标识)
     * @param vPage 访问页面(url)
     * @param vCount 访问量
     * @throws Exception
     */
    public void saveVisit(String vType, String vDate, String vIp, String visitor, String vPage, String vCount)throws  Exception{
        int vCnt = 1;
        int vTyp = 1;
        try{
            vCnt = Integer.parseInt(vCount);
            vTyp = Integer.parseInt(vType);
        }catch(Exception e){}

        Example example = new Example(WhgVisitData.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("visitType", vTyp);
        c.andEqualTo("visitDate", vDate);
        c.andEqualTo("visitIp", vIp);
        c.andEqualTo("visitUser", visitor);
        c.andEqualTo("visitPage", vPage);

        int rows = this.whgVisitDataMapper.selectCountByExample(example);
        if(rows > 0){
            WhgVisitData wvd = new WhgVisitData();
            wvd.setVisitCount(vCnt);
            wvd.setVisitTime(new Date());
            this.whgVisitDataMapper.updateByExampleSelective(wvd, example);
        }else{
            WhgVisitData wvd = new WhgVisitData();
            wvd.setVisitId(IDUtils.getID32());
            wvd.setVisitType(vTyp);
            wvd.setVisitIp(vIp);
            wvd.setVisitPage(vPage);
            wvd.setVisitCount(vCnt);
            wvd.setVisitDate(vDate);
            wvd.setVisitTime(new Date());
            wvd.setVisitUser(visitor);

            //页面类型（资讯，活动，场馆，培训）， ID
            Entity entity = parseEntity(vPage);
            int vetype = entity.getvEtype();
            String veid = entity.getvEid();
            wvd.setVisitEtype(vetype);
            wvd.setVisitEid(veid);

            this.whgVisitDataMapper.insert(wvd);
        }
    }

    class Entity {
        private int vEtype = 0;
        private String vEid = "";

        public int getvEtype() {
            return vEtype;
        }

        public void setvEtype(int vEtype) {
            this.vEtype = vEtype;
        }

        public String getvEid() {
            return vEid;
        }

        public void setvEid(String vEid) {
            this.vEid = vEid;
        }
    }

    /**
     * 根据访问连接解析是否是活动/场馆/培训/资讯类型及其ID
     * @param vPage 访问连接地址
     * @return Entity.vEtype: 1-活动, 2-培训, 3-场馆, 4-活动室, 5-资讯, 9-其它 Entity.vEid：对象ID
     */
    private Entity parseEntity(String vPage){
        int vEtype = 9;//表示没有解析到ID属性的连接
        String vEid = "";

        //解析ID属性 - 根据whg_visit_data.visit_page完善解析ID算法
        if(StringUtils.isNotEmpty(vPage)){
            Map<String, String> paramMap = new HashMap<>();
            String[] queryParam = vPage.split("\\?");
            if(queryParam != null && queryParam.length > 1){
                String queryString = queryParam[1];
                String[] paramArr = queryString.split("&");
                for(String param : paramArr){
                    String[] pArr = param.split("=");
                    paramMap.put(pArr[0], pArr[1]);
                }
            }
            if(paramMap.containsKey("id")){
                vEtype = 8;
                vEid = paramMap.get("id");
            }
            if(StringUtils.isEmpty(vEid)){
                vEid = paramMap.get("resid");
            }
        }
        Entity entity = new Entity();
        entity.setvEtype(vEtype);
        entity.setvEid(vEid);
        return entity;
    }

    public static void main(String[] args) {
        Date now = new Date();
        System.out.println(new SimpleDateFormat("yyyy-MM-dd("+new SimpleDateFormat("EEEE").format(now)+")ahh:mm").format(now));

        String vPage = "http://www.baiud.com/query?id=asdf&saf=3wf&fwf=alkfdsj";
        if(StringUtils.isNotEmpty(vPage)){
            String[] queryParam = vPage.split("\\?");
            if(queryParam != null && queryParam.length > 1){
                String queryString = queryParam[1];
                String[] paramArr = queryString.split("&");
                for(String param : paramArr){
                    String[] pArr = param.split("=");
                    System.out.println( pArr[0]+"=>"+pArr[1] );
                }
            }
        }
    }
}
