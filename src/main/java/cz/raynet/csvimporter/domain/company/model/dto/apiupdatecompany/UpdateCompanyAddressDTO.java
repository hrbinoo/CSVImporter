package cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany;

import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyAddressDTO;
import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyContactInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateCompanyAddressDTO implements Serializable {
    private APICompanyAddressDTO address;
    private APICompanyContactInfoDTO contactInfo;
}
