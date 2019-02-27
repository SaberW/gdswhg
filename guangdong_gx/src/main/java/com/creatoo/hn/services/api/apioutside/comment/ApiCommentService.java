package com.creatoo.hn.services.api.apioutside.comment;

import com.creatoo.hn.dao.mapper.WhgCommentMapper;
import com.creatoo.hn.dao.model.WhgComment;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.ReqParamsUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * 个人用户中心--点评业务类
 * @author dzl
 *
 */
@Service
@CacheConfig(cacheNames = "ApiComment", keyGenerator = "simpleKeyGenerator")
public class ApiCommentService extends BaseService {
	
   @Autowired
   private WhgCommentMapper whgcommMapper;
   
   /**
    * 我的活动点评查询
    * @param rmuid
    * @return
    */
   @Cacheable
   public Object selectMyActComm(String rmuid){
	   return this.whgcommMapper.selectMyActComm(rmuid);
   }
   
   /**
    * 我的培训点评查询
    * @param rmuid
    * @return
    */
   @Cacheable
   public Object selectMyTraitmComm(String rmuid){
	   return this.whgcommMapper.selectMyTraitmComm(rmuid);
   }
   
   /**
	 * 添加点评信息
	 * @param whcomm
	 * @return
	 */
   @CacheEvict(allEntries = true)
  public Object addMyComm(WhgComment whcomm){
    return this.whgcommMapper.insert(whcomm);
  }
  
	/**
	 * 删除我的点评
	 * @param rmid
	 * @return
	 */
	@CacheEvict(allEntries = true)
	public Object removeComm(String rmid){
		return this.whgcommMapper.deleteByPrimaryKey(rmid);
	}
	
	/**
	 * 查询培训/活动/艺术作品的评论信息
	 * @param reftyp 评论类型（培训/活动/艺术作品）
	 * @param refid 评论类型的标识
	 * @return 评论列表
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@Cacheable
	public List<HashMap> srchComment(String reftyp, String refid)throws Exception{
		/*Example example = new Example(WhgComment.class);
		Criteria criteria = example.createCriteria();
		criteria.andNotEqualTo("rmreftyp", reftyp);
		criteria.andNotEqualTo("rmrefid", refid);
		criteria.andNotEqualTo("rmtyp", "0");*/
		
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("reftyp", reftyp);
		param.put("refid", refid);
		
		return this.whgcommMapper.searchComment(param);
	}
	/** 查询回复
	 * @return
	 * @throws Exception
	 */
	@Cacheable
	public List<Object> searchCommentHuifu(String rmids) throws Exception{
		List<String> rmidsList = Arrays.asList( rmids.split(",") );
		return this.whgcommMapper.searchCommentHuifu(rmidsList);
	}
	
	/**
	 * 个人中心 评论查询
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Cacheable
	public Object loadcomLoad(WebRequest request){
		//获取请求参数
		Map<String, Object> param = ReqParamsUtil.parseRequest(request);
		
		//分页信息
		int page = Integer.parseInt((String)param.get("page"));
		int rows = Integer.parseInt((String)param.get("rows"));
		String userid = (String) param.get("rmuid");
		String rmreftyp = (String) param.get("rmreftyp");
		//根据用户id获取所有评论
		PageHelper.startPage(page, rows);
		List<HashMap> commList = this.whgcommMapper.searchMyComment(userid,rmreftyp);
		System.out.println("commList============"+commList);
		// 取分页信息
		PageInfo<HashMap> pinfo = new PageInfo<>(commList);
		commList = pinfo.getList();
		
		//将评论id 装数组
		ArrayList<String> ids = null;
		if(commList != null && commList.size()>0){
			ids = new ArrayList<>();
			for(HashMap m :commList){
				ids.add((String)m.get("rmid"));
			}
		}
		System.out.println("ids==============="+ids);
		List<HashMap> commReList = this.whgcommMapper.searchMyCommentRetry(ids);

        //将回复  和对应的 放map
        //Map<String, HashMap> commRetryMap = new HashMap<String, HashMap>();
        List commRetryMap = new ArrayList();
        if(commReList != null){
            for(HashMap m :commReList){
                //commRetryMap.put((String)m.get("rmid"), m);
                commRetryMap.add(m);
            }
        }
		
		Map<String, Object> commMapAll = new HashMap<>();
		commMapAll.put("total", pinfo.getTotal());
		commMapAll.put("commList", commList);
		commMapAll.put("commRetryMap", commRetryMap);
		return commMapAll;
	}

	/**
	 * 删除评论
	 */
	@CacheEvict(allEntries = true)
	public void removeContent(WebRequest request){
			String rmid=request.getParameter("rmid");

			//查出所有回复 
			Example example=new Example(WhgComment.class);
			example.or().andEqualTo("rmrefid",rmid);
			List<WhgComment> list = this.whgcommMapper.selectByExample(example);
			for(int i=0;i<list.size();i++){
				if(rmid.equals(list.get(i).getRmrefid())){
					this.whgcommMapper.deleteByPrimaryKey(list.get(i).getRmid());
				}
			}
		 this.whgcommMapper.deleteByPrimaryKey(rmid);
	}
}
