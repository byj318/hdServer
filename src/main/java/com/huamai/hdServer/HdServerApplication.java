package com.huamai.hdServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 启动类
 * @author byj
 *
 */
@SpringBootApplication
@MapperScan("com.huamai.hdServer.mapper")
@EnableScheduling
public class HdServerApplication{
	public static void main(String[] args) {
		SpringApplication.run(HdServerApplication.class, args);
	}
}
