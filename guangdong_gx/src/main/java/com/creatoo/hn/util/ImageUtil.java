package com.creatoo.hn.util;

import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang.StringUtils;
import sun.util.resources.th.CalendarData_th;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片处理辅助类
 * Created by wangxl on 2017/12/6.
 */
public class ImageUtil {
    /**
     * 获取3比2的缩放图片，主要针对OSS的文件名
     * @param imgname
     * @return
     */
    public static String getImgName_750_500(String imgname){
        if(StringUtils.isNotEmpty(imgname)){
            int idx1 = imgname.lastIndexOf(".");
            imgname = imgname.substring(0, idx1)+"_750_500"+imgname.substring(idx1);
        }
        return imgname;
    }

    /**
     * 获取3比2的缩放图片，主要针对OSS的文件名
     * @param imgname
     * @return
     */
    public static String getImgName_720_540(String imgname){
        if(StringUtils.isNotEmpty(imgname)){
            int idx1 = imgname.lastIndexOf(".");
            imgname = imgname.substring(0, idx1)+"_720_540"+imgname.substring(idx1);
        }
        return imgname;
    }

    /**
     * 获取32比1的缩放图片，主要针对OSS的文件名
     * @param imgname
     * @return
     */
    public static String getImgName_700_350(String imgname){
        if(StringUtils.isNotEmpty(imgname)){
            int idx1 = imgname.lastIndexOf(".");
            imgname = imgname.substring(0, idx1)+"_700_350"+imgname.substring(idx1);
        }
        return imgname;
    }

    /**
     * 上传时生成的备份文件
     * @param imgname
     * @return
     */
    public static String getImgName_bak(String imgname){
        if(StringUtils.isNotEmpty(imgname)){
            int idx1 = imgname.lastIndexOf(".");
            imgname = imgname.substring(0, idx1)+"_bak"+imgname.substring(idx1);
        }
        return imgname;
    }

    /**
     * 删除文件
     * @param filepath
     */
    public static void deleteTmpFile(String filepath){
        try{
            File tmpFile = new File(filepath);
            if(tmpFile.exists()){
                tmpFile.delete();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void saveZoomImg(String libid, String source, int width, int height, BufferedImage image, String tmpBasePathFile){
        saveZoomImg(libid, source, width, height, image, tmpBasePathFile, null);
    }

    /**
     * 缩放图片
     * @param libid
     * @param source
     * @param width
     * @param height
     * @param image
     */
    public static void saveZoomImg(String libid, String source, int width, int height, BufferedImage image, String tmpBasePathFile, String zoomBeforeFileName){
        FileInputStream fis = null;
        String tmp_file_path = null;
        try{
            String zoomFileName = "";
            if(StringUtils.isNotEmpty(zoomBeforeFileName)){
                zoomFileName = ImageUtil.getFileName(zoomBeforeFileName, width, height);//缩放后的文件名,不包含路径
            }else{
                zoomFileName = ImageUtil.getFileName(source, width, height);//缩放后的文件名,不包含路径
            }
            File tmp_filename = ImageUtil.zoomImg(source, zoomFileName, width, height, image, tmpBasePathFile);//缩放后保存为临时目录
            tmp_file_path = tmp_filename.getAbsolutePath();
            fis = new FileInputStream(tmp_filename);
            String key_res = "lib"+libid+"/"+zoomFileName;//放在同一个资源库中
            AliyunOssUtil.uploadFile_file(fis, key_res);
            fis.close();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if(fis != null){
                try{ fis.close(); }catch (Exception e){ e.printStackTrace();}
            }
            //清除临时文件夹里的文件
            if(tmp_file_path != null){
                ImageUtil.deleteTmpFile(tmp_file_path);
            }
        }
    }
    /**
     * 获取缩放图片的文件名称，不包含路径
     * @param fileName 原图名
     * @param width 缩放宽
     * @param height  缩放高
     * @return 缩放后的文件名
     */
    public static String getFileName(String fileName, int width, int height){
        String _filename = null;
        try{
            if(StringUtils.isNotEmpty(fileName)){
                int idx1 = fileName.lastIndexOf(".");
                _filename = fileName.substring(0, idx1)+"_"+width+"_"+height+fileName.substring(idx1);
                _filename = _filename.substring(_filename.lastIndexOf(File.separator)+1);
                _filename = _filename.substring(_filename.lastIndexOf("/")+1);
             }
        } catch (Exception e){
            e.printStackTrace();
        }
        return _filename;
    }

    /**
     * 缩放图片
     * @param source 图片源
     * @param outputFileName 文件名
     * @param width 宽
     * @param height 高
     * @param waterbi 水印图片
     * @return 缩放后的图片
     */
    public static File zoomImg(String source, String outputFileName, int width, int height, BufferedImage waterbi, String basePath) {
        File outputFile = null;
        InputStream fis = null;
        try {
            //原图宽高
            fis = new FileInputStream(new File(source));
            BufferedImage image = ImageIO.read(fis);
            int imageWidth = image.getWidth();
            int imageHeitht = image.getHeight();

            //缩放
            Thumbnails.Builder<BufferedImage> builder = null;
            if ((float)width / height != (float)imageWidth / imageHeitht) {//宽高比不相等
                if(width > height){
                    if (imageWidth > imageHeitht) {
                        image = Thumbnails.of(source).height(height).asBufferedImage();
                    } else {
                        image = Thumbnails.of(source).width(width).asBufferedImage();
                    }
                }else{
                    if (imageWidth > imageHeitht) {
                        image = Thumbnails.of(source).width(width).asBufferedImage();
                    } else {
                        image = Thumbnails.of(source).height(height).asBufferedImage();
                    }
                }
                //水印
                if(waterbi != null){
                    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height).watermark(Positions.BOTTOM_RIGHT,waterbi,1f);
                }else{
                    builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, width, height).size(width, height);
                }
            } else {//宽度比相等
                builder = Thumbnails.of(image).size(width, height);
            }

            //输出
            builder.toFile(basePath+File.separator+outputFileName);

            //输出文件
            outputFile = new File(basePath, outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis != null){
                try{fis.close();} catch (Exception e){}
            }
        }
        return outputFile;
    }

    public static void main(String[] args)throws Exception {

//        BufferedImage image = ImageIO.read(new File("D:\\temp\\sgsy.png"));
//        File imgFile = new File("D:\\temp\\b.jpg");
////        ImageUtil.zoomImg(imgFile.getAbsolutePath(), "D:\\temp\\b_3_2.jpg", 750, 500, image, "D:\\temp");
//
//        int width = 750;
//        int height = 500;
//        String source = imgFile.getAbsolutePath();
//        String zoomFileName = ImageUtil.getFileName(source, width, height);
//        File tmp_filename = ImageUtil.zoomImg(source, zoomFileName, width, height, image, "D:\\temp");
//
//        System.out.println("缩放的文件："+tmp_filename.getAbsoluteFile());

        String imgname = "http://szwhg-gds-admin.oss-cn-shanghai.aliyuncs.com/libf3de8dbcac324f19983573a8623d8ae5/2017011100000022.jpg";
        System.out.println(imgname);
        System.out.println(ImageUtil.getImgName_750_500(imgname));
        System.out.println(ImageUtil.getImgName_720_540(imgname));

        //ImageUtil.zoomImg(imgFile.getAbsolutePath(), "D:\\temp\\a_4_3.jpg", 720, 540, image);
        //ImageUtil.zoomImg(imgFile.getAbsolutePath(), "D:\\temp\\a_2_1.jpg", 700, 350, image);
    }
}
