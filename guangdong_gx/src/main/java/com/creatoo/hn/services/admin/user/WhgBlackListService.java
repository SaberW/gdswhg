package com.creatoo.hn.services.admin.user;

import com.creatoo.hn.dao.mapper.WhgUserBlacklistMapper;
import com.creatoo.hn.dao.model.WhgUserBlacklist;
import com.creatoo.hn.util.IDUtils;
import com.creatoo.hn.util.enums.EnumState;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 *
 * 黑名单services
 * @author wenjingqiang
 * @version 1-201703
 * Created by Administrator on 2017/4/26.
 */
@SuppressWarnings("ALL")
@Service
public class WhgBlackListService {

    @Autowired
    private WhgUserBlacklistMapper whgUserBlacklistMapper;


    /**
     * 管理端分页查询黑名单列表
     * @param page
     * @param pageSize
     * @param record
     * @param sort
     * @param order
     * @return
     */
    public PageInfo srchList4p(int page, int pageSize, WhgUserBlacklist record, String sort, String order){
        Example example = new Example(record.getClass());
        Example.Criteria c = example.or();

        if (record != null) {
            if (record.getUserphone() != null && !record.getUserphone().isEmpty()) {
                c.andLike("userphone", "%"+record.getUserphone()+"%");
                record.setUserphone(null);
            }

            c.andEqualTo(record);
        }

        if (sort!=null && !sort.isEmpty()){
            Example.OrderBy orderBy = example.orderBy(sort);
            if (order!=null && "desc".equalsIgnoreCase(order)){
                orderBy.desc();
            }
        }else{
            example.setOrderByClause("state desc, jointime desc");
        }

        PageHelper.startPage(page, pageSize);
        List list = this.whgUserBlacklistMapper.selectByExample(example);

        return new PageInfo(list);
    }


    /**
     * 按用户ID取消黑名单限制
     * @param userid
     * @throws Exception
     */
    public void unBlackList4uid(String userid) throws Exception{
        if (userid == null && userid.isEmpty()) {
            return;
        }

        WhgUserBlacklist info = new WhgUserBlacklist();
        info.setState(EnumState.STATE_NO.getValue());
        info.setUpdatetime(new Date());

        Example example = new Example(info.getClass());
        example.or().andEqualTo("userid", userid);

        this.whgUserBlacklistMapper.updateByExampleSelective(info, example);
    }


    /**
     * 验证指定用户是否被列入黑名单
     * @param userid
     * @return
     */
    public boolean isBlackListUser(String userid){
        if (userid==null || userid.isEmpty()){
            return false;
        }

        try {
            WhgUserBlacklist record = new WhgUserBlacklist();
            record.setUserid(userid);
            record.setState(EnumState.STATE_YES.getValue());
            int count = this.whgUserBlacklistMapper.selectCount(record);
            return count>0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 添加黑名单信息
     * @param userid
     * @param userphone
     * @param note
     * @throws Exception
     */
    public void addBlackList(String userid, String userphone, String note) throws Exception {
        WhgUserBlacklist info = new WhgUserBlacklist();
        info.setUserid(userid);
        info.setUserphone(userphone);
        info.setNote(note);

        this.addBlackList(info);
    }

    /**
     * 添加黑名单信息
     * @param info
     * @throws Exception
     */
    public void addBlackList(WhgUserBlacklist info) throws Exception{
        if (info == null) {
            throw new Exception("info is null");
        }
        if (info.getUserid() == null || info.getUserid().isEmpty()){
            throw new Exception("userid is null");
        }

        boolean isBlack = this.isBlackListUser(info.getUserid());
        if (isBlack){
            throw new Exception("in black user");
        }

        if (info.getId() == null || info.getId().isEmpty()) {
            info.setId(IDUtils.getID());
        }

        if (info.getState() == null) {
            info.setState(EnumState.STATE_YES.getValue());
        }

        info.setJointime(new Date());
        this.whgUserBlacklistMapper.insert(info);
    }


    /**
     * 取指定用户最近移出黑名单时间
     * @param userid
     * @return
     */
    public Date getLastUpdateTime(String userid){
        if (userid == null || userid.isEmpty()) {
            return null;
        }

        try {
            Example example = new Example(WhgUserBlacklist.class);
            example.or().andEqualTo("userid", userid)
                    .andEqualTo("state", EnumState.STATE_NO.getValue());
            example.orderBy("updatetime").desc();

            List<WhgUserBlacklist> list = this.whgUserBlacklistMapper.selectByExampleAndRowBounds(example, new RowBounds(0, 1));
            if (list == null || list.size() == 0) {
                return null;
            }
            WhgUserBlacklist info = list.get(0);
            return info.getUpdatetime();
        } catch (Exception e) {
            return null;
        }
    }


}
