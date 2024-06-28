package cz.raynet.csvimporter.crm.integration;

import cz.raynet.csvimporter.domain.company.model.entity.Company;
import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;

public interface CompanyApiUpdater {
    void send(final Company company, final APICompanyResponse fetchedCompany);
}
