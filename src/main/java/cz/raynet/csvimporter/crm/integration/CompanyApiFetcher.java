package cz.raynet.csvimporter.crm.integration;


import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CompanyApiFetcher {
    Mono<List<APICompanyResponse>> getByRegNumber(final String regNumber);
}
