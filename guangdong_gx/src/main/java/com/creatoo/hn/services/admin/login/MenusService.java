package com.creatoo.hn.services.admin.login;

import com.creatoo.hn.util.enums.EnumConsoleSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

/**
 * Created by rbg on 2017/3/15.
 */
@Service("menusService")
public class MenusService {
    /**
     * log对象
     */
    Logger log = Logger.getLogger(this.getClass());

    /**
     * 菜单列表-Map中添加parent属性:为父菜单的id属性
     */
    private List<Map> meunsTreeList = new ArrayList<Map>();

    /**
     * 菜单列表
     */
    private List<Map> meunsList = new ArrayList<Map>();

    /**
     * 配置在菜单中的操作说明
     */
    private Map<String, String> optsList = new HashMap<String,String>();

    /**
     * 初始化此Bean时，调用解析菜单
     */
    @PostConstruct
    private void autoloadMenuXml(){
        try {
            File menuxml = this.getMenuXmlFile();
            this.parseMenu(menuxml);
        } catch (Exception e) {
            log.error("加载系统菜单配置文件出错", e);
        }
    }

    /**
     * 获取menuxml配置文件
     * @return
     * @throws Exception
     */
    private File getMenuXmlFile() throws Exception{
        Resource source = new ClassPathResource("menus.xml");
        return source.getFile();
    }

    /**
     * 解析 menuxml 到菜单载体
     * @param menuxml
     */
    private void parseMenu(File menuxml) throws Exception{
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(menuxml);
        //xml root
        Element root = doc.getRootElement();
        this.parseOptsList(root);
        this.parseTreeListMeun(root);
    }

    /**
     * 解析所有操作说明
     * @param root
     * @throws Exception
     */
    private void parseOptsList(Element root) throws Exception{
        List<Element> optList = root.elements("opts");
        for (Element els : optList){
            List<Element> _el = els.elements("opt");
            if(_el != null){
                for(Element el : _el){
                    String id = el.attributeValue("id");
                    String text = el.getText();
                    if (id!=null && !id.isEmpty()){
                        id = id.trim();
                        this.optsList.put(id, text);
                    }
                }
            }
        }
    }

    /**
     * 以传入的根 取到相关菜单配置的 list-map 树
     * @param root
     * @throws Exception
     */
    private void parseTreeListMeun(Element root) throws Exception{
        //xml menus
        List<Element> menus_list = root.elements("menus");
        for (Element menues : menus_list){
            Iterator<Element> it = menues.elementIterator();
            while (it.hasNext()){
                this._parseTreeMenu(it.next(), this.meunsTreeList, null);
            }
        }
    }

    /**
     * 回调取子菜单树
     * @param el
     * @param list
     */
    private void _parseTreeMenu(Element el, List<Map> list, String parent) throws Exception{
        String ename = el.getName();
        if (ename == null || !"menu".equalsIgnoreCase(ename)){
            return;
        }

        Map menuMap = this.elementMenu2Map(el);
        if (parent!=null){
            menuMap.put("parent", parent);
        }

        String state = (String) menuMap.get("state");//0-无效 1-有效
        String type = (String) menuMap.get("type");//0-分类 1-菜单
        if("1".equals(state)){
            list.add(menuMap);
            if("1".equals(type)) {
                this.meunsList.add(menuMap);
            }
        }

        if ("0".equals(type)){
            List children = (List) menuMap.get("children");
            if (children == null){
                children = new ArrayList();
                menuMap.put("children", children);
            }

            Iterator<Element> it = el.elementIterator();
            while (it.hasNext()){
                this._parseTreeMenu(it.next(), children, (String) menuMap.get("id"));
            }
        }
    }

    /**
     * menu转成Map
     * @param el
     * @return
     */
    private Map elementMenu2Map(Element el) throws Exception{
        Map map = new HashMap();
        if (el == null){
            return map;
        }

        Iterator<Attribute> it = el.attributeIterator();
        while (it.hasNext()){
            Attribute att = it.next();
            map.put(att.getName(), att.getValue().trim());
        }
        return map;
    }

    /**
     * 按登录者权限返回可操作的树
     * @param currentUser
     * @return
     */
    public List<Map> getMeunsTree4Auth(Subject currentUser){
        List<Map> restMap = new ArrayList<>();
        for(Map meun : this.meunsTreeList){
            Map authMap = getAuthMeunMap(meun, currentUser, null);
            if (authMap!=null){
                restMap.add(authMap);
            }
        }
        return restMap;
    }

