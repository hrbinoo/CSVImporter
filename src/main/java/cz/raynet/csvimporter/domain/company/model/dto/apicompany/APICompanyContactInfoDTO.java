package cz.raynet.csvimporter.domain.company.model.dto.apicompany;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class APICompanyContactInfoDTO implements Serializable {
    private String email;
    private String tel1;
}
