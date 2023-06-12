package eu.chargetime.ocpp.jsonclientimplementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JsonClientImplementationApplication {

	public static void main(String[] args) {
		if (args.length > 0) {
			String chargeboxId = args[0];
			System.setProperty("chargeboxId", chargeboxId);
			String port = args[1];
			System.setProperty("port", port);
		}
		SpringApplication.run(JsonClientImplementationApplication.class, args);
	}
}
