package cz.raynet.csvimporter.crm.integration.impl;

import cz.raynet.csvimporter.crm.integration.CompanyApiFetcher;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.shared.api.ApiRequestSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyApiFetcherImpl implements CompanyApiFetcher {
    private final ApiRequestSender apiRequestSender;

    /**
     * Fetches companies from the API based on the registration number.
     *
     * @param regNumber registration number of the company.
     * @return list of companies.
     * */
    public Mono<List<APICompanyResponse>> getByRegNumber(final String regNumber) {
        log.info("Fetching companies from the API based on the registration number: {}", regNumber);
        return apiRequestSender.sendForCompanies(regNumber, 0, 1);
    }

}
