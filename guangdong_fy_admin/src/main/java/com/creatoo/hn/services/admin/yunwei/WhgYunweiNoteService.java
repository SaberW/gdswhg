package com.creatoo.hn.services.admin.yunwei;

import com.creatoo.hn.dao.mapper.WhgYwiNoteMapper;
import com.creatoo.hn.dao.model.WhgYwiNote;
import com.creatoo.hn.util.IDUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

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
}
