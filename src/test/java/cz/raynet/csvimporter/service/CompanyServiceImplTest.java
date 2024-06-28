package cz.raynet.csvimporter.service;

import cz.raynet.csvimporter.crm.integration.CompanyApiFetcher;
import cz.raynet.csvimporter.csv.processing.CSVFileProcessor;
import cz.raynet.csvimporter.domain.company.model.dto.csv.CSVCompanyDTO;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.repository.CompanyRepository;
import cz.raynet.csvimporter.domain.company.service.CRMUpsertService;
import cz.raynet.csvimporter.domain.company.service.impl.CompanyServiceImpl;
import cz.raynet.csvimporter.shared.mailer.SESMailer;
import cz.raynet.csvimporter.shared.validation.CompanyValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CompanyServiceImplTest {

    @Mock
    private CompanyValidator companyValidator;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CSVFileProcessor csvFileProcessor;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CompanyApiFetcher companyApiFetcher;
    @Mock
    private CRMUpsertService crmUpsertService;
    @Mock
    private SESMailer sesMailer;

    @InjectMocks
    private CompanyServiceImpl companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessFile_withValidAndInvalidCompanies() {
        MultipartFile file = mock(MultipartFile.class);
        List<CSVCompanyDTO> csvCompanies = Arrays.asList(new CSVCompanyDTO(), new CSVCompanyDTO());
        List<Company> companies = Arrays.asList(new Company(), new Company());

        when(csvFileProcessor.parseCSVFile(eq(file), eq(CSVCompanyDTO.class))).thenReturn(csvCompanies);
        when(modelMapper.map(any(CSVCompanyDTO.class), eq(Company.class))).thenReturn(companies.get(0), companies.get(1));
        when(companyValidator.validateCompany(anyString(), anyString(), anyString())).thenReturn(true, false);

        Mono<List<Company>> result = companyService.processFile(file);

        StepVerifier.create(result)
                .expectNext(Collections.emptyList())
                .verifyComplete();

        verify(sesMailer).sendFailureEmail(anyList());
    }

    @Test
    void testInvalidCompaniesHandler() {
        List<Company> invalidCompanies = Arrays.asList(new Company(), new Company());

        List<Company> result = companyService.invalidCompaniesHandler(invalidCompanies);

        assertEquals(Collections.emptyList(), result);
        verify(sesMailer).sendFailureEmail(invalidCompanies);
    }

}

