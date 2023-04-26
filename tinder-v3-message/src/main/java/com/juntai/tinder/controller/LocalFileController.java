package com.juntai.tinder.controller;

import com.juntai.soulboot.common.exception.SoulBootErrorCode;
import com.juntai.soulboot.common.exception.SoulBootException;
import com.juntai.tinder.config.FileConfig;
import com.juntai.tinder.netty.handler.WebSocketHandler;
import com.juntai.tinder.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/message")
public class LocalFileController {

   @Autowired
    private WebSocketHandler webSocketHandler;

    @GetMapping("/test/websocket")
    @ResponseBody
    public void websocket() {
        webSocketHandler.sendAllMessage("hello");
    }




}