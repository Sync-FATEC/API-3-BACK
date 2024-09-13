package com.sync.api;

import com.sync.api.database.CreateDatabase;
import com.sync.api.database.AddDataToDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		CreateDatabase.main(new String[]{});
		SpringApplication.run(ApiApplication.class, args);
		AddDataToDatabase.main(new String[]{});
	}

}
