package org.sensornetwork.device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class DeviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeviceApplication.class, args);
	}
}