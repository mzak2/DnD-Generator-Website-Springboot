package com.dnd.gen.demo;

import com.dnd.gen.demo.service.ConsoleAppService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(DemoApplication.class, args);

		//Launch our console based roller, simply uncomment "consoleAppService.start();"
		ConsoleAppService consoleAppService = new ConsoleAppService();

		//consoleAppService.start();
	}

}
