package cz.raynet.csvimporter.domain.company.model.dto.apicompany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APICompanyResponseDTO {
    private boolean success;
    private int totalCount;
    private APINewCompanyDTO data;
}
