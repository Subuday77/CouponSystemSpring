package com.couponsystem.CouponSystemSpring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.couponsystem.CouponSystemSpring.rest.AdminController;

@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling

public class CouponSystemSpringApplication {

	public static void main(String[] args) throws InterruptedException {
		ApplicationContext ctx = SpringApplication.run(CouponSystemSpringApplication.class, args);
		System.out.println("Server started.......");

		// ScheduledCleanUp cleanUp = ctx.getBean(ScheduledCleanUp.class);
//		cleanUp.cleanUp();
//		CleanUp cleanUp = ctx.getBean(CleanUp.class);
//		Thread cleanUpThread = new Thread(cleanUp.newRunnable());
//
//		cleanUpThread.start();

	}
}