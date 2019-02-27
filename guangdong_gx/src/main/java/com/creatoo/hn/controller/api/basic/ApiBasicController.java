package com.creatoo.hn.controller.api.basic;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.model.WhgYwiLbt;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiLbtService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 基础接口
 * 
 * @author dzl
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/api/basic")
public class ApiBasicController extends BaseController {



	@Autowired
	private WhgYunweiLbtService whgYunweiLbtService;

	/**
	 * 获得类别
	 *
	 * 4 活动 5 培训 2 场馆 3 活动室
	 *
	 * 6 区域
	 *
	 * @return
	 */
	/*@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/lbts", method = RequestMethod.POST)
	public Object lbts(String type,String userId,@RequestParam(value = "page", defaultValue = "1")int page,@RequestParam(value = "toproject", defaultValue = "")String toproject,@RequestParam(value = "cultid", defaultValue = "")String cultid
			,@RequestParam(value = "rows", defaultValue = "10") int rows) {
		ApiResultBean arb = new ApiResultBean();
		List cultlist=null;
		WhgYwiLbt whgYwiLbt =new WhgYwiLbt();
		whgYwiLbt.setState(1);
		whgYwiLbt.setType(type);
		if(!toproject.equals("")){
			whgYwiLbt.setToproject(toproject);
		}
		if(!cultid.equals("")){
			cultlist= Arrays.asList(cultid.replaceAll(","," ").trim().split(" "));
		}
		try {
			PageInfo<WhgYwiLbt> list= this.whgYunweiLbtService.t_srchList(whgYwiLbt,page,rows,cultlist);
			arb.setPageInfo(list);
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("获取区域类别失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}*/

	/**
	 * 获得类别
	 *
	 * 4 活动 5 培训 2 场馆 3 活动室
	 *
	 * 6 区域
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/lbts", method = RequestMethod.POST)
	public Object lbts(
			String type,
			@RequestParam(value = "page", defaultValue = "1")int page,
			@RequestParam(value = "rows", defaultValue = "10") int rows,
			@RequestParam(value = "cultid", defaultValue = "")String cultid,
			@RequestParam(value = "city", defaultValue = "")String city
	) {
		ApiResultBean arb = new ApiResultBean();
		try {
			WhgYwiLbt whgYwiLbt =new WhgYwiLbt();
			whgYwiLbt.setState(EnumState.STATE_YES.getValue());
			whgYwiLbt.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
			whgYwiLbt.setType(type);
			if(StringUtils.isEmpty(cultid)){//全省站
				whgYwiLbt.setProvince("广东省");
			}else{
				if(cultid.indexOf(",") > -1){//全市站
					whgYwiLbt.setCity(city);
				}else{//文化馆
					whgYwiLbt.setCultid(cultid);
				}
			}
			PageInfo<WhgYwiLbt> list= this.whgYunweiLbtService.t_srchList(whgYwiLbt,page,rows, null);
			arb.setPageInfo(list);
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("获取区域类别失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}


}
