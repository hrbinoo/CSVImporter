package cz.raynet.csvimporter.domain.company.model.dto.apifetchcompany;

import cz.raynet.csvimporter.domain.company.model.dto.apicompany.APICompanyAddressesDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class APICompanyResponseDTO implements Serializable {
    private Long id;
    private String name;
    private String regNumber;
    private APICompanyAddressesDTO primaryAddress;

    public String toString() {
        return "APICompanyResponseDTO(id=" + this.getId() + ", name=" + this.getName() + ", regNumber=" + this.getRegNumber() + ", primaryAddress=" + this.getPrimaryAddress() + ")";
    }
}
