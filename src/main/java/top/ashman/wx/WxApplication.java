package top.ashman.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author singoasher
 * @date 2018/1/30
 */
@EnableDiscoveryClient
@SpringBootApplication
public class WxApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxApplication.class, args);
	}
}
