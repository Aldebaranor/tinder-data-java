package com.juntai.tinder.controller;

import com.juntai.tinder.netty.handler.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class MessageController {

   @Autowired
    private WebSocketHandler webSocketHandler;

    @GetMapping("/test/websocket")
    @ResponseBody
    public void websocket() {
        webSocketHandler.sendAllMessage("hello");
    }




}