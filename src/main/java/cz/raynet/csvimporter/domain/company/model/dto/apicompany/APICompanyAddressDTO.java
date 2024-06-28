package cz.raynet.csvimporter.domain.company.model.dto.apicompany;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class APICompanyAddressDTO implements Serializable {
    private String name;
}
