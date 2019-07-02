package fun.billon.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan("fun.billon.member.dao")
@EnableFeignClients(basePackages = {
        "fun.billon.auth.api.feign"})
@ComponentScan(basePackages = {
        "fun.billon.member",
        "fun.billon.auth.api.hystrix"})
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
