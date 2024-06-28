package cz.raynet.csvimporter.domain.company.model.dto.apicompany;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class APICompanyAddressesDTO implements Serializable {
    private Long id;
    private APICompanyAddressDTO address;
    private APICompanyContactInfoDTO contactInfo;
}
