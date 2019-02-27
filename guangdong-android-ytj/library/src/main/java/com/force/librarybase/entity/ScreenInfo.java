package com.force.librarybase.entity;

/**
 * @author Jacky.Cai
 * @version v1.0
 * @Package com.dayhr.cscec.pmc.entity.device
 * @Description:
 * @date 16/7/28 下午2:57
 */
public class ScreenInfo {
    public int widthPixels;
    public int heightPixels;
    public float density;

    @Override
    public String toString() {
        return "ScreenInfo{" +
                "widthPixels=" + widthPixels +
                ", heightPixels=" + heightPixels +
                ", density=" + density +
                '}';
    }
}
