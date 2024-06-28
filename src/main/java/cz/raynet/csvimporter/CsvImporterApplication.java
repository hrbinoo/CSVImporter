package cz.raynet.csvimporter;

import cz.raynet.csvimporter.shared.configuration.APIConfiguration;
import cz.raynet.csvimporter.shared.configuration.MailConfiguration;
import cz.raynet.csvimporter.shared.configuration.WebClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({APIConfiguration.class, MailConfiguration.class})
public class CsvImporterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvImporterApplication.class, args);
    }

}
