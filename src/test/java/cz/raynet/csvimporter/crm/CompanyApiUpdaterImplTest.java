package cz.raynet.csvimporter.crm;

import cz.raynet.csvimporter.crm.integration.impl.CompanyApiUpdaterImpl;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyAddressesDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponseDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyAddressDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyDTO;
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

import java.util.List;

import static org.mockito.Mockito.*;
class CompanyApiUpdaterImplTest {
    @Mock
    private ApiRequestSender apiRequestSender;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private APIConfiguration apiConfiguration;

    @InjectMocks
    private CompanyApiUpdaterImpl companyApiUpdater;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(apiConfiguration.getCompanyPath()).thenReturn("/api/companies/");
    }

    @Test
    void testSend() {
        // Arrange
        Company company = new Company();
        APICompanyResponse fetchedCompany = new APICompanyResponse();
        UpdateCompanyDTO updateCompanyDTO = new UpdateCompanyDTO();
        UpdateCompanyAddressDTO updateCompanyAddressDTO = new UpdateCompanyAddressDTO();

        APICompanyResponseDTO responseDTO = new APICompanyResponseDTO();
        responseDTO.setId(1L);
        APICompanyAddressesDTO primaryAddress = new APICompanyAddressesDTO();
        primaryAddress.setId(2L);
        responseDTO.setPrimaryAddress(primaryAddress);
        fetchedCompany.setData(List.of(responseDTO));
        // Act
        when(modelMapper.map(any(Company.class), eq(UpdateCompanyDTO.class))).thenReturn(updateCompanyDTO);
        when(modelMapper.map(any(Company.class), eq(UpdateCompanyAddressDTO.class))).thenReturn(updateCompanyAddressDTO);

        companyApiUpdater.send(company, fetchedCompany);

        // Assert
        verify(apiRequestSender).send(eq(HttpMethod.POST), eq("/api/companies/1"), eq(updateCompanyDTO), eq(void.class));
        verify(apiRequestSender).send(eq(HttpMethod.POST), eq("/api/companies/1/address/2"), eq(updateCompanyAddressDTO), eq(void.class));
    }
}
