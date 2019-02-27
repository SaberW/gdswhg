package com.creatoo.hn.controller.api.comm;

import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.dao.mapper.WhgInfColinfoMapper;
import com.creatoo.hn.dao.mapper.WhgPubInfoMapper;
import com.creatoo.hn.dao.mapper.WhgSysCultMapper;
import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.services.admin.information.WhgInfoService;
import com.creatoo.hn.services.admin.mylive.MyLiveService;
import com.creatoo.hn.services.admin.resourse.WhgResourceService;
import com.creatoo.hn.services.admin.system.WhgSystemCultService;
import com.creatoo.hn.services.admin.system.WhgSystemDeptService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiKeyService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTagService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiTypeService;
import com.creatoo.hn.services.admin.yunwei.WhgYunweiYjpzservice;
import com.creatoo.hn.services.api.apioutside.collection.ApiCollectionService;
import com.creatoo.hn.services.api.apioutside.user.ApiUserService;
import com.creatoo.hn.util.bean.ApiResultBean;
import com.creatoo.hn.util.enums.EnumState;
import com.creatoo.hn.util.enums.EnumStateDel;
import com.github.pagehelper.PageInfo;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * 公共接口
 *
 * @author dzl
 *
 */

@CrossOrigin
@RestController
@RequestMapping("/api/comm")
public class ApiCommonController extends BaseController {

	@Autowired
	private ApiCollectionService colleService;

	@Autowired
	private ApiUserService apiUserService;

	@Autowired
	private WhgYunweiTypeService whgYunweiTypeService;

	@Autowired
	private WhgYunweiKeyService whgYunweiKeyService;

	@Autowired
	private WhgYunweiTagService whgYunweiTagService;

	@Autowired
	private WhgSystemCultService whgSystemCultService;

	@Autowired
	private WhgResourceService whgResourceService;

	@Autowired
	private WhgYunweiYjpzservice whgYunweiYjpzservice;

	@Autowired
	private WhgSystemDeptService deptService;

	@Autowired
	private WhgInfoService whgInfoService;

	@Autowired
	private WhgSysCultMapper whgSysCultMapper;

	/**
	 * 一体机首页视频
	 */
	@Autowired
	private MyLiveService myLiveService;



