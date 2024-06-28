package cz.raynet.csvimporter.domain.company.service.impl;

import cz.raynet.csvimporter.crm.integration.CompanyApiSender;
import cz.raynet.csvimporter.crm.integration.CompanyApiUpdater;
import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.domain.company.service.CRMUpsertService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CRMUpsertServiceImpl implements CRMUpsertService {
    private final CompanyApiSender companyApiSender;
    private final CompanyApiUpdater companyApiUpdater;

    public void upsertCompanies(final Company company,final List<APICompanyResponse> fetchedCompanies) {
            if (fetchedCompanies.get(0).getData().isEmpty())
                companyApiSender.send(company);
            else
                companyApiUpdater.send(company, fetchedCompanies.get(0));
    }
}
