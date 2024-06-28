package cz.raynet.csvimporter.domain.company.service;

import cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany.APICompanyResponse;
import cz.raynet.csvimporter.domain.company.model.entity.Company;

import java.util.List;

public interface CRMUpsertService {
    void upsertCompanies(Company company, List<APICompanyResponse> fetchedCompanies);
}
