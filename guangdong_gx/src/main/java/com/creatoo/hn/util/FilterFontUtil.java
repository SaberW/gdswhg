package com.creatoo.hn.util;

import com.creatoo.hn.dao.model.*;
import com.creatoo.hn.dao.vo.TrainLiveVO;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 过滤富文本的字体和大小的类
 */
public class FilterFontUtil {
    /**
     * 富文本的字体正则
     */
    public static Pattern PATTERN_FONT_FAMILY_D = Pattern.compile("font\\-family\\:([^;]*?)\"");

    /**
     * 富文本的字体正则
     */
    public static Pattern PATTERN_FONT_FAMILY_B = Pattern.compile("font\\-family\\:([^;]*?);");

    /**
     * 富文本的大小正则
     */
    public static Pattern PATTERN_FONT_SIZE = Pattern.compile("\\s*font\\-size\\:([^;]*?)\"");

    /**
     * 富文本的大小正则
     */
    public static Pattern PATTERN_FONT_SIZE_B = Pattern.compile("\\s*font\\-size\\:([^;]*?);");

    /**
     * 清除富文本中的字体和大小
     * @param str
     * @return
     */
    public static String clearFont(String str){
        if(str != null){
            try {
                str = str.replaceAll("quot;", "AAABBBCCC");

                //先替换掉第一个正则表达式的内容:字体
                Matcher m = PATTERN_FONT_FAMILY_D.matcher(str);
                if (m.find()) {
                    str = m.replaceAll("\"");
                }

                //再替换掉第二个正则表达式的内容:字体
                m = PATTERN_FONT_FAMILY_B.matcher(str);
                if (m.find()) {
                    str = m.replaceAll("");
                }
                //再替换掉第二个正则表达式的内容:大小
                m = PATTERN_FONT_SIZE.matcher(str);
                if (m.find()) {
                    str = m.replaceAll("\"");
                }
                m = PATTERN_FONT_SIZE_B.matcher(str);
                if (m.find()) {
                    str = m.replaceAll("\"");
                }

                str = str.replaceAll("AAABBBCCC","quot;");
            }catch( Exception e){}
        }
        return str;
    }

    /**
     * 培训对象富文本清除字体
     * @param tra 培训对象
     * @return
     */
    public static Map clearFont4Tra(Map tra){
        if(tra != null){
            try{
                String[] columns = new String[]{"coursedesc","intro", "outline", "teacherdesc"};
                for(String column : columns){
                    if(tra.containsKey(column)){
                        String val = (String) tra.get(column);
                        tra.put(column, clearFont(val));
                    }
                }
            }catch( Exception e){}
        }
        return tra;
    }

    /**
     * Map对象富文本清除字体
     * @param map 培训对象
     * @return
     */
    public static Map clearFont(Map map, String[] keys){
        if(map != null){
            try{
                for(String column : keys){
                    if(map.containsKey(column)){
                        String val = (String) map.get(column);
                        map.put(column, clearFont(val));
                    }
                }
            }catch( Exception e){}
        }
        return map;
    }

    /**
     * 在线培训对象富文本清除字体
     * @param vo 培训对象
     * @return
     */
    public static TrainLiveVO clearFont(TrainLiveVO vo){
        if(vo != null){
            try{
                vo.setOutline(clearFont(vo.getOutline()));
                vo.setCoursedesc(clearFont(vo.getCoursedesc()));
                vo.setTeacherdesc(clearFont(vo.getTeacherdesc()));
            }catch( Exception e){}
        }
        return vo;
    }

    /**
     * 微专业对象富文本清除字体
     * @param major 培训对象
     * @return
     */
    public static WhgTraMajor clearFont(WhgTraMajor major){
        if(major != null){
            try{
                major.setDetail(clearFont(major.getDetail()));
            }catch( Exception e){}
        }
        return major;
    }

    /**
     * 活动对象富文本清除字体
     * @param act 活动对象
     * @return
     */
    public static WhgActivity clearFont(WhgActivity act){
        if(act != null){
            try{
                act.setRemark(clearFont(act.getRemark()));
            }catch( Exception e){}
        }
        return act;
    }

