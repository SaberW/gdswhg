package com.creatoo.hn.dao.mapper.aliyunsms;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class AliyunSmsProvider {

    public String selectAliySmsRefgc4Groupid(){
        SQL sql = new SQL();
        sql.SELECT("gc.*");
        sql.SELECT("c.tpcode ");
        sql.SELECT("c.tpname ");
        sql.SELECT("c.tpcontent ");
        sql.SELECT("c.tpdesc ");
        sql.FROM("whg_ywi_aliysms_refgc gc, whg_ywi_aliysms_code c ");
        sql.WHERE("gc.codeid = c.id ");
        sql.WHERE("gc.groupid = #{groupid} ");
        sql.ORDER_BY("gc.actpoint");

        return sql.toString();
    }

    public String selectAliySmsRefuseGroup(Map record){
        SQL sql = new SQL();

        sql.SELECT("ref.*");
        sql.SELECT("g.gptype");
        sql.SELECT("g.gpdesc");
        sql.SELECT("g.isdefault");

        sql.FROM("whg_ywi_aliysms_refusegrop ref, whg_ywi_aliysms_group g ");
        sql.WHERE("ref.groupid = g.id ");
        sql.WHERE("ref.entid = #{entid}");

        String enttype = (String) record.get("enttype");
        if (enttype != null && !enttype.isEmpty()){
            sql.WHERE("ref.groupid = #{enttype}");
        }

        return sql.toString();
    }
}
