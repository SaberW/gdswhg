package com.force.librarybase.entity;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @Package com.dayhr.cscec.pmc.entity.app
 * @Description:
 * @date 16/7/28 下午2:55
 */
public class AppInfo {

    public String appName="com.dayhr.cscec.pmc";
    public String versionName;
    public int versionCode;

    @Override
    public String toString() {
        return "AppInfo{" +
                "versionName='" + versionName + '\'' +
                ", versionCode='" + versionCode + '\'' +
                '}';
    }

}
