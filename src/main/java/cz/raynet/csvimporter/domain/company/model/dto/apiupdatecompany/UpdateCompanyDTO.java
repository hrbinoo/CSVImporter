package cz.raynet.csvimporter.domain.company.model.dto.apiupdatecompany;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateCompanyDTO implements Serializable {
    private String name;
}
