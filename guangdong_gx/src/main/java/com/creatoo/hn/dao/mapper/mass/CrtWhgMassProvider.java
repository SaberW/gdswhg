package com.creatoo.hn.dao.mapper.mass;

import com.creatoo.hn.util.enums.EnumProject;
import com.creatoo.hn.util.enums.EnumTypeClazz;
import org.apache.ibatis.jdbc.SQL;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rbg on 2017/11/4.
 */
public class CrtWhgMassProvider {

    /**
     * 处理 tabalias.filed in (recode.mapkey[i]...)
     * @param sql
     * @param recode
     * @param filed
     * @param mapkey
     */
    private void inForeach(SQL sql, Map recode, String filed, String mapkey){
        List<String> items = (List<String>) recode.get(mapkey);
        if ( items!=null && items.size()>0 ){
            StringBuilder builder = new StringBuilder();
            builder.append(filed+" in (");

            MessageFormat mf = new MessageFormat("#'{'{0}[{1}]'}'");
            for (int i=0; i<items.size(); i++) {
                if (i>0) builder.append(",");
                builder.append(  mf.format(new Object[]{mapkey, i}) );
            }
            builder.append(")");
            sql.WHERE(builder.toString());
        }
    }

    /**
     * 处理查询 cultid 用 in
     * @param sql
     * @param tabalias
     * @param recode
     */
    /*private void setInCultid(SQL sql, String tabalias, Map recode){
        List<String> cultid = (List<String>) recode.get("cultid");
        if ( cultid!=null && cultid.size()>0 ){
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
    }*/

    /**
     * 设置排序
     * @param sql
     * @param params
     * @param tableAlias
     * @param defOrderStr
     */
    private void setOrderBy(SQL sql, Map params, String tableAlias, String defOrderStr){
        String sort = params.get("sort")!=null? params.get("sort").toString() : null;
        String order = params.get("order")!=null? params.get("order").toString() : null;
        if (sort != null && !sort.isEmpty()) {
            if (order != null && "desc".equalsIgnoreCase(order)) {
                order = "desc";
            }else{
                order = "asc";
            }

            StringBuffer sb = new StringBuffer();
            if (tableAlias != null && !tableAlias.isEmpty()) {
                sb.append(tableAlias);
                sb.append(".");
            }
            sb.append(sort+" "+order);

            sql.ORDER_BY( sb.toString() );
        }else{
            if (defOrderStr != null && !defOrderStr.isEmpty()) {
                sql.ORDER_BY( defOrderStr );
            }
        }
    }


    /**
     * 构建插入关联表的 values(?,?,?),(?,?,?)...
     * @param params
     * @param key
     * @param fields
     * @return
     */
    private StringBuilder makeValues(Map params, String key, List<String> fields){
        List<Map> refs = (List<Map>) params.get(key);
        StringBuilder values = new StringBuilder(" values ");
        if (refs != null && refs.size() > 0) {
            for (int i=0; i<refs.size(); i++) {
                Map info = refs.get(i);
                boolean isInser = true;
                for (String field : fields) {
                    if (!info.containsKey(field) || info.get(field) == null) {
                        isInser = false;
                        break;
                    }
                }

                if (isInser){
                    MessageFormat mf = new MessageFormat("#'{'{0}[{1}].{2}'}'");
                    if (i>0) values.append(",");
                    values.append("(");
                    for (int j=0; j<fields.size(); j++) {
                        if (j>0) values.append(",");
                        String pfm = mf.format(new Object[]{key, i, fields.get(j)});
                        values.append(pfm);
                    }
                    values.append(")");
                }
            }
        }

        return values;
    }


