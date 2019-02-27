package com.creatoo.hn.dao.mapper.api.train;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class KaoqinTraProvider {

    public String selectTraKaoqin(Map record){
        String traid = (String)record.get("traid");
        String userid = (String)record.get("userid");
        String yyyy = (String)record.get("yyyy");
        String mm = (String)record.get("mm");

        SQL sql = new SQL();

        sql.SELECT("tra.id",
                "tra.title",
                "tc.title AS coruseTitle",
                "tc.starttime",
                "tc.endtime",
                //"tec.sign",
                //"tec.signtime",
                //"tlv.state AS traleaveState",
                "(select sign from  whg_tra_enrol_course t where t.traid = tra.id and t.courseid = tc.id and t.userid = u.id) as sign",
                "(select state from  whg_traleave t where t.traid = tra.id and t.courseid = tc.id and t.userid = u.id) as traleaveState",
                "te.userid",
                "te.realname enrolName",
                "u.phone",
                "u.`name`",
                "u.nickname",
                "tc.id AS coruseId",
                "te.id AS enrolId" //,
                //"tlv.id AS traleaveId"
        );
        sql.FROM("whg_tra tra");
        sql.INNER_JOIN("whg_tra_course tc ON tra.id = tc.traid");
        sql.LEFT_OUTER_JOIN("whg_tra_enrol te ON te.traid = tc.traid");
        sql.LEFT_OUTER_JOIN("whg_user u ON u.id = te.userid");
        //sql.LEFT_OUTER_JOIN("whg_tra_enrol_course tec ON tec.courseid = tc.id");
        //sql.LEFT_OUTER_JOIN("whg_traleave tlv ON tlv.courseid = tc.id");
        //sql.LEFT_OUTER_JOIN("whg_tra_enrol_course tec ON tec.userid = u.id");
        //sql.LEFT_OUTER_JOIN("whg_traleave tlv ON tlv.userid = u.id");
        sql.WHERE(
                "tra.state = 6",
                "tra.delstate = 0",
                "tc.state = 1",
                "tc.delstate = 0",
                "te.state = 6"
                );

        if (traid!=null && !traid.isEmpty()){
            sql.WHERE("tra.id = #{traid}");
        }
        if (userid!=null && !userid.isEmpty()){
            sql.WHERE("te.userid = #{userid}");
        }
        if (yyyy!=null && !yyyy.isEmpty()){
            sql.WHERE("DATE_FORMAT(tc.starttime, '%Y') = #{yyyy}");
        }
        if (mm!=null && !mm.isEmpty()){
            sql.WHERE("DATE_FORMAT(tc.starttime, '%m') = #{mm}");
        }


        sql.ORDER_BY("tra.id, tc.starttime");

        //System.out.println("sql=====>>>"+sql.toString());
        return sql.toString();
    }
}
