package cz.raynet.csvimporter.crm.integration;

import cz.raynet.csvimporter.domain.company.model.entity.Company;

public interface CompanyApiSender {
    void send(final Company company);
}
