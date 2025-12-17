package com.demo.wxchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WxChat Demo 启动类
 */
@SpringBootApplication
@MapperScan("com.demo.wxchat.mapper")
public class WxChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxChatApplication.class, args);
        System.out.println("WxChat Demo 启动成功！");
    }
}