    /**
     * 获得标签
     *
     * 4 活动 5 培训 2 场馆 3 活动室
     *
     *
     * @return
     */
    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public Object getTags(String type,@RequestParam(value = "cultid", defaultValue = "")String cultid,
                          @RequestParam(value = "tags", defaultValue = "") String[] tags) {
        ApiResultBean arb = new ApiResultBean();
        try {
            ArrayList<String> tagList = new ArrayList<>(Arrays.asList(tags));
            List<WhgYwiTag> list = whgYunweiTagService.findAllYwiTag(type,cultid,tagList);
            arb.setRows(list);
        } catch (Exception e) {
            arb.setCode(1);
            arb.setMsg("获得标签失败");
            log.error(e.getMessage(), e);
        }
        return arb;
    }


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
	@RequestMapping(value = "/getTypes", method = RequestMethod.POST)
	public Object getTypes(String type,@RequestParam(value = "cultid", required =false,defaultValue = "")String cultid ) {
		ApiResultBean arb = new ApiResultBean();
		try {
			if(cultid.indexOf(",")!=-1){//带逗号文化馆id串 代表全市站，默认取公共类别 与全省站一致
				cultid="";
			}
			List list =whgYunweiTypeService.findAllYwiType(type, cultid, true);
			arb.setRows(list);
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("获取区域类别失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}


	/**
	 * 获取关键字
	 *
	 * 4 活动 5 培训 2 场馆 3 活动室
	 *
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/getKeys", method = RequestMethod.POST)
	public Object getKeys(String type,@RequestParam(value = "cultid", defaultValue = "")String cultid,@RequestParam(value = "page", defaultValue = "1")int page
			,@RequestParam(value = "rows", defaultValue = "10") int rows) {
		ApiResultBean arb = new ApiResultBean();
		WhgYwiKey whgYwiKey =new WhgYwiKey();
		whgYwiKey.setType(type);
		whgYwiKey.setState(1);
		whgYwiKey.setDelstate(0);
		if(cultid!=""){
			whgYwiKey.setCultid(cultid);
		}
		try {
			PageInfo<WhgYwiKey> pageInfo = this.whgYunweiKeyService.t_srchList4p(page,rows,whgYwiKey, null);
			arb.setPageInfo(pageInfo);
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("获取关键字失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}

	/**
	 * 获得标签
	 *
	 * 4 活动 5 培训 2 场馆 3 活动室
	 *
	 *
	 * @return
	 */
	@ResponseBody
	@CrossOrigin
	@RequestMapping(value = "/getTags", method = RequestMethod.POST)
	public Object getTags(String type,@RequestParam(value = "cultid", defaultValue = "")String cultid,@RequestParam(value = "page", defaultValue = "1")int page
			,@RequestParam(value = "rows", defaultValue = "10") int rows) {
		ApiResultBean arb = new ApiResultBean();
		WhgYwiTag whgYwiTag =new WhgYwiTag();
		whgYwiTag.setType(type);
		whgYwiTag.setState(1);
		whgYwiTag.setDelstate(0);
		if(cultid!=""){
			whgYwiTag.setCultid(cultid);
		}
		try {
			PageInfo<WhgYwiTag> pageInfo = this.whgYunweiTagService.t_srchList4p(page, rows, whgYwiTag, null, null);
			arb.setPageInfo(pageInfo);
		} catch (Exception e) {
			arb.setCode(1);
			arb.setMsg("获得标签失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}


	/**
	 * 按市和省取文化馆列表
	 * @param city
	 * @param province
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getCult4city", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public Object getCult4city(@RequestParam(value = "city", required = false) String city,
							   @RequestParam(value = "level", required = false) String level,
							   @RequestParam(value = "province", required = false)String province){
		ApiResultBean arb = new ApiResultBean();

		try {
			WhgSysCult recode = new WhgSysCult();
			recode.setState(6);
			recode.setIsshow(1);
			if (province != null && !province.isEmpty()) {
				recode.setProvince(province);
			}if (city != null && !city.isEmpty()) {
				recode.setCity(city);
			}
			List list = this.whgSystemCultService.t_srchList(null, null, recode,level);
			arb.setRows(list);
		} catch (Exception e) {
			arb.setCode(101);
			arb.setMsg("查询数据失败");
			log.error(e.getMessage(),e);
		}

		return arb;
	}

	/**
	 * 取指定的文化馆
	 * @param id
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getCult", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Object getCult(@RequestParam(value = "id", required = false) String id,
						  @RequestParam(value = "cultsite", required = false) String cultsite,
						  @RequestParam(value = "city", required = false) String city) {
		ApiResultBean arb = new ApiResultBean();
		Object info = null;
		try {
			if (id != null) {
				info = this.whgSystemCultService.t_srchOne(id);
			}
			if (info == null && cultsite != null) {//存在实际文化馆站点
				info = this.whgSystemCultService.t_srchOneBySite(cultsite);
			}
			if (info == null && cultsite != null && !"".equals(cultsite) && city != null) {//虚拟文化馆站点 如 全省站 全市站
				info = this.whgSystemCultService.t_srchOneByCity(cultsite, city);
			}
			arb.setData(info);
		} catch (Exception e) {
			arb.setCode(101);
			arb.setMsg("查询数据失败");
			log.error(e.getMessage(), e);
		}
		return arb;
	}

	/**
	 * 取相枚举定义值
	 * @param type
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getEnumValues/{type}", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Object getSupplyTypes(@PathVariable("type")String type){
		ApiResultBean arb = new ApiResultBean();
		List<Map<String, String>> statelist = new ArrayList<Map<String, String>>();
		try {
			if(type != null && !type.isEmpty()) {
				Class<?> class1 = Class.forName("com.creatoo.hn.util.enums." + type);
				Object[] objs = class1.getEnumConstants();
				for (Object obj : objs) {
					Method valueMethod = obj.getClass().getMethod("getValue");
					Method nameMethod = obj.getClass().getMethod("getName");
					Map<String, String> map = new HashMap<String, String>();
					map.put("name", nameMethod.invoke(obj).toString());
					map.put("value", valueMethod.invoke(obj).toString());
					statelist.add(map);
				}
			}
			arb.setRows(statelist);
		} catch (Exception e) {
			arb.setCode(101);
			arb.setMsg("查询数据失败");
			log.error(e.getMessage(),e);
		}
		return arb;
	}

	/**
	 * 查询资源
	 * @param reftype   实体类型
	 * @param refid     实体ID
	 * @param enttype   资源类型（1图片/2视频/3音频4/文档）
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/resource",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ApiResultBean getResource(@RequestParam(value = "reftype", required = false) String reftype,
									 @RequestParam(value = "refid", required = false) String refid,
									 @RequestParam(value = "enttype", required = false) String enttype,
									 @RequestParam(value = "wechat", required = false) String wechat,
									 @RequestParam(value = "page", defaultValue = "1") int page,
									 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
		ApiResultBean arb = new ApiResultBean();
		try {
			arb = whgResourceService.t_getResource(reftype, refid, enttype, wechat, page, pageSize);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(102);
			arb.setMsg("查询数据失败");
		}
		return arb;
	}

	/**
	 * 查询部门
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getDepts",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ApiResultBean getDepts(@RequestParam(value = "cultid", required = false,defaultValue ="") String cultid){
		ApiResultBean arb = new ApiResultBean();
		try {
			if(cultid!="") {
				WhgSysDept dept = new WhgSysDept();
				dept.setCultid(cultid);
				dept.setIsfront(1);//是否前端显示
				dept.setState(EnumState.STATE_YES.getValue());
				dept.setDelstate(EnumStateDel.STATE_DEL_NO.getValue());
				List<WhgSysDept> list = deptService.t_srchList(dept);
				arb.setRows(list);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(102);
			arb.setMsg("查询数据失败");
		}
		return arb;
	}

	/**
	 * 查询硬件配置
	 * @param type   实体类型
	 * @param entid     实体ID
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/yjpz",method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public ApiResultBean getYjpz(@RequestParam(value = "page")int page,
								 @RequestParam(value = "pageSize")int pageSize,
								 @RequestParam(value = "type",required = false)Integer type,
								 @RequestParam(value = "entid",required = false)String entid){
		ApiResultBean arb = new ApiResultBean();
		try {
			arb = whgYunweiYjpzservice.t_getYjpz(page,pageSize,type,entid);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(102);
			arb.setMsg("查询数据失败");
		}
		return arb;
	}


	/**
	 * 查询实体关联的资讯信息
	 *
	 * @param entityid 实体ID
	 * @param clnftype 栏目ID
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/glzx", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ApiResultBean findColinfo(String entityid, String clnftype,
									 @RequestParam(value = "page", required = false) Integer page,
									 @RequestParam(value = "pageSize", required = false) Integer pageSize) {
		ApiResultBean arb = new ApiResultBean();
		try {
			/*List<WhgInfColinfo> info = this.whgInfoService.findColinfo(entityid, clnftype);
			arb.setData(info);*/

			PageInfo pageInfo = this.whgInfoService.findColinfo(page, pageSize, entityid, clnftype);
			if (page!=null && pageSize != null){
				arb.setPageInfo(pageInfo);
			}else{
				arb.setData(pageInfo.getList());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(102);
			arb.setMsg("查询数据失败");
		}
		return arb;
	}


	/**
	 * 查询一体机首页视频
	 *
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/ytjvideo", method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public ApiResultBean getytjvideo(String type) {
		ApiResultBean arb = new ApiResultBean();
		try {
			Map<String, String> map = new HashMap<>();
			map.put("url", "");
			map.put("title", "");
			WhgLiveComm live = this.myLiveService.getYTJLive();
			if(live != null){
				map.put("url", live.getEnturl());
				map.put("title", live.getName());
			}
			arb.setData(map);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			arb.setCode(102);
			arb.setMsg("查询数据失败");
		}
		return arb;
	}


	/**
	 * 取指定的文化馆
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getAddr", method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Object getCult(HttpServletRequest request) {
		//获取IP
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				ip = ip.substring(0, index);
			}
		} else {
			if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Real-IP");
			} else {
				ip = request.getRemoteAddr();
			}
		}
		//ip ="219.137.148.0";
        //ip ="121.14.255.77";
		//根据IP判断区域
		String json_result = "";
		try {
			json_result = getAddresses("ip=" + ip, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		JSONObject json = JSONObject.fromObject(json_result);
		//国家
		String country = JSONObject.fromObject(json.get("data")).get("country").toString();
		//省份
		String region = JSONObject.fromObject(json.get("data")).get("region").toString()+"省";
		//城市
		String city = JSONObject.fromObject(json.get("data")).get("city").toString()+"市";
		//区县
		String county = JSONObject.fromObject(json.get("data")).get("county").toString()+"区";
		//互联网服务提供商
		//String isp = JSONObject.fromObject(json.get("data")).get("isp").toString();
		//地区
		//String area = JSONObject.fromObject(json.get("data")).get("area").toString();

		ApiResultBean arb = new ApiResultBean();
		Object info = null;
		List<WhgSysCult> list=null;

		WhgSysCult cult=new WhgSysCult();
		cult.setState(6);
		cult.setDelstate(0);
		cult.setProvince("广东省");

		try {
			if("广东省".equals(region)){
				if (county != null) {//查询是否存在对应区馆
					//搜索条件
					cult.setArea(county);
					Example example = whgSystemCultService.parseExample(cult, null,null, null,"3", null, null);
					list = this.whgSysCultMapper.selectByExample(example);
				}
				if (list.size()<1 && city != null) {//查询是否存在对应市馆
					//搜索条件
					cult.setArea(null);
					cult.setCity(city);
					Example example = whgSystemCultService.parseExample(cult, null,null, null,"2", null, null);
					list = this.whgSysCultMapper.selectByExample(example);
				}
				if(list.size()==1){//有对应的站点
					arb.setData(list.get(0));
				}else{
                    cult.setArea(null);
                    cult.setCity(null);
                    Example example = whgSystemCultService.parseExample(cult, null,null, null,"1", null, null);
                    list = this.whgSysCultMapper.selectByExample(example);
					arb.setData(list.get(0));//无对应站点返回省站
				}
			}else{
                cult.setArea(null);
                cult.setCity(null);
                Example example = whgSystemCultService.parseExample(cult, null,null, null,"1", null, null);
                list = this.whgSysCultMapper.selectByExample(example);
				arb.setData(list.get(0));//不在广东省返回省站
			}

		} catch (Exception e) {
			arb.setCode(101);
			arb.setData("fail");
			arb.setMsg("获取地区信息失败");
			log.error(e.getMessage(), e);
		}
		return arb;

	}


	//ip判断地区
	public static String getAddresses(String content, String encodingString) throws UnsupportedEncodingException {
		// 这里调用淘宝API
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		// 从http://whois.pconline.com.cn取得IP所在的省市区信息
		String returnStr = getResult(urlStr, content, encodingString);
		if (returnStr != null) {
			// 处理返回的省市区信息
			System.out.println("(1) unicode转换成中文前的returnStr : " + returnStr);
			returnStr = decodeUnicode(returnStr);
			System.out.println("(2) unicode转换成中文后的returnStr : " + returnStr);
			String[] temp = returnStr.split(",");
			if(temp.length<3){
				return "0";//无效IP，局域网测试
			}
			return returnStr;
		}
		return null;
	}

	private static String getResult(String urlStr, String content, String encoding) {
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection();// 新建连接实例
			connection.setConnectTimeout(2000);// 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000);// 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true);// 是否打开输出流 true|false
			connection.setDoInput(true);// 是否打开输入流true|false
			connection.setRequestMethod("POST");// 提交方法POST|GET
			connection.setUseCaches(false);// 是否缓存true|false
			connection.connect();// 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
			out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush();// 刷新
			out.close();// 关闭输出流
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), encoding));// 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();// 关闭连接
			}
		}
		return null;
	}

	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + aChar - 'A';
								break;
							default:
								throw new IllegalArgumentException(
										"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}






}
