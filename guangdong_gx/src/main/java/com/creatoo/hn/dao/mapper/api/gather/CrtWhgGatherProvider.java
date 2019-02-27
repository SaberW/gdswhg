package com.creatoo.hn.dao.mapper.api.gather;

import com.creatoo.hn.util.enums.EnumTypeClazz;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/9/26.
 */
public class CrtWhgGatherProvider {


    private void setInCultid(SQL sql, String tabalias, Map recode){
        List<String> cultid = (List<String>) recode.get("cultid");
        if (cultid!=null && cultid.size()>0){
            StringBuilder incultid = new StringBuilder();

            if (tabalias!= null && !tabalias.isEmpty()){
                incultid.append(tabalias+".");
            }
            incultid.append("cultid in (");

            MessageFormat mf = new MessageFormat("#'{'cultid[{0}]'}'");
            for (int i=0; i<cultid.size(); i++) {
                if (i>0) incultid.append(",");
                incultid.append(  mf.format(new Object[]{i}) );
            }
            incultid.append(")");
            sql.WHERE(incultid.toString());
        }

        List<String> deptid = (List<String>) recode.get("deptid");
        if (deptid!=null && deptid.size()>0){
            StringBuilder incultid = new StringBuilder();

            if (tabalias!= null && !tabalias.isEmpty()){
                incultid.append(tabalias+".");
            }
            incultid.append("deptid in (");

            MessageFormat mf = new MessageFormat("#'{'deptid[{0}]'}'");
            for (int i=0; i<deptid.size(); i++) {
                if (i>0) incultid.append(",");
                incultid.append(  mf.format(new Object[]{i}) );
            }
            incultid.append(")");
            sql.WHERE(incultid.toString());
        }
    }


    /**
     * 查询品牌列表
     * @param recode
     * @return
     */
    public String selectBrands(Map<String,Object> recode){
        //String cultid = (String) recode.get("cultid");

        String province = (String) recode.get("province");
        String city = (String) recode.get("city");
        String area = (String) recode.get("area");

        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("whg_gather_brand xxx");
        sql.WHERE("xxx.state=6 and xxx.delstate=0");

        this.setInCultid(sql, "xxx", recode);

        if (province != null && !province.isEmpty()) {
            sql.WHERE("xxx.province = #{province}");
        }
        if (city != null && !city.isEmpty()) {
            sql.WHERE("xxx.city = #{city}");
        }
        if (area != null && !area.isEmpty()) {
            sql.WHERE("xxx.area = #{area}");
        }
        sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");

        return sql.toString();

//        return new SQL(){{
//            SELECT("*");
//            FROM("whg_gather_brand xxx");
//            WHERE("xxx.state=6 and xxx.delstate=0");
//            /*if (cultid!=null && !cultid.isEmpty()){
//                WHERE("xxx.cultid = #{cultid}");
//            }*/
//            List<String> cultid = (List<String>) recode.get("cultid");
//            if (cultid!=null && cultid.size()>0){
//                StringBuilder incultid = new StringBuilder("xxx.cultid in (");
//                MessageFormat mf = new MessageFormat("#'{'cultid[{0}]'}'");
//                for (int i=0; i<cultid.size(); i++) {
//                    if (i>0) incultid.append(",");
//                    incultid.append(  mf.format(new Object[]{i}) );
//                }
//                incultid.append(")");
//                WHERE(incultid.toString());
//            }
//
//            List<String> deptid = (List<String>)recode.get("deptid");
//            if (deptid!=null && deptid.size()>0){
//                StringBuilder incultid = new StringBuilder("xxx.deptid in (");
//                MessageFormat mf = new MessageFormat("#'{'deptid[{0}]'}'");
//                for (int i=0; i<deptid.size(); i++) {
//                    if (i>0) incultid.append(",");
//                    incultid.append(  mf.format(new Object[]{i}) );
//                }
//                incultid.append(")");
//                WHERE(incultid.toString());
//            }
//
//            if (province != null && !province.isEmpty()) {
//               WHERE("xxx.province = #{province}");
//            }
//            if (city != null && !city.isEmpty()) {
//               WHERE("xxx.city = #{city}");
//            }
//            if (area != null && !area.isEmpty()) {
//                WHERE("xxx.area = #{area}");
//            }
//            ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");
//        }}.toString();
    }

