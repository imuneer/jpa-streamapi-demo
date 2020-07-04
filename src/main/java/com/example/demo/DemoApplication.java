package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Scanner;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private TicketService ticketService;

	final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Thread.sleep(1000);
		System.out.print("\033[H\033[2J");
		System.out.println("Running console application");
		//boolean proceed = true;
		//while (proceed) {
			System.out.println("");
			System.out.println("Press enter to continue..");
			//System.out.print("Enter your input: ");
			Scanner scan = new Scanner(System.in);
			int i = scan.nextInt();
			System.out.println("");
			//System.out.println("You entered: " + i);
			switch (i) {
				case 1:
					ticketService.readAndCollect();
					break;
				//case 3:
					//proceed = false;
					//break;
			}
		//}
	}
}
