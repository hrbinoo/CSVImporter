package cz.raynet.csvimporter.crm.integration.impl;

import cz.raynet.csvimporter.crm.integration.CompanyApiUpdater;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyAddressDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany.UpdateCompanyDTO;
import cz.raynet.csvimporter.shared.api.ApiRequestSender;
import cz.raynet.csvimporter.shared.configuration.APIConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyApiUpdaterImpl implements CompanyApiUpdater {
    private final ApiRequestSender apiRequestSender;
    private final ModelMapper modelMapper;
    private final APIConfiguration apiConfiguration;


    /**
     * Sends the updated company to the API
     *
     * @param company updated company to be sent
     * @param fetchedCompany fetched company from the API
     * */
    public void send(final Company company,final APICompanyResponse fetchedCompany) {
        final var updateCompanyDTO = modelMapper.map(company, UpdateCompanyDTO.class);
        final var addressDTO = modelMapper.map(company, UpdateCompanyAddressDTO.class);

        log.info("Updating company with ID: {}", fetchedCompany.getData().get(0).getId());
        sendUpdatedCompanyToAPI(updateCompanyDTO, fetchedCompany);
        sendUpdatedCompanyAddressToAPI(addressDTO, fetchedCompany);
    }

    /**
     * Sends the updated company address to the API
     *
     * @param address updated address to be sent
     * @param fetchedCompany fetched company from the API
     * */
    private void sendUpdatedCompanyAddressToAPI(final UpdateCompanyAddressDTO address,final APICompanyResponse fetchedCompany) {
        final long companyId = fetchedCompany.getData().get(0).getId();
        final long addressId = fetchedCompany.getData().get(0).getPrimaryAddress().getId();
        final String companyPath = apiConfiguration.getCompanyPath();
        final String path = String.format("%s%d/address/%d", companyPath,companyId, addressId);

        log.info("Updating company address with ID: {}", addressId);
        apiRequestSender.send(HttpMethod.POST, path, address, void.class);
    }

    /**
     * Sends the updated company to the API
     *
     * @param company updated company to be sent
     * @param fetchedCompany fetched company from the API
     * */
    private void sendUpdatedCompanyToAPI(UpdateCompanyDTO company,APICompanyResponse fetchedCompany) {
        final long companyId = fetchedCompany.getData().get(0).getId();
        final String companyPath = apiConfiguration.getCompanyPath();
        final String path = String.format("%s%d", companyPath,companyId);
        apiRequestSender.send(HttpMethod.POST, path, company, void.class);
    }

}