    /**
     * 查关联资讯
     * @param params
     * @return
     */
    public String selectRefzxList(Map params){
        params.put("project", "%"+EnumProject.PROJECT_QWZY.getValue()+"%");

        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与资讯表关联
        sql.FROM("whg_mass_ref_zx ref", "whg_inf_colinfo xxx", "whg_inf_column yyy");
        sql.WHERE("xxx.clnfid = ref.zxid");
        sql.WHERE("xxx.clnftype = yyy.colid");
        //限制资讯为群文系统的
        sql.WHERE("yyy.toproject like #{project} ");
        //限制资讯状态为发布的
        sql.WHERE("xxx.clnfstata = 6");

        //参数条件
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()){
            sql.WHERE("ref.mid = #{mid}");
        }
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("ref.mtype = #{mtype}");
        }
        if (params.get("clnvenueid") != null && !params.get("clnvenueid").toString().isEmpty()) {
            sql.WHERE("xxx.clnvenueid = #{clnvenueid}");
        }
        if (params.get("deptid") != null && !params.get("deptid").toString().isEmpty()) {
            sql.WHERE("xxx.deptid = #{deptid}");
        }
        if (params.get("clnftltle") != null && !params.get("clnftltle").toString().isEmpty()) {
            params.put("like_clnftltle", "%"+params.get("clnftltle")+"%");
            sql.WHERE("xxx.clnftltle like #{like_clnftltle}");
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.clnfcrttime desc");

        return sql.toString();
    }

    /**
     * 查可选资讯
     * @param params
     * @return
     */
    public String selectZxList4Mass(Map params){
        params.put("project", "%"+EnumProject.PROJECT_QWZY.getValue()+"%");

        SQL sql = new SQL();

        sql.SELECT("xxx.*");
        sql.FROM("whg_inf_colinfo xxx", "whg_inf_column yyy");
        sql.WHERE(" xxx.clnftype = yyy.colid ");
        //限制资讯为群文系统的
        sql.WHERE("yyy.toproject like #{project} ");
        //限制资讯状态为发布的
        sql.WHERE("xxx.clnfstata = 6");

        /*if (params.get("clnvenueid") != null && !params.get("clnvenueid").toString().isEmpty()) {
            sql.WHERE("xxx.clnvenueid = #{clnvenueid}");
        }*/
        if (params.get("clnvenueid") != null) {
            this.inForeach(sql,  params, "xxx.clnvenueid", "clnvenueid");
        }
        if (params.get("deptid") != null){
            this.inForeach(sql,  params, "xxx.deptid", "deptid");
        }

        if (params.get("clnftltle") != null && !params.get("clnftltle").toString().isEmpty()) {
            params.put("like_clnftltle", "%"+params.get("clnftltle")+"%");
            sql.WHERE("xxx.clnftltle like #{like_clnftltle}");
        }

        //排除与当前主体类型有关的
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("xxx.clnfid not in (");
            sb.append("select DISTINCT zxid from whg_mass_ref_zx refzx where refzx.mid= #{mid}");
            if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
                sb.append(" and refzx.mtype= #{mtype}");
            }
            sb.append(")");

            sql.WHERE(sb.toString());
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.clnfcrttime desc");

        return sql.toString();
    }



    /**
     * 插入资讯关联
     * @param params
     * @return
     */
    public String insertMassRefZxList(Map params){
        List<String> fields = Arrays.asList("mid", "mtype", "zxid");

        // insert into whg_mass_ref_zx (mid, mtype, zxid)
        SQL sql = new SQL();
        sql.INSERT_INTO("whg_mass_ref_zx");
        sql.INTO_COLUMNS((String[]) fields.toArray());

        // values (?,?,?),(?,?,?)...
        StringBuilder values = this.makeValues(params, "list", fields);

        return sql.toString()+values.toString();
    }

    /**
     * 删除资讯关联
     * @param params
     * @return
     */
    public String deleteMassRefZxList(Map params){

        List<String> zxids = (List<String>) params.get("zxids");
        MessageFormat mf = new MessageFormat("#'{'zxids[{0}]'}'");
        StringBuffer sb = new StringBuffer();
        sb.append(" zxid in (");
        for(int i = 0; i<zxids.size(); i++){
            if (i > 0) {
                sb.append(",");
            }
            sb.append( mf.format(new Object[]{i}) );
        }
        sb.append(" )");

        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_zx");

        sql.WHERE(sb.toString());
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }

    /**
     * 清理资讯关联
     * @param params
     * @return
     */
    public String deleteMassRefZx4Mid(Map params){
        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_zx");
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }


    /**
     * 查关联的艺术人才
     * @param params
     * @return
     */
    public String selectRefrcList(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_ref_rc ref", "whg_mass_artist xxx");
        sql.WHERE("xxx.id = ref.rcid");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        //参数条件
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()){
            sql.WHERE("ref.mid = #{mid}");
        }
        if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }
        if (params.get("deptid") != null && !params.get("deptid").toString().isEmpty()) {
            sql.WHERE("xxx.deptid = #{deptid}");
        }
        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            params.put("like_name", "%"+params.get("name")+"%");
            sql.WHERE("xxx.name like #{like_name}");
        }
        if (params.get("feattype") != null && !params.get("feattype").toString().isEmpty()) {
            params.put("like_feattype", "%"+params.get("feattype")+"%");
            sql.WHERE("xxx.feattype like #{like_feattype}");
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.crtdate desc");

        return sql.toString();
    }

    /**
     * 查可选的艺术人才
     * @param params
     * @return
     */
    public String selectRcList(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_artist xxx");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        //参数条件
        /*if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        if (params.get("cultid") != null){
            this.inForeach(sql,  params, "xxx.cultid", "cultid");
        }
        if (params.get("deptid") != null){
            this.inForeach(sql,  params, "xxx.deptid", "deptid");
        }

        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            params.put("like_name", "%"+params.get("name")+"%");
            sql.WHERE("xxx.name like #{like_name}");
        }
        if (params.get("feattype") != null && !params.get("feattype").toString().isEmpty()) {
            params.put("like_feattype", "%"+params.get("feattype")+"%");
            sql.WHERE("xxx.feattype like #{like_feattype}");
        }

        //排除与当前主体类型有关的
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("xxx.id not in (");
            sb.append("select DISTINCT rcid from whg_mass_ref_rc refrc where refrc.mid= #{mid}");
            sb.append(")");

            sql.WHERE(sb.toString());
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.crtdate desc");

        return sql.toString();
    }


    /**
     * 插入 艺术人才关联
     * @param params
     * @return
     */
    public String insertMassRefRcList(Map params){
        List<String> fields = Arrays.asList("mid", "mtype", "rcid");

        // insert into whg_mass_ref_rc (mid, mtype, rcid)
        SQL sql = new SQL();
        sql.INSERT_INTO("whg_mass_ref_rc");
        sql.INTO_COLUMNS((String[]) fields.toArray());

        // values (?,?,?),(?,?,?)...
        StringBuilder values = this.makeValues(params, "list", fields);

        return sql.toString()+values.toString();
    }



    /**
     * 删除艺术人才关联
     * @param params
     * @return
     */
    public String deleteMassRefRcList(Map params){

        List<String> rcids = (List<String>) params.get("rcids");
        MessageFormat mf = new MessageFormat("#'{'rcids[{0}]'}'");
        StringBuffer sb = new StringBuffer();
        sb.append(" rcid in (");
        for(int i = 0; i<rcids.size(); i++){
            if (i > 0) { sb.append(","); }
            sb.append( mf.format(new Object[]{i}) );
        }
        sb.append(" )");

        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_rc");

        sql.WHERE(sb.toString());
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }

    /**
     * 清理人才关联
     * @param params
     * @return
     */
    public String deleteMassRefRc4Mid(Map params){
        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_rc");
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }




    /**
     * 查关联的艺术团队
     * @param params
     * @return
     */
    public String selectReftdList(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_ref_td ref", "whg_mass_team xxx");
        sql.WHERE("xxx.id = ref.tdid");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        //参数条件
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()){
            sql.WHERE("ref.mid = #{mid}");
        }
        if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }
        if (params.get("deptid") != null && !params.get("deptid").toString().isEmpty()) {
            sql.WHERE("xxx.deptid = #{deptid}");
        }
        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            params.put("like_name", "%"+params.get("name")+"%");
            sql.WHERE("xxx.name like #{like_name}");
        }
        if (params.get("feattype") != null && !params.get("feattype").toString().isEmpty()) {
            params.put("like_feattype", "%"+params.get("feattype")+"%");
            sql.WHERE("xxx.feattype like #{like_feattype}");
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.crtdate desc");

        return sql.toString();
    }

    /**
     * 查可选的艺术团队
     * @param params
     * @return
     */
    public String selectTdList(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_team xxx");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        //参数条件
        /*if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        if (params.get("cultid") != null){
            this.inForeach(sql, params, "xxx.cultid", "cultid");
        }
        if (params.get("deptid") != null){
            this.inForeach(sql, params, "xxx.deptid", "deptid");
        }

        if (params.get("name") != null && !params.get("name").toString().isEmpty()) {
            params.put("like_name", "%"+params.get("name")+"%");
            sql.WHERE("xxx.name like #{like_name}");
        }
        if (params.get("feattype") != null && !params.get("feattype").toString().isEmpty()) {
            params.put("like_feattype", "%"+params.get("feattype")+"%");
            sql.WHERE("xxx.feattype like #{like_feattype}");
        }

        //排除与当前主体类型有关的
        if (params.get("mid") != null && !params.get("mid").toString().isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("xxx.id not in (");
            sb.append("select DISTINCT tdid from whg_mass_ref_td refrc where refrc.mid= #{mid}");
            sb.append(")");

            sql.WHERE(sb.toString());
        }

        //排序
        this.setOrderBy(sql, params, null, "xxx.crtdate desc");

        return sql.toString();
    }


    /**
     * 插入 艺术团队关联
     * @param params
     * @return
     */
    public String insertMassRefTdList(Map params){
        List<String> fields = Arrays.asList("mid", "mtype", "tdid");

        // insert into whg_mass_ref_td (mid, mtype, tdid)
        SQL sql = new SQL();
        sql.INSERT_INTO("whg_mass_ref_td");
        sql.INTO_COLUMNS((String[]) fields.toArray());

        // values (?,?,?),(?,?,?)...
        StringBuilder values = this.makeValues(params, "list", fields);

        return sql.toString()+values.toString();
    }

    /**
     * 删除艺术团队关联
     * @param params
     * @return
     */
    public String deleteMassRefTdList(Map params){

        List<String> rcids = (List<String>) params.get("tdids");
        MessageFormat mf = new MessageFormat("#'{'tdids[{0}]'}'");
        StringBuffer sb = new StringBuffer();
        sb.append(" tdid in (");
        for(int i = 0; i<rcids.size(); i++){
            if (i > 0) { sb.append(","); }
            sb.append( mf.format(new Object[]{i}) );
        }
        sb.append(" )");

        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_td");

        sql.WHERE(sb.toString());
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }

    /**
     * 清理团队关联
     * @param params
     * @return
     */
    public String deleteMassRefTd4Mid(Map params){
        SQL sql = new SQL();
        sql.DELETE_FROM("whg_mass_ref_td");
        sql.WHERE("mid = #{mid}");
        if (params.get("mtype") != null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("mtype = #{mtype}");
        }

        return sql.toString();
    }




    //TODO API Methods

    /**
     * api 查询关联资讯
     * @param params
     * @return
     */
    public String apiSelectMassZxlist(Map params){
        params.put("project", "%"+EnumProject.PROJECT_QWZY.getValue()+"%");

        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与资讯表关联
        sql.FROM("whg_mass_ref_zx ref", "whg_inf_colinfo xxx", "whg_inf_column yyy ");
        sql.WHERE("xxx.clnfid = ref.zxid");
        sql.WHERE("xxx.clnftype = yyy.colid");
        //限制资讯为群文系统的
        sql.WHERE("yyy.toproject like #{project} ");
        //限制资讯状态为发布的
        sql.WHERE("xxx.clnfstata = 6");

        if (params.get("mid")!=null && !params.get("mid").toString().isEmpty()) {
            sql.WHERE("ref.mid = #{mid}");
        }
        if (params.get("mtype")!=null && !params.get("mtype").toString().isEmpty()) {
            sql.WHERE("ref.mtype = #{mtype}");
        }
        if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.clnvenueid = #{cultid}");
        }

        //分组去重
        sql.GROUP_BY("xxx.clnfid");

        //排序
        sql.ORDER_BY("xxx.clnfghp desc, xxx.clnfidx asc, xxx.clnfcrttime desc");

        return sql.toString();
    }

    /**
     * api 查询届次的关联艺术人才
     * @param params
     * @return
     */
    public String apiSelectBatchRclist(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_ref_rc ref", "whg_mass_artist xxx");
        sql.WHERE("xxx.id = ref.rcid");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        sql.WHERE("ref.mid = #{batchid}");

        //分组去重
        sql.GROUP_BY("xxx.id");

        //排序
        sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");

        return sql.toString();
    }

    /**
     * api 查询届次的关联艺术团队
     * @param params
     * @return
     */
    public String apiSelectBatchTdlist(Map params){
        SQL sql = new SQL();
        sql.SELECT("xxx.*");
        //关系表与人才表关联
        sql.FROM("whg_mass_ref_td ref", "whg_mass_team xxx");
        sql.WHERE("xxx.id = ref.tdid");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        sql.WHERE("ref.mid = #{batchid}");

        //分组去重
        sql.GROUP_BY("xxx.id");

        //排序
        sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");

        return sql.toString();
    }

    /**
     * api 查询艺术人才列表
     * @param params
     * @return
     */
    public String apiSelectMassArtistList(Map params){
        SQL sql = new SQL();

        sql.SELECT("xxx.*");
        sql.FROM("whg_mass_artist xxx");
        //状态为发布的
        sql.WHERE("xxx.state = 6 and xxx.delstate = 0");

        /*if (params.get("cultid") != null && !params.get("cultid").toString().isEmpty()) {
            sql.WHERE("xxx.cultid = #{cultid}");
        }*/
        //this.setInCultid(sql, "xxx", params);
        if (params.get("cultid") != null ){
            this.inForeach(sql, params, "xxx.cultid", "cultid");
        }
        if (params.get("deptid") != null){
            this.inForeach(sql, params, "xxx.deptid", "deptid");
        }

        if (params.get("province") != null && !params.get("province").toString().isEmpty()) {
            sql.WHERE("xxx.province = #{province}");
        }
        if (params.get("city") != null && !params.get("city").toString().isEmpty()) {
            sql.WHERE("xxx.city = #{city}");
        }
        if (params.get("area") != null && !params.get("area").toString().isEmpty()) {
            sql.WHERE("xxx.area = #{area}");
        }
        if (params.get("years") != null && !params.get("years").toString().isEmpty()) {
            sql.WHERE("xxx.years = #{years}");
        }

        if (params.get("feattype") != null && !params.get("feattype").toString().isEmpty()) {
            params.put("feattype", "%"+params.get("feattype")+"%");
            sql.WHERE("xxx.feattype like #{feattype}");
        }

        if (params.get("content") != null && !params.get("content").toString().isEmpty()) {
            params.put("content", "%"+params.get("content")+"%");
            sql.WHERE(" ( xxx.name like #{content} or xxx.summary like #{content} ) ");
        }

        //排序
        sql.ORDER_BY("xxx.recommend desc, xxx.statemdfdate desc");

        return sql.toString();
    }


    /**
     * api 群文收藏查询
     * @param params
     * @return
     */
    public String apiSelectMassCollections(Map params){
//        List etclst = Arrays.asList(EnumTypeClazz.TYPE_MASS_ARTIST.getValue(), EnumTypeClazz.TYPE_MASS_RESOURCE);
//        String cmreftyp = params.get("cmreftyp").toString();

        SQL sql = new SQL();
        sql.SELECT("xxx.*");
//        if (cmreftyp != null && etclst.contains(cmreftyp)) {
//            if (cmreftyp.equals(EnumTypeClazz.TYPE_MASS_ARTIST.getValue())){
//                sql.SELECT("yyy.imgurl as imgurl");
//            }else{
//
//            }
//        }
        sql.FROM("whg_collection xxx");

//        if (cmreftyp != null && etclst.contains(cmreftyp)) {
//            if (cmreftyp.equals(EnumTypeClazz.TYPE_MASS_ARTIST.getValue())){
//                sql.FROM("whg_mass_artist as yyy");
//                sql.WHERE("xxx.cmrefid = yyy.id and yyy.state=6 and yyy.delstate=0");
//            }else{
//                /*sql.FROM("whg_mass_artist as yyy");
//                sql.WHERE("xxx.cmrefid = yyy.id");*/
//            }
//        }

        sql.WHERE("xxx.systype = 'QWZY' ");
        sql.WHERE("xxx.cmuid = #{userid} ");
        sql.WHERE("xxx.cmreftyp = #{cmreftyp} ");
        sql.ORDER_BY("xxx.cmdate desc");

        return sql.toString();
    }
}
