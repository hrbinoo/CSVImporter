package cz.raynet.csvimporter.crm;

import cz.raynet.csvimporter.crm.integration.impl.CompanyApiFetcherImpl;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.shared.api.ApiRequestSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class CompanyApiFetcherImplTest {
    @Mock
    private ApiRequestSender apiRequestSender;

    @InjectMocks
    private CompanyApiFetcherImpl companyApiFetcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByRegNumber() {
        String regNumber = "123456";
        List<APICompanyResponse> apiResponse = Collections.singletonList(new APICompanyResponse());

        when(apiRequestSender.sendForCompanies(eq(regNumber), eq(0), eq(1))).thenReturn(Mono.just(apiResponse));

        Mono<List<APICompanyResponse>> result = companyApiFetcher.getByRegNumber(regNumber);

        StepVerifier.create(result)
                .expectNext(apiResponse)
                .verifyComplete();

        verify(apiRequestSender).sendForCompanies(eq(regNumber), eq(0), eq(1));
    }
}
