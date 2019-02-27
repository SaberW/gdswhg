package com.force.librarybase.update;

/**
 * @author Jacky.Cai
 * @version v1.1
 * @Package com.daycanyin.caterers.pad.api.response
 * @Description:
 * @date 17/3/22 下午2:47
 */
public class AppUpdateResponse {
    /**
     * basePath : http://static.dayhr.com
     * pageIndex : 0
     * pageSize : 0
     * returnCode : 0
     * returnData : {"editionContent":"<p>&nbsp; [全新内容］<\/p><ol class=\" list-paddingleft-2\" style=\"list-style-type: decimal;\"><li><p>新赛季：s6赛季荣耀开启，下一个荣耀王者，就是你！<\/p><\/li><li><p>新增战场pvp系统。<br/><\/p><\/li><\/ol><p>［更多优化］<\/p><ol class=\" list-paddingleft-2\" style=\"list-style-type: decimal;\"><li><p>布局优化，包括：自定义技能槽位、局内聊天系统等；<\/p><\/li><\/ol><p>&nbsp; 2.战队赛系统优化<\/p><p>［其他修复］<\/p><ol class=\" list-paddingleft-2\" style=\"list-style-type: decimal;\"><li><p>修复诸葛亮受控后动画卡住的问题；<\/p><\/li><\/ol><p>&nbsp; 2.修复启动界面闪退问题。<\/p><p><br/><\/p>","editionId":"2","editionTime":"2017-03-22 17:35:38","editionTitle":"caterers-pad_v1.1","editionType":1003,"editionUrl":"group1/M00/00/29/wKgGoFjSRPKAVuS5ANe-vcRgXOU810.apk","groupType":0,"isMust":1,"typeName":"赢食通餐饮","uuId":3}
     * returnMessage : 操作成功
     * totalPages : 0
     * totalRecords : 0
     */

    public String basePath;
    public int pageIndex;
    public int pageSize;
    public int returnCode;
    public ReturnDataEntity returnData;
    public String returnMessage;
    public int totalPages;
    public int totalRecords;

    public static class ReturnDataEntity {
        /**
         * editionContent : <p>&nbsp; [全新内容］</p><ol class=" list-paddingleft-2" style="list-style-type: decimal;"><li><p>新赛季：s6赛季荣耀开启，下一个荣耀王者，就是你！</p></li><li><p>新增战场pvp系统。<br/></p></li></ol><p>［更多优化］</p><ol class=" list-paddingleft-2" style="list-style-type: decimal;"><li><p>布局优化，包括：自定义技能槽位、局内聊天系统等；</p></li></ol><p>&nbsp; 2.战队赛系统优化</p><p>［其他修复］</p><ol class=" list-paddingleft-2" style="list-style-type: decimal;"><li><p>修复诸葛亮受控后动画卡住的问题；</p></li></ol><p>&nbsp; 2.修复启动界面闪退问题。</p><p><br/></p>
         * editionId : 2
         * editionTime : 2017-03-22 17:35:38
         * editionTitle : caterers-pad_v1.1
         * editionType : 1003
         * editionUrl : group1/M00/00/29/wKgGoFjSRPKAVuS5ANe-vcRgXOU810.apk
         * groupType : 0
         * isMust : 1
         * typeName : 赢食通餐饮
         * uuId : 3
         */

        public String editionContent;
        public String editionId;
        public String editionTime;
        public String editionTitle;
        public int editionType;
        public String editionUrl;
        public int groupType;
        public int isMust;
        public String typeName;
        public long uuId;
    }
}
