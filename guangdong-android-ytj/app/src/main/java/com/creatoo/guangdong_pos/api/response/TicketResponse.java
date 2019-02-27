package com.creatoo.guangdong_pos.api.response;

import com.force.librarybase.network.retrofit.HttpResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @author Administrator
 * @version v1.0
 * @Package com.creatoo.guangdong_pos.api.response
 * @Description:
 * @date 2018/4/24
 */
public class TicketResponse extends HttpResponse implements Serializable{


    /**
     * data : {"act":{"actlat":23.145261,"actlon":113.3131,"actsummary":"测试用","acturl":"","address":"广东省文化馆","areaid":"荔湾区","arttype":"2018020169538204,1709191068138532","checkdate":1524212714000,"checkor":"33e73a673b79496690ca5f07db240420","city":"广州市","contacts":"吃啥","coorganizer":"","crtdate":1524212720000,"crtuser":"33e73a673b79496690ca5f07db240420","cultid":"2017111881609457","delstate":0,"deptid":"2017111838254719","ebrand":"2018041297685041","ekey":"","endtime":1528387200000,"enterendtime":1527754932000,"enterstrtime":1524212531000,"etag":"2017112193807612,2017122089476135","etype":"2017083024617358,2017101071496320","filepath":"","hasfees":0,"host":"","id":"2018042064123850","imgurl":"/img/2018/201804/20180420/dac70048f77b4412a68a667aac9c00b0.jpg","integral":1,"integralnum":50,"isbigbanner":0,"isrealname":0,"isrecommend":0,"name":"雷文洁赴省文物交流信息中心 古建筑保护中心调研","noticetype":"ZNX","organizer":"","performed":"","province":"广东省","publishdate":1524212720000,"publisher":"33e73a673b79496690ca5f07db240420","remark":" <p>&nbsp;辞旧迎新贺新春，马欢羊叫来接班，羊歌声声唱吉祥。2015年2月10日早上9时在纱帽街大嘴村委会大院内举行的2015\u201c三下乡活动\u201d由区委宣传部、区文体新广局、区科技局、区卫计委、区农业局等联合主办。我馆组织文艺小分队，为农民献上了14个精彩的文艺节目。同时，我馆还组织了汉南区书画家协会会员，现场为农民业务写春联1800多幅。<\/p>","roomid":"2017111812094576","seats":3,"sellticket":2,"speaker":"","starttime":1527782400000,"state":6,"statemdfdate":1524212720000,"statemdfuser":"33e73a673b79496690ca5f07db240420","telephone":"0321-88889999","telphone":"18522225555","tempstorage":0,"ticketnum":50,"venueid":"2017111873961425"},"ticket":[{"id":"2018042472641098","orderid":"2018042449852761","printtime":1524557032000,"seatcode":"票1","seatid":"P1","ticketstatus":0}],"time":{"actid":"2018042064123850","id":"2018042085164023","playdate":1528300800000,"playendtime":1528333200000,"playetime":"09:00","playstarttime":1528329600000,"playstime":"08:00","seats":50,"state":1},"order":{"activityid":"2018042064123850","eventid":"2018042085164023","id":"2018042449852761","ordercreatetime":1524557032000,"orderisvalid":1,"ordername":"hzy","ordernumber":"118042436102","orderphoneno":"18807070822","ordersmsstate":2,"ordersmstime":1524557032000,"printtickettimes":0,"printtime":1524559108086,"ticketstatus":1,"userid":"2017073198563071"}}
     * page : 0
     * pageSize : 0
     * total : 0
     */

    private DataInfo data;
    private int page;
    private int pageSize;
    private int total;

    public DataInfo getData() {
        return data;
    }