    /**
     * Map对象富文本清除字体
     * @param resource 活动对象
     * @return
     */
    public static Map clearFont4Map4All(Map resource){
        if(resource != null){
            try{
                for(Object key : resource.keySet()){
                    if (key instanceof String ){
                        String keyStr = (String) key;
                        Object obj = resource.get(keyStr);
                        if(obj instanceof String){
                            String objStr = (String)obj;
                            resource.put(keyStr, clearFont(objStr));
                        }
                    }
                }
            }catch( Exception e){}
        }
        return resource;
    }

    /**
     * 数字展览富文本清除字体
     * @param exh
     * @return
     */
    public static WhgDigitalExhibition clearFont(WhgDigitalExhibition exh){
        if(exh != null){
            try{
                exh.setRemark(clearFont(exh.getRemark()));
            }catch( Exception e){}
        }
        return exh;
    }

    /**
     * 资讯富文本清除字体
     * @param info
     * @return
     */
    public static WhgInfColinfo clearFont(WhgInfColinfo info){
        if(info != null){
            try{
                info.setClnfdetail(clearFont(info.getClnfdetail()));
            }catch( Exception e){}
        }
        return info;
    }

    /**
     * 文化品牌富文本清除字体
     * @param whpp
     * @return
     */
    public static WhgYwiWhpp clearFont(WhgYwiWhpp whpp){
        if(whpp != null){
            try{
                whpp.setIntroduction(clearFont(whpp.getIntroduction()));
            }catch( Exception e){}
        }
        return whpp;
    }

    /**
     * 展品富文本清除字体
     * @param goods
     * @return
     */
    public static WhgExhGoods clearFont(WhgExhGoods goods){
        if(goods != null){
            try{
                goods.setCowrydesc(clearFont(goods.getCowrydesc()));
            }catch( Exception e){}
        }
        return goods;
    }

    /**
     * 众筹富文本清除字体
     * @param gather
     * @return
     */
    public static WhgGather clearFont(WhgGather gather){
        if(gather != null){
            try{
                gather.setDescriptions(clearFont(gather.getDescriptions()));
            }catch( Exception e){}
        }
        return gather;
    }

    public static void main(String[] args) {
        String str = "<p style=\" font-size:18px; \">\n" +
                "    <span style=\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;; font-size: 20px;\">;lwfjw;q&nbsp;</span>\n" +
                "</p>";

        str = "<p style=\"text-indent:28px\"><span style=\";font-family:微软雅黑;font-size:14px\">5月15日上午，由广西壮族自治区群众艺术馆</span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">党总支部委员会吴颉副书记带队</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">的</span>2018年“三区”文化人才全区公共文化管理与创新培训班学员一行56人来我馆交流学习。</span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">吴颉副书记</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">一行在馆领导及相关部门负责人的陪同下参观了我馆</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">小剧场</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">、</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">非遗展厅，</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">并一同观摩体验了</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">数字</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">文化馆终端设备。随后，双方在八楼会议室召开了座谈会。</span></span></p><p style=\"text-indent:28px\"><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">双方结合工作实际，围绕公共文化服务、培训辅导、群文创作、数字文化馆建设等工作展开深入交流，相互分享工作经验和做法，并深入探讨工作过程中遇到的短板问题及其解决方法。</span></span></p><p style=\"text-indent:28px\"><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">会议现场气氛热烈，大家各抒己见，畅所欲言。王惠君馆长表示，</span>“两广一家人”，群众文化交流密切，我们要利用地缘优势相互“传经授宝”，取长补短；建设数字文化馆要严格遵循相关标准，规范数字资源建设，探索运用</span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">新媒体传播群众文化服务模式</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">。</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">吴颉副书记则</span></span><span style=\";font-family:微软雅黑;font-size:14px\"><span style=\"font-family:微软雅黑\">高度肯定我馆在公共服务效能、志愿者队伍建设、非遗展示以及数字文化馆建设方面所取得的工作成效。大家表示，双方今后要以本次交流活动为契机，相互学习、相互借鉴，借助各自地域特色和文化内涵，进一步加强</span>“两广”的沟通与合作，共同为“两广”文化交流活动增色添彩，推动公共文化服务水平不断提高。</span></p><p><br/></p>";
        System.out.println(FilterFontUtil.clearFont(str));
    }
}
