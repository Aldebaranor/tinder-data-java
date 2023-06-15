package com.juntai.tinder.controller;

import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.tinder.config.FileConfig;
import com.juntai.tinder.exception.TinderErrorCode;
import com.juntai.tinder.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 本地文件
 *
 * @author huangkang
 * @version 1.0.0
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/free/local-file")
public class LocalFileController {

    private final FileConfig fileConfig;

    @GetMapping("/{id}/download")
    @ResponseBody
    public void download(@PathVariable String id, HttpServletRequest req, HttpServletResponse resp) {
        Path filePath = Paths.get(fileConfig.getPath(), id);
        if (!Files.exists(filePath)) {
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件不存在");
        }
        resp.setContentType("application/x-download");
        FileUtil.setAttachment(id, req, resp);
        try {
            FileCopyUtils.copy(Files.newInputStream(filePath, StandardOpenOption.READ), resp.getOutputStream());
        } catch (IOException e) {
            // 抛出自己的异常
            log.error("文件下载失败", e);
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件下载失败");
        }
    }

    @PostMapping("/upload/icon")
    @ResponseBody
    public Map<String, Object> upload(@RequestParam String kind, @RequestParam String name, @RequestPart("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件不能为空");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String type = "";
        if (StringUtils.isNotEmpty(originalFilename) && StringUtils.contains(originalFilename, ".")) {
            type = originalFilename.split("\\.")[1];
        }

        Map<String, Object> obj = new HashMap<>(16);

        String fileId = String.format("/icon/%s/%s", kind, name);
        Path filePath = Paths.get(fileConfig.getPath(), fileId + "." + type);
        try {
            FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(filePath, StandardOpenOption.CREATE));
            obj.put("id", fileId + "." + type);
            obj.put("size", multipartFile.getSize());
            obj.put("name", name);
            obj.put("type", type);
            return obj;
        } catch (IOException e) {
            log.error("文件存储失败", e);

            // 抛出自己的异常
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件存储失败");
        }
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> upload(@RequestPart("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件不能为空");
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
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件存储失败");
        }
    }

    @PostMapping("/{id}/upload")
    @ResponseBody
    public Map<String, Object> uploadWithId(@PathVariable String id, @RequestPart("file") MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件不能为空");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String type = "";
        if (StringUtils.isNotEmpty(originalFilename) && StringUtils.contains(originalFilename, ".")) {
            type = originalFilename.split("\\.")[1];
        }

        Map<String, Object> obj = new HashMap<>(16);
        Path filePath = Paths.get(fileConfig.getPath(), id + "." + type);
        try {
            FileCopyUtils.copy(multipartFile.getInputStream(), Files.newOutputStream(filePath, StandardOpenOption.CREATE));
            obj.put("id", id + "." + type);
            obj.put("size", multipartFile.getSize());
            obj.put("name", originalFilename);
            obj.put("type", type);
            return obj;
        } catch (IOException e) {
            log.error("文件存储失败", e);
            // 抛出自己的异常
            throw new SoulBootException(TinderErrorCode.FILE_ERROR, "文件存储失败");
        }
    }


}