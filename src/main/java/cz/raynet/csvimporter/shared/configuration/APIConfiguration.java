package cz.raynet.csvimporter.shared.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter @Setter
@ConfigurationProperties("api")
public class APIConfiguration {
    private String url;
    private String key;
    private String username;
    private String instanceName;
    private String companyPath;
}
