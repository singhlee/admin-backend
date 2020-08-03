package org.singhlee.admin.common.utils;


import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: admin-backend
 * @description:
 * @author: singhlee
 * @date: 2020-07-15 15:42
 **/
@Slf4j
public class FileUtil {

    public static File transferToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
        }
        file.delete();
    }

    public static void fileWrite(String str, String fileNamePath) throws IOException {
        FileWriter writer = null;
        try {
            File file = new File(fileNamePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            writer = new FileWriter(file, true);
            writer.write(str + System.getProperty("line.separator"));

        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    public static void copyFile(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * @param srcFile     Unzipped file
     * @param destDirPath Unzipped destination folder
     * @throws RuntimeException
     * @throws IOException
     * @author panchaoyuan
     */
    public static void unZip(MultipartFile srcFile, String destDirPath, String savePath) throws RuntimeException, IOException {
        long startTime = System.currentTimeMillis();
        File file = null;
        InputStream ins = srcFile.getInputStream();
        file = new File(savePath + srcFile.getOriginalFilename());
        log.info("MultipartFile transform to File,MultipartFile name:" + srcFile.getOriginalFilename());
        inputStreamToFile(ins, file);

        if (!file.exists()) {
            throw new RuntimeException(file.getPath() + ",file is not found");
        }
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(file);
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                log.info("zipFile context name:" + entry.getName());
                if (entry.isDirectory()) {
                    String dirPath = destDirPath + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {
                    File targetFile = new File(destDirPath + File.separator + entry.getName());
                    targetFile.setExecutable(true);
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    targetFile.createNewFile();
                    InputStream is = zipFile.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    int len;
                    byte[] buf = new byte[1024];
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.close();
                    is.close();

                }
            }
            long endTime = System.currentTimeMillis();
            log.info("unZip time-->" + (endTime - startTime) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("unzip error from FileUtil", e);
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //MultipartFile change to file may create a temp file in the project root folder(delete the temp file)
            File del = new File(file.toURI());
            del.delete();
        }
    }

    /**
     * MultipartFile changed to File
     *
     * @return
     * @author panchaoyuan
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
            log.info("MultipartFile transform to File completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<File> getSubFiles(String desFile, List<File> fileList) {
        File file = new File(desFile);
        File[] files = file.listFiles();
        for (File fileIndex : files) {
            if (!fileIndex.exists()) {
                throw new NullPointerException("Cannot find " + fileIndex);
            } else if (fileIndex.isFile()) {
                fileList.add(fileIndex);
            } else {
                if (fileIndex.isDirectory()) {
                    getSubFiles(fileIndex.getAbsolutePath(), fileList);
                }
            }
        }
        return fileList;
    }

    public static String readJsonFile(String filePath) throws IOException {
        String content = null;
        try {
            content = FileUtils.readFileToString(new File(filePath), "UTF-8");
        } catch (IOException e) {
            throw e;
        }
        return content;
    }

    //写文件
    public static String writeJsonfile(String content, String filePath) throws IOException {
        try {
            FileUtils.writeStringToFile(new File(filePath), content, "UTF-8");
        } catch (IOException e) {
            throw e;
        }
        return "success";
    }

    public static File createJsonFile(String jsonString, String filePath, String fileName) {
        String fullPath = filePath + File.separator + fileName + ".json";
        File file = new File(fullPath);

        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
        return file;
    }

}

