package cz.raynet.csvimporter.domain.company.service.impl;

import cz.raynet.csvimporter.crm.integration.CompanyApiFetcher;
import cz.raynet.csvimporter.csv.processing.CSVFileProcessor;
import cz.raynet.csvimporter.domain.company.model.dto.company.CompanyDTO;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.dto.csv.CSVCompanyDTO;
import cz.raynet.csvimporter.domain.company.repository.CompanyRepository;
import cz.raynet.csvimporter.domain.company.service.CRMUpsertService;
import cz.raynet.csvimporter.domain.company.service.CompanyService;
import cz.raynet.csvimporter.shared.mailer.SESMailer;
import cz.raynet.csvimporter.shared.validation.CompanyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyValidator companyValidator;
    private final CompanyRepository companyRepository;
    private final CSVFileProcessor csvFileProcessor;
    private final ModelMapper modelMapper;
    private final CompanyApiFetcher companyApiFetcher;
    private final CRMUpsertService crmUpsertService;
    private final SESMailer sesMailer;

    public Mono<List<CompanyDTO>> companyFileHandler(final MultipartFile file) {
        return processFile(file)
                .flatMapMany(Flux::fromIterable)
                .map(company -> modelMapper.map(company, CompanyDTO.class))
                .collectList();
    }

    /**
     * Processes the file and saves/updates the valid companies to the database.
     *
     * @param file CSV file with companies data
     * @return list of companies
     * */
    public Mono<List<Company>> processFile(final MultipartFile file) {
        log.info("Started processing file: {}", file.getOriginalFilename());
        final List<CSVCompanyDTO> companiesDTO = csvFileProcessor.parseCSVFile(file, CSVCompanyDTO.class);
        final List<Company> companies = companiesDTO.stream()
                .map(companyDTO -> modelMapper.map(companyDTO, Company.class)).toList();

        final Map<Boolean, List<Company>> partitionedCompanies = partitionCompanies(companies);
        final List<Company> invalidCompanies = partitionedCompanies.get(false);
        final List<Company> validCompanies = partitionedCompanies.get(true);
        log.info("Found {} valid companies and {} invalid companies.", validCompanies.size(), invalidCompanies.size());

        return invalidCompanies.isEmpty() ?
               validCompaniesHandler(validCompanies) :
                Mono.fromCallable(() -> invalidCompaniesHandler(invalidCompanies));
    }

    /**
     * Processes the invalid companies and sends failure email after processing.
     *
     * @param invalidCompanies list of invalid companies to be processed
     * @return empty list
     */
    public List<Company> invalidCompaniesHandler(final List<Company> invalidCompanies) {
        log.warn("Sending failure email for {} invalid companies.", invalidCompanies.size());
        sesMailer.sendFailureEmail(invalidCompanies);
        return new ArrayList<>();
    }

    /**
     * Processes the valid companies and saves them to the database and updates them in the CRM
     * after success mail is sent.
     *
     * @param validCompanies list of valid companies to be processed and saved
    * */
    public Mono<List<Company>> validCompaniesHandler(final List<Company> validCompanies) {
        log.info("Processing {} valid companies.", validCompanies.size());

        return saveCompanies(validCompanies)
                .doOnSuccess(companiesList -> {
                    log.info("Sending success email for {} valid companies.", companiesList.size());
                    sesMailer.sendSuccessEmail();
                })
                .flatMap(companiesList -> {
                    log.info("Fetching companies from the CRM based on the registration number.");
                    return Flux.fromIterable(companiesList)
                            .flatMap(company -> companyApiFetcher.getByRegNumber(company.getRegNumber())
                                    .map(apiCompanies -> {
                                        log.info("Fetched {} companies from the CRM based on the registration number: {}", apiCompanies.size(), company.getRegNumber());
                                        crmUpsertService.upsertCompanies(company, apiCompanies);
                                        return company;
                                    }))
                            .collectList();
                });
    }

    /**
     * Method for partitioning companies into two lists: valid and invalid.
     *
     * @param companies list of companies to be partitioned
     * @return map with two lists of companies: valid and invalid
     * */
    private Map<Boolean, List<Company>> partitionCompanies(final List<Company> companies) {
        return companies.stream().collect(
                Collectors.partitioningBy(
                        company -> companyValidator.validateCompany(company.getTitle(), company.getEmail(), company.getPhone())));
    }

    /**
     * Method for saving companies to the database.
     *
     * @param companies list of companies to be saved
     * @return list of saved companies
     * */
    private Mono<List<Company>> saveCompanies(final List<Company> companies) {
        log.info("Saving {} companies to the database.", companies.size());

        return Flux.fromIterable(companies)
                .flatMap(company ->
                        companyRepository.findByRegNumber(Mono.just(company.getRegNumber()))
                                .map(existingCompany -> {
                                    log.debug("Found existing company with ID: {} for registration number: {}", existingCompany.getId(), company.getRegNumber());
                                    company.setId(existingCompany.getId());
                                    return company;
                                })
                                .defaultIfEmpty(company))
                .flatMap(companyRepository::save)
                .collectList()
                .doOnSuccess(companiesList -> log.info("Successfully saved {} companies to the database.", companiesList.size()))
                .doOnError(ex -> log.error("Error while saving companies to the database: {}", ex.getMessage()));
    }

}
