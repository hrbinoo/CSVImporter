package cz.raynet.csvimporter.shared.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final APIConfiguration apiConfiguration;

    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(apiConfiguration.getUrl())
                .defaultHeaders(header -> {
                    header.setBasicAuth(
                            apiConfiguration.getUsername(),
                            apiConfiguration.getKey());
                    header.set("X-Instance-Name", apiConfiguration.getInstanceName());
                })
                .build();
    }
}