    /**
     * 查询指定品牌 SQL
     * @return
     */
    public String findBrand(){
        return new SQL(){{
            SELECT("*");
            FROM("whg_gather_brand xxx");
            WHERE("xxx.id=#{id}");
        }}.toString();
    }

    /**
     * 查询品牌的项目列表 SQL
     * @param recode
     * @return
     */
    public String selectBrandGathers(Map<String, Object> recode){
        //String cultid = (String) recode.get("cultid");
        //String brandid = (String) recode.get("brandid");
        String filter = (String) recode.get("filter");

        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("whg_gather xxx");
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");
        sql.WHERE("xxx.brandid = #{brandid}");
        /*if (cultid != null && !cultid.isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        this.setInCultid(sql, "xxx", recode);

        // filter = 1 查历史
        if (filter != null && "1".equals(filter)) {
            Date now = new Date();
            recode.put("now", now);
            sql.WHERE("xxx.timeend < #{now}");
        }

        sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");

        return sql.toString();
    }

    /**
     * 查询品牌的资源信息列表 SQL
     * @param recode
     * @return
     */
    public String selectBrandResources(Map recode){
        recode.put("reftype", EnumTypeClazz.TYPE_GAT.getValue());

        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("whg_resource xxx");
        sql.WHERE("xxx.reftype = #{reftype}");

        sql.WHERE("xxx.refid in (SELECT ga.id FROM whg_gather ga WHERE ga.brandid = #{refid} AND ga.state = 6 )");
        //sql.WHERE("xxx.enttype in ('1','2','3')");
        String enttype = (String) recode.get("enttype");
        if (enttype != null && !enttype.isEmpty()) {
            sql.WHERE("xxx.enttype = #{enttype}");
        }
        String name = (String) recode.get("name");
        if (name != null && !name.isEmpty()){
            recode.put("name", "%"+name+"%");
            sql.WHERE("xxx.name like #{name}");
        }
        sql.ORDER_BY("xxx.crtdate desc");
        return sql.toString();
    }


    public String selectGatherResources(Map recode){
        recode.put("reftype", EnumTypeClazz.TYPE_GAT.getValue());

        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("whg_resource xxx");
        sql.WHERE("xxx.reftype = #{reftype}");

        sql.WHERE("xxx.refid = #{refid}");
        //sql.WHERE("xxx.enttype in ('1','2','3')");
        String enttype = (String) recode.get("enttype");
        if (enttype != null && !enttype.isEmpty()) {
            sql.WHERE("xxx.enttype = #{enttype}");
        }
        sql.ORDER_BY("xxx.crtdate desc");

        return sql.toString();
    }


    /**
     * 查询众筹项目列表 SQL
     * @param recode
     * @return
     */
    public String selectGathers(Map<String,Object> recode){
        //String cultid = (String) recode.get("cultid");
        String province = (String) recode.get("province");
        String city = (String) recode.get("city");
        String area = (String) recode.get("area");
        String etype = (String) recode.get("etype");
        String jd = (String) recode.get("jd");
        String content = (String) recode.get("content");
        String px = (String) recode.get("px");

        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("whg_gather xxx");
        sql.WHERE("xxx.state = 6 and xxx.delstate=0");

        /*if (cultid != null && !cultid.isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        this.setInCultid(sql, "xxx", recode);

        if (province != null && !province.isEmpty()) {
            sql.WHERE("xxx.province = #{province}");
        }
        if (city != null && !city.isEmpty()) {
            sql.WHERE("xxx.city = #{city}");
        }
        if (area != null && !area.isEmpty()) {
            sql.WHERE("xxx.area = #{area}");
        }
        if (etype != null && !etype.isEmpty()) {
            sql.WHERE("xxx.etype = #{etype}");
        }
        if (content != null && !content.isEmpty()) {
            recode.put("content", "%"+content+"%");
            sql.WHERE("xxx.title like #{content}");
        }

        if (jd != null){
            recode.put("now", new Date());
            switch (jd){
                //即将开始
                case "1":
                    sql.WHERE("xxx.timestart > #{now}");
                    break;
                //进行中
                case "2":
                    sql.WHERE("xxx.timestart < #{now} and xxx.timeend > #{now}");
                    break;
                //众筹结束
                case "3":
                    sql.WHERE("xxx.timeend < #{now}");
                    break;
                //众筹成功
                case "4":
                    sql.WHERE("xxx.issuccess = 1");
                    break;
            }
        }


        if (px != null && "1".equals(px)) { //最新排序
            sql.ORDER_BY("xxx.statemdfdate desc");
        }else { //推荐排序
            sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");
        }


        return sql.toString();
    }

    /**
     * 查用户订单相关的众筹列表 SQL
     * @param recode
     * @return
     */
    public String selectUserOrderGathers(Map recode){
        SQL sql = new SQL();

        sql.SELECT("*");
        StringBuilder fromSB = new StringBuilder();
        fromSB.append("(");
        fromSB.append(" select gat.etype, gat.refid, od.userid as ouserid, od.gatherid as gid, od.id as oid, od.orderid as orderid, od.crtdate as otime, od.state as ostate from whg_gather gat, whg_gather_order od  where gat.id = od.gatherid ");
        fromSB.append(" UNION");
        fromSB.append(" select gat.etype, gat.refid, od.userid as ouserid, gat.id as gid, od.id as oid, od.orderid as orderid, od.crttime as otime, od.state as ostate  from whg_gather gat, whg_tra_enrol od where gat.etype = '5' and gat.refid = od.traid ");
        fromSB.append(" UNION");
        fromSB.append(" select gat.etype, gat.refid, od.userid as ouserid, gat.id as gid, od.id as oid, od.ordernumber as orderid, od.ordercreatetime as otime, od.ticketstatus as ostate from whg_gather gat, whg_act_order od where gat.etype = '4' and gat.refid = od.activityid and od.delstate =0 ");
        fromSB.append(") temporder, whg_gather xxx");
        sql.FROM(fromSB.toString());

        sql.WHERE("temporder.gid = xxx.id and temporder.ouserid = #{userid}");

        String filter = (String) recode.get("filter");
        if (filter != null) {
            recode.put("now", new Date());
            switch (filter){
                //进行中
                case "1":
                    sql.WHERE("xxx.timestart < #{now} and xxx.timeend > #{now}");
                    break;
                //众筹成功
                case "2":
                    sql.WHERE("xxx.issuccess = 1");
                    break;
                //众筹失败
                case "3":
                    sql.WHERE("xxx.issuccess = 2");
                    break;
                //历史
                case "4":
                    sql.WHERE("xxx.timeend <= #{now}");
                    break;
            }
        }

        sql.ORDER_BY("temporder.otime desc");

        return sql.toString();
    }


    /**
     * 查用户收藏表相应的众筹列表 SQL
     * @param recode
     * @return
     */
    public String selectGathers4UserColle(Map recode){
        //String cultid = (String) recode.get("cultid");
        String province = (String) recode.get("province");
        String city = (String) recode.get("city");
        String area = (String) recode.get("area");
        String etype = (String) recode.get("etype");

        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        sql.SELECT("yyy.cmurl");
        sql.FROM("whg_gather xxx, whg_collection yyy");
        sql.WHERE("xxx.id = yyy.cmrefid and yyy.cmopttyp = '0' and yyy.cmreftyp='28'");
        sql.WHERE("xxx.state = 6 and xxx.delstate=0");
        sql.WHERE("yyy.cmuid = #{userid}");

        /*if (cultid != null && !cultid.isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        this.setInCultid(sql, "xxx", recode);

        if (province != null && !province.isEmpty()) {
            sql.WHERE("xxx.province = #{province}");
        }
        if (city != null && !city.isEmpty()) {
            sql.WHERE("xxx.city = #{city}");
        }
        if (area != null && !area.isEmpty()) {
            sql.WHERE("xxx.area = #{area}");
        }
        if (etype != null && !etype.isEmpty()) {
            sql.WHERE("xxx.etype = #{etype}");
        }

        sql.ORDER_BY("yyy.cmdate desc");

        return sql.toString();
    }


    /**
     * 其它众筹订单详情查询
     * @return
     */
    public String findGatherOrder(){
        SQL sql = new SQL();

        sql.SELECT("xxx.*");
        sql.SELECT("yyy.title gathertitle");
        sql.SELECT("zzz.name username, zzz.nickname usernickname");
        sql.FROM("whg_gather_order xxx, whg_gather yyy, whg_user zzz");
        sql.WHERE("yyy.id = xxx.gatherid and xxx.userid = zzz.id");
        sql.WHERE("xxx.id = #{id}");

        return sql.toString();
    }

}
