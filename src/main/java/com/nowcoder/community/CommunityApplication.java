package com.nowcoder.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CommunityApplication {
	/**
	 * 底层：自动创建spring容器，将类装配到容器中
	 * 			被@SpringBootApplication：扫描配置类所在的包和子包的类：且需要加上Ioc注解(component..)
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(CommunityApplication.class, args);
	}

	/*@PostConstruct
	public void init(){
		//解决elasticsearch和netty启动冲突的问题
		System.setProperty("es.set.netty.runtime.available.processors","false");
	}*/

}
