package com.software.jpetstore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations={"classpath:kaptcha.xml"})
@MapperScan("com.software.jpetstore.persistence")
@Configuration
// 启用AOP注解特性
@EnableAspectJAutoProxy
public class JpetstoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpetstoreApplication.class, args);
	}
}
