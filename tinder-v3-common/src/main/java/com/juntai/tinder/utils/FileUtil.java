package com.juntai.tinder.utils;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.tinder.config.FileConfig;
import com.juntai.tinder.exception.TinderErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Slf4j
public class FileUtil {

    private static final String BLANK_SPACE = " ";
    private static final String USER_AGENT = "User-Agent";
    private static final String USER_AGENT_IE = "MSIE";

    private static final int BUFFER_SIZE = 2 * 1024;


    private static Pattern ALPHA_NUMBER_PATTERN = Pattern.compile("[0-9a-zA-Z_]{1,}");

    private static FileConfig fileConfig;

    private FileUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean string2File(String res, String filePath) {
        boolean flag = true;
        File distFile = new File(filePath);
        if (!distFile.getParentFile().exists()) {
            distFile.getParentFile().mkdirs();
        }

        try (
                BufferedReader bufferedReader = new BufferedReader(new StringReader(res));
                BufferedWriter bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(distFile), StandardCharsets.UTF_8))
        ) {
            //字符缓冲区
            char[] buf = new char[1024];
            int len;
            while ((len = bufferedReader.read(buf)) != -1) {
                bufferedWriter.write(buf, 0, len);
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            flag = false;
        }
        return flag;
    }

    public static void createParentPath(String filePath) {
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
    }

    public static void createFile(String filePath) {
        File file = new File(filePath);
        File fileParent = file.getParentFile();
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new SoulBootException(TinderErrorCode.FILE_CREATE_ERROR,e);
        }
    }


    /**
     * MultipartFile 转 File
     *
     * @param file
     */
    public static File multipartFileToFile(MultipartFile file) {
        File toFile = null;
        if (file == null) {
            throw new SoulBootException(TinderErrorCode.FILE_EMPTY_ERROR);
        } else {
            try (InputStream ins = file.getInputStream()) {
                String originalFilename = file.getOriginalFilename();
                if (StringUtils.isNotEmpty(originalFilename) && StringUtils.contains(originalFilename, BLANK_SPACE)) {
                    originalFilename = originalFilename.replace(BLANK_SPACE, "");
                }
                toFile = new File(Objects.requireNonNull(originalFilename));
                inputStreamToFile(ins, toFile);
            } catch (IOException e) {
                throw new SoulBootException(TinderErrorCode.FILE_WRITE_ERROR,e);
            }
        }
        return toFile;
    }

    /**
     * 获取流文件
     *
     * @param ins
     * @param file
     */
    private static void inputStreamToFile(InputStream ins, File file) {
        try (OutputStream os = new FileOutputStream(file)) {
            int bytesRead;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new SoulBootException(TinderErrorCode.FILE_WRITE_ERROR,e);
        }
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void deleteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            try {
                Files.delete(del.toPath());
            } catch (IOException e) {
                log.error("Failed to delete temp file({}).", del.getAbsolutePath(), e);
            }
        }


    }

    public static void setAttachment(String name, HttpServletRequest req, HttpServletResponse resp) {
        String userAgent = req.getHeader(USER_AGENT);
        if (StringUtils.isBlank(userAgent) || StringUtils.containsIgnoreCase(userAgent, USER_AGENT_IE)) {
            // IE浏览器
            try {
                name = URLEncoder.encode(name, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                // ignore
                log.error("encode err:", e);
            }
        } else {
            // 谷歌、火狐等现代浏览器
            name = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        resp.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resp.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"");
    }

    /**
     * 压缩成ZIP 方法1
     *
     * @param sourceDir        压缩文件夹路径
     * @param targetDir        压缩后文件的路径名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static void toZip(String sourceDir, String targetDir, boolean keepDirStructure)
            throws RuntimeException {
        //Files.getDir(CstDir.config)

        File sourceFile = new File(sourceDir);
        String sourcePath = sourceFile.getParentFile().toString();
        String fileName = sourceFile.getName();

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            FileOutputStream out = null;
            if (Strings.isEmpty(targetDir)) {
                if (sourceFile.isDirectory()) {
                    out = new FileOutputStream(new File(sourcePath + "/" + fileName + ".zip"));
                } else {
                    out = new FileOutputStream(new File(sourcePath + "/" + fileName.substring(0, fileName.lastIndexOf('.')) + ".zip"));
                }
            } else {
                out = new FileOutputStream(new File(targetDir));

            }

            zos = new ZipOutputStream(out);
            compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 压缩成ZIP 方法2
     *
     * @param srcFiles  需要压缩的文件列表
     * @param targetDir 压缩目标路径
     * @throws RuntimeException 压缩失败会抛出运行时异常
     *                          多个文件一起压缩，若上级目录为根节点，则压缩后的名字与最后一个文件的命名相同
     *                          若上级目录不空，则压缩后的名字为上级目录的名字
     */
    public static void toZips(List<File> srcFiles, String targetDir) throws RuntimeException {
        long start = System.currentTimeMillis();

        FileOutputStream out = null;
        String targetName = "";
        String sourcePath = srcFiles.get(0).getParent();

        ZipOutputStream zos = null;
        try {
            if (StringUtils.isEmpty(targetDir)) {
                if (srcFiles.size() > 0 && srcFiles != null) {
                    //获得最后一个文件的名字
                    targetName = srcFiles.get(srcFiles.size() - 1).getName();
                    targetName = targetName.substring(0, targetName.lastIndexOf('.'));
                }
                out = new FileOutputStream(new File(sourcePath + "/" + targetName + ".zip"));

            } else {
                out = new FileOutputStream(new File(targetDir));
            }
            zos = new ZipOutputStream(out);
            for (File srcFile : srcFiles) {
                compress(srcFile, zos, srcFile.getName(), true);
            }
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name,
                                 boolean keepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (keepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (keepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), keepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), keepDirStructure);
                    }
                }
            }
        }
    }

    public static boolean isAlphaNumeric(String s) {
        Matcher m = ALPHA_NUMBER_PATTERN.matcher(s);
        return m.matches();
    }

    /**
     * 上传文件
     *
     * @param multipartFile
     * @return
     */
    public static Map<String, Object> upload(MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            throw new SoulBootException(TinderErrorCode.FILE_EMPTY_ERROR);
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String type = "";
        if (StringUtils.isNotEmpty(originalFilename) && StringUtils.contains(originalFilename, ".")) {
            type = originalFilename.split("\\.")[1];
        }

        Map<String, Object> obj = new HashMap<>(16);
        String fileId = UUID.randomUUID().toString().replace("-", "");
        Path filePath = Paths.get(fileConfig.getPath(), fileId + "." + type);
        try {
            FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(filePath, StandardOpenOption.CREATE));
            obj.put("id", fileId + "." + type);
            obj.put("size", multipartFile.getSize());
            obj.put("name", originalFilename);
            obj.put("type", type);
            return obj;
        } catch (IOException e) {
            log.error("文件存储失败", e);
            // 抛出自己的异常
            throw new SoulBootException(TinderErrorCode.FILE_WRITE_ERROR,e);
        }
    }

}
