package com.abc.backend.CNPM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;

@SpringBootApplication(exclude = {
		SecurityAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class // Thêm dòng này để loại trừ bảo mật của Actuator
})
public class CnpmApplication {

	public static void main(String[] args) {
		SpringApplication.run(CnpmApplication.class, args);
	}
}