package org.singhlee.admin.common.utils;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Gzip压缩
 * @author singhlee
 *
 */
public class GzipUtils {
    public static void gzip(File source,File dest) throws Exception{
        GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(dest));
        FileInputStream fileInputStream = new FileInputStream(source);
        byte[] buf = new byte[1024];
        int i = -1;
        while((i = fileInputStream.read(buf))> -1){
            gzip.write(buf,0,i);
        }
        gzip.close();
        fileInputStream.close();
    }

    public static void ungzip(File zipFile,File dest) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(zipFile);
        GZIPInputStream gzip = new GZIPInputStream(fileInputStream);
        byte[] buf = new byte[1024];
        int num = -1;
        FileOutputStream bos = new FileOutputStream(dest);
        while ((num = gzip.read(buf, 0, buf.length)) > -1) {
            bos.write(buf, 0, num);
        }

        bos.flush();
        bos.close();
        gzip.close();


    }
    public static byte[] gzip(byte[] data) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(bos);
        gzip.write(data);
        gzip.finish();
        gzip.close();
        byte[] ret = bos.toByteArray();
        bos.close();
        return ret;
    }

    public static byte[] ungzip(byte[] data) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        GZIPInputStream gzip = new GZIPInputStream(bis);
        byte[] buf = new byte[1024];
        int num = -1;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((num = gzip.read(buf, 0, buf.length)) != -1) {
            bos.write(buf, 0, num);
        }
        gzip.close();
        bis.close();
        byte[] ret = bos.toByteArray();
        bos.flush();
        bos.close();
        return ret;
    }


}
