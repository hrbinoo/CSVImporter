package cz.raynet.csvimporter.shared.api;

import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.shared.configuration.APIConfiguration;
import cz.raynet.csvimporter.shared.exception.APIRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiRequestSender {
    private final WebClient webClient;
    private final APIConfiguration apiConfiguration;

    /**
     * Sends a request to the API
     *
     * @param httpMethod HTTP method of the request
     * @param path path of the request
     * @param requestBody request body of the request
     * */
    public <T> Mono<T> send(final HttpMethod httpMethod,final String path, final Object requestBody,final Class<T> responseType) {
        WebClient.RequestBodySpec request =
                (WebClient.RequestBodySpec) webClient
                    .method(httpMethod)
                    .uri(uriBuilder -> uriBuilder.path(path).build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody);

        return request.retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new APIRequestException("Error: " + error ,clientResponse.statusCode())))
                )
                .bodyToMono(responseType)
                .doOnSuccess(message -> log.info("Response: {}", message))
                .doOnError(error -> log.error("Error: {}", error.getMessage()));
    }


    /**
     * Fetches a list of APICompanyResponse objects from a specific endpoint.
     *
     * @param regNumber Registration number of the company.
     * @param offset    Offset of the list.
     * @param limit     Limit of the list.
     * @return A Flux containing a list of APICompanyResponse objects.
     */
    public Mono<List<APICompanyResponse>> sendForCompanies(final String regNumber,final int offset,final int limit) {
        final String companyPath = apiConfiguration.getCompanyPath();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(companyPath)
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .queryParam("regNumber[EQ]", regNumber)
                        .build())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new APIRequestException("Error: " + error ,clientResponse.statusCode())))
                )
                .bodyToFlux(APICompanyResponse.class)
                .collectList();

    }
}