    /**
     * 根据系统标识获取菜单树
     * @param sysflag bizmgr-业务管理系统; sysmgr-总分馆管理系统
     * @return 菜单树
     */
    public List<Map> getMeunsTree4Sysflag(String sysflag){
        List<Map> restMap = new ArrayList<>();
        for(Map meun : this.meunsTreeList){
            Map authMap = getAuthMeunMap(meun, null, sysflag);
            if (authMap!=null){
                restMap.add(authMap);
            }
        }
        return restMap;
    }

    /**
     * 根据管理员权限过滤菜单树
     * @param meunMap 菜单Map
     * @param currentUser 管理员
     * @return 菜单Map
     */
    private Map getAuthMeunMap(Map meunMap, Subject currentUser, String sysflag) {
        String type = (String) meunMap.get("type");

        //菜单项时，查验是否有权限
        if("1".equals(type)){
            boolean hasPms = false;//是否有权限
            if(currentUser != null){
                String permission = meunMap.get("id")+":view";
                hasPms = currentUser.isPermitted(permission);
            }else if(StringUtils.isNotEmpty(sysflag)){
                String sys = (String) meunMap.get("sys");//业务管理系统菜单(bizmgr)：没有sys属性菜单+sys属性包含2的菜单； 总分馆管理系统(sysmgr)：sys属性包含1或者2的菜单
                if(EnumConsoleSystem.bizmgr.name().equals(sysflag)){
                    if(StringUtils.isEmpty(sys) || (StringUtils.isNotEmpty(sys) && sys.indexOf("2") > -1)){
                        hasPms = true;
                    }
                }else if(EnumConsoleSystem.sysmgr.name().equals(sysflag)){
                    if(StringUtils.isNotEmpty(sys) && ("1".equals(sys) || "2".equals(sys))){
                        hasPms = true;
                    }
                }
            }
            if(hasPms){
                return copyMap(meunMap);
            } else {
                return null;
            }
        }

        //菜单组时
        Map mmp = copyMap(meunMap);
        if("0".equals(type)){
            //无子级不用添加
            List children = (List) meunMap.get("children");
            if (children != null) {
                //装入 权限相子列表
                List clist = (List) mmp.get("children");
                if (clist == null){
                    clist = new ArrayList();
                    mmp.put("children", clist);
                }
                for(Object obj : children){
                    Map mMap = (Map) obj;
                    Map cmmp = getAuthMeunMap(mMap, currentUser, sysflag);
                    if (cmmp != null){
                        clist.add(cmmp);
                    }
                }
            }
        }

        if (mmp.get("children") == null ){
            return null;
        }
        List ls = (List) mmp.get("children");
        if (ls == null || ls.size() == 0){
            return null;
        }
        return mmp;
    }

    /**
     * 复制菜单Map对象,除去children属性
     * @param mp
     * @return
     */
    private Map copyMap(Map mp){
        Map res = new HashMap();
        if (mp == null) return res;
        for(Object key : mp.keySet() ){
            String k = (String) key;
            if ("children".equalsIgnoreCase(k)){
                continue;
            }
            res.put(k, mp.get(k));
        }
        return res;
    }

    /**
     * 获取菜单树
     * @return
     */
    public List<Map> getMeunsTreeList(){
        return this.meunsTreeList;
    }

    /**
     * 获取菜单项列表
     * @return
     */
    public List<Map> getMeunsList(){
        return this.meunsList;
    }

    /**
     * 根据系统类型获取菜单
     * @return
     */
    public List<Map> getMenusList4Sysflag(String sysflag){
        List<Map> meunsList4Sysflag = new ArrayList<>();
        for(Map menu : this.meunsList){
            if(EnumConsoleSystem.sysmgr.getValue().equals(sysflag)){
                if(menu.containsKey("sys")){
                    meunsList4Sysflag.add(menu);
                }
            }else{
                if(!menu.containsKey("sys") || "2".equals( (String)menu.get("sys") )){
                    meunsList4Sysflag.add(menu);
                }
            }
        }
        return meunsList4Sysflag;
    }

    /**
     * 获取菜单操作定义项
     * @return
     */
    public Map<String, String> getOptsList(){
        return this.optsList;
    }
}