    public void setData(DataInfo data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class DataInfo {
        /**
         * act : {"actlat":23.145261,"actlon":113.3131,"actsummary":"测试用","acturl":"","address":"广东省文化馆","areaid":"荔湾区","arttype":"2018020169538204,1709191068138532","checkdate":1524212714000,"checkor":"33e73a673b79496690ca5f07db240420","city":"广州市","contacts":"吃啥","coorganizer":"","crtdate":1524212720000,"crtuser":"33e73a673b79496690ca5f07db240420","cultid":"2017111881609457","delstate":0,"deptid":"2017111838254719","ebrand":"2018041297685041","ekey":"","endtime":1528387200000,"enterendtime":1527754932000,"enterstrtime":1524212531000,"etag":"2017112193807612,2017122089476135","etype":"2017083024617358,2017101071496320","filepath":"","hasfees":0,"host":"","id":"2018042064123850","imgurl":"/img/2018/201804/20180420/dac70048f77b4412a68a667aac9c00b0.jpg","integral":1,"integralnum":50,"isbigbanner":0,"isrealname":0,"isrecommend":0,"name":"雷文洁赴省文物交流信息中心 古建筑保护中心调研","noticetype":"ZNX","organizer":"","performed":"","province":"广东省","publishdate":1524212720000,"publisher":"33e73a673b79496690ca5f07db240420","remark":" <p>&nbsp;辞旧迎新贺新春，马欢羊叫来接班，羊歌声声唱吉祥。2015年2月10日早上9时在纱帽街大嘴村委会大院内举行的2015\u201c三下乡活动\u201d由区委宣传部、区文体新广局、区科技局、区卫计委、区农业局等联合主办。我馆组织文艺小分队，为农民献上了14个精彩的文艺节目。同时，我馆还组织了汉南区书画家协会会员，现场为农民业务写春联1800多幅。<\/p>","roomid":"2017111812094576","seats":3,"sellticket":2,"speaker":"","starttime":1527782400000,"state":6,"statemdfdate":1524212720000,"statemdfuser":"33e73a673b79496690ca5f07db240420","telephone":"0321-88889999","telphone":"18522225555","tempstorage":0,"ticketnum":50,"venueid":"2017111873961425"}
         * ticket : [{"id":"2018042472641098","orderid":"2018042449852761","printtime":1524557032000,"seatcode":"票1","seatid":"P1","ticketstatus":0}]
         * time : {"actid":"2018042064123850","id":"2018042085164023","playdate":1528300800000,"playendtime":1528333200000,"playetime":"09:00","playstarttime":1528329600000,"playstime":"08:00","seats":50,"state":1}
         * order : {"activityid":"2018042064123850","eventid":"2018042085164023","id":"2018042449852761","ordercreatetime":1524557032000,"orderisvalid":1,"ordername":"hzy","ordernumber":"118042436102","orderphoneno":"18807070822","ordersmsstate":2,"ordersmstime":1524557032000,"printtickettimes":0,"printtime":1524559108086,"ticketstatus":1,"userid":"2017073198563071"}
         */

        private ActivitiyInfo act;
        private TimeInfo time;
        private OrderInfo order;
        private List<TicketInfo> ticket;


        private VenRoomInfo venRoom;
        private VenInfo ven;


        public VenRoomInfo getVenRoom() {
            return venRoom;
        }

        public void setVenRoom(VenRoomInfo venRoom) {
            this.venRoom = venRoom;
        }

        public VenInfo getVen() {
            return ven;
        }

        public void setVen(VenInfo ven) {
            this.ven = ven;
        }



        public static class VenRoomInfo {
            /**
             * checkdate : 1524642013000
             * checkor : c8eef75df35b46eab56e1575844aefc0
             * contacts : 测试
             * crtdate : 1524641950000
             * crtuser : c8eef75df35b46eab56e1575844aefc0
             * delstate : 0
             * deptid : 2017111838254719
             * description :  <p>房东房东飞</p>
             * ekey :
             * etag : 2017112437196258
             * etype : 1709191306417676
             * facility : 2017082958132067,2017102824763981
             * hasfees : 0
             * id : 2018042571450286
             * imgurl : /img/2018/201804/20180425/749eefceb5b74004b97213367910e3b9.jpg
             * isrealname : 1
             * location : 测试
             * noticetype : ZNX
             * openweek : 2,3,4,5
             * phone : 13200002222
             * publishdate : 1524642017000
             * publisher : c8eef75df35b46eab56e1575844aefc0
             * recommend : 0
             * seatcols : 5
             * seatjson : [[{"numrow":0,"numcol":0,"type":1,"numreal":"1排1座"},{"numrow":0,"numcol":1,"type":1,"numreal":"1排2座"},{"numrow":0,"numcol":2,"type":1,"numreal":"1排3座"},{"numrow":0,"numcol":3,"type":1,"numreal":"1排4座"},{"numrow":0,"numcol":4,"type":1,"numreal":"1排5座"}],[{"numrow":1,"numcol":0,"type":1,"numreal":"2排1座"},{"numrow":1,"numcol":1,"type":1,"numreal":"2排2座"},{"numrow":1,"numcol":2,"type":1,"numreal":"2排3座"},{"numrow":1,"numcol":3,"type":1,"numreal":"2排4座"},{"numrow":1,"numcol":4,"type":1,"numreal":"2排5座"}],[{"numrow":2,"numcol":0,"type":1,"numreal":"3排1座"},{"numrow":2,"numcol":1,"type":1,"numreal":"3排2座"},{"numrow":2,"numcol":2,"type":1,"numreal":"3排3座"},{"numrow":2,"numcol":3,"type":1,"numreal":"3排4座"},{"numrow":2,"numcol":4,"type":1,"numreal":"3排5座"}],[{"numrow":3,"numcol":0,"type":1,"numreal":"4排1座"},{"numrow":3,"numcol":1,"type":1,"numreal":"4排2座"},{"numrow":3,"numcol":2,"type":1,"numreal":"4排3座"},{"numrow":3,"numcol":3,"type":1,"numreal":"4排4座"},{"numrow":3,"numcol":4,"type":1,"numreal":"4排5座"}],[{"numrow":4,"numcol":0,"type":1,"numreal":"5排1座"},{"numrow":4,"numcol":1,"type":1,"numreal":"5排2座"},{"numrow":4,"numcol":2,"type":1,"numreal":"5排3座"},{"numrow":4,"numcol":3,"type":1,"numreal":"5排4座"},{"numrow":4,"numcol":4,"type":1,"numreal":"5排5座"}]]
             * seatrows : 5
             * sizearea : 100.0
             * sizepeople : 20
             * state : 6
             * statemdfdate : 1524641933000
             * statemdfuser : c8eef75df35b46eab56e1575844aefc0
             * summary : 范德萨发
             * telephone : 0321-55556666
             * title : 烦烦烦
             * venid : 2018020174516380
             * weektimejson : {"2":[{"timestart":"08:00","timeend":"10:00"},{"timestart":"10:30","timeend":"12:00"},{"timestart":"14:00","timeend":"16:00"},{"timestart":"16:30","timeend":"18:00"}],"3":[{"timestart":"08:00","timeend":"10:00"},{"timestart":"10:30","timeend":"12:00"},{"timestart":"14:00","timeend":"16:00"},{"timestart":"16:30","timeend":"18:00"}],"4":[{"timestart":"08:00","timeend":"10:00"},{"timestart":"10:30","timeend":"12:00"},{"timestart":"14:00","timeend":"16:00"},{"timestart":"16:30","timeend":"18:00"}],"5":[{"timestart":"08:00","timeend":"10:00"},{"timestart":"10:30","timeend":"12:00"},{"timestart":"14:00","timeend":"16:00"},{"timestart":"16:30","timeend":"18:00"}]}
             */

            private long checkdate;
            private String checkor;
            private String contacts;
            private long crtdate;
            private String crtuser;
            private int delstate;
            private String deptid;
            private String description;
            private String ekey;
            private String etag;
            private String etype;
            private String facility;
            private int hasfees;
            private String id;
            private String imgurl;
            private int isrealname;
            private String location;
            private String noticetype;
            private String openweek;
            private String phone;
            private long publishdate;
            private String publisher;
            private int recommend;
            private int seatcols;
            private String seatjson;
            private int seatrows;
            private double sizearea;
            private int sizepeople;
            private int state;
            private long statemdfdate;
            private String statemdfuser;
            private String summary;
            private String telephone;
            private String title;
            private String venid;
            private String weektimejson;

            public long getCheckdate() {
                return checkdate;
            }

            public void setCheckdate(long checkdate) {
                this.checkdate = checkdate;
            }

            public String getCheckor() {
                return checkor;
            }

            public void setCheckor(String checkor) {
                this.checkor = checkor;
            }

            public String getContacts() {
                return contacts;
            }

            public void setContacts(String contacts) {
                this.contacts = contacts;
            }

            public long getCrtdate() {
                return crtdate;
            }

            public void setCrtdate(long crtdate) {
                this.crtdate = crtdate;
            }

            public String getCrtuser() {
                return crtuser;
            }

            public void setCrtuser(String crtuser) {
                this.crtuser = crtuser;
            }

            public int getDelstate() {
                return delstate;
            }

            public void setDelstate(int delstate) {
                this.delstate = delstate;
            }

            public String getDeptid() {
                return deptid;
            }

            public void setDeptid(String deptid) {
                this.deptid = deptid;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getEkey() {
                return ekey;
            }

            public void setEkey(String ekey) {
                this.ekey = ekey;
            }

            public String getEtag() {
                return etag;
            }

            public void setEtag(String etag) {
                this.etag = etag;
            }

            public String getEtype() {
                return etype;
            }

            public void setEtype(String etype) {
                this.etype = etype;
            }

            public String getFacility() {
                return facility;
            }

            public void setFacility(String facility) {
                this.facility = facility;
            }

            public int getHasfees() {
                return hasfees;
            }

            public void setHasfees(int hasfees) {
                this.hasfees = hasfees;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public int getIsrealname() {
                return isrealname;
            }

            public void setIsrealname(int isrealname) {
                this.isrealname = isrealname;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getNoticetype() {
                return noticetype;
            }

            public void setNoticetype(String noticetype) {
                this.noticetype = noticetype;
            }

            public String getOpenweek() {
                return openweek;
            }

            public void setOpenweek(String openweek) {
                this.openweek = openweek;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public long getPublishdate() {
                return publishdate;
            }

            public void setPublishdate(long publishdate) {
                this.publishdate = publishdate;
            }

            public String getPublisher() {
                return publisher;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }

            public int getRecommend() {
                return recommend;
            }

            public void setRecommend(int recommend) {
                this.recommend = recommend;
            }

            public int getSeatcols() {
                return seatcols;
            }

            public void setSeatcols(int seatcols) {
                this.seatcols = seatcols;
            }

            public String getSeatjson() {
                return seatjson;
            }

            public void setSeatjson(String seatjson) {
                this.seatjson = seatjson;
            }

            public int getSeatrows() {
                return seatrows;
            }

            public void setSeatrows(int seatrows) {
                this.seatrows = seatrows;
            }

            public double getSizearea() {
                return sizearea;
            }

            public void setSizearea(double sizearea) {
                this.sizearea = sizearea;
            }

            public int getSizepeople() {
                return sizepeople;
            }

            public void setSizepeople(int sizepeople) {
                this.sizepeople = sizepeople;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getStatemdfdate() {
                return statemdfdate;
            }

            public void setStatemdfdate(long statemdfdate) {
                this.statemdfdate = statemdfdate;
            }

            public String getStatemdfuser() {
                return statemdfuser;
            }

            public void setStatemdfuser(String statemdfuser) {
                this.statemdfuser = statemdfuser;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVenid() {
                return venid;
            }

            public void setVenid(String venid) {
                this.venid = venid;
            }

            public String getWeektimejson() {
                return weektimejson;
            }

            public void setWeektimejson(String weektimejson) {
                this.weektimejson = weektimejson;
            }
        }

        public static class VenInfo {
            /**
             * address : 广东省文化馆
             * area : 测试1
             * checkdate : 1517454373000
             * checkor : 2c5d35a456bd4aaab169fdaed2a9cd40
             * city : 广州--测试区域
             * contacts : 管理员
             * crtdate : 1517453972000
             * crtuser : 2c5d35a456bd4aaab169fdaed2a9cd40
             * cultid : 2017111881609457
             * delstate : 0
             * deptid : 2017111838254719
             * description :  <p style="margin-top: 1em; margin-bottom: 1em; padding: 0px; font-family: 微软雅黑; color: rgb(86, 86, 86); white-space: normal; background-color: rgb(255, 255, 255);">梁家河村地处陕北黄土高原丘陵沟壑区，土地贫瘠，水源缺乏。有史以来，这里的居民靠天吃饭，小麦、土豆、糜子等耐旱作物的种植勉强维持生存。</p> <p style="margin-top: 1em; margin-bottom: 1em; padding: 0px; font-family: 微软雅黑; color: rgb(86, 86, 86); white-space: normal; background-color: rgb(255, 255, 255);">　　从前“掏个坡坡，吃个窝窝”的黄土高原贫困村在最近40年里加速改变：通电、通路、建水塔、退耕还林……村民们近年来还种苹果、办养殖合作社、建旅游公司，2017年人均收入就超2万。</p> <p>     <br/> </p>
             * ekey : 多功能厅,广东省馆的关键字--站点管理员
             * etag : 2017112410634728
             * etype : 2017102884359612
             * facilitydesc :
             * id : 2018020174516380
             * imgurl : /img/2018/201802/20180201/81423d90ac844c78ab671453f227679a.jpg
             * latitude : 23.145241
             * longitude : 113.312910
             * phone : 15266663333
             * province : 广东省
             * publishdate : 1517454385000
             * publisher : 2c5d35a456bd4aaab169fdaed2a9cd40
             * recommend : 0
             * state : 6
             * statemdfdate : 1517454311000
             * statemdfuser : 2c5d35a456bd4aaab169fdaed2a9cd40
             * summary : 梁家河村地处陕北黄土高原丘陵沟壑区，土地贫瘠，水源缺乏。有史以来，这里的居民靠天吃饭，小麦、土豆、糜子等耐旱作物的种植勉强维持生存。
             * telephone : 0321-88889999
             * title : 站点的超级管理员进行添加场馆
             */

            private String address;
            private String area;
            private long checkdate;
            private String checkor;
            private String city;
            private String contacts;
            private long crtdate;
            private String crtuser;
            private String cultid;
            private int delstate;
            private String deptid;
            private String description;
            private String ekey;
            private String etag;
            private String etype;
            private String facilitydesc;
            private String id;
            private String imgurl;
            private String latitude;
            private String longitude;
            private String phone;
            private String province;
            private long publishdate;
            private String publisher;
            private int recommend;
            private int state;
            private long statemdfdate;
            private String statemdfuser;
            private String summary;
            private String telephone;
            private String title;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public long getCheckdate() {
                return checkdate;
            }

            public void setCheckdate(long checkdate) {
                this.checkdate = checkdate;
            }

            public String getCheckor() {
                return checkor;
            }

            public void setCheckor(String checkor) {
                this.checkor = checkor;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getContacts() {
                return contacts;
            }

            public void setContacts(String contacts) {
                this.contacts = contacts;
            }

            public long getCrtdate() {
                return crtdate;
            }

            public void setCrtdate(long crtdate) {
                this.crtdate = crtdate;
            }

            public String getCrtuser() {
                return crtuser;
            }

            public void setCrtuser(String crtuser) {
                this.crtuser = crtuser;
            }

            public String getCultid() {
                return cultid;
            }

            public void setCultid(String cultid) {
                this.cultid = cultid;
            }

            public int getDelstate() {
                return delstate;
            }

            public void setDelstate(int delstate) {
                this.delstate = delstate;
            }

            public String getDeptid() {
                return deptid;
            }

            public void setDeptid(String deptid) {
                this.deptid = deptid;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getEkey() {
                return ekey;
            }

            public void setEkey(String ekey) {
                this.ekey = ekey;
            }

            public String getEtag() {
                return etag;
            }

            public void setEtag(String etag) {
                this.etag = etag;
            }

            public String getEtype() {
                return etype;
            }

            public void setEtype(String etype) {
                this.etype = etype;
            }

            public String getFacilitydesc() {
                return facilitydesc;
            }

            public void setFacilitydesc(String facilitydesc) {
                this.facilitydesc = facilitydesc;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public long getPublishdate() {
                return publishdate;
            }

            public void setPublishdate(long publishdate) {
                this.publishdate = publishdate;
            }

            public String getPublisher() {
                return publisher;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }

            public int getRecommend() {
                return recommend;
            }

            public void setRecommend(int recommend) {
                this.recommend = recommend;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getStatemdfdate() {
                return statemdfdate;
            }

            public void setStatemdfdate(long statemdfdate) {
                this.statemdfdate = statemdfdate;
            }

            public String getStatemdfuser() {
                return statemdfuser;
            }

            public void setStatemdfuser(String statemdfuser) {
                this.statemdfuser = statemdfuser;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }

        public ActivitiyInfo getAct() {
            return act;
        }

        public void setAct(ActivitiyInfo act) {
            this.act = act;
        }

        public TimeInfo getTime() {
            return time;
        }

        public void setTime(TimeInfo time) {
            this.time = time;
        }

        public OrderInfo getOrder() {
            return order;
        }

        public void setOrder(OrderInfo order) {
            this.order = order;
        }

        public List<TicketInfo> getTicket() {
            return ticket;
        }

        public void setTicket(List<TicketInfo> ticket) {
            this.ticket = ticket;
        }

        public static class ActivitiyInfo {
            /**
             * actlat : 23.145261
             * actlon : 113.3131
             * actsummary : 测试用
             * acturl :
             * address : 广东省文化馆
             * areaid : 荔湾区
             * arttype : 2018020169538204,1709191068138532
             * checkdate : 1524212714000
             * checkor : 33e73a673b79496690ca5f07db240420
             * city : 广州市
             * contacts : 吃啥
             * coorganizer :
             * crtdate : 1524212720000
             * crtuser : 33e73a673b79496690ca5f07db240420
             * cultid : 2017111881609457
             * delstate : 0
             * deptid : 2017111838254719
             * ebrand : 2018041297685041
             * ekey :
             * endtime : 1528387200000
             * enterendtime : 1527754932000
             * enterstrtime : 1524212531000
             * etag : 2017112193807612,2017122089476135
             * etype : 2017083024617358,2017101071496320
             * filepath :
             * hasfees : 0
             * host :
             * id : 2018042064123850
             * imgurl : /img/2018/201804/20180420/dac70048f77b4412a68a667aac9c00b0.jpg
             * integral : 1
             * integralnum : 50
             * isbigbanner : 0
             * isrealname : 0
             * isrecommend : 0
             * name : 雷文洁赴省文物交流信息中心 古建筑保护中心调研
             * noticetype : ZNX
             * organizer :
             * performed :
             * province : 广东省
             * publishdate : 1524212720000
             * publisher : 33e73a673b79496690ca5f07db240420
             * remark :  <p>&nbsp;辞旧迎新贺新春，马欢羊叫来接班，羊歌声声唱吉祥。2015年2月10日早上9时在纱帽街大嘴村委会大院内举行的2015“三下乡活动”由区委宣传部、区文体新广局、区科技局、区卫计委、区农业局等联合主办。我馆组织文艺小分队，为农民献上了14个精彩的文艺节目。同时，我馆还组织了汉南区书画家协会会员，现场为农民业务写春联1800多幅。</p>
             * roomid : 2017111812094576
             * seats : 3
             * sellticket : 2
             * speaker :
             * starttime : 1527782400000
             * state : 6
             * statemdfdate : 1524212720000
             * statemdfuser : 33e73a673b79496690ca5f07db240420
             * telephone : 0321-88889999
             * telphone : 18522225555
             * tempstorage : 0
             * ticketnum : 50
             * venueid : 2017111873961425
             */

            private double actlat;
            private double actlon;
            private String actsummary;
            private String acturl;
            private String address;
            private String areaid;
            private String arttype;
            private long checkdate;
            private String checkor;
            private String city;
            private String contacts;
            private String coorganizer;
            private long crtdate;
            private String crtuser;
            private String cultid;
            private int delstate;
            private String deptid;
            private String ebrand;
            private String ekey;
            private long endtime;
            private long enterendtime;
            private long enterstrtime;
            private String etag;
            private String etype;
            private String filepath;
            private int hasfees;
            private String host;
            private String id;
            private String imgurl;
            private int integral;
            private int integralnum;
            private int isbigbanner;
            private int isrealname;
            private int isrecommend;
            private String name;
            private String noticetype;
            private String organizer;
            private String performed;
            private String province;
            private long publishdate;
            private String publisher;
            private String remark;
            private String roomid;
            private int seats;
            private int sellticket;
            private String speaker;
            private long starttime;
            private int state;
            private long statemdfdate;
            private String statemdfuser;
            private String telephone;
            private String telphone;
            private int tempstorage;
            private int ticketnum;
            private String venueid;

            public double getActlat() {
                return actlat;
            }

            public void setActlat(double actlat) {
                this.actlat = actlat;
            }

            public double getActlon() {
                return actlon;
            }

            public void setActlon(double actlon) {
                this.actlon = actlon;
            }

            public String getActsummary() {
                return actsummary;
            }

            public void setActsummary(String actsummary) {
                this.actsummary = actsummary;
            }

            public String getActurl() {
                return acturl;
            }

            public void setActurl(String acturl) {
                this.acturl = acturl;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAreaid() {
                return areaid;
            }

            public void setAreaid(String areaid) {
                this.areaid = areaid;
            }

            public String getArttype() {
                return arttype;
            }

            public void setArttype(String arttype) {
                this.arttype = arttype;
            }

            public long getCheckdate() {
                return checkdate;
            }

            public void setCheckdate(long checkdate) {
                this.checkdate = checkdate;
            }

            public String getCheckor() {
                return checkor;
            }

            public void setCheckor(String checkor) {
                this.checkor = checkor;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getContacts() {
                return contacts;
            }

            public void setContacts(String contacts) {
                this.contacts = contacts;
            }

            public String getCoorganizer() {
                return coorganizer;
            }

            public void setCoorganizer(String coorganizer) {
                this.coorganizer = coorganizer;
            }

            public long getCrtdate() {
                return crtdate;
            }

            public void setCrtdate(long crtdate) {
                this.crtdate = crtdate;
            }

            public String getCrtuser() {
                return crtuser;
            }

            public void setCrtuser(String crtuser) {
                this.crtuser = crtuser;
            }

            public String getCultid() {
                return cultid;
            }

            public void setCultid(String cultid) {
                this.cultid = cultid;
            }

            public int getDelstate() {
                return delstate;
            }

            public void setDelstate(int delstate) {
                this.delstate = delstate;
            }

            public String getDeptid() {
                return deptid;
            }

            public void setDeptid(String deptid) {
                this.deptid = deptid;
            }

            public String getEbrand() {
                return ebrand;
            }

            public void setEbrand(String ebrand) {
                this.ebrand = ebrand;
            }

            public String getEkey() {
                return ekey;
            }

            public void setEkey(String ekey) {
                this.ekey = ekey;
            }

            public long getEndtime() {
                return endtime;
            }

            public void setEndtime(long endtime) {
                this.endtime = endtime;
            }

            public long getEnterendtime() {
                return enterendtime;
            }

            public void setEnterendtime(long enterendtime) {
                this.enterendtime = enterendtime;
            }

            public long getEnterstrtime() {
                return enterstrtime;
            }

            public void setEnterstrtime(long enterstrtime) {
                this.enterstrtime = enterstrtime;
            }

            public String getEtag() {
                return etag;
            }

            public void setEtag(String etag) {
                this.etag = etag;
            }

            public String getEtype() {
                return etype;
            }

            public void setEtype(String etype) {
                this.etype = etype;
            }

            public String getFilepath() {
                return filepath;
            }

            public void setFilepath(String filepath) {
                this.filepath = filepath;
            }

            public int getHasfees() {
                return hasfees;
            }

            public void setHasfees(int hasfees) {
                this.hasfees = hasfees;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public int getIntegralnum() {
                return integralnum;
            }

            public void setIntegralnum(int integralnum) {
                this.integralnum = integralnum;
            }

            public int getIsbigbanner() {
                return isbigbanner;
            }

            public void setIsbigbanner(int isbigbanner) {
                this.isbigbanner = isbigbanner;
            }

            public int getIsrealname() {
                return isrealname;
            }

            public void setIsrealname(int isrealname) {
                this.isrealname = isrealname;
            }

            public int getIsrecommend() {
                return isrecommend;
            }

            public void setIsrecommend(int isrecommend) {
                this.isrecommend = isrecommend;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNoticetype() {
                return noticetype;
            }

            public void setNoticetype(String noticetype) {
                this.noticetype = noticetype;
            }

            public String getOrganizer() {
                return organizer;
            }

            public void setOrganizer(String organizer) {
                this.organizer = organizer;
            }

            public String getPerformed() {
                return performed;
            }

            public void setPerformed(String performed) {
                this.performed = performed;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public long getPublishdate() {
                return publishdate;
            }

            public void setPublishdate(long publishdate) {
                this.publishdate = publishdate;
            }

            public String getPublisher() {
                return publisher;
            }

            public void setPublisher(String publisher) {
                this.publisher = publisher;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getRoomid() {
                return roomid;
            }

            public void setRoomid(String roomid) {
                this.roomid = roomid;
            }

            public int getSeats() {
                return seats;
            }

            public void setSeats(int seats) {
                this.seats = seats;
            }

            public int getSellticket() {
                return sellticket;
            }

            public void setSellticket(int sellticket) {
                this.sellticket = sellticket;
            }

            public String getSpeaker() {
                return speaker;
            }

            public void setSpeaker(String speaker) {
                this.speaker = speaker;
            }

            public long getStarttime() {
                return starttime;
            }

            public void setStarttime(long starttime) {
                this.starttime = starttime;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public long getStatemdfdate() {
                return statemdfdate;
            }

            public void setStatemdfdate(long statemdfdate) {
                this.statemdfdate = statemdfdate;
            }

            public String getStatemdfuser() {
                return statemdfuser;
            }

            public void setStatemdfuser(String statemdfuser) {
                this.statemdfuser = statemdfuser;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getTelphone() {
                return telphone;
            }

            public void setTelphone(String telphone) {
                this.telphone = telphone;
            }

            public int getTempstorage() {
                return tempstorage;
            }

            public void setTempstorage(int tempstorage) {
                this.tempstorage = tempstorage;
            }

            public int getTicketnum() {
                return ticketnum;
            }

            public void setTicketnum(int ticketnum) {
                this.ticketnum = ticketnum;
            }

            public String getVenueid() {
                return venueid;
            }

            public void setVenueid(String venueid) {
                this.venueid = venueid;
            }

            @Override
            public String toString() {
                return "ActivitiyInfo{" +
                        "actlat=" + actlat +
                        ", actlon=" + actlon +
                        ", actsummary='" + actsummary + '\'' +
                        ", acturl='" + acturl + '\'' +
                        ", address='" + address + '\'' +
                        ", areaid='" + areaid + '\'' +
                        ", arttype='" + arttype + '\'' +
                        ", checkdate=" + checkdate +
                        ", checkor='" + checkor + '\'' +
                        ", city='" + city + '\'' +
                        ", contacts='" + contacts + '\'' +
                        ", coorganizer='" + coorganizer + '\'' +
                        ", crtdate=" + crtdate +
                        ", crtuser='" + crtuser + '\'' +
                        ", cultid='" + cultid + '\'' +
                        ", delstate=" + delstate +
                        ", deptid='" + deptid + '\'' +
                        ", ebrand='" + ebrand + '\'' +
                        ", ekey='" + ekey + '\'' +
                        ", endtime=" + endtime +
                        ", enterendtime=" + enterendtime +
                        ", enterstrtime=" + enterstrtime +
                        ", etag='" + etag + '\'' +
                        ", etype='" + etype + '\'' +
                        ", filepath='" + filepath + '\'' +
                        ", hasfees=" + hasfees +
                        ", host='" + host + '\'' +
                        ", id='" + id + '\'' +
                        ", imgurl='" + imgurl + '\'' +
                        ", integral=" + integral +
                        ", integralnum=" + integralnum +
                        ", isbigbanner=" + isbigbanner +
                        ", isrealname=" + isrealname +
                        ", isrecommend=" + isrecommend +
                        ", name='" + name + '\'' +
                        ", noticetype='" + noticetype + '\'' +
                        ", organizer='" + organizer + '\'' +
                        ", performed='" + performed + '\'' +
                        ", province='" + province + '\'' +
                        ", publishdate=" + publishdate +
                        ", publisher='" + publisher + '\'' +
                        ", remark='" + remark + '\'' +
                        ", roomid='" + roomid + '\'' +
                        ", seats=" + seats +
                        ", sellticket=" + sellticket +
                        ", speaker='" + speaker + '\'' +
                        ", starttime=" + starttime +
                        ", state=" + state +
                        ", statemdfdate=" + statemdfdate +
                        ", statemdfuser='" + statemdfuser + '\'' +
                        ", telephone='" + telephone + '\'' +
                        ", telphone='" + telphone + '\'' +
                        ", tempstorage=" + tempstorage +
                        ", ticketnum=" + ticketnum +
                        ", venueid='" + venueid + '\'' +
                        '}';
            }
        }

        public static class TimeInfo {
            /**
             * actid : 2018042064123850
             * id : 2018042085164023
             * playdate : 1528300800000
             * playendtime : 1528333200000
             * playetime : 09:00
             * playstarttime : 1528329600000
             * playstime : 08:00
             * seats : 50
             * state : 1
             */

            private String actid;
            private String id;
            private long playdate;
            private long playendtime;
            private String playetime;
            private long playstarttime;
            private String playstime;
            private int seats;
            private int state;

            public String getActid() {
                return actid;
            }

            public void setActid(String actid) {
                this.actid = actid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getPlaydate() {
                return playdate;
            }

            public void setPlaydate(long playdate) {
                this.playdate = playdate;
            }

            public long getPlayendtime() {
                return playendtime;
            }

            public void setPlayendtime(long playendtime) {
                this.playendtime = playendtime;
            }

            public String getPlayetime() {
                return playetime;
            }

            public void setPlayetime(String playetime) {
                this.playetime = playetime;
            }

            public long getPlaystarttime() {
                return playstarttime;
            }

            public void setPlaystarttime(long playstarttime) {
                this.playstarttime = playstarttime;
            }

            public String getPlaystime() {
                return playstime;
            }

            public void setPlaystime(String playstime) {
                this.playstime = playstime;
            }

            public int getSeats() {
                return seats;
            }

            public void setSeats(int seats) {
                this.seats = seats;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            @Override
            public String toString() {
                return "TimeInfo{" +
                        "actid='" + actid + '\'' +
                        ", id='" + id + '\'' +
                        ", playdate=" + playdate +
                        ", playendtime=" + playendtime +
                        ", playetime='" + playetime + '\'' +
                        ", playstarttime=" + playstarttime +
                        ", playstime='" + playstime + '\'' +
                        ", seats=" + seats +
                        ", state=" + state +
                        '}';
            }
        }

        public static class OrderInfo {

            //活动订单
            /**
             * activityid : 2018042064123850
             * eventid : 2018042085164023
             * id : 2018042449852761
             * ordercreatetime : 1524557032000
             * orderisvalid : 1
             * ordername : hzy
             * ordernumber : 118042436102
             * orderphoneno : 18807070822
             * ordersmsstate : 2
             * ordersmstime : 1524557032000
             * printtickettimes : 0
             * printtime : 1524559108086
             * ticketstatus : 1
             * userid : 2017073198563071
             */


            //场馆订单
            //            //            "crtdate":1524642078000,
            ////                    "delstate":0,
            ////                    "hasfees":0,
            ////                    "id":"2018042524691507",
            ////                    "ordercontact":"婷婷妹哆",
            ////                    "ordercontactphone":"17300000003",
            ////                    "orderid":"318042558197",
            ////                    "printtickettimes":0,
            ////                    "printtime":1524642719752,
            ////                    "purpose":"",
            ////                    "roomid":"2018042571450286",
            ////                    "state":3,
            ////                    "ticketcheckstate":1,
            ////                    "ticketstatus":1,
            ////                    "timeday":1524672000000,
            ////                    "timeend":7200000,
            ////                    "timelong":120.0,
            ////                    "timestart":0,
            ////                    "userid":"2017121163512789"

            private String activityid;
            private String eventid;
            private String id;
            private long ordercreatetime;
            private int orderisvalid;
            private String ordername;
            private String ordernumber;
            private String orderphoneno;
            private int ordersmsstate;
            private long ordersmstime;
            private int printtickettimes;
            private long printtime;
            private int ticketstatus;
            private String userid;
            private long crtdate;
            private int delstate;
            private int hasfees;
            private String ordercontact;
            private String ordercontactphone;
            private String orderid;
            private String purpose;
            private String roomid;
            private int state;
            private int ticketcheckstate;
            private long timeday;
            private long timeend;
            private int timelong;
            private long timestart;

            public long getCrtdate() {
                return crtdate;
            }

            public void setCrtdate(long crtdate) {
                this.crtdate = crtdate;
            }

            public int getDelstate() {
                return delstate;
            }

            public void setDelstate(int delstate) {
                this.delstate = delstate;
            }

            public int getHasfees() {
                return hasfees;
            }

            public void setHasfees(int hasfees) {
                this.hasfees = hasfees;
            }

            public String getOrdercontact() {
                return ordercontact;
            }

            public void setOrdercontact(String ordercontact) {
                this.ordercontact = ordercontact;
            }

            public String getOrdercontactphone() {
                return ordercontactphone;
            }

            public void setOrdercontactphone(String ordercontactphone) {
                this.ordercontactphone = ordercontactphone;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public String getPurpose() {
                return purpose;
            }

            public void setPurpose(String purpose) {
                this.purpose = purpose;
            }

            public String getRoomid() {
                return roomid;
            }

            public void setRoomid(String roomid) {
                this.roomid = roomid;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public int getTicketcheckstate() {
                return ticketcheckstate;
            }

            public void setTicketcheckstate(int ticketcheckstate) {
                this.ticketcheckstate = ticketcheckstate;
            }

            public long getTimeday() {
                return timeday;
            }

            public void setTimeday(long timeday) {
                this.timeday = timeday;
            }

            public long getTimeend() {
                return timeend;
            }

            public void setTimeend(long timeend) {
                this.timeend = timeend;
            }

            public int getTimelong() {
                return timelong;
            }

            public void setTimelong(int timelong) {
                this.timelong = timelong;
            }

            public long getTimestart() {
                return timestart;
            }

            public void setTimestart(long timestart) {
                this.timestart = timestart;
            }



            public String getActivityid() {
                return activityid;
            }

            public void setActivityid(String activityid) {
                this.activityid = activityid;
            }

            public String getEventid() {
                return eventid;
            }

            public void setEventid(String eventid) {
                this.eventid = eventid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public long getOrdercreatetime() {
                return ordercreatetime;
            }

            public void setOrdercreatetime(long ordercreatetime) {
                this.ordercreatetime = ordercreatetime;
            }

            public int getOrderisvalid() {
                return orderisvalid;
            }

            public void setOrderisvalid(int orderisvalid) {
                this.orderisvalid = orderisvalid;
            }

            public String getOrdername() {
                return ordername;
            }

            public void setOrdername(String ordername) {
                this.ordername = ordername;
            }

            public String getOrdernumber() {
                return ordernumber;
            }

            public void setOrdernumber(String ordernumber) {
                this.ordernumber = ordernumber;
            }

            public String getOrderphoneno() {
                return orderphoneno;
            }

            public void setOrderphoneno(String orderphoneno) {
                this.orderphoneno = orderphoneno;
            }

            public int getOrdersmsstate() {
                return ordersmsstate;
            }

            public void setOrdersmsstate(int ordersmsstate) {
                this.ordersmsstate = ordersmsstate;
            }

            public long getOrdersmstime() {
                return ordersmstime;
            }

            public void setOrdersmstime(long ordersmstime) {
                this.ordersmstime = ordersmstime;
            }

            public int getPrinttickettimes() {
                return printtickettimes;
            }

            public void setPrinttickettimes(int printtickettimes) {
                this.printtickettimes = printtickettimes;
            }

            public long getPrinttime() {
                return printtime;
            }

            public void setPrinttime(long printtime) {
                this.printtime = printtime;
            }

            public int getTicketstatus() {
                return ticketstatus;
            }

            public void setTicketstatus(int ticketstatus) {
                this.ticketstatus = ticketstatus;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            @Override
            public String toString() {
                return "OrderInfo{" +
                        "activityid='" + activityid + '\'' +
                        ", eventid='" + eventid + '\'' +
                        ", id='" + id + '\'' +
                        ", ordercreatetime=" + ordercreatetime +
                        ", orderisvalid=" + orderisvalid +
                        ", ordername='" + ordername + '\'' +
                        ", ordernumber='" + ordernumber + '\'' +
                        ", orderphoneno='" + orderphoneno + '\'' +
                        ", ordersmsstate=" + ordersmsstate +
                        ", ordersmstime=" + ordersmstime +
                        ", printtickettimes=" + printtickettimes +
                        ", printtime=" + printtime +
                        ", ticketstatus=" + ticketstatus +
                        ", userid='" + userid + '\'' +
                        '}';
            }
        }

        public static class TicketInfo {
            /**
             * id : 2018042472641098
             * orderid : 2018042449852761
             * printtime : 1524557032000
             * seatcode : 票1
             * seatid : P1
             * ticketstatus : 0
             */

            private String id;
            private String orderid;
            private long printtime;
            private String seatcode;
            private String seatid;
            private int ticketstatus;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public long getPrinttime() {
                return printtime;
            }

            public void setPrinttime(long printtime) {
                this.printtime = printtime;
            }

            public String getSeatcode() {
                return seatcode;
            }

            public void setSeatcode(String seatcode) {
                this.seatcode = seatcode;
            }

            public String getSeatid() {
                return seatid;
            }

            public void setSeatid(String seatid) {
                this.seatid = seatid;
            }

            public int getTicketstatus() {
                return ticketstatus;
            }

            public void setTicketstatus(int ticketstatus) {
                this.ticketstatus = ticketstatus;
            }


        }
    }

    @Override
    public String toString() {
        return "TicketResponse{" +
                "data=" + data +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", total=" + total +
                '}';
    }
}
