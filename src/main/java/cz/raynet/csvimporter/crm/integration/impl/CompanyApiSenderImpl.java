package cz.raynet.csvimporter.crm.integration.impl;

import cz.raynet.csvimporter.crm.integration.CompanyApiSender;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APINewCompanyDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiresponse.APICompanyCreatedResponse;
import cz.raynet.csvimporter.shared.api.ApiRequestSender;
import cz.raynet.csvimporter.shared.configuration.APIConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyApiSenderImpl implements CompanyApiSender {
    private final ApiRequestSender apiRequestSender;
    private final ModelMapper modelMapper;
    private final APIConfiguration apiConfiguration;

    /**
     * Sends the new company to the API
     *
     * @param company new company to be sent
     * */
    public void send(final Company company) {
        final var newCompanyDTO = modelMapper.map(company, APINewCompanyDTO.class);
        String path = apiConfiguration.getCompanyPath();
        Mono<APICompanyCreatedResponse> responseMono = apiRequestSender.send(HttpMethod.PUT, path, newCompanyDTO, APICompanyCreatedResponse.class);
        responseMono.subscribe(response -> {
            if(response.isSuccess()) {
                log.info("Company created successfully");
            } else {
                log.warn("Company creation failed");
            }
        }, error -> log.error("Error while creating company", error));
    }

}
