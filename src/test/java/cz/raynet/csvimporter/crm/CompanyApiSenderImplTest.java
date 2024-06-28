package cz.raynet.csvimporter.crm;

import cz.raynet.csvimporter.crm.integration.impl.CompanyApiSenderImpl;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APINewCompanyDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiresponse.APICompanyCreatedResponse;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.shared.api.ApiRequestSender;
import cz.raynet.csvimporter.shared.configuration.APIConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;

class CompanyApiSenderImplTest {
    @Mock
    private ApiRequestSender apiRequestSender;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private APIConfiguration apiConfiguration;

    @InjectMocks
    private CompanyApiSenderImpl companyApiSenderImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSend_success() {
        Company company = new Company();
        APINewCompanyDTO newCompanyDTO = new APINewCompanyDTO();
        APICompanyCreatedResponse successResponse = new APICompanyCreatedResponse();
        successResponse.setSuccess(true);

        when(modelMapper.map(company, APINewCompanyDTO.class)).thenReturn(newCompanyDTO);
        when(apiConfiguration.getCompanyPath()).thenReturn("/api/company");
        when(apiRequestSender.send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class))
                .thenReturn(Mono.just(successResponse));

        companyApiSenderImpl.send(company);

        verify(apiRequestSender, times(1)).send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class);
        verify(apiConfiguration, times(1)).getCompanyPath();
        verify(modelMapper, times(1)).map(company, APINewCompanyDTO.class);
    }

    @Test
    void testSend_failure() {
        Company company = new Company();
        APINewCompanyDTO newCompanyDTO = new APINewCompanyDTO();
        APICompanyCreatedResponse failureResponse = new APICompanyCreatedResponse();
        failureResponse.setSuccess(false);

        when(modelMapper.map(company, APINewCompanyDTO.class)).thenReturn(newCompanyDTO);
        when(apiConfiguration.getCompanyPath()).thenReturn("/api/company");
        when(apiRequestSender.send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class))
                .thenReturn(Mono.just(failureResponse));

        companyApiSenderImpl.send(company);

        verify(apiRequestSender, times(1)).send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class);
        verify(apiConfiguration, times(1)).getCompanyPath();
        verify(modelMapper, times(1)). map(company, APINewCompanyDTO.class);
    }

    @Test
    void testSend_apiError() {
        Company company = new Company();
        APINewCompanyDTO newCompanyDTO = new APINewCompanyDTO();

        when(modelMapper.map(company, APINewCompanyDTO.class)).thenReturn(newCompanyDTO);
        when(apiConfiguration.getCompanyPath()).thenReturn("/api/company");
        when(apiRequestSender.send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        companyApiSenderImpl.send(company);

        verify(apiRequestSender, times(1)).send(HttpMethod.PUT, "/api/company", newCompanyDTO, APICompanyCreatedResponse.class);
        verify(apiConfiguration, times(1)).getCompanyPath();
        verify(modelMapper, times(1)).map(company, APINewCompanyDTO.class);
    }
}
