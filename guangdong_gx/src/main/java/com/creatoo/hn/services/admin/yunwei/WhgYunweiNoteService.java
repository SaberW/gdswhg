package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiNoteMapper;
import com.creatoo.hn.dao.model.WhgYwiNote;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统操作日志服务类
 * Created by wangxl on 2017/4/25.
 */
@Service
public class WhgYunweiNoteService {
    /**
     * 日志
     */
    Logger log = Logger.getLogger(this.getClass().getName());

    /**
     * 操作日志DAO
     */
    @Autowired
    private WhgYwiNoteMapper whgYwiNoteMapper;

    /**
     * 分页查询日志
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @param stime
     * @param etime
     * @param note
     * @return
     * @throws Exception
     */
    public PageInfo<WhgYwiNote> t_srchList4p(int page, int rows, String sort, String order, String stime, String etime, WhgYwiNote note) throws Exception {
        //搜索条件
        Example example = new Example(WhgYwiNote.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (note != null && note.getAdminaccount() != null) {
            c.andLike("adminaccount", "%" + note.getAdminaccount() + "%");
            note.setAdminaccount(null);
        }
        if (note != null && note.getOptargs() != null) {
            c.andLike("optargs", "%" + note.getOptargs() + "%");
            note.setOptargs(null);
        }
        if (note != null && note.getOptdesc() != null) {
            c.andLike("optdesc", "%" + note.getOptdesc() + "%");
            note.setOptdesc(null);
        }
        if (note != null && note.getCultid() != null) {
            c.andEqualTo("cultid", note.getCultid());
            note.setCultid(null);
        }
        if(stime != null || etime != null){
            Date sdate = null;
            Date edate = null;
            try{sdate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stime);}catch(Exception e){}
            try{edate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(etime);}catch(Exception e){}
            if(sdate != null && edate != null){
                c.andBetween("opttime", sdate, edate);
            }else if(sdate != null){
                c.andGreaterThanOrEqualTo("opttime", sdate);
            }else if(edate != null){
                c.andLessThanOrEqualTo("opttime", edate);
            }
        }

        //其它条件
        c.andEqualTo(note);

        //排序
        example.setOrderByClause("opttime desc");

        //分页查询
        PageHelper.startPage(page, rows);
        List<WhgYwiNote> list = this.whgYwiNoteMapper.selectByExample(example);
        return new PageInfo<WhgYwiNote>(list);
    }

    /**
     * 查询列表数据
     * @param sort
     * @param order
     * @param stime
     * @param etime
     * @param note
     * @return
     * @throws Exception
     */
    public List<WhgYwiNote> t_srchList(String sort, String order, String stime, String etime, WhgYwiNote note) throws Exception {
        //搜索条件
        Example example = new Example(WhgYwiNote.class);
        Example.Criteria c = example.createCriteria();

        //名称条件
        if (note != null && note.getAdminaccount() != null) {
            c.andLike("adminaccount", "%" + note.getAdminaccount() + "%");
            note.setAdminaccount(null);
        }
        if (note != null && note.getOptargs() != null) {
            c.andLike("optargs", "%" + note.getOptargs() + "%");
            note.setOptargs(null);
        }
        if (note != null && note.getOptdesc() != null) {
            c.andLike("optdesc", "%" + note.getOptdesc() + "%");
            note.setOptdesc(null);
        }
        if (note != null && note.getCultid() != null) {
            c.andEqualTo("cultid", note.getCultid());
            note.setCultid(null);
        }
        if(stime != null || etime != null){
            Date sdate = null;
            Date edate = null;
            try{sdate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stime);}catch(Exception e){}
            try{edate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(etime);}catch(Exception e){}
            if(sdate != null && edate != null){
                c.andBetween("opttime", sdate, edate);
            }else if(sdate != null){
                c.andGreaterThanOrEqualTo("opttime", sdate);
            }else if(edate != null){
                c.andLessThanOrEqualTo("opttime", edate);
            }
        }

        //其它条件
        c.andEqualTo(note);

        //排序
        example.setOrderByClause("opttime desc");

        //查询
        List<WhgYwiNote> list = this.whgYwiNoteMapper.selectByExample(example);
        return list;
    }

    /**
     * 查询操作日志详情
     * @param id 操作日志ID
     * @return
     * @throws Exception
     */
    public WhgYwiNote t_srchOne(String id)throws Exception{
        return this.whgYwiNoteMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存操作日志
     * @param note
     * @throws Exception
     */
    public void t_saveNote(WhgYwiNote note) throws Exception{
        if (note!=null){
            note.setId(IDUtils.getID());
            this.whgYwiNoteMapper.insert(note);
        }
    }

    /**
     * 导出操作日志到客户端
     * @param out
     * @param sort
     * @param order
     * @param stime
     * @param etime
     * @param note
     * @throws Exception
     */
    public void exportExcel(ServletOutputStream out, String sort, String order, String stime, String etime, WhgYwiNote note, String cookieName, HttpServletResponse response)throws Exception{
        try {
            // 第一步，创建一个workbook，对应一个Excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            HSSFSheet hssfSheet = workbook.createSheet("操作日志");
            // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow hssfRow = hssfSheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle hssfCellStyle = workbook.createCellStyle();
            //居中样式
            hssfCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            hssfSheet.setColumnWidth(0, 20*256);
            hssfSheet.setColumnWidth(1, 30*256);
            hssfSheet.setColumnWidth(2, 50*256);
            hssfSheet.setColumnWidth(3, 25*256);

            //设置标题
            String[] titles = new String[]{"操作人", "操作对象", "操作说明", "操作时间"};
            HSSFCell hssfCell = null;
            for (int i = 0; i < titles.length; i++) {
                hssfCell = hssfRow.createCell(i);//列索引从0开始
                hssfCell.setCellValue(titles[i]);//列名1
                hssfCell.setCellStyle(hssfCellStyle);//列居中显示
            }

            //操作对象
            Map<String, String> optObj = optObj();

            // 第五步，写入实体数据
            List<WhgYwiNote> notes = this.t_srchList(sort, order, stime, etime, note);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (notes != null && !notes.isEmpty()) {
                for (int i = 0; i < notes.size(); i++) {
                    hssfRow = hssfSheet.createRow(i + 1);

                    // 第六步，创建单元格，并设置值
                    WhgYwiNote tnote = notes.get(i);
                    hssfRow.createCell(0).setCellValue(tnote.getAdminaccount());
                    hssfRow.createCell(1).setCellValue(optObj.get(tnote.getOpttype()+""));
                    hssfRow.createCell(2).setCellValue(tnote.getOptdesc());
                    hssfRow.createCell(3).setCellValue(sdf.format(tnote.getOpttime()));
                }
            }

            //下载完成后设置Cookie
            Cookie cookie = new Cookie(cookieName, "");
            response.addCookie(cookie);
            //输出到客户端
            workbook.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw e;
        }finally {
            out.close();
        }
    }

    /**
     * 通过KEY获得操作对象
     * @return
     * @throws Exception
     */
    private Map<String, String> optObj()throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        Class<?> class1 = Class.forName("com.creatoo.hn.util.enums.EnumOptType");
        Object[] objs = class1.getEnumConstants();
        for(Object obj : objs){
            Method valueMethod = obj.getClass().getMethod("getValue");
            Method nameMethod = obj.getClass().getMethod("getName");
            String _val = valueMethod.invoke(obj).toString();
            String _nam = nameMethod.invoke(obj).toString();
            map.put(_val, _nam);
        }
        return map;
    }
}
