package com.wistron.swpc.wismarttrafficlight;

import com.wistron.swpc.wismarttrafficlight.helper.WiLicenseHelper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ServletComponentScan
@SpringBootApplication
@EnableScheduling
public class WiSmartTrafficLightApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WiSmartTrafficLightApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		WiLicenseHelper.checkLicence();
	}
}
