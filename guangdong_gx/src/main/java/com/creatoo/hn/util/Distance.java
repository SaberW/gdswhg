package com.creatoo.hn.util;

/**
 * Created by Administrator on 2015/7/14.
 */
public class Distance {
    // 经度
    double longitude;
    // 维度
    double dimensionality;

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public double getDimensionality()
    {
        return dimensionality;
    }

    public void setDimensionality(double dimensionality)
    {
        this.dimensionality = dimensionality;
    }

    /*
     * 计算两点之间距离
     *
     * @param start
     *
     * @param end
     *
     * @return 米
     */
    public static   double getDistance(Distance start, Distance end)
    {
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.dimensionality;
        double lat2 = (Math.PI / 180) * end.dimensionality;
        // 地球半径
        double R = 6371;
        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d ;
    }

  public  static  void  main(String args[]){
      Distance a=new Distance();
      a.setLongitude(28.2339710000);
      a.setDimensionality(112.9453330000);
      Distance b=new Distance();
      b.setLongitude(28.2272930000);
      b.setDimensionality(112.9610220000);
      double c=getDistance(a, b);
      System.out.println("c = " + c);
  }
}
