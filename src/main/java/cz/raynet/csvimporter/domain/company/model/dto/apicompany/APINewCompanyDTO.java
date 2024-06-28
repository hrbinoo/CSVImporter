package cz.raynet.csvimporter.domain.company.model.dto.apicompany;

import cz.raynet.csvimporter.domain.company.model.enums.Rating;
import cz.raynet.csvimporter.domain.company.model.enums.Role;
import cz.raynet.csvimporter.domain.company.model.enums.State;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class APINewCompanyDTO implements Serializable {
    private String name;
    private Rating rating;
    private String regNumber;
    private State state;
    private Role role;
    private List<APICompanyAddressesDTO> addresses;
}
